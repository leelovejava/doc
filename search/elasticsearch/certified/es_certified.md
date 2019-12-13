# 考试

## doc
- [Elastic认证考试](https://mp.weixin.qq.com/s/dze7OZOUYuRwvfdCxN01yQ)

- [Elastic认证工程师考试经验分享](https://elasticsearch.cn/article/13530)

- [Elastic 认证考试经验分享](https://www.bilibili.com/video/av51449470/)

- [Elastic认证考试心得](https://elasticsearch.cn/article/6133)

- [Elastic认证考试，请先看这一篇！](https://blog.csdn.net/laoyang360/article/details/95036171)

- [objectives](https://training.elastic.co/exam/elastic-certified-engineer#objectives)

## 大纲

### 1、考点1：安装和配置(Installation and Configuration)
1)、集群部署
    部署和启动Elastic集群以满足给定要求。
    难度系数：3星（配置要熟）

2)、集群配置
    配置集群的节点以满足给定要求。
    难度系数：3星（配置要熟）

3)、安全配置
    使用Elasticsearch安全性保障集群安全。
    难度系数：4星（配置要熟）

4)、角色划分
    使用Elasticsearch安全性定义基于角色的访问控制。
    难度系数：4星

* Deploy and start an Elasticsearch cluster that satisfies a given set of requirements
* Configure the nodes of a cluster to satisfy a given set of requirements
* Secure a cluster using Elasticsearch Security
* Define role-based access control using Elasticsearch Security

### 2、考点2：索引数据(Indexing Data)
1)、索引定义
    定义满足给定要求的索引。
    难度系数：2星
    
2)、增删改查文档
    在给定索引上执行：索引、创建、读、更新、删除文档操作。
    难度系数：2星
3)、索引别名
    定义和使用索引别名。
    难度系数：2星
4)、静态索引模板
    按照指定规则定义和适用索引模板，以满足给定需求。
    难度系数：4星（不经常用）
5)、动态索引模板
    定义和适用满足给定需求的动态模板。
    难度系数：4星（不经常用）
6)、索引迁移
    适用Reindex API 和 Update_by_query API reindex及更新文档。
    难度系数：5星（涉及复杂操作）
7)、Ingest节点&管道
    定义和使用管道（ingest pipeline)）以满足给定需求，包括：使用脚本修改文档(painless)。
    难度系数：5星（脚本使用有一定难度）

* Define an index that satisfies a given set of requirements
* Perform index, create, read, update, and delete operations on the documents of an index
* Define and use index aliases
* Define and use an index template for a given pattern that satisfies a given set of requirements
* Define and use a dynamic template that satisfies a given set of requirements
* Use the Reindex API and Update By Query API to reindex and/or update documents
* Define and use an ingest pipeline that satisfies a given set of requirements, including the use of Painless to modify documents

### 3、考点3：检索(Queries)
1)、简单检索
    在给定索引的一个或多个fields上撰写和执行terms或者phrases检索语句。
    难度系数：2星
    
2)、复杂检索
    撰写和执行包含复杂检索(query)和过滤(filter)的bool检索语句。
    难度系数：3星
    
3)、高亮
    在检索返回结果中高亮字段，也就是：执行特定字段的高亮检索。
    难度系数：2星
    
4)、排序
    为给定检索执行排序以满足一系列需求。
    难度系数：2星
    
5)、分页
    为检索结果实施分页。
    难度系数：2星
    
6)、全部遍历
    使用scroll API 获取超大数据量的返回结果。
    难度系数：3星
    
7)、模糊匹配
    使用fuzzy匹配检索。
    难度系数：3星
    
8)、检索模板
    定义和适用search template。
    难度系数：4星（不经常用）
    
9)、跨集群检索
    撰写和执行跨集群检索query。
    难度系数：4星（不经常用）

* Write and execute a search query for terms and/or phrases in one or more fields of an index
* Write and execute a search query that is a Boolean combination of multiple queries and filters
* Highlight the search terms in the response of a query
* Sort the results of a query by a given set of requirements
* Implement pagination of the results of a search query
* Use the scroll API to retrieve large numbers of results
* Apply fuzzy matching to a query
* Define and use a search template
* Write and execute a query that searches across multiple clusters

[Elasticsearch基础但非常有用的功能之二：模板](https://mp.weixin.qq.com/s/ddpwXr-YHiAfAPgK6dJwng)

### 4、考点4：聚合(Aggregations)
1)、Metric&bucket聚合
    撰写和执行Metric和bucket聚合
    难度系数：4星
    
2)、子聚合
    撰写和执行包含子聚合的聚合
    难度系数：4星
    
3)、管道聚合
    撰写和执行pipeline（管道）聚合
    难度系数：4星

* Write and execute metric and bucket aggregations
* Write and execute aggregations that contain sub-aggregations

### 5、考点5：映射和文本分析(Mappings and Text Analysis)
1)、Mapping定义
    定义满足给定需求的映射（Mapping）。
    难度系数：2星
    
2)、自定义分词器
    定义和使用自定义分词器，以满足给定需求。
    难度系数：5星（复杂的自定义分词）
    
3)、Multi-fields定义
    定义和使用多fields以及不同field指定不同的分词器。
    难度系数：2星
    
4)、Nested对象定义和使用
    配置一个索引，使得它能恰当的管理nested嵌套对象类型。
    难度系数：4星
    
5)、父子关联索引配置
    配置父子关联关系索引
    难度系数：4星

* Define a mapping that satisfies a given set of requirements
* Define and use a custom analyzer that satisfies a given set of requirements
* Define and use multi-fields with different data types and/or analyzers
* Configure an index so that it properly maintains the relationships of nested arrays of objects

### 6、考点6：集群管理(Cluster Administration)
1)、分片分配
    基于给定需求，在指定节点的索引上分配分片。
    难度系数：4星
    
2)、分片感知配置
    为索引配置分片感知和强制感知。
    难度系数：4星
    
3)、集群健康诊断与修复
    诊断分片问题、修复集群健康问题。
    难度系数：5星（异常情况处理，平时多积累）
    
4)、备份与恢复
    为集群或指定分片备份和恢复。
    难度系数：4星
    
5)、冷热架构部署
    为集群配置冷热架构。
    难度系数：4星
    
6)、跨集群检索
    为集群配置跨集群检索。
    难度系数：4星

* Allocate the shards of an index to specific nodes based on a given set of requirements
* Configure shard allocation awareness and forced awareness for an index
* Diagnose shard issues and repair a cluster’s health
* Backup and restore a cluster and/or specific indices
* Configure a cluster for use with a hot/warm architecture
* Configure a cluster for cross cluster search

### 7、考试难度与含金量
难度大不大？

难度非常大，原因：
1、全英文实战题
2、只允许参阅官网文档
3、考试时网络极有可能卡顿，必须有非常扎实的实践经验&文档非常熟悉，才有可能一考而过。

裸考能过吗？
几乎不可能。
Elastic中文社区排名第一名：wood大叔第一次裸考都没有通过考试，可见考试难度之大。

证书含金量？
考试难度和含金量成正比，难度越大、含金量越高。
一些牛逼的公司已经拿它作为工作敲门砖，且对考过童鞋报销考试费用。

### 8、前人经验
全球第一个考过的是日本人，接受过专访:https://www.elastic.co/blog/celebrating-the-first-elastic-certified-engineer
国内第一个考过的是魏彬老师，他目前是elasticsearch的技术负责人（专注于elk国内的培训，是Elastic国内首批合作企业），他是elastic日报发起人，本科上交大、硕士浙大。他们公司有几个考过的。之前看他们公司运营分享，国内考过的不到10人。（数据待确认）。他的Elastic认证考试心得：https://v.qq.com/x/page/f073779epxd.html
Wood大叔：Elastic 官方认证考试那些事儿 ：https://elasticsearch.cn/article/6133

总结了各位大佬的考过经验，干货如下：
1、考试时间非常紧、题目非常多。
2、满分才是通过，差一点也是不通过。（180分钟，11题必须全对，中间任何的出错，整个考试就是不通过。）——势必：证书含金量非常大。
3、所有的考试都是越早考过越好。考试当前是基于6.5版本，因为Elastic推陈出新非常快，7.X版本只是时间问题。
但是，新版本新特性如果没有来得及实践，势必难度会更大。(`Elasticsearch 7.2`)
4、文档必须非常熟悉、要o(1)复杂度快速定位章节。阿里云欧阳楚才兄也反馈，需要我们本机连接美国服务器，网络时延会比较大，考试中网络偶尔会非常卡。时间有限，翻文档的时间多了，势必后面题会做不完！
5、单纯看文档是没有用的，必须`kibana`实践敲一遍甚至多遍。
6、攻克英文关，全程英文，没有一点中文。
7、`centos7+`网络命令要熟悉。
8、`kibana` tool 命令行要熟悉。
9、考试费用400美金，试错成本太高。

### 9、考题回顾
* 冷热分离架构配置
* update_by_query + script按照要求更新索引
* 自定义分词插件，让king's和kings有相同的评分
* nested类型和nested query
* dynamic mapping
* multi-match, boost, most_fields
* date-histogram, sub-aggregation
* 开启security
* 集群备份snapshot
* match_phrase, highLighting, sort

### 冷热分离
[官网5.x hot-warm](https://www.elastic.co/cn/blog/hot-warm-architecture-in-elasticsearch-5-x)

[使用索引生命周期管理实现热温冷架构](https://www.elastic.co/blog/implementing-hot-warm-cold-in-elasticsearch-with-index-lifecycle-management)

[Elasticsearch冷热分离原理和实践](https://elasticsearch.cn/article/13566)


## 模拟考试

### 安装配置

#### 部署 3 节点的集群，需要同时满足以下要求
1) 集群名为"geektime"
    `cluster.name`
    
2) 将每个节点的名字设为和机器名一样，分别为 node1，node2，node3
    `node.name`
3) node1 配置成`dedicated` `master-eligable`节点

4) node2和node3配置成 ingest 和 data node
    记得要将 node.ml 设置成 false
    
5) 设置 jvm 为1g
    设置 jvm.options
    
#### 配置 3节点的集群，加上一个 Kibana 的实例，设定以下安全防护
1) 为集群配置 basic authentication

2) 将 Kibana 连接到 Elasticsearch

3) 创建一个名为 geektime 的用户

4) 创建一个名为 orders 的索引

5) geektime 用户只能读取和写入 oders 的索引，不能删除及修改 orders
        
#### 配置 3节点的集群，同时满足以下要求
     
1) 确保索引 A 的分片全部落在在节点1

2) 索引 B 分片全部落在 节点 2和3

3) 不允许删除数据的情况下，保证集群状态为 Green

### 索引数据
1) 为一个索引，按要求设置以下 dynamic Mapping
     
    一切 text 类型的字段，类型全部映射成 keyword
     
    一切以 int_开头命名的字段，类型都设置成 integer
    
2) 设置一个Index Template，符合以下的要求
 
为 log 和log- 开头的索引。创建 3 个主分片，1 个副本分片
 
同时为索引创建一个相应的 alias
 
使用 bulk API，写入多条电影数据

3) 为 movies index 设定一个 Index Alias，默认查询只返回评分大于3的电影

4) 给一个索引 A，要求创建索引 B，通过 Reindex API，将索引 A 中的文档写入索引 B，同时满足以下要求
 
增加一个整形字段，将索引 A中的一个字段的字符串长度，计算后写入
 
将 A 文档中的字符串以“；”分隔后，写入索引B中的数组字段中

5) 定义一个 Pipeline，并且将 eathquakes 索引的文档进行更新
 
pipeline的 ID 为 eathquakes_pipeline
 
将 magnitude_type 的字段值改为大写
 
如果文档不包含 “batch_number”, 增加这个字段，将数值设置为 1
 
如果已经包含 batch_number, 字段值➕1 

6) 为索引中的文档增加一个新的字段，字段值为 现有字段1+现有字段2+现有字段3

### 查询

1) 写一个查询，要求某个关键字在文档的 4 个字段中至少包含两个以上

 bool 查询，should / minimum_should_match
 
2) 按照要求写一个 search template
写入 search template
  
根据 search template 写出相应的 query
 
3) 对一个文档的多个字段进行查询，要求最终的算分是几个字段上算分的总和，同时要求对特定字段设置 boosting 值
 
4) 针对一个索引进行查询，当索引的文档中存在对象数组时，会搜索到了不期望的数据。需要重新定义 mapping，并提供改写后的 query 语句
Nested Object

### 聚合
 
1) earthquakes索引中包含了过去11个月的地震信息，请通过一句查询，获取以下信息

过去11个月，每个月的平均 地震等级（magiitude）
  
过去11个月里，平均地震等级最高的一个月及其平均地震等级
  
搜索不能返回任何文档
 
2) Query Fileter Bucket Filter
 
3) Pipeline Aggregation -> Bucket Filter

### 映射与分词
1) 一篇文档，字段内容包括了 “hello & world”，索引后，要求使用 match_phrase query, 查询 hello & world 或者 hello and world 都能匹配
 
2) reindex 索引，同时确保给定的两个查询，都能搜索到相关的文档，并且文档的算分是一样的

match 查询，分别查 “smith's” ，“smiths”
 
在不改变字段的属性，将数据索引到新的索引上
 
确保两个查询有一致的搜索结果和算分

### 集群管理
 
1) 安装并配置 一个 hot & warm 架构的集群
 
 三个节点， node 1 为 hot ， node2 为 warm，node 3 为cold
  
 三个节点均为 master-eligable 节点
  
 新创建的索引，数据写入 hot 节点
  
 通过一条命令，将数据从 hot 节点移动到 warm 节点
 
2) 为两个集群配置跨集群搜索
 两个集群都有 movies 的索引
  
 创建跨集群搜索
  
 创建一条查询，能够同时查到两个集群上的 movies 数据

3) 解决集群变红或者变黄的问题
 
 技能1：通过 explain API 查看
  
 
 技能2：shard filtering API，查看 include
  
 
 技能3： 更新一下 routing，确认 replica 可以分配（include 更加多的 rack）
  
 
 解决方案
  
     为集群配置延迟分配和一个节点上最多几个分片的配置
      
     设置 Replica 为 0
      
     删除 dangling index
      
     使用了错误的 routing node attribute
 
4) 备份一个集群中指定的几个索引