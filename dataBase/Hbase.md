# Hbase

##Overview
[官网](http://hbase.apache.org/)
[下载](http://hbase.apache.org/downloads.html)
[学习时使用1.2.4版本,下载地址](http://archive.apache.org/dist/hbase/1.2.4/hbase-1.2.4-bin.tar.gz)

##doc

###官方翻译文档
http://abloz.com/hbase/book.html
https://www.jianshu.com/p/dfa7488c5414
[HBase原理和设计](https://www.toutiao.com/i6601011304679342596)

###官方quick start翻译
http://hbase.apache.org/book.html#quickstart
https://www.cnblogs.com/LeslieXia/p/5743436.html

##官方安装
[quick start](http://hbase.apache.org/book.html#quickstart)

[HBase完全分布式配置](https://m.imooc.com/article/details?article_id=27253)
###配置
1. hbase-core.xml
```
<configuration>
  <!-- hbase 在hdfs的root目录 -->
  <property>
    <name>hbase.rootdir</name>
    <value>file:///usr/local/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/usr/local/zookeeper/dataDir</value>
  </property>
  <!-- hbase 是否为分布式 -->
  <property>
    <name>hbase.unsafe.stream.capability.enforce</name>
    <value>false</value>
   </property> 
</configuration>
```
2. 配置文件中所记录的 hbase 存放元文件来创建路径
mkdir -p /usr/local/hbase/metadata/hbase
3. conf/hbase-env.sh
```
export JAVA_HOME=/usr/local/java
# fasle禁用自带的zookeeper
export HBASE_MANAGES_ZK=false
```
4. 拷贝ZOOKEEPER_HOME/conf/zoo.cfg到HBASE_HOME/conf/,官方推荐
5. 启动进程
/usr/local/hbase/bin/start-hbase.sh
6. [浏览器访问](http://192.168.109.131:16010/)
7. 数据存储至hdfs
hbase-core.xml
  <property>
    <name>hbase.rootdir</name>
    <value>hdfs://hadoop001:9000/hbase</value>
  </property>
8. [hbase使用外部（非自带）zookeeper搭建伪分布式环境](https://blog.csdn.net/xuedingkai/article/details/78816862)  
hbase-env.sh
```
export HBASE_MANAGES_ZK=false
```
hbase-site.xml
```
<!-- hbase数据存放的目录，若用本地目录，必须带上file://,否则hbase启动不起来 --> 
 <property>
    <name>hbase.rootdir</name>
    <value>file:///usr/local/hbase/rootdir</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/usr/localhost/zookeeper/dataDir</value>
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
#####基于行的过滤器 
* PrefixFilter:行的前缀过滤器
* PageFilter:基于行的分页

#####基于列的过滤器
* ColumnPrefixFilter:列前缀匹配
* FirstKeyOnlyFilter:只返回每一行的第一列

#####基于单元值的过滤器
* KeyOnlyFilter:只返回的数据不包括单元值,只包含行键与列
* TimestampsFilter:根据数据的时间戳版本进行过滤

#####基于列和单元值的过滤器
* SingleColumnValueFilter:对该列的单元值进行比较过滤
* SingleColumnValueExcludeFilter:对该值的单元值进行比较过滤

#####比较过滤器
* 比较过滤器通常需要一个比较运算符以及一个比较器来实现过滤,比如说RowFilter、FamilyFilter、QualifierFilter、ValueFilter

#####综合过滤器
* FilterList:综合使用多个过滤器

#####自定义过滤器

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

##.Hbase的协作模块
HMaster启动,注册到Zookeeper,等待RegionServer汇报,
RegionServer注册到Zookeeper,并向HMaster汇报
对各个RegionServer(包括失效的)的数据进行整理,分配Region和meta信息

RegionServer失效:
HMaster将失效RegionServer上的Region分配到其他节点
HMaster更新hbase:meta表以保证数据正常访问

HMaster失效
处于Backup状态的其他HMaster节点推选出一个转为Active状态
数据能正常读写,但是不能创建删除表,也不能更改表结构

##Phoenix-HBase中间层

####doc
[Phoenix综述（史上最全Phoenix中文文档）](https://www.cnblogs.com/linbingdong/p/5832112.html)

#### 简介
* 构建于Apache Hbase之上的SQL中间层
* 可以在Apache HBase上执行SQL查询,性能强劲
* 较完善的查询支持,支持二级索引,查询效率较高

![avatar](http://phoenix.apache.org/images/using/all.png)

#### 优势
* Put the SQL back in NoSQL
* 具有完整的ACID事务功能的标准SQL和JDBC API的强大功能
* 完全可以和其他Hadoop产品,例如Spark、Hive、Pig、Flume以及MapReduce集成

####Phoenix vs Hive
HBase的查询工具，如：Hive、Tez、Impala、Spark SQL、Phoenix

![image](http://phoenix.apache.org/images/PhoenixVsHive.png）
#### Phoenix比HBase快的原因
* 通过HBase协处理器,在服务端进行操作,从而最大限度的减少客户端和服务端的数据传输
* 通过定制的过滤器对数据的处理
* 使用本地的HBase Api而不是通过MapReduce框架,从而最大限度的降低启动成本

#### 功能特性
* 多租户
* 二级索引
* 用户定义函数
* 行时间戳列
* 分页查询
* 视图

#### 安装&部署
1、下载&解压
http://phoenix.apache.org/download.html
[镜像站点](http://www.apache.org/dyn/closer.lua/phoenix/)
http://archive.apache.org/dist/phoenix/
和hbase对应版本
wget http://mirrors.hust.edu.cn/apache/phoenix/apache-phoenix-5.0.0-HBase-2.0/bin/apache-phoenix-5.0.0-HBase-2.0-bin.tar.gz
2、复制phoenix-core-5.0.0-HBase-2.0.jar/phoenix-5.0.0-HBase-2.0-server.jar到hbase regionServer的lib
3、增加hbase-site.xml 配置
```
<property>
    <name>hbase.table.sanity.checks</name>
    <value>false</value>
</property>
```

编译源码安装
1) 安装git maven
2) 下载CDH版的Phoenix
https://github.com/chiastic-security/phoenix-for-cloudera/tree/4.8-HBase-1.2-cdh5.8
3)  编译
mvn clean package -DskipTests

https://www.cnblogs.com/zlslch/p/7096402.html
#### shell命令
##### 进入
bin/sqlline.py
bin/sqlline.py hadoop000
bin/sqlline.py 127.0.0.1:2181
##### 常用命令
创建表
    create table if not exists PERSON(ID INTEGER NOT NULL PRIMARY KEY,NAME VARCHAR(20),AGE INTEGER);
查看所有表
    !tables
查询
    select * fro PERSON;                   
插入数据    
    upsert into PERSON(ID,NAME,AGE) values(1,'Bella',27); 
    upsert into PERSON(ID,NAME,AGE) values(2,'Anne',18);
    upsert into PERSON(ID,NAME,AGE) values(3,'Colin',25);
    upsert into PERSON(ID,NAME,AGE) values(4,'Jerry',30);
查看表信息
    !describe tables_name
修改表结构  
    alter table PERSON add sex varchar(10);         
删除表
    DROP TABLE tables_name
修改数据
    update PERSON set sex='男' where ID=1;              
删除表结构
    drop table "person";  
创建表某一列索引
    create index "person_index" on "person"("cf"."name");   
模糊查找
    select count(*) from table_name where TIMESTAMP  like '2016-07-03%';
删除索引
    drop index "person_index" on "person"                   
删除表中数据
    delete from "person" where name='zhangsan';
修改表中数据              
    upsert into "person"(id,sex) values(1, '女');            
case when
    select (case name when 'zhangsan' then 'sansan' when 'lisi' then 'sisi' else name end)as showname from "person";
退出
    !quit                 
关联hbase中已经存在的表
    create view "test"(id varchar not null primary key, "cf1"."name" varchar, "cf1"."age" varchar, "cf1"."sex" varchar);
    注意：
    （1）如果不加列族会报错如下：
    Error: ERROR 505 (42000): Table is read only. (state=42000,code=505)
    （2）如果不加双引号则会匹配不到hbase表中的字段，结果就是虽然关联上数据库但是没有值！！！
    （3）关联的时候，Phoenix建表最好都是varchar类型，不容易出错
    （4）最好创建view视图，不要创建table表格。因为Phoenix端删除table会连带删除hbase表格，如果是view则不会。  

####java api操作
java调用Hbase java.net.SocketTimeoutException
 将java连接hbase的IP地址添加到windows下C:\Windows\System32\drivers\etc\hosts文件中   
 
###squirrel
#### 简介
 windows的Phoenix可视化工具
#### 使用 
[下载地址](http://www.squirrelsql.org/)
https://www.jianshu.com/p/9d3e938081d2
java -jar squirrel-sql-3.8.1-standard.jar