## 基于 Flink 的百亿数据去重实践

在工作中经常会遇到去重的场景，例如基于 App 的用户行为日志分析系统，用户的行为日志从手机客户端上报到 Nginx 服务端，通过 Logstash、Flume 或其他工具将日志从 Nginx 写入到 Kafka 中。

由于用户手机客户端的网络可能出现不稳定，所以手机客户端上传日志的策略是：宁可重复上报，也不能丢日志。所以导致 Kafka 中必然会出现日志重复的情况，即：同一条日志出现了 2 条或 2 条以上。

通常情况下，Flink 任务的数据源都是 Kafka，若 Kafka 中数据出现了重复，在实时 ETL 或者流计算时都需要考虑对日志主键进行去重，否则会导致流计算结果偏高或结果不准确的问题，例如用户 a 在某个页面只点击了一次，但由于日志重复上报，所以用户 a 在该页面的点击日志在 Kafka 中出现了 2 次，最后统计该页面的 click 数时，结果就会偏高。

这里只阐述了一种可能造成 Kafka 中数据重复的情况，在生产环境中很多情况都可能造成 Kafka 中数据重复，这里不一一列举，本节主要讲述出现了数据重复后，该如何处理。

### 实现去重的通用解决方案

Kafka 中数据出现重复后，各种解决方案都比较类似，一般需要一个全局 set 集合来维护历史所有数据的主键。当处理新日志时，需要拿到当前日志的主键与历史数据的 set 集合按照规则进行比较，若 set 集合中已经包含了当前日志的主键，说明当前日志在之前已经被处理过了，则当前日志应该被过滤掉，否则认为当前日志不应该被过滤应该被处理，而且处理完成后需要将新日志的主键加入到 set 集合中，set 集合永远存放着所有已经被处理过的数据。程序流程图如下图所示：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103901.jpg)

处理流程很简单，关键在于如何维护这个 set 集合，可以简单估算一下这个 set 集合需要占用多大空间。本小节要解决的问题是百亿数据去重，所以就按照每天 1 百亿的数据量来计算。

由于每天数据量巨大，因此主键占用空间通常会比较大，如果主键占用空间小意味着表示的数据范围就比较小，就可能导致主键冲突，例如：4 个字节的 int 类型表示数据范围是为 -2147483648~2147483647，总共可以表示 42 亿个数，如果这里每天百亿的数据量选用 int 类型做为主键的话，很明显会有大量的主键发生冲突，会将不重复的数据认为是发生了重复。

用户的行为日志是在手机客户端生成的，没有全局发号器，一般会选取 UUID 做为日志的主键，UUID 会生成 36 位的字符串，例如："f106c4a1-4c6f-41c1-9d30-bbb2b271284a"。每个主键占用 36 字节，每天 1 百亿数据，36 字节 * 100亿 ≈ 360GB。这仅仅是一天的数据量，所以该 set 集合要想存储空间不发生持续地爆炸式增长，必须增加一个功能，那就是给所有的主键增加 ttl（Time To Live的缩写，即：过期时间）。

如果不增加 ttl，10 天数据量的主键占用空间就 3.6T，100 天数据量的主键占用空间 36T，所以在设计之初必须考虑为主键设定 ttl。如果要求按天进行去重或者认为日志发生重复上报的时间间隔不可能大于 24 小时，那么为了系统的可靠性 ttl 可以设置为 36 小时。每天数据量 1 百亿，且 set 集合中存放着 36 小时的数据量，即 100 亿 * 1.5 = 150 亿，所以 set 集合中需要维护 150 亿的数据量。

且 set 集合中每条数据都增加了 ttl，意味着 set 集合需要为每条数据再附带保存一个时间戳，来确定该数据什么时候过期。例如 Redis 中为一个 key 设置了 ttl，如果没有为这个 key 附带时间戳，那么根本无法判断该 key 什么时候应该被清理。所以在考虑每条数据占用空间时，不仅要考虑数据本身，还需要考虑是否需要其他附带的存储。主键本身占用 36 字节加上 long 类型的时间戳 8 字节，所以每条数据至少需要占用 44 字节，150 亿 * 44 字节 = 660GB。所以每天百亿的数据量，如果我们使用 set 集合的方案来实现，至少需要占用 660GB 以上的存储空间。

### 使用 BloomFilter 来实现去重

有些流计算的场景对准确性要求并不是很高，例如传统的 Labmda 架构中，都会有离线去矫正实时计算的结果，所以根据业务场景，当业务要求可以接受结果有小量误差时，可以选择使用一些低成本的数据结构。BloomFilter 和 HyperLogLog 都是相对低成本的数据结构，分别有自己的应用场景，且两种数据结构都有一定误差。

HyperLogLog 可以估算出 HyperLogLog 中插入了多少个不重复的元素，而不能告诉我们之前是否插入了哪些元素。BloomFilter 则恰好相反，比起 BloomFilter 更像是一个 set 集合，BloomFilter 可以告诉你 BloomFilter 中**肯定不包含**元素 a，或者告诉你 BloomFilter 中**可能包含**元素 b，但 BloomFilter 不能告诉你 BloomFilter 中插入了多少个元素。接下来了解一下 BloomFilter 的实现原理。

#### bitmap 位图

了解 BloomFilter，从 bitmap（位图）开始说起。现在有 1 千万个整数，数据范围在 0 到 2 千万之间。如何快速查找某个整数是否在这 1 千万个整数中呢？可以将这 1 千万个数保存在 HashMap 中，不考虑对象头及其他空间，1000 万个 int 类型数据需要占用大约 1000 万 * 4 字节 ≈ 40MB 存储空间。有没有其他方案呢？因为数据范围是 0 到 2 千万，所以如下图所示，可以申请一个长度为 2000 万、boolean 类型的数组。将这 1 千万个整数作为数组下标，将其对应的数组值设置成 true，如下图所示，数组下标为 2、666、999 的位置存储的数据为 true，表示 1 千万个数中包含了 2、666、999 等。当查询某个整数 K 是否在这 1 千万个整数中时，只需要将对应的数组值 array[K] 取出来，看是否等于 true。如果等于 true，说明 1 千万整数中包含这个整数 K，否则表示不包含这个整数 K。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103907.jpg)

Java 的 boolean 基本类型占用一个字节（8bit）的内存空间，所以上述方案需要申请 2000 万字节。如下图所示，可以通过编程语言用二进制位来模拟布尔类型，二进制的 1 表示 true、二进制的 0 表示 false。通过二进制模拟布尔类型的方案，只需要申请 2000 万 bit 即可，相比 boolean 类型而言，存储空间占用仅为原来的 1/8。2000 万 bit ≈ 2.4MB，相比存储原始数据的方案 40 MB 而言，占用的存储空间少了很多。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103905.jpg)

假如这 1 千万个整数的数据范围是 0 到 100 亿，那么就需要申请 100 亿个 bit 约等于 1200MB，比存储原始数据方案的 40MB 还要大很多。该情况下，直接使用位图使用的存储空间更多了，怎么解决呢？可以只申请 1 亿 bit 的存储空间，对 1000 万个数求hash，映射到 1 亿的二进制位上，最后大约占用 12 MB 的存储空间，但是可能存在 hash 冲突的情况。例如 3 和 100000003（一亿零三）这两个数对一亿求余都为 3，所以映射到长度为 1 亿的位图上，这两个数会占用同一个 bit，就会导致一个问题：1 千万个整数中包含了一亿零三，所以位图中下标为 3 的位置存储着二进制 1。当查询 1 千万个整数中是否包含数字 3 时，同样也是去位图中下标 3 的位置去查找，发现下标为 3 的位置存储着二进制 1，所以误以为 1 千万个整数中包含数字 3。为了减少 hash 冲突，于是诞生了 BloomFilter。

#### BloomFilter 原理介绍

hash 存在 hash 冲突（碰撞）的问题，两个不同的 key 通过同一个 hash 函数得到的值有可能相同。为了减少冲突，可以多引入几个 hash 函数，如果通过其中的一个 hash 函数发现某元素不在集合中，那么该元素肯定不在集合中。当所有的 hash 函数告诉我们该元素在集合中时，才能确定该元素存在于集合中，这便是BloomFilter的基本思想。

如下图所示，是往 BloomFilter 中插入元素 a、b 的过程，有 3 个 hash 函数，元素 a 经过 3 个 hash 函数后对应的 2、8、10 这三个二进制位，所以将这三个二进制位置为 1，元素 b 经过 3 个 hash 函数后，对应的 5、10、14 这三个二进制位，将这三个二进制位也置为 1，其中下标为 10 的二进制位被 a、b 元素都涉及到。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103858.jpg)

如下图所示，是从 BloomFilter 中查找元素 c、d 的过程，同样包含了 3 个 hash 函数，元素 c 经过 3 个 hash 函数后对应的 2、6、9 这三个二进制位，其中下标 6 和 9 对应的二进制位为 0，所以会认为 BloomFilter 中不存在元素 c。元素 d 经过 3 个 hash 函数后对应的 5、8、14 这三个二进制位，这三个位对应的二进制位都为 1，所以会认为 BloomFilter 中存在元素 d，但其实 BloomFilter 中并不存在元素 d，是因为元素 a 和元素 b 也对应到了 5、8、14 这三个二进制位上，所以 BloomFilter 会有误判。但是从实现原理来看，当 BloomFilter 告诉你不包含元素 c 时，BloomFilter 中**肯定不包含**元素 c，当 BloomFilter 告诉你 BloomFilter 中包含元素 d 时，它只是**可能包含**，也有可能不包含。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103906.jpg)

#### 使用 BloomFilter 实现数据去重

Redis 4.0 之后 BloomFilter 以插件的形式加入到 Redis 中，关于 api 的具体使用这里不多赘述。BloomFilter 在创建时支持设定一个预期容量和误判率，预期容量即预计插入的数据量，误判率即：当 BloomFilter 中插入的数据达到预期容量时，误判的概率，如果 BloomFilter 中插入数据较少的话，误判率会更低。

经笔者测试，申请一个预期容量为 10 亿，误判率为千分之一的 BloomFilter，BloomFilter 会申请约 143 亿个 bit，即：14G左右，相比之前 660G 的存储空间小太多了。但是在使用过程中，需要记录 BloomFilter 中插入元素的个数，当插入元素个数达到 10 亿时，为了保障误差率，可以将当前 BloomFilter 清除，重新申请一个新的 BloomFilter。

通过使用 Redis 的 BloomFilter，我们可以通过相对较小的内存实现百亿数据的去重，但是 BloomFilter 有误差，所以只能使用在那些对结果能承受一定误差的应用场景，对于广告计费等对数据精度要求非常高的场景，极力推荐大家使用精准去重的方案来实现。

### 使用 HBase 维护全局 set 实现去重

通过之前分析，我们知道要想实现百亿数据量的精准去重，需要维护 150 亿数据量的 set 集合，每条数据占用 44 KB，总共需要 660 GB 的存储空间。注意这里说的是存储空间而不是内存空间，为什么呢？因为 660G 的内存实在是太贵了，660G 的 Redis 云服务一个月至少要 2 万 RMB 以上，俗话说设计架构不考虑成本等于耍流氓。这里使用 Redis 确实可以解决问题，但是成本较高。HBase 基于 rowkey Get 的效率比较高，所以这里可以考虑将这个大的 set 集合以 HBase rowkey 的形式存放到 HBase 中。HBase 表设置 ttl 为 36 小时，最近 36 小时的 150 亿条日志的主键都存放到 HBase 中，每来一条数据，先拿到主键去 HBase 中查询，如果 HBase 表中存在该主键，说明当前日志已经被处理过了，当前日志应该被过滤。如果 HBase 表中不存在该主键，说明当前日志之前没有被处理过，此时应该被处理，且处理完成后将当前主键 Put 到 HBase 表中。由于数据量比较大，所以一定要提前对 HBase 表进行预分区，将压力分散到各个 RegionServer 上。

#### 使用 HBase rowkey 去重带来的问题

一天 100 亿的数据量，平均一秒 11.57 万条日志。但是数据一般都会有高峰期，例如外卖软件高峰期肯定是饭前的一两个小时，其余时间段数据量相对比较少。所以虽然每天 100 亿数据量，但是到了数据高峰期每秒数据量可以达到 20 万左右。按照之前的思路，每条数据来了都会对 HBase 进行一次 Get 操作，当前数据处理完还会对 HBase 进行一次 Put 操作，所以每秒需要对 HBase 请求 40 万次。单个的 Get 和 Put 请求效率比较低，这里可以优化为批量操作的 API 或异步 API 来提高访问 HBase 的效率。

性能问题优化后，再分析这里使用 HBase 去重到底能不能保证 Exactly Once？拿计算 PV 的案例来讲。

假如 PV 信息维护在 Flink 的状态中，通过幂等性将 PV 统计结果写入到 Redis 供其他业务方查询实时统计的 PV 值。如下图所示，Flink 处理完日志 b 后进行 Checkpoint，将 PV = 2 和 Kafka 对应的 offset 信息保存起来，此时 HBase 表中有两条 rowkey 分别是 a、b，表示主键为 a 和 b 的日志已经被处理过了。

接着往后处理，当处理完日志 d 以后，PV = 4，HBase 表中有 4 条 rowkey 分别是 a、b、c、d，表示主键为 a、b、c、d 的日志已经被处理过了。但此时机器突然故障，导致 Flink 任务挂掉，如右图所示 Flink 任务会从最近一次成功的 Checkpoint 处恢复任务，从日志 b 之后的位置开始消费，且 PV 恢复为 2，因为处理完日志 b 时 PV 为 2。

但由于 HBase 中的数据不是由 Flink 来维护，所以无法恢复到 Checkpoint 时的状态。所以 Flink 任务恢复后，PV = 2 且 HBase 中 rowkey 为 a、b、c、d。此时 Flink 任务从日志 c 开始继续处理数据，当处理日志 c 和 d 时，Flink 任务会先查询 HBase，发现 HBase 中已经保存了主键 c 和 d，所以认为日志 c 和 d 已经被处理了，会将日志 c 和 d 过滤掉，于是就产生了丢数据的现象，日志 c 和 d 其实并没有参与 PV 的计算。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103859.jpg)

同学们可能会想，日志 c 和 d 已经被处理过了，此时就算从 Checkpoint 处恢复，PV 值也应该为 4，不应该是 2。请注意上述方案，笔者描述的是 PV 信息维护在 Flink 的状态中，所以从 Checkpoint 处恢复任务时，会将 Checkpoint 时状态中保存的 PV 信息恢复，所以恢复为 2。

当然还有其他统计 PV 的方式，不需要将 PV 信息维护在 Flink 状态中，而是仅仅在 Redis 中保存 PV 结果，每处理一条数据，将 Redis 中的 PV 值加一即可。如下图所示，PV 不维护在状态中，所以当处理完日志 b 进行 Checkpoint 时，只会将当前消费的 offset 信息维护起来。处理完日志 d 以后，由于机器故障，Flink 任务挂掉，任务依然会从日志 b 之后开始消费，此时 Redis 中保存的 PV=4，且 HBase 中保存的 rowkey 信息为 a、b、c、d。紧接着开始处理 c 和 d，因为 HBase 中保存了主键 c、d，因此不会重复处理日志 c、d，因此 PV 值计算正确，也不会出现重复消费的问题。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103903.jpg)

这种策略貌似没有问题，但是问题百出。我们的任务处理元素 d 需要两个操作：

① 将 Redis 中 PV 值加一 ② 将主键 id 加入到 HBase

由于 Redis 和 HBase 都不支持事务，所以以上两个操作并不能保障原子性。如果代码中先执行步骤 ①，可能会造成 ① 执行成功 ② 还未执行成功，那么恢复任务时 PV=4，HBase 中保存主键 a、b、c，此时日志 d 就会重复计算，就会造成 PV 值计算偏高的问题。如果代码中先执行步骤 ②，可能会造成 ② 执行成功 ① 还未执行成功，那么恢复任务时 PV=3，HBase 中保存主键 a、b、c、d，此时日志 d 就会被漏计算，就会造成 PV 值计算偏低的问题。这里只是拿 HBase 举例而已，上述情况中外部的任何存储介质维护 set 集合都不能保证 Exactly Once，因为 Flink 从 Checkpoint 处恢复时，外部存储介质并不能恢复到 Checkpoint 时的状态。既然外部存储介质不能恢复到 Checkpoint 时的状态，那使用 Flink 内置的状态后端可以吗？当然可以！！！

### 使用 Flink 的 KeyedState 实现去重

#### 使用 Flink 状态来维护 set 集合的优势

Flink 的状态太强大了，可以使用状态 api 将我们要维护的 set 集合保存到 Flink 的状态中，当任务从 Checkpoint 处恢复时，就可以拿到 Checkpoint 时的状态快照信息。如下图所示，可以将主键信息维护在 Flink 的状态中，当处理完日志 b 时，将 PV=2 和状态中的主键信息：a、b 一块保存到状态后端。无论后续什么情况发生，只要从 chk-1 对应的 Checkpoint 处恢复，那么会将 PV=2 和状态中的主键信息：a、b 做为一个整体来恢复。所以就可以保障 Exactly Once 了。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103902.jpg)

Flink 内置了三种状态后端，分别是 MemoryStateBackend、FsStateBackend 和 RocksDBStateBackend，其中 MemoryStateBackend、FsStateBackend 都会将状态信息存储在 TaskManager 的内存中，RocksDBStateBackend 将状态信息存储在 TaskManager 本地的 RocksDB 数据库中，实际使用的是内存加磁盘的方式。set 集合较大，且集合中的数据都要维护在状态后端，所以这里只能选择内存加磁盘的 RocksDBStateBackend。

简单介绍一下 RocksDB 的背景，LevelDB 是 Google 开源的 NoSQL 存储引擎库，在 LevelDB 的基础之上，Facebook 开发了另一个 NoSQL 存储引擎库 RocksDB，沿用了 LevelDB 先进技术架构的同时还解决了 LevelDB 的一些短板。RocksDB 是一个可嵌入的 KV 数据库，RocksDB 与 HBase 类似都是基于 LSM 树实现的。久经考验的 RocksDB 和 HBase 都吸取了对方的优点，所以 RocksDB 的性能并不比 HBase 差。

使用 RocksDBStateBackend 方案比使用 HBase 方案的优势仅仅是能保证 Exactly Once 吗？当然不是，使用 RocksDBStateBackend 有一个非常大的优势是 RocksDB 位于 TaskManager 本地的机器上，RocksDB 将状态中的数据保存在 TaskManager 机器的内存和磁盘，而 HBase 的 RegionServer 分布在集群中各个节点。当使用 HBase 维护 set 集合时，每次 Get、Put 请求都需要通过 RPC 请求 HBase，需要用到网络传输。

而位于 TaskManager 本地机器的 RocksDB 优势很明显了，RocksDB 并不比 HBase 性能差，而且每次请求都是本地操作减少了网络传输，所以使用起来性能当然不会差。所以 Flink 引入状态后端是 Flink 被广泛使用的一个重要原因，引入状态后端使得 Flink 任务大多数情况下在处理数据环节可以不依赖第三方存储，依赖第三方存储仅仅是为了提供数据给外部系统查询，当然 Flink 的状态也支持外部系统查询。

#### 如何使用 KeyedState 维护 set 集合

Flink 有两种 State 分别是 OperatorState 和 KeyedState，OperatorState 是一个 Operator 实例对应一个State，KeyedState 是每个 key 对应一个 State。在百亿去重中，主键相同的日志可能分布在不同的 Operator 实例，为了保证全局去重，相同的日志主键应该访问同一个 State，所以这里不能选用 OperatorState 必须选用 KeyedState。

DataStream 类型数据集 keyBy 后生成 KeyedStream 类型数据集，Flink 的 KeyedState 作用于 KeyedStream 类型数据集对应的 Function 和 Operator 之上，每个 key 对应一个 State。要按照日志的主键进行去重，所以按照日志的主键进行 keyBy，每个日志主键会对应一个 State。KeyedState 支持的数据结构有 ValueState、MapState、ListState、ReducingState 和 AggregatingState。RocksDB 是一个 KV 数据库，将日志的主键 id 当做 key 存放到数据库，那 value 存什么信息呢？这里类似于 Java 中 Map 和 Set 的关系，Map 中存储 KV 格式的数据且会按照 key 进行去重，Set 中按照元素进行去重，可以把 Map 当做 Set 来用，将要存储在 Set 中的数据当做 key 存放在 Map 中也能起到去重的作用。

在百亿去重案例中，只要 RocksDB 中存在当前 key 就认为当前日志被处理过了，不存在当前 key 就认为当前日志还没有被处理，此时 RocksDB 中 key 对应的 value 并没有意义，可以随意设置，但是要尽量节省存储空间，所以这里选用 ValueState 即可。ValueState 中还需要存储数据，依然为了节省存储空间可以选取 Boolean 类型，所以最后使用的 Boolean 类型的 ValueState。当处理一条日志时，根据日志的主键 id 从 ValueState 中 get 数据，如果不为 null 就认为当前处理的日志在之前已经被处理过了，此时应该被过滤；如果为 null 就认为当前日志在之前还没有被处理过，此时应该被处理，并且需要 update 一个值到 ValueState 中，来标识当前日志被处理过了。具体实现代码如下所示：

```java
public class KeyedStateDeduplication {

    public static void main(String[] args) throws Exception{

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(6);

        // 使用 RocksDBStateBackend 做为状态后端，并开启增量 Checkpoint
        RocksDBStateBackend rocksDBStateBackend = new RocksDBStateBackend(
                "hdfs:///flink/checkpoints", true);
        rocksDBStateBackend.setNumberOfTransferingThreads(3);
        // 设置为机械硬盘+内存模式，强烈建议为 RocksDB 配备 SSD
        rocksDBStateBackend.setPredefinedOptions(
                PredefinedOptions.SPINNING_DISK_OPTIMIZED_HIGH_MEM);
        rocksDBStateBackend.enableTtlCompactionFilter();
        env.setStateBackend(rocksDBStateBackend);

        // Checkpoint 间隔为 10 分钟
        env.enableCheckpointing(TimeUnit.MINUTES.toMillis(10));
        // 配置 Checkpoint
        CheckpointConfig checkpointConf = env.getCheckpointConfig();
        checkpointConf.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        checkpointConf.setMinPauseBetweenCheckpoints(TimeUnit.MINUTES.toMillis(8));
        checkpointConf.setCheckpointTimeout(TimeUnit.MINUTES.toMillis(20));
        checkpointConf.enableExternalizedCheckpoints(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // Kafka Consumer 配置
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker_list);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "keyed-state-deduplication");
        FlinkKafkaConsumerBase<String> kafkaConsumer = new FlinkKafkaConsumer011<>(
                DeduplicationExampleUtil.topic, new SimpleStringSchema(), props)
                .setStartFromGroupOffsets();

        env.addSource(kafkaConsumer)
            .map(log -> GsonUtil.fromJson(log, UserVisitWebEvent.class))  // 反序列化 JSON
            .keyBy((KeySelector<UserVisitWebEvent, String>) UserVisitWebEvent::getId)
            .addSink(new KeyedStateSink());

        env.execute("KeyedStateDeduplication");
    }

    // 用来维护实现百亿去重逻辑的算子
    public static class KeyedStateSink extends RichSinkFunction<UserVisitWebEvent> {
        // 使用该 ValueState 来标识当前 Key 是否之前存在过
        private ValueState<Boolean> isExist;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            ValueStateDescriptor<Boolean> keyedStateDuplicated =
                    new ValueStateDescriptor<>("KeyedStateDeduplication",
                            TypeInformation.of(new TypeHint<Boolean>() {}));
            // 状态 TTL 相关配置，过期时间设定为 36 小时
            StateTtlConfig ttlConfig = StateTtlConfig
                    .newBuilder(Time.hours(36))
                    .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                    .setStateVisibility(
                            StateTtlConfig.StateVisibility.NeverReturnExpired)
                    .cleanupInRocksdbCompactFilter(50000000L)
                    .build();
            // 开启 TTL
            keyedStateDuplicated.enableTimeToLive(ttlConfig);
            // 从状态后端恢复状态
            isExist = getRuntimeContext().getState(keyedStateDuplicated);
        }

        @Override
        public void invoke(UserVisitWebEvent value, Context context) throws Exception {
            // 当前 key 第一次出现时，isExist.value() 会返回 null
            // key 第一次出现，说明当前 key 在之前没有被处理过，
            // 此时应该执行正常处理代码的逻辑，并给状态 isExist 赋值，标识当前 key 已经处理过了，
            // 下次再有相同的主键 时，isExist.value() 就不会为 null 了
            if ( null == isExist.value()) {
                // ... 这里执行代码处理的逻辑
                // 执行完处理逻辑后，更新状态值
                isExist.update(true);
            } else {
                // 如果 isExist.value() 不为 null，表示当前 key 在之前已经被处理过了，
                // 所以当前数据应该被过滤
            }
        }
    }
}
```

上述代码是百亿数据去重的简单实现，可以通过设置 Job 的并行度来提高吞吐量，上述代码中关于状态 TTL 的相关配置请参考 4.1.7 节 State TTL（存活时间）。笔者使用的机械硬盘对上述代码进行性能测试，TaskManager 分配 3 G 内存，每个 TaskManager 分配 3 个 slot，从多个维度测试分析，得到的测试结果如下所示。

**磁盘空间占用信息**

当状态中 key 的个数为 24 亿时，占用的磁盘空间为 91.7 GB，相当于每个 key 占用 38 字节，与 12.2.1 节中计算的单个 key 占用 44 字节比较接近，这里应该是 RocksDB 在存储数据时做了一些优化，例如相邻的多个 key 前缀如果相同可以只保存一份。

**处理吞吐量**

测试代码中没有业务处理的代码，代码中的 isExist.value() 表示从 RocksDB 中获取数据，isExist.update(true) 表示将数据更新到 RocksDB 中，仅仅测试 RocksDB 做为状态后端时，获取和更新数据的效率。使用机械硬盘的情况下，单并行度的状态中包含 4 亿条数据时，单并行度的 TPS 为每秒处理 5000 条日志，相当于单并行度存储 20 多 G 数据时，RocksDB 每秒可以获取并更新 5000 条数据。如果存储的状态值比较少，吞吐量相对会提升。

RocksDB 是基于 LSM Tree 实现的，从 LSM Tree 的实现原理来看，写数据都是先缓存到内存中，所以 RocksDB 的写请求效率比较高。RocksDB 使用内存结合磁盘的方式来存储数据，每次获取数据时，先从内存中 blockcache 中查找，如果内存中没有再去磁盘中查询，所以这里单并行度 TPS 5000 record/s，性能瓶颈主要在于 RocksDB 对磁盘的读请求，所以当处理性能不够时，仅需要横向扩展并行度即可提高整个 Job 的吞吐量。

#### 优化主键来减少状态大小，且提高吞吐量

从之前分析中，我们发现了日志的主键 id 是通过 UUID 生成的 36 位字符串占用 36 字节，在不影响去重精度的前提下是否可以通过某种方式缩短主键呢？从而使得状态大小变小。这里可以借鉴一下 hash 思想，将主键 id 通过 hash 算法映射为一个 int 类型，但是不同的主键 id 通过 hash 算法得到的 hash 值可能是相同的，这就是 hash 冲突。

假设将每天百亿数据量的主键通过 hash 算法转换为 long 类型，然后我们可以把 long 类型的数据当做主键来存储，那么 hash 冲突的概率高吗？int 类型是 32 位，表示的数据范围是 42 亿左右，long 类型是 64 位，表示的数据范围是 42 亿 * 42 亿，一个很大很大的数据范围。

所以笔者在这里猜测，将 100 亿个不同的 UUID 通过 hash 算法转换为 long 类型以后，冲突的概率不高，因为 long 类型表述的范围实在是太大了。所以笔者做了一个测试，清洗出 200 亿左右不重复的日志主键 id，通过 MurmurHash3 算法将主键 id 转换为 long 类型，然后看 long 类型重复的个数。测试过程如下所示：

\1. 清洗出测试要使用的日志主键 id 到 test.tmp*event*id 表中：

```sql
  select * from test.tmp_event_id limit 6;
  +--------------------------------------+
  | event_id                             |
  +--------------------------------------+
| 65f37a7f-f938-44a9-b660-0004963e0163 |
  | 74d5c030-a4a1-4e28-8721-dc2d6b1de0dd |
  | 9bda7924-f093-42f4-9962-08b28d29b66d |
  | 286c1593-dc7f-415e-8df3-b952517d5ffc |
  | 7b5e9189-20f2-4764-8b68-54e834b84a72 |
  | 3e174384-4613-4191-8793-a08a75830117 |
  +--------------------------------------+
```

\2. 查询表中的数据量及去重后的数据量：

```sql
   select count(*)
         ,count(distinct event_id)
      from test.tmp_event_id;
   +-------------+--------------------------+
   | count(*)    | count(distinct event_id) |
   +-------------+--------------------------+
   | 20062345973 | 20062345973              |
   +-------------+--------------------------+
```

从上述查询结果来看，表中数据量为 20062345973，对主键 event*id 去重后的数量同样也是 20062345973，表示测试表中的 event*id 并没有重复。

\3. 将表中主键 id 通过 MurmurHash3 转换为 long 类型以后，查看 long 类型数据重复的数据量：

```sql
   select count(*) 
     from 
       (
           select func.murmur_hash(event_id) as murmur
             from test.tmp_event_id 
           group by murmur 
           having count(*) > 1
       ) a 
   ;
   +----------+                                                                                               
   | count(*) |
   +----------+
   | 11       |
   +----------+
```

首先将主键 id 通过 MurmurHash3 转化为 long 类型的 murmur，按照生成的 long 类型 murmur 进行 group by，统计每个 long 数据出现几次，大于 1 次表示 long 类型数据发生了重复，仅留下那些出现次数大于 1 次的数据，可以看出仅有 11 条数据发生了重复。意味着，在 200 亿数据量的前提下，如果将 200 亿个主键 id 转换为 long 类型，仅有 11 条数据会重复。long 类型仅占 8 个字节，比 36 个字节的字符串要节省 28 个字节的存储空间。在生产环境中，大多数业务 200 亿数据丢 11 条数据是可以接受的，所以完全可以使用 long 类型来代替主键 id。

上述流程验证了使用 long 类型代替主键 id 是可以行得通的，那代码如何实现呢？

```java
env.addSource(kafkaConsumer)
    .map(string -> GsonUtil.fromJson(string, UserVisitWebEvent.class))  // 反序列化 JSON
    // 这里将日志的主键 id 通过 murmur3_128 hash 后将生成 long 类型数据当做 key
    .keyBy((KeySelector<UserVisitWebEvent, Long>) log -> 
            Hashing.murmur3_128(5).hashUnencodedChars(log.getId()).asLong())
    .addSink(new KeyedStateDeduplication.KeyedStateSink());
```

核心代码只改动了一行，就是 keyBy 时不是把日志的主键 id 当做 key，而是将日志的主键 id 通过 murmur3_128 hash 后，将生成 long 类型数据当做 key。通过该操作使得 long 类型数据完全替换掉原来的主键 id，从而节省了 RocksDB 中的存储空间。虽然 hash 方案都会存在 hash 冲突，但是 200 亿数据仅仅冲突了 11 条数据，我们是可以接受的。做了此优化后，笔者在同样的硬件资源下进行了性能测试，测试数据如下所示。

**磁盘空间占用信息**

当状态中 key 的个数为 34.65 亿时，占用的磁盘空间为 55.8 GB，相当于每个 key 占用 16.1 字节。优化之前每个 key 平均占用 38 字节，从磁盘占用空间来讲优化了 1 倍以上。

**处理吞吐量**

依然是使用机械硬盘的情况下，单并行度的状态中包含 6 亿条数据时，单并行度的 TPS 为每秒处理 9000 条日志。优化之前单并行度状态中包含 4 亿条数据时，单并行度的 TPS 为每秒处理 5000 条日志，从吞吐量来讲性能也优化了将近 1 倍。这里吞吐量提升主要在于磁盘中数据量减少，所以 RocksDB 的查找效率得到了提升。

通过使用 long 类型数据来替换日志主键的方案，使得无论是存储空间还是处理吞吐量方面，性能都提升了 1 倍，而且丢失的数据量相比 200 亿数据来讲，基本可以忽略，所以极力推荐大家使用该方案来优化。

### 大状态情况下，使用 RocksDBStateBackend 的优化点

在使用上述方案的过程中，可能会出现吞吐量时高时低，或者吞吐量比笔者的测试性能要低一些。可以尝试从以下几点来分析性能瓶颈。

#### 设置本地 RocksDB 的数据目录

RocksDB 使用内存加磁盘的方式存储数据，当状态比较大时，磁盘占用空间会比较大。类似于上述案例中，如果对 RocksDB 有频繁的读取请求，那么磁盘 IO 会成为 Flink 任务瓶颈。

强烈建议在 `flink-conf.yaml` 中配置 `state.backend.rocksdb.localdir` 参数来指定 RocksDB 在磁盘中的存储目录。当一个 TaskManager 包含 3 个 slot 时，那么单个服务器上的三个并行度都对磁盘造成频繁读写，从而导致三个并行度的之间相互争抢同一个磁盘 IO，这样务必导致三个并行度的吞吐量都会下降。

庆幸的是 Flink 的 state.backend.rocksdb.localdir 参数可以指定多个目录，一般大数据所使用的服务器都会挂载很多块硬盘，我们期望三个并行度使用不同的硬盘从而减少资源竞争。具体参数配置如下所示：

```yaml
state:
  backend:
    rocksdb:
      localdir: /data1/flink/rocksdb,/data2/flink/rocksdb,/data3/flink/rocksdb,/data4/flink/rocksdb,/data5/flink/rocksdb,/data6/flink/rocksdb,/data7/flink/rocksdb,/data8/flink/rocksdb,/data9/flink/rocksdb,/data10/flink/rocksdb,/data11/flink/rocksdb,/data12/flink/rocksdb
```

注意：务必将目录配置到多块不同的磁盘上，不要配置单块磁盘的多个目录，这里配置多个目录是为了让多块磁盘来分担压力。如下所示是笔者测试过程中磁盘的 IO 使用率，可以看出三个大状态算子的并行度分别对应了三块磁盘，这三块磁盘的 IO 平均使用率都保持在 45% 左右，IO 最高使用率几乎都是 100%，而其他磁盘的 IO 平均使用率相对低很多。由此可见使用 RocksDB 做为状态后端且有大状态的频繁读取时，对磁盘 IO 性能消耗确实比较大。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103904.jpg)

上述属于理想情况，当设置多个 RocksDB 本地磁盘目录时，Flink 会随机选择要使用的目录，所以就可能存在三个并行度共用同一目录的情况。如下图所示，其中两个并行度共用了 sdb 磁盘，一个并行度使用 sdj 磁盘。可以看到 sdb 磁盘的 IO 使用率已经达到了 91.6%，就会导致 sdb 磁盘对应的两个并行度吞吐量大大降低，从而使得整个 Flink 任务吞吐量降低。如果服务器磁盘数较多，一般不会出现该情况，但是如果任务重启后吞吐量较低，可以检查是否发生了多个并行度共用同一块磁盘的情况。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-03-103900.jpg)

如果每个服务器上有一两块 SSD，强烈建议将 RocksDB 的本地磁盘目录配置到 SSD 的目录下，从 HDD 改为 SSD 对于性能的提升可能比你配置 10 个优化参数更有效。

#### Checkpoint 参数相关配置

Checkpoint 时会将 RocksDB 内存中 MemTable 的数据 flush 到磁盘中生成 sst 文件，并且将这部分新生成的 sst 文件上传到 hdfs 来保障高可用，如果状态过大，该过程可能比较耗时，强烈建议开启增量 Checkpoint 来减少数据的读取频率，关于增量 Checkpoint 实现原理请参考 4.3.3 节 Checkpoint 流程。对于大状态的任务由于每次 Checkpoint 时需要读写的文件较多，因此强烈建议将 Checkpoint 的周期调大，例如 10 分钟触发一次 Checkpoint。并且调大两次 Checkpoint 之间的暂停之间，例如设置两次 Checkpoint 之间至少暂停 8 分钟。

如果 Checkpoint 语义配置为 EXACTLY_ONCE，那么在 Checkpoint 过程中还会存在 barrier 对齐的过程，可以通过 Flink Web UI 的 Checkpoint 选项卡来查看 Checkpoint 过程中各阶段的耗时情况，从而确定到底是哪个阶段导致 Checkpoint 时间过长然后针对性的解决问题。

#### RocksDB 参数相关配置

对于大状态场景，这里给出一些应该去调节的参数。

|                       参数                        |                        含义及设置建议                        |
| :-----------------------------------------------: | :----------------------------------------------------------: |
|      state.backend.rocksdb.block.cache-size       | 整个 RocksDB 共享一个 block cache，读数据时内存的 cache 大小，该参数越大读数据时缓存命中率越高，强烈建议调大该参数，例如：512M |
|         state.backend.rocksdb.thread.num          |  用于后台 flush 和合并 sst 文件的线程数，默认为 1，建议调大  |
|      state.backend.rocksdb.writebuffer.size       | RocksDB 中，每个 State 使用一个 Column Family，每个 Column Family 使用独占的 write buffer，建议调大，例如：32M |
|      state.backend.rocksdb.writebuffer.count      |          每个 Column Family 对应的 writebuffer 数目          |
| state.backend.rocksdb.writebuffer.number-to-merge | 将数据从 writebuffer 中 flush 到磁盘时，需要合并的 writebuffer 数量 |
|           state.backend.local-recovery            | 设置本地恢复，当 Flink 任务失败时，可以基于本地的状态信息进行恢复任务，可能不需要从 hdfs 拉取数据 |

### 小结与反思

本节讲述了如何基于 Flink 来实现百亿数据去重，首先讲述了实现去重的通用解决方案，再讲述了如何 bitmap 和 BloomFilter 的实现原理及如何使用 BloomFilter 实现去重。由于 BloomFilter 有误差，而且需要定期重建，所以为了保证精准去重引出了 HBase rowkey 的方案，并讲述了通过外部存储系统维护 set 集合存在的问题。下一小节中讲述了通过 Flink 的 KeyedState 结合 RocksDBStateBackend 的方案来更优雅的实现百亿数据去重，并在保证去重精度的前提下给出了一种优化主键存储空间的方案。最后介绍了大状态情况下，从多个维度给出了使用 RocksDBStateBackend 的一些优化点。请问还有其他更高效的百亿去重方案吗？或者在本节去重方案的基础上，还能做哪些优化呢？