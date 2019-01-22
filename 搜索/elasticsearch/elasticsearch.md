
## doc

-[官网](https://www.elastic.co/cn/webinars/getting-started-elasticsearch)

-[Elasticsearch权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html)

-[史上超全面的Elasticsearch使用指南](https://www.javazhiyin.com/4588.html)

-[SpringBoot整合elasticsearch](https://www.cnblogs.com/dalaoyang/p/8990989.html)

-[Elasticsearch 6.3 发布，你们要的 SQL 功能来了](https://www.iteblog.com/archives/2378.html?from=like)

-[京东到家订单中心 Elasticsearch 演进历程](https://mp.weixin.qq.com/s/TrCJJtvhjB2m29fOOa3Rzg)

-[日均5亿查询量，京东到家订单中心ES架构演进](https://mp.weixin.qq.com/s/n8ZfAabQ2lmcSExXrgjpuw)

-[Elasticsearch搜索引擎性能调优看懂这一篇就够了](https://mp.weixin.qq.com/s/VHULA5vfDBxjGzukZyYJbg)

-[图解elasticsearch原理](https://mp.weixin.qq.com/s/5cY2XFcyTCBBH8RtsM18dA)

-[ElasticSearch基础分布式架构讲解](https://mp.weixin.qq.com/s/HXWZW8_e5GRmTk23pRIYSQ)

## 下载历史版本
https://www.elastic.co/downloads/past-releases

## 使用
* Download and unzip Elasticsearch

* Run bin/elasticsearch (or bin\elasticsearch.bat on Windows)

* Run curl http://localhost:9200/ or Invoke-RestMethod http://localhost:9200 with PowerShell

* https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html

## 图形化界面

### 1.Cerebro

> https://github.com/lmenezes/cerebro

#### 安装

> Download from https://github.com/lmenezes/cerebro/releases

> Extract files

> Run bin/cerebro(or bin/cerebro.bat if on Windows)
    > windows ${elasticsearch_home}/bin/elasticsearch.bat
    > 指定端口 Run bin/cerebro -Dhttp.port=1234 -Dhttp.address=127.0.0.1

> Access on http://localhost:9200

### 2.elasticsearch-head

> 安装复杂，推荐`Cerebro`

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

1、特点：全文检索，结构化检索，数据统计、分析，接近实时处理，分布式搜索(可部署数百台服务器)，处理PB级别的数据
			搜索纠错，自动完成

2、使用场景：日志搜索，数据聚合，数据监控，报表统计分析

3、国内外使用者：维基百科，Stack Overflow，GitHub

### [elasticsearch与数据库的类比](https://www.elastic.co/guide/en/elasticsearch/reference/current/_mapping_concepts_across_sql_and_elasticsearch.html)
关系型数据库（比如Mysql）	非关系型数据库（Elasticsearch）

数据库Database	            索引Index

表Table	                    类型Type

数据行Row	                文档Document

数据列Column	            字段Field

约束 Schema	                映射Mapping

### 常用框架：
    1、Lucene
        Apache下面的一个开源项目，高性能的、可扩展的工具库，提供搜索的基本架构；
        如果开发人员需用使用的话，需用自己进行开发,成本比较大，但是性能高
    
    2、solr
        Solr基于Lucene的全文搜索框架，提供了比Lucene更为丰富的功能，
        同时实现了可配置、可扩展并对查询性能进行了优化
        建立索引时，搜索效率下降，实时索引搜索效率不高
        数据量的增加，Solr的搜索效率会变得更低,适合小的搜索应用，对应java客户端的是solrj
    
    
    3、elasticSearch
        基于Lucene的搜索框架, 它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口
        上手容易，拓展节点方便，可用于存储和检索海量数据，接近实时搜索，海量数据量增加，搜索响应性能几乎不受影响；
        分布式搜索框架，自动发现节点，副本机制，保障可用性

ES存入数据和搜索数据机制
1）索引对象（blog）：存储数据的表结构 ，任何搜索数据，存放在索引对象上 。
2）映射（mapping）：数据如何存放到索引对象上，需要有一个映射配置， 包括：数据类型、是否存储、是否分词等。
3）文档（document）：一条数据记录，存在索引对象上 
4）文档类型（type）：一个索引对象，存放多种类型数据，数据用文档类型进行标识  

## 使用

[spring-data-elasticsearch](https://github.com/spring-projects/spring-data-elasticsearch)

### ElasticSearch目录和配置文件介绍
    bin: 启动文件
    log: 日志文件，包括运行日志，慢查询日志
    config: 核心配置文件
    lib: 依赖包
    plugins :插件

### 命令

1.查询节点列表
> http://localhost:9200/_cat/nodes?v

2.查看所有索引
> http://localhost:9200/_cat/indices?v

目前 集群中没有任何索引

    补充：
        curl 
        -X 指定http的请求方法 有HEAD GET POST PUT DELETE 
        -d 指定要传输的数据 
        -H 指定http请求头信息 

3.新增索引
> curl -XPUT 'localhost:9200/blog_test?pretty'
> curl -XPUT 'localhost:9200/blog?pretty'

4.删除索引
> curl -XDELETE 'localhost:9200/blog_test?pretty'    
    
### SQL Access

[SQL Access](https://www.elastic.co/guide/en/elasticsearch/reference/current/xpack-sql.html)

*1.安装
*2.SQL REST API
*3.SQL Translate API
*4.SQL CLI
*5.SQL JDBC

#### 导入数据
```
POST twitter/doc/
{
  "name":"medcl",
  "twitter":"sql is awesome",
  "date":"2018-07-27",
  "id":123
}
```

#### RESTful下调用SQL(SQL Translate API)
```
POST /_xpack/sql?format=txt
{
    "query": "SELECT * FROM twitter"
}
```

```
curl -X POST "http:localhost:9200/_xpack/sql/translate" -H 'Content-Type: application/json' -d'
{
    "query": "SELECT * FROM library ORDER BY page_count DESC",
    "fetch_size": 10
}
```
> format支持的格式 `yaml、smile、cbor 、txt、csv、tsv`

#### SQL CLI
> elasticsearch-sql-cli

#### SQL JDBC
[sql-jdbc](https://www.elastic.co/guide/en/elasticsearch/reference/current/sql-jdbc.html)

```xml
<dependency>
  <groupId>org.elasticsearch.plugin</groupId>
  <artifactId>x-pack-sql-jdbc</artifactId>
  <version>6.5.4</version>
</dependency>
```

```java
class EsJDBC{
     public static void main(String[] args) {
        // jdbc:es://[http|https]?[host[:port]]*/[prefix]*[?[option=value]&]*
        String address = "jdbc:es://" + elasticsearchAddress;     
        Properties connectionProperties = connectionProperties(); 
        Connection connection = DriverManager.getConnection(address, connectionProperties);
         
        try (Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(
                    "SELECT name, page_count FROM library ORDER BY page_count DESC LIMIT 1")) {
            assertTrue(results.next());
            assertEquals("Don Quixote", results.getString(1));
            assertEquals(1072, results.getInt(2));
            SQLException e = expectThrows(SQLException.class, () -> results.getInt(1));
            assertTrue(e.getMessage(), e.getMessage().contains("unable to convert column 1 to an int"));
            assertFalse(results.next());
        }
    }
}
```
## 项目实战

BAT大牛亲授 基于ElasticSearch的搜房网实战