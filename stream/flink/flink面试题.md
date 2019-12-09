# `Flink`面试题

## 基础
### 1.`Flink`计算单位是什么？

### 2.`Flink`时间类型有那些，他们有什么区别？
事件时间、注入时间和`Processing Time`

### 3.`Flink`窗口类型有哪些，你们目前用的什么窗口？

#### (1)TimeWindow

* Tumbling Window(滚动窗口)
    
* Sliding Window(滑动窗口)
    
* Session Window(会话窗口)
    
* Global Window(全局窗口)

#### (2)countWindow

#### (3)自定义window

### 4.`Flink`的状态你们有没有用过，用的什么类型的状态？

### 5.`Flink`如何处理延迟数据？

### 6.`Flink`中managed state和raw state区别？

### 7.`Flink`的`keyState`有什么不足，优点是什么，缺点是什么？

### 8.`Flink`的`watermark`有哪几种？

### 9.`Flink`自定义sink和source有没有写过，遇到了什么问题？

### 10.`Flink`自定udf函数有没有写过，解决的什么问题？

### 11. `Flink` 的 抽象层次有几种

Stateful stream processing

Core API

Table SQL

### 12. `Flink`,storm,spark streaming的区别
| 项目/引擎 | Storm | `Flink` |Spark Streaming|
| :------| ------: | :------: |:------: |
| API | 灵活的底层API和具有事务保证的Trident API | 流API和更加适合数据开发的Table API和`Flink` SQL支持 |流API和Structured-Streaming API同时也可以使用更适合数据开发的Spark SQL|
| 容错机制 | ACK机制 | State分布式快照保存点 |RDD保存点|
| 状态管理 | Trident State状态管理 | Key State和Operator State两种可以使用,支持多种持久化方案 |有UpdateStateByKey等API进行带状态的变更,支持多种持久化方案|
| 处理模式 | 单条流式处理 | 单条流式处理 |Mic batch处理|
| 延迟 | 毫秒级 | 毫秒级 |秒级|
| 语义保障 | At Least Once,Exactly Once | Exactly Once,At Least Once  |At Least Once|

### 13. checkpoint的理解
* 轻量级容错机制(全局异步，局部同步)
* 保证exactly-once 语义
* 用于内部失败的恢复
* 基本原理：通过往`source` 注入`barrier`，`barrier`作为checkpoint的标志

### 15. Flink runtime architecture

Job Manager
Task Manger
Client
角色间的通信(Akka)
数据的传输（Netty）

### 14. Savepoint的理解
* 流处理过程中的状态历史版本
* 具有可以replay的功能
* 外部恢复（应用重启和升级）
* 两种方式触发：
Cancel with savepoint，手动主动触发

bin/flink savepoint :jobId [:targetDirectory]

bin/flink cancel -s [:targetDirectory] :jobId

### 15.什么是solts

solts ：槽，slot 的数量通常与每个 TaskManager 的可用 CPU 内核数成比例。

一般情况下你的 slot 数是你每个 TaskManager 的 cpu 的核数

### 16.什么是状态

* Definition
task/operator在某个时刻的中间结果

Snapshot

* Effect：记录状态并且在失败时用于恢复

* Basic type：Operator state、Keyed state

### 17.[优化](https://www.iteblog.com/archives/2303.html)

1 使用 Flink Tuples
2 重用 Flink 对象
3 使用函数注解
4 选择 Join 类型

## 项目

### 1.你们项目中有没有遇到过背压？如何解决的？

### 2.你们项目中有没有遇到数据倾斜？如何解决的？

### 3.你们项目中有没有遇到状态异常需要人工修改？如何解决的？

### 4.你们项目中有没有遇到离线数据历史数据需要迁移到实时流中，比如历史视频的播放量，想要衔接到实时流中进行累加？如何解决的？

### 5.你们项目中有没有遇到手动维护kafka的offset，如何获取kafka的offset？

### 6.你们项目中有没有遇到checkpoint的oom现象，rocksDB有点和不足，checkpoint和savepoint的区别是什么？

### 7.你们项目中有没有遇到异步io读写的场景？

### 8.你们项目中有没有使用过广播的场景？

### 9.你们项目中有没有使用实时去重复，实时topN的场景，如何做的？

## 面试

### 1.梳理项目背景，你做的什么项目，数据量多少，这个项目应用场景。

### 2.每天多少条数据，数据量多大容量（多少TB）每秒钟处理多少条数据，你在项目中遇到了哪些问题，你是如何解决的?

### 3.项目中你用到了什么技术，这个技术有什么优点和不足，你要思考，为什么选这个技术，其他技术为什么可以？这个你要思考。

### 4.你的任务什么时间调度，有没有相应的监控，数据异常了有没有报警

### 5.思考好项目组分工，如何跟前端交互的，数据来源+加工+呈现，这个流程梳理清楚