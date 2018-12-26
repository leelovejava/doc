# hadoop

## doc

[用大白话告诉你小白都能看懂的Hadoop架构原理](https://mp.weixin.qq.com/s/mEk3m4MOuOrP5yub2onCGg)

[亿级PV，常见性能优化策略总结与真实案例](https://mp.weixin.qq.com/s/rpSMOieVEQ9r3f55WlvkLQ)

## 简介
[传智大数据](http://www.itcast.cn/subject/cloudzly/index.shtml)

大数据5V特点:

1）Volume(大量) ['vɒljuːm]
    数据量大，包括采集、存储和计算的量都非常大。大数据的起始计量单位至少是P（1000个T）、E（100万个T）或Z（10亿个T）

2）Velocity(高速)[vəˈlɒsəti]
    数据增长速度快，处理速度也快，时效性要求高。比如搜索引擎要求几分钟前的新闻能够被用户查询到，个性化推荐算法尽可能要求实时完成推荐。这是大数据区别于传统数据挖掘的显著特征

3) Variety(多样)[və'raɪətɪ] 
    种类和来源多样化。包括结构化、半结构化和非结构化数据，具体表现为网络日志、音频、视频、图片、地理位置信息等等，多类型的数据对数据的处理能力提出了更高的要求

4) Value(低价值密度)['væljuː] 
    数据价值密度相对较低，或者说是浪里淘沙却又弥足珍贵。随着互联网以及物联网的广泛应用，信息感知无处不在，信息海量，但价值密度较低，如何结合业务逻辑并通过强大的机器算法来挖掘数据价值，是大数据时代最需要解决的问题

5) Veracity(速度)[vəˈræsəti] 
    实时推荐,提高订单转换率

[大数据5v指的是什么？——以沃尔玛为例](https://blog.csdn.net/goddess_ever_never/article/details/80063066)
    
## Overview
[hadoop官网](http://hadoop.apache.org)

[十年了，Hadoop的前世今生](https://blog.csdn.net/lfq1532632051/article/details/53219558)

### hadoop由来

项目作者的孩子对黄色大象玩具的命名

开源、分布式存储与分布式计算的平台

Hadoop能做什么：

1. 搭建大型数据仓库，PB级数据的存储、处理、分析、统计等业务

2. 搜索引擎、日志分析、数据挖掘、商业智能

## hadoop生态系统
* HDFS->分布式文件系统
* Yarn->分布式资源调度,xx on yarn,资源按需分配,提高集群资源的利用率
* Zookeeper->分布式协调服务,解决单点故障
* Flume->日志收集框架
* mahout-(数据挖掘库)机器学习,基于MapReduce的mahout停止维护,转向基于Spark
* Oozie->工作流程调度程序(工作流,A作业输出作为B作业的输出)
* Sqoop->数据同步工具,关系型数据库到hadoop生态上的数据交换工具
* Storm->分布式实时计算系统
* MapReduce->分布式计算框架
* Spark->快速，通用引擎用于大规模数据处理
* impala->实时查询数据
* Ambari->基于web的部署/管理/监控Hadoop集群的工具集
* Kafka->消息队列
* Phoenix->SQL中间层,构建于Hbase之上
* Hive->基于Hadoop的数据仓库
* pig->数据流处理,现基本不用
* Hbase->分布式列数据库
* R->用做统计分析

![image](https://github.com/leelovejava/doc/blob/master/img/hadoop/04-ecosystem.png)

### Hadoop
    名称由来：项目作者DougCutting的孩子对黄色大象玩具的命名
    开源、分布式存储与分布式计算的平台,分布式计算的基础架构    
    
    狭义Hadoop VS 广义Hadoop
    狭义:HDFS、MapReduce、YARN
    广义:Hadoop生态系统,每个子系统只解决某一个特定的问题领域,小而精 

### HDFS（分布式文件系统）
    1. 源于Google在2003年10月发表的GFS论文
    2. 对GFS的克隆
    3. 特点：扩展性、容错性、海量数据存储
    4. 将文件切分成指定大小的数据块并且多副本存于多个机器上
    5. 数据切分、多副本、容错对用户是透明的

![image](https://github.com/leelovejava/doc/blob/master/img/hadoop/02-hdfs.png)    
    
### YARN（资源管理）
    1. 整个集群资源的管理与调度
    2. 特点：扩展性、容错性、多框架资源统一调度 

![image](https://github.com/leelovejava/doc/blob/master/img/hadoop/01-yarn.png)    

### MapReduce（分布式计算框架）
    1. 2004年12月的GoogleMapReduce论文
    2. Google MapReduce的克隆版
    3. 特点：扩展性、容错性、海量数据的离线处理

### Hive
    最初用于解决海量结构化的日志数据统计问题.比如:统计访问量
    构建在hadoop之上的数据仓库
    定义一种类SQL的查询语言,HQL(类似但不完全相同)->hibernate HQL
    通常于进行离线数据处理
    底层采用多种执行引擎(Spark、Tez、MapReduce)
    支持多种不同的压缩格式(gzip snappy bzip2)、存储格式(TextFile SequenceFile RCFile ORC Parquet)、自定义函数(UDF)
    
![image](https://github.com/leelovejava/doc/blob/master/img/hadoop/03-mapreduce.png)        

## 特点：
1.开源、社区活跃
2.成熟，涉及范围广

## hadop常用发行版及选型
Apache Hadoop
CDH:Cloudera Distributed		70%
HDP:Hortonworks Data Platform	

http://archive.cloudera.com/cdh5/cdh/5/
cdh-5.7.0
生产或者测试环境选择对应CDH版本时，一定要采用尾号是一样的版本

## hadoop环境搭建

### doc
[hadoop官方单机安装](http://archive.cloudera.com/cdh5/cdh/5/hadoop/hadoop-project-dist/hadoop-common/SingleCluster.html)

### 步骤
1) 下载Hadoop
	[cdh下载地址](http://archive.cloudera.com/cdh5/cdh/5/)
	2.6.0-cdh5.7.0

	wget http://archive.cloudera.com/cdh5/cdh/5/hadoop-2.6.0-cdh5.7.0.tar.gz

2）安装jdk
	下载
	解压到app目录：tar -zxvf jdk-7u51-linux-x64.tar.gz -C ~/app/
	验证安装是否成功：~/app/jdk1.7.0_51/bin      ./java -version
	建议把bin目录配置到系统环境变量(~/.bash_profile)中
		export JAVA_HOME=/home/hadoop/app/jdk1.7.0_51
		export PATH=$JAVA_HOME/bin:$PATH
3）机器参数设置
	1. hostname: hadoop001
    2. 修改机器名: /etc/sysconfig/network
		NETWORKING=yes
		HOSTNAME=hadoop001
	3. 设置ip和hostname的映射关系: /etc/hosts
		192.168.199.200 hadoop001
		127.0.0.1 localhost
	4. ssh免密码登陆(本步骤可以省略，但是后面你重启hadoop进程时是需要手工输入密码才行)
		> ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
        > cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
        > ssh localhost
4）Hadoop配置文件修改: ~/app/hadoop-2.6.0-cdh5.7.0/etc/hadoop
	1. hadoop-env.sh
		export JAVA_HOME=/home/hadoop/app/jdk1.7.0_51
    2. core-site.xml
        hadoop1.0 9000 默认,hadoop2.0 8200默认
```
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop001:8020</value>
    </property>	
    
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/home/hadoop/app/tmp</value>
    </property>	
```
   3. hdfs-site.xml
```    
<property>
    <name>dfs.replication</name>
    <value>1</value>
</property>
```
5）格式化HDFS
	注意：这一步操作，只是在第一次时执行，每次如果都格式化的话，那么HDFS上的数据就会被清空
	bin/hdfs namenode -format

6）启动HDFS
	sbin/start-dfs.sh

	验证是否启动成功:
		jps
			DataNode
			SecondaryNameNode
			NameNode

		浏览器
			http://hadoop001:50070/
      3.0 http://hadoop001:9870/


7）停止HDFS
	sbin/stop-dfs.sh
	
8) window
[hadoop2.7.3的hadoop.dll和winutils.exe](https://download.csdn.net/download/chenxf10/9621093)
hadoop本地库
HADOOP_HOME	

## Spring for Apache Hadoop

### 1、[Overview](https://spring.io/projects/spring-hadoop)
Spring Hadoop简化了Apache Hadoop，提供了一个统一的配置模型以及简单易用的API来使用HDFS、MapReduce、Pig以及Hive。
还集成了其它Spring生态系统项目，如Spring Integration和Spring Batch

### 2、Features特点
* Support to create Hadoop applications that are configured using Dependency Injection and run as standard Java applications vs. using Hadoop command line utilities.
  >支持创建Hadoop应用，配置使用依赖注入和运行标准的java应用程序和使用Hadoop的命令行工具
* Integration with Spring Boot to simply creat Spring apps that connect to HDFS to read and write data.
  >集成Spring Boot，可以简单地创建Spring应用程序去连接HDFS进行读写数据
* Create and configure applications that use Java MapReduce, Streaming, Hive, Pig, or HBase
  >创建和配置，使用java的MapReduce，Streaming，Hive，Pig或HBase
* Extensions to Spring Batch to support creating Hadoop based workflows for any type of Hadoop Job or HDFS operation.
  >扩展Spring Batch支持创建基于Hadoop的工作流的任何类型的Hadoop Job或HDFS的操作
* Script HDFS operations using any JVM based scripting language.
  >脚本HDFS操作使用任何基于JVM的脚本语言
* Easily create custom Spring Boot based aplications that can be deployed to execute on YARN.
  >基于SpringBoot轻松地创建自定义的基础应用，应用可以部署在YARN上
* DAO support (Template & Callbacks) for HBase.
  >支持DAO，可以使用模板或回调的方式操作Hbase
* Support for Hadoop Security.
  >支持Hadoop安全验证

### 3、Spring Boot Config
```
<dependencies>
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-hadoop</artifactId>
        <version>2.5.0.RELEASE</version>
    </dependency>
</dependencies>
```

### 4、Quick start

### 5、Doc&Api
https://docs.spring.io/spring-hadoop/docs/2.5.0.RELEASE/reference/html/
https://docs.spring.io/spring-hadoop/docs/2.5.0.RELEASE/api/
http://blog.51cto.com/zero01/2094901?from=singlemessage

### 6、spring-hadoop
[Using a Windows client together with a Linux cluster](https://github.com/spring-projects/spring-hadoop/wiki/Using-a-Windows-client-together-with-a-Linux-cluster)


## Hadoop3.0

###端口改变
https://issues.apache.org/jira/browse/HDFS-9427

#### Namenode 端口: 
50470 --> 9871
50070 --> 9870
8020 --> 9820

#### Secondary NN 端口:
50091 --> 9869
50090 --> 9868

#### Datanode 端口: 
50020 --> 9867
50010 --> 9866
50475 --> 9865
50075 --> 9864

#####史上最快! 10小时大数据入门

[(一)-大数据概述](https://www.jianshu.com/p/e67f2cc89b83)

[(二)-初识Hadoop](https://www.jianshu.com/p/830765229fc2)

[(三)-分布式文件系统HDFS](https://www.jianshu.com/p/e35817bdc4a8)

[(四)-分布式资源调度YARN](https://www.jianshu.com/p/f59165b9c049)

[(五)-分布式计算框架MapReduce](https://www.jianshu.com/p/b39a50f070d2)

[(六)- Hadoop 项目实战](https://www.jianshu.com/p/c7b7ea540149)

[(七)- Hadoop 分布式环境搭建](https://www.jianshu.com/p/d7c47bcbbd01)

[(八)- Hadoop 集成 Spring 的使用](https://www.jianshu.com/p/89222ac3d84b)

[(九)- 前沿技术拓展Spark,Flink,Beam](https://www.jianshu.com/p/4d0341a4d7d7)

[(十)-Hadoop3.x新特性](https://www.jianshu.com/p/984a1939d03c)