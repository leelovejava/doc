# hue

## 简介

Hue是一个开源的Apache Hadoop UI系统。

通过使用Hue我们可以在浏览器端的Web控制台上与Hadoop集群进行交互来分析处理数据。
例如操作HDFS上的数据、运行Hive脚本、管理Oozie任务等等。

是基于Python Web框架Django实现的。

支持任何版本Hadoop

## 功能

基于文件浏览器（File Browser）访问HDFS

基于web编辑器来开发和运行Hive查询

支持基于Solr进行搜索的应用，并提供可视化的数据视图，报表生成

通过web调试和开发impala交互式查询

spark调试和开发

Pig开发和调试

oozie任务的开发，监控，和工作流协调调度

Hbase数据查询和修改，数据展示

Hive的元数据（metastore）查询

MapReduce任务进度查看，日志追踪

创建和提交MapReduce，Streaming，Java job任务

Sqoop2的开发和调试

Zookeeper的浏览和编辑

数据库（MySQL，PostGres，SQlite，Oracle）的查询和展示