# storm

## 0、 doc

[官网](http://storm.apache.org)

[Apache Storm 官方文档中文版](http://ifeve.com/apache-storm/)

[storm 1.1.0中文文档](http://storm.apachecn.org/)

## 1、storm入门
* 是什么
* 发展历史
* Storm对比Hadoop
* Storm对比Spark Streaming
* Storm优势
* Storm应用现状和发展趋势
* Storm应用案例

### 是什么

1). 开源、免费、分布式的实时计算系统
Apache Storm is a free and open source distributed realtime computation system


2). 可靠的处理无界数据
Storm makes it easy to reliably process unbounded streams of data, doing for realtime processing what Hadoop did for batch processing
Storm可以轻松可靠地处理无界数据流,实时处理好比,Hadoop为批处理所做的工作
unbounded: 无界 只要有数据，就能像水龙头一样
bounded: 有界 Hadoop/Spark SQL 离线(input ...output)

3).学习成本低，支持多种语言
Storm is simple, can be used with any programming language, and is a lot of fun to use
Storm非常简单，可以与任何编程语言一起使用，还能做一些有意思的东西

使用场景:

    realtime analytics-实时分析
    
    online machine learning-在线机器学习
    
    continuous computation-持续性计算 PK Spark Structured Streaming(实时的数据不断追到一张表上-Unbounded Table)
    
    distributed RPC-分布式的RPC
    
    ETL
    
特点: 
    fast快: 高性能/低延时
        a benchmark clocked it at over a million tuples processed per second per node.      
    scalable：      可伸缩、可扩展
    fault-tolerant: 容错-健壮性
    guarantees your data will be processed:保证你的数据得到处理-数据不丢失
    storm可以集成一些大数据技术


Storm能实现高频数据和大规模的实时处理


### 1.3 Storm对比Hadoop  
                   Hadoop                          Storm
数据源/处理领域    离线                             实时
处理过程           map、reduce              Spout(水龙头 数据源) Bolt(水桶 逻辑处理)     
进程是否结束       是                                否
处理速度           慢                                快
适用场景           离线批计算               实时数据处理、分布式RPC、ETL 

### 1.4 Spark Streaming对比Storm  

[流式大数据处理的三种框架：Storm，Spark和Flink](https://blog.csdn.net/cm_chenmin/article/details/53072498)

Spark Streaming小批次，近似于实时
Storm 真正实时

Spark提供一站式解决方案
Storm需要数据落地


|对比点| Storm|  Spark Streaming|
|:----:|:----|----:|
|实时计算模型   | 纯实时 |准实时，对一个时间段内的数据收集起来，作为一个RDD，再处理|
|实时计算延迟度 | 毫秒级 |秒级 |
|吞吐量         | 低     | 高 |
|事务机制       |支持完善|支持，但不够完善|
|健壮性 / 容错性|ZooKeeper，Acker，非常强|Checkpoint，WAL，一般|
|动态调整并行度 |支持    |不支持|


1). 处理模型以及延迟
可扩展性(Scalability)和可容错性(Fault Tolerance)
Storm处理的是每次传入的一个事件
Spark Streaming是处理某个时间段窗口内的事件流
因此，Storm处理一个事件可以达到亚秒级的延迟，而Spark Streaming则有秒级的延迟  

2). 容错和数据保证
Spark Streaming提供了更好的支持容错状态计算

Storm:
 在Storm中，当每条单独的记录通过系统时必须被跟踪，所以Storm能够至少保证每条记录将被处理一次
，但是在从错误中恢复过来时候允许出现重复记录，这意味着可变状态可能不正确地被更新两次

 Storm的 Trident library库也提供了完全一次处理的功能。但是它依赖于事务更新状态，而这个过程是很慢的，并且通常必须由用户实现

Spark:
 Spark Streaming只需要在批处理级别对记录进行跟踪处理，因此可以有效地保证每条记录将完全被处理一次，即便一个节点发生故障

总结: 
需要亚秒级的延迟，Storm是一个不错的选择，而且没有数据丢失。
如果你需要有状态的计算，而且要完全保证每个事件只被处理一次，Spark Streaming则更好。
Spark Streaming编程逻辑也可能更容易，因为它类似于批处理程序，特别是在你使用批次(尽管是很小的)时

3).实现和编程API
Storm主要是由Clojure语言实现，Spark Streaming是由Scala实现

Storm提供了Java API，同时也支持其他语言的API。 
Spark Streaming支持Scala、Java、Python、R

Spark Streaming提供一站式解决方案,减少了单独编写流批量处理程序和历史数据处理程序

4）适用场景
Storm:
    需要纯实时，不能忍受1秒以上延迟的场景下使用，比如实时金融系统，要求纯实时进行金融交易和分析
    对高峰低峰时间段，动态调整实时计算程序的并行度，以最大限度利用集群资源
    要求可靠的事务机制和可靠性机制，即数据的处理完全精准，一条也不能多，一条也不能少
    纯粹的实时计算，不需要在中间执行SQL交互式查询、复杂的transformation算子等
    
Spark:
    不要求纯实时，不要求强大可靠的事务机制
    实时计算之外，还包括了离线批处理、交互式查询等业务功能,高延迟批处理、交互式查询等功能
    
### 优势
编程模型:Spout、Bolt 
扩展性
可靠性
容错性
多语言
本地调试

### 应用案例
电商:
    一淘-实时分析系统
    携程-网站性能监控
    阿里妈妈-用户画像
    
电信:
    诈骗电话分析
    流量预警

## 基本概念

拓扑（Topologies）: 将整个流程串起来;计算拓扑,由spout、bolt组成

流（Streams）: 数据流,水流;没有边界的tuple构成

数据源（Spouts）: 产生数据/水的东西 小溪流的源头Topology的消息生产者

数据流处理组件（Bolts）: 处理数据/水的东西 水壶/水桶 消息处理单元，可以做过滤、聚合、查询、写数据库的操作

数据(Tuple)            : 水;消息/数据 传递的基本单位

数据流分组（Stream groupings）

可靠性（Reliability）

任务（Tasks）

工作进程（Workers）  

![image](https://github.com/leelovejava/doc/blob/master/img/storm/01-storm-flow.png?raw=true)
  