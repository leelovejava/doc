# 结构化流(Structured Streaming)

### 参考
[Spark 2.3.0 Structured Streaming详解](https://blog.csdn.net/l_15156024189/article/details/81612860)

[官网](http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)

[秒懂StructuredStreaming-StructuredStreaming是何方神圣](https://blog.csdn.net/lovechendongxing/article/details/81748237)

[秒懂StructuredStreaming-手把手教你写StructuredStreaming + Kafka程序](https://blog.csdn.net/lovechendongxing/article/details/81748553)

### 是什么?
* 是一个建立在Spark SQL引擎之上可扩展且容错的流处理引擎。
* 可以使用与静态数据批处理计算相同的方式来表达流计算。
* 当不断有流数据到达时，Spark SQL引擎将会增量地、连续地计算它们，然后更新最终的结果。
* 最后，系统通过检查点和预写日志的方式确保端到端只执行一次的容错保证。
* 总之，结构化流（Structured Streaming）提供了快速的、可扩展的、容错的和端到端只执行一次的（end-to-end exactly-once）流处理，用户无需考虑流本身。

### 为什么要有?
Structured Streaming是Spark2.0版本提出的新的实时流框架（2.0和2.1是实验版本，从Spark2.2开始为稳定版本），
相比于Spark Streaming，优点如下：
* 同样能支持多种数据源的输入和输出，参考如上的数据流图
* 以结构化的方式操作流式数据，能够像使用Spark SQL处理离线的批处理一样，处理流数据，代码更简洁，写法更简单
* 基于Event-Time，相比于Spark Streaming的Processing-Time更精确，更符合业务场景
* 解决了Spark Streaming存在的代码升级，DAG图变化引起的任务失败，无法断点续传的问题（Spark Streaming的硬伤！！！）


* 结构化流查询(Structured Streaming Query）内部默认使用微批处理引擎(micro-batch processing engine)，
* 它将数据流看作一系列小的批任务(batch jobs)来处理，
* 从而达到端到端如100毫秒这样低的延迟以及只执行一次容错的保证。
* 从Spark 2.3，我们已经引入了一个新的低延迟处理方式——连续处理(Continuous Processing)，
* 可以达到端到端如1毫秒这样低的延迟至少一次保证。
* 不用改变查询中DataSet/DataFrame的操作，你就能够选择基于应用要求的查询模式
        
### 官方quick start
1) Running Netcat

> yum install -y nc

> nc -lk 9999

> apache spark
> apache hadoop

2) TERMINAL 2: RUNNING StructuredNetworkWordCount
> ./bin/run-example org.apache.spark.examples.sql.streaming.StructuredNetworkWordCount localhost 9999

### Output Sinks 输出的类型
[官网解释](http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#output-sinks)
1)file，保存成csv或者parquet
```scala
writeStream
.format("parquet")        // can be "orc", "json", "csv", etc.
.option("path", "path/to/destination/dir")
.option("checkpointLocation", "path/to/checkpoint/dir")
.start()
```
2)console，直接输出到控制台。一般做测试的时候用这个比较方便
```
writeStream
.format("console")
.start()
```
3)memory，可以保存在内容，供后面的代码使用
```scala
writeStream
      .queryName("aggregates")// this query name will be the table name
       .outputMode("complete")
      .format("memory")
      .start()
      spark.sql("select * from aggregates").show()
```
4).foreach，参数是一个foreach的方法，用户可以实现这个方法实现一些自定义的功能
   Foreach sink - Runs arbitrary computation on the records in the output. See later in the section for more details.
```
 writeStream
  .foreach(...)
  .start()

streamingDF.writeStream.foreachBatch { (batchDF: DataFrame, batchId: Long) =>
// Transform and write batchDF
}.start()
```
5).Kafka sink - Stores the output to one or more topics in Kafka.
```
writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
      .option("topic", "updates")
      .start()
```

### output Mode 输出模式
与普通的Spark的输出不同，只有三种类型：
1).complete: 
    把所有的DataFrame的内容输出(全量输出)，
    场景:只能在做agg聚合操作的时候使用(数据全集计算)，比如ds.group.count
2).append:   
    普通的dataframe在做完map或者filter之后可以使用
    这种模式会把新的batch的数据输出出来(每次都是添加新的行)
    场景:且只适用于:一旦产生计算结果便永远不会去修改的情形， 所以它能保证每一行数据只被数据一次
3).update:
    把此次新增的数据输出，更新整个dataframe。有点类似之前的streaming的state处理。
    对于数据库类型的sink来说，这是一种理想的模式

[官网解释](http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#output-modes)
* Append mode (default) - This is the default mode, where only the new rows added to the Result Table since the last trigger will be outputted to the sink. This is supported for only those queries where rows added to the Result Table is never going to change. Hence, this mode guarantees that each row will be output only once (assuming fault-tolerant sink). For example, queries with only select, where, map, flatMap, filter, join, etc. will support Append mode.
* Complete mode - The whole Result Table will be outputted to the sink after every trigger. This is supported for aggregation queries.
* Update mode - (Available since Spark 2.1.1) Only the rows in the Result Table that were updated since the last trigger will be outputted to the sink. More information to be added in future releases.
