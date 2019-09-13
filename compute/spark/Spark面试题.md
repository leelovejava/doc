# Spark 面试题

https://blog.csdn.net/shujuelin/article/details/82851836

## Spark大纲

### 第一阶段：熟练的掌握Scala及java语言

* Spark框架是采用Scala语言编写的，精致而优雅。要想成为Spark高手，你就必须阅读Spark的源代码，就必须掌握Scala,;

* 虽然说现在的Spark可以采用多语言Java、Python等进行应用程序开发，但是最快速的和支持最好的开发API依然并将永远是Scala方式的API，所以你必须掌握Scala来编写复杂的和高性能的Spark分布式程序;

* 尤其要熟练掌握Scala的trait、apply、函数式编程、泛型、逆变与协变等;

* 掌握JAVA语言多线程，netty，rpc，ClassLoader，运行环境等(源码需要)。

### 第二阶段：精通Spark平台本身提供给开发者API

* 掌握Spark中面向RDD的开发模式部署模式：本地(调试)，Standalone，yarn等 ，掌握各种transformation和action函数的使用;

* 掌握Spark中的宽依赖和窄依赖以及lineage机制;

* 掌握RDD的计算流程，例如Stage的划分、Spark应用程序提交给集群的基本过程和Worker节点基础的工作原理等

* 熟练掌握spark on yarn的机制原理及调优

### 第三阶段：深入Spark内核

* 此阶段主要是通过Spark框架的源码研读来深入Spark内核部分：

* 通过源码掌握Spark的任务提交过程;

* 通过源码掌握Spark集群的任务调度;

* 尤其要精通DAGScheduler、TaskScheduler，Driver和Executor节点内部的工作的每一步的细节;

* Driver和Executor的运行环境及RPC过程

* 缓存RDD，Checkpoint，Shuffle等缓存或者暂存垃圾清除机制

* 熟练掌握BlockManager，Broadcast，Accumulator，缓存等机制原理

* 熟练掌握Shuffle原理源码及调优

### 第四阶级:掌握基于Spark Streaming

* <font color=#0099ff face="黑体">Spark作为云计算大数据时代的集大成者，其中其组件spark Streaming在企业准实时处理也是基本是必备，所以作为大数据从业者熟练掌握也是必须且必要的</font>

* <font color=#0099ff face="黑体">Spark Streaming是非常出色的实时流处理框架，要掌握其DStream、transformation和checkpoint等</font>

* <font color=#0099ff face="黑体">熟练掌握kafka 与spark Streaming结合的两种方式及调优方式</font>

* <font color=#0099ff face="黑体">熟练掌握Structured Streaming原理及作用并且要掌握其余kafka结合</font>

* <font color=#0099ff face="黑体">熟练掌握SparkStreaming的源码尤其是和kafka结合的两种方式的源码原理</font>

* <font color=#0099ff face="黑体">熟练掌握spark Streaming的web ui及各个指标，如：批次执行事件处理时间，调度延迟，待处理队列并且会根据这些指标调优</font>

* <font color=#0099ff face="黑体">会自定义监控系统</font>

### 第五阶级:掌握基于Spark SQL

* 企业环境中也还是以数据仓库居多，鉴于大家对实时性要求比较高，那么spark sql就是我们作为仓库分析引擎的最爱(浪尖负责的两个集群都是计算分析一spark sql为主)：

* spark sql要理解Dataset的概念及与RDD的区别，各种算子

* 要理解基于hive生成的永久表和没有hive的临时表的区别

* spark sql+hive metastore基本是标配，无论是sql的支持，还是永久表特性

* 要掌握存储格式及性能对比

* Spark sql也要熟悉它的优化器catalyst的工作原理。

* Spark Sql的dataset的链式计算原理，逻辑计划翻译成物理计划的源码(非必须，面试及企业中牵涉到sql源码调优的比较少)

### 第六阶级:掌握基于spark机器学习及图计算
企业环境使用spark作为机器学习及深度学习分析引擎的情况也是日渐增多，结合方式就很多了：

* java系：
 * spark ml/mllib spark自带的机器学习库，目前也逐步有开源的深度学习及nlp等框架( spaCy, CoreNLP, OpenNLP, Mallet, GATE, Weka, UIMA, nltk, gensim, Negex, word2vec, GloVe)

 * 与DeepLearning4j目前用的也比较多的一种形式

* python系：

  * pyspark

spark与TensorFlow结合

### 第七阶级:掌握spark相关生态边缘

* 企业中使用spark肯定也会涉及到spark的边缘生态，这里我们举几个常用的软件框架：

* hadoop系列：kafka，hdfs，yarn

* 输入源及结果输出，主要是：mysql/redis/hbase/mongod

* 内存加速的框架redis，Alluxio

* es、solr

### 第八阶级:做商业级别的Spark项目
通过一个完整的具有代表性的Spark项目来贯穿Spark的方方面面，包括项目的架构设计、用到的技术的剖析、开发实现、运维等，完整掌握其中的每一个阶段和细节，这样就可以让您以后可以从容面对绝大多数Spark项目

### 第九阶级：提供Spark解决方案

* 彻底掌握Spark框架源码的每一个细节;

* 根据不同的业务场景的需要提供Spark在不同场景的下的解决方案;

* 根据实际需要，在Spark框架基础上进行二次开发，打造自己的Spark框架;

--------------------------------
### ⑴.SparkStreaming提纲复习

#### ①、DStream
Spark Streaming 是`微批次架构`，编程抽象是DStream(离散化流)。
它是一个RDD 序列，每个 RDD 代表数据流中一个时间段内的数据
Spark Streaming 为每个输入源启动对 应的接收器,接收器以任务的
形式运行在应用的执行器进程中，从输入源收集数据并保存为 RDD。
它们收集到输入数据后会把数据复制到另一个执行器进程来保障容错性(默认行为)。数据保存在执行器进程的内存中，和缓存 RDD 的方式一样。驱动器程序中的 StreamingContext 会周期性地运行 Spark 作业来处理这些数据，把数据与之前时间区间中的 RDD 进行整合

#### ②、transformation

㈠ 有状态转换

01).map

02).flatMap

03).filter

04).repartition

05).union

06).count

07).reduce

08).countByValue

09).reduceByKey

11).cogroup



![image](assets/interview/11_trans.png)

㈡ 无状态转换

01).updateStateByKey 追踪状态变化
```
# 使用updateStateByKey来更新状态，统计从运行开始以来单词总的次数
val pairs = ssc.socketTextStream("master01", 9999).flatMap(_.split(" ")).map(word => (word, 1))
pairs.updateStateByKey[Int](updateFunc)
//val wordCounts = pairs.reduceByKey(_ + _)
```

02).transform
允许DStream上执行任意的RDD-to-RDD函数
即使这些函数并没有在DStream的API中暴露出来，通过该函数可以方便的扩展Spark API

```
# RDD containing spam information
val spamInfoRDD = ssc.sparkContext.newAPIHadoopRDD(...) 

val cleanedDStream = wordCounts.transform { rdd =>
  # join data stream with spam information to do data cleaning
  # 在进行单词统计的时候，想要过滤掉spam的信息,本质是对DStream中的RDD应用转换
  rdd.join(spamInfoRDD).filter(...) 
  ...
}
```

03).join 连接
leftOuterJoin、rightOuterJoin、fullOuterJoin、Stream-Stream、windows-stream to windows-stream、stream-dataset

Stream-Stream Joins
```scala
val stream1: DStream[String, String] = ...
val stream2: DStream[String, String] = ...
val joinedStream = stream1.join(stream2)

val windowedStream1 = stream1.window(Seconds(20))
val windowedStream2 = stream2.window(Minutes(1))
val joinedStream = windowedStream1.join(windowedStream2)
```

Stream-dataset joins
```scala
val dataset: RDD[String, String] = ...
val windowedStream = stream.window(Seconds(20))...
val joinedStream = windowedStream.transform { rdd => rdd.join(dataset) }
```


#### ③、checkpoint
检查点机制是我们在 Spark Streaming 中用来保障容错性的主要机制。与应
用程序逻辑无关的错误（即系统错位，JVM 崩溃等）有迅速恢复的能力
目的:
1) 控制发生失败时需要重算的状态数。SparkStreaming 可以通 过转化图
的谱系图来重算状态，检查点机制则可以控制需要在转化图中回溯多远。
2) 提供驱动器程序容错。从检查点恢复

> ssc.checkpoint("hdfs://...") 

#### ④、[熟练掌握kafka 与spark Streaming结合的两种方式及调优方式](https://blog.csdn.net/weixin_39843430/article/details/80019519)
[SparkStreaming+kafka的receiver模式与direct模式](https://blog.csdn.net/wyqwilliam/article/details/84430548)

sparkStreaming读取kafka的两种方式
①Receiver-base
    先把数据从kafka中读取出来然后缓存到内存然后再定时处理
    Spark1.3淘汰
    kafkaUtils.createStream()
   
   这种方式可以保证数据不丢失，但是无法保证数据只被处理一次，WAL实现的是At-least-once语义（至少被处理一次），如果在写入到外部存储的数据还没有将offset更新到zookeeper就挂掉,这些数据将会被反复消费. 同时,降低了程序的吞吐量
   
   问题:
    数据丢失风险
        开启WAL(write ahead log)预写日志机制
            在接受过来数据备份到其他节点的时候，同时备份到HDFS上一份
            数据持久化级别,降低到`MEMORY_AND_DISK`,提高数据安全性
    提高并行度,`spark.streaming.blockInterval`,默认200ms

②[Direct](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/streaming/DirectKafkaWordCount.scala)
   延迟，action触发,官方推荐,Streaming负责追踪消费的offset,并保存到checkpoint
   
   确保机制更加健壮. 区别于使用Receiver来被动接收数据, Direct模式会周期性地主动查询Kafka, 来获得每个topic+partition的最新的offset, 从而定义每个batch的offset的范围. 当处理数据的job启动时, 就会使用Kafka的简单consumer api来获取Kafka指定offset范围的数据
   
   简化并行读取(读取多分区,无需创建多个Dstream进行union,kafka和RDD分区之间有一对一的映射关系)
   高性能(kafka数据复制,通过kafka副本进行回复) 
   一次且仅一次的事务机制(sparkStreaming程序自己消费完成后，自己主动去更新zk上面的偏移量)
   读取速度快:直接到kafka拿数据消费,不会存到内存再消费

```scala
 val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))
``` 

#### ⑤、熟练掌握SparkStreaming的源码尤其是和kafka结合的两种方式的源码原理
```scala
writeStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
    .option("topic", "updates")
    .start()
```

#### ⑥、熟练掌握Structured Streaming原理及作用并且要掌握与kafka结合
①原理:把数据流当作一个没有边界的数据表来对待,在流上使用Spark SQL进行流处理

②作用:
* 同样能支持多种数据源的输入和输出
* 以结构化的方式操作流式数据，能够像使用Spark SQL处理离线的批处理一样，处理流数据，代码更简洁，写法更简单
* 基于Event-Time，相比于Spark Streaming的Processing-Time更精确，更符合业务场景
* 解决了Spark Streaming存在的代码升级，DAG图变化引起的任务失败，无法断点续传的问题（Spark Streaming的硬伤！！！）


#### ⑦、熟练掌握spark Streaming的web ui及各个指标，如：批次执行事件处理时间，调度延迟，待处理队列并且会根据这些指标调优

①Processing Time:批数据处理的时间
最优的最小批次间隔500毫秒
②Scheduling Delay:前面的批处理完毕之后，当前批在队列中的等待时间。如果批处理时间比批间隔时间持续更长或者队列等待时间持续增加，这就预示系统无法以批数据产生的速度处理这些数据，整个处理过程滞后了。在这种情况下，考虑减少批处理时间

性能调优
①、设置合理的批处理时间
②、增加Job并行度
③、使用Kryo系列化
http://www.iteblog.com/archives/1328 
在Spark中自定义Kryo序列化输入输出API
④、缓存需要经常使用的数据
⑤、清除不需要的数据
⑥、设置合理的GC
⑦、设置合理的CPU资源数

cpu使用（1）、用于接收数据；（2）、用于处理数据。足够的CPU资源用于接收和处理数据，才能及时高效地处理数据

1.降低批次处理时间：

  ①数据接收并行度。

  (1)增加DStream：接收网络数据(如Kafka，flume，Socket等)时会对数据进行反序列化再存储在Spark，由于一个DStream只有Receiver对象，如果成为瓶颈可考虑增加DStream。

  (2)设置”spark.streaming.blockInterval”参数：接受的数据被存储在Spark内存前，会被合并成block，而block数量决定了task数量；举例，当批次时间间隔为2秒且block时间间隔为200毫秒时，Task数量约为10；如果Task数量过低，则浪费了cpu资源；推荐的最小block时间间隔为50ms。

  (3)显式对Input DStream重新分区：再进行更深层次处理前，先对输入数据进行重新分区。

  ②数据处理并行度：reduceByKey，reduceByKeyAndWindow等operation可通过设置”spark.default.parallelism”参数或显式设置并行度方法参数控制。

  ③数据序列化：可配置更高效的kryo序列化。

2.设置合理批次时间间隔：

  ①原则：处理数据的速度应大于或等于数据输入的速度，即批次处理时间大于或等于批次时间间隔。

  ②方法：

  (1)先设置批次时间间隔为5~10秒数据输入速度；

  (2)再通过查看log4j日志中的”Total delay”，逐步调整批次时间间隔，保证”Total delay”小于批次时间间隔。

3.内存调优：

  ①持久化级别：开启压缩，设置参数”spark.rdd.compress”；

  ②GC策略：在Driver和Executor上开启CMS(Content Management System 内容管理系统)

7、会自定义监控系统

Spark Streaming程序的处理过程可以通过StreamingListener接口来监控，允许获得receiver状态和处理时间。
自定义监听器，实现异常邮件或钉钉提醒

通过可插拔的方式添加自己实现的listener
> ssc.addStreamingListener()

8、Structured Streaming VS Flink
https://mp.weixin.qq.com/s/iAI-PCgboTClhzOgQSf5-g

9、Spark Streaming VS Flink
https://mp.weixin.qq.com/s/qamHUR8vDbxDOnLcUa4jLw

10、SparkStreaming与Storm的区别
a.Storm是纯实时的流式处理框架，SparkStreaming是准实时的处理框架（微批处理）。因为微批处理，SparkStreaming的吞吐量比Storm要高。
b.Storm 的事务机制要比SparkStreaming的要完善。
c.Storm支持动态资源调度。(spark1.2开始和之后也支持)

SparkStreaming擅长复杂的业务处理，Storm不擅长复杂的业务处理，擅长简单的汇总型计算


----
a.Spark Streaming最低可在0.5秒~2秒内做一次处理，而Storm最快可达到0.1秒，在实时性和容错性上，Spark Streaming不如Strom.
b.Spark Streaming的集成性优于Storm,可以通过RDD无缝对接Spark上的所有组件，还可以很容易的与kafka,flume等分布式框架进行集成。
c.在数据吞吐量上，Spark Streaming要远远优于Storm。

综上所诉，Spark Streaming更适用于大数据流式处理。

11. spark streamning 工作流程


11、熟练掌握Shuffle原理源码及调优?(重要)
a. shuffle过程的划分
a. spark shuffle的具体过程，你知道几种shuffle方式 
b. shuffle的中间结果如何存储
c. shuffle的数据如何拉取过来
d. 画图，讲讲shuffle的过程。那你怎么在编程的时候注意避免这些性能问题？

简单说一下hadoop和spark的shuffle过程？ 
hadoop：map端保存分片数据，通过网络收集到reduce端。 
spark：spark的shuffle是在DAGSchedular划分Stage的时候产生的,TaskSchedule要分发Stage到各个worker的executor。 
减少shuffle可以提高性能。

(1)从high-level的角度来看，两者并没有大的差别。都是将mapper(Spark中是ShuffleMapTask)的输出进行partition，不同的partition送到不同的reducer(Spark里的reducer可能是下一个stage的ShuffleMapTask，也可能是ResultTask)。Reducer以内存做缓冲区，边shuffle边aggregate数据，等数据aggregate好之后再进行reduce()(Spark里可能是后续的一系列操作)

(2)从low-level的角度来看，两者差距不小。Hadoop MapReduce是sort-based，进入combiner()和reduce()的records必须先sort。这样的好处在于combiner()/reduce()可以处理大规模的数据，因为其输入数据可以通过外排得到(mapper对每段数据先做排序，reducer的shuffle对排好序的每段数据做归并)。目前spark选择的是hash-based，通常使用HashMap对shuffle来的数据进行aggregate，不会对数据进行提前排序。如果用户需要进行排序的数据，那么要自己调用类似SortByKey()的操作。

(3)从现实角度来看，两者也有不小差距。Hadoop MapReduce将处理流程划分出明显的几个阶段：map()，spill，merge，shuffle，sort，reduce()等。每个阶段各司机制，可以按照过程式的编程思想来逐一实现每个阶段的功能。在Spark中，没有这样功能明确的阶段，只有不同的stage和一系列的transformation()，所以spill、sort、aggregate等操作需要蕴含在transformation()中。如果我们将map()端划分数据、持久化数据的过程称为shuffle write，而将reducer读入数据、aggregate数据的过程称为shuffle read。那么在spark中，问题就变成怎么在job的逻辑或者物理执行图中加入shuffle write、shuffle read的处理逻辑，以及两个处理逻辑怎么高效实现。Shuffle write由于不要求数据有序，shuffle write的任务很简单：将数据partition好，并持久化。之所以要持久化，一方面是要减少内存存储空间压力，另一方面也是为了fault-tolerance

spark的shuffle过程
a.Spark的shuffle总体而言就包括两个基本的过程：Shuffle write和Shuffle read。ShuffleMapTask的整个执行过程就是Shuffle write。将数据根据hash的结果，将各个Reduce分区的数据写到各自的磁盘中，写数据时不做排序操作。

b.首先是将map的输出结果送到对应的缓冲区bucket中，每个bucket里的文件都会被写入本地磁盘文件ShuffleBlockFile中，形成一个FileSegment文件。

c.Shuffle Read指的是reducer对属于自己的FileSegment文件进行fetch操作，这里采用的netty框架，fetch操作会等到所有的Shuffle write过程结束后再进行，.reducer通过fetch得到的FileSegment先放在缓冲区softBuffer中，默认大小45MB



e. 哪些算子操作涉及到shuffle
e. 什么时候join不发生shuffle？


http://www.cnblogs.com/jxhd1/p/6528540.html

https://mp.weixin.qq.com/s/6zvUHOa935xaEKpp_KCyGA

12).kafka数据堆积
https://mp.weixin.qq.com/s/gWzFR_btIv4bxmTwFYnHsQ

13).flume整合Spark Streaming问题。
    
(1)如何实现Spark Streaming读取flume中的数据
    
    
    
    前期经过技术调研，在查看官网资料，发现Spark Streaming整合flume有两种方式：拉模式，推模式。
    
    拉模式：Flume把数据push到Spark Streaming
    
    推模式：Spark Streaming从flume中poll数据
    
(2)在实际开发的时候是如何保证数据不丢失的
    flume那边采用的channel是将数据落地到磁盘中，保证数据源端安全性（可以在补充一下，flume在这里的channel可以设置为memory内存中，提高数据接收处理的效率，但是由于数据在内存中，安全机制保证不了，故选择channel为磁盘存储

(3)Spark Streaming的数据可靠性
checkpoint机制、write ahead log机制、Receiver缓存机器、可靠的Receiver（即数据接收并备份成功后会发送ack），可以保证无论是worker失效还是driver失效，都是数据0丢失。
原因：如果没有Receiver服务的worker失效了，RDD数据可以依赖血统来重新计算；如果Receiver所在worker失败了，由于Reciever是可靠的，并有write ahead log机制，则收到的数据可以保证不丢；如果driver失败了，可以从checkpoint中恢复数据重新构建

14).spark streming在实时处理时会发生什么故障，如何停止，解决?


----------------------------
Spark Core

1).Spark技术栈
1）Spark core：是其它组件的基础，spark的内核，主要包含：有向循环图、RDD、Lingage、Cache、broadcast等，并封装了底层通讯框架，是Spark的基础。
2）SparkStreaming是一个对实时数据流进行高通量、容错处理的流式处理系统，可以对多种数据源（如Kdfka、Flume、Twitter、Zero和TCP 套接字）进行类似Map、Reduce和Join等复杂操作，将流式计算分解成一系列短小的批处理作业。
3）Spark sql：Shark是SparkSQL的前身，Spark SQL的一个重要特点是其能够统一处理关系表和RDD，使得开发人员可以轻松地使用SQL命令进行外部查询，同时进行更复杂的数据分析
4）BlinkDB ：是一个用于在海量数据上运行交互式 SQL 查询的大规模并行查询引擎，它允许用户通过权衡数据精度来提升查询响应时间，其数据的精度被控制在允许的误差范围内。
5）MLBase是Spark生态圈的一部分专注于机器学习，让机器学习的门槛更低，让一些可能并不了解机器学习的用户也能方便地使用MLbase。MLBase分为四部分：MLlib、MLI、ML Optimizer和MLRuntime。
6）GraphX是Spark中用于图和图并行计算

1).Spark在什么场景比不上MapReduce?
Spark 在内存中处理数据，需要很大的内存容量。
如果 Spark 与其它资源需求型服务一同运行在YARN 上，又或者数据块太大以至于不能完全读入内存，
此时 Spark 的性能就会有很大的降低，此时Spark可能比不上MapReduce。
当对数据的操作只是简单的ETL的时候，Spark比不上MapReduce

spark产生背景及优势。

MapReduce编程的不便性：
1）繁杂：开发一个作业，既要写Map,又要写Reduce和驱动类。当需求变动要改变大量的代码
2）效率低：MapReduce基于进程，进程的启动和销毁要花费时间。
I/O频繁：网络I/O和磁盘I/O频繁
每个阶段都必须排序，但其实有些任务的排序是不必要的
3）不适合作迭代处理
4）只适合离线计算，不适合作实时处理

spark基于线程，线程直接从线程池中获取即可。
MapReduce也可以基于内存，但有一定限度。

很多框架都对spark做了兼容，使用起来很方便

Spark为什么比mapreduce快?
1）基于内存计算，减少低效的磁盘交互；

2）高效的调度算法，基于DAG；

3)容错机制Linage，精华部分就是DAG和Lingae


2).Spark为什么快?

spark能都取代hadoop?

Spark是一个计算框架，它没有自己的存储，它的存储还得借助于HDFS，所以说Spark不能取代Hadoop,要取代也是取代MapReduce



3)Spark的特点？

Apache Spark 是一个快速的处理大规模数据的通用工具。它是一个基于内存计算框架。它有以下的四个特点：
1）快速：基于内存的计算比MapReduce快100倍，基于磁盘快10倍。
2）易用：编写一个spark的应用程序可以使用 Java, Scala, Python, R，这就使得我们的开发非常地灵活。并且，对比于MapReduce,spark内置了80多个高级操作，这使得开发十分高效和简单。
3）运行范围广：spark可以运行在local、yarn、mesos、standalone、kubernetes等多种平台之上。它可以访问诸如HDFS, Cassandra, HBase, S3等多种多样的数据源。

4）通用：spark提供了SparkSQL、SparkStreaming、GraphX、MLlib等一系列的分析工具。



5)spark的主要特性在哪个版本引入。
外部数据源 1.2
DataFrame 1.3
动态资源调度 1.5
Dataset 1.6

6).RDD
rdd操作类型？
（1）transformation：转化，进行数据状态的转换，对已有的RDD创建新的RDD。

（2）Action：执行，触发具体的作业，对RDD最后取结果的一种操作

（3）Controller：控制，对性能效率和容错方面的支持。persist , cache, checkpoint

1、RDD是由一系列的分区组成。2、操作一个RDD实际上操作的是RDD的所有分区。3、RDD之间存在各种依赖关系。4、可选的特性，key-value型的RDD是通过hash进行分区。5、RDD的每一个分区在计算时会选择最佳的计算位置。

RDD五大特性？

什么是RDD？
RDD产生的意义在于降低开发分布式应用程序的门槛和提高执行效率。RDD全称resilient distributed dataset（弹性分布式数据集），它是一个可以容错的不可变集合，集合中的元素可以进行并行化地处理，Spark是围绕RDDs的概念展开的。RDD可以通过有两种创建的方式，一种是通过已经存在的驱动程序中的集合进行创建，另一种是通引用外部存储系统中的数据集进行创建，这里的外部系统可以是像HDFS或HBase这样的共享文件系统，也可以是任何支持hadoop InputFormat的数据。
在源码中，RDD是一个具备泛型的可序列化的抽象类。具备泛型意味着RDD内部存储的数据类型不定，大多数类型的数据都可以存储在RDD之中。RDD是一个抽象类则意味着RDD不能直接使用，我们使用的时候通常使用的是它的子类，如HadoopRDD,BlockRDD,JdbcRDD,MapPartitionsRDD,CheckpointRDD等。

在spark的官网，介绍了RDD的五大特性：1、RDD是由一系列的分区组成。2、操作一个RDD实际上操作的是RDD的所有分区。3、RDD之间存在各种依赖关系。4、可选的特性，key-value型的RDD是通过hash进行分区。5、RDD的每一个分区在计算时会选择最佳的计算位置。


RDD（Resilient Distributed Dataset）叫做分布式数据集，是spark中最基本的数据抽象，它代表一个不可变，可分区，里面的元素可以并行计算的集合
   
   Dataset：就是一个集合，用于存放数据的
   
   Destributed：分布式，可以并行在集群计算
   
   Resilient：表示弹性的，弹性表示
   
   1.RDD中的数据可以存储在内存或者磁盘中;
   
   2.RDD中的分区是可以改变的；
   
   五大特性：
   
   1.A list of partitions:一个分区列表，RDD中的数据都存储在一个分区列表中
   
   2.A function for computing each split:作用在每一个分区中的函数
   
   3.A list of dependencies on other RDDs:一个RDD依赖于其他多个RDD，这个点很重要，RDD的容错机制就是依据这个特性而来的
   
   4.Optionally,a Partitioner for key-value RDDs(eg:to say that the RDD is hash-partitioned):可选的，针对于kv类型的RDD才有这个特性，作用是决定了数据的来源以及数据处理后的去向
   
   5.可选项，数据本地性，数据位置最优

RDD有哪些缺陷？
1）不支持细粒度的写和更新操作（如网络爬虫），spark写数据是粗粒度的
所谓粗粒度，就是批量写入数据，为了提高效率。但是读数据是细粒度的也就是
说可以一条条的读
2）不支持增量迭代计算，Flink支持

.RDD创建有哪几种方式？
1).使用程序中的集合创建rdd
2).使用本地文件系统创建rdd
3).使用hdfs创建rdd，
4).基于数据库db创建rdd
5).基于Nosql创建rdd，如hbase
6).基于s3创建rdd，
7).基于数据流，如socket创建rdd
如果只回答了前面三种，是不够的，只能说明你的水平还是入门级的，实践过程中有很多种创建方式。


7).cache()和persist()方法的区别？
Cache：缓存数据，默认是缓存在内存中，，其本质还是调用persist

Persist：缓存数据，有丰富的缓存策略。数据可以保存在内存也可以保存在磁盘中，使用的时候指定对应的缓存级别。

Spark1.x和2.x的区别。

在${SPARK_HOME}/jars目录下有许多jar包，而在spark1.0版本中只有一个大的jar包

8).SparkContext?

目前在一个JVM进程中可以创建多个SparkContext，但是只能有一个active级别的。如果你需要创建一个新的SparkContext实例，必须先调用stop方法停掉当前active级别的SparkContext实例。

初始化一个SparkContext之前你需要构建一个SparkConf对象，初始化后，就可以使用SparkContext对象所包含的各种方法来创建和操作RDD和共享变量，Spark shell会自动初始化一个SparkContext

9).宽依赖和窄依赖？
   
   
   窄依赖是指父RDD的一个分区至多被子RDD的分区使用一次。(与数据规模无关)
   宽依赖是指父RDD的一个分区至少被子RDD的分区使用两次。(与数据规模有关)
   
   
   窄依赖的函数有：map, filter, union, join(父RDD是hash-partitioned ), mapPartitions, mapValues 
   
   宽依赖的函数有：xxxByKey, join(父RDD不是hash-partitioned ), partitionBy

10).Spark工作流程
画图讲解spark工作流程。以及在集群上和各个角色的对应关系

用户在client端提交作业后，会由Driver运行main方法并创建spark context上下文。 
执行add算子，形成dag图输入dagscheduler，按照add之间的依赖关系划分stage输入task scheduler。 
task scheduler会将stage划分为task set分发到各个节点的executor中执行。

1.构造Spark Application的运行环境(启动SparkContext)，SparkContext向资源管理器(可以是standalone、Mesos或Yarn)注册并申请运行Executor资源；

2.资源管理器分配Executor资源，Executor运行情况将随着心跳发送到资源管理器上；

3.SparkContext构建DAG图，将DAG图分解成Stage，并将Taskset发送给TaskSchedular。Executor向SparkContext申请Task，TaskSchedular将Task发送给Executor运行同时SparkContext将应用程序代码发送给Executor。

4.Task在Executor上运行，运行完毕释放所有资源。


什么是Spark Executor？
当SparkContext连接到集群管理器时，它会在集群中的节点上获取Executor。 executor是Spark进程，它运行计算并将数据存储在工作节点上。 SparkContext的最终任务被转移到executors以执行它们

Executor之间如何共享数据？
答：基于hdfs或者基于tachyon



11).DAG理解?
    DAG，有向无环图，简单的来说，就是一个由顶点和有方向性的边构成的图中，从任意一个顶点出发，没有任意一条路径会将其带回到出发点的顶点位置，
   为每个spark job计算具有依赖关系的多个stage任务阶段，通常根据shuffle来划分stage，
   如reduceByKey,groupByKey等涉及到shuffle的transformation就会产生新的stage,
   然后将每个stage划分为具体的一组任务,以TaskSets的形式提交给底层的任务调度模块来执行,
   其中不同stage之前的RDD为宽依赖关系,TaskScheduler任务调度模块负责具体启动任务，监控和汇报任务运行情况
   
12).Stage理解?Stage是基于什么原理分割task的?

spark中如何划分stage？

a.spark application中可以因为不同的action触发众多的job，一个Application中可以有很多job，每个job是有一个或多个stage构成的，后面的stage依赖于前面的stage，也就是说只有前面的stage计算完毕后，后面的stage才会运行。

b.stage划分的依据是宽依赖，何时产生宽依赖，例如ReduceBykey，GroupByKey的算子，会导致宽依赖的产生。

c.由Action算子(例如collect)导致了SparkContext.RunJob的执行，最终导致了DAGSchedular的submitJob的执行，其核心是通过发送一个case class Jobsubmitted对象给eventProcessLoop。

EventProcessLoop是DAGSchedularEventProcessLoop的具体事例，而DAGSchedularEventProcessLoop是eventLoop的子类，具体实现EventLoop的onReceiver方法，onReceiver方法转过来回调doOnReceive。

d.在handleJobSubmitted中首先创建finalStage，创建finalStage时候会建立父Stage的依赖链条。

总结：依赖是从代码的逻辑层面上来展开说的，可以简单点说：写介绍什么是RDD中的宽窄依赖，然后再根据DAG有向无环图进行划分，从当前job的最后一个算子往前推，遇到宽依赖，那么当前在这个批次中的所有算子操作都划分成一个stage，然后继续按照这种方式再继续往前推，如再遇到宽依赖，又划分成一个stage，一直到最前面的一个算子。最后整个job会被划分成多个stage，而stage之间又存在依赖关系，后面的stage依赖于前面的stage。
-

13).task

理解?   

spark中task有几种类型？
2种类型：1）result task类型，最后一个task，2）是shuffleMapTask类型，除了最后一个task都是

Spaek程序执行，有时候默认为什么会产生很多task，怎么修改默认task执行个数？
答：1）因为输入数据有很多task，尤其是有很多小文件的时候，有多少个输入
block就会有多少个task启动；2）spark中有partition的概念，每个partition都会对应一个task，task越多，在处理大规模数据的时候，就会越有效率。不过task并不是越多越好，如果平时测试，或者数据量没有那么大，则没有必要task数量太多。3）参数可以通过spark_home/conf/spark-default.conf配置文件设置:
spark.sql.shuffle.partitions 50 spark.default.parallelism 10
第一个是针对spark sql的task数量
第二个是非spark sql程序设置生效

Spark组件
a. master：管理集群和节点，不参与计算。 
b. worker：计算节点，进程本身不参与计算，和master汇报。 
c. Driver：运行程序的main方法，创建spark context对象。 
d. spark context：控制整个application的生命周期，包括dagsheduler和task scheduler等组件。 
e. client：用户提交程序的入口。  

Spark中Work的主要工作是什么?
主要功能：管理当前节点内存，CPU的使用状况，接收master分配过来的资源指令，通过ExecutorRunner启动程序分配任务，worker就类似于包工头，管理分配新进程，做计算的服务，相当于process服务。需要注意的是：

1）worker会不会汇报当前信息给master，worker心跳给master主要只有workid，它不会发送资源信息以心跳的方式给mater，master分配的时候就知道work，只有出现故障的时候才会发送资源。

2）worker不会运行代码，具体运行的是Executor是可以运行具体appliaction写的业务逻辑代码，操作代码的节点，它不会运行程序的代码的。


Spark driver的功能是什么? 
1）一个Spark作业运行时包括一个Driver进程，也是作业的主进程，具有main函数，并且有SparkContext的实例，是程序的人口点；
2）功能：负责向集群申请资源，向master注册信息，负责了作业的调度，，负责作业的解析、生成Stage并调度Task到Executor上。包括DAGScheduler，TaskScheduler。


14).血统


15).RDD容错方法,基本原理是什么?
   
16).粗粒度和细粒度
   
17).算子,Transformation和action是什么?区别?举几个常用方法
   
 
概述一下spark中的常用算子区别（map,mapPartitions，foreach，foreachPatition）?
   
   map：用于遍历RDD，将函数应用于每一个元素，返回新的RDD（transformation算子）
   
   foreach：用于遍历RDD，将函数应用于每一个元素，无返回值（action算子）
   
   mapPatitions：用于遍历操作RDD中的每一个分区，返回生成一个新的RDD（transformation算子）
   
   foreachPatition：用于遍历操作RDD中的每一个分区，无返回值（action算子）
   
   总结：一般使用mapPatitions和foreachPatition算子比map和foreach更加高效，推荐使用
 
 介绍一下cogroup rdd实现原理，你在什么场景下用过这个rdd？
 答：cogroup的函数实现:这个实现根据两个要进行合并的两个RDD操作,生成一个CoGroupedRDD的实例,这个RDD的返回结果是把相同的key中两个RDD分别进行合并操作,最后返回的RDD的value是一个Pair的实例,这个实例包含两个Iterable的值,第一个值表示的是RDD1中相同KEY的值,第二个值表示的是RDD2中相同key的值.由于做cogroup的操作,需要通过partitioner进行重新分区的操作,因此,执行这个流程时,需要执行一次shuffle的操作(如果要进行合并的两个RDD的都已经是shuffle后的rdd,同时他们对应的partitioner相同时,就不需要执行shuffle,)，
 场景：表关联查询
   

18).RDD、DataFrame互相转换、DataSet

rdd转为dataFrame两种方式？

19).Spark中的RDD的Partition数是由什么决定的?
20).flume配置需要关注哪些点?
21).时间窗口函数如何做到这个时间的窗口值依赖上个时间的窗口值
22).大数据领域常用端口?
23).数据倾斜?
1）前提是定位数据倾斜，是OOM了，还是任务执行缓慢，看日志，看WebUI
2)解决方法，有多个方面
· 避免不必要的shuffle，如使用广播小表的方式，将reduce-side-join提升为map-side-join
·分拆发生数据倾斜的记录，分成几个部分进行，然后合并join后的结果
·改变并行度，可能并行度太少了，导致个别task数据压力大
·两阶段聚合，先局部聚合，再全局聚合
·自定义paritioner，分散key的分布，使其更加均匀

[Spark：对数据倾斜的八种处理方法](https://blog.csdn.net/weixin_38750084/article/details/82721319)

24).Spark master使用zookeeper进行HA的，有哪些元数据保存在Zookeeper？Spark 中Master 实现HA有哪些方式 ？如何通过Zookeeper做HA？
    spark通过这个参数spark.deploy.zookeeper.dir指定master元数据在zookeeper中保存的位置，包括Worker，Driver和Application以及Executors。standby节点要从zk中，获得元数据信息，恢复集群运行状态，才能对外继续提供服务，作业提交资源申请等，在恢复前是不能接受请求的。另外，Master切换需要注意2点
    1）在Master切换的过程中，所有的已经在运行的程序皆正常运行！因为Spark Application在运行前就已经通过Cluster Manager获得了计算资源，所以在运行时Job本身的调度和处理和Master是没有任何关系的！
    2） 在Master的切换过程中唯一的影响是不能提交新的Job：一方面不能够提交新的应用程序给集群，因为只有Active Master才能接受新的程序的提交请求；另外一方面，已经运行的程序中也不能够因为Action操作触发新的Job的提交请求；
  
25).部署模式

spark的有几种部署模式，每种模式特点？
1）本地模式
Spark不一定非要跑在hadoop集群，可以在本地，起多个线程的方式来指定。将Spark应用以多线程的方式直接运行在本地，一般都是为了方便调试，本地模式分三类
·  local：只启动一个executor
·  local[k]:启动k个executor
·  local[*]
：启动跟cpu数目相同的 executor
2)standalone模式
分布式部署集群， 自带完整的服务，资源管理和任务监控是Spark自己监控，这个模式也是其他模式的基础，
3)Spark on yarn模式
分布式部署集群，资源和任务监控交给yarn管理，但是目前仅支持粗粒度资源分配方式，包含cluster和client运行模式，cluster适合生产，driver运行在集群子节点，具有容错功能，client适合调试，dirver运行在客户端
4）Spark On Mesos模式
1)   粗粒度模式（Coarse-grained Mode）：每个应用程序的运行环境由一个Dirver和若干个Executor组成，其中，每个Executor占用若干资源，内部可运行多个Task（对应多少个“slot”）。应用程序的各个任务正式运行之前，需要将运行环境中的资源全部申请好，且运行过程中要一直占用这些资源，即使不用，最后程序运行结束后，回收这些资源。
2)   细粒度模式（Fine-grained Mode）：鉴于粗粒度模式会造成大量资源浪费，Spark On Mesos还提供了另外一种调度模式：细粒度模式，这种模式类似于现在的云计算，思想是按需分配。

画图，画Spark的工作模式，部署分布架构图

spark on yarn 作业执行流程，yarn-client 和 yarn cluster 有什么区别

spark-submit的时候如何引入外部jar包?

26).Spark并行度怎么设置比较合适
spark并行度，每个core承载2~4个partition,如，32个core，那么64~128之间的并行度，也就是
设置64~128个partion，并行读和数据规模无关，只和内存使用量和cpu使用时间有关
27).Spark中数据的位置是被谁管理的？
 每个数据分片都对应具体物理位置，数据的位置是被blockManager，无论
    数据是在磁盘，内存还是tacyan，都是由blockManager管理
    
  BlockManager怎么管理硬盘和内存的？
    
28).Spark的数据本地性有哪几种?
a. PROCESS_LOCAL是指读取缓存在本地节点的数据
b. NODE_LOCAL是指读取本地节点硬盘数据
c. ANY是指读取非本地节点数据
通常读取数据PROCESS_LOCAL>NODE_LOCAL>ANY，尽量使数据以PROCESS_LOCAL或NODE_LOCAL方式读取。其中PROCESS_LOCAL还和cache有关，如果RDD经常用的话将该RDD cache到内存中，注意，由于cache是lazy的，所以必须通过一个action的触发，才能真正的将该RDD cache到内存中。

29).Spark如何处理不能被序列化的对象？
    将不能序列化的内容封装成object
    
30).介绍一下join操作优化经验？(重要)
join其实常见的就分为两类： map-side join 和  reduce-side join。当大表和小表join时，用map-side join能显著提高效率。
将多份数据进行关联是数据处理过程中非常普遍的用法，不过在分布式计算系统中，这个问题往往会变的非常麻烦，因为框架提供的 join 操作一般会将所有数据根据 key 发送到所有的 reduce 分区中去，也就是 shuffle 的过程。
造成大量的网络以及磁盘IO消耗，运行效率极其低下，这个过程一般被称为 reduce-side-join。如果其中有张表较小的话，我们则可以自己实现在 map 端实现数据关联，跳过大量数据进行 shuffle 的过程，运行时间得到大量缩短，根据不同数据可能会有几倍到数十倍的性能提升

简述hadoop实现join的及各种方式？
 
31).Spark累加器有哪些特点？
    1）累加器在全局唯一的，只增不减，记录全局集群的唯一状态
    2）在exe中修改它，在driver读取
    3）executor级别共享的，广播变量是task级别的共享
    两个application不可以共享累加器，但是同一个app不同的job可以共享
 
32).怎么用spark做数据清洗

33).Spark性能优化主要有哪些手段？
通过spark-env文件、程序中sparkconf和set property设置。 
（1）计算量大，形成的lineage过大应该给已经缓存了的rdd添加checkpoint，以减少容错带来的开销。 
（2）小分区合并，过小的分区造成过多的切换任务开销，使用repartition

34).简要描述Spark分布式集群搭建的步骤？ 

a. 准备linux环境，设置集群搭建账号和用户组，设置ssh，关闭防火墙，关闭seLinux，配置host，hostname
b. 配置jdk到环境变量
c. 搭建hadoop集群，如果要做master ha，需要搭建zookeeper集群
   修改hdfs-site.xml,hadoop_env.sh,yarn-site.xml,slaves等配置文件
d. 启动hadoop集群，启动前要格式化namenode
e. 配置spark集群，修改spark-env.xml，slaves等配置文件，拷贝hadoop相关配置到spark conf目录下
f. 启动spark集群。


35).spark 如何防止内存溢出 ？

a. driver端的内存溢出 
可以增大driver的内存参数：spark.driver.memory (default 1g)
这个参数用来设置Driver的内存。在Spark程序中，SparkContext，DAGScheduler都是运行在Driver端的。对应rdd的Stage切分也是在Driver端运行，如果用户自己写的程序有过多的步骤，切分出过多的Stage，这部分信息消耗的是Driver的内存，这个时候就需要调大Driver的内存。

b.map过程产生大量对象导致内存溢出 
这种溢出的原因是在单个map中产生了大量的对象导致的，例如：rdd.map(x=>for(i <- 1 to 10000) yield i.toString)，这个操作在rdd中，每个对象都产生了10000个对象，这肯定很容易产生内存溢出的问题。针对这种问题，在不增加内存的情况下，可以通过减少每个Task的大小，以便达到每个Task即使产生大量的对象Executor的内存也能够装得下。具体做法可以在会产生大量对象的map操作之前调用repartition方法，分区成更小的块传入map。例如：rdd.repartition(10000).map(x=>for(i <- 1 to 10000) yield i.toString)。 
面对这种问题注意，不能使用rdd.coalesce方法，这个方法只能减少分区，不能增加分区，不会有shuffle的过程。

c.数据不平衡导致内存溢出 
数据不平衡除了有可能导致内存溢出外，也有可能导致性能的问题，解决方法和上面说的类似，就是调用repartition重新分区。这里就不再累赘了。

d.shuffle后内存溢出 
shuffle内存溢出的情况可以说都是shuffle后，单个文件过大导致的。在Spark中，join，reduceByKey这一类型的过程，都会有shuffle的过程，在shuffle的使用，需要传入一个partitioner，大部分Spark中的shuffle操作，默认的partitioner都是HashPatitioner，默认值是父RDD中最大的分区数,这个参数通过spark.default.parallelism控制(在spark-sql中用spark.sql.shuffle.partitions) ， spark.default.parallelism参数只对HashPartitioner有效，所以如果是别的Partitioner或者自己实现的Partitioner就不能使用spark.default.parallelism这个参数来控制shuffle的并发量了。如果是别的partitioner导致的shuffle内存溢出，就需要从partitioner的代码增加partitions的数量。
standalone模式下资源分配不均匀导致内存溢出

e. 在standalone的模式下如果配置了–total-executor-cores 和 –executor-memory 这两个参数，但是没有配置–executor-cores这个参数的话，就有可能导致，每个Executor的memory是一样的，但是cores的数量不同，那么在cores数量多的Executor中，由于能够同时执行多个Task，就容易导致内存溢出的情况。这种情况的解决方法就是同时配置–executor-cores或者spark.executor.cores参数，确保Executor资源分配均匀。
使用rdd.persist(StorageLevel.MEMORY_AND_DISK_SER)代替rdd.cache()

f. rdd.cache()和rdd.persist(Storage.MEMORY_ONLY)是等价的，在内存不足的时候rdd.cache()的数据会丢失，再次使用的时候会重算，而rdd.persist(StorageLevel.MEMORY_AND_DISK_SER)在内存不足的时候会存储在磁盘，避免重算，只是消耗点IO时间。

36).Spark Executor OOM: How to set Memory Parameters on Spark
    OOM是内存里堆的东西太多了
    1）增加job的并行度，即增加job的partition数量，把大数据集切分成更小的数据，可以减少一次性load到内存中的数据量。InputFomart， getSplit来确定。
    2）spark.storage.memoryFraction
    管理executor中RDD和运行任务时的内存比例，如果shuffle比较小，只需要一点点shuffle memory，那么就调大这个比例。默认是0.6。不能比老年代还要大。大了就是浪费。
    3）spark.executor.memory如果还是不行，那么就要加Executor的内存了，改完executor内存后，这个需要重启。
    
--------------------------
Spark SQL

1).spark sql和sql的区别？

没关系 语法类似而已

2).spark sql 你使用过没有，在哪个项目里面使用的

3).Spark使用parquet文件存储格式能带来哪些好处？
a. 如果说HDFS 是大数据时代分布式文件系统首选标准，那么parquet则是整个大数据时代文件存储格式实时首选标准
b. 速度更快：从使用spark sql操作普通文件CSV和parquet文件速度对比上看，绝大多数情况
会比使用csv等普通文件速度提升10倍左右，在一些普通文件系统无法在spark上成功运行的情况
下，使用parquet很多时候可以成功运行
c. parquet的压缩技术非常稳定出色，在spark sql中对压缩技术的处理可能无法正常的完成工作
（例如会导致lost task，lost executor）但是此时如果使用parquet就可以正常的完成
d. 极大的减少磁盘I/o,通常情况下能够减少75%的存储空间，由此可以极大的减少spark sql处理
数据的时候的数据输入内容，尤其是在spark1.6x中有个下推过滤器在一些情况下可以极大的
减少磁盘的IO和内存的占用，（下推过滤器）
e. spark 1.6x parquet方式极大的提升了扫描的吞吐量，极大提高了数据的查找速度spark1.6和spark1.5x相比而言，提升了大约1倍的速度，在spark1.6X中，操作parquet时候cpu也进行了极大的优化，有效的降低了cpu
f. 采用parquet可以极大的优化spark的调度和执行。我们测试spark如果用parquet可以有效的减少stage的执行消耗，同时可以优化执行路径

4).spark怎么整合hive？

5).hbase region多大会分区，spark读取hbase数据是如何划分partition的？

6).Spark如何处理结构化数据，Spark如何处理非结构话数据？

7).讲讲列式存储的 parquet文件底层格式

8).spark sql为什么比hive快？

a.消除了冗余的HDFS读写

b.消除了冗余的MapReduce阶段

3.JVM的优化
--------------------------
Spark ML
spark 机器学习接触过没，能举例说明你用它做过什么吗?

mllib支持的算法？ 
分类、聚类、回归、协同过滤

1).ALS算法原理？ 
答：对于user-product-rating数据，als会建立一个稀疏的评分矩阵，其目的就是通过一定的规则填满这个稀疏矩阵。 
als会对稀疏矩阵进行分解，分为用户-特征值，产品-特征值，一个用户对一个产品的评分可以由这两个矩阵相乘得到。 
通过固定一个未知的特征值，计算另外一个特征值，然后交替反复进行最小二乘法，直至差平方和最小，即可得想要的矩阵。

2).kmeans算法原理？ 
随机初始化中心点范围，计算各个类别的平均值得到新的中心点。 
重新计算各个点到中心值的距离划分，再次计算平均值得到新的中心点，直至各个类别数据平均值无变化。

3).canopy算法原理？ 
根据两个阈值来划分数据，以随机的一个数据点作为canopy中心。 
计算其他数据点到其的距离，划入t1、t2中，划入t2的从数据集中删除，划入t1的其他数据点继续计算，直至数据集中无数据。

4).朴素贝叶斯分类算法原理？ 
对于待分类的数据和分类项，根据待分类数据的各个特征属性，出现在各个分类项中的概率判断该数据是属于哪个类别的。

5).关联规则挖掘算法apriori原理？ 
一个频繁项集的子集也是频繁项集，针对数据得出每个产品的支持数列表，过滤支持数小于预设值的项，对剩下的项进行全排列，重新计算支持数，再次过滤，重复至全排列结束，可得到频繁项和对应的支持数。


--------------------------
Spark graphx

spark 图计算接触过没，能举例说明你用它做过什么吗?


--------------------------
scala
1).scala中trait特征和用法？

2).项目用什么语言写？ Scala？ Scala的特点？ 和Java的区别？
3).Scala怎样声明变量与常量？
var val

4).什么是闭包？（******************）

闭包是一个函数，返回值依赖于声明在函数外部的一个或多个变量。

var factor = 3  
val multiplier = (i:Int) => i * factor 

闭包的实质就是代码与用到的非局部变量的混合，即：

闭包 = 代码 + 用到的非局部变量

5).什么是Scala的伴生类和伴生对象?(重点)
[Scala单例对象与伴生对象](https://www.jianshu.com/p/1f3012b54e4a)

6).使用shll和scala代码实现WordCount？

----------------------------
1).Hbase的设计有什么心得？
2).Hbase的操作是用的什么API还是什么工具？
3).有没有用过Zookeeper呢？ Zookeeper的适用场景是什么？ HA 状态维护 分布式锁 全局配置文件管理 操作Zookeeper是用的什么？
4).做过hbase的二级索引吗？
5).公司里集群规模。hbase数据量。数据规模
6).hbase存数据用什么rowkey？加时间戳的话，会不会出现时间戳重复的问题，怎么做的呢？