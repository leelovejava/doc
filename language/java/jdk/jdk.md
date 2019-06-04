# jdk

[Java 5～11各个版本新特性史上最全总结](https://mp.weixin.qq.com/s/6PgdGCulBm3Q5o75MJQVAA)

[从Java5到Java12每个版本的新特性（1）](https://www.jianshu.com/p/a051a2f0c3ab)

[从Java5到Java12每个版本的新特性（2）](https://www.jianshu.com/p/e5fba5376371)

[Oracle JDK和 OpenJDK 之间的区别](https://mp.weixin.qq.com/s/kpuCYqzQtpK7Fja6ZRbPZw)

## 配置多版本的jdk

windows版本：修改JAVA_HOME

linux/macOS:
 * 设置多版本环境变量: JAVA_8_HOME JAVA_11_HOME JAVA_12_HOME
 * 配置默认的环境变量 export JAVA_HOME = $JAVA_11_HOME
 * 设置命令别名,动态切换JDK版本: alias      alias jdk11 = "export JAVA_HOME=$JAVA_11_HOME"
 * 查看JDK版本信息: java-version

## [jdk12](http://openjdk.java.net/jeps/326)

[download](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html)

[189	Shenandoah: A Low-Pause-Time Garbage Collector (Experimental)    低暂停时间的 GC](http://openjdk.java.net/jeps/189)

[230	Microbenchmark Suite    微基准测试套件](http://openjdk.java.net/jeps/189)

[325	Switch Expressions (Preview)    Switch 表达式](http://openjdk.java.net/jeps/189)

[334	JVM Constants API    JVM 常量 API](http://openjdk.java.net/jeps/334)

[340	One AArch64 Port, Not Two    只保留一个 AArch64 实现](http://openjdk.java.net/jeps/340)

[341	Default CDS Archives    默认类数据共享归档文件](http://openjdk.java.net/jeps/340)

[344	Abortable Mixed Collections for G1    可中止的 G1 Mixed GC](http://openjdk.java.net/jeps/344)

[346	Promptly Return Unused Committed Memory from G1    G1 及时返回未使用的已分配内存](http://openjdk.java.net/jeps/346)