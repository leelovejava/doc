# Elasticsearch 冷热集群架构 知识点串讲

官方叫法：热温暖架构(hot-warm)

## 1、什么是热温架构？
通俗解读：热节点存放用户最关心的热数据；温节点或者冷节点存放用户不太关心或者关心优先级低的冷数据或者暖数据。

官方解读：
热温架构是一项十分强大的功能，能够让您将 Elasticsearch 部署划分为“热”数据节点和“温”数据节点。
热数据节点处理所有新输入的数据，并且存储速度也较快，以便确保快速地采集和检索数据。
温节点的存储密度则较大，如需在较长保留期限内保留日志数据，不失为一种具有成本效益的方法。
将这两种类型的数据节点结合到一起后，您便能够有效地处理输入数据，并将其用于查询，同时还能在节省成本的前提下在较长时间内保留数据。
此架构对日志用例来说尤其大有帮助，因为在日志用例中，人们的大部分精力都会专注于近期的日志（例如最近两周），而较早的日志（由于合规性或者其他原因仍需要保留）则可以接受较慢的查询时间。

典型应用场景：
一句话：在成本有限的前提下，让客户关注的实时数据和历史数据硬件隔离，最大化解决客户反应的响应时间慢的问题。

业务场景描述：每日增量6TB日志数据，高峰时段写入及查询频率都较高，集群压力较大，查询ES时，常出现查询缓慢问题。
ES集群的索引写入及查询速度主要依赖于磁盘的IO速度，冷热数据分离的关键为使用SSD磁盘存储热数据，提升查询效率。
若全部使用SSD，成本过高，且存放冷数据较为浪费，因而使用普通SATA磁盘与SSD磁盘混搭，可做到资源充分利用，性能大幅提升的目标。

## 2、最最核心的实现原理
借助：Elasticsearch的分片分配策略，确切的说是：

第一：集群节点层面支持规划节点类型，这是划分热暖节点的前提。

第二：索引层面支持将数据路由到给定节点，这为数据写入热、暖节点做了保障。

[Shard allocation awareness](https://www.elastic.co/guide/en/elasticsearch/reference/current/allocation-awareness.html)

以及：

[Index-level shard allocation filtering](https://www.elastic.co/guide/en/elasticsearch/reference/current/shard-allocation-filtering.html#)

## 3、7.3版本ES实践一把
第一：搭建一个两个节点的集群，划分热、暖节点用。

第二：节点层面设置节点类型。
> node.attr.hotwarm_type: hot

第三：索引层面指定路由。
```shell script
PUT /logs_2019-08-31
{
  "settings": {
    "index.routing.allocation.require.hotwarm_type": "hot",
    "number_of_replicas": 0
  }
}
```

```shell script
PUT /logs_2019-08-01
{
  "settings": {
    "index.routing.allocation.require.hotwarm_type": "warm",
    "number_of_replicas": 0
  }
}
```

第四：效果图
[冷热分离 实际效果图](http://q2c2s5mn1.bkt.clouddn.com/es/es_hot_warm_effect.jpg)

4、坑：
`node.attr.hotwarm_type`:
单纯搜索你是找不到的。

因为：node.attr.*，你可以指定type类型、各种结合业务场景你的需要指定的值。包括：官方的：按照磁盘大小设定。和咱们的热暖节点。

白话文：就是标定节点划分分类的一个属性类型值。

这个坑网友也有疑惑：[node属性(tag)如何设置，查资料看到了好几种方法很混乱 - Elastic 中文社区](https://elasticsearch.cn/question/3865)，官方文档说的不是特别清楚。

## 5、Good 参考深入学习

1）最新冷热架构官方文档：[deploying-a-hot-warm-logging-cluster-on-the-elasticsearch-service](https://www.elastic.co/cn/blog/deploying-a-hot-warm-logging-cluster-on-the-elasticsearch-service)

2）最多参考冷热架构文档：[Elasticsearch Hot Warm Architecture | Elastic Blog](https://www.elastic.co/cn/blog/hot-warm-architecture-in-elasticsearch-5-x)

3）国内最佳实践：[elasticsearch冷热数据读写分离 - Elastic 中文社区](https://elasticsearch.cn/article/6127)

## 6、评论

我们现有的架构也是冷热分离，热节点使用的是ssd，indexing和search性能都不错，其中保存4天的数据，4天之后数据推到warm节点；

warm节点使用的是hdd。

在运维过程中，能体会到这种架构的特点是：冷节点或者热节点的离群不会影响另外一个种类型节点的功能；

但是如果整个集群中有节点产生stw，整个集群的性能都会被影响

这种架构能在相对节约成本的前提下极大的提升性能，但是不能完全做到一种类型节点的故障对其他类型节点是无感的