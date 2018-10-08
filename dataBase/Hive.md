###Hive
####Overview
[官网](hive.apache.org)
The Apache Hive ™ data warehouse software facilitates reading, writing, and managing large datasets residing in distributed storage using SQL. Structure can be projected onto data already in storage. A command line tool and JDBC driver are provided to connect users to Hive.
apache的hive是一个数据参考的软件,它能方便提供非常方便的读、写和管理很大数据集,这个数据集是存储在分布式的存储系统上的，能够使用sql。已经存储在存储系统上的数据,可以用上结构化的东西,命令行的工具或jdbc的驱动能够连接到hive,

大数据仓库,构建于Hadoop之上的数据仓库
最初用于解决海量结构化的日志数据统计问题
定义类SQL查询语言,HQL
####产生背景
1. MapReduce编程的不便性
2. HDFS上的文件缺少Schema
####定义
通常用于进行海量数据处理(采用MapReduce)
底层支持多种不同的执行引擎
支持多种不同的压缩格式、存储格式以及自定义函数
#####底层执行引擎
Hive底层的执行引擎有：MapReduce、Tez、Spark
	Hive on MapReduce
	Hive on Tez
	Hive on Spark
#####压缩/存储格式
压缩：GZIP、LZO、Snappy、BZIP2..
存储：TextFile、SequenceFile、RCFile、ORC、Parquet
UDF：自定义函数
####好处
1. 简单易上手
2. 为超大数据集设计的计算/存储扩展能力(MR计算、HDFS存储)
3. 统一的元数据管理(可与Presto/Impala/Spark SQL等共享数据)
    元数据管理(hive表名、表字段类型、分隔符等)
####Hive环境搭建
[quick start](https://cwiki.apache.org/confluence/display/Hive/GettingStarted)
1）Hive下载：http://archive.cloudera.com/cdh5/cdh/5/
	wget http://archive.cloudera.com/cdh5/cdh/5/hive-1.1.0-cdh5.7.0.tar.gz

2）解压
	tar -zxvf hive-1.1.0-cdh5.7.0.tar.gz -C ~/app/

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
####Hive使用
创建表
```
CREATE  TABLE table_name 
  [(col_name data_type [COMMENT col_comment])]
  

create table hive_wordcount(context string);
```

加载数据到hive表
```
LOAD DATA LOCAL INPATH 'filepath' INTO TABLE tablename 

load data local inpath '/home/hadoop/data/hello.txt' into table hive_wordcount;


select word, count(1) from hive_wordcount lateral view explode(split(context,'\t')) wc as word group by word;

lateral view explode(): 是把每行记录按照指定分隔符进行拆解
```

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