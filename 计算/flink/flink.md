# Apache Flink

Apache Flink is an open source stream processing framework with powerful stream- and batch-processing capabilities.

Learn more about Flink at [http://flink.apache.org/](http://flink.apache.org/)

## doc
[阿里巴巴为什么选择Apache Flink？](https://mp.weixin.qq.com/s/AoSDPDKbTbjH9rviioK-5Q)

[阿里重磅开源Blink：为什么我们等了这么久？](https://yq.aliyun.com/articles/680813)

[基于 Apache Flink 的实时计算引擎 Blink 在阿里搜索中的应用](https://yq.aliyun.com/articles/183934)

[Apache Flink 干货合集打包](https://mp.weixin.qq.com/s/rsJlZEP_oVG3NiFRyeS8gw)

[Apache Flink数据流容错机制](https://www.iteblog.com/archives/1987.html)

[美团点评基于 Flink 的实时数仓建设实践](https://mp.weixin.qq.com/s/PJmdXkdUE5gtzcYAgAM8wQ)

[四种优化 Apache Flink 应用程序的方法](https://www.iteblog.com/archives/2303.html)

[Flink on YARN部署快速入门指南](https://www.iteblog.com/archives/1620.html)

[flink的神奇分流器-sideoutput](https://mp.weixin.qq.com/s/nrBT2xkXpY8MmEksLessIw)

[Flink特异的迭代操作-bulkIteration](https://mp.weixin.qq.com/s/b5aCHqyhN1gO2OeYXdGG5g)

[Flink Forward China 2018](https://github.com/zheniantoushipashi/2018-flink-forward-china)

[《从0到1学习Flink》—— Flink 写入数据到 ElasticSearch](https://segmentfault.com/a/1190000017874148)

[写在阿里Blink正式开源之际](https://mp.weixin.qq.com/s/Sx2k0jS7bl6DZAlWlTAcsA)

[用Flink取代Spark Streaming！知乎实时数仓架构演进](https://mp.weixin.qq.com/s/kN93pMYTxeIRZv5wWu7-ug)

[从Storm到Flink，有赞五年实时计算效率提升实践](https://mp.weixin.qq.com/s/ZkVK9S-BSoQTo09ALBI9aA)

[Flink+HBase场景化解决方案](https://mp.weixin.qq.com/s/3a_7_XLvJ6sLxu_jILdfpg)

[Kylin 实时流处理技术探秘](https://mp.weixin.qq.com/s/ucdxocEP09YbF5zcDwMzLA)]

[基于Flink+TensorFlow打造实时智能异常检测平台](https://mp.weixin.qq.com/s/q2rgEm-tGxOlUUFfb80pEg)

[Apache Flink 1.9 重大特性提前解读](https://mp.weixin.qq.com/s/ZcjKRlmRtD_-tSVaTL4j7Q)

## Features
 
* A streaming-first runtime that supports both batch processing and data streaming programs

* Elegant and fluent APIs in Java and Scala

* A runtime that supports very high throughput and low event latency at the same time

* Support for *event time* and *out-of-order* processing in the DataStream API, based on the *Dataflow Model*

* Flexible windowing (time, count, sessions, custom triggers) across different time semantics (event time, processing time)

* Fault-tolerance with *exactly-once* processing guarantees

* Natural back-pressure in streaming programs

* Libraries for Graph processing (batch), Machine Learning (batch), and Complex Event Processing (streaming)

* Built-in support for iterative programs (BSP) in the DataSet (batch) API

* Custom memory management for efficient and robust switching between in-memory and out-of-core data processing algorithms

* Compatibility layers for Apache Hadoop MapReduce and Apache Storm

* Integration with YARN, HDFS, HBase, and other components of the Apache Hadoop ecosystem


## Streaming Example
```scala
case class WordWithCount(word: String, count: Long)

val text = env.socketTextStream(host, port, '\n')

val windowCounts = text.flatMap { w => w.split("\\s") }
  .map { w => WordWithCount(w, 1) }
  .keyBy("word")
  .timeWindow(Time.seconds(5))
  .sum("count")

windowCounts.print()
```

## Batch Example
```scala
case class WordWithCount(word: String, count: Long)

val text = env.readTextFile(path)

val counts = text.flatMap { w => w.split("\\s") }
  .map { w => WordWithCount(w, 1) }
  .groupBy("word")
  .sum("count")

counts.writeAsCsv(outputPath)
```



## Building Apache Flink from Source

Prerequisites for building Flink:

* Unix-like environment (we use Linux, Mac OS X, Cygwin)
* git
* Maven (we recommend version 3.2.5)
* Java 8 (Java 9 and 10 are not yet supported)

```
git clone https://github.com/apache/flink.git
cd flink
mvn clean package -DskipTests # this will take up to 10 minutes
```

Flink is now installed in `build-target`

*NOTE: Maven 3.3.x can build Flink, but will not properly shade away certain dependencies. Maven 3.0.3 creates the libraries properly.
To build unit tests with Java 8, use Java 8u51 or above to prevent failures in unit tests that use the PowerMock runner.*

## Developing Flink

The Flink committers use IntelliJ IDEA to develop the Flink codebase.
We recommend IntelliJ IDEA for developing projects that involve Scala code.

Minimal requirements for an IDE are:
* Support for Java and Scala (also mixed projects)
* Support for Maven with Java and Scala


## IntelliJ IDEA

The IntelliJ IDE supports Maven out of the box and offers a plugin for Scala development.

* IntelliJ download: [https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)
* IntelliJ Scala Plugin: [http://plugins.jetbrains.com/plugin/?id=1347](http://plugins.jetbrains.com/plugin/?id=1347)

Check out our [Setting up IntelliJ](https://github.com/apache/flink/blob/master/docs/internals/ide_setup.md#intellij-idea) guide for details.

## Eclipse Scala IDE

**NOTE:** From our experience, this setup does not work with Flink
due to deficiencies of the old Eclipse version bundled with Scala IDE 3.0.3 or
due to version incompatibilities with the bundled Scala version in Scala IDE 4.4.1.

**We recommend to use IntelliJ instead (see above)**

## Support

Don’t hesitate to ask!

Contact the developers and community on the [mailing lists](http://flink.apache.org/community.html#mailing-lists) if you need any help.

[Open an issue](https://issues.apache.org/jira/browse/FLINK) if you found a bug in Flink.


## Documentation

The documentation of Apache Flink is located on the website: [http://flink.apache.org](http://flink.apache.org)
or in the `docs/` directory of the source code.


## Fork and Contribute

This is an active open-source project. We are always open to people who want to use the system or contribute to it.
Contact us if you are looking for implementation tasks that fit your skills.
This article describes [how to contribute to Apache Flink](http://flink.apache.org/how-to-contribute.html).


## About

Apache Flink is an open source project of The Apache Software Foundation (ASF).
The Apache Flink project originated from the [Stratosphere](http://stratosphere.eu) research project.


### Flink 实战

[1、《从0到1学习Flink》—— Apache Flink 介绍](https://mp.weixin.qq.com/s/M-XK0bEHdbVLtwtsRG_e8Q)

[2、《从0到1学习Flink》—— Mac 上搭建 Flink 1.6.0 环境并构建运行简单程序入门](https://mp.weixin.qq.com/s/woieA6jQ31Wb-PWXbQFbiw)

[3、《从0到1学习Flink》—— Flink 配置文件详解](https://mp.weixin.qq.com/s/770kfRCx1j09Lw8F_mAymA)

[4、《从0到1学习Flink》—— Data Source 介绍](https://mp.weixin.qq.com/s/aPbmsgPbpfY3U3Ax7nENWw)

[5、《从0到1学习Flink》—— 如何自定义 Data Source ？](https://mp.weixin.qq.com/s/xPrprtXXpItfyehz3tjYJw)

[6、《从0到1学习Flink》—— Data Sink 介绍](https://mp.weixin.qq.com/s/yH4oNUs1VO5wx6XvYhVJcg)

[7、《从0到1学习Flink》—— 如何自定义 Data Sink ？](https://mp.weixin.qq.com/s/dRS-fX8vkc7Ag4gipr_FNw)

[8、《从0到1学习Flink》—— Flink Data transformation(转换)](https://mp.weixin.qq.com/s/l9l0MPfbEanLwFlP8a0gbQ)

[9、《从0到1学习Flink》—— 介绍 Flink 中的 Stream Windows](https://mp.weixin.qq.com/s/OJnLsU0eHQtD_ZHohQy1ew)

[10、《从0到1学习Flink》—— Flink 中的几种 Time 详解](https://mp.weixin.qq.com/s/WrDwd1Ca1jMch6ERCpb_FA)

[11、《从0到1学习Flink》—— Flink 读取 Kafka 数据写入到 ElasticSearch](https://mp.weixin.qq.com/s/doE5TmBomyX3mo2EqYH3ag)

[12、《从0到1学习Flink》—— Flink 项目如何运行？](https://mp.weixin.qq.com/s/79C2z0xMY2nh4bzUc1sVbQ)

[13、《从0到1学习Flink》—— Flink 读取 Kafka 数据写入到 Kafka](https://mp.weixin.qq.com/s/cWzMzuoRA7eALPYR-rU5fQ)

[14、《从0到1学习Flink》—— Flink JobManager 高可用性配置](https://mp.weixin.qq.com/s/KRK4GqBKDAkEraRQUKjtkA)

[15、《从0到1学习Flink》—— Flink parallelism 和 Slot 介绍](https://mp.weixin.qq.com/s/600OHnmUsII0LgujcjBhSA)

[16、《从0到1学习Flink》—— Flink 读取 Kafka 数据批量写入到 MySQL](https://mp.weixin.qq.com/s/McSuBQP6vbEYkiXtWnUkrg)

[17、《从0到1学习Flink》—— Flink 读取 Kafka 数据写入到 RabbitMQ](https://mp.weixin.qq.com/s/Zi3rpImUtEM60tY32WsIfg)

[18、《从0到1学习Flink》—— 你上传的 jar 包藏到哪里去了](https://mp.weixin.qq.com/s/SSbu1lDO3yfDhV-Li3qLgQ)

[19、大数据“重磅炸弹”——实时计算框架 Flink](https://mp.weixin.qq.com/s/jmN-n7kmD6tP_RymFsAKPQ)

[20、《Flink 源码解析》—— 源码编译运行](https://mp.weixin.qq.com/s/SdJShDpuM9YjgIdMRo6Mdg)

[21、为什么说流处理即未来？](https://mp.weixin.qq.com/s/eksorQsBSFU3Uga1GeeA1w)

[22、OPPO数据中台之基石：基于Flink SQL构建实数据仓库](https://mp.weixin.qq.com/s/JsoMgIW6bKEFDGvq_KI6hg)

[23、流计算框架 Flink 与 Storm 的性能对比](https://mp.weixin.qq.com/s/NOVGMQXdhdTVAbSe3BFAEQ)

[24、Flink状态管理和容错机制介绍](https://mp.weixin.qq.com/s/ipDG_y3Nstm7fujZT9-JGw)

[25、原理解析 | Apache Flink 结合 Kafka 构建端到端的 Exactly-Once 处理](https://mp.weixin.qq.com/s/H7h5e-fxxKHGgE-mGE-73w)

[26、Apache Flink 是如何管理好内存的？](https://mp.weixin.qq.com/s/K65OjUNM1Wf8Z5H9GS4zbg)

[27、《从0到1学习Flink》——Flink 中这样管理配置，你知道？](https://mp.weixin.qq.com/s/renA0tZZPTJw-eYp8cEZng)

[28、《从0到1学习Flink》——Flink 不可以连续 Split(分流)？](https://mp.weixin.qq.com/s/600OHnmUsII0LgujcjBhSA)

[29、Flink 从0到1学习—— 分享四本 Flink 的书和二十多篇 Paper 论文](https://mp.weixin.qq.com/s/_PdtL-E6AEmSMBwY09samA)

[30、360深度实践：Flink与Storm协议级对比](https://mp.weixin.qq.com/s/E7pM5XKb_QH225nl0JKFkg)
