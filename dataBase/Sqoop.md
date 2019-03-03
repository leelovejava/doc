# Sqoop
## 简介
核心功能:数据的导入和导出
用于在Hadoop和传统的数据库(mysql、postgresql等)进行的数据传递
可以通过hadoop的mapreduce把数据从关系型数据库中导入到Hadoop集群
传输大量结构化或半结构化数据的过程是完全自动化

## doc
[官网](http://sqoop.apache.org/)
[doc](http://sqoop.apache.org/docs/1.4.7/SqoopUserGuide.html)
![avatar](https://images2018.cnblogs.com/blog/1228818/201804/1228818-20180412130640231-449939615.png)
[Sqoop学习之路](https://www.cnblogs.com/qingyunzong/p/8807252.html)


## 工作机制

将导入或导出命令翻译成 MapReduce 程序来实现 在翻译出的 MapReduce 中主要是对 InputFormat 和 OutputFormat 进行定制

## 安装

1).下载&解压
[下载地址](http://mirrors.hust.edu.cn/apache/)

sqoop版本说明

绝大部分企业所使用的sqoop的版本都是 sqoop1

sqoop-1.4.6 或者 sqoop-1.4.7 它是 sqoop1
![avatar](https://images2018.cnblogs.com/blog/1228818/201804/1228818-20180412131040413-312918279.png)

2). 修改conf/sqoop-env-template.sh
> mv sqoop-env-template.sh sqoop-env.sh

3). 修改 sqoop-env.sh

4). 加入 mysql 驱动包到 sqoop/lib 目录下
常用的驱动:
    mysql 
        com.mysql.jdbc.Driver 
        http://central.maven.org/maven2/mysql/mysql-connector-java/8.0.12/mysql-connector-java-8.0.12.jar
    oracle
        oracle.jdbc.OracleDriver
        http://www.datanucleus.org/downloads/maven2/oracle/ojdbc6/11.2.0.3/ojdbc6-11.2.0.3.jar

## 命令
* sqoop 帮助
> ./sqoop help
* sqoop import命令帮助
> ./sqoop import --help
* sqoop 列出database；
> ./sqoop list-databases --connect jdbc:mysql://127.0.0.1/ --username root -P
* sqoop 测试连接查询数据
> ./sqoop eval --connect jdbc:mysql://127.0.0.1/dianping --username root --password 123456 --query "select SHOP_NAME,STAR from SHOP limit 10"
* sqoop导入数据到hdfs
> ./sqoop import --connect jdbc:mysql://127.0.0.1/dianping --username root -P  --table SHOP --target-dir /dianping.shop --num-mappers 1
* sqoop导出hdfs数据到数据库
> ./sqoop export --connect jdbc:mysql://127.0.0.1:3306/dbTest --username root -P --table testTable --export-dir /dianping.shop --num-mappers 1
* Sqoop导入数据到hbase
> ./sqoop import --connect jdbc:mysql://127.0.0.1/dianping --username root -P  --query "select URL_CODE,SHOP_NAME,STAR,CREATE_TIME from SHOP WHERE 1=1 and \$CONDITIONS" --hbase-table dianping --hbase-create-table --hbase-row-key URL_CODE --split-by CREATE_TIME --column-family cf
* sqoop导出hbase数据到数据库