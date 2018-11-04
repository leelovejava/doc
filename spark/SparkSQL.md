# Spark SQL and DataFrame

## 1.课程目标
### 1.1.掌握Spark SQL的原理

### 1.2.掌握DataFrame数据结构和使用方式

### 1.3.熟练使用Spark SQL完成计算任务

## 2.Spark SQL

### 2.1.Spark SQL概述

### 2.1.1.什么是Spark SQL

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/01.png)
Spark SQL是Spark用来处理结构化数据的一个模块，它提供了一个编程抽象叫做DataFrame并且作为分布式SQL查询引擎的作用。

将数据的计算任务通过SQL的形式转化为RDD的计算,类似于Hive通过SQl的形式将数据的计算任务转换成MapReduce

### 2.1.2.为什么要学习Spark SQL

我们已经学习了Hive，它是将Hive SQL转换成MapReduce然后提交到集群上执行，大大简化了编写MapReduce的程序的复杂性，由于MapReduce这种计算模型执行效率比较慢。所有Spark SQL的应运而生，它是将Spark SQL转换成RDD，然后提交到集群执行，执行效率非常快！

#### 1.易整合

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/02.png)

和Spark core的无缝整合,写RDD的应用时,配置Spark SQL实现逻辑

#### 2.统一的数据访问方式

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/03.png)

Spark 提供了标准化的SQL查询

#### 3.兼容Hive

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/04.png)

Hive的继承,Spark SQL通过内嵌Hive或者连接外部已经部署好的hive实例,实现对Hive语法的继承和操作

#### 4.标准的数据连接

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/05.png)

Spark SQL可以通过thrift Server来支持JDBC、ODBC的访问,将自己作为一个BI Server使用

### 2.2.DataFrames
#### 2.2.1.什么是DataFrames

Spark SQL的数据抽象

与RDD类似，DataFrame也是一个分布式数据容器。然而DataFrame更像传统数据库的二维表格，除了数据以外，还记录数据的结构信息，即schema。同时，与Hive类似，DataFrame也支持嵌套数据类型（struct、array和map）。从API易用性的角度上 看，DataFrame API提供的是一套高层的关系操作，比函数式的RDD API要更加友好，门槛更低。由于与R和Pandas的DataFrame类似，Spark DataFrame很好地继承了传统单机数据分析的开发体验。

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/06.png)
            

#### 2.2.3 RDD vs DataFrames vs DataSet

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/17.png)     

版本的产生
Spark Core->RDD(Spark 1.0)
                
                DataFrame(Spark 1.3)
Spark SQL->     DataSet(Spark 1.6) 

在后期的Spark版本中，DataSet会逐步取代RDD和DataFrame成为唯一的API接口

##### RDD
* RDD是一个懒执行的不可变的可以支持Lambda表达式的并行数据集合。
* RDD的最大好处就是简单，API的人性化程度很高。
* RDD的劣势是性能限制，它是一个JVM驻内存对象，这也就决定了存在GC的限制和数据增加时Java序列化成本的升高

##### Dataframe

与RDD类似，DataFrame也是一个分布式数据容器。

然而DataFrame更像传统数据库的二维表格，除了数据以外，还记录数据的结构信息，即schema。

同时，与Hive类似，DataFrame也支持嵌套数据类型（struct、array和map）。

从API易用性的角度上看，DataFrame API提供的是一套高层的关系操作，比函数式的RDD API要更加友好，门槛更低。

由于与R和Pandas的DataFrame类似，Spark DataFrame很好地继承了传统单机数据分析的开发体验

##### Dataset

1)是Dataframe API的一个扩展，是Spark最新的数据抽象

2)用户友好的API风格，具有**类型安全检查**和Dataframe的查询优化特性。

3)Dataset支持编解码器，当需要访问非堆上的数据时可以避免反序列化整个对象，提高了效率。

4)样例类被用来在Dataset中定义数据的结构信息，样例类中每个属性的名称直接映射到DataSet中的字段名称。

5)Dataframe是Dataset的特列，DataFrame=Dataset[Row] ，所以可以通过as方法将Dataframe转换为Dataset。Row是一个类型，跟Car、Person这些的类型一样，所有的表结构信息我都用Row来表示。

6)DataSet是强类型的。比如可以有Dataset[Car]，Dataset[Person].

DataFrame只是知道字段，但是不知道字段的类型，所以在执行这些操作的时候是没办法在编译的时候检查是否类型失败的，比如你可以对一个String进行减法操作，在执行的时候才报错，而DataSet不仅仅知道字段，而且知道字段类型，所以有更严格的错误检查。就跟JSON对象和类对象之间的类比

##### 共性

1)、RDD、DataFrame、Dataset全都是spark平台下的分布式弹性数据集，为处理超大型数据提供便利
2)、三者都有惰性机制，在进行创建、转换，如map方法时，不会立即执行，只有在遇到Action如foreach时，三者才会开始遍历运算，极端情况下，如果代码里面有创建、转换，但是后面没有在Action中使用对应的结果，在执行时会被直接跳过.
```
val sparkconf = new SparkConf().setMaster("local").setAppName("test").set("spark.port.maxRetries","1000")
val spark = SparkSession.builder().config(sparkconf).getOrCreate()
val rdd=spark.sparkContext.parallelize(Seq(("a", 1), ("b", 1), ("a", 1)))
// map不运行
rdd.map{line=>
  println("运行")
  line._1
}
```
3)、三者都会根据spark的内存情况自动缓存运算，这样即使数据量很大，也不用担心会内存溢出
4)、三者都有partition的概念
5)、三者有许多共同的函数，如filter，排序等
6)、在对DataFrame和Dataset进行操作许多操作都需要这个包进行支持
```
import spark.implicits._
```
7)、DataFrame和Dataset均可使用模式匹配获取各个字段的值和类型
DataFrame:
```
testDF.map{
      case Row(col1:String,col2:Int)=>
        println(col1);println(col2)
        col1
      case _=>
        ""
    }
```
Dataset:
```
case class Coltest(col1:String,col2:Int)extends Serializable //定义字段名和类型
    testDS.map{
      case Coltest(col1:String,col2:Int)=>
        println(col1);println(col2)
        col1
      case _=>
        ""
    }
```

##### 三者的区别

RDD:

1)、RDD一般和spark mlib同时使用

2)、**RDD不支持sparksql操作**

DataFrame:

1)、与RDD和Dataset不同，DataFrame每一行的类型固定为Row，只有通过解析才能获取各个字段的值，如
```
testDF.foreach{
  line =>
    val col1=line.getAs[String]("col1")
    val col2=line.getAs[String]("col2")
}
```
**每一列的值没法直接访问**


2)、DataFrame与Dataset一般与spark ml同时使用

3)、**DataFrame与Dataset均支持sparksql的操作**，比如select，groupby之类，还能注册临时表/视窗，进行sql语句操作，如
```
dataDF.createOrReplaceTempView("tmp")
spark.sql("select  ROW,DATE from tmp where DATE is not null order by DATE").show(100,false)
```

4)、DataFrame与Dataset支持一些特别方便的保存方式，比如保存成csv，可以带上表头，这样每一列的字段名一目了然
```
//保存
val saveoptions = Map("header" -> "true", "delimiter" -> "\t", "path" -> "hdfs://master01:9000/test")
datawDF.write.format("com.atguigu.spark.csv").mode(SaveMode.Overwrite).options(saveoptions).save()
//读取
val options = Map("header" -> "true", "delimiter" -> "\t", "path" -> "hdfs://master01:9000/test")
val datarDF= spark.read.options(options).format("com.atguigu.spark.csv").load()
```
利用这样的保存方式，可以方便的获得字段名和列的对应，而且分隔符（delimiter）可以自由指定
5)、*劣势:编译期间缺少类型安全检查、运行期检查*

Dataset:

**Dataset和DataFrame拥有完全相同的成员函数，区别只是每一行的数据类型不同。**
**DataFrame也可以叫Dataset[Row],每一行的类型是Row，不解析，每一行究竟有哪些字段，各个字段又是什么类型都无从得知，只能用上面提到的getAS方法或者共性中的第七条提到的模式匹配拿出特定字段**
**而Dataset中，每一行是什么类型是不一定的，在自定义了case class之后可以很自由的获得每一行的信息**
```
case class Coltest(col1:String,col2:Int)extends Serializable //定义字段名和类型
/**
 rdd
 ("a", 1)
 ("b", 1)
 ("a", 1)
**/
val test: Dataset[Coltest]=rdd.map{line=>
      Coltest(line._1,line._2)
    }.toDS
test.map{
      line=>
        println(line.col1)
        println(line.col2)
    }
```
可以看出，Dataset在需要访问列中的某个字段时是非常方便的，然而，如果要写一些适配性很强的函数时，如果使用Dataset，行的类型又不确定，可能是各种case class，无法实现适配，这时候用DataFrame即Dataset[Row]就能比较好的解决问题

#### 2.2.4.创建DataFrames
 在Spark SQL中SQLContext是创建DataFrames和执行SQL的入口，在spark-1.5.2中已经内置了一个sqlContext

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/07.png)

1.在本地创建一个文件，有三列，分别是id、name、age，用空格分隔，然后上传到hdfs上
hdfs dfs -put person.txt /

2.在spark shell执行下面命令，读取数据，将每一行的数据使用列分隔符分割
val lineRDD = sc.textFile("hdfs://node1.itcast.cn:9000/person.txt").map(_.split(" "))

3.定义case class（相当于表的schema）
case class Person(id:Int, name:String, age:Int)

4.将RDD和case class关联
val personRDD = lineRDD.map(x => Person(x(0).toInt, x(1), x(2).toInt))

5.将RDD转换成DataFrame
val personDF = personRDD.toDF

6.对DataFrame进行处理
personDF.show
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/08.png)

### 2.3.DataFrame常用操作
#### 2.3.1.DSL风格语法
//查看DataFrame中的内容
personDF.show

//查看DataFrame部分列中的内容
personDF.select(personDF.col("name")).show
personDF.select(col("name"), col("age")).show
personDF.select("name").show

//打印DataFrame的Schema信息
personDF.printSchema

//查询所有的name和age，并将age+1
personDF.select(col("id"), col("name"), col("age") + 1).show
personDF.select(personDF("id"), personDF("name"), personDF("age") + 1).show
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/09.png)

//过滤age大于等于18的
personDF.filter(col("age") >= 18).show
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/10.png)

//按年龄进行分组并统计相同年龄的人数
personDF.groupBy("age").count().show()
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/11.png)

#### 2.3.2.SQL风格语法
如果想使用SQL风格的语法，需要将DataFrame注册成表
personDF.registerTempTable("t_person")

//查询年龄最大的前两名
sqlContext.sql("select * from t_person order by age desc limit 2").show

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/12.png)

//显示表的Schema信息
sqlContext.sql("desc t_person").show

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/13.png)

## 3.以编程方式执行Spark SQL查询
### 3.1.编写Spark SQL查询程序
前面我们学习了如何在Spark Shell中使用SQL完成查询，现在我们来实现在自定义的程序中编写Spark SQL查询程序。首先在maven项目的pom.xml中添加Spark SQL的依赖
```xml
<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-sql_2.10</artifactId>
    <version>1.5.2</version>
</dependency>
```
#### 3.1.1.通过反射推断Schema
创建一个object为cn.itcast.spark.sql.InferringSchema
```
package cn.itcast.spark.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object InferringSchema {
  def main(args: Array[String]) {

    //创建SparkConf()并设置App名称
    val conf = new SparkConf().setAppName("SQL-1")
    //SQLContext要依赖SparkContext
    val sc = new SparkContext(conf)
    //创建SQLContext
    val sqlContext = new SQLContext(sc)

    //从指定的地址创建RDD
    val lineRDD = sc.textFile(args(0)).map(_.split(" "))

    //创建case class
    //将RDD和case class关联
    val personRDD = lineRDD.map(x => Person(x(0).toInt, x(1), x(2).toInt))
    //导入隐式转换，如果不到人无法将RDD转换成DataFrame
    // 隐式转换函数是以implicits关键字声明的带有单个参数的函数.这种函数将会自动转换应用,将值从一种类型转换为另一种类型
    //将RDD转换成DataFrame
    import sqlContext.implicits._
    val personDF = personRDD.toDF
    //注册表
    personDF.registerTempTable("t_person")
    //传入SQL
    val df = sqlContext.sql("select * from t_person order by age desc limit 2")
    //将结果以JSON的方式存储到指定位置
    df.write.json(args(1))
    //停止Spark Context
    sc.stop()
  }
}

//case class一定要放到外面
case class Person(id: Int, name: String, age: Int)
```
将程序打成jar包，上传到spark集群，提交Spark任务
```
/usr/local/spark-1.5.2-bin-hadoop2.6/bin/spark-submit \
--class cn.itcast.spark.sql.InferringSchema \
--master spark://node1.itcast.cn:7077 \
/root/spark-mvn-1.0-SNAPSHOT.jar \
hdfs://node1.itcast.cn:9000/person.txt \
hdfs://node1.itcast.cn:9000/out 
```
查看运行结果
hdfs dfs -cat  hdfs://node1.itcast.cn:9000/out/part-r-*

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/14.png)

#### 3.1.2.通过StructType直接指定Schema
创建一个object为cn.itcast.spark.sql.SpecifyingSchema
```
package cn.itcast.spark.sql

import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types._
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by ZX on 2015/12/11.
  */
object SpecifyingSchema {
  def main(args: Array[String]) {
    //创建SparkConf()并设置App名称
    val conf = new SparkConf().setAppName("SQL-2")
    //SQLContext要依赖SparkContext
    val sc = new SparkContext(conf)
    //创建SQLContext
    val sqlContext = new SQLContext(sc)
    //从指定的地址创建RDD
    val personRDD = sc.textFile(args(0)).map(_.split(" "))
    //通过StructType直接指定每个字段的schema
    val schema = StructType(
      List(
        StructField("id", IntegerType, true),
        StructField("name", StringType, true),
        StructField("age", IntegerType, true)
      )
    )
    //将RDD映射到rowRDD
    val rowRDD = personRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).toInt))
    //将schema信息应用到rowRDD上
    val personDataFrame = sqlContext.createDataFrame(rowRDD, schema)
    //注册表
    personDataFrame.registerTempTable("t_person")
    //执行SQL
    val df = sqlContext.sql("select * from t_person order by age desc limit 4")
    //将结果以JSON的方式存储到指定位置
    df.write.json(args(1))
    //停止Spark Context
    sc.stop()
  }
}
```
将程序打成jar包，上传到spark集群，提交Spark任务
```
/usr/local/spark-1.5.2-bin-hadoop2.6/bin/spark-submit \
--class cn.itcast.spark.sql.InferringSchema \
--master spark://node1.itcast.cn:7077 \
/root/spark-mvn-1.0-SNAPSHOT.jar \
hdfs://node1.itcast.cn:9000/person.txt \
hdfs://node1.itcast.cn:9000/out1 
```
查看结果
hdfs dfs -cat  hdfs://node1.itcast.cn:9000/out1/part-r-*

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/15.png)

## 4.数据源
### 4.1.JDBC
Spark SQL可以通过JDBC从关系型数据库中读取数据的方式创建DataFrame，通过对DataFrame一系列的计算后，还可以将数据再写回关系型数据库中。

### 4.1.1.从MySQL中加载数据（Spark Shell方式）
#### 1.启动Spark Shell，必须指定mysql连接驱动jar包
```
/usr/local/spark-1.5.2-bin-hadoop2.6/bin/spark-shell \
--master spark://node1.itcast.cn:7077 \
--jars /usr/local/spark-1.5.2-bin-hadoop2.6/mysql-connector-java-5.1.35-bin.jar \
--driver-class-path /usr/local/spark-1.5.2-bin-hadoop2.6/mysql-connector-java-5.1.35-bin.jar 
```
#### 2.从mysql中加载数据
```
val jdbcDF = sqlContext.read.format("jdbc").options(Map("url" -> "jdbc:mysql://192.168.10.1:3306/bigdata", "driver" -> "com.mysql.jdbc.Driver", "dbtable" -> "person", "user" -> "root", "password" -> "123456")).load()
```
##### 3.执行查询
jdbcDF.show()

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-sql/16.png)

### 4.1.2.将数据写入到MySQL中（打jar包方式）
#### 1.编写Spark SQL程序
```
package cn.itcast.spark.sql

import java.util.Properties
import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.sql.types.{StringType, IntegerType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

object JdbcRDD {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("MySQL-Demo")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    //通过并行化创建RDD
    val personRDD = sc.parallelize(Array("1 tom 5", "2 jerry 3", "3 kitty 6")).map(_.split(" "))
    //通过StructType直接指定每个字段的schema
    val schema = StructType(
      List(
        StructField("id", IntegerType, true),
        StructField("name", StringType, true),
        StructField("age", IntegerType, true)
      )
    )
    //将RDD映射到rowRDD
    val rowRDD = personRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).toInt))
    //将schema信息应用到rowRDD上
    val personDataFrame = sqlContext.createDataFrame(rowRDD, schema)
    //创建Properties存储数据库相关属性
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "123456")
    //将数据追加到数据库
    personDataFrame.write.mode("append").jdbc("jdbc:mysql://192.168.10.1:3306/bigdata", "bigdata.person", prop)
    //停止SparkContext
    sc.stop()
  }
}
```

#### 2.用maven将程序打包

#### 3.将Jar包提交到spark集群
```
/usr/local/spark-1.5.2-bin-hadoop2.6/bin/spark-submit \
--class cn.itcast.spark.sql.JdbcRDD \
--master spark://node1.itcast.cn:7077 \
--jars /usr/local/spark-1.5.2-bin-hadoop2.6/mysql-connector-java-5.1.35-bin.jar \
--driver-class-path /usr/local/spark-1.5.2-bin-hadoop2.6/mysql-connector-java-5.1.35-bin.jar \
/root/spark-mvn-1.0-SNAPSHOT.jar 
```