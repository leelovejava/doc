# Scala

## doc

[挑逗 Java 程序员的那些 Scala 绝技](https://mp.weixin.qq.com/s/gTCGSa6UkxpdaeO2qzWovw)

## 简介
  [官网](https://www.scala-lang.org/)             
 
* java语言的脚本化(JVM的高层次语言)
* 面向对象+面向过程(函数式编程)
* java和scala相互调用(scala运行在jvm)
* 静态类型语言
* 性能与Java差不多(并发性)
* 类型推断机制(通常不需要显式写出类型)
    
    
Scala combines object-oriented and functional programming in one concise, high-level language. 
Scala's static types help avoid bugs in complex applications, and its JVM and JavaScript runtimes let you build high-performance systems with easy access to huge ecosystems of libraries

Scala是一个面向对象和函数式编程的语言,是一个高级别的语言
Scala的静态类型能够帮助我们在复杂的应用程序避免掉许多bug,在jvm和javascript的运行环境中是高性能的系统,容易的访问生态系统中已有的jar包 


## 学习Scala的意义
1) 钱
2) 做东西:Spark Kafka Flink(生态系统)
           优雅
           开发速度
           生态系统

##  Scala对比Java
### 定义变量和函数
```
// 变量
var x:Int=6
var x=6 // 类型推断
var y="scala"

// 函数
def square(x:Int):Int =x*x;
def square(x:Int)={x*x}
def annunce(text:String) {
    println(text);
}   
```

```
// 变量
int x=6;
final String y="scala";
int square(int x) {
    return x*x;
}

// 函数
void announce(String text) {
    Sysstem.out.println(text);
}

// 定义
def 方法名(参数名:参数类型):返回类型={
    // 括号内的叫做方法体
    
    // 方法体内的最后一行为返回值,不需要使用return
}
```

## 循环

1  to 10
1.to(10)

## 隐式转换和隐式参数

### 隐式转换
```scala
package cn.itcast.impli

import java.io.File
import scala.io.Source


//隐式的增强File类的方法
class RichFile(val from: File) {
  def read = Source.fromFile(from.getPath).mkString
}

object RichFile {
  //隐式转换方法
  implicit def file2RichFile(from: File) = new RichFile(from)

}

object MainApp{
  def main(args: Array[String]): Unit = {
    //导入隐式转换
    import RichFile._
    //import RichFile.file2RichFile
    println(new File("c://words.txt").read)

  }
}
```    