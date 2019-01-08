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

* Spark作为云计算大数据时代的集大成者，其中其组件spark Streaming在企业准实时处理也是基本是必备，所以作为大数据从业者熟练掌握也是必须且必要的：

* Spark Streaming是非常出色的实时流处理框架，要掌握其DStream、transformation和checkpoint等;

* 熟练掌握kafka 与spark Streaming结合的两种方式及调优方式

* 熟练掌握Structured Streaming原理及作用并且要掌握其余kafka结合

* 熟练掌握SparkStreaming的源码尤其是和kafka结合的两种方式的源码原理。

* 熟练掌握spark Streaming的web ui及各个指标，如：批次执行事件处理时间，调度延迟，待处理队列并且会根据这些指标调优。

* 会自定义监控系统

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

## 1、SDD,DAG,Stage怎么理解?

DAG，有向无环图，简单的来说，就是一个由顶点和有方向性的边构成的图中，从任意一个顶点出发，没有任意一条路径会将其带回到出发点的顶点位置，
为每个spark job计算具有依赖关系的多个stage任务阶段，通常根据shuffle来划分stage，
如reduceByKey,groupByKey等涉及到shuffle的transformation就会产生新的stage,
然后将每个stage划分为具体的一组任务,以TaskSets的形式提交给底层的任务调度模块来执行,
其中不同stage之前的RDD为宽依赖关系,TaskScheduler任务调度模块负责具体启动任务，监控和汇报任务运行情况

## 2、宽依赖 窄依赖怎么理解?

## 3、Stage是基于什么原理分割task的?

## 4、血统的概念

## 5、任务的概念

## 6、容错方法

## 7、粗粒度和细粒度

## 8、Spark优越性

## 9、Spark为什么快

## 10、Transformation和action是什么?区别?举几个常用方法

## 11、SDD怎么理解,有哪些特性？

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


## 12、spark 作业提交流程是怎么样的，client和 cluster 有什么区别，各有什么作用

## 13、spark on yarn 作业执行流程，yarn-client 和 yarn cluster 有什么区别

## 14、spark streamning 工作流程是怎么样的，和 storm 比有什么区别

## 15、spark sql 你使用过没有，在哪个项目里面使用的

## 16、spark 机器学习和 spark 图计算接触过没，能举例说明你用它做过什么吗?

## 17、spark sdd 是怎么容错的，基本原理是什么?

## 18、概述一下spark中的常用算子区别（map,mapPartitions，foreach，foreachPatition）?

map：用于遍历RDD，将函数应用于每一个元素，返回新的RDD（transformation算子）

foreach：用于遍历RDD，将函数应用于每一个元素，无返回值（action算子）

mapPatitions：用于遍历操作RDD中的每一个分区，返回生成一个新的RDD（transformation算子）

foreachPatition：用于遍历操作RDD中的每一个分区，无返回值（action算子）

总结：一般使用mapPatitions和foreachPatition算子比map和foreach更加高效，推荐使用