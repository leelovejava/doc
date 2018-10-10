# Spark

## Overview
spark.apache.org

## 1.课程目标
## 1.1.目标1：熟悉Spark相关概念
## 1.2.目标2：搭建Spark集群
## 1.3.目标3：编写简单的Spark应用程序

## 2.Spark概述
### 2.1.什么是Spark（官网：http://spark.apache.org）
Apache Spark™ is a unified analytics engine for large-scale data processing.

*Apache Spark™ 是一个快速通用的处理大规模数据的引擎*

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/01.png)
 内存计算框架

Spark是一种快速、通用、可扩展的大数据分析引擎，2009年诞生于加州大学伯克利分校AMPLab，2010年开源，2013年6月成为Apache孵化项目，2014年2月成为Apache顶级项目。
目前，Spark生态系统已经发展成为一个包含多个子项目的集合，其中包含SparkSQL、Spark Streaming、GraphX、MLlib等子项目，Spark是基于内存计算的大数据并行计算框架。
Spark基于内存计算，提高了在大数据环境下数据处理的实时性，同时保证了高容错性和高可伸缩性，允许用户将Spark部署在大量廉价硬件之上，形成集群。
Spark得到了众多大数据公司的支持，这些公司包括Hortonworks、IBM、Intel、Cloudera、MapR、Pivotal、百度、阿里、腾讯、京东、携程、优酷土豆。
当前百度的Spark已应用于凤巢、大搜索、直达号、百度大数据等业务；阿里利用GraphX构建了大规模的图计算和图挖掘系统，实现了很多生产系统的推荐算法；
腾讯Spark集群达到8000台的规模，是当前已知的世界上最大的Spark集群。

#### 产生背景
##### MapReduce局限性
1）代码繁琐 
2）只能够支持map和reduce方法 
3）执行效率低下 
4）不适合迭代多次、交互式、流式的处理

##### 框架多样化
1）批处理（离线）：MapReduce、Hive、Pig 
2）流式处理（实时）：Storm，JStorm 
3）交互式计算：Impala

### 2.2.为什么要学Spark
中间结果输出：基于MapReduce的计算引擎通常会将中间结果输出到磁盘上，进行存储和容错。
出于任务管道承接的，考虑，当一些查询翻译到MapReduce任务时，往往会产生多个Stage，而这些串联的Stage又依赖于底层文件系统（如HDFS）来存储每一个Stage的输出结果
Hadoop	Spark
	
Spark是MapReduce的替代方案，而且兼容HDFS、Hive，可融入Hadoop的生态系统，以弥补MapReduce的不足。

### 2.3.Spark特点
#### 2.3.1.快
与Hadoop的MapReduce相比，Spark基于内存的运算要快100倍以上，基于硬盘的运算也要快10倍以上。Spark实现了高效的DAG执行引擎，可以通过基于内存来高效处理数据流。
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/02-feature-speed.png)

#### 2.3.2.易用
Spark支持Java、Python和Scala的API，还支持超过80种高级算法，使用户可以快速构建不同的应用。而且Spark支持交互式的Python和Scala的shell，可以非常方便地在这些shell中使用Spark集群来验证解决问题的方法。
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/16-feature-ease-of-use.png)


#### 2.3.3.通用
Spark提供了统一的解决方案。Spark可以用于批处理、交互式查询（Spark SQL）、实时流处理（Spark Streaming）、机器学习（Spark MLlib）和图计算（GraphX）。
这些不同类型的处理都可以在同一个应用中无缝使用。Spark统一的解决方案非常具有吸引力，毕竟任何公司都想用统一的平台去处理遇到的问题，减少开发和维护的人力成本和部署平台的物力成本。

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/39-generality.png)

#### 2.3.4.兼容性
Spark可以非常方便地与其他的开源产品进行融合。比如，Spark可以使用Hadoop的YARN和Apache Mesos作为它的资源管理和调度器，并且可以处理所有Hadoop支持的数据，包括HDFS、HBase和Cassandra等。
这对于已经部署Hadoop集群的用户特别重要，因为不需要做任何数据迁移就可以使用Spark的强大处理能力。
Spark也可以不依赖于第三方的资源管理和调度器，它实现了Standalone作为其内置的资源管理和调度框架，这样进一步降低了Spark的使用门槛，使得所有人都可以非常容易地部署和使用Spark。
此外，Spark还提供了在EC2上部署Standalone的Spark集群的工具。

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/03.png)

### 2.4.Hadoop和Spark的对比

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/22-spark-PK-hadoop.png)

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/23-spark-PK-hadoop2.jpg)

## 3.Spark集群安装
### 3.1.安装
### 3.1.1.机器部署
准备两台以上Linux服务器，安装好JDK1.7

### 3.1.2.下载Spark安装包
#### 1) 下载tar包 
[spark1.5.2-hadoop2.6](http://www.apache.org/dyn/closer.lua/spark/spark-1.5.2/spark-1.5.2-bin-hadoop2.6.tgz)
上传解压安装包
上传spark-1.5.2-bin-hadoop2.6.tgz安装包到Linux上
解压安装包到指定位置
tar -zxvf spark-1.5.2-bin-hadoop2.6.tgz -C /usr/local

#### 2) 编译源码
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/04-download.png)

##### [Building Spark] (http://spark.apache.org/docs/latest/building-spark.html)

##### 前置条件(环境)
* jdk 8+
* maven 3.3.9+
* hadoop-2.6.0-cdh5.7.0.tar.gz
* Scala-2.11.8

##### 修改文件 spark-2.2.0/pom.xml
```xml
<repositorys>
    <repository>
      <id>cloudera</id>
      <url>https://repository.cloudera.com/artifactory/cloudera-repos/</url>
    </repository>
    
    <repository>  
        <id>alimaven</id>  
        <name>aliyun maven</name>  
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
    </repository>
</repositorys>

```
##### 编译
```
/dev/make-distribution.sh --name 2.6.0-cdh5.7.0 --tgz -Phadoop-2.6 -Dhadoop.version=2.6.0-cdh5.7.0 -Phive -Phive-thriftserver -Pyarn
```

### 3.1.3.配置Spark
#### 1) local模式
启动spark-shell
```
spark-shell --master local[2]
```
#### 2) standalone模式
Spark Standalone模式的架构和Hadoop HDFS/YARN很类似：1 master + n worker

##### 进入到Spark安装目录
cd /usr/local/spark-1.5.2-bin-hadoop2.6

##### conf/spark-env.sh
进入conf目录并重命名并修改spark-env.sh.template文件
```
export JAVA_HOME=/home/hadoop/java/jdk1.8
export SCALA_HOME=/home/hadoop/scala-2.11.7
export HADOOP_HOME=/home/hadoop/hadoop-2.7.2
export HADOOP_CONF_DIR=/home/hadoop-2.7.2/etc/hadoop

export SPARK_MASTER_PORT=7077
export SPARK_MASTER_HOST=hadoop000
export SPARK_MASTER_IP=hadoop000
export SPARK_WORKER_CORES=2g
export SPARK_WORKER_MEMORY=2g
export SPARK_WORKER_INSTANCES=1
```
```
# 配置解释
JAVA_HOME：Java安装目录
SCALA_HOME：Scala安装目录
HADOOP_HOME：hadoop安装目录
HADOOP_CONF_DIR：hadoop集群的配置文件的目录
SPARK_MASTER_IP：spark集群的Master节点的ip地址(to bind the master to a different IP address or hostname)
SPARK_WORKER_MEMORY：每个worker节点能够最大分配给exectors的内存大小(to set how much total memory workers have to give executors (e.g. 1000m, 2g))
SPARK_WORKER_CORES：每个worker节点所占有的CPU核数目(to set the number of cores to use on this machine)
SPARK_WORKER_INSTANCES：每台机器上开启的worker节点的数目(to set the number of worker processes per node)
```

##### conf/slaves
重命名并修改slaves.template文件
在该文件中添加子节点所在的位置（Worker节点）
hadoop000
hadoop001
hadoop002

将配置好的Spark拷贝到其他节点上

scp -r spark-1.5.2-bin-hadoop2.6/ hadoop000:/usr/local/

scp -r spark-1.5.2-bin-hadoop2.6/ hadoop001:/usr/local/

scp -r spark-1.5.2-bin-hadoop2.6/ hadoop002.cn:/usr/local/

### 3.1.4.启动Spark
Spark集群配置完毕，目前是1个Master，3个Work，在hadoop000上启动Spark集群
```
sbin/start-all.sh
```

启动后执行jps命令，主节点上有Master进程，其他子节点上有Work进行，登录Spark管理界面查看集群状态（主节点）：http://node1.itcast.cn:8080/
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/05.png)

到此为止，Spark集群安装完毕，但是有一个很大的问题，那就是Master节点存在单点故障，要解决此问题，就要借助zookeeper，并且启动至少两个Master节点来实现高可靠，配置方式比较简单：
Spark集群规划：node1，node2是Master；node3，node4，node5是Worker
安装配置zk集群，并启动zk集群
停止spark所有服务，修改配置文件spark-env.sh，在该配置文件中删掉SPARK_MASTER_IP并添加如下配置
export SPARK_DAEMON_JAVA_OPTS="-Dspark.deploy.recoveryMode=ZOOKEEPER -Dspark.deploy.zookeeper.url=zk1,zk2,zk3 -Dspark.deploy.zookeeper.dir=/spark"
1.在node1节点上修改slaves配置文件内容指定worker节点
2.在node1上执行sbin/start-all.sh脚本，然后在node2上执行sbin/start-master.sh启动第二个Master

## 4.[Quick start](http://spark.apache.org/docs/latest/quick-start.html)
[2.2.1官方quick start翻译](https://blog.csdn.net/zuolovefu/article/details/79117824)
### 4.1.前言

This tutorial provides a quick introduction to using Spark. We will first introduce the API through Spark’s interactive shell (in Python or Scala), then show how to write applications in Java, Scala, and Python.

*本教程提供了使用Spark的快速入门教程。我们将首先通过Spark的交互式shell（Python或Scala）介绍其API，然后展示如何用Java，Scala和Python编写Spark应用程序。*

To follow along with this guide, first download a packaged release of Spark from the Spark website. Since we won’t be using HDFS, you can download a package for any version of Hadoop

*要学习本教程，请先从Spark网站下载Spark的安装包。由于我们不会使用HDFS，因此您可以下载任何版本的Hadoop的软件包*

Note that, before Spark 2.0, the main programming interface of Spark was the Resilient Distributed Dataset (RDD). After Spark 2.0, RDDs are replaced by Dataset, which is strongly-typed like an RDD, but with richer optimizations under the hood.
*请注意，在Spark 2.0之前，Spark的主要编程接口是弹性分布式数据集（RDD）。在Spark 2.0之后，RDD被DataSet取代，DataSet类似于RDD的加强版，在引擎盖下有更丰富的优化。*

The RDD interface is still supported, and you can get a more complete reference at the RDD programming guide. However, we highly recommend you to switch to use Dataset, which has better performance than RDD. See the SQL programming guide to get more information about Dataset
*RDD接口仍然可使用，您可以在[RDD编程指南](http://blog.csdn.net/zuolovefu/article/details/79117926)中获得更完整的参考资料。但是，我们强烈建议您切换到使用DataSet，这具有比RDD更好的性能。请参阅[SQL编程指南](http://spark.apache.org/docs/latest/sql-programming-guide.html)以获取有关数据集的更多信息。*

### 4.2.Interactive Analysis with the Spark Shell(使用spark shell进行交互式操作)
#### 4.2.1.Basics(基本用法)
Spark’s shell provides a simple way to learn the API, as well as a powerful tool to analyze data interactively. 
It is available in either Scala (which runs on the Java VM and is thus a good way to use existing Java libraries) or Python. Start it by running the following in the Spark directory:
*Spark的shell提供了一个学习API的简单方法，同时也是交互式分析数据的强大工具。它可以使用Scala或Python语言进行开发，可通过在Spark目录运行以下命令启动Spark-Shell：*
```
./bin/spark-shell
```
#### Spark’s primary abstraction is a distributed collection of items called a Dataset. Datasets can be created from Hadoop InputFormats (such as HDFS files) or by transforming other Datasets. Let’s make a new Dataset from the text of the README file in the Spark source directory:
*Spark的主要抽象是一个名为Dataset的分布式集合。
DataSet可以从Hadoop输入格式或者其他Dataset转换得来。 
让我们利用Spark根目录中的README文件的文本中创建一个新的DataSet：*
```
scala> val textFile = spark.read.textFile("README.md")
textFile: org.apache.spark.sql.Dataset[String] = [value: string]
```

#### You can get values from Dataset directly, by calling some actions, or transform the Dataset to get a new one. For more details, please read the API doc.
*我们可以直接调用方法从DataSet里得出某些值，也可以把一个DataSet转换成一个新的Dataset。更多信息，请看DataSet [API文档](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.Dataset)*
```
scala> textFile.count() // Number of items in this Dataset
res0: Long = 126 // May be different from yours as README.md will change over time, similar to other outputs

scala> textFile.first() // First item in this Dataset
res1: String = # Apache Spark
```
#### Now let’s transform this Dataset to a new one. We call filter to return a new Dataset with a subset of the items in the file.
*现在让我们转换这个Dataset到一个新的Dataset。我们调用filter来返回一个新的Dataset。*
```
scala> val linesWithSpark = textFile.filter(line => line.contains("Spark"))
linesWithSpark: org.apache.spark.sql.Dataset[String] = [value: string]
```
输出 linesWithSpark: org.apache.spark.sql.Dataset[String] = [value: string]
#### We can chain together transformations and actions:
*当然，我们可以把Transform 和 Action 连接起来一起执行：*
```
textFile.filter(line => line.contains("Spark")).count() // How many lines contain "Spark"?
res3: Long = 15
```

#### 4.2.2.More on Dataset Operations(更多DataSet 操作)
#####
Dataset actions and transformations can be used for more complex computations. 
Let’s say we want to find the line with the most words:

*DataSet的Action和Transformation操作可实现更复杂的计算。比方说，我们想找到最多的单词：*
```
scala> textFile.map(line => line.split(" ").size).reduce((a, b) => if (a > b) a else b)
res4: Long = 15
```
##### 
This first maps a line to an integer value, creating a new Dataset. 
reduce is called on that Dataset to find the largest word count. 
The arguments to map and reduce are Scala function literals (closures), and can use any language feature or Scala/Java library. 
For example, we can easily call functions declared elsewhere. 
We’ll use Math.max() function to make this code easier to understand:

*里面的操作如下：* 
*1. 将一行map到一个整数值，创建一个新的DataSet。 *
*2. 在该DataSet上调用reduce来查找最大的字数*
```
scala> import java.lang.Math
scala> textFile.map(line => line.split(" ").size).reduce((a, b) => Math.max(a, b))
res5: Int = 15
```
##### 
One common data flow pattern is MapReduce, as popularized by Hadoop. Spark can implement MapReduce flows easily:
*一种常见的数据流模式是MapReduce，由Hadoop所普及的。Spark可以轻易实现MapReduce流：*
```
scala> val wordCounts = textFile.flatMap(line => line.split(" ")).groupByKey(identity).count()
wordCounts: org.apache.spark.sql.Dataset[(String, Long)] = [value: string, count(1): bigint]
```
##### collection
Here, we call flatMap to transform a Dataset of lines to a Dataset of words, and then combine groupByKey and count to compute the per-word counts in the file as a Dataset of (String, Long) pairs. 

*具体流程如下：* 
*1. 调用flatMap将行数据集转换为单词数据集* 
*2. 结合groupByKey和count来计算文件中每个字的计数，作为（String，Long）格式保存起来*
*To collect the word counts in our shell, we can call collect:*
*如果想要把结果收集起来，可以调用collect方法：*

```
scala> wordCounts.collect()
res6: Array[(String, Int)] = Array((means,1), (under,2), (this,3), (Because,1), (Python,2), (agree,1), (cluster.,1), ...)
```

#### 4.2.3.Caching(缓存)
Spark also supports pulling data sets into a cluster-wide in-memory cache. This is very useful when data is accessed repeatedly, such as when querying a small “hot” dataset or when running an iterative algorithm like PageRank. 

*Spark还支持将DataSet保存到集群范围内的内存缓存中。当重复访问数据时，如查询小的“热”数据集或运行迭代算法（如PageRank）时，这非常有用。*
As a simple example, let’s mark our linesWithSpark dataset to be cached:
*举一个简单的例子，让我们标记我们的linesWithSpark数据集被缓存：*

```
scala> linesWithSpark.cache()
res7: linesWithSpark.type = [value: string]

scala> linesWithSpark.count()
res8: Long = 15

scala> linesWithSpark.count()
res9: Long = 15
```
It may seem silly to use Spark to explore and cache a 100-line text file. 
The interesting part is that these same functions can be used on very large data sets, even when they are striped across tens or hundreds of nodes. 
*使用Spark探索和缓存100行文本文件似乎很愚蠢，但有趣的部分是这些相同的功能可以用在非常大的数据集上，即使当它们被划分成数十或数百个节点时也可以缓存*
You can also do this interactively by connecting bin/spark-shell to a cluster, as described in the [RDD programming guide](http://spark.apache.org/docs/latest/rdd-programming-guide.html#using-the-shell).

### 4.3.Self-Contained Applications(Spark应用程序)
Suppose we wish to write a self-contained application using the Spark API. 
We will walk through a simple application in Scala (with sbt), Java (with Maven), and Python (pip).

*假设我们希望使用Spark API编写一个Spark 应用程序。我们将通过一个简单的应用程序，通过Scala（与SBT），Java（与Maven）和Python（PIP）*
*我们将在Scala中创建一个非常简单的Spark应用程序，事实上，它被命名为SimpleApp.scala：*

```
 //* SimpleApp.scala */
 import org.apache.spark.sql.SparkSession
 
 object SimpleApp {
   def main(args: Array[String]) {
     val logFile = "YOUR_SPARK_HOME/README.md" // Should be some file on your system
     val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
     val logData = spark.read.textFile(logFile).cache()
     val numAs = logData.filter(line => line.contains("a")).count()
     val numBs = logData.filter(line => line.contains("b")).count()
     println(s"Lines with a: $numAs, Lines with b: $numBs")
     spark.stop()
   }
 }
```
Note that applications should define a main() method instead of extending scala.App. Subclasses of scala.App may not work correctly.

*请注意，应用程序应该定义一个main（）方法，而不是扩展scala.App。 scala.App的子类可能无法正常工作*

This program just counts the number of lines containing ‘a’ and the number containing ‘b’ in the Spark README. Note that you’ll need to replace YOUR_SPARK_HOME with the location where Spark is installed. Unlike the earlier examples with the Spark shell, which initializes its own SparkSession, we initialize a SparkSession as part of the program.

*这个程序只是计算Spark Readme文件中包含’a’的行数和包含’b’的数字*

We call SparkSession.builder to construct a [[SparkSession]], then set the application name, and finally call getOrCreate to get the [[SparkSession]] instance.

*我们调用SparkSession.builder来构造[[SparkSession]]，然后设置应用程序名称，最后调用getOrCreate来获取[[SparkSession]]实例*

Our application depends on the Spark API, so we’ll also include an sbt configuration file, build.sbt, which explains that Spark is a dependency. This file also adds a repository that Spark depends on:

*我们的应用程序依赖于Spark API，所以我们还将包含一个sbt配置文件build.sbt，它解释了Spark是一个依赖项。该文件还添加了Spark所依赖的存储库：*

```
name := "Simple Project"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.2"
```

For sbt to work correctly, we’ll need to layout SimpleApp.scala and build.sbt according to the typical directory structure. Once that is in place, we can create a JAR package containing the application’s code, then use the spark-submit script to run our program.

*为了正常工作，我们需要根据典型的目录结构来放置SimpleApp.scala和build.sbt。一旦放置好，我们可以创建一个包含应用程序代码的JAR包，然后使用spark-submit脚本来运行我们的程序。*

bin/spark-submit
```
# Your directory layout should look like this
$ find .
.
./build.sbt
./src
./src/main
./src/main/scala
./src/main/scala/SimpleApp.scala

# Package a jar containing your application
$ sbt package
...
[info] Packaging {..}/{..}/target/scala-2.11/simple-project_2.11-1.0.jar

# Use spark-submit to run your application
$ YOUR_SPARK_HOME/bin/spark-submit \
  --class "SimpleApp" \
  --master local[4] \
  target/scala-2.11/simple-project_2.11-1.0.jar
...
Lines with a: 46, Lines with b: 23
```

```java
/* SimpleApp.java */
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;

public class SimpleApp {
  public static void main(String[] args) {
    String logFile = "YOUR_SPARK_HOME/README.md"; // Should be some file on your system
    SparkSession spark = SparkSession.builder().appName("Simple Application").getOrCreate();
    Dataset<String> logData = spark.read().textFile(logFile).cache();

    long numAs = logData.filter(s -> s.contains("a")).count();
    long numBs = logData.filter(s -> s.contains("b")).count();

    System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);

    spark.stop();
  }
}
```
```xml
<project>
  <groupId>edu.berkeley</groupId>
  <artifactId>simple-project</artifactId>
  <modelVersion>4.0.0</modelVersion>
  <name>Simple Project</name>
  <packaging>jar</packaging>
  <version>1.0</version>
  <dependencies>
    <!-- Spark dependency -->
    <dependency> 
      <groupId>org.apache.spark</groupId>
      <artifactId>spark-sql_2.11</artifactId>
      <version>2.3.2</version>
    </dependency>
  </dependencies>
</project>
```
####
We lay out these files according to the canonical Maven directory structure:
```
$ find .
./pom.xml
./src
./src/main
./src/main/java
./src/main/java/SimpleApp.java
```
#### 
Now, we can package the application using Maven and execute it with ./bin/spark-submit.
#### Package a JAR containing your application
```
# Package a JAR containing your application
$ mvn package
...
[INFO] Building jar: {..}/{..}/target/simple-project-1.0.jar

# Use spark-submit to run your application
$ YOUR_SPARK_HOME/bin/spark-submit \
  --class "SimpleApp" \
  --master local[4] \
  target/simple-project-1.0.jar
...
Lines with a: 46, Lines with b: 23
```

### 4.4.Where to Go from Here(更多)

Congratulations on running your first Spark application!

*祝贺您运行您的第一个Spark应用程序！*

For an in-depth overview of the API, start with the [RDD programming guide](http://spark.apache.org/docs/latest/rdd-programming-guide.html) and the SQL programming guide, or see “Programming Guides” menu for other components.

*有关API的深入概述，请从[RDD编程指南](http://blog.csdn.net/zuolovefu/article/details/79117926)和[SQL编程指南](http://spark.apache.org/docs/latest/sql-programming-guide.html)开始，或者参阅其他组件的“编程指南”菜单*

For running applications on a cluster, head to the [deployment overview](http://spark.apache.org/docs/latest/cluster-overview.html).

*要在集群上运行应用程序，请转到[部署概述](http://spark.apache.org/docs/latest/cluster-overview.html)*

Finally, Spark includes several samples in the examples directory (Scala, Java, Python, R). You can run them as follows:

*最后，Spark在示例目录（Scala，Java，Python，R）中包含了几个示例。你可以如下运行它们：*

```
# For Scala and Java, use run-example:
./bin/run-example SparkPi

# For Python examples, use spark-submit directly:
./bin/spark-submit examples/src/main/python/pi.py

# For R examples, use spark-submit directly:
./bin/spark-submit examples/src/main/r/dataframe.R
```

## 5.执行Spark程序
### 5.1.执行第一个spark程序
```
/usr/local/spark-1.5.2-bin-hadoop2.6/bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master spark://node1.itcast.cn:7077 \
--executor-memory 1G \
--total-executor-cores 2 \
/usr/local/spark-1.5.2-bin-hadoop2.6/lib/spark-examples-1.5.2-hadoop2.6.0.jar \
100
```
参数说明
* class           作业的主类,即main函数所有的类,参考值:org.apache.spark.examples.SparkPi     
* master          master的URL.参考值:yarn
* deploy-mode     client和cluster2种模式,参考值:client
* driver-memory   driver 使用的内存，不可超过单机的 core 总数,参考值:4g
* num-executors   创建多少个 executor,参考值:2
* executor-memory 各个 executor 使用的最大内存，不可超过单机的最大可使用内存。参考值:2g
* executor-cores  各个 executor 使用的并发线程数目，也即每个 executor 最大可并发执行的 Task 数目。 参考值:2
* conf            指定key=value形式的配置
该算法是利用蒙特·卡罗算法求PI

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/21-spark-on-submit.png)

### 5.2.启动Spark Shell
spark-shell是Spark自带的交互式Shell程序，方便用户进行交互式编程，用户可以在该命令行下用scala编写spark程序。

#### 5.2.1.启动spark shell
```
--master MASTER_URL         spark://host:port, mesos://host:port, yarn, or local.
// 2:运行的线程数,*:表示使用机器上所有可用的核数
bin/spark-shell --master local[2]
// 本地模式使用2GB内存
spark-shell --driver-memory 2g --master local[*]
// 在yarn上启动
bin/spark-shell --master yarn-client

/usr/local/spark-1.5.2-bin-hadoop2.6/bin/spark-shell \
--master spark://node1.itcast.cn:7077 \
--executor-memory 2g \
--total-executor-cores 2

参数说明：
--master spark://node1.itcast.cn:7077 指定Master的地址
--executor-memory 2g 指定每个worker可用内存为2G
--total-executor-cores 2 指定整个集群使用的cup核数为2个
```
注意：
如果启动spark shell时没有指定master地址，但是也可以正常启动spark shell和执行spark shell中的程序，其实是启动了spark的local模式，该模式仅在本机启动一个进程，没有与集群建立联系。

Spark Shell中已经默认将SparkContext类初始化为对象sc。用户代码如果需要用到，则直接应用sc即可

#### 5.2.2.在spark shell中编写WordCount程序
##### 1.首先启动hdfs
##### 2.向hdfs上传一个文件到hdfs://node1.itcast.cn:9000/words.txt
##### 3.在spark shell中用scala语言编写spark程序
```
sc.textFile("hdfs://node1.itcast.cn:9000/words.txt").flatMap(_.split(" "))
.map((_,1)).reduceByKey(_+_).saveAsTextFile("hdfs://node1.itcast.cn:9000/out")
```
##### 4.使用hdfs命令查看结果
```
hdfs dfs -ls hdfs://node1.itcast.cn:9000/out/p*
```
说明：
sc是SparkContext对象，该对象时提交spark程序的入口
textFile(hdfs://node1.itcast.cn:9000/words.txt)是hdfs中读取数据
flatMap(_.split(" "))先map在压平
map((_,1))将单词和1构成元组
reduceByKey(_+_)按照key进行reduce，并将value累加
saveAsTextFile("hdfs://node1.itcast.cn:9000/out")将结果写入到hdfs中

### 5.3.在IDEA中编写WordCount程序
spark shell仅在测试和验证我们的程序时使用的较多，在生产环境中，通常会在IDE中编制程序，然后打成jar包，然后提交到集群，最常用的是创建一个Maven项目，利用Maven来管理jar包的依赖。

1.创建一个项目

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/06.png)

2.选择Maven项目，然后点击next

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/07.png)

3.填写maven的GAV，然后点击next

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/08.png)

4.填写项目名称，然后点击finish

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/09.png)

5.创建好maven项目后，点击Enable Auto-Import

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/10.png)

6.配置Maven的pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.itcast.spark</groupId>
    <artifactId>spark-mvn</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>1.7</maven.compiler.source>
        <maven.compiler.target>1.7</maven.compiler.target>
        <encoding>UTF-8</encoding>
        <scala.version>2.10.6</scala.version>
        <scala.compat.version>2.10</scala.compat.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-library</artifactId>
            <version>${scala.version}</version>
        </dependency>

        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_2.10</artifactId>
            <version>1.5.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-streaming_2.10</artifactId>
            <version>1.5.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-client</artifactId>
            <version>2.6.2</version>
        </dependency>
    </dependencies>

    <build>
        <sourceDirectory>src/main/scala</sourceDirectory>
        <testSourceDirectory>src/test/scala</testSourceDirectory>
        <plugins>
            <plugin>
                <groupId>net.alchim31.maven</groupId>
                <artifactId>scala-maven-plugin</artifactId>
                <version>3.2.0</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>testCompile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-make:transitive</arg>
                                <arg>-dependencyfile</arg>
                                <arg>${project.build.directory}/.scala_dependencies</arg>
                            </args>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.18.1</version>
                <configuration>
                    <useFile>false</useFile>
                    <disableXmlReport>true</disableXmlReport>
                    <includes>
                        <include>**/*Test.*</include>
                        <include>**/*Suite.*</include>
                    </includes>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>2.3</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <filters>
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>cn.itcast.spark.WordCount</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```
7.将src/main/java和src/test/java分别修改成src/main/scala和src/test/scala，与pom.xml中的配置保持一致

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/18-pom-build.png)

8.新建一个scala class，类型为Object

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/12.png)


9.编写spark程序
```
package cn.itcast.spark

import org.apache.spark.{SparkContext, SparkConf}

object WordCount {
  def main(args: Array[String]) {
    //创建SparkConf()并设置App名称
    val conf = new SparkConf().setAppName("WC")
    //创建SparkContext，该对象是提交spark App的入口
    val sc = new SparkContext(conf)
    //使用sc创建RDD并执行相应的transformation和action
    sc.textFile(args(0)).flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_, 1).sortBy(_._2, false).saveAsTextFile(args(1))
    //停止sc，结束该任务
    sc.stop()
  }
}
```

10.使用Maven打包：首先修改pom.xml中的main class

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/17-pom-main-class.png)


点击idea右侧的Maven Project选项

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/13.png)


点击Lifecycle,选择clean和package，然后点击Run Maven Build

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/14.png)


11.选择编译成功的jar包，并将该jar上传到Spark集群中的某个节点上

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/15.png)


12.首先启动hdfs和Spark集群
启动hdfs
```
/usr/local/hadoop-2.6.1/sbin/start-dfs.sh
```
启动spark
```
/usr/local/spark-1.5.2-bin-hadoop2.6/sbin/start-all.sh
```
13.使用spark-submit命令提交Spark应用（注意参数的顺序）
```
/usr/local/spark-1.5.2-bin-hadoop2.6/bin/spark-submit \
--class cn.itcast.spark.WordCount \
--master spark://node1.itcast.cn:7077 \
--executor-memory 2G \
--total-executor-cores 4 \
/root/spark-mvn-1.0-SNAPSHOT.jar \
hdfs://node1.itcast.cn:9000/words.txt \
hdfs://node1.itcast.cn:9000/out
```

查看程序执行结果
```
hdfs dfs -cat hdfs://node1.itcast.cn:9000/out/part-00000
(hello,6)
(tom,3)
(kitty,2)
(jerry,1)
```
### 6.运行模式
#### local(本地模式)
* 采用单节点多线程（cpu)方式运行,是一种OOTB（开箱即用）的方式,只需要在spark-env.sh导出JAVA_HOME,无需其他任何配置即可使用，因而常用于开发和学习
* 方式：./spark-shell - -master local[n] ，n代表线程数
#### Standalone
* 由一个主节点多个从节点组成,主，即为master;从，即为worker
* 集群模式
spark-env.sh
```
SPARK_MASTER_HOST=192.168.137.200 ##配置Master节点
SPARK_WORKER_CORES=2 ##配置应用程序允许使用的核数（默认是所有的core）
SPARK_WORKER_MEMORY=2g  ##配置应用程序允许使用的内存（默认是一个G）

```
slaves
```
192.168.137.200
192.168.137.201
192.168.137.202
```
* 启动集群
```
 sbin/start-all.sh
```
#### Spark on Yarn
* 将Spark应用程序跑在Yarn集群之上，通过Yarn资源调度将executor启动在container中，从而完成driver端分发给executor的各个任务
* 将Spark作业跑在Yarn上，首先需要启动Yarn集群，然后通过spark-shell或spark-submit的方式将作业提交到Yarn上运行
spark-env.sh
```
HADOOP_CONF_DIR=/opt/software/hadoop-2.6.0-cdh5.7.0/etc/hadoop
YARN_CONF_DIR=
```
##### client模式
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/19-spark-on-yarn-client.png)
##### cluster模式
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/20-spark-on-yarn-cluster.png)

#### Spark On Mesos

## 7.[DataFrame&DataSet](http://spark.apache.org/docs/latest/sql-programming-guide.html)

### problem
* 1、DataFrame比RDD有哪些优点？
* 2、DataFrame和Dataset有什么关系？
* 3、有了DataFrame为什么还有引入Dataset？
* 4、Dataset在Spark源码中长什么样？




### 7.1.概述
[快速理解Spark Dataset](https://www.jianshu.com/p/77811ae29fdd)

#### 7.1.1.RDD
##### 概述
弹性分布式数据集，是Spark对数据进行的一种抽象，可以理解为Spark对数据的一种组织方式
*简单:RDD就是一种数据结构，里面包含了数据和操作数据的方法*

##### 特点
###### 弹性：
* 数据可完全放内存或完全放磁盘，也可部分存放在内存，部分存放在磁盘，并可以自动切换
RDD出错后可自动重新计算（通过血缘自动容错）
* 可checkpoint（设置检查点，用于容错），可persist或cache（缓存）
* 里面的数据是分片的（也叫分区，partition），分片的大小可自由设置和细粒度调整

###### 分布式：
* RDD中的数据可存放在多个节点上

###### 数据集：
* 数据的集合
相对于与DataFrame和Dataset，RDD是Spark最底层的抽象，目前是开发者用的最多的，但逐步会转向DataFrame和Dataset（当然，这是Spark的发展趋势）

#### 7.1.2.DataSet
  A Dataset is a distributed collection of data. 
  *Dataset：分布式数据集*  

```
A Dataset is a distributed collection of data. 
Dataset is a new interface added in Spark 1.6 that provides the benefits of RDDs (strong typing, ability to use powerful lambda functions) with the benefits 
of Spark SQL’s optimized execution engine. 
A Dataset can be constructed from JVM objects and then manipulated using functional transformations (map, flatMap, filter, etc.). 
The Dataset API is available in Scala and Java. 
Python does not have the support for the Dataset API. But due to Python’s dynamic nature, many of the benefits of the Dataset API are already available (i.e. you can access the field of a row by name naturally row.columnName). 
The case for R is similar.
```  
  
  
#### 7.1.3.DataFrame 
  A DataFrame is a Dataset organized into named columns.
  * DataFrame：以列（列名，列的类型，列值）的形式构成的分布式数据集*
  
  DataFrame:思想来源于Python的pandas库，RDD是一个数据集，DataFrame在RDD的基础上加了Schema（描述数据的信息，可以认为是元数据，DataFrame曾经就有个名字叫SchemaRDD）

#### 7.2.对比
##### RDD：
* java/scala ⇒ jvm
* python ⇒ python runtime
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/30-rdd-data.png)

##### DataFrame:    
* java/scala/python ⇒ Logic Plan

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/31-dataFrame-data.png)

DataFrame比RDD多了一个表头信息（Schema），像一张表了，DataFrame还配套了新的操作数据的方法，DataFrame API（如df.select())和SQL(select id, name from xx_table where ...)

有了DataFrame这个高一层的抽象后，我们处理数据更加简单了，甚至可以用SQL来处理数据了，对开发者来说，易用性有了很大的提升。

通过DataFrame API或SQL处理数据，会自动经过Spark 优化器（Catalyst）的优化，即使你写的程序或SQL不高效，也可以运行的很快

*DataFrame是用来处理结构化数据的*

###### 结构化和非结构化数据
结构化数据:也称行数据,二维表结构来逻辑表达和实现的数据, 对于表结构的每一列，都有着清晰的定义

非结构化数据:不方便用数据库二维逻辑表来表现的数据,特点是数据结构不规则或不完整，没有预定义的数据模型

##### Dataset
相对于RDD，Dataset提供了强类型支持，也是在RDD的每行数据加了类型约束

数据格式

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/32-Dataset-data.png)

或者这种,每行数据是个Object

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/33-DataSet-data2.png)

DataFrame = Dataset[Row]
Dataset：强类型  typed  case class
DataFrame：弱类型   Row

使用Dataset API的程序，会经过Spark SQL的优化器进行优化
目前仅支持Scala、Java API，尚未提供Python的API

1) 相比DataFrame，Dataset提供了*编译时类型检查*，对于分布式程序来讲，提交一次作业太费劲了（要编译、打包、上传、运行），避免到提交到集群运行时才发现错误，这也是引入Dataset的一个重要原因

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/34-DataFrame.png)

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/35-Dataset.png)

2) RDD转换DataFrame后不可逆，但RDD转换Dataset是可逆的（这也是Dataset产生的原因）

* 启动spark-shell，创建一个RDD

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/36-creae-rdd.png)

* 通过RDD创建DataFrame，再通过DataFrame转换成RDD，发现RDD的类型变成了Row类型

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/37-DataFrame-convert-rdd.png)

* 通过RDD创建Dataset，再通过Dataset转换为RDD，发现RDD还是原始类型

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/38-Dataset-convert-rdd.png)

### 7.2.DataFrame 基本API常用操作

1）DataFrameApp.scala
```
package com.lihaogn.spark

import org.apache.spark.sql.SparkSession

/**
  * DataFrame API基本操作
  */
object DataFrameApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()

    // 将json文件加载成一个dataframe
    val peopleDF = spark.read.format("json").
      load("/Users/Mac/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.json")

    // 输出dataframe对应的schema信息
    peopleDF.printSchema()

    // 输出数据集的前20条记录
    peopleDF.show()

    // 查询某列所有的数据：select name from table-name
    peopleDF.select("name").show()

    // 查询某几列所有的数据，并对列进行计算：select name,age+10 as age2 from table-name
    peopleDF.select(peopleDF.col("name"),(peopleDF.col("age")+10).as("age2")).show()

    // 根据某一列的值进行过滤：select * from table where age>19
    peopleDF.filter(peopleDF.col("age")>19).show()

    // 根据某一列进行分组，然后再进行聚合操作：select age,count(1) from table-name group by age
    peopleDF.groupBy("age").count().show()

    spark.stop()

  }
}
```

2）运行结果 
 
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/24.png)

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/25.png)

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/26.png)

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/27.png)

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/28.png)

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark/29.png)


### 7.3.DataFrame与RDD互操作
  Spark SQL supports two different methods for converting existing RDDs into Datasets. The first method uses reflection to infer the schema of an RDD that contains specific types of objects. This reflection based approach leads to more concise code and works well when you already know the schema while writing your Spark application.
  
  The second method for creating Datasets is through a programmatic interface that allows you to construct a schema and then apply it to an existing RDD. While this method is more verbose, it allows you to construct Datasets when the columns and their types are not known until runtime.


准备操作文件：infos.txt
```
1,zhangsan,20
2,lisi,30
3,wangwu,40
```
1）方式一：反射，前提：事先需要知道字段，字段类型 
2）方式二：编程，如果第一种情况不能满足需求（实现不知道列）

DataFrameRDDApp.scala
```
package com.lihaogn.spark

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
  * DataFrame与RDD互操作
  */
object DataFrameRDDApp {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()

    // 反射方式
//    inferReflection(spark)

    // 编程方式
    program(spark)

    spark.stop()
  }

  def program(spark:SparkSession): Unit ={

    // RDD ==> DataFrame
    val rdd=spark.sparkContext.textFile("/Users/Mac/testdata/infos.txt")

    val infoRDD=rdd.map(_.split(",")).map(line=>Row(line(0).toInt,line(1),line(2).toInt))

    val structType=StructType(Array(StructField("id",IntegerType,true),
      StructField("name",StringType,true),
      StructField("age",IntegerType,true)))

    val infoDF=spark.createDataFrame(infoRDD,structType)
    infoDF.printSchema()
    infoDF.show()

    // 通过df的api操作
    infoDF.filter(infoDF.col("age")>30).show()

    // 通过sql方式操作
    infoDF.createOrReplaceTempView("infos")
    spark.sql("select * from infos where age>30").show()
  }


  def inferReflection(spark:SparkSession): Unit ={

    // RDD ==> DataFrame
    val rdd=spark.sparkContext.textFile("/Users/Mac/testdata/infos.txt")

    // 需要导入隐式转换
    import spark.implicits._
    val infoDF=rdd.map(_.split(",")).map(line=>Info(line(0).toInt,line(1),line(2).toInt)).toDF()

    infoDF.show()

    infoDF.filter(infoDF.col("age")>30).show()

    // sql方式
    infoDF.createOrReplaceTempView("infos")
    spark.sql("select * from infos where age>30").show()
  }

  case class Info(id: Int, name: String, age: Int)

}
```

### 7.4.DataSet

1）使用 
DatasetApp.scala
```
package com.imooc.spark

import org.apache.spark.sql.SparkSession

/**
 * Dataset操作
 */
object DatasetApp {

  def main(args: Array[String]) {
    val spark = SparkSession.builder().appName("DatasetApp")
      .master("local[2]").getOrCreate()

    //注意：需要导入隐式转换
    import spark.implicits._

    val path = "file:///home/hadoop/data/sales.csv"

    //spark如何解析csv文件？
    val df = spark.read.option("header","true").option("inferSchema","true").csv(path)
    df.show

    val ds = df.as[Sales]
    ds.map(line => line.itemId).show

    spark.stop()
  }

  case class Sales(transactionId:Int,customerId:Int,itemId:Int,amountPaid:Double)
}
```
