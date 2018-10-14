# Scala

## 简介
    java语言的脚本化(JVM的高层次语言)
    面向对象+面向过程(函数式编程)
    
    java和scala相互调用
    
    静态类型
    性能与Java差不多
    通常不需要显式写出类型(类型推断机制)
    
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
```    