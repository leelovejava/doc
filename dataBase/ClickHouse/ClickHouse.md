# ClickHouse

## 1. 大纲
* 什么是`ClickHouse`
* 列式存储
* `ClickHouse`安装
* 单机模式
* 分布式集群安装
* 整型、浮点型、布尔型
* 字符串、枚举类型、数组、元组、Date
* TinyLog、Memory、MergeTree、ReplacingMergeTree
* SummingMergeTree、Distributed
* 案例实操&常见问题总汇



## 2. 概述

[每天十亿级数据更新，秒出查询结果，ClickHouse在携程酒店的应用](https://mp.weixin.qq.com/s/BPRGyF1WFcGOeSISJTSRow)

[clickhouse中国社区](http://www.clickhouse.com.cn/)

`ClickHouse`是一款用于大数据实时分析的列式数据库管理系统，而非数据库。通过向量化执行以及对cpu底层指令集（SIMD）的使用，它可以对海量数据进行并行处理，从而加快数据的处理速度



`ClickHouse`是一个用于联机分析处理（OLAP）的列式数据库管理系统（columnar DBMS）

> 数据分析的目标则是探索并挖掘数据价值，作为企业高层进行决策的参考，通常被称为OLAP（On-Line Analytical Processing，联机分析处理）

> 从功能角度来看，OLTP负责基本业务的正常运转，而业务数据积累时所产生的价值信息则被OLAP不断呈现，企业高层通过参考这些信息会不断调整经营方针，也会促进基础业务的不断优化，这是OLTP与OLAP最根本的区别



## 3. 应用场景:

### 3.1 使用背景:

1）携程酒店每天有上千表，累计十多亿数据更新，如何保证数据更新过程中生产应用高可用；

2）每天有将近百万次数据查询请求，用户可以从粗粒度国家省份城市汇总不断下钻到酒店，房型粒度的数据，我们往往无法对海量的明细数据做进一步层次的预聚合，大量的关键业务数据都是好几亿数据关联权限，关联基础信息，根据用户场景获取不同维度的汇总数据；

3）为了让用户无论在app端还是pc端查询数据提供秒出的效果，我们需要不断的探索，研究找到最合适的技术框架



###  3.2 应用场景

* 1. 绝大多数请求都是用于读访问的

     > 数据高效压缩: 数据压缩空间大，减少io；处理单查询高吞吐量每台服务器每秒最多数十亿行
* 2. 数据需要以大批次（大于1000行）进行更新，而不是单行更新；或者根本没有更新操作

     > **多核并行处理**: 尽量做1000条以上批量的写入，避免逐行insert或小批量的insert，update，delete操作，因为ClickHouse底层会不断的做异步的数据合并，会影响查询性能，这个在做实时数据写入的时候要尽量避开
    
     > 索引非B树结构，不需要满足最左原则；只要过滤条件在索引列中包含即可；即使在使用的数据不在索引中，由于各种并行处理机制ClickHouse全表扫描的速度也很快
  
* 3. 数据只是添加到数据库，没有必要修改
* 4. 读取数据时，会从数据库中提取出大量的行，但只用到一小部分列
* 5. 表很“宽”，即表中包含大量的列

     > **向量化引擎**: 为了高效的使用CPU，数据不仅仅按列存储，同时还按向量进行处理(矢量 - 列)
* 6. 查询频率相对较低（通常每台服务器每秒查询数百次或更少）
* 7. 对于简单查询，允许大约50毫秒的延迟
* 8. 列的值是比较小的数值和短字符串（例如，每个URL只有60个字节）
* 9. 在处理单个查询时需要高吞吐量（每台服务器每秒高达数十亿行）
* 10. 不需要事务
* 11. 数据一致性要求较低
* 12. 每次查询中只会查询一个大表。除了一个大表，其余都是小表
* 13. 查询结果显著小于数据源。即数据有过滤或聚合。返回结果不超过单个服务器内存大小
  14.  写入速度非常快，50-200M/s，对于大量的数据更新非常适用；



### 3.3 ClickHouse 和一些技术的比较

1.商业OLAP数据库

例如：HP Vertica, Actian the Vector,

区别：ClickHouse是开源而且免费的



2.云解决方案

例如：亚马逊RedShift和谷歌的BigQuery

区别：ClickHouse可以使用自己机器部署，无需为云付费



3.Hadoop生态软件

例如：Cloudera Impala, Spark SQL, Facebook Presto , Apache Drill

区别：

​	ClickHouse支持实时的高并发系统

​	ClickHouse不依赖于Hadoop生态软件和基础

​	ClickHouse支持分布式机房的部署



4.开源OLAP数据库

例如：InfiniDB, MonetDB, LucidDB

区别：这些项目的应用的规模较小，并没有应用在大型的互联网服务当中，相比之下，ClickHouse的成熟度和稳定性远远超过这些软件。



5.开源分析，非关系型数据库

例如：Druid , Apache Kylin

区别：ClickHouse可以支持从原始数据的直接查询，ClickHouse支持类SQL语言，提供了传统关系型数据的便利



### 3.4  ClickHouse在酒店数据智能平台的实践

**3.4.1** **数据更新**



我们的主要数据源是Hive到ClickHouse，现在主要采用如下两种方式：



1）Hive到MySql，再导入到ClickHouse



初期在DataX不支持hive到ClickHouse的数据导入，我们是通过DataX将数据先导入mysql，再通过ClickHouse原生api将数据从mysql导入到ClickHouse。



为此我们设计了一套完整的数据导入流程，保证数据从hive到mysql再到ClickHouse能自动化，稳定的运行，并保证数据在同步过程中线上应用的高可用。



![img](https://mmbiz.qpic.cn/mmbiz_png/kEeDgfCVf1d3OXCPOme7qibOldKaVgk1wRHcvNg4mRxEYlHhU6eicUOQ278JZ7zEic8ESokJ4UVjrOcujLaRf6quA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



2）Hive到ClickHouse



DataX现在支持hive到ClickHouse，我们部分数据是通过DataX直接导入ClickHouse。但DataX暂时只支持导入，因为要保证线上的高可用，所以仅仅导入是不够的，还需要继续依赖我们上面的一套流程来做ReName，增量数据更新等操作。



针对数据高可用，我们对数据更新机制做了如下设计：



**全量数据导入流程**



![img](https://mmbiz.qpic.cn/mmbiz_png/kEeDgfCVf1d3OXCPOme7qibOldKaVgk1wT6JOrkY8TRicwKOXIXdSzSTO8Bek5hfFRLEo1TLIkf94S7780YRAj8w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



全量数据的导入过程比较简单，仅需要将数据先导入到临时表中，导入完成之后，再通过对正式表和临时表进行ReName操作，将对数据的读取从老数据切换到新数据上来。



**增量数据的导入过程**



![img](https://mmbiz.qpic.cn/mmbiz_png/kEeDgfCVf1d3OXCPOme7qibOldKaVgk1wR4fQ20xT9TTOiaNby2Q4C4YgXVlTV7me0JuwU87EiaD5J5Z9ULWRHIoA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



增量数据的导入过程，我们使用过两个版本。



由于ClickHouse的delete操作过于沉重，所以最早是通过删除指定分区，再把增量数据导入正式表的方式来实现的。



这种方式存在如下问题：一是在增量数据导入的过程中，数据的准确性是不可保证的，如果增量数据越多，数据不可用的时间就越长；二是ClickHouse删除分区的动作，是在接收到删除指令之后内异步执行，执行完成时间是未知的。如果增量数据导入后，删除指令也还在异步执行中，会导致增量数据也会被删除。最新版的更新日志说已修复这个问题。



针对以上情况，我们修改了增量数据的同步方案。在增量数据从Hive同步到ClickHouse的临时表之后，将正式表中数据反写到临时表中，然后通过ReName方法切换正式表和临时表。



通过以上流程，基本可以保证用户对数据的导入过程是无感知的。





**3.4.2** **数据导入过程的监控与预警**



由于数据量大，数据同步的语句经常性超时。为保证数据同步的每一个过程都是可监控的，我们没有使用ClickHouse提供的JDBC来执行数据同步语句，所有的数据同步语句都是通过调用ClickHouse的RestfulAPI来实现的。



调用RestfulAPI的时候，可以指定本次查询的QueryID。在数据同步语句超时的情况下，通过轮询来获得某QueryID的执行进度。这样保证了整个查询过程的有序运行。在轮询的过程中，会对异常情况进行记录，如果异常情况出现的频次超过阈值，JOB会通过短信给相关人员发出预警短信。





**3.4.3** **服务器分布与运维**



现在主要根据场景分国内，海外/供应商，实时数据，风控数据4个集群。每个集群对应的两到三台服务器，相互之间做主备，程序内部将查询请求分散到不同的服务器上做负载均衡。



假如某一台服务器出现故障，通过配置界面修改某个集群的服务器节点，该集群的请求就不会落到有故障的服务器上。如果在某个时间段某个特定的数据查询量比较大，组建虚拟集群，将所有的请求分散到其他资源富裕的物理集群上。



下半年计划把每个集群的两台机器分散到不同的机房，可以继续起到现有的主备，负载均衡的作用还能起到dr的作用。同时为了保障线上应用的高可用，我们会实现自动健康检测机制，针对突发异常的服务器自动拉出我们的虚拟集群。



![img](https://mmbiz.qpic.cn/mmbiz_png/kEeDgfCVf1d3OXCPOme7qibOldKaVgk1wuicDarEk0g1XX4MMcEkxqXG0kjJxPTGplZeazRibn4zpswmndQkq6lxA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



我们会监控每台服务器每天的查询量，每个语句的执行时间，服务器CPU，内存相关指标，以便于及时调整服务器上查询量比较高的请求到其他服务器。



![img](https://mmbiz.qpic.cn/mmbiz_png/kEeDgfCVf1d3OXCPOme7qibOldKaVgk1w7nkwrTUTIqrALyz7zibzx2MmIqyUCs6MT18fmAEK93R1E2iazc8jzZkQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![img](https://mmbiz.qpic.cn/mmbiz_png/kEeDgfCVf1d3OXCPOme7qibOldKaVgk1wWumfChDEGcib6n2EkpCLUnwDVytdHsqp6bYGXBgf87rUBaejfz7cH4Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



### 3.5. **ClickHouse使用探索**

1）关闭Linux虚拟内存。在一次ClickHouse服务器内存耗尽的情况下，我们Kill掉占用内存最多的Query之后发现，这台ClickHouse服务器并没有如预期的那样恢复正常，所有的查询依然运行的十分缓慢。



通过查看服务器的各项指标，发现虚拟内存占用量异常。因为存在大量的物理内存和虚拟内存的数据交换，导致查询速度十分缓慢。关闭虚拟内存，并重启服务后，应用恢复正常。



2）为每一个账户添加join_use_nulls配置。ClickHouse的SQL语法是非标准的，默认情况下，以Left Join为例，如果左表中的一条记录在右表中不存在，右表的相应字段会返回该字段相应数据类型的默认值，而不是标准SQL中的Null值。对于习惯了标准SQL的我们来说，这种返回值经常会造成困扰。



3）JOIN操作时一定要把数据量小的表放在右边，ClickHouse中无论是Left Join 、Right Join还是Inner Join永远都是拿着右表中的每一条记录到左表中查找该记录是否存在，所以右表必须是小表。



4）通过ClickHouse官方的JDBC向ClickHouse中批量写入数据时，必须控制每个批次的数据中涉及到的分区的数量，在写入之前最好通过Order By语句对需要导入的数据进行排序。无序的数据或者数据中涉及的分区太多，会导致ClickHouse无法及时的对新导入的数据进行合并，从而影响查询性能。



5）尽量减少JOIN时的左右表的数据量，必要时可以提前对某张表进行聚合操作，减少数据条数。有些时候，先GROUP BY再JOIN比先JOIN再GROUP BY查询时间更短。



6）ClickHouse版本迭代很快，建议用去年的稳定版，不能太激进，新版本我们在使用过程中遇到过一些bug，内存泄漏，语法不兼容但也不报错，配置文件并发数修改后无法生效等问题。



7）避免使用分布式表，ClickHouse的分布式表性能上性价比不如物理表高，建表分区字段值不宜过多，太多的分区数据导入过程磁盘可能会被打满。



8）服务器CPU一般在50%左右会出现查询波动，CPU达到70%会出现大范围的查询超时，所以ClickHouse最关键的指标CPU要非常关注。我们内部对所有ClickHouse查询都有监控，当出现查询波动的时候会有邮件预警。



9）查询测试Case有：6000W数据关联1000W数据再关联2000W数据sum一个月间夜量返回结果：190ms；2.4亿数据关联2000W的数据group by一个月的数据大概390ms。但ClickHouse并非无所不能，查询语句需要不断的调优，可能与查询条件有关，不同的查询条件表是左join还是右join也是很有讲究的



酒店数据智能平台从去年7月份(2018年)试点，到现在80%以上的业务都已接入ClickHouse。**满足每天十多亿的数据更新和近百万次的数据查询，支撑app性能98.3%在1秒内返回结果，pc端98.5%在3秒内返回结果。**



从使用的角度，查询性能不是数据库能相比的，从成本上也是远低于关系型数据库成本的，单机支撑40亿以上的数据查询毫无压力。与ElasticSearch，Redis相比ClickHouse可以满足我们大部分使用场景

## 4. 缺点

* 1. 不支持真正的删除/更新支持 不支持事务

* 2. 不支持二级索引

* 3. 有限的SQL支持，join实现与众不同

     > sql满足日常使用80%以上的语法，join写法比较特殊；最新版已支持类似sql的join，但性能不好；

* 4. 不支持窗口功能

* 5. 元数据管理需要人工干预维护

* 6. 不支持高并发，官方建议qps为100，可以通过修改配置文件增加连接数，但是在服务器足够好的情况下

> Clickhouse快是因为采用了并行处理机制，即使一个查询，也会用服务器一半的cpu去执行，所以ClickHouse不能支持高并发的使用场景，默认单查询使用cpu核数为服务器核数的一半，安装时会自动识别服务器核数，可以通过配置文件修改该参数
>
> 

## 5. 常见的SQL用法

```sql
-- 列出数据库列表
show databases;

-- 列出数据库中表列表
show tables;

-- 创建数据库
create database test;

-- 删除一个表
drop table if exists test.t1;

-- 创建第一个表
create /*temporary*/ table /*if not exists*/ test.m1 (
 id UInt16
,name String
) ENGINE = Memory
;
-- 插入测试数据
insert into test.m1 (id, name) values (1, 'abc'), (2, 'bbbb');

-- 查询
select * from test.m1;
```



## 6. 默认值

默认值 的处理方面， `ClickHouse` 中，默认值总是有的，如果没有显示式指定的话，会按字段类型处理：

* 数字类型:  0
* 字符串:    空字符串
* 数组:      空数组
* 日期:      0000-00-00
* 时间:      0000-00-00 00:00:00
注：NULLs 是不支持的



## 7. 数据类型

### 1.整型：UInt8,UInt16,UInt32,UInt64,Int8,Int16,Int32,Int64
范围U开头-2N/2~2N-1;非U开头0～2^N-1



### 2.枚举类型：Enum8,Enum16

Enum('hello'=1,'test'=-1),Enum是有符号的整型映射的，因此负数也是可以的



### 3.字符串型：FixedString(N),String

N是最大字节数，不是字符长度，如果是UTF8字符串，那么就会占3个字节，GBK会占2字节;String可以用来替换VARCHAR,BLOB,CLOB等数据类型



### 4.时间类型：Date



### 5.数组类型：Array(T)

T是一个基本类型，包括arry在内，官方不建议使用多维数组



### 6.元组：Tuple



### 7.结构：Nested(name1 Type1,name2 Type2,...)
类似一种map的结



## 8. 引擎/engine

------

引擎是clickhouse设计的精华部分

### TinyLog

------

最简单的一种引擎，每一列保存为一个文件，里面的内容是压缩过的，不支持索引
 这种引擎没有并发控制，所以，当你需要在读，又在写时，读会出错。并发写，内容都会坏掉。

`应用场景:`
 a. 基本上就是那种只写一次
 b. 然后就是只读的场景。
 c. 不适用于处理量大的数据，官方推荐，使用这种引擎的表最多 100 万行的数据

```
drop table if exists test.tinylog;
create table test.tinylog (a UInt16, b UInt16) ENGINE = TinyLog;
insert into test.tinylog(a,b) values (7,13);
```

此时`/var/lib/clickhouse/data/test/tinylog`保存数据的目录结构：

```
├── a.bin
├── b.bin
└── sizes.json
```

a.bin 和 b.bin 是压缩过的对应的列的数据， sizes.json 中记录了每个 *.bin 文件的大小



### Log

------

这种引擎跟 TinyLog 基本一致
 它的改进点，是加了一个 __marks.mrk 文件，里面记录了每个数据块的偏移
 这样做的一个用处，就是可以准确地切分读的范围，从而使用并发读取成为可能
 但是，它是不能支持并发写的，一个写操作会阻塞其它读写操作
 Log 不支持索引，同时因为有一个 __marks.mrk 的冗余数据，所以在写入数据时，一旦出现问题，这个表就废了

`应用场景:`
 同 TinyLog 差不多，它适用的场景也是那种写一次之后，后面就是只读的场景，临时数据用它保存也可以

```
drop table if exists test.log;
create table test.log (a UInt16, b UInt16) ENGINE = Log;
insert into test.log(a,b) values (7,13);
```

此时`/var/lib/clickhouse/data/test/log`保存数据的目录结构：

```
├── __marks.mrk
├── a.bin
├── b.bin
└── sizes.json
```



### Memory

------

内存引擎，数据以未压缩的原始形式直接保存在内存当中，服务器重启数据就会消失
 可以并行读，读写互斥锁的时间也非常短
 不支持索引，简单查询下有非常非常高的性能表现

`应用场景:`
 a. 进行测试
 b. 在需要非常高的性能，同时数据量又不太大（上限大概 1 亿行）的场景



### Merge

------

一个工具引擎，本身不保存数据，只用于把指定库中的指定多个表链在一起。
 这样，读取操作可以并发执行，同时也可以利用原表的索引，但是，此引擎不支持写操作
 指定引擎的同时，需要指定要链接的库及表，库名可以使用一个表达式，表名可以使用正则表达式指定

```sql
create table test.tinylog1 (id UInt16, name String) ENGINE=TinyLog;
create table test.tinylog2 (id UInt16, name String) ENGINE=TinyLog;
create table test.tinylog3 (id UInt16, name String) ENGINE=TinyLog;

insert into test.tinylog1(id, name) values (1, 'tinylog1');
insert into test.tinylog2(id, name) values (2, 'tinylog2');
insert into test.tinylog3(id, name) values (3, 'tinylog3');

use test;
create table test.merge (id UInt16, name String) ENGINE=Merge(currentDatabase(), '^tinylog[0-9]+');
select _table,* from test.merge order by id desc
```

┌─_table───┬─id─┬─name─────┐
 │ tinylog3 │  3 │ tinylog3 │
 │ tinylog2 │  2 │ tinylog2 │
 │ tinylog1 │  1 │ tinylog1 │
 └──────────┴────┴──────────┘

`注：`_table 这个列，是因为使用了 Merge 多出来的一个的一个虚拟列

> a. 它表示原始数据的来源表，它不会出现在 `show table` 的结果当中
>  b. `select *` 不会包含它



### Distributed

------

与 Merge 类似， Distributed 也是通过一个逻辑表，去访问各个物理表，设置引擎时的样子是：

```sql
Distributed(remote_group, database, table [, sharding_key])
```

其中：

> `remote_group` /etc/clickhouse-server/config.xml中remote_servers参数
>  `database` 是各服务器中的库名
>  `table` 是表名
>  `sharding_key` 是一个寻址表达式，可以是一个列名，也可以是像 rand() 之类的函数调用，它与 remote_servers 中的 weight 共同作用，决定在 写 时往哪个 shard 写

配置文件中的 `remote_servers`

```xml
<remote_servers>
   <log>
       <shard>
           <weight>1</weight>
           <internal_replication>false</internal_replication>
           <replica>
               <host>172.17.0.3</host>
               <port>9000</port>
           </replica>
       </shard>
       <shard>
           <weight>2</weight>
           <internal_replication>false</internal_replication>
           <replica>
               <host>172.17.0.4</host>
               <port>9000</port>
           </replica>
       </shard>
   </log>
</remote_servers>
```

> `log` 是某个 shard 组的名字，就是上面的 remote_group 的值
>  `shard` 是固定标签
>  `weight` 是权重，前面说的 sharding_key 与这个有关。
>  简单来说，上面的配置，理论上来看:
>  第一个 shard “被选中”的概率是 1 / (1 + 2) ，第二个是 2 / (1 + 2) ，这很容易理解。但是， sharding_key 的工作情况，是按实际数字的“命中区间”算的，即第一个的区间是 [0, 1) 的周期，第二个区间是 [1, 1+2) 的周期。比如把 sharding_key 设置成 id ，当 id=0 或 id=3 时，一定是写入到第一个 shard 中，如果把 sharding_key 设置成 rand() ，那系统会对应地自己作一般化转换吧，这种时候就是一种概率场景了。
>  `internal_replication` 是定义针对多个 replica 时的写入行为的。
>  如果为 false ，则会往所有的 replica 中写入数据，但是并不保证数据写入的一致性，所以这种情况时间一长，各 replica 的数据很可能出现差异。如果为 true ，则只会往第一个可写的 replica 中写入数据（剩下的事“物理表”自己处理）。
>  `replica` 就是定义各个冗余副本的，选项有 host ， port ， user ， password 等

看一个实际的例子，我们先在两台机器上创建好物理表并插入一些测试数据：

```
create table test.tinylog_d1(id UInt16, name String) ENGINE=TinyLog;
insert into test.tinylog_d1(id, name) values (1, 'Distributed record 1');
insert into test.tinylog_d1(id, name) values (2, 'Distributed record 2');
```

在其中一台创建逻辑表：

```
create table test.tinylog_d (id UInt16, name String) ENGINE=Distributed(log, test,tinylog_d1 , id);

-- 插入数据到逻辑表，观察数据分发情况
insert into test.tinylog_d(id, name) values (0, 'main');
insert into test.tinylog_d(id, name) values (1, 'main');
insert into test.tinylog_d(id, name) values (2, 'main');

select name,sum(id),count(id) from test.tinylog_d group by name;
```

> `注：`逻辑表中的写入操作是异步的，会先缓存在本机的文件系统上，并且，对于物理表的不可访问状态，并没有严格控制，所以写入失败丢数据的情况是可能发生的



### Null

------

空引擎，写入的任何数据都会被忽略，读取的结果一定是空。

但是注意，虽然数据本身不会被存储，但是结构上的和数据格式上的约束还是跟普通表一样是存在的，同时，你也可以在这个引擎上创建视图



### Buffer

------

`1.`Buffer 引擎，像是Memory 存储的一个上层应用似的（磁盘上也是没有相应目录的）
 `2.`它的行为是一个缓冲区，写入的数据先被放在缓冲区，达到一个阈值后，这些数据会自动被写到指定的另一个表中
 `3.`和Memory 一样，有很多的限制，比如没有索引
 `4.`Buffer 是接在其它表前面的一层，对它的读操作，也会自动应用到后面表，但是因为前面说到的限制的原因，一般我们读数据，就直接从源表读就好了，缓冲区的这点数据延迟，只要配置得当，影响不大的
 `5.`Buffer 后面也可以不接任何表，这样的话，当数据达到阈值，就会被丢弃掉

一些特点：

- 如果一次写入的数据太大或太多，超过了 max 条件，则会直接写入源表。
- 删源表或改源表的时候，建议 Buffer 表删了重建。
- “友好重启”时， Buffer 数据会先落到源表，“暴力重启”， Buffer 表中的数据会丢失。
- 即使使用了 Buffer ，多次的小数据写入，对比一次大数据写入，也 慢得多 （几千行与百万行的差距）

```
-- 创建源表
create table test.mergetree (sdt  Date, id UInt16, name String, point UInt16) ENGINE=MergeTree(sdt, (id, name), 10);
-- 创建 Buffer表
-- Buffer(database, table, num_layers, min_time, max_time, min_rows, max_rows, min_bytes, max_bytes)
create table test.mergetree_buffer as test.mergetree ENGINE=Buffer(test, mergetree, 16, 3, 20, 2, 10, 1, 10000);

insert into test.mergetree (sdt, id, name, point) values ('2017-07-10', 1, 'a', 20);
insert into test.mergetree_buffer (sdt, id, name, point) values ('2017-07-10', 1, 'b', 10);
select * from test.mergetree;
select '------';
select * from test.mergetree_buffer;
```

`database`  数据库
 `table` 源表，这里除了字符串常量，也可以使用变量的。
 `num_layers` 是类似“分区”的概念，每个分区的后面的 min / max 是独立计算的，官方推荐的值是 16 。
 `min / max` 这组配置荐，就是设置阈值的，分别是 时间（秒），行数，空间（字节）。

`阈值的规则:` 是“所有的 min 条件都满足， 或 至少一个 max 条件满足”。

如果按上面我们的建表来说，所有的 min 条件就是：过了 3秒，2条数据，1 Byte。一个 max 条件是：20秒，或 10 条数据，或有 10K



### Set

------

Set 这个引擎有点特殊，因为它只用在 IN 操作符右侧，你不能对它 select

```sql
create table test.set(id UInt16, name String) ENGINE=Set;
insert into test.set(id, name) values (1, 'hello');
-- select 1 where (1, 'hello') in test.set; -- 默认UInt8 需要手动进行类型转换
select 1 where (toUInt16(1), 'hello') in test.set;
```

> `注:` Set 引擎表，是全内存运行的，但是相关数据会落到磁盘上保存，启动时会加载到内存中。所以，意外中断或暴力重启，是可能产生数据丢失问题的



### Join

------

```
TODO
```



### MergeTree

------

这个引擎是 ClickHouse 的`重头戏`，它支持`一个日期和一组主键的两层式索引`，还可以`实时更新数据`。同时，索引的粒度可以自定义，外加直接支持采样功能

```
MergeTree(EventDate, (CounterID, EventDate), 8192)
MergeTree(EventDate, intHash32(UserID), (CounterID, EventDate, intHash32(UserID)), 8192)
```

`EventDate` 一个日期的列名
 `intHash32(UserID)` 采样表达式
 `(CounterID, EventDate)` 主键组（里面除了列名，也支持表达式），也可以是一个表达式
 `8192` 主键索引的粒度

```
drop table if exists test.mergetree1;
create table test.mergetree1 (sdt  Date, id UInt16, name String, cnt UInt16) ENGINE=MergeTree(sdt, (id, name), 10);

-- 日期的格式，好像必须是 yyyy-mm-dd
insert into test.mergetree1(sdt, id, name, cnt) values ('2018-06-01', 1, 'aaa', 10);
insert into test.mergetree1(sdt, id, name, cnt) values ('2018-06-02', 4, 'bbb', 10);
insert into test.mergetree1(sdt, id, name, cnt) values ('2018-06-03', 5, 'ccc', 11);
```

此时`/var/lib/clickhouse/data/test/mergetree1`的目录结构：

```
├── 20180601_20180601_1_1_0
│   ├── checksums.txt
│   ├── columns.txt
│   ├── id.bin
│   ├── id.mrk
│   ├── name.bin
│   ├── name.mrk
│   ├── cnt.bin
│   ├── cnt.mrk 
│   ├── cnt.idx
│   ├── primary.idx
│   ├── sdt.bin
│   └── sdt.mrk -- 保存一下块偏移量
├── 20180602_20180602_2_2_0
│   └── ...
├── 20180603_20180603_3_3_0
│   └── ...
├── format_version.txt
└── detached
```

### ReplacingMergeTree

`1`.在 MergeTree 的基础上，添加了“处理重复数据”的功能=>实时数据场景
 `2`.相比 MergeTree ,ReplacingMergeTree 在最后加一个"版本列",它跟时间列配合一起，用以区分哪条数据是"新的"，并把旧的丢掉(这个过程是在 merge 时处理，不是数据写入时就处理了的，平时重复的数据还是保存着的，并且查也是跟平常一样会查出来的)
 `3`.主键列组用于区分重复的行

```sq;
-- 版本列 允许的类型是， UInt 一族的整数，或 Date 或 DateTime
create table test.replacingmergetree (sdt  Date, id UInt16, name String, cnt UInt16) ENGINE=ReplacingMergeTree(sdt, (name), 10, cnt);

insert into test.replacingmergetree (sdt, id, name, cnt) values ('2018-06-10', 1, 'a', 20);
insert into test.replacingmergetree (sdt, id, name, cnt) values ('2018-06-10', 1, 'a', 30);
insert into test.replacingmergetree (sdt, id, name, cnt) values ('2018-06-11', 1, 'a', 20);
insert into test.replacingmergetree (sdt, id, name, cnt) values ('2018-06-11', 1, 'a', 30);
insert into test.replacingmergetree (sdt, id, name, cnt) values ('2018-06-11', 1, 'a', 10);

select * from test.replacingmergetree;

-- 如果记录未执行merge，可以手动触发一下 merge 行为
optimize table test.replacingmergetree;
```

┌────────sdt─┬─id─┬─name─┬─cnt─┐
 │ 2018-06-11 │  1 │ a    │  30 │
 └────────────┴────┴──────┴─────┘



### SummingMergeTree

------

`1`.SummingMergeTree 就是在 merge 阶段把数据sum求和
 `2`.sum求和的列可以指定，不可加的未指定列，会取一个最先出现的值

```sql
create table test.summingmergetree (sdt Date, name String, a UInt16, b UInt16) ENGINE=SummingMergeTree(sdt, (sdt, name), 8192, (a));

insert into test.summingmergetree (sdt, name, a, b) values ('2018-06-10', 'a', 1, 20);
insert into test.summingmergetree (sdt, name, a, b) values ('2018-06-10', 'b', 2, 11);
insert into test.summingmergetree (sdt, name, a, b) values ('2018-06-11', 'b', 3, 18);
insert into test.summingmergetree (sdt, name, a, b) values ('2018-06-11', 'b', 3, 82);
insert into test.summingmergetree (sdt, name, a, b) values ('2018-06-11', 'a', 3, 11);
insert into test.summingmergetree (sdt, name, a, b) values ('2018-06-12', 'c', 1, 35);

-- 手动触发一下 merge 行为
optimize table test.summingmergetree;

select * from test.summingmergetree;
```

┌────────sdt─┬─name─┬─a─┬──b─┐
 │ 2018-06-10       │         a    │   1   │   20   │
 │ 2018-06-10       │         b    │   2   │   11   │
 │ 2018-06-11       │         a    │   3   │   11   │
 │ 2018-06-11       │         b    │   6   │   18   │
 │ 2018-06-12       │         c    │   1   │   35   │
 └────────────┴──────┴───┴────┘
 `注:` 可加列不能是主键中的列，并且如果某行数据可加列都是 null ，则这行会被删除



### AggregatingMergeTree

------

AggregatingMergeTree 是在 MergeTree 基础之上，针对聚合函数结果，作增量计算优化的一个设计，它会在 merge 时，针对主键预处理聚合的数据
 应用于AggregatingMergeTree 上的聚合函数除了普通的 sum, uniq等，还有 sumState , uniqState ，及 sumMerge ， uniqMerge 这两组

`1.`聚合数据的预计算
 是一种“空间换时间”的权衡，并且是以减少维度为代价的

| dim1 | dim2 | dim3 | measure1 |
| :--- | ---: | ---: | :------: |
| aaaa |    a |    1 |    1     |
| aaaa |    b |    2 |    1     |
| bbbb |    b |    3 |    1     |
| cccc |    b |    2 |    1     |
| cccc |    c |    1 |    1     |
| dddd |    c |    2 |    1     |
| dddd |    a |    1 |    1     |

假设原始有三个维度，一个需要 count 的指标

| dim1 | dim2 | dim3 | measure1 |
| :--- | ---: | ---: | :------: |
| aaaa |    a |    1 |    1     |
| aaaa |    b |    2 |    1     |
| bbbb |    b |    3 |    1     |
| cccc |    b |    2 |    1     |
| cccc |    c |    1 |    1     |
| dddd |    c |    2 |    1     |
| dddd |    a |    1 |    1     |

通过减少一个维度的方式，来以 count 函数聚合一次 M

| dim2 | dim3 | count(measure1) |
| :--- | ---: | :-------------: |
| a    |    1 |        3        |
| b    |    2 |        2        |
| b    |    3 |        1        |
| c    |    1 |        1        |
| c    |    2 |        1        |

`2.`聚合数据的增量计算

对于 AggregatingMergeTree 引擎的表，不能使用普通的 INSERT 去添加数据，可以用：
 `a.` INSERT SELECT 来插入数据
 `b.` 更常用的，是可以创建一个物化视图

```sql
drop table if exists test.aggregatingmergetree;
create table test.aggregatingmergetree(
sdt Date
, dim1 String
, dim2 String
, dim3 String
, measure1 UInt64
) ENGINE=MergeTree(sdt, (sdt, dim1, dim2, dim3), 8192);

-- 创建一个物化视图，使用 AggregatingMergeTree
drop table if exists test.aggregatingmergetree_view;
create materialized view test.aggregatingmergetree_view
ENGINE = AggregatingMergeTree(sdt,(dim2, dim3), 8192)
as
select sdt,dim2, dim3, uniqState(dim1) as uv
from test.aggregatingmergetree
group by sdt,dim2, dim3;

insert into test.aggregatingmergetree (sdt, dim1, dim2, dim3, measure1) values ('2018-06-10', 'aaaa', 'a', '10', 1);
insert into test.aggregatingmergetree (sdt, dim1, dim2, dim3, measure1) values ('2018-06-10', 'aaaa', 'a', '10', 1);
insert into test.aggregatingmergetree (sdt, dim1, dim2, dim3, measure1) values ('2018-06-10', 'aaaa', 'b', '20', 1);
insert into test.aggregatingmergetree (sdt, dim1, dim2, dim3, measure1) values ('2018-06-10', 'bbbb', 'b', '30', 1);
insert into test.aggregatingmergetree (sdt, dim1, dim2, dim3, measure1) values ('2018-06-10', 'cccc', 'b', '20', 1);
insert into test.aggregatingmergetree (sdt, dim1, dim2, dim3, measure1) values ('2018-06-10', 'cccc', 'c', '10', 1);
insert into test.aggregatingmergetree (sdt, dim1, dim2, dim3, measure1) values ('2018-06-10', 'dddd', 'c', '20', 1);
insert into test.aggregatingmergetree (sdt, dim1, dim2, dim3, measure1) values ('2018-06-10', 'dddd', 'a', '10', 1);

-- 按 dim2 和 dim3 聚合 count(measure1)
select dim2, dim3, count(measure1) from test.aggregatingmergetree group by dim2, dim3;

-- 按 dim2 聚合 UV
select dim2, uniq(dim1) from test.aggregatingmergetree group by dim2;

-- 手动触发merge
OPTIMIZE TABLE test.aggregatingmergetree_view;
select * from test.aggregatingmergetree_view;

-- 查 dim2 的 uv
select dim2, uniqMerge(uv) from test.aggregatingmergetree_view group by dim2 order by dim2;
```

### CollapsingMergeTree

是专门为 OLAP 场景下，一种“变通”存数做法而设计的，在数据是不能改，更不能删的前提下，通过“运算”的方式，去抹掉旧数据的影响，把旧数据“减”去即可，从而解决"最终状态"类的问题，比如 `当前有多少人在线？`

“以加代删”的增量存储方式，带来了聚合计算方便的好处，代价却是存储空间的翻倍，并且，对于只关心最新状态的场景，中间数据都是无用的

CollapsingMergeTree 在创建时与 MergeTree 基本一样，除了最后多了一个参数，需要指定 Sign 位（必须是 Int8 类型）

```sql
create table test.collapsingmergetree(sign Int8, sdt Date, name String, cnt UInt16) ENGINE=CollapsingMergeTree(sdt, (sdt, name), 8192, sign);
```