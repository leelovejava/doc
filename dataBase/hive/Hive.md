# Hive

## 大纲

1、熟练掌握hive的使用

2、熟练掌握hql的编写

3、理解hive的工作原理

4、具备hive应用实战能力

### Hive入门&安装
* 什么是Hive
* Hive的优缺点
* Hive架构原理
* Hive和数据库比较
* Hive安装地址
* Hive安装部署
* 将本地文件导入Hive案例
* MySql安装
* 安装MySql服务器
* 安装MySql客户端

### Hive远程连接
* MySql中user表中主机配置
* Hive元数据配置到MySql
* 驱动拷贝
* 配置Metastore到MySql
* 多窗口启动Hive测试
* HiveJDBC访问
* 启动hiveserver2服务
* 启动beeline
* 连接hiveserver2
* Hive常用交互命令

### Hive命令 & 数据类型
* Hive其他命令操作
* Hive常见属性配置
* Hive数据仓库位置配置
* Hive查询后信息显示配置
* Hive运行日志信息配置
* 参数配置方式
* Hive数据类型
* Hive基本数据类型
* Hive集合数据类型
* 类型转化

### Hive DDL数据定义
* 创建数据库
* 查询数据库
* 修改数据库
* 删除数据库
* 创建管理表
* 创建外部表
* 管理表与外部表的互相转换
* 分区表基本操作
* 分区表注意事项
* 修改表、删除表
* 

### Hive DML数据操作
* 向表中装载数据（Load）
* 通过查询语句向表中插入数据（Insert）
* 查询语句中创建表并加载数据（as select）
* 创建表时通过location指定加载数据路径
* Import数据到指定hive表中
* Insert导出
* Hadoop命令导出到本地
* Hive Shell 命令导出
* Export导出到HDFS上
* 清除表中数据（Truncate） 
* 

### Hive查询
* 全表和特定列查询
* 列别名
* 算术运算符、常用函数
* LIMIT语句
* WHERE语句
* 比较运算符（BETWEEN/IN/ IS NULL）
* LIKE和RLIKE
* 逻辑运算符（AND/OR/NOT）
* 分组、GROUP BY语句
* HAVING语句
* 

### Hive JOIN & 排序
* 等值JOIN & 表的别名
* 内连接
* 左外连接、右外连接
* 满外连接、多表连接
* 笛卡尔积 JOIN
* 全局排序（Order By）
* 按照别名排序、多个列排序
* 每个MapReduce内部排序（Sort By）
* 分区排序（Distribute By）
* CLUSTER BY
* 

### Hive分桶 & 函数
* 分桶表数据存储
* 分桶抽样查询
* 常用查询函数
* 空字段赋值
* CASE WHEN
* 行转列
* 列转行
* 窗口函数
* Rank
* 系统内置函数
* 

### ive函数 & 压缩 & 存储
* 自定义UDF、UDAF、UDTF函数
* 压缩和存储
* 源码编译支持Snappy压缩
* 开启Map输出阶段压缩
* 开启Reduce输出阶段压缩
* 列式存储和行式存储
* TextFile格式、Orc格式
* Parquet格式
* 主流文件存储格式对比实验
* 存储和压缩结合案例

### 企业级调优
* Fetch抓取
* 本地模式
* 表的优化
* 小表、大表Join
* 大表Join大表
* MapJoin
* Group By
* Count(Distinct) 去重统计
* 笛卡尔积
* 行列过滤

### Hive企业级调优（二）
* 动态分区调整
* 分桶、分区
* Map数
* 小文件进行合并
* 复杂文件增加Map数
* Reduce数
* 并行执行、严格模式
* JVM重用、推测执行
* 压缩
* EXPLAIN（执行计划）

### 企业级调优 & 项目实战
* 项目视频表
* 项目用户表
* 项目技术选型
* 数据清洗
* 数据分析
* ETL之ETLUtil
* ETL之Mapper
* ETL之Runner
* 执行ETL
* 项目数据准备

### Hive项目实战&企业真题
* 统计视频观看数Top10
* 统计视频类别热度Top10
* 统计出视频观看数最高的20个视频的所属类别以及类别包含Top20视频的个数
* 统计视频观看数Top50所关联视频的所属类别Rank
* 统计每个类别中的视频热度Top10，以Music为例
* 统计每个类别中视频流量Top10，以Music为例
* 统计上传视频最多的用户Top10以及他们上传的观看次数在前20的视频
* 统计每个类别视频观看数Top10
* 项目总结
* 企业高频真题讲解2道

## Overview
[官网](http://hive.apache.org/)

[Hive介绍与核心知识点](https://mp.weixin.qq.com/s/8D3pemhEwOWfSjDAZAkLNg)

①什么是Hive?

The Apache Hive ™ data warehouse software facilitates reading, writing, and managing large datasets residing in distributed storage using SQL. Structure can be projected onto data already in storage. A command line tool and JDBC driver are provided to connect users to Hive.

apache的hive是一个数据仓库的软件,它能方便提供非常方便的读、写和管理很大数据集,这个数据集是存储在分布式的存储系统上的，能够使用sql

已经存储在存储系统上的数据,可以用上结构化的东西,命令行的工具或jdbc的驱动能够连接到hive,

1. 大数据仓库,构建于Hadoop之上的数据仓库

2. 定义类SQL查询语言(HQL)

3. 将结构化的数据文件映射为一张数据库表

最初用于解决海量结构化的日志数据统计问题


②为什么使用Hive?

产生背景:

    1. MapReduce编程的不便性
    
    2. HDFS上的文件缺少Schema

好处:

    1. 简单易上手(操作接口采用类SQL语法，提供快速开发的能力)
    
    2. 避免了去写MapReduce，减少开发人员的学习成本
    
    3. 为超大数据集设计的计算/存储扩展能力(MR计算、HDFS存储)
    
    4. 统一的元数据管理(可与Presto/Impala/Spark SQL等共享数据)
        元数据管理(hive表名、表字段类型、分隔符等)
    
    5. 扩展功能很方便
        可扩展(集群扩展)、延展性(自定义函数)、容错

## Hive架构

![image](https://github.com/leelovejava/doc/blob/master/img/dataBase/hive/01.png?raw=true)

Jobtracker是hadoop1.x中的组件，它的功能相当于： Resourcemanager+AppMaster

TaskTracker 相当于：  Nodemanager  +  yarnchild

### 基本组成
用户接口：包括 CLI、JDBC/ODBC、WebGUI。

元数据存储：通常是存储在关系数据库如 mysql , derby中。

解释器、编译器、优化器、执行器

### 各组件的基本功能
用户接口主要由三个：CLI、Client(JDBC/ODBC)、WebGUI。
        
        最常用的是CLI(shell命令行),Cli启动的时候，会同时启动一个Hive副本；
        JDBC/ODBC是Hive的JAVA实现，与传统数据库JDBC类似
        WebGUI是通过浏览器访问Hive

元数据存储：Hive 将元数据存储在数据库中。Hive 中的元数据包括表的名字，表的列和分区及其属性，表的属性（是否为外部表等），表的数据所在目录等。

解释器、编译器、优化器完成 HQL 查询语句从词法分析、语法分析、编译、优化以及查询计划的生成。生成的查询计划存储在 HDFS 中，并在随后有 MapReduce 调用执行。
    
数据存储在HDFS中，大部分的查询、计算由MapReduce完成（包含*的查询，比如select * from tbl不会生成MapRedcue任务）

### Hive与Hadoop的关系
Hive利用HDFS存储数据，利用MapReduce查询数据

用户:发送sql->hive:处理,转换成MapReduce,提交作业到Hadoop->Hadoop,运行作业->hdfs:存储

### Hive与传统数据库对比

![image](https://github.com/leelovejava/doc/blob/master/img/dataBase/hive/02.png?raw=true)

总结：hive具有sql数据库的外表，但应用场景完全不同，hive只适合用来做批量数据统计分析

### 数据存储

数据存储在hdfs上,没有专门的数据存储格式

在创建表的时候告诉 Hive 数据中的列分隔符和行分隔符,Hive 就可以解析数据

支持数据模型：DB、Table、External Table(外部表)、Partition(hdfs中table目录的字目录)、Bucket(桶)

## 定义

通常用于进行海量数据处理(采用MapReduce)

底层支持多种不同的执行引擎

支持多种不同的压缩格式、存储格式以及自定义函数

## 底层执行引擎

Hive底层的执行引擎有：MapReduce、Tez、Spark

	Hive on MapReduce
	
	Hive on Tez
	
	Hive on Spark

hive on Spark和Spark SQL的区别?
	
## 压缩/存储格式

压缩：GZIP、LZO、Snappy、BZIP2..

存储：TextFile、SequenceFile、RCFile、ORC、Parquet

UDF：自定义函数


## Hive环境搭建

### 方式
单机版、元数据库mysql版、远程服务器模式

用于非Java客户端访问元数据库，在服务器端启动MetaStoreServer，客户端利用Thrift协议通过MetaStoreServer访问元数据库

[quick start](https://cwiki.apache.org/confluence/display/Hive/GettingStarted)

0) yum  install mysql-server
   GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123' WITH GRANT OPTION;

1）Hive下载：http://archive.cloudera.com/cdh5/cdh/5/
> wget http://archive.cloudera.com/cdh5/cdh/5/hive-1.1.0-cdh5.7.0.tar.gz

2）解压
> tar -zxvf hive-1.1.0-cdh5.7.0.tar.gz -C ~/app/

3）配置
	1. 系统环境变量(~/.bahs_profile)
		export HIVE_HOME=/home/hadoop/app/hive-1.1.0-cdh5.7.0
		export PATH=$HIVE_HOME/bin:$PATH
    2. 实现安装一个mysql， yum install xxx

	hive-site.xml
```
	<property>
  		<name>javax.jdo.option.ConnectionURL</name>
    	<value>jdbc:mysql://localhost:3306/sparksql?createDatabaseIfNotExist=true</value>
    </property>
    
	<property>
    	<name>javax.jdo.option.ConnectionDriverName</name>
        <value>com.mysql.jdbc.Driver</value>
   	</property>

	<property>
  		<name>javax.jdo.option.ConnectionUserName</name>
    	<value>root</value>
    </property>

	<property>
  		<name>javax.jdo.option.ConnectionPassword</name>
    	<value>root</value>
    </property>
```
4）拷贝mysql驱动到$HIVE_HOME/lib/

5）启动hive: $HIVE_HOME/bin/hive

## hive的使用

### hive数据类型
    : primitive_type
      | array_type
      | map_type
      | struct_type
    ：primitive_type
      |TINYINT
      | SMALLINT
      | INT
      | BIGINT
      | BOOLEAN
      | FLOAT
      | DOUBLE
      | STRING

### 删除表
```
DROP (DATABASE|SCHEMA) [IF EXISTS] database_name [RESTRICT|CASCADE];

create database test;
```

### Hive 查看表描述
```
DESCRIBE [EXTENDED|FORMATTED] table_name

desc formatted psn;
```

### 创建表
```
CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_name
  [COMMENT database_comment]
  [LOCATION hdfs_path]
  [WITH DBPROPERTIES (property_name=property_value, ...)];
  
CREATE  TABLE table_name 
  [(col_name data_type [COMMENT col_comment])]

1) create table
create table psn (
id int,
name string,
likes ARRAY <string>,
address MAP <string, string>
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
COLLECTION ITEMS TERMINATED BY '-'
MAP KEYS TERMINATED BY ':';

2). load data
1,Lilei,book-tv-code,beijing:chaoyang-shanghai:pudong
2,Hanmeimei,book-Lilei-code,beijing:haidian-shanghai:huangpu

LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)]

load data local inpath '/home/hadoop/data/hive/psn' into table psn;

3) select data
select * from psn;

1	Liei	["book","tv","code"]	{"beijing":"chaoyang","shanghai":"pudong"}
2	Hanmeimei	["book","Lilei","code"]	{"beijing":"haidian","shanghai":"huangpu"}

```

### 创建外部表
[LOCATION hdfs_path];

create EXTERNAL table psn2 (
id int,
name string,
likes ARRAY <string>,
address MAP <string, string>
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
COLLECTION ITEMS TERMINATED BY '-'
MAP KEYS TERMINATED BY ':'
location '/user/hive/warehouse/test.db/psn2/';

load data local inpath '/home/hadoop/data/hive/psn2' into table psn2;

### 创建分区表
[PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]

create table psn3 (
id int,
name string,
likes ARRAY <string>,
address MAP <string, string>
)
PARTITIONED BY(age int)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
COLLECTION ITEMS TERMINATED BY '-'
MAP KEYS TERMINATED BY ':';

load data local inpath '/home/hadoop/data/hive/psn3' into table psn3 partition (age=10);

添加分区
alter table psn3 add partition(age=20,sex='man');

删除分区
alter table psn3 dro partition (age=10);

### 加载数据到hive表
```
LOAD DATA LOCAL INPATH 'filepath' INTO TABLE tablename 

load data local inpath '/home/hadoop/data/hello.txt' into table hive_wordcount;

select word, count(1) from hive_wordcount lateral view explode(split(context,'\t')) wc as word group by word;

lateral view explode(): 是把每行记录按照指定分隔符进行拆解
```

### 分区
    Hive 分区partition
    必须在表定义时指定对应的partition字段

#### 单分区建表语句：
    create table day_table (id int, content string) partitioned by (dt string);
    单分区表，按天分区，在表结构中存在id，content，dt三列。
    以dt为文件夹区分

#### 双分区建表语句：
    create table day_hour_table (id int, content string) partitioned by (dt string, hour string);
    双分区表，按天和小时分区，在表结构中新增加了dt和hour两列。
    先以dt为文件夹，再以hour子文件夹区分

#### Hive添加分区表语法
    （表已创建，在此基础上添加分区）：
    ALTER TABLE table_name ADD [IF NOT EXISTS] PARTITION partition_spec  [LOCATION 'location1'] partition_spec [LOCATION 'location2'] ...;
    
    partition_spec:
    : (partition_column = partition_col_value, partition_column = partition_col_value, ...)
    例：
    ALTER TABLE day_table ADD PARTITION (dt='2008-08-08', hour='08')

#### Hive删除分区语法：

    ALTER TABLE table_name DROP partition_spec, partition_spec,...
    partition_spec:
    : (partition_column = partition_col_value, partition_column = partition_col_value, ...)
    
    用户可以用 ALTER TABLE DROP PARTITION 来删除分区。
    内部表中、对应分区的元数据和数据将被一并删除。
    
    例：
    ALTER TABLE day_hour_table DROP PARTITION (dt='2008-08-08', hour='09');

#### Hive向指定分区添加数据语法：
     
     LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)] 
     
     例：
     LOAD DATA INPATH '/user/pv.txt' INTO TABLE day_hour_table PARTITION(dt='2008-08- 08', hour='08'); 
     LOAD DATA local INPATH '/user/hua/*' INTO TABLE day_hour partition(dt='2010-07- 07');
     
     当数据被加载至表中时，不会对数据进行任何转换。Load操作只是将数据复制至Hive表对应的位置。数据加载时在表下自动创建一个目录
     
#### Hive查询执行分区语法
     
     SELECT day_table.* FROM day_table WHERE day_table.dt>= '2008-08-08'; 
     
     分区表的意义在于优化查询。查询时尽量利用分区字段。如果不使用分区字段，就会全部扫描。
     
     
     Hive查询表的分区信息语法：
     SHOW PARTITIONS day_hour_table; 
     
     预先导入分区数据，但是无法识别怎么办
     Msck repair table tablename
     直接添加分区
 
### 分桶
* 对列值取哈希值的方式,将不同数据放到不同文件中存储
* 对于hive中的每一个表,分区都可以进一步进行分桶
* 由列的哈希值除以桶的个数来决定每条数据在哪个桶中

使用场景:
    数据抽样(samping)、map-join 

### 函数
内置运算符: 关系运算符、算术运算符、逻辑运算符、复杂类型函数、对复杂类型函数操作

内置函数: 数学函数、收集函数、类型转换函数、日期函数、条件函数、字符函数、 

内置的聚合函数(UDAF): count()、sum()、min()、max()....

内置表生成函数(UDTF) : explode(array<TYPE> a)、json_tuple
```
explode(array<TYPE> a)
数组一条记录中有多个参数，将参数拆分，每个参数生成一列
select explode(likes) from psn3;

book
tv
code
book
Lilei
code
```

自定义函数:UDF、UDAF
```
自定义UDF函数
1、UDF函数可以直接应用于select语句，对查询结构做格式化处理后，再输出内容。
2、编写UDF函数的时候需要注意一下几点：
    a）自定义UDF需要继承org.apache.hadoop.hive.ql.UDF。
    b）需要实现evaluate函数，evaluate函数支持重载。
3、步骤
    a）把程序打包放到目标机器上去；
    b）进入hive客户端，添加jar包：hive>add jar /run/jar/udf_test.jar;
       
    c）创建临时函数：
        add_example: 函数名 hive.udf.Add: 包名
        hive>CREATE TEMPORARY FUNCTION add_example AS 'hive.udf.Add';
    d）查询HQL语句：
        SELECT add_example(8, 9) FROM scores;
        SELECT add_example(scores.math, scores.art) FROM scores;
        SELECT add_example(6, 7, 8, 6.8) FROM scores;
    e）销毁临时函数：
        hive> DROP TEMPORARY FUNCTION add_example;
```

     
### 项目实战
hive ql提交执行以后会生成mr作业，并在yarn上运行
```
create table emp(
    empno int,
    ename string,
    job string,
    mgr int,
    hiredate string,
    sal double,
    comm double,
    deptno int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

create table dept(
    deptno int,
    dname string,
    location string
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

load data local inpath '/home/hadoop/data/emp.txt' into table emp;
load data local inpath '/home/hadoop/data/dept.txt' into table dept;

求每个部门的人数
select deptno, count(1) from emp group by deptno;
```

## 面试题

[面试题整理(Hive)](https://blog.csdn.net/wxfghy/article/details/81361400)

1. Hive数据倾斜

原因

    key分布不均匀
    
    业务数据本身的特性
    
    SQL语句造成数据倾斜

解决方法

    1).参数调节
        hive设置hive.map.aggr=true和hive.groupby.skewindata=true
        
        hive.map.aggr:
        在map中会做部分聚集操作，效率更高但需要更多的内存
        
        hive.groupby.skewindata:
        有数据倾斜的时候进行负载均衡，当选项设定为true,生成的查询计划会有两个MR Job。
        第一个MR Job中，Map的输出结果集合会随机分布到Reduce中，每个Reduce做部分聚合操作，并输出结果，这样处理的结果是相同Group By Key有可能被分发到不同的Reduce中，从而达到负载均衡的目的；
        第二个MR Job在根据预处理的数据结果按照 Group By Key 分布到Reduce中(这个过程可以保证相同的 Group By Key 被分布到同一个Reduce中)，最后完成最终的聚合操作。
    
    2).SQL语句调整: 
        A. 选用join key 分布最均匀的表作为驱动表。做好列裁剪和filter操作，以达到两表join的时候，数据量相对变小的效果。
        
        B. 大小表Join： 使用map join让小的维度表（1000条以下的记录条数）先进内存。在Map端完成Reduce。
        
        C. 大表Join大表：把空值的Key变成一个字符串加上一个随机数，把倾斜的数据分到不同的reduce上，由于null值关联不上，处理后并不影响最终的结果。
        
        D. count distinct大量相同特殊值：count distinct时，将值为空的情况单独处理，如果是计算count distinct，可以不用处理，直接过滤，在做后结果中加1。如果还有其他计算，需要进行group by，可以先将值为空的记录单独处理，再和其他计算结果进行union.

2. Hive中的排序关键字有哪些

order by ：查询结果全排序,限制单个reduce.严格模式下,必须加limit.

sort by ：reduce排序

distribute by ：分区排序

cluster by：相当于 Sort By + Distribute By,不能指定排序规则 ˈkləstər

通常：sort by+distribute by

* sort by和order by之间的区别？

order by:全局排序

distribute和sort:分组排序

3. 海量数据分布在100台电脑中，想个办法高效统计出这批数据的TOP10

方案1:

    在每台电脑上求出TOP10，可以采用包含10个元素的堆完成(TOP10小，用最大堆，TOP10大，用最小堆)。
    
    比如求TOP10大，我们首先取前10个元素调整成最小堆，如果发现，然后扫描后面的数据，并与堆顶元素比较，如果比堆顶元素大，那么用该元素替换堆顶，然后再调整为最小堆。
    
    最后堆中的元素就是TOP10大。

方案2

    求出每台电脑上的TOP10后，然后把这100台电脑上的TOP10组合起来，共1000个数据
    
    再利用上面类似的方法求出TOP10就可以了。

4. Hive中追加导入数据的4种方式是什么？请写出简要语法
    从本地导入： load data local inpath '/home/1.txt' (overwrite)into table student;
    
    从Hdfs导入： load data inpath '/user/hive/warehouse/1.txt' (overwrite)into table student;
    
    查询导入： create table student1 as select * from student;(也可以具体查询某项数据)
    
    查询结果导入：insert (overwrite) into table staff select * from track_log;
    
5. Hive导出数据有几种方式？如何导出数据

①用insert overwrite导出方式 

    A. 导出到本地： 
    
    insert overwrite local directory '/home/robot/1/2' rom format delimited fields terminated by ‘\t’ select * from staff;(递归创建目录)
    
    B. 导出到HDFS 
    
    insert overwrite directory '/user/hive/1/2’ rom format delimited fields terminated by ‘\t’ select * from staff;

②Bash shell覆盖追加导出 

例如：$ bin/hive -e “select * from staff;” > /home/z/backup.log

③Sqoop把hive数据导出到外部

6. hive 内部表和外部表区别?Hive分区表和分桶表的区别?

内部表：
      
       默认内部表
       hive管理(加载数据到 hive 所在的 hdfs 目录,删除时，元数据和数据都删除)
       适用于:原始数据和比较重要的数据建表存储

外部表：

       hdfs管理(不加载数据到 hive 所在的 hdfs 目录)
       删除时，只删除元数据
       使用于:日志文件进行分析,不会删除文件等

分区表:

        按照数据表的某列或某些列分为多个区存储，分区在HDFS上的表现形式是一个目录,可针对某个特定的时间做业务分析,不必扫描整个表
        优点:细化数据管理，直接读对应目录,缩小mapreduce程序要扫描的数据量

分桶表:

        对分区表更细粒度划分,分桶在HDFS上的表现形式是一个单独的文件,将整个数据内容按照某列属性值得hash值进行区分
        优点:1、提高join查询的效率（用分桶字段做连接字段）(防止数据倾斜) 2、提高采样的效率

* 表类型

内部表、外部表、分区表、桶表

* 创建表方式

create + load、like + load、as 创建表的同时加载数据、create + insert

7. Hive的分组方式

row_number() 是没有重复值的排序(即使两天记录相等也是不重复的),可以利用它来实现分页
dense_rank() 是连续排序,两个第二名仍然跟着第三名
rank()       是跳跃排序的,两个第二名下来就是第四名

8. 请谈一下hive的特点是什么？hive和RDBMS有什么异同？
hive是基于Hadoop的一个数据仓库工具,将结构化的数据文件映射为一张数据库表,提供类SQL查询功能,可以将sql语句转换为MapReduce任务进行运行。

优点是学习成本低，可以通过类SQL语句快速实现简单的MapReduce统计，不必开发专门的MapReduce应用

hive具有sql数据库的外表，但应用场景完全不同,hive只适合用来做批量数据统计分析

9 简要描述数据库中的 null，说出null在hive底层如何存储，
并解释select a.* from t1 a left outer join t2 b on a.id=b.id where b.id is null语句的含义

null与任何值运算的结果都是null, 可以使用is null、is not null函数指定在其值为null情况下的取值。

null在hive底层默认是用'\N'来存储的，可以通过alter table test SET SERDEPROPERTIES('serialization.null.format' = 'a');来修改。

查询出t1表中与t2表中id相等的所有信息
    
10. Hive优化

[Hive性能优化（全面）](https://mp.weixin.qq.com/s/9V99Dgz_yp-CPMtFFATtXw)

以下SQL不会转为Mapreduce来执行
    select仅查询本表字段
    where仅对本表字段做条件过滤

Explain 显示执行计划
    EXPLAIN [EXTENDED] query
    
hive运行方式:本地、集群,本地模式适用开发、数据量小
    `hive.exec.mode.local.auto.inputbytes.max` 默认值为128M,表示加载文件的最大值，若大于该配置仍会以集群方式来运行    

严格模式:
    查询限制：
    1、对于分区表，必须添加where对于分区字段的条件过滤
    2、order by语句必须包含limit输出限制
    3、限制执行笛卡尔积的查询

并行计算
    
Hive排序
    Order By - 对于查询结果做全排序，只允许有一个reduce处理
        （当数据量较大时，应慎用。严格模式下，必须结合limit来使用）
    Sort By - 对于单个reduce的数据进行排序
    Distribute By - 分区排序，经常和Sort By结合使用
    Cluster By - 相当于 Sort By + Distribute By
        （Cluster By不能通过asc、desc的方式指定排序规则；
        可通过 distribute by column sort by column asc|desc 的方式）

Hive join
    join计算时,小表在前,大表在后
    Map join,map端完成join(1.SQL添加mapJoin标记 2.开启自动的MapJoin)

Hive Side:map端完成join

控制Hive中Map以及Reduce的数量

JVM重用:JVM重用,适用场景：1、小文件个数过多 2、task个数过多
    
①通用设置

    hive.optimize.cp=true：列裁剪 
    hive.optimize.prunner：分区裁剪 
    hive.limit.optimize.enable=true：优化LIMIT n语句 
    hive.limit.row.max.size=1000000： 
    hive.limit.optimize.limit.file=10：最大文件数

②本地模式(小任务)
    job的输入数据大小必须小于参数：hive.exec.mode.local.auto.inputbytes.max(默认128MB)
    job的map数必须小于参数：hive.exec.mode.local.auto.tasks.max(默认4)
    job的reduce数必须为0或者1 
    hive.exec.mode.local.auto.inputbytes.max=134217728 
    hive.exec.mode.local.auto.tasks.max=4 
    hive.exec.mode.local.auto=true 
    hive.mapred.local.mem：本地模式启动的JVM内存大小

③并发计算
    hive.exec.parallel=true ，默认为false 
    hive.exec.parallel.thread.number=8
    
    前提:服务器资源够用,SQL支持并行计算

④Strict Mode：开启严格模式

hive.mapred.mode=true，严格模式不允许执行以下查询： 
    
    分区表上没有指定了分区
    
    没有limit限制的order by语句
    
    笛卡尔积：JOIN时没有ON语句

⑤动态分区

    hive.exec.dynamic.partition.mode=strict：该模式下必须指定一个静态分区 
    hive.exec.max.dynamic.partitions=1000 
    hive.exec.max.dynamic.partitions.pernode=100：在每一个mapper/reducer节点允许创建的最大分区数 
    DATANODE：dfs.datanode.max.xceivers=8192：允许DATANODE打开多少个文件

⑥推测执行

    mapred.map.tasks.speculative.execution=true 
    mapred.reduce.tasks.speculative.execution=true 
    hive.mapred.reduce.tasks.speculative.execution=true;

⑦多个group by合并
    hive.multigroupby.singlemar=true：当多个GROUP BY语句有相同的分组列，则会优化为一个MR任务

⑧虚拟列
    hive.exec.rowoffset：是否提供虚拟列

⑨分组
    两个聚集函数不能有不同的DISTINCT列，以下表达式是错误的： 
    INSERT OVERWRITE TABLE pv_gender_agg SELECT pv_users.gender, count(DISTINCT pv_users.userid), count(DISTINCT pv_users.ip) FROM pv_users GROUP BY pv_users.gender;
    SELECT语句中只能有GROUP BY的列或者聚集函数。

⑩Combiner聚合
    hive.map.aggr=true;在map中会做部分聚集操作，效率更高但需要更多的内存。 
    hive.groupby.mapaggr.checkinterval：在Map端进行聚合操作的条目数目

⑪数据倾斜
㈠hive.groupby.skewindata=true：数据倾斜时负载均衡，当选项设定为true，生成的查询计划会有两个MRJob。

㈡第一个MRJob 中，Map的输出结果集合会随机分布到Reduce中，每个Reduce做部分聚合操作，并输出结果，这样处理的结果是相同的GroupBy Key 
有可能被分发到不同的Reduce中，从而达到负载均衡的目的；

㈢第二个MRJob再根据预处理的数据结果按照GroupBy Key分布到Reduce中（这个过程可以保证相同的GroupBy Key被分布到同一个Reduce中），最后完成最终的聚合操作。

⑫排序
    ORDER BY colName ASC/DESC 
        hive.mapred.mode=strict时需要跟limit子句 
        hive.mapred.mode=nonstrict时使用单个reduce完成排序 
    SORT BY colName ASC/DESC ：
        每个reduce内排序 
    DISTRIBUTE BY(子查询情况下使用 )：
        控制特定行应该到哪个reducer，并不保证reduce内数据的顺序 
    CLUSTER BY ：
        当SORT BY 、DISTRIBUTE BY使用相同的列时。

⑬合并小文件
    hive.merg.mapfiles=true：合并map输出 
    hive.merge.mapredfiles=false：合并reduce输出 
    hive.merge.size.per.task=256*1000*1000：合并文件的大小 
    hive.mergejob.maponly=true：如果支持CombineHiveInputFormat则生成只有Map的任务执行merge 
    hive.merge.smallfiles.avgsize=16000000：文件的平均大小小于该值时，会启动一个MR任务执行merge。

⑭自定义map/reduce数目

㈠减少map数目： 
　　set mapred.max.split.size 
　　set mapred.min.split.size 
　　set mapred.min.split.size.per.node 
　　set mapred.min.split.size.per.rack 
　　set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat

㈡增加map数目： 
⑴当input的文件都很大，任务逻辑复杂，map执行非常慢的时候，可以考虑增加Map数，来使得每个map处理的数据量减少，从而提高任务的执行效率。

⑵假设有这样一个任务： 
select data_desc, count(1), count(distinct id),sum(case when …),sum(case when ...),sum(…) from a group by data_desc

⑶如果表a只有一个文件，大小为120M，但包含几千万的记录，如果用1个map去完成这个任务，肯定是比较耗时的，这种情况下，我们要考虑将这一个文件合理的拆分成多个，这样就可以用多个map任务去完成。 
　　set mapred.reduce.tasks=10; 
　　create table a_1 as select * from a distribute by rand(123);

⑷这样会将a表的记录，随机的分散到包含10个文件的a_1表中，再用a_1代替上面sql中的a表，则会用10个map任务去完成。每个map任务处理大于12M（几百万记录）的数据，效率肯定会好很多。

㈢reduce数目设置： 

参数1：hive.exec.reducers.bytes.per.reducer=1G：每个reduce任务处理的数据量

参数2：hive.exec.reducers.max=999(0.95*TaskTracker数)：每个任务最大的reduce数目

reducer数=min(参数2,总输入数据量/参数1)

set mapred.reduce.tasks：每个任务默认的reduce数目。典型为0.99*reduce槽数，hive将其设置为-1，自动确定reduce数目。

⑮使用索引：
    hive.optimize.index.filter：自动使用索引 
    hive.optimize.index.groupby：使用聚合索引优化GROUP BY操作