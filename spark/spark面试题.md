
# Spark 面试题

https://blog.csdn.net/shujuelin/article/details/82851836

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