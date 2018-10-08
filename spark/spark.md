####Spark
####Overview
spark.apache.org

####Spring for Apache Hadoop
##### 1、[Overview](https://spring.io/projects/spring-hadoop)
Spring Hadoop简化了Apache Hadoop，提供了一个统一的配置模型以及简单易用的API来使用HDFS、MapReduce、Pig以及Hive。
还集成了其它Spring生态系统项目，如Spring Integration和Spring Batch
##### 2、Features特点
* Support to create Hadoop applications that are configured using Dependency Injection and run as standard Java applications vs. using Hadoop command line utilities.
  >支持创建Hadoop应用，配置使用依赖注入和运行标准的java应用程序和使用Hadoop的命令行工具
* Integration with Spring Boot to simply creat Spring apps that connect to HDFS to read and write data.
  >集成Spring Boot，可以简单地创建Spring应用程序去连接HDFS进行读写数据
* Create and configure applications that use Java MapReduce, Streaming, Hive, Pig, or HBase
  >创建和配置，使用java的MapReduce，Streaming，Hive，Pig或HBase
* Extensions to Spring Batch to support creating Hadoop based workflows for any type of Hadoop Job or HDFS operation.
  >扩展Spring Batch支持创建基于Hadoop的工作流的任何类型的Hadoop Job或HDFS的操作
* Script HDFS operations using any JVM based scripting language.
  >脚本HDFS操作使用任何基于JVM的脚本语言
* Easily create custom Spring Boot based aplications that can be deployed to execute on YARN.
  >基于SpringBoot轻松地创建自定义的基础应用，应用可以部署在YARN上
* DAO support (Template & Callbacks) for HBase.
  >支持DAO，可以使用模板或回调的方式操作Hbase
* Support for Hadoop Security.
  >支持Hadoop安全验证

##### 3、Spring Boot Config
```
<dependencies>
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-hadoop</artifactId>
        <version>2.5.0.RELEASE</version>
    </dependency>
</dependencies>
```

##### 4、Quick start
##### 5、Doc&Api
https://docs.spring.io/spring-hadoop/docs/2.5.0.RELEASE/reference/html/
https://docs.spring.io/spring-hadoop/docs/2.5.0.RELEASE/api/
http://blog.51cto.com/zero01/2094901?from=singlemessage

##### 6、spring-hadoop
[Using a Windows client together with a Linux cluster](https://github.com/spring-projects/spring-hadoop/wiki/Using-a-Windows-client-together-with-a-Linux-cluster)
