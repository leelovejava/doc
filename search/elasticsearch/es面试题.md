# es面试题

### 1、elasticsearch了解多少，说说你们公司es的集群架构，索引数据大小，分片有多少，以及一些调优手段 

考察点:
    公司接触的ES使用场景、规模，有没有做过比较大规模的索引设计、规划、调优

es的理解:
    
    是一个实时分布式搜索和分析引擎
    
    基于luence,提供restful接口
    
    用于全文搜索、结构化搜索、分析
 
使用场景:
    
    elk(日志搜索、检索)
    
    基于ES实现HBase二级索引(快速复杂查询和海量数据存储)
    
    结构化搜索(商品表索引:产品标题和产品描述)   
    
公司集群架构/数据大小/分片:

ES集群架构13个节点，索引根据通道不同共20+索引，根据日期，每日递增20+

索引：10分片，每日递增1亿+数据，

每个通道每天索引大小控制：150GB之内   

调优:

[es 在数据量很大的情况下（数十亿级别）如何提高查询效率啊？](https://doocs.github.io/advanced-java/#/docs/high-concurrency/es-optimizing-query-performance)

设计阶段:

    使用别名进行索引管理
    
    采取冷热分离机制，热数据存储到SSD，提高检索效率；冷数据定期进行shrink操作，以缩减存储 
    
    精心设计字段类型
    
    不要再在一个索引下创建多个type(6.x默认对应doc,5.x type对应table)
    
    仅针对需要分词的字段，合理的设置分词器

写入调优:   

    写入前副本数设置为0 
    
    采取bulk批量写入

查询调优:

    禁用`wildcard`(通配符查询)、`terms`(多值搜索 in查询) ['waɪldkɑrd] [tɜ:mz]
    
    数据量大时候，可以先基于时间敲定索引再检索 
    
    控制返回字段和结果(返回业务相关字段_source)
    
    分页(不允许深度分页,scoll api) 
    scroll 会一次性生成所有数据的一个快照，然后每次滑动向后翻页就是通过游标 scroll_id 移动，获取下一页下一页这样子，性能会比普通分页性能要高很多很多，基本上都是毫秒级的
    
    非实时查询场景,调大`refresh_interval` ['ɪntəv(ə)l]
    
    减少复杂的关联查询,冗余关联字段

其他调优:

    部署调优、业务调优 

业务调优
    
   性能优化的杀手锏——filesystem cache,只存需要检索的字段,其他字段存es,采用es+hbase架构
    
   数据预热
   
   冷热分离
    
### 2、elasticsearch的倒排索引是什么？

考察点:基础概念的认知

通俗解释:

正排索引：文档 Id 到文档内容、单词的关联关系。通过 Id获取到文档的内容

倒排索引:单词到文档 Id 的关联关系。通过单词搜索到文档 Id.

		对数据进行分词,记录该词在数据中出现的次数、位置、文档id

查询流程:

    根据关键字,搜索索引区域,查询对应的docId,通过docId,去数据区域查询数据

倒排索引包括如下信息:
1. 文档ID，用于获取原始文档的信息 
2. 单词频率（TF，Term Frequency），记录该单词在该文档中出现的次数，用于后续相关性算分。 
3. 位置（Position），记录单词在文档中的分词位置（多个），用于做词语搜索。 
4. 偏移（Offset），记录单词在文档的开始和结束位置，用于高亮显示

原理:
底层实现是基于：FST（Finite State Transducer）数据结构

FST有两个优点：

1）空间占用小。通过对词典中单词前缀和后缀的重复利用，压缩了存储空间

2）查询速度快。O(len(str))的查询时间复杂度

对比mysql的索引

mysql:b+tree

### 3.elasticsearch 索引数据多了怎么办，如何调优，部署？

考察点:想了解大数据量的运维能力。

解答：索引数据的规划，应在前期做好规划，正所谓“设计先行，编码在后”，这样才能有效的避免突如其来的数据激增导致集群处理能力不足引发的线上客户检索或者其他业务受到影响

调优:
    动态索引(模板+时间+rollover api滚动)、存储(冷热数据分离存储)、部署(动态新增机器)

### 4、elasticsearch是如何实现master选举的？

[Elasticsearch原理（五）：Master机制及脑裂分析 源码级](https://blog.csdn.net/xiaoyu_bd/article/details/82016395)

考察点:ES集群的底层原理

前置前提：

1）只有候选主节点（master：true）的节点才能成为主节点。

2）最小主节点数（min_master_nodes）的目的是防止脑裂

核心入口为findMaster，选择主节点成功返回对应Master，否则返回null：

流程:

第一步：确认候选主节点数达标

    elasticsearch.yml设置的值`discovery.zen.minimum_master_nodes`

第二步：比较

    先判定是否具备master资格，具备候选主节点资格的优先返回
    
    若两节点都为候选主节点，则id小的值会主节点。注意这里的id为string类型

### 5、详细描述一下Elasticsearch索引文档的过程？
考察点:ES集群的底层原理

[es写数据过程、读数据过程、搜索数据过程](https://doocs.github.io/advanced-java/#/docs/high-concurrency/es-write-query-search)

文档写入包含:单文档写入和批量bulk写入

第一步：客户写集群某节点写入数据，发送请求。（如果没有指定路由/协调节点，请求的节点扮演路由节点的角色。）

第二步：节点1接受到请求后，使用文档_id来确定文档属于分片0。请求会被转到另外的节点，假定节点3。因此分片0的主分片分配到节点3上。

第三步：节点3在主分片上执行写操作，如果成功，则将请求并行转发到节点1和节点2的副本分片上，等待结果返回。所有的副本分片都报告成功，节点3将向协调节点（节点1）报告成功，节点1向请求客户端报告写入成功

第二步中的文档获取分片的过程？

借助路由算法获取，路由算法就是根据路由和文档id计算目标的分片id的过程
> shard = hash(_routing) % (num_of_primary_shards)

### 6、详细描述一下Elasticsearch搜索的过程？
考察点:ES搜索的底层原理

搜索拆解为“query then fetch” 两个阶段。
query阶段的目的：定位到位置，但不取。

步骤拆解如下：

1）假设一个索引数据有5主+1副本 共10分片，一次请求会命中（主或者副本分片中）的一个。
2）每个分片在本地进行查询，结果返回到本地有序的优先队列中。
3）第2）步骤的结果发送到协调节点，协调节点产生一个全局的排序列表。

fetch阶段的目的：取数据。
路由节点获取所有文档，返回给客户端

### 7、Elasticsearch在部署时，对Linux的设置有哪些优化方法？
考察点:ES集群的运维能力。

解答:

1. 关闭缓存swap;
2. 堆内存设置为：Min（节点内存/2, 32GB）;
3. 设置最大文件句柄数；
4. 线程池+队列大小根据业务需要做调整；
5. 磁盘存储raid方式——存储有条件使用RAID10，增加单节点性能以及避免单节点存储故障

### 8、[lucence内部结构是什么？](https://www.jianshu.com/p/0dfcee4637c5)
考察点:知识面的广度和深度

Lucene是有索引和搜索的两个过程，包含索引创建，索引，搜索三个要点

[lucene字典实现原理](http://www.cnblogs.com/LBSer/p/4119841.html)

### 9、[es 的分布式架构原理能说一下么（es 是如何实现分布式的啊）？](https://doocs.github.io/advanced-java/#/docs/high-concurrency/es-architecture)

[从Elasticsearch来看分布式系统架构设计](https://zhuanlan.zhihu.com/p/32990496)

副本机制、master选举

### 10、对于GC方面，在使用Elasticsearch时要注意什么？

倒排词典的索引需要常驻内存，无法GC，需要监控data node上segment memory增长趋势

### 10、terms 和term 的区别 [字节跳动]
在查询的字段只有一个值的时候，应该使用term而不是terms，在查询字段包含多个的时候才使用terms(类似于sql中的in、or)，使用terms语法，JSON中必须包含数组

### 11、filter 和query的区别 [字节跳动]

[吃透 | Elasticsearch filter和query的不同](https://blog.csdn.net/laoyang360/article/details/80468757)
过滤器（filter）通常用于过滤文档的范围，比如某个字段是否属于某个类型，或者是属于哪个时间区间

查询器（query）的使用方法像极了filter，但query更倾向于更准确的查找

query filter在性能上对比：filter是不计算相关性的，同时可以cache。因此，filter速度要快于query

适用于完全精确匹配，范围检索,答案只有是否
以下场景适用于filter过滤检索：

举例1：时间戳timestamp 是否在2015至2016年范围内？

举例2：状态字段status 是否设置为“published”？

所以，选择参考：
1、全文搜索、评分排序，使用query；
2、是非过滤，精确匹配，使用filter

### 12、MySQL 数据同步到es中： pagesize大小设置，数据超过这个pagesize该怎么处理 [字节跳动]

1). 自己造轮子,分为全量、增量(分析binlog、基于更新时间)
    先获取要同步总数,按同步的速率和时间,计算
    
2). Debezium
    步骤1： 基Debezium的binlog机制，将Mysql数据同步到Kafka。
    步骤2： 基于Kafka_connector机制，将kafka数据同步到Elasticsearch
    
### 13、一个数字如何设置mapping，为什么 [字节跳动]

### 14、怎么避免脑裂？

原因:

集群中不同的节点对于master的选择出现了分歧，出现了多个master竞争，导致主分片和副本的识别也发生了分歧，对一些分歧中的分片标识为了坏片

1.网络问题：集群间的网络延迟导致一些节点访问不到master，认为master挂掉了从而选举出新的master，并对master上的分片和副本标红，分配新的主分片

2.节点负载：主节点的角色既为master又为data，访问量较大时可能会导致ES停止响应造成大面积延迟，此时其他节点得不到主节点的响应认为主节点挂掉了，会重新选取主节点。

3.内存回收：data节点上的ES进程占用的内存较大，引发JVM的大规模内存回收，造成ES进程失去响应

解决方案：

1.减少误判：discovery.zen.ping_timeout节点状态的响应时间，默认为3s，可以适当调大，如果master在该响应时间的范围内没有做出响应应答，判断该节点已经挂掉了。调大参数（如6s，discovery.zen.ping_timeout:6），可适当减少误判。

2.选举触发 discovery.zen.minimum_master_nodes:1

该参数是用于控制选举行为发生的最小集群主节点数量。

当备选主节点的个数大于等于该参数的值，且备选主节点中有该参数个节点认为主节点挂了，进行选举。官方建议为（n/2）+1，n为主节点个数（即有资格成为主节点的节点个数）

增大该参数，当该值为2时，我们可以设置master的数量为3，这样，挂掉一台，其他两台都认为主节点挂掉了，才进行主节点选举。

3.角色分离：即master节点与data节点分离，限制角色

主节点配置为：

> node.master: true 
> node.data: false

从节点配置为：

> node.master: false 
> node.data: true

### 15、ES和solr的区别？

https://www.cnblogs.com/jajian/p/9801154.html

lucene是现存功能最强大、最先进搜索库，直接基于lucene开发，api非常复杂大量的java代码、需要深入了解原理

对比处:

只有一个索引库、
传统的遍历搜索方式
采用B+树索引;
Es是基于lucene的，隐藏了lucene复杂部分的一个分布式全文检索框架

对比处:

一个es的集群包含多个索引库、
分布式搜索
Es是采用倒排式索引
es没有事务概念,删除不能恢复
es开源免费
正排索引：id ---> value

倒排索引：value ---> id

### 16、你还了解哪些全文检索工具？
   
   Lucene，Solr，HadoopContrib，Katta

### 17、ES在高并发的情况下如何保证数据线程安全问题？ 
   
   在读数据与写数据之间如果有其他线程进行写操作，就会出问题，es使用版本控制才避免这种问题
       
   在修改数据的时候指定版本号，操作一次版本号加1