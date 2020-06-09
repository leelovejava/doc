## 如何处理 Flink Job BackPressure （反压）问题？

反压（BackPressure）机制被广泛应用到实时流处理系统中，流处理系统需要能优雅地处理反压问题。反压通常产生于这样的场景：短时间的负载高峰导致系统接收数据的速率远高于它处理数据的速率。许多日常问题都会导致反压，例如，垃圾回收停顿可能会导致流入的数据快速堆积，或遇到大促、秒杀活动导致流量陡增。反压如果不能得到正确的处理，可能会导致资源耗尽甚至系统崩溃。反压机制是指系统能够自己检测到被阻塞的 Operator，然后自适应地降低源头或上游数据的发送速率，从而维持整个系统的稳定。

Flink 任务一般运行在多个节点上，数据从上游算子发送到下游算子需要网络传输，若系统在反压时想要降低数据源头或上游算子数据的发送速率，那么肯定也需要网络传输。所以下面先来了解一下 Flink 的网络流控（Flink 对网络数据流量的控制）机制。

### Flink 流处理为什么需要网络流控

下图是一个简单的 Flink 流任务执行图：任务首先从 Kafka 中读取数据、通过 map 算子对数据进行转换、keyBy 按照指定 key 对数据进行分区（key 相同的数据经过 keyBy 后分到同一个 subtask 实例中），keyBy 后对数据进行 map 转换，然后使用 Sink 将数据输出到外部存储。

![简单的Flink流任务执行图.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-07-152946.jpg)

众所周知，在大数据处理中，无论是批处理还是流处理，单点处理的性能总是有限的，我们的单个 Job 一般会运行在多个节点上，通过多个节点共同配合来提升整个系统的处理性能。图中，任务被切分成 4 个可独立执行的 subtask 分别是 A0、A1、B0、B1，在数据处理过程中就会存在 shuffle。例如，subtask A0 处理完的数据经过 keyBy 后被发送到 subtask B0、B1 所在节点去处理。那么问题来了，subtask A0 应该以多快的速度向 subtask B0、B1 发送数据呢？把上述问题抽象化，如下图所示，将 subtask A0 当作 Producer，subtask B0 当做 Consumer，上游 Producer 向下游 Consumer 发送数据，在发送端和接收端有相应的 Send Buffer 和 Receive Buffer，但是上游 Producer 生产数据的速率比下游 Consumer 消费数据的速率大，Producer 生产数据的速率为 2MB/s， Consumer 消费数据速率为 1MB/s，Receive Buffer 容量只有 5MB，所以过了 5 秒后，接收端的 Receive Buffer 满了。

![网络流控存在的问题.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-004946.png)

下游消费速率慢，且接收区的 Receive Buffer 有限，如果上游一直有源源不断的数据，那么将会面临着以下两种情况：

1. 下游消费者的缓冲区放不下数据，导致下游消费者会丢弃新到达的数据。
2. 为了不丢弃数据，所以下游消费者的 Receive Buffer 持续扩张，最后耗尽消费者的内存，导致 OOM 程序挂掉。

常识告诉我们，这两种情况在生产环境下都是不能接受的，第一种会丢数据、第二种会把应用程序挂掉。所以，该问题的解决方案不应该是下游 Receive Buffer 一直累积数据，而是上游 Producer 发现下游 Consumer 消费比较慢的时候，应该在 Producer 端做出限流的策略，防止在下游 Consumer 端无限制地堆积数据。那上游 Producer 端该如何做限流呢？可以采用下图所示静态限流的策略：

![网络流控-静态限速.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005000.png)

静态限速的思想就是，提前已知下游 Consumer 端的消费速率，然后在上游 Producer 端使用类似令牌桶的思想，限制 Producer 端生产数据的速率，从而控制上游 Producer 端向下游 Consumer 端发送数据的速率。但是静态限速会存在问题：

1. 通常无法事先预估下游 Consumer 端能承受的最大速率。
2. 就算通过某种方式预估出下游 Consumer 端能承受的最大速率，在运行过程中也可能会因为网络抖动、CPU 共享竞争、内存紧张、IO阻塞等原因造成下游 Consumer 的吞吐量降低，但是上游 Producer 的吞吐量正常，然后又会出现之前所说的下游接收区的 Receive Buffer 有限，上游一直有源源不断的数据发送到下游的问题，还是会造成下游要么丢数据，要么为了不丢数据 buffer 不断扩充导致下游 OOM 的问题。

综上所述，我们发现了，上游 Producer 端必须有一个限流的策略，且静态限流是不可靠的，于是就需要一个动态限流的策略。可以采用下图所示的动态反馈策略：

![网络流控-动态限速.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005009.png)

下游 Consumer 端会频繁地向上游 Producer 端进行动态反馈，告诉 Producer 下游 Consumer 的负载能力，从而使 Producer 端可以动态调整向下游 Consumer 发送数据的速率，以实现 Producer 端的动态限流。当 Consumer 端处理较慢时，Consumer 将负载反馈到 Producer 端，Producer 端会根据反馈适当降低 Producer 自身从上游或者 Source 端读数据的速率来降低向下游 Consumer 发送数据的速率。当 Consumer 处理负载能力提升后，又及时向 Producer 端反馈，Producer 会通过提升自身从上游或 Source 端读数据的速率来提升向下游发送数据的速率，通过动态反馈的策略来动态调整系统整体的吞吐量。

读到这里，应该知道 Flink 为什么需要网络流控机制了，并且知道 Flink 的网络流控机制必须是一个动态反馈的策略。但是还有以下几个问题：

1. Flink 中数据具体是怎么从上游 Producer 端发送到下游 Consumer 端的？
2. Flink 的动态限流具体是怎么实现的？下游的负载能力和压力是如何传递给上游的？

带着这两个问题，学习下面的 Flink 网络流控与反压机制。

### Flink 1.5 之前网络流控机制介绍

在 Flink 1.5 之前，Flink 没有使用任何复杂的机制来解决反压问题，因为根本不需要那样的方案！Flink 利用自身作为纯数据流引擎的优势来优雅地响应反压问题。下面我们会深入分析 Flink 是如何在 Task 之间传输数据的，以及数据流如何实现自然降速的。

如下图所示，Job 分为 Task A、B、C，Task A 是 Source Task、Task B 处理转换数据、Task C 是 Sink Task。

- Task A 从外部 Source 端读取到数据后将数据序列化放到 Send Buffer 中，再由 Task A 的 Send Buffer 发送到 Task B 的 Receive Buffer；
- Task B 的算子从 Task B 的 Receive Buffer 中将数据反序列后进行处理，将处理后数据序列化放到 Task B 的 Send Buffer 中，再由 Task B 的 Send Buffer 发送到 Task C 的 Receive Buffer；
- Task C 再从 Task C 的 Receive Buffer 中将数据反序列后输出到外部 Sink 端，这就是所有数据的传输和处理流程。

![简单的3个Task数据传输示意图.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005021.png)

Flink 中，动态反馈策略原理比较简单，假如 Task C 由于各种原因吞吐量急剧降低，那么肯定会造成 Task C 的 Receive Buffer 中堆积大量数据，此时 Task B 还在给 Task C 发送数据，但是毕竟内存是有限的，持续一段时间后 Task C 的 Receive Buffer 满了，此时 Task B 发现 Task C 的 Receive Buffer 满了后，就不会再往 Task C 发送数据了，Task B 处理完的数据就开始往 Task B 的 Send Buffer 积压，一段时间后 Task B 的 Send Buffer 也满了，Task B 的处理就会被阻塞，这时 Task A 还在往 Task B 的 Receive Buffer 发送数据。

同样的道理，Task B 的 Receive Buffer 很快满了，导致 Task A 不再往 Task B 发送数据，Task A 的 Send Buffer 也会被用完，Task A 是 Source Task 没有上游，所以 Task A 直接降低从外部 Source 端读取数据的速率甚至完全停止读取数据。

通过以上原理，Flink 将下游的压力传递给上游。

如果下游 Task C 的负载能力恢复后，如何将负载提升的信息反馈给上游呢？

实际上 Task B 会一直向 Task C 发送探测信号，检测 Task C 的 Receive Buffer 是否有足够的空间，当 Task C 的负载能力恢复后，Task C 会优先消费 Task C Receive Buffer 中的数据，Task C Receive Buffer 中有足够的空间时，Task B 会从 Send Buffer 继续发送数据到 Task C 的 Receive Buffer，Task B 的 Send Buffer 有足够空间后，Task B 又开始正常处理数据，很快 Task B 的 Receive Buffer 中也会有足够空间，同理，Task A 会从 Send Buffer 继续发送数据到 Task B 的 Receive Buffer，Task A 的 Receive Buffer 有足够空间后，Task A 就可以从外部的 Source 端开始正常读取数据了。

通过以上原理，Flink 将下游负载过低的消息传递给上游。所以说 Flink 利用自身纯数据流引擎的优势优雅地响应反压问题，并没有任何复杂的机制来解决反压。上述流程，就是 Flink 动态限流（反压机制）的简单描述，可以看到 Flink 的反压是从下游往上游传播的，一直往上传播到 Source Task 后，Source Task 最终会降低或提升从外部 Source 端读取数据的速率。

如下图所示，对于一个 Flink 任务，动态反馈要考虑如下两种情况：

\1. 跨 Task，动态反馈具体如何从下游 Task 的 Receive Buffer 反馈给上游 Task 的 Send Buffer。

- 当下游 Task C 的 Receive Buffer 满了，如何告诉上游 Task B 应该降低数据发送速率；
- 当下游 Task C 的 Receive Buffer 空了，如何告诉上游 Task B 应该提升数据发送速率。

> 注：这里又分了两种情况，Task B 和 Task C 可能在同一个 TaskManager 上运行，也有可能不在同一个 TaskManager 上运行。
>
> 1. Task B 和 Task C 在同一个 TaskManager 运行指的是：一个 TaskManager 包含了多个 Slot，Task B 和 Task C 都运行在这个 TaskManager 上。此时 Task B 给 Task C 发送数据实际上是同一个 JVM 内的数据发送，所以**不存在网络通信**。
> 2. Task B 和 Task C 不在同一个 TaskManager 运行指的是：Task B 和 Task C 运行在不同的 TaskManager 中。此时 Task B 给 Task C 发送数据是跨节点的，所以**会存在网络通信**。

\2. Task 内，动态反馈如何从内部的 Send Buffer 反馈给内部的 Receive Buffer。

- 当 Task B 的 Send Buffer 满了，如何告诉 Task B 内部的 Receive Buffer，自身的 Send Buffer 已经满了？要让 Task B 的 Receive Buffer 感受到压力，才能把下游的压力传递到 Task A。
- 当 Task B 的 Send Buffer 空了，如何告诉 Task B 内部的 Receive Buffer 下游 Send Buffer 空了，并把下游负载很低的消息传递给 Task A。

![简单的3个Task反压图示.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-07-153007.jpg)

到目前为止，动态反馈的具体细节抽象成了三个问题：

- 跨 Task 且 Task 不在同一个 TaskManager 内，动态反馈具体如何从下游 Task 的 Receive Buffer 反馈给上游 Task 的 Send Buffer；
- 跨 Task 且 Task 在同一个 TaskManager 内，动态反馈具体如何从下游 Task 的 Receive Buffer 反馈给上游 Task 的 Send Buffer；
- Task 内，动态反馈具体如何从 Task 内部的 Send Buffer 反馈给内部的 Receive Buffer。

#### TaskManager 之间网络传输相关组件

如下图所示，是 TaskManager 之间数据传输流向，可以看到：

- Source Task 给 Task B 发送数据，Source Task 做为 Producer，Task B 做为 Consumer，Producer 端产生的数据最后通过网络发送给 Consumer 端。
- Producer 端 Operator 实例对一条条的数据进行处理，处理完的数据首先缓存到 ResultPartition 内的 ResultSubPartition 中。
- ResultSubPartition 中一个 Buffer 写满或者超时后，就会触发将 ResultSubPartition 中的数据拷贝到 Producer 端 Netty 的 Buffer 中，之后又把数据拷贝到 Socket 的 Send Buffer 中，这里有一个从用户态拷贝到内核态的过程，最后通过 Socket 发送网络请求，把 Send Buffer 中的数据发送到 Consumer 端的 Receive Buffer。
- 数据到达 Consumer 端后，再依次从 Socket 的 Receive Buffer 拷贝到 Netty 的 Buffer，再拷贝到 Consumer Operator InputGate 内的 InputChannel 中，最后 Consumer Operator 就可以读到数据进行处理了。

这就是两个 TaskManager 之间的数据传输过程，我们可以看到发送方和接收方各有三层的 Buffer。当 Task B 往下游发送数据时，整个流程与 Source Task 给 Task B 发送数据的流程类似。

![TaskManager 之间数据传输流向.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005036.png)

根据上述流程，下表中对 Flink 通信相关的一些术语进行介绍：

| 概念/术语          | 解释                                                         |
| :----------------- | :----------------------------------------------------------- |
| ResultPartition    | 生产者生产的数据首先写入到 ResultPartition 中，一个 Operator 实例对应一个ResultPartition。 |
| ResultSubpartition | 一个 ResultPartition 是由多个 ResultSubpartition 组成。当 Producer Operator 实例生产的数据要发送给下游 Consumer Operator n 个实例时，那么该 Producer Operator 实例对应的 ResultPartition 中就包含 n 个 ResultSubpartition。 |
| InputGate          | 消费者消费的数据来自于 InputGate 中，一个 Operator 实例对应一个InputGate。网络中传输的数据会写入到 Task 的 InputGate。 |
| InputChannel       | 一个 InputGate 是由多个 InputChannel 组成。当 Consumer Operator 实例读取的数据来自于上游 Producer Operator n 个实例时，那么该 Consumer Operator 实例对应的 InputGate 中就包含 n 个 InputChannel。 |
| RecordReader       | 用于将记录从Buffer中读出。                                   |
| RecordWriter       | 用于将记录写入Buffer。                                       |
| LocalBufferPool    | 为 ResultPartition 或 InputGate 分配内存，每一个 ResultPartition 或 InputGate分别对应一个 LocalBufferPool。 |
| NetworkBufferPool  | 为 LocalBufferPool 分配内存，NetworkBufferPool 是 Task 之间共享的，每个 TaskManager 只会实例化一个。 |

InputGate 和 ResultPartition 的内存是如何申请的呢？如下图所示，了解一下 Flink 网络传输相关的内存管理。

- 在 TaskManager 初始化时，Flink 会在 NetworkBufferPool 中生成一定数量的内存块 MemorySegment，内存块的总数量就代表了网络传输中所有可用的内存。
- NetworkBufferPool 是 Task 之间共享的，每个 TaskManager 只会实例化一个。
- Task 线程启动时，会为 Task 的 InputChannel 和 ResultSubPartition 分别创建一个 LocalBufferPool。InputGate 或 ResultPartition 需要写入数据时，会向相对应的 LocalBufferPool 申请内存（图中①），当 LocalBufferPool 没有足够的内存且还没到达 LocalBufferPool 设置的上限时，就会向 NetworkBufferPool 申请内存（图中②），并将内存分配给相应的 InputChannel 或 ResultSubPartition（图③④）。
- 虽然可以申请，但是必须明白内存申请肯定是有限制的，不可能无限制的申请，我们在启动任务时可以指定该任务最多可能申请多大的内存空间用于 NetworkBufferPool。
- 当 InputChannel 的内存块被 Operator 读取消费掉或 ResultSubPartition 的内存块已经被写入到了 Netty 中，那么 InputChannel 和 ResultSubPartition 中的内存块就可以还给 LocalBufferPool 了（图中⑤），如果 LocalBufferPool 中有较多空闲的内存块，就会还给 NetworkBufferPool （图中⑥）。

![Flink 网络传输相关的内存管理.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005044.png)

了解了 Flink 网络传输相关的内存管理，我们来分析 3 种动态反馈的具体细节。

#### 跨 Task 且 Task 不在同一个 TaskManager 内时，反压如何向上游传播

如下图所示，Producer 端生产数据速率为 2MB/s，Consumer 消费数据速率为 1MB/s。持续下去，下游消费较慢，Buffer 容量又是有限的，那 Flink 反压是怎么做的？

![跨TaskManager反压如何向上游传播.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005052.png)

数据从 Task A 的 ResultSubPartition 按照上面的流程最后传输到 Task B 的 InputChannel 供 Task B 读取并计算。持续一段时间后，由于 Task B 消费比较慢，导致 InputChannel 被占满了，所以 InputChannel 向 LocalBufferPool 申请新的 Buffer 空间，LocalBufferPool 分配给 InputChannel 一些 Buffer。

再持续一段时间后，InputChannel 重复向 LocalBufferPool 申请 Buffer 空间，导致 LocalBufferPool 内的 Buffer 空间被用完了，所以 LocalBufferPool 向 NetWorkBufferPool 申请 Buffer 空间，NetWorkBufferPool 给 LocalBufferPool 分配 Buffer。再持续下去，NetWorkBufferPool 也用完了，或者说 NetWorkBufferPool 不能把自己的 Buffer 全分配给 Task B 对应的 LocalBufferPool，因为 TaskManager 上一般会运行了多个 Task，每个 Task 只能使用 NetWorkBufferPool 中的一部分。

此时可以认为 Task B 把自己可以使用的 LocalBufferPool 和 NetWorkBufferPool 都用完了。此时 Netty 还想把数据写入到 InputChannel，但是发现 InputChannel 满了，所以 Socket 层会把 Netty 的 autoRead disable，Netty 不会再从 Socket 中去读消息。由于 Netty 不从 Socket 的 Receive Buffer 读数据了，所以很快 Socket 的 Receive Buffer 就会变满，TCP 的 Socket 通信有动态反馈的流控机制，会把下游容量为 0 的消息反馈给上游发送端，所以上游的 Socket 就不会往下游再发送数据。

可以看到下图中 Consumer 端多个通道显示 ❌，表示该通道所能提供的内存已经被申请完，数据已经不能往下游写了，发生了阻塞。

![跨TaskManager反压如何向上游传播-下游全部阻塞.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005101.png)

此时 Task A 持续生产数据，发送端 Socket 的 Send Buffer 很快被打满，所以 Task A 端的 Netty 也会停止往 Socket 写数据。数据会在 Netty 的 Buffer 中缓存数据，Netty 的 Buffer 是无界的，可以设置 Netty 的高水位，即：设置一个 Netty 中 Buffer 的上限。

所以每次 ResultSubPartition 向 Netty 中写数据时，都会检测 Netty 是否已经到达高水位，如果达到高水位就不会再往 Netty 中写数据，防止 Netty 的 Buffer 无限制的增长。接下来，数据会在 Task A 的 ResultSubPartition 中累积，ResultSubPartition 数据写满后，会向 LocalBufferPool 申请新的 Buffer 空间，LocalBufferPool 分配给 ResultSubPartition 一些 Buffer。

持续下去 LocalBufferPool 也会用完，LocalBufferPool 再向 NetWorkBufferPool 申请 Buffer。NetWorkBufferPool 也会被用完，或者说 NetWorkBufferPool 不能把自己的 Buffer 全分配给 Task A 对应的 LocalBufferPool，因为 TaskManager 上一般会运行了多个 Task，每个 Task 只能使用 NetWork BufferPool 中的一部分。此时，Task A 已经申请不到任何的 Buffer 了，Task A 的 Record Writer 输出就被 wait，Task A 不再生产数据。如下图所示，Producer 和 Consumer 端所有的通道都被阻塞。

![跨TaskManager反压如何向上游传播-上下游全部阻塞.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005119.png)

当下游 Task B 持续消费，Task B 的 InputChannel 中部分的 Buffer 可以被回收，所有被阻塞的数据通道会被一个个打开，之后 Task A 又可以开始正常的生产数据了。通过上述的整个流程，来动态反馈，保障各个 Buffer 都不会因为数据太多导致内存溢出。

#### 跨 Task 且 Task 在同一个 TaskManager 内，反压如何向上游传播

一般情况下，一个 TaskManager 内会运行多个 slot，每个 slot 内运行一个 SubTask。所以，Task 之间的数据传输可能存在上游的 Task A 和下游的 Task B 运行在同一个 TaskManager 的情况，整个数据传输流程与上述类似，只不过由于 Task A 和 B 运行在同一个 JVM，所以不需要网络传输的环节，Task A 会将 Buffer 直接交给 Task B，一旦 Task B 消费了该 Buffer，则该 Buffer 就会被 Task A ResultSubPartition 对应的 LocalBufferPool 回收。

如果 Task B 消费的速度一直比 Task A 生产的速度慢，持续下去就会导致 Task A 申请不到 LocalBufferPool，最终造成 Task A 生产数据被阻塞。当下游 Task B 消费速度恢复后，Task A 就可以回收 ResultSubPartition 对应的已经被 Task B 消费的 Buffer，Task A 又可以正常的开始生产数据了，通过上述流程，来实现跨 Task 且 Task 在同一个 TaskManager 内的动态反馈。

#### Task 内部，反压如何向上游传播

假如 Task A 的下游所有 Buffer 都占满了，那么 Task A 的 Record Writer 会被 block，Task A 的 Record Reader、Operator、Record Writer 都属于同一个线程，Task A 的 Record Reader 也会被 block。

这里分为两种情况，假如 Task A 是 Source Task，那么 Task A 就不会从外部的 Source 端读取数据，假如 Task A 还有上游的 Task，那么 Task A 就不会从自身的 InputChannel 中读取数据，然后又通过第一种动态反馈策略，将 Task A 的压力反馈给 Task A 的上游 Task。

当 Task A 的下游消费恢复后，ResultSubPartition 就可以申请到 Buffer，Task A 的 Record Writer 就不会被 block，Task A 就可以恢复正常的消费。通过上述流程，来实现 Task 内部的动态反馈。

通过以上三种情况的分析，得出的结论：**Flink 1.5 之前并没有特殊的机制来处理反压，因为 Flink 中的数据传输相当于已经提供了应对反压的机制。**

### 基于 Credit 的反压机制

#### 1.5 之前反压机制存在的问题

看似完美的反压机制，其实是有问题的。

如下图所示，我们的任务有 4 个 SubTask，SubTask A 是 SubTask B的上游，即 SubTask A 给 SubTask B 发送数据。Job 运行在两个 TaskManager中，TaskManager 1 运行着 SubTask A1 和 SubTask A2，TaskManager 2 运行着 SubTask B1 和 SubTask B2。假如 SubTask B2 遇到瓶颈、处理速率有所下降，上游源源不断地生产数据，最后导致 SubTask A2 与 SubTask B2 产生反压。虽然此时 SubTask B1 没有压力，但是发现在 SubTask A1 和 A2 中都积压了很多 SubTask B1 的数据。本来只是 SubTask B2 遇到瓶颈了，但是也影响到 SubTask B1 的正常处理，为什么呢？

![Flink 1.5之前反压机制存在的问题.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005131.png)

这里需要明确一点：不同 Job 之间的每个（远程）网络连接将在 Flink 的网络堆栈中获得自己的TCP通道。但是，如果同一 Task 的不同 SubTask 被安排到同一个 TaskManager，则它们与其他 TaskManager 的网络连接将被多路复用并共享一个TCP信道以减少资源使用。图中的 SubTask A1 和 A2 是 Task A 不同并行度的实例，且安排到同一个 TaskManager 内部，所以 SubTask A1 和 A2 与其他 TaskManager 进行网络数据传输时共享同一个 TCP 信道。同理，SubTask B1 和 B2 与其他 TaskManager 进行网络数据传输时也共享同一个 TCP 信道。所以，图中所示的 A1 → B1、A1 → B2、A2 → B1、A2 → B2 这四个网络连接将会多路复用共享一个 TCP 信道。

从上面跨 TaskManager 的反压流程，我们知道现在 SubTask B1 没有压力，根据跨 TaskManager 之间的动态反馈（反压）原理，当 SubTask A2 与 SubTask B2 产生反压时，会把 TaskManager1 端任务对应 Socket 的 Send Buffer 和 TaskManager2 端该任务对应 Socket 的 Receive Buffer 占满，也就是说多路复用的 TCP 通道被完全阻塞了或者整个 TCP 通道的传输速率大大降低了，导致 SubTask A1 和 SubTask A2 发送给 SubTask B1 的数据被阻塞了，使得本来没有压力的 SubTask B1 现在也接收不到数据了。所以，Flink 1.5 之前的反压机制会存在当一个 SubTask 出现反压时，可能导致其他正常的 SubTask 也接收不到数据。

#### 基于 Credit 的反压机制原理

为了解决上述所描述的问题，Flink 1.5 之后的版本，引入了基于 Credit 的反压机制。如下图所示，反压机制直接作用于 Flink 的应用层，即在 ResultSubPartition 和 InputChannel 这一层引入了反压机制。基于 Credit 的流量控制可确保发送端已经发送的任何数据，接收端都具有足够的 Buffer 来接收。上游 SubTask 给下游 SubTask 发送数据时，会把 Buffer 中要发送的数据和上游 ResultSubPartition 堆积的数据量 Backlog size 发给下游，下游接收到上游发来的 Backlog size 后，会向上游反馈现在的 Credit 值，Credit 值表示目前下游可以接收上游的 Buffer 量，1 个Buffer 等价于 1 个 Credit。上游接收到下游反馈的 Credit 值后，上游下次最多只会发送 Credit 个数据到下游，保障不会有数据积压在 Socket 这一层。

Flink 1.5 之前一个 Operator 实例对应一个InputGate，每个 InputGate 的多个 InputChannel 共用一个 LocalBufferPool。Flink 1.5 之后每个 Operator 实例的每个远程输入通道(Remote InputChannel)现在都有自己的一组独占缓冲区(Exclusive Buffer)，而不是只有一个共享的 LocalBufferPool。与之前不同，LocalBufferPool 的缓冲区称为流动缓冲区(Floating buffers)，每个 Operator 对应一个 Floating buffers，Floating buffers 内的 buffer 会在 InputChannel 间流动并且可用于每个 InputChannel。

![Credit 反压机制原理.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005137.png)

如上图所示，上游 SubTask A2 发送完数据后，还有 4 个 Buffer 被积压，会把要发送的 Buffer 数据和 Backlog size = 4 一块发送给下游 SubTask B2，下游接受到数据后，知道上游积压了 4 个Buffer 的数据，于是向 Buffer Pool 申请 Buffer，申请完成后由于容量有限，下游 InputChannel 目前仅有 2 个 Buffer 空间，所以，SubTask B2 会向上游 SubTask A2 反馈 Channel Credit = 2，上游就知道了下游目前最多只能承载 2 个 Buffer 的数据。

所以下一次上游给上游发送数据时，最多只给下游发送 2 个 Buffer 的数据。当下游 SubTask 反压比较严重时，可能就会向上游反馈 Channel Credit = 0，此时上游就知道下游目前对应的 InputChannel 没有可用空间了，所以就不向下游发送数据了。

此时，上游还会定期向下游发送探测信号，检测下游返回的 Credit 是否大于 0，当下游返回的 Credit 大于 0 表示下游有可用的 Buffer 空间，上游就可以开始向下游发送数据了。

通过这种基于 Credit 的反馈策略，就可以保证每次上游发送的数据都是下游 InputChannel 可以承受的数据量，所以在公用的 TCP 这一层就不会产生数据堆积而影响其他 SubTask 通信。基于 Credit 的反压机制还带来了一个优势：由于我们在发送方和接收方之间缓存较少的数据，可能会更早地将反压反馈给上游，缓冲更多数据只是把数据缓冲在内存中，并没有提高处理性能。

### Flink 如何定位产生反压的位置？

#### 反压监控原理介绍

Flink 的反压太过于天然了，导致无法简单地通过监控 BufferPool 的使用情况来判断反压状态。Flink 通过对运行中的任务进行采样来确定其反压，如果一个 Task 因为反压导致处理速度降低了，那么它肯定会卡在向 LocalBufferPool 申请内存块上。那么该 Task 的 stack trace 应该是这样：

```
java.lang.Object.wait(Native Method)
o.a.f.[...].LocalBufferPool.requestBuffer(LocalBufferPool.java:163)
o.a.f.[...].LocalBufferPool.requestBufferBlocking(LocalBufferPool.java:133) <--- BLOCKING request
[...]
```

Flink 的反压监控就是依赖上述原理，通过不断对每个 Task 的 stack trace 采样来进行反压监控。由于反压监控对正常的任务运行有一定影响，因此只有当 Web 页面切换到 Job 的 BackPressure 页面时，JobManager 才会对该 Job 触发反压监控。

默认情况下，JobManager 会触发 100 次 stack trace 采样，每次间隔 50ms 来确定反压。Web 界面看到的比率表示在内部方法调用中有多少 stack trace 被卡在 LocalBufferPool.requestBufferBlocking()，例如：0.01 表示在 100 个采样中只有 1 个被卡在 LocalBufferPool.requestBufferBlocking()。采样得到的比例与反压状态的对应关系如下：

- OK：0 <= 比例 <= 0.10
- LOW：0.10 < 比例 <= 0.5
- HIGH：0.5 < 比例 <= 1

Task 的状态为 OK 表示没有反压，HIGH 表示这个 Task 被反压。

#### 利用 Flink Web UI 定位产生反压的位置

如下图所示，表示 Flink Web UI 中 BackPressure 选项卡，可以查看任务中 subtask 的反压状态。

![back_pressure_sampling_ok.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-07-153013.jpg)

![back_pressure_sampling_high.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005150.png)

如果看到一个 Task 发生反压警告（例如：High），意味着它生产数据的速率比下游 Task 消费数据的速率要快。在工作流中数据记录是从上游向下游流动的（例如：从 Source 到 Sink）。反压沿着相反的方向传播，沿着数据流向上游传播。以一个简单的 Source -> Sink Job 为例。如果看到 Source 发生了警告，意味着 Sink 消费数据的速率比 Source 生产数据的速率要慢，Sink 的吞吐量降低了，Sink 正在向上游的 Source 算子产生反压。应该找 Sink 出了什么问题导致反压，而不是找 Source 出了什么问题。

假如一个 Job 由 Task A、B、C 组成，数据流向是 A → B → C，当看到 Task A、B 的反压状态为 HIGH，Task C 的反压状态为 OK 时，实际上是 C 的吞吐量较低导致的，为什么呢？从实现原理来讲，当 Task C 吞吐量较低时，Task C 会产生反压且 InputChannel 所有可以申请的 Buffer 已经占满了，Task C 会给上游 Task B 返回 Credit = 0，导致 Task B 的数据发送不到 Task C，Task C 此时不需要申请 Buffer 空间，所以 Task C 的 stack trace 不会卡在 LocalBufferPool.requestBufferBlocking()，Task C 此时在处理那些 InputChannel 中待处理的数据。再来分析 Task B，Task B 此时正在处理数据，需要将处理完的数据输出到 ResultSubPartition，但此时 ResultSubPartition 在 LocalBufferPool 申请不到空闲的 Buffer 空间，所以 Task B 会卡在 LocalBufferPool.requestBufferBlocking() 这一步等待申请 Buffer 空间。同理可得，当 Task C 反压比较严重时，Task B 上游的 Task A 也会卡在 LocalBufferPool.requestBufferBlocking()。得出结论：当 Flink 的某个 Task 出现故障导致吞吐量严重下降时，在 Flink 的反压页面，我们会看到该 Task 的反压状态为 OK，而该 Task 上游所有 Task 的反压状态为 HIGH。所以，我们根据 Flink 的 BackPressure 页面去定位哪个 Task 出故障时，首先要找到反压状态为 HIGH 的最后一个 Task，该 Task 紧跟的下一个 Task 就是我们要找的有故障的 Task。

如下图所示是一个 Job 的执行计划图，任务被切分为三个 Operator 分别是 Source、FlatMap、Sink。当看到 Source 和 FlatMap 的 BackPressure 页面都显示 HIGH，Sink 的 BackPressure 页面显示 OK 时，意思是任务产生了反压，且反压的根源是 Sink，也就是说 Sink 算子目前遇到了性能瓶颈吞吐量较低，下一步就应该定位什么原因导致 Sink 算子吞吐量降低。

![单任务流执行计划.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-07-153012.jpg)

当集群中网络 IO 遇到瓶颈时也可能会导致 Job 产生反压，假设有两个 Task A 和 B，Task A 是 Task B 的上游，若 Task B 的吞吐量很高，但是由于网络瓶颈，造成 Task A 的数据不能快速的发送给 Task B，所以导致上游 Task A 被反压了。此时在反压监控页面也会看到 Task A 的反压状态为 HIGH、Task B 的反压状态为 OK，但实际上并不是 Task B 遇到了瓶颈。像这种网络遇到瓶颈的情况应该比较少见，但大家要清楚可能会出现，如果发现 Task B 没有任何瓶颈时，要注意查看是不是网络瓶颈导致。

如下图所示是一个多 Sink 任务流的执行计划，任务被切分为四个 Operator 分别是 Source、FlatMap、HBase Sink 和 Redis Sink。当看到 Source 和 FlatMap 的 BackPressure 页面都显示 HIGH，HBase Sink 和 Redis Sink 的 BackPressure 页面显示 OK 时，意思是任务产生了反压，且反压的根源是 Sink，但此时无法判断到底是 HBase Sink 还是 Redis Sink 出现了故障。这种情况，该如何来定位反压的来源呢？来学习我们下一部分利用 Flink Metrics 定位产生反压的位置。

![多 Sink 任务流执行计划.png](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-12-005200.png)

#### 利用 Flink Metrics 定位产生反压的位置

当某个 Task 吞吐量下降时，基于 Credit 的反压机制，上游不会给该 Task 发送数据，所以该 Task 不会频繁卡在向 Buffer Pool 去申请 Buffer。反压监控实现原理就是监控 Task 是否卡在申请 buffer 这一步，所以遇到瓶颈的 Task 对应的反压页面必然会显示 OK，即表示没有受到反压。如果该 Task 吞吐量下降，造成该 Task 上游的 Task 出现反压时，必然会存在：该 Task 对应的 InputChannel 变满，已经申请不到可用的 Buffer 空间。

如果该 Task 的 InputChannel 还能申请到可用 Buffer，那么上游就可以给该 Task 发送数据，上游 Task 也就不会被反压了，所以说遇到瓶颈且导致上游 Task 受到反压的 Task 对应的 InputChannel 必然是满的（这里不考虑网络遇到瓶颈的情况）。

从这个思路出发，可以对该 Task 的 InputChannel 的使用情况进行监控，如果 InputChannel 使用率 100%，那么该 Task 就是我们要找的反压源。Flink Metrics 提供了 inputExclusiveBuffersUsage、 inputFloatingBuffersUsage、inPoolUsage 等参数可以帮助我们来监控 InputChannel 的 Buffer 使用情况。

| 参数                       | 解释                                                         |
| :------------------------- | :----------------------------------------------------------- |
| inputFloatingBuffersUsage  | 每个 Operator 实例对应一个 FloatingBuffers，inputFloatingBuffersUsage 表示 Operator 对应的 FloatingBuffers 使用率 |
| inputExclusiveBuffersUsage | 每个 Operator 实例的每个远程输入通道(Remote InputChannel)都有自己的一组独占缓冲区(ExclusiveBuffer)，inputExclusiveBuffersUsage 表示 ExclusiveBuffer 的使用率 |
| inPoolUsage                | Flink 1.5 - 1.8 中的 inPoolUsage 表示 inputFloatingBuffersUsage。Flink 1.9 及以上版本 inPoolUsage 表示 inputFloatingBuffersUsage 和 inputExclusiveBuffersUsage 的总和 |

Flink 输入 BufferPool 相关的 Metrics 还有 inputQueueLength 指标，类似于 inPoolUsage，但是 inputQueueLength 表示的是 Buffer 使用的个数，而 inPoolUsage 表示的使用率。有时候我们看到 buffer 使用的个数并不知道其是否压力大，因为我们没有拿到 buffer 的总数量，所以使用率会更直观，强烈建议使用 inPoolUsage。

上图案例中，若无法判断是 Redis Sink 还是 HBase Sink 吞吐量降低导致 Job 反压，只需要在 Flink Web UI 的 Metrics 页面分别查看两个 Task 的 inputExclusiveBuffersUsage、 inputFloatingBuffersUsage、inPoolUsage 参数，我们就可以定位到反压源。

定位反压源的流程首先通过 Flink Web UI 来定位，如果定位不到再通过 Metrics 来辅助我们精确定位。但上述几个 Metrics 参数仅适用于网络传输的情况，当任务执行过程中不存在数据网络传输时，就不存在 InputChannel 变满的情况，此时也无法通过 Metrics 来定位反压源，可以凭借开发者的经验或者改动代码、删掉可能是瓶颈的算子然后发布看处理性能是否提升来定位反压源。

### 定位到反压来源后，该如何处理？

假设确定了反压源（瓶颈）的位置，下一步就是分析为什么会发生这种情况。下面，列出了从最基本到比较复杂的一些反压潜在原因。建议先检查基本原因，然后再深入研究更复杂的原因，最后找出导致瓶颈的原因。

还请记住，反压可能是暂时的，可能是由于负载高峰、CheckPoint 或作业重启引起的数据积压而导致反压。如果反压是暂时的，应该忽略它。另外，请记住，断断续续的反压会影响我们分析和解决问题。话虽如此，以下几点需要检查。

#### 系统资源

首先，应该检查涉及服务器基本资源的使用情况，如 CPU、网络或磁盘 I/O。如果某些资源被充分利用或大量使用，则可以执行以下操作之一：

1. 尝试优化代码。代码分析器在这种情况下很有用。
2. 针对特定的资源调优 Flink。
3. 通过增加并行度或增加群集中的服务器数量来横向扩展。
4. 减少瓶颈算子上游的并行度，从而减少瓶颈算子接受的数据量。这个方案虽然可以使得瓶颈算子压力减少，但是不建议，可能会造成整个 Job 的数据延迟增大。

#### 垃圾收集（GC）

通常，长时间GC暂停会导致性能问题。您可以通过打印调试GC日志（通过 `-XX:+PrintGCDetails`）或使用某些内存或 GC 分析器来验证是否处于这种情况。由于处理 GC 问题高度依赖于应用程序，独立于 Flink，因此不会在此详细介绍。

#### CPU/线程瓶颈

有时，一个或几个线程导致 CPU 瓶颈，而整个机器的 CPU 使用率仍然相对较低，则可能无法看到 CPU 瓶颈。例如，48 核的服务器上，单个 CPU 瓶颈的线程仅占用 2％ 的 CPU 使用率，就算单个线程发生了 CPU 瓶颈，我们也看不出来。可以考虑使用代码分析器，它们可以显示每个线程的 CPU 使用情况来识别热线程。

#### 线程竞争

与上面的 CPU/线程瓶颈问题类似，subtask 可能会因为共享资源上高负载线程的竞争而成为瓶颈。同样，CPU 分析器是你最好的朋友！考虑在用户代码中查找同步开销、锁竞争，尽管避免在用户代码中添加同步。

#### 负载不平衡

如果瓶颈是由数据倾斜引起的，可以尝试通过将数据分区的 key 进行加盐或通过实现本地预聚合来减轻数据倾斜的影响。关于数据倾斜的详细解决方案，会在第 9.6 节详细讨论。

#### 外部依赖

如果发现我们的 Source 端数据读取性能比较低或者 Sink 端写入性能较差，需要检查第三方组件是否遇到瓶颈，例如，Kafka 集群是否需要扩容，Kafka 连接器是否并行度较低，HBase 的 rowkey 是否遇到热点问题。关于第三方组件的性能问题，需要结合具体的组件来分析，这里不进行详细介绍。

以上情况并非很详细。通常，为了解决瓶颈和减小反压，首先要分析反压发生的位置，然后找出原因并解决。

### 小结与反思

本节详细介绍 Flink 的反压机制，首先讲述了 Flink 为什么需要网络流控机制，再介绍了 Flink 1.5 之前的网络流控机制以及存在的问题，从而引出了基于 Credit 的反压机制，最后讲述 Flink 如何定位产生反压的位置以及定位到反压源后该如何处理。某个 Flink Job 中从 Source 到 Sink 的所有算子都不产生 shuffle，当任务的吞吐量降低时，无论是通过 Flink WebUI 还是 Metrics 都无法来辅助定位任务的瓶颈，对于这种情况，如何来定位任务的瓶颈，并解决呢？