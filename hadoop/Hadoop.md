# hadoop
## Overview
[hadoop官网](http://hadoop.apache.org)

## hadoop生态系统
* HDFS->分布式文件系统
* MapReduce->分布式计算框架
* Spark->快速，通用引擎用于大规模数据处理
* Yarn->分布式资源调度
* Zookeeper->分布式协调服务
* pig->数据流处理
* Flume->日志收集框架
* mahout-(数据挖掘库)机器学习
* Oozie->工作流程调度程序(工作流,A作业输出作为B作业的输出)
* Sqoop->数据同步工具
* Storm->分布式实时计算系统
* impala->实时查询数据
* Ambari->基于web的部署/管理/监控Hadoop集群的工具集
* Kafka->消息队列
* Spark SQL
* Phoenix->SQL中间层,构建于Hbase之上
* Hive->基于Hadoop的数据仓库
* Hbase->分布式列数据库

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
		ssh-keygen -t rsa
		cp ~/.ssh/id_rsa.pub ~/.ssh/authorized_keys

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