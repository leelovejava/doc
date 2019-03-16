# Hbase

## Overview
[官网](http://hbase.apache.org/)
[下载](http://hbase.apache.org/downloads.html)
[学习时使用1.2.4版本,下载地址](http://archive.apache.org/dist/hbase/1.2.4/hbase-1.2.4-bin.tar.gz)

[HBase 默认配置](https://blog.csdn.net/u011414200/article/details/50427529)

## doc

[HBase Procedure解读](https://mp.weixin.qq.com/s/mpG5Gte-7g_Ue0Sr8VLS6w)

[HBase在人工智能场景的使用](https://mp.weixin.qq.com/s/zicVKfRH207b1Yw4NB315A)

[58HBase平台实践和应用—平台建设篇](https://mp.weixin.qq.com/s/Czx-tDEdN11srmGuA-4Jkw)
多租户支持、数据读写接口、数据导入导出和平台优化四个方面重点讲解了58HBase平台的建设

[HBase实战 | 排查HBase堆外内存溢出](https://mp.weixin.qq.com/s/JDgtbulwfPgnjRbJ-zrfug)

[HBase2实战之HBase Flink和Kafka整合](https://mp.weixin.qq.com/s/1UgPtCmP2t3vSfVsRX_OWA)

[从MySQL到HBase：分库分表方案转型的演进](https://mp.weixin.qq.com/s/aPaty1t30TxrRHieS64OWw)

项目实战 Hbase+Spring boot实战分布式文件存储

[云HBase全文索引服务，增强HBase的检索能力](https://mp.weixin.qq.com/s/e6Zv8jVfzJfVKdHBlfyAFw)

[HBase迁移 | HBase金融大数据乾坤大挪移](https://mp.weixin.qq.com/s/NmJBrGz_rTxR-spRtWTC8A)

[HBase实战 | 不中断业务，腾讯10P+金融数据跨机房迁移实战](https://mp.weixin.qq.com/s/e7CRCaUVWU8mhuKeL7ge_w)

[都是 HBase 上的 SQL 引擎，Kylin 和 Phoenix 有什么不同？](https://mp.weixin.qq.com/s/CQ9nlO14Do0Q0_Y6QU9jBw)

[HBase 读流程解析与优化的最佳实践](https://mp.weixin.qq.com/s/cj-HJNfZ2O7kCAFNL4l7Eg)

[HBase基本概念与基本使用](https://www.cnblogs.com/swordfall/p/8737328.html)

[hbase的rowkey设计](http://www.cnblogs.com/kxdblog/p/4328699.html)

### 官方翻译文档

http://abloz.com/hbase/book.html

https://www.jianshu.com/p/dfa7488c5414

[HBase原理和设计](https://www.toutiao.com/i6601011304679342596)

### 官方quick start翻译

http://hbase.apache.org/book.html#quickstart
https://www.cnblogs.com/LeslieXia/p/5743436.html

## 官方安装

[quick start](http://hbase.apache.org/book.html#quickstart)

[HBase完全分布式配置](https://m.imooc.com/article/details?article_id=27253)

[hbase使用外部（非自带）zookeeper搭建伪分布式环境](https://blog.csdn.net/xuedingkai/article/details/78816862)  

## NoSQL

Cassandra hbase mongodb 
Couchdb，文件存储数据库
Neo4j非关系型图数据库

## 简介
Hadoop Database，是一个高可靠性、高性能、面向列、可伸缩、实时读写的分布式数据库

利用Hadoop HDFS作为其文件存储系统,利用Hadoop MapReduce来处理HBase中的海量数据,利用Zookeeper作为其分布式协同服务

主要用来存储非结构化和半结构化的松散数据（列存 NoSQL 数据库）

来源谷歌的三篇论文之big table

## 数据模型

row key:行键,相当于mysql主键
        最关键的是row key的设计
        决定一行数据
        按照字典顺序排序
        只能存储64k的字节数据

timestamep:时间戳,是版本号
            
CF:  列族,HBASE表中的每个列，都归属于某个列族。
     列族是表的schema的一部分(而列不是)，必须在使用表之前定义。
     列名都以列族作为前缀。例如courses：history，courses：math 都属于courses这个列族
     权限控制、存储以及调优都是在列族层面进行

cell:单元格,由行、列的坐标交叉决定
     实际保存数据
     单元格的内容是未解析的字节数组
     
Hlog(WAL log)
    记录操作和数据(可恢复数据)

## 配置
1.
```
# 配置Java环境
export JAVA_HOME=/usr/local/java
# fasle禁用自带的zookeeper
export HBASE_MANAGES_ZK=false
```
2. 拷贝ZOOKEEPER_HOME/conf/zoo.cfg到HBASE_HOME/conf/,官方推荐
或者修改 conf/hbase-site.xml

hbase-site.xml
```
<!-- hbase数据存放的目录，若用本地目录，必须带上file://,否则hbase启动不起来 --> 
 <property>
    <name>hbase.rootdir</name>
    <value>file:///usr/local/hbase/rootdir</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/usr/local/zookeeper/dataDir</value>
  </property>
  <!-- Zookeeper集群的地址列表 -->
  <property>
    <name>hbase.zookeeper.quorum</name>
    <value>localhost</value>
    <description>the pos of zk</description>
  </property>

  <property>
    <name>hbase.unsafe.stream.capability.enforce</name>
    <value>false</value>
    <description>
      Controls whether HBase will check for stream capabilities (hflush/hsync).

      Disable this if you intend to run on LocalFileSystem, denoted by a rootdir
      with the 'file://' scheme, but be mindful of the NOTE below.

      WARNING: Setting this to false blinds you to potential data loss and
      inconsistent system state in the event of process and/or node failures. If
      HBase is complaining of an inability to use hsync or hflush it's most
      likely not a false positive.
    </description>
  </property>
  
  <!-- 此处必须为true，不然hbase仍用自带的zk，若启动了外部的zookeeper，会导致冲突，hbase启动不起来 -->
  <!-- 开启集群运行方式 --> 
  <property>
     <name>hbase.cluster.distributed</name>
     <value>true</value>
  </property>

  <!-- hbase主节点的位置 -->
  <property>
    <name>hbase.master</name>
    <value>localhost:60000</value>
  </property>
```
3. 启动进程
/usr/local/hbase/bin/start-hbase.sh
 
4.浏览器访问
> http://localhost:16010/

hbase.regionserver.port
60020->16020

#### 启动遇到的问题
1. ERROR [main] master.HMasterCommandLine: Master exiting
   java.lang.NoClassDefFoundError: org/apache/htrace/SamplerBuilder
   解决办法:
   cp $HBASE_HOME/lib/client-facing-thirdparty/htrace-core-3.1.0-incubating.jar $HBASE_HOME/lib/    
2. File /hbase/.tmp/hbase.version could only be replicated to 0 nodes instead of minReplica
   问题定位:
   datanode没有启动
   可通过jps命令查看启动状态
   
   查看日志 logs/hadoop-root-datanode-hadoop001.log
   异常信息:
   java.io.IOException: Incompatible clusterIDs in /usr/local/hadoop/data/dfs/data: namenode clusterID = CID-3ee7da46-fc08-4fb5-810f-03ded0cdd9a7; datanode clusterID = CID-6ed4b66b-d177-42ec-8f68-efab7881b38a
   ERROR org.apache.hadoop.hdfs.server.datanode.DataNode: Initialization failed for Block pool <registering> (Datanode Uuid 1141965e-c710-402a-b176-cf0d27087a69) service to hadoop001/0.0.0.0:9000. Exiting. 
   java.io.IOException: All specified directories have failed to load.
   
   dfs.namenode.name.dir配置有误,应为file:///
3. org.apache.hadoop.hdfs.server.datanode.DataNode: Block pool ID needed, but service not yet registered with NN, trace   
4. org.apache.hadoop.hbase.NotServingRegionException: Region is not online 错误
    问题分析:regionserver 挂掉或者其他原因导致连接不上regionserver,原因:数据损坏大致当前数据存放的regin无法使用
    解决办法:关闭ZooKeeper，删除zookeeper的dataDir路径下的version-2文件夹，然后重启
5. java.lang.NoSuchMethodException: org.apache.hadoop.fs.LocalFileSystem.setStoragePolicy(org.apache.hadoop.fs.Path, java.lang.String)

6. ERROR: Can't get master address from ZooKeeper; znode data == null
运行hbase(zookeeper)的用户无法写入zookeeper文件，导致znode data为空
 <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/home/hadoop/app/tmp/zk</value>
  </property>
  
## hbase shell

### 开启
bin/hbase shell

### Create a table
create 'test', 'cf'

### List Information About your Table
list 'test'

### Now use the describe command to see details, including configuration defaults
describe 'test'

### Put data into your table
put 'test', 'row1', 'cf:a', 'value1'
put 'test', 'row2', 'cf:b', 'value2'
put 'test', 'row3', 'cf:c', 'value3'

### Scan the table for all data at once
scan 'test'

### Disable a table
disable 'test'
enable 'test'

### Drop the table.
drop 'test'

### Exit the HBase Shell
quit

### hbase原理和实战
* HBase的Java Api
* HBase配置类-HBaseConfiguration
* HBase管理Admin类-HBaseAdmin
* HBase Table操作类-Table
* HBase添加操作数据模型-PUT
* HBase单个查询操作数据模型-Get
* HBase Scan检索操作数据模型-Scan
* HBase查询的结果模型-Result
* HBase检索结果模型-ResultScanner

### java操作API
* HbaseAdmin
 只需配置zookeepr地址
 configuration.set("hbase.zookeeper.quorum", "192.168.109.131:2181");
* HBaseAdmin表管理类
* HTableDescriptor表描述类
* HColumnDescriptorHColumnDescriptor
* 创建表
* 删除表
* HTable创建表的类
* 单条插入Put
* 批量插入
* 查询两种方式:Get/Scan
* Table中Family和Qualifier的关系与区别

### HBase过滤器

#### 作用
 多个维度过滤数据(行、列、数据版本)
 
#### 分类:

##### 基于行的过滤器 

* PrefixFilter:行的前缀过滤器
* PageFilter:基于行的分页

##### 基于列的过滤器

* ColumnPrefixFilter:列前缀匹配
* FirstKeyOnlyFilter:只返回每一行的第一列

##### 基于单元值的过滤器

* KeyOnlyFilter:只返回的数据不包括单元值,只包含行键与列
* TimestampsFilter:根据数据的时间戳版本进行过滤

##### 基于列和单元值的过滤器

* SingleColumnValueFilter:对该列的单元值进行比较过滤
* SingleColumnValueExcludeFilter:对该值的单元值进行比较过滤

##### 比较过滤器
* 比较过滤器通常需要一个比较运算符以及一个比较器来实现过滤,比如说RowFilter、FamilyFilter、QualifierFilter、ValueFilter

##### 综合过滤器
* FilterList:综合使用多个过滤器

##### 自定义过滤器

### 原理

### 写流程
client->zookeeper
client->rgionServer->memStore->StoreFile

1).Client会先访问zookeeper,得到对应的regionServer地址(表信息)
2).Client对RegionServer发起写请求,RegionServer接收数据写入内存(检查各种操作,例如是否只读)
3).当MemStore的大小达到一定的值后,flush到storeFile并存储到HDFS(合并机制)

RegionServer:处理数据的输入和输出请求,管理多个Region
Region:hbase存储的基本单元,有对应的HLog实例,一个Region,只存储列族的数据
一个Region宝行,一个store对象,一个store,对应一个mem、一个或多个Store File
mem:是数据在内存中的实体,是有序的,当有数据写入时,会先写入到memStore,当memStor的大小达到上限之后,store会创建storeFile,storeFile是hStoreFile的一层封装,storeFile的数据会最终写入到HFile之中
HFile:hbase依赖禹hdfs,存储在HDFS,默认3个副本,是wal的一种实现(Write Ahead Log 预写日志,是事务中一致性的常见实现),每个HFile都有一个HLog的实例,先写日志在写内存(hbase先写内存再写日志)

### 读流程
scan
Client->Zookeeper
Client->RegionServer

1).Client会先访问Zookeeper,得到对应的RegionServer地址
2).Client对RegionServer发起读请求
3).当RegionServer收到client的读请求后,先扫描自己的MemStore,再扫描BlockCache(加速读内容缓存区)如果还没找到则StoreFile中读取数据,然后数据返回给Client

## Hbase的协作模块
HMaster启动,注册到Zookeeper,等待RegionServer汇报,
RegionServer注册到Zookeeper,并向HMaster汇报
对各个RegionServer(包括失效的)的数据进行整理,分配Region和meta信息

RegionServer失效:
HMaster将失效RegionServer上的Region分配到其他节点
HMaster更新hbase:meta表以保证数据正常访问

HMaster失效
处于Backup状态的其他HMaster节点推选出一个转为Active状态
数据能正常读写,但是不能创建删除表,也不能更改表结构


### hbase+protobuf
安装
```
tar -xzf protobuf-2.1.0.tar.gz 

cd protobuf-2.1.0 

./configure

make & install

# 查看安装目录
whereis protobuf

protoc --help
```

编译.proto文件
> vim phone.proto
```
package com.leelovejava.hbase; 
message PhoneDetail 
{ 
    required string     dnum = 1; 
    required string    length = 2; 
    required string     type = 3; 
    required string     date = 4; 
}
```
--java_out=OUT_DIR          Generate Java source file.
PS: 类名不能和文件同名
> protoc phone.proto --java_out=/home/hadoop/data/protobuf/

嵌套 Message
```
package com.leelovejava.hbase;
message PhoneDetail2 { 
    required string dnum = 1; 
    required string length = 2; 
    required string type = 3;
    required string date = 4;  
} 

message dayPhoneDetail { 
   repeated PhoneDetail2 dayPhoneDetail = 1;
}
```

## hbase优化

[表的设计](https://www.cnblogs.com/panfeng412/archive/2012/03/08/hbase-performance-tuning-section1.html)

[写表操作](https://www.cnblogs.com/panfeng412/archive/2012/03/08/hbase-performance-tuning-section2.html)

[读表操作](https://www.cnblogs.com/panfeng412/archive/2012/03/08/hbase-performance-tuning-section3.html)

[数据计算](https://www.cnblogs.com/panfeng412/archive/2012/03/08/hbase-performance-tuning-section4.html)


flush 'tableName'

split 'tableName'

1). 表设计
    列族:1-2个