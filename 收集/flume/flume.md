# flume

http://archive.cloudera.com/cdh5/cdh/5/flume-ng-1.6.0-cdh5.7.0/

## 简介
* 1.flume是分布式的日志收集系统，把收集来的数据传送到目的地去。

* 2.flume里面有个核心概念，叫做agent.agent是一个java进程，运行在日志收集节点。

* 3.agent里面包含3个核心组件：source、channel、sink。

* 3.1 source组件是专用于收集日志的，可以处理各种类型各种格式的日志数据,
    包括avro、thrift、exec、jms、spooling directory、netcat、sequence generator、syslog、http、legacy、自定义。
    source组件把数据收集来以后，临时存放在channel中。

* 3.2 channel组件是在agent中专用于临时存储数据的，可以存放在memory、jdbc、file、自定义。
    channel中的数据只有在sink发送成功之后才会被删除。

* 3.3 sink组件是用于把数据发送到目的地的组件，目的地包括hdfs、logger、avro、thrift、ipc、file、null、hbase、solr、自定义。

* 4.在整个数据传输过程中，流动的是event。事务保证是在event级别。

* 5.flume可以支持多级flume的agent，支持扇入(fan-in)、扇出(fan-out)。

## flume架构

Agent:

    Flume以agent为最小的独立运行单位,一个Agent包含多个source、channel、sink和其他组件

source

    flume提供多种source供用户进行选择，尽可能多的满足大部分日志采集的需求，常用的source的类型包括avro、exec、netcat、spooling-directory和syslog等。具体的使用范围和配置方法详见source.

channel

    flume中的channel不如source和sink那么重要，但却是不可忽视的组成部分。常用的channel为memory-channel，同时也有其他类型的channel，如JDBC、file-channel、custom-channel等，详情见channel.

sink

    flume的sink也有很多种，常用的包括avro、logger、HDFS、Hbase以及file-roll等，除此之外还有其他类型的sink，如thrift、IRC、custom等。具体的使用范围和使用方法详见sink.

## 配置

常见的source

avro source:avro可以监听和收集指定端口的日志，使用avro的source需要说明被监听的主机ip和端口号

例子：

agent1.sources = r1

#描述source

agent1.sources.r1.type = avro  (类型为avro source)

agent1.sources.r1.bind = 0.0.0.0 （指定监听的主机ip.本机是0.0.0.0.）

agent1.sources.r1.port = 16667 (指定监听的端口号)

exec source:可以通过指定的操作对日志进行读取，使用exec时需要指定shell命令，对日志进行读取

```
#agent1表示代理名称
agent1.sources=source1
agent1.sinks=sink1
agent1.channels=channel1
```

```properties
#Spooling Directory是监控指定文件夹中新文件的变化，一旦新文件出现，就解析该文件内容，然后写入到channle。写入完成后，标记该文件已完成或者删除该文件。
#配置source1
agent1.sources.source1.type=spooldir
agent1.sources.source1.spoolDir=/root/hmbbs
agent1.sources.source1.channels=channel1
# 是否在event的Header中添加文件名，boolean类型, 默认false
agent1.sources.source1.fileHeader = false
agent1.sources.source1.interceptors = i1
agent1.sources.source1.interceptors.i1.type = timestamp
 
#配置sink1
agent1.sinks.sink1.type=hdfs
# 写入hdfs的路径，需要包含文件系统标识，比如：hdfs://namenode/flume/webdata/
agent1.sinks.sink1.hdfs.path=hdfs://hadoop0:9000/hmbbs
# 文件格式,包括:SequenceFile(默认)、DataStream、CompressedStream 当使用DataStream时候，文件不会被压缩，不需要设置hdfs.codeC;当使用CompressedStream时候，必须设置一个正确的hdfs.codeC值
agent1.sinks.sink1.hdfs.fileType=DataStream
# 写sequence文件的格式。包含：Text、Writable(默认)
agent1.sinks.sink1.hdfs.writeFormat=TEXT
# hdfs sink间隔多长将临时文件滚动成最终目标文件,单位:秒,默认值：30
agent1.sinks.sink1.hdfs.rollInterval=1
agent1.sinks.sink1.channel=channel1
# 默认值：FlumeData 写入hdfs的文件名前缀，可以使用flume提供的日期及%{host}表达式
agent1.sinks.sink1.hdfs.filePrefix=%Y-%m-%d
 
#配置channel1
agent1.channels.channel1.type=file
# 存放检查点目录
agent1.channels.channel1.checkpointDir=/root/hmbbs_tmp/123
# 存放数据的目录，dataDirs可以是多个目录，以逗号隔开。用独立的多个磁盘上的多个目录可以提高file channel的性能
agent1.channels.channel1.dataDirs=/root/hmbbs_tmp/
```

启动参数:
全局

--conf,-c <conf>
在<conf>目录使用配置文件。指定配置文件放在什么目录

--classpath,-C <cp>
追加一个classpath

--dryrun,-d
不真正运行Agent，而只是打印命令一些信息。

--plugins-path <dirs>
插件目录列表。默认：$FLUME_HOME/plugins.d

-Dproperty=value	   
设置一个JAVA系统属性值。

-Xproperty=value	   
设置一个JAVA -X的选项。

----
Agent选项
--conf-file,-f <file>
指定配置文件，这个配置文件必须在全局选项的--conf参数定义的目录下。（必填）

--name,-n <name>
Agent的名称（必填）

--help,-h
帮助

----
Avro客户端选项

--rpcProps,-P <file>
   连接参数的配置文件

--host,-H <host>
    Event所要发送到的Hostname

--port,-p <port>
    Avro Source的端口

--dirname <dir>
    Avro Source流到达的目录

--filename,-F <file>	   
    Avro Source流到达的文件名

--headerFile,-R <file>	   
    设置一个JAVA -X的选项

>> bin/flume-ng agent -n agent1 -c conf -f conf/example -Dflume.root.logger=DEBUG,console
