## 项目背景

## 项目架构
### 架构图
ELK + MySQL + Spring全家桶 + FastDFS + Kafka + Dubbo + Zookeeper

## 技术点

### 分布式事务

###  订单数据同步
#### 一阶段
建立数据中间表。把需要检索的业务数据，统一放到一张MySQL 表中，这张中间表对应了业务需要的Elasticsearch 索引，每一列对应索引中的一个Mapping 字段

随着业务数据越来越多，MySQL 中间表的数据量越来越大。当需要在 Elasticsearch 的索引中新增 Mapping 字段时，相应的 MySQL 中间表也需要新增列，在数据量庞大的表中，扩展列的耗时是难以忍受的

Elasticsearch 索引中的 Mapping 字段随着业务发展增多，需要由业务方增加相应的写入 MySQL 中间表方法，这也带来一部分开发成本

#### 二阶段
MySQL + Kafka + Elasticsearch

基于 MySQL Binlog 来进行 MySQL 数据同步到 Elasticsearch,Binlog 是 MySQL 通过 Replication 协议用来做主从数据同步的数据

基于开源项目 `go-mysql-elasticsearch`

为什么使用`kafka`?
1) 保证BinLog的安全性
2) 降低耦合度,提高扩展性

挑战:
无法保证数据的完整性和有序性
1) 完整性
kafka无法保证全局消息有序,局部有序(partition)

根据mysql的primary key,hash到各个partition,保证同一条mysql记录都发送到同一个partition

2) 完整性
写入elasticsearch才算成功,手动offset
监控

流程图:
配置中心(Apollo)

mysql -> canal(监听binlog) -> kafka-> 规则模块 -> binlog数据解析 -> elasticsearch


