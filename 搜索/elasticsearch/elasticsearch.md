
## doc
[Elasticsearch权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html)

[史上超全面的Elasticsearch使用指南](https://www.javazhiyin.com/4588.html)

[SpringBoot整合elasticsearch](https://www.cnblogs.com/dalaoyang/p/8990989.html)

[Elasticsearch 6.3 发布，你们要的 SQL 功能来了](https://www.iteblog.com/archives/2378.html?from=like)

[图解 Elasticsearch 原理](https://mp.weixin.qq.com/s/a68yVzZK7xju2GPoAftBAw)

[京东到家订单中心 Elasticsearch 演进历程](https://mp.weixin.qq.com/s/TrCJJtvhjB2m29fOOa3Rzg)

[Elasticsearch搜索引擎性能调优看懂这一篇就够了](https://mp.weixin.qq.com/s/VHULA5vfDBxjGzukZyYJbg)

## 下载历史版本
https://www.elastic.co/downloads/past-releases

## 使用
* Download and unzip Elasticsearch
* Run bin/elasticsearch (or bin\elasticsearch.bat on Windows)
* Run curl http://localhost:9200/ or Invoke-RestMethod http://localhost:9200 with PowerShell
* https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html

## 图形化界面

### Cerebro

> https://github.com/lmenezes/cerebro

#### 安装

> Download from https://github.com/lmenezes/cerebro/releases

> Extract files

> Run bin/cerebro(or bin/cerebro.bat if on Windows)

    > 指定端口 Run bin/cerebro -Dhttp.port=1234 -Dhttp.address=127.0.0.1

> Access on http://localhost:9000

### elasticsearch-head

> git clone git://github.com/mobz/elasticsearch-head.git

> cd elasticsearch-head

> npm install

> npm run start

> open http://localhost:9100/

## Override

### 概念

**Elasticsearch是一个实时分布式搜索和分析引擎。它用于全文搜索、结构化搜索、分析**

全文检索：将非结构化数据中的一部分信息提取出来,重新组织,使其变得有一定结构,然后对此有一定结构的数据进行搜索,从而达到搜索相对较快的目的。
结构化检索：我想搜索商品分类为日化用品的商品都有哪些，select * from products where category_id='日化用品'
数据分析：电商网站，最近7天牙膏这种商品销量排名前10的商家有哪些；新闻网站，最近1个月访问量排名前3的新闻版块是哪些

### 适用场景

### 特点

### elasticsearch与数据库的类比
关系型数据库（比如Mysql）	非关系型数据库（Elasticsearch）
数据库Database	        索引Index
表Table	                类型Type
数据行Row	            文档Document
数据列Column	            字段Field
约束 Schema	            映射Mapping

ES存入数据和搜索数据机制
1）索引对象（blog）：存储数据的表结构 ，任何搜索数据，存放在索引对象上 。
2）映射（mapping）：数据如何存放到索引对象上，需要有一个映射配置， 包括：数据类型、是否存储、是否分词等。
3）文档（document）：一条数据记录，存在索引对象上 
4）文档类型（type）：一个索引对象，存放多种类型数据，数据用文档类型进行标识  

## 使用

https://github.com/spring-projects/spring-data-elasticsearch

## 项目实战

BAT大牛亲授 基于ElasticSearch的搜房网实战