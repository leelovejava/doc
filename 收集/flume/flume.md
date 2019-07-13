# flume

http://archive.cloudera.com/cdh5/cdh/5/flume-ng-1.6.0-cdh5.7.0/

[flume官网](http://flume.apache.org/)
[用户手册](http://flume.apache.org/FlumeUserGuide.html)
[Flume-1-7-0中文用户手册](https://www.cnblogs.com/ximengchj/p/6423689.html)
[Flume-1.8.0用户手册官方文档理解](https://blog.csdn.net/weixin_40483882/article/details/81227952)
[flume学习系列](https://www.jianshu.com/p/bce1088eb8a6)

## 大纲

### Flume概述/基础架构
* Flume快速入门
* Flume案例实操
* Flume进阶案例
* Flume事务、Agent内部原理
* Flume拓扑结构
* Flume企业开发案例
* 自定义Interceptor、Source、Sink
* Flume数据流监控-Ganglia
* 企业真实面试题（重点）

## 1.简介
Flume is a distributed(分布式), reliable(可靠), and available(可用) service for efficiently collecting(收集), aggregating(聚合), and moving(移动) large amounts of log data. 

*Apache Flume是为有效收集聚合和移动大量来自不同源到中心数据存储而设计的可分布，可靠的，可用的系统*

It has a simple and flexible architecture based on streaming data flows. 

It is robust and fault tolerant with tunable reliability mechanisms and many failover and recovery mechanisms. 

It uses a simple extensible data model that allows for online analytic application

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

## 2.为什么需要flume？
* 解耦：对于数据产生者，不关心数据被谁使用，对于数据使用者，不关心数据从哪来。
   
* 安全，稳定：flume 是提供数据安全保证的。
   
* 缓冲：数据生产速度 和 消费速度 可以得到一个平衡，不至于因为生产过快导致程序崩溃。
   
* 简单：内置大量现成组件，使用成本低。
   
* 负载均衡：flume 是分布式，对于大数据收集有天然优势
   
* 对 hdfs 支持友好
   
* 灵活：flume 收集基于单个 agent，扩展方便灵活
   
## 3.flume 有什么优势？
* 组件灵活，可定制化高

* 数据处理能力相对较强

* 对hdfs 有特殊优化
   
## 4.flume架构

Agent:

    Flume以agent为最小的独立运行单位,一个Agent就是一个JVM进程,一个Agent包含多个source、channel、sink三大组件

source

    源(收集数据),常用的source的类型包括avro、exec、netcat、spooling-directory和syslog等

channel

    通道(数据的缓冲区),常用的channel为memory-channel，其他如JDBC、file-channel、custom-channel等

sink

    写入数据源,flume的sink,常用的包括avro、logger、HDFS、Hbase以及file-roll等，其他如thrift、IRC、custom等

![image](https://github.com/leelovejava/doc/blob/master/img/collect/flume/01.png?raw=true)

webServer作为一个客户端，会产生数据，将数据发送到 一个叫 source 的组件

source 将收到的数据存到一个叫 channel 的组件

sink 会从channel里面取出source 储存的数据，并将它放到 hdfs 上


## 5.配置

flume-env.sh,配置java环境变量


### 5.1.source

#### 5.1.2.avro 
source:avro可以监听和收集指定端口的日志,使用avro的source需要说明被监听的主机ip和端口号

```
agent1.sources = r1

#描述source

agent1.sources.r1.type = avro  (类型为avro source)

agent1.sources.r1.bind = 0.0.0.0 （指定监听的主机ip.本机是0.0.0.0.）

agent1.sources.r1.port = 16667 (指定监听的端口号)

exec source:可以通过指定的操作对日志进行读取，使用exec时需要指定shell命令，对日志进行读取

#agent1表示代理名称
agent1.sources=source1
agent1.sinks=sink1
agent1.channels=channel1
```

```
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
#### 5.1.2.kafka

### 5.1.sinks

#### 5.1.1.HDFS 

#### 5.1.2.Hive 

#### 5.1.3.Kafka 

#### 5.1.4.Hbase
HBaseSink      同步
AsyncHBaseSink 异步 

### 6.启动参数:
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

## 6.案例

### 6.1.监听端口,输出端口数据

#### 6.1.1、创建Flume Agent配置文件flume-telnet.conf
```
# Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = netcat
a1.sources.r1.bind = localhost
a1.sources.r1.port = 44444

# Describe the sink
a1.sinks.k1.type = logger

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1
```

#### 6.1.2、安装telnet工具
检查是否安装:
>> rpm -qa telnet-server
>> rpm -qa xinetd

卸载
>> sudo rpm -e telnet-server-0.17-48.el6.x86_64

yum方式安装
>> sudo  yum -y install telnet-server.x86_64
>> sudo  yum -y install install telnet.x86_64
>> sudo  yum -y install xinetd.x86_64

rpm方式安装
>> $ sudo rpm -ivh telnet-server-0.17-59.el7.x86_64.rpm 
>> $ sudo rpm -ivh telnet-0.17-59.el7.x86_64.rpm

#### 6.1.3、首先判断44444端口是否被占用
>> $ netstat -an | grep 44444

#### 6.1.4、先开启flume先听端口
>> $ bin/flume-ng agent --conf conf/ --name a1 --conf-file conf/flume-telnet.conf -Dflume.root.logger==INFO,console

#### 6.1.5、使用telnet工具向本机的44444端口发送内容。
>> $ telnet localhost 44444

### 6.2.监听上传Hive日志文件到HDFS
#### 6.2.1 拷贝Hadoop相关jar到Flume的lib目录下
```
share/hadoop/common/lib/hadoop-auth-2.5.0-cdh5.3.6.jar
share/hadoop/common/lib/commons-configuration-1.6.jar
share/hadoop/mapreduce1/lib/hadoop-hdfs-2.5.0-cdh5.3.6.jar
share/hadoop/common/hadoop-common-2.5.0-cdh5.3.6.jar
```

#### 6.2.2 创建flume-hdfs.conf文件
```
# Name the components on this agent
a2.sources = r2
a2.sinks = k2
a2.channels = c2

# Describe/configure the source
a2.sources.r2.type = exec
a2.sources.r2.command = tail -f /home/hadoop/app/hive-1.1.0-cdh5.7.0/logs/hive.log
a2.sources.r2.shell = /bin/bash -c

# Describe the sink
a2.sinks.k2.type = hdfs
a2.sinks.k2.hdfs.path = hdfs://hadoop000:8020/flume/%Y%m%d/%H
#上传文件的前缀
a2.sinks.k2.hdfs.filePrefix = events-hive-
#是否按照时间滚动文件夹
a2.sinks.k2.hdfs.round = true
#多少时间单位创建一个新的文件夹
a2.sinks.k2.hdfs.roundValue = 1
#重新定义时间单位
a2.sinks.k2.hdfs.roundUnit = hour
#是否使用本地时间戳
a2.sinks.k2.hdfs.useLocalTimeStamp = true
#积攒多少个Event才flush到HDFS一次
a2.sinks.k2.hdfs.batchSize = 1000
#设置文件类型，可支持压缩
a2.sinks.k2.hdfs.fileType = DataStream
#多久生成一个新的文件
a2.sinks.k2.hdfs.rollInterval = 600
#设置每个文件的滚动大小
a2.sinks.k2.hdfs.rollSize = 134217700
#文件的滚动与Event数量无关
a2.sinks.k2.hdfs.rollCount = 0
#最小冗余数
a2.sinks.k2.hdfs.minBlockReplicas = 1


# Use a channel which buffers events in memory
a2.channels.c2.type = memory
a2.channels.c2.capacity = 1000
a2.channels.c2.transactionCapacity = 100

# Bind the source and sink to the channel
a2.sources.r2.channels = c2
a2.sinks.k2.channel = c2
```

#### 6.2.3、执行监控配置
>> $ bin/flume-ng agent --conf conf/ --name a2 --conf-file conf/flume-hdfs.conf 

### 6.3.Flume监听整个目录
#### 6.3.1 创建配置文件flume-dir.conf
>> $ cp -a flume-hdfs.conf flume-dir.conf

```
a3.sources = r3
a3.sinks = k3
a3.channels = c3

# Describe/configure the source
a3.sources.r3.type = spooldir
a3.sources.r3.spoolDir = /opt/modules/cdh/apache-flume-1.5.0-cdh5.3.6-bin/upload
a3.sources.r3.fileHeader = true
#忽略所有以.tmp结尾的文件，不上传
a3.sources.r3.ignorePattern = ([^ ]*\.tmp)

# Describe the sink
a3.sinks.k3.type = hdfs
a3.sinks.k3.hdfs.path = hdfs://hadoop000.20:8020/flume/upload/%Y%m%d/%H
#上传文件的前缀
a3.sinks.k3.hdfs.filePrefix = upload-
#是否按照时间滚动文件夹
a3.sinks.k3.hdfs.round = true
#多少时间单位创建一个新的文件夹
a3.sinks.k3.hdfs.roundValue = 1
#重新定义时间单位
a3.sinks.k3.hdfs.roundUnit = hour
#是否使用本地时间戳
a3.sinks.k3.hdfs.useLocalTimeStamp = true
#积攒多少个Event才flush到HDFS一次
a3.sinks.k3.hdfs.batchSize = 1000
#设置文件类型，可支持压缩
a3.sinks.k3.hdfs.fileType = DataStream
#多久生成一个新的文件
a3.sinks.k3.hdfs.rollInterval = 600
#设置每个文件的滚动大小
a3.sinks.k3.hdfs.rollSize = 134217700
#文件的滚动与Event数量无关
a3.sinks.k3.hdfs.rollCount = 0
#最小冗余数
a3.sinks.k3.hdfs.minBlockReplicas = 1


# Use a channel which buffers events in memory
a3.channels.c3.type = memory
a3.channels.c3.capacity = 1000
a3.channels.c3.transactionCapacity = 100

# Bind the source and sink to the channel
a3.sources.r3.channels = c3
a3.sinks.k3.channel = c3
```

#### 6.3.2、执行测试
>> $ bin/flume-ng agent --conf conf/ --name a3 --conf-file conf/flume-dir.conf &
				
#### 6.3.3、总结：
在使用Spooling Directory Source
注意事项：
    1、不要在监控目录中创建并持续修改文件
    2、上传完成的文件会以.COMPLETED结尾
    3、被监控文件夹每600毫秒扫描一次变动
