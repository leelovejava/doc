#  [码出规范：《阿里巴巴Java开发手册》详解](http://www.imooc.com/read/55) 

## 01 开篇词：为什么学习本专栏

更新时间：2019-10-30 10:06:34

![img](http://img.mukewang.com/5db6b8130001dea906400359.jpg)

![img](http://www.imooc.com/static/img/column/bg-l.png)![img](http://www.imooc.com/static/img/column/bg-r.png)

你好，我是明明如月，一个重视方法、喜欢思考的 Java 高级开发工程师。

相信很多程序员都希望自己能够找到一些宝典，通过修炼 “打开任督二脉”，从此快速进阶成为高手。《Java 开发手册》[1](http://www.imooc.com/read/55/article/1138#fn1)（以下简称《手册》）就是诸多宝典之一，它几乎是每个 Java 工程师人手必备的一本参考指南。该手册包括 **编程规约、异常日志、单元测试、安全规约、MySQL 数据库、工程结构、设计规约 7 个部分** ，涵盖了 Java 开发的常见知识点。认真实践该《手册》能够帮助 Java 开发者养成好的编程习惯，帮助企业的开发团队在 Java 开发上更加高效、提高容错性、团队协作更好，并有助于提高代码的质量、降低项目维护的难度。然而很多人会遇到看过就忘，记住却不理解、不会用的困境。

另外在实际的学习和工作中，你是否遇到过如下尴尬：

1. 看《手册》等 Java 技术图书时觉得啥都懂，实战时就忘了；
2. 很多知识点，知其然而不知其所以然，面试时多问你几个为什么就 “靓仔语塞”；
3. 想通过读源码来进阶，但是容易迷失在细节中，总是半途而废；
4. 不重视需求分析，导致开发完成才意识到设计和需求有偏差；
5. 遇到问题时如果无法简单地定位原因，会优先通过百度、请教别人来解决问题；
6. 开发中遇到问题排查耗时很久，方法很原始；
7. 自己开发的项目，每次上线几乎必出 BUG，而有些同事的项目质量则很高，自己却不知道如何才能尽可能地避免。

结合自己学习和工作这么长时间的思考，将出现上述尴尬的原因归结为以下几个原因：

1. **知道很容易，懂很难，很多人把知道当做懂**。自认为掌握了就不愿意再深入学习，恰恰错过了彻底掌握该知识的最佳机会；
2. **专业基础不够扎实。** 很多人急于求成，只重视解决眼前问题，不能够未雨绸缪，巩固好专业基础，最终导致很多问题” 知其然而不知其所以然 “，排查问题时靠猜、靠问，而不是靠扎实的专业基础之上的推测和验证；
3. 很多人**不愿意改变学习方法，学习和培养好的编程习惯，不敢走出舒适区**。比如很多人学了很多技术，却从来没有认真仔细阅读过官方文档；比如读源码毫无章法，随心所欲，常常半途而废；
4. **态度决定一切**。很多人嘴上说想学好，但是对自己代码要求很低，总是为自己找各种理由不去学更好的方法，不去努力写更优雅的代码；
5. 在学习技术过程中，很多人**把脑力劳动当成了体力劳动**，**把需要思考的问题当做了纯记忆的问题**，学习和工作过程中缺乏思考。比如很多人是 “记忆” 经典图书的知识点，而不是理解知识点，导致容易遗忘，不能灵活运用。在学习很多知识点时缺乏思考，没有去搞懂是什么、不明白为什么、不知道如何去做；
6. **没有养成好的解决问题的习惯，排查问题靠猜，而不是思考和验证**。也没有主动掌握常见的排查问题的步骤和工具等。

很多人缺乏的不只是好的资料，而是学习的方法。学一样的技术，使用不同的方法，最终学习的效果截然不同。而**技术是学不完的**，如果没有科学的方法，无法很好地应对层出不穷的新技术。**每个人的成长速度是不同的**，有的人工作多年，却只有一年的技术经验；而有的人工作一年，却有超越一年的技术经验。造成这种差异的主要原因在于**学习能力**。

**从 Java 新手到高手的进阶过程是一个漫长的爬坑过程。** 很多同学遇到 BUG 时由于基础不扎实也没有系统地排查方法，为了解决一个小问题浪费了大把的时间。而且写出的 BUG 太多将直接或间接影响绩效，影响同事、领导对你的印象。

**阻碍初学者进步的往往是一叶障目不见泰山的盲目自信，往往是一成不变学习方法。破解上述尴尬的核心在于提高学习和排错能力**。

为了解决上面提出诸多尴尬，本专栏的具体应对策略如下：

1. 从学习方法主要切入点，结合源码，Java 语言规范 [2](http://www.imooc.com/read/55/article/1138#fn2) 和 Java 虚拟机规范 [3](http://www.imooc.com/read/55/article/1138#fn3) 等对《手册》的讲解和补充；
2. 设计者角度思考问题，很多知识点将从设计者视角去思考分析问题，更容易理解问题的根源；
3. 通过对开发中常用的思维导图、流程图和常见 UML 图的讲解，让大家可以 “大战需求分析”，前期明确需求，后期少返工；
4. 通过单元测试、Code Review 等相关知识的学习和运用，促进代码质量的提升
5. 通过独特的学习源码视角，来从正确的角度和方法来学习源码的精髓，反向促进日常的开发；
6. 结合实际的开发经验，给出相关知识点掌握不牢容易造成的坑，给出一些避坑建议。

本专栏大多数章节的结构设计如下：

1. 逻辑特色：采用 2w1h 分析方法，即是什么（what），为什么（why）和如何去做（how）的角度来学习知识；
2. 问题驱动：采用 "5w 思考法"，即不断的追问逐渐思考问题的本质，从而实现知识理解的更加深入；
3. 方法驱动：每节将使用一些学习和解决问题的方法，让大家可以掌握学习的章法；
4. 对比和类比分析：大多数章节会对知识点和类似的知识点进行对比或类比，从而找出知识之间的联系和差异，加深对知识的理解；
5. 坑点解读：讲解知识理解不到位可能造成的坑点，分析趟坑原因并给出避坑建议。

**技术是学不完的，学习能力和态度才是进阶的关键**。作为一个技术人员，只有保持 “Stay Hungry， Stay Foolish” 的心态，才能够保持进取心；只有真正知道哪些才是更有价值的东西，才能真正少走弯路。

希望大家能够通本专栏的学习，改变学习技术的思维意识，**从 “学习具体内容” 为主，转变到学习 “学习的方法” 为主**；**从技术的学习者变为技术的思考者**。希望本专栏可以帮助到更多朋友加速技术成长的步伐，做一个更加专业和优秀的 Java 工程师。



### 参考资料

------

1. 阿里巴巴与 Java 社区开发者.《 Java 开发手册 1.5.0》华山版. 2019 [↩︎](http://www.imooc.com/read/55/article/1138#fnref1)
2. James Gosling, Bill Joy, Guy Steele, Gilad Bracha, Alex Buckley.《Java Language Specification: Java SE 8 Edition》. 2015 [↩︎](http://www.imooc.com/read/55/article/1138#fnref2)
3. Tim Lindholm, Frank Yellin, Gilad Bracha, Alex Buckley.《Java Language Specification: Java SE 8 Edition》. 2015 [↩︎](http://www.imooc.com/read/55/article/1138#fnref3)

## 02 Integer缓存问题分析

更新时间：2019-11-10 13:22:38

![img](http://img.mukewang.com/5db6b8280001f1af06400359.jpg)

![img](http://www.imooc.com/static/img/column/bg-l.png)

![img](http://www.imooc.com/static/img/column/bg-r.png)



### 1.前言

《手册》第 7 页有一段关于包装对象之间值的比较问题的规约[1](http://www.imooc.com/read/55/article/1139#fn1)：

> 【强制】所有整型包装类对象之间值的比较，全部使用 equals方法比较。
> 说明: 对于 Integer var = ? 在-128 至 127 范围内的赋值，Integer 对象是在 IntegerCache.cache 产 生，会复用已有对象，这个区间内的 Integer 值可以直接使用 == 进行判断，但是这个区间之外的所有数据，都会在堆上产生，并不会复用已有对象，这是一个大坑，推荐使用 equals 方法进行判断。

这条建议非常值得大家关注， 而且该问题在 Java 面试中十分常见。

我们还需要思考以下几个问题：

- 如果不看《手册》，我们如何知道 `Integer var = ?`会缓存 -128 到 127之间的赋值？
- 为什么会缓存这个范围的赋值？
- 我们如何学习和分析类似的问题？



### 2. Integer 缓存问题分析

我们先看下面的示例代码，并思考该段代码的输出结果：

```java
public class IntTest {
	public static void main(String[] args) {
	    Integer a = 100, b = 100, c = 150, d = 150;
	    System.out.println(a == b);
	    System.out.println(c == d);
	}
}
```

通过运行代码可以得到答案，程序输出的结果分别为： `true` , `false`。

**那么为什么答案是这样？**

结合《手册》的描述很多人可能会颇有自信地回答：**因为缓存了 -128 到 127 之间的数值**，就没有然后了。

那么为什么会缓存这一段区间的数值？缓存的区间可以修改吗？其它的包装类型有没有类似缓存？

**what? 咋还有这么多问题？这谁知道啊**！

莫急，且看下面的分析。



#### 2.1 源码分析法

首先我们可以通过源码对该问题进行分析。

我们知道，`Integer var = ?` 形式声明变量，会通过 `java.lang.Integer#valueOf(int)`来构造 `Integer`对象。

很多人可能会说：“你咋能知道这个呢”？

如果不信大家可以打断点，运行程序后会调到这里，总该信了吧？（后面还会再作解释）。

我们先看该函数源码：

```java
/**
 * Returns an {@code Integer} instance representing the specified
 * {@code int} value.  If a new {@code Integer} instance is not
 * required, this method should generally be used in preference to
 * the constructor {@link #Integer(int)}, as this method is likely
 * to yield significantly better space and time performance by
 * caching frequently requested values.
 *
 * This method will always cache values in the range -128 to 127,
 * inclusive, and may cache other values outside of this range.
 *
 * @param  i an {@code int} value.
 * @return an {@code Integer} instance representing {@code i}.
 * @since  1.5
 */
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

通过源码可以看出，如果用 `Ineger.valueOf(int)` 来创建整数对象，参数大于等于整数缓存的最小值（ `IntegerCache.low` ）并小于等于整数缓存的最大值（ `IntegerCache.high`）, 会直接从缓存数组(`java.lang.Integer.IntegerCache#cache`) 中提取整数对象; 否则会 `new` 一个整数对象。

**那么这里的缓存最大和最小值分别是多少呢？**

从上述注释中我们可以看出，最小值是 -128, 最大值是127。

**那么为什么会缓存这一段区间的整数对象呢？**

通过注释我们可以得知：**如果不要求必须新建一个整型对象，缓存最常用的值（提前构造缓存范围内的整型对象），会更省空间，速度也更快。**

这给我们一个非常重要的启发：

> 如果想减少内存占用，提高程序运行的效率，可以将常用的对象提前缓存起来，需要时直接从缓存中提取。

那么我们再思考下一个问题： **`Integer` 缓存的区间可以修改吗？**

通过上述源码和注释我们还无法回答这个问题，接下来，我们继续看 `java.lang.Integer.IntegerCache` 的源码：

```java
/**
 * Cache to support the object identity semantics of autoboxing for values between
 * -128 and 127 (inclusive) as required by JLS.
 *
 * The cache is initialized on first usage.  The size of the cache
 * may be controlled by the {@code -XX:AutoBoxCacheMax=<size>} option.
 * During VM initialization, java.lang.Integer.IntegerCache.high property
 * may be set and saved in the private system properties in the
 * sun.misc.VM class.
 */

private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];
    static {
            // high value may be configured by property
            int h = 127;
            String integerCacheHighPropValue =
                sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
           // 省略其它代码
    }
      // 省略其它代码
}
```

通过 `IntegerCache` 代码和注释我们可以看到，最小值是固定值 -128， 最大值并不是固定值，缓存的最大值是可以通过虚拟机参数 `-XX:AutoBoxCacheMax=}` 或 `-Djava.lang.Integer.IntegerCache.high=` 来设置的，未指定则为127。

因此可以通过修改这两个参数其中之一，让缓存的最大值大于等于150。

如果作出这种修改，示例的输出结果便会是： `true`,`true`。

**学到这里是不是发现，对此问题的理解和最初的想法有些不同呢？**

这段注释也解答了为什么要缓存这个范围的数据：

> **是为了自动装箱时可以复用这些对象**，这也是JLS[2](http://www.imooc.com/read/55/article/1139#fn2) 的要求。

我们可以参考 JLS 的 [Boxing Conversion 部分](https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1.7)的相关描述。

> If the value`p`being boxed is an integer literal of type `int`between `-128`and `127`inclusive ([§3.10.1](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10.1)), or the boolean literal `true`or`false`([§3.10.3](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10.3)), or a character literal between `'\u0000'`and `'\u007f'`inclusive ([§3.10.4](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10.4)), then let `a`and `b`be the results of any two boxing conversions of `p`. It is always the case that `a`==`b`.
>
> 在 -128 到 127 （含）之间的 int 类型的值，或者 boolean类型的 true 或false， 以及范围在’\u0000’和’\u007f’ （含）之间的 char 类型的数值 p， 自动包装成 a 和 b 两个对象时， 可以使用 a == b 判断 a 和 b的值是否相等。



#### 2.2 反汇编法

那么究竟 `Integer var = ?` 形式声明变量，是不是通过 `java.lang.Integer#valueOf(int)`来构造 `Integer`对象呢？ 总不能都是猜测 N 个可能的函数，然后断点调试吧？

**如果遇到其它类似的问题，没人告诉我底层调用了哪个方法，该怎么办？** 囧…

这类问题有个杀手锏，可以通过对编译后的 class 文件进行反汇编来查看。

首先编译源代码：`javac IntTest.java`

然后需要对代码进行反汇编，执行：`javap -c IntTest`

> 如果想了解 `javap` 的用法，直接输入 `javap -help` 查看用法提示（很多命令行工具都支持 `-help` 或 `--help` 给出用法提示）。
> ![图片描述](http://img.mukewang.com/5db654e80001823606050363.png)

反编译后，我们得到以下代码：

```java
Compiled from "IntTest.java"
public class com.chujianyun.common.int_test.IntTest {
  public com.chujianyun.common.int_test.IntTest();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: bipush        100
       2: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
       5: astore_1
       6: bipush        100
       8: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      11: astore_2
      12: sipush        150
      15: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      18: astore_3
      19: sipush        150
      22: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      25: astore        4
      27: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
      30: aload_1
      31: aload_2
      32: if_acmpne     39
      35: iconst_1
      36: goto          40
      39: iconst_0
      40: invokevirtual #4                  // Method java/io/PrintStream.println:(Z)V
      43: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
      46: aload_3
      47: aload         4
      49: if_acmpne     56
      52: iconst_1
      53: goto          57
      56: iconst_0
      57: invokevirtual #4                  // Method java/io/PrintStream.println:(Z)V
      60: return
}
```

可以明确得"看到" 这四个 ``Integer var = ?`形式声明的变量的确是通过`java.lang.Integer#valueOf(int)`来构造`Integer`对象的。

**接下来对汇编后的代码进行详细分析，如果看不懂可略过**：

根据[《Java Virtual Machine Specification : Java SE 8 Edition》](https://docs.oracle.com/javase/specs/jvms/se8/html/index.html)[3](http://www.imooc.com/read/55/article/1139#fn3)，后缩写为 JVMS , 第 6 章 [虚拟机指令集](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html)的相关描述以及《深入理解 Java 虚拟机》[4](http://www.imooc.com/read/55/article/1139#fn4) 414-149 页的 附录B “虚拟机字节码指令表”。 我们对上述指令进行解读：

偏移为 0 的指令为：`bipush 100` ，其含义是将单字节整型常量 100 推入操作数栈的栈顶；

偏移为 2 的指令为：`invokestatic #2 // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;` 表示调用一个 `static` 函数，即 `java.lang.Integer#valueOf(int)`；

偏移为 5 的指令为：`astore_1` ，其含义是从操作数栈中弹出对象引用，然后将其存到第 1 个局部变量Slot中；

偏移 6 到 25 的指令和上面类似；

偏移为 30 的指令为 `aload_1` ，其含义是从第 1 个局部变量Slot取出对象引用（即 a），并将其压入栈；

偏移为 31 的指令为 `aload_2` ，其含义是从第 2 个局部变量Slot取出对象引用（即 b），并将其压入栈；

偏移为 32 的指令为`if_acmpn`，该指令为条件跳转指令，`if_` 后以 a开头表示对象的引用比较。

由于该指令有以下特性：

> - `if_acmpeq`比较栈两个引用类型数值，相等则跳转
> - `if_acmpne`比较栈两个引用类型数值，不相等则跳转

由于 `Integer` 的缓存问题，所以 a 和 b 引用指向同一个地址，因此此条件不成立（成立则跳转到偏移为 39 的指令处），执行偏移为 35 的指令。

偏移为 35 的指令: `iconst_1`，其含义为将常量 1 压栈（ Java 虚拟机中 boolean 类型的运算类型为 int ，其中 true 用 1 表示，详见 [2.11.1 数据类型和 Java 虚拟机](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.11.1)。

然后执行偏移为 36 的 `goto` 指令，跳转到偏移为 40 的指令。

偏移为 40 的指令：`invokevirtual #4 // Method java/io/PrintStream.println:(Z)V`。

可知参数描述符为 `Z` ，返回值描述符为 `V`。

根据[4.3.2 字段描述符](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.2) ，可知 `FieldType` 的字符为 `Z` 表示 `boolean` 类型， 值为 `true`或`false`。
根据 [4.3.3 字段描述符](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.3) ，可知返回值为 `void`。

因此可以知，最终调用了 `java.io.PrintStream#println(boolean)` 函数打印栈顶常量即 `true`。

然后比较执行偏移 43 到 57 之间的指令，比较 c 和 d， 打印 `false` 。

执行偏移为 60 的指令，即 `retrun` ，程序结束。

可能有些朋友会对反汇编的代码有些抵触和恐惧，这都是非常正常的现象。

我们分析和研究问题的时候，**看懂核心逻辑即可**，不要纠结于细节，而失去了重点。

一回生两回熟，随着遇到的例子越来越多，遇到类似的问题时，会喜欢上 `javap` 来分析和解决问题。

如果想深入学习 java 反汇编，强烈建议结合官方的 JVMS 或其中文版:《Java 虚拟机规范》这本书进行拓展学习。

如果大家不喜欢命令行的方式进行 Java 的反汇编，这里推荐一个简单易用的可视化工具：[classpy](https://github.com/zxh0/classpy) ，大家可以自行了解学习。
![图片描述](http://img.mukewang.com/5db655530001a19710690822.png)



[Integer的valueOf() 为255 问题详解](https://www.imooc.com/article/294578)

### 3.Long的缓存问题分析

我们学习的目的之一就是要学会举一反三。因此我们对 `Long` 也进行类似的研究，探究两者之间有何异同。



#### 3.1 源码分析

类似的，我们接下来分析 `java.lang.Long#valueOf(long)`的源码：

```java
/**
 * Returns a {@code Long} instance representing the specified
 * {@code long} value.
 * If a new {@code Long} instance is not required, this method
 * should generally be used in preference to the constructor
 * {@link #Long(long)}, as this method is likely to yield
 * significantly better space and time performance by caching
 * frequently requested values.
 *
 * Note that unlike the {@linkplain Integer#valueOf(int)
 * corresponding method} in the {@code Integer} class, this method
 * is <em>not</em> required to cache values within a particular
 * range.
 *
 * @param  l a long value.
 * @return a {@code Long} instance representing {@code l}.
 * @since  1.5
 */
public static Long valueOf(long l) {
    final int offset = 128;
    if (l >= -128 && l <= 127) { // will cache
        return LongCache.cache[(int)l + offset];
    }
    return new Long(l);
}
```

发现该函数的写法和 `Ineger.valueOf(int)` 非常相似。

我们同样也看到， `Long` 也用到了缓存。 使用`java.lang.Long#valueOf(long)`构造`Long`对象时，值在 **[-128, 127]** 之间的`Long`对象直接从缓存对象数组中提取。

而且注释同样也提到了：**缓存的目的是为了提高性能**。

但是通过注释我们发现这么一段提示：

> Note that unlike the {@linkplain Integer#valueOf(int) corresponding method} in the {@code Integer} class, this method is *not* required to cache values within a particular range.
>
> 注意：和 `Ineger.valueOf(int)` 不同的是，此方法并没有被要求缓存特定范围的值。

这也正是上面源码中缓存范围判断的注释为何用 `// will cache` 的原因（可以对比一下上面 `Integer` 的缓存的注释）。

因此我们可知，虽然此处采用了缓存，但应该不是 JLS 的要求。

**那么 `Long` 类型的缓存是如何构造的呢？**

我们查看缓存数组的构造：

```java
private static class LongCache {
    private LongCache(){}

    static final Long cache[] = new Long[-(-128) + 127 + 1];

    static {
        for(int i = 0; i < cache.length; i++)
            cache[i] = new Long(i - 128);
    }
}
```

可以看到，它是在静态代码块中填充缓存数组的。



#### 3.2 反编译

同样地我们也编写一个示例片段：

```java
public class LongTest {

    public static void main(String[] args) {
        Long a = -128L, b = -128L, c = 150L, d = 150L;
        System.out.println(a == b);
        System.out.println(c == d);
    }
}
```

编译源代码： `javac LongTest.java`

对编译后的类文件进行反汇编: `javap -c LongTest`

得到下面反编译的代码：

```java
public class com.imooc.basic.learn_int.LongTest {
  public com.imooc.basic.learn_int.LongTest();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: ldc2_w        #2                  // long -128l
       3: invokestatic  #4                  // Method java/lang/Long.valueOf:(J)Ljava/lang/Long;
       6: astore_1
       7: ldc2_w        #2                  // long -128l
      10: invokestatic  #4                  // Method java/lang/Long.valueOf:(J)Ljava/lang/Long;
      13: astore_2
      14: ldc2_w        #5                  // long 150l
      17: invokestatic  #4                  // Method java/lang/Long.valueOf:(J)Ljava/lang/Long;
      20: astore_3
      21: ldc2_w        #5                  // long 150l
      24: invokestatic  #4                  // Method java/lang/Long.valueOf:(J)Ljava/lang/Long;
      27: astore        4
      29: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
      32: aload_1
      33: aload_2
      34: if_acmpne     41
      37: iconst_1
      38: goto          42
      41: iconst_0
      42: invokevirtual #8                  // Method java/io/PrintStream.println:(Z)V
      45: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
      48: aload_3
      49: aload         4
      51: if_acmpne     58
      54: iconst_1
      55: goto          59
      58: iconst_0
      59: invokevirtual #8                  // Method java/io/PrintStream.println:(Z)V
      62: return
}
```

我们从上述代码中发现 `Long var = ?` 的确是通过 `java.lang.Long#valueOf(long)` 来构造对象的。



### 4.总结

本小节通过源码分析法、阅读 JLS和 JVMS、使用反汇编法，对 `Integer` 和 `Long` 缓存的目的和实现方式问题进行了深入分析。

让大家能够通过更丰富的手段来学习知识和分析问题，通过对缓存目的的思考来学到更通用和本质的东西。

本节使用的几种手段将是我们未来常用的方法，也是工作进阶的必备技能和一个程序员专业程度的体现，希望大家未来能够多动手实践。

下一节我们将介绍 Java 序列化相关问题，包括序列化的定义，序列化常见的方案，序列化的坑点等。



### 5.课后题

**第 1 题**：请大家根据今天的研究分析过程，对下面的一个示例代码进行分析。

```java
public class CharacterTest {
    public static void main(String[] args) {
        Character a = 126, b = 126, c = 128, d = 128;
        System.out.println(a == b);
        System.out.println(c == d);
    }
}
```

**第 2 题**： 结合今天的讲解，请自行对`Character`、 `Short` 、`Boolean` 的缓存问题进行分析，并比较它们的异同。



### 参考资料

------

1. 阿里巴巴与 Java 社区开发者.《 Java 开发手册 1.5.0》华山版. 2019. 7 [↩︎](http://www.imooc.com/read/55/article/1139#fnref1)
2. James Gosling, Bill Joy, Guy Steele, Gilad Bracha, Alex Buckley.《Java Language Specification: Java SE 8 Edition》. 2015 [↩︎](http://www.imooc.com/read/55/article/1139#fnref2)
3. Tim Lindholm, Frank Yellin, Gilad Bracha, Alex Buckley.《Java Language Specification: Java SE 8 Edition》. 2015 [↩︎](http://www.imooc.com/read/55/article/1139#fnref3)
4. 周志明.《深入理解 Java 虚拟机》. 机械工业出版社. 2018 [↩︎](http://www.imooc.com/read/55/article/1139#fnref4)

03 Java序列化引发的血案

更新时间：2019-11-04 17:16:42

![img](https://img3.mukewang.com/5db6b9110001da7606400359.jpg)

![img](https://www.imooc.com/static/img/column/bg-l.png)![img](https://www.imooc.com/static/img/column/bg-r.png)

##  **03 Java序列化引发的血案** 

### 1、前言

《手册》第 9 页 “OOP 规约” 部分有一段关于序列化的约定 [1](https://www.imooc.com/read/55/article/1140#fn1)：

> 【强制】当序列化类新增属性时，请不要修改 serialVersionUID 字段，以避免反序列失败；如果完全不兼容升级，避免反序列化混乱，那么请修改 serialVersionUID 值。
> 说明：注意 serialVersionUID 值不一致会抛出序列化运行时异常。

我们应该思考下面几个问题：

- 序列化和反序列化到底是什么？
- 它的主要使用场景有哪些？
- Java 序列化常见的方案有哪些？
- 各种常见序列化方案的区别有哪些？
- 实际的业务开发中有哪些坑点？

接下来将从这几个角度去研究这个问题。



### 2. 序列化和反序列化是什么？为什么需要它？

**序列化**是将内存中的对象信息转化成可以存储或者传输的数据到临时或永久存储的过程。而**反序列化**正好相反，是从临时或永久存储中读取序列化的数据并转化成内存对象的过程。

![图片描述](https://img.mukewang.com/5db655fc000168f810200424.png)

**那么为什么需要序列化和反序列化呢？**

希望大家能够养成从本源上思考这个问题的思维方式，即思考它为什么会出现，而不是单纯记忆。

> 大家可以回忆一下，平时都是如果将文字文件、图片文件、视频文件、软件安装包等传给小伙伴时，这些资源在计算机中存储的方式是怎样的。
>
> 进而再思考，Java 中的对象如果需要存储或者传输应该通过什么形式呢？

我们都知道，一个文件通常是一个 m 个字节的序列：B0, B1, …, Bk, …, Bm-1。所有的 I/O 设备（例如网络、磁盘和终端）都被模型化为文件，而所有的输入和输出都被当作对应文件的读和写来执行。[2](https://www.imooc.com/read/55/article/1140#fn2)

因此本质上讲，文本文件，图片、视频和安装包等文件底层都被转化为二进制字节流来传输的，对方得文件就需要对文件进行解析，因此就需要有能够根据不同的文件类型来解码出文件的内容的程序。

大家试想一个典型的场景：如果要实现 Java 远程方法调用，就需要将调用结果通过网路传输给调用方，如果调用方和服务提供方不在一台机器上就很难共享内存，就需要将 Java 对象进行传输。而想要将 Java 中的对象进行网络传输或存储到文件中，就需要将对象转化为二进制字节流，这就是所谓的序列化。存储或传输之后必然就需要将二进制流读取并解析成 Java 对象，这就是所谓的反序列化。

序列化的主要目的是：**方便存储到文件系统、数据库系统或网络传输等**。

实际开发中常用到序列化和反序列化的场景有：

- 远程方法调用（RPC）的框架里会用到序列化。
- 将对象存储到文件中时，需要用到序列化。
- 将对象存储到缓存数据库（如 Redis）时需要用到序列化。
- 通过序列化和反序列化的方式实现对象的深拷贝。



## 3. 常见的序列化方式

常见的序列化方式包括 Java 原生序列化、Hessian 序列化、Kryo 序列化、JSON 序列化等。



#### 3.1 Java 原生序列化

正如前面章节讲到的，对于 JDK 中有的类，最好的学习方式之一就是直接看其源码。

`Serializable` 的源码非常简单，只有声明，没有属性和方法：

```java
// 注释太长，省略
public interface Serializable {
}
```

在学习源码注释之前，希望大家可以站在设计者的角度，先思考一个问题：如果一个类序列化到文件之后，类的结构发生变化还能否保证正确地反序列化呢？

答案显然是不确定的。

**那么如何判断文件被修改过了呢？** 通常可以通过加密算法对其进行签名，文件作出任何修改签名就会不一致。但是 Java 序列化的场景并不适合使用上述的方案，因为类文件的某些位置加个空格，换行等符号类的结构没有发生变化，这个签名就不应该发生变化。还有一个类新增一个属性，之前的属性都是有值的，之前都被序列化到对象文件中，有些场景下还希望反序列化时可以正常解析，怎么办呢？

那么是否可以通过约定一个唯一的 ID，通过 ID 对比，不一致就认为不可反序列化呢？

**实现序列化接口后，如果开发者不手动指定该版本号 ID 怎么办？**

既然 Java 序列化场景下的 “签名” 应该根据类的特点生成，我们是否可以不指定序列化版本号就默认根据类名、属性和函数等计算呢？

如果针对某个自己定义的类，想自定义序列化和反序列化机制该如何实现呢？支持吗？

带着这些问题我们继续看序列化接口的注释。

`Serializable` 的源码注释特别长，其核心大致作了下面的说明：

Java 原生序列化需要实现 `Serializable` 接口。序列化接口不包含任何方法和属性等，它只起到序列化标识作用。

一个类实现序列化接口则其子类型也会继承序列化能力，但是实现序列化接口的类中有其他对象的引用，则其他对象也要实现序列化接口。序列化时如果抛出 `NotSerializableException` 异常，说明该对象没有实现 `Serializable` 接口。

每个序列化类都有一个叫 `serialVersionUID` 的版本号，反序列化时会校验待反射的类的序列化版本号和加载的序列化字节流中的版本号是否一致，如果序列化号不一致则会抛出 `InvalidClassException` 异常。

强烈推荐每个序列化类都手动指定其 `serialVersionUID`，如果不手动指定，那么编译器会动态生成默认的序列化号，因为这个默认的序列化号和类的特征以及编译器的实现都有关系，很容易在反序列化时抛出 `InvalidClassException` 异常。建议将这个序列化版本号声明为私有，以避免运行时被修改。

实现序列化接口的类可以提供自定义的函数修改默认的序列化和反序列化行为。

自定义序列化方法：

```java
private void writeObject(ObjectOutputStream out) throws IOException;
```

自定义反序列化方法：

```java
private void readObject(ObjectInputStream in) 
  throws IOException, ClassNotFoundException;
```

通过自定义这两个函数，可以实现序列化和反序列化不可序列化的属性，也可以对序列化的数据进行数据的加密和解密处理。



#### 3.2 Hessian 序列化

Hessian 是一个动态类型，二进制序列化，也是一个基于对象传输的网络协议。Hessian 是一种跨语言的序列化方案，序列化后的字节数更少，效率更高。Hessian 序列化会把复杂对象的属性映射到 `Map` 中再进行序列化。



#### 3.3 Kryo 序列化

Kryo 是一个快速高效的 Java 序列化和克隆工具。Kryo 的目标是快速、字节少和易用。Kryo 还可以自动进行深拷贝或者浅拷贝。Kryo 的拷贝是对象到对象的拷贝而不是对象到字节，再从字节到对象的恢复。Kryo 为了保证序列化的高效率，会提前加载需要的类，这会带一些消耗，但是这是序列化后文件较小且反序列化非常快的重要原因。



#### 3.4 JSON 序列化

JSON (JavaScript Object Notation) 是一种轻量级的数据交换方式。JSON 序列化是基于 JSON 这种结构来实现的。JSON 序列化将对象转化成 JSON 字符串，JSON 反序列化则是将 JSON 字符串转回对象的过程。常用的 JSON 序列化和反序列化的库有 Jackson、GSON、Fastjson 等。



### 4.Java 常见的序列化方案对比

我们想要对比各种序列化方案的优劣无外乎两点，一点是查资料，一点是自己写代码验证。



#### 4.1 Java 原生序列化

Java 序列化的优点是：对对象的结构描述清晰，反序列化更安全。主要缺点是：效率低，序列化后的二进制流较大。



#### 4.2 Hessian 序列化

Hession 序列化二进制流较 Java 序列化更小，且序列化和反序列化耗时更短。但是父类和子类有相同类型属性时，由于先序列化子类再序列化父类，因此反序列化时子类的同名属性会被父类的值覆盖掉，开发时要特别注意这种情况。

> Hession2.0 序列化二进制流大小是 Java 序列化的 50%，序列化耗时是 Java 序列化的 30%，反序列化的耗时是 Java 序列化的 20%。 [3](https://www.imooc.com/read/55/article/1140#fn3)

编写待测试的类：

```java
@Data
public class PersonHessian implements Serializable {
    private Long id;
    private String name;
    private Boolean male;
}

@Data
public class Male extends PersonHessian {
    private Long id;
}
```

编写单测来模拟序列化继承覆盖问题：

```java
/**
 * 验证Hessian序列化继承覆盖问题
 */
@Test
public void testHessianSerial() throws IOException {
    HessianSerialUtil.writeObject(file, male);
    Male maleGet = HessianSerialUtil.readObject(file);
    // 相等
    Assert.assertEquals(male.getName(), maleGet.getName());
    // male.getId()结果是1，maleGet.getId()结果是null
    Assert.assertNull(maleGet.getId());
    Assert.assertNotEquals(male.getId(), maleGet);
}
```

上述单测示例验证了：反序列化时子类的同名属性会被父类的值覆盖掉的问题。



#### 4.3 Kryo 序列化

Kryo 优点是：速度快、序列化后二进制流体积小、反序列化超快。但是缺点是：跨语言支持复杂。注册模式序列化更快，但是编程更加复杂。



#### 4.4 JSON 序列化

JSON 序列化的优势在于可读性更强。主要缺点是：没有携带类型信息，只有提供了准确的类型信息才能准确地进行反序列化，这点也特别容易引发线上问题。

下面给出使用 Gson 框架模拟 JSON 序列化时遇到的反序列化问题的示例代码：

```java
/**
 * 验证GSON序列化类型错误
 */
@Test
public void testGSON() {
    Map<String, Object> map = new HashMap<>();
    final String name = "name";
    final String id = "id";
    map.put(name, "张三");
    map.put(id, 20L);

    String jsonString = GSONSerialUtil.getJsonString(map);
    Map<String, Object> mapGSON = GSONSerialUtil.parseJson(jsonString, Map.class);
    // 正确
    Assert.assertEquals(map.get(name), mapGSON.get(name));
    // 不等  map.get(id)为Long类型 mapGSON.get(id)为Double类型
    Assert.assertNotEquals(map.get(id).getClass(), mapGSON.get(id).getClass());
    Assert.assertNotEquals(map.get(id), mapGSON.get(id));
}
```

下面给出使用 fastjson 模拟 JSON 反序列化问题的示例代码：

```java
/**
 * 验证FatJson序列化类型错误
 */
@Test
public void testFastJson() {
    Map<String, Object> map = new HashMap<>();
    final String name = "name";
    final String id = "id";
    map.put(name, "张三");
    map.put(id, 20L);

    String fastJsonString = FastJsonUtil.getJsonString(map);
    Map<String, Object> mapFastJson = FastJsonUtil.parseJson(fastJsonString, Map.class);

    // 正确
    Assert.assertEquals(map.get(name), mapFastJson.get(name));
    // 错误  map.get(id)为Long类型 mapFastJson.get(id)为Integer类型
    Assert.assertNotEquals(map.get(id).getClass(), mapFastJson.get(id).getClass());
    Assert.assertNotEquals(map.get(id), mapFastJson.get(id));
}
```

大家还可以通过单元测试构造大量复杂对象对比各种序列化方式或框架的效率。

如定义下列测试类为 User，包括以下多种类型的属性：

```java
@Data
public class User implements Serializable {
    private Long id;
    private String name;
    private Integer age;
    private Boolean sex;
    private String nickName;
    private Date birthDay;
    private Double salary;
}
```



#### 4.5 各种常见的序列化性能排序

实验的版本：kryo-shaded 使用 4.0.2 版本，gson 使用 2.8.5 版本，hessian 用 4.0.62 版本。

实验的数据：构造 50 万 User 对象运行多次。

大致得出一个结论：

- 从二进制流大小来讲：JSON 序列化 > Java 序列化 > Hessian2 序列化 > Kryo 序列化 > Kryo 序列化注册模式；
- 从序列化耗时而言来讲：GSON 序列化 > Java 序列化 > Kryo 序列化 > Hessian2 序列化 > Kryo 序列化注册模式；
- 从反序列化耗时而言来讲：GSON 序列化 > Java 序列化 > Hessian2 序列化 > Kryo 序列化注册模式 > Kryo 序列化；
- 从总耗时而言：Kryo 序列化注册模式耗时最短。

> 注：由于所用的序列化框架版本不同，对象的复杂程度不同，环境和计算机性能差异等原因结果可能会有出入。



### 5. 序列化引发的一个血案

接下来我们看下面的一个案例：

> 前端调用服务 A，服务 A 调用服务 B，服务 B 首次接到请求会查 DB，然后缓存到 Redis（缓存 1 个小时）。服务 A 根据服务 B 返回的数据后执行一些处理逻辑，处理后形成新的对象存到 Redis（缓存 2 个小时）。
>
> 服务 A 通过 Dubbo 来调用服务 B，A 和 B 之间数据通过 `Map` 类型传输，服务 B 使用 Fastjson 来实现 JSON 的序列化和反序列化。
>
> 服务 B 的接口返回的 `Map` 值中存在一个 `Long` 类型的 `id` 字段，服务 A 获取到 `Map` ，取出 `id` 字段并强转为 `Long` 类型使用。

执行的流程如下：
![图片描述](https://img.mukewang.com/5db656b00001507517500718.png)通过分析我们发现，服务 A 和服务 B 的 RPC 调用使用 Java 序列化，因此类型信息不会丢失。

但是由于服务 B 采用 JSON 序列化进行缓存，第一次访问没啥问题，其执行流程如下：
![图片描述](https://img.mukewang.com/5db656cb000199cf08570550.png)

**如果服务 A 开启了缓存**，服务 A 在第一次请求服务 B 后，缓存了运算结果，且服务 A 缓存时间比服务 B 长，因此不会出现错误。
![图片描述](https://img.mukewang.com/5db656ee0001020406490340.png)
**如果服务 A 不开启缓存**，服务 A 会请求服务 B ，由于首次请求时，服务 B 已经缓存了数据，服务 B 从 Redis（B）中反序列化得到 `Map`。流程如下图所示：

![图片描述](https://img.mukewang.com/5db657060001151308390423.png)
然而问题来了： 服务 A 从 Map 取出此 `Id` 字段，强转为 `Long` 时会出现类型转换异常。

最后定位到原因是 Json 反序列化 Map 时如果原始值小于 Int 最大值，反序列化后原本为 Long 类型的字段，变为了 Integer 类型，服务 B 的同学紧急修复。

服务 A 开启缓存时， 虽然采用了 JSON 序列化存入缓存，但是采用 DTO 对象而不是 Map 来存放属性，所以 JSON 反序列化没有问题。

**因此大家使用二方或者三方服务时，当对方返回的是 `Map` 类型的数据时要特别注意这个问题**。

> 作为服务提供方，可以采用 JDK 或者 Hessian 等序列化方式；
>
> 作为服务的使用方，我们不要从 Map 中一个字段一个字段获取和转换，可以使用 JSON 库直接将 Map 映射成所需的对象，这样做不仅代码更简洁还可以避免强转失败。

代码示例：

```java
@Test
public void testFastJsonObject() {
    Map<String, Object> map = new HashMap<>();
    final String name = "name";
    final String id = "id";
    map.put(name, "张三");
    map.put(id, 20L);

    String fastJsonString = FastJsonUtil.getJsonString(map);
    // 模拟拿到服务B的数据
    Map<String, Object> mapFastJson = FastJsonUtil.parseJson(fastJsonString,map.getClass());
    // 转成强类型属性的对象而不是使用map 单个取值
    User user = new JSONObject(map).toJavaObject(User.class);
    // 正确
    Assert.assertEquals(map.get(name), mapFastJson.get(name));
    // 正确
    Assert.assertEquals(map.get(id), user.getId());
}
```



### 6. 总结

本节的主要讲解了序列化的主要概念、主要实现方式，以及序列化和反序列化的几个坑点，希望大家在实际业务开发中能够注意这些细节，避免趟坑。

下一节将讲述浅拷贝和深拷贝的相关知识。



### 7. 课后题

给出一个 `PersonTransit` 类，一个 `Address` 类，假设 `Address` 是其它 jar 包中的类，没实现序列化接口。请使用今天讲述的自定义的函数 `writeObject` 和 `readObject` 函数实现 `PersonTransit` 对象的序列化，要求反序列化后 `address` 的值正常。

```java
@Data
public class PersonTransit implements Serializable {

    private Long id;
    private String name;
    private Boolean male;
    private List<PersonTransit> friends;
    private Address address;
}

@Data
@AllArgsConstructor
public class Address {
    private String detail;
}
```



### 参考资料

------

1. 阿里巴巴与 Java 社区开发者.《 Java 开发手册 1.5.0》华山版. 2019. 9 [↩︎](https://www.imooc.com/read/55/article/1140#fnref1)
2. [美] Randal E.Bryant/ David O’Hallaron.《深入理解计算机系统》. [译] 龚奕利，贺莲。机械工业出版社. 2016 [↩︎](https://www.imooc.com/read/55/article/1140#fnref2)
3. 杨冠宝。高海慧.《码出高效：Java 开发手册》. 电子工业出版社. 2018 [↩︎](https://www.imooc.com/read/55/article/1140#fnref3)

04 学习浅拷贝和深拷贝的正确方式

更新时间：2019-11-11 14:15:56

![img](https://img2.mukewang.com/5db6ba8800012e3d05860328.jpg)

![img](https://www.imooc.com/static/img/column/bg-l.png)![img](https://www.imooc.com/static/img/column/bg-r.png)



##  **04 学习浅拷贝和深拷贝的正确方式** 

### 1. 前言

《手册》第 10 页有关于 `Object` 的 `clone` 问题的描述 [1](https://www.imooc.com/read/55/article/1141#fn1)：

> 【推荐】慎用 Object 的 clone 方法来拷贝对象。
> 说明：对象 clone 方法默认是浅拷贝，若想实现深拷贝需覆写 clone 方法实现域对象的深度遍历式拷贝。

那么我们要思考几个问题：

1. 什么是浅拷贝？
2. 浅拷贝和深拷贝的区别是什么？
3. 拷贝的目的是什么？
4. 拷贝的使用场景是什么？
5. 如何实现深拷贝？

网上也有很多介绍浅拷贝和深拷贝的文章，但文章质量参差不齐，有些文章读完仍然对概念得理解非常含糊。读完这些文章对拷贝的使用场景，对深拷贝的实现方式等都无法有全面和深刻的理解。

为此本节将带着大家系统地研究这上述问题，以便大家未来遇到类似问题时可以举一反三，灵活迁移。



### 2. 概念介绍



#### 2.1 拷贝 / 克隆的概念

我们先研究第 1 个问题：**什么是拷贝？**

维基百科对 “克隆” 的描述如下 [2](https://www.imooc.com/read/55/article/1141#fn2)：

> 克隆 (英语： Clone) 在广义上是指利用生物技术由无性生殖产生与原原个体有完全相同基因组之后代的过程。
>
> 在园艺学上，克隆指通过营养繁殖产生的单一植株的后代，很多植物都是通过克隆这样的无性繁殖方式从单一植株获得大量的子代个体。
>
> 在生物学上，是指选择性地复制出一段 DNA 序列（分子克隆）、细胞（细胞克隆）或个体（个体克隆）。
>
> 克隆一个生物体意味着创造一个与原先的生物体具有完全一样的遗传信息的新生物体。

计算机中的拷贝或克隆和上述概念很类似，可以类比理解。

对象的拷贝，就是根据原来的对象 “复制” 一份属性、状态一致的新的对象。



#### 2.2 为什么需要拷贝方法？

我们思考第 2 个问题：**为什么需要拷贝呢？**

我们来看下面的订单类和商品类。

订单类（ `Order` ）：

```java
@Data
public class Order {

    private Long id;

    private String orderNo;

    private List<Item> itemList;
}
```

商品类（ Item` ）：

```java
@Data
public class Item {
    private Long id;

    private Long itemId;

    private String name;

    private String desc;

    // 省略其他
}
```

如果我们查询得到 1 个订单对象，该对象包括 6 个商品对象。

如果我们还需要构造多个新的订单对象，属性和上述订单对象非常相似，只是订单号不同或者商品略有区别。

这时如果有一个 “复制” 方法，可以将订单复制一个副本，而且修改副本中的订单号和商品列表 ( `itemList` ) 不影响原始对象，是不是很方便？

另外一个非常典型的场景是在多线程中。如果只用一个主线程，在主线程中修改订单号分别调用 `doSomeThing` 函数，想分别打印 first 和 second 两个订单编号字符串。

```java
@Slf4j
public class CloneDemo { 
  
  public static void main(String[] args) {
        Order order = OrderMocker.mock();
        order.setOrderNo("first");
        doSomeThing(order);
        order.setOrderNo("second");
        doSomeThing(order);
    }

   private static void doSomeThing(Order order) {
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(order.getOrderNo());
   }
}
```

运行程序后输出的结果的确是: `first`、`second`。

但在多线程环境中，如果我们不通过克隆构造新的对象，线程池中两个线程会公用同一个对象，后面对订单号的修改将影响到其它线程。

```java
@Slf4j
public class CloneDemo {
  
	public static void main(String[] args) {
	    ExecutorService executorService = Executors.newFixedThreadPool(5);
	    Order order = OrderMocker.mock();
	    order.setOrderNo("first");
	    executorService.execute(() -> doSomeThing(order));
	    order.setOrderNo("second");
	    executorService.execute(() -> doSomeThing(order));

	}

	private static void doSomeThing(Order order) {
	    try {
	        TimeUnit.SECONDS.sleep(1L);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	    System.out.println(order.getOrderNo());
	}
}
```

输出的结果是: `second`、`second`。

因此如果能够克隆一个新的对象，并且对新对象的修改不影响原始对象，就能实现我们期待的效果。



#### 2.3 什么是浅拷贝？浅拷贝和深拷贝的区别是什么？

通过前言部分的介绍，我们知道 `Object` 的 `clone` 函数默认是浅拷贝。

按照惯例我们进入源码，看看是否能够得到我们想要的答案：

```java
/**
 * Creates and returns a copy of this object.  The precise meaning
 * of "copy" may depend on the class of the object. The general
 * intent is that, for any object {@code x}, the expression:
 * <blockquote>
 * <pre>
 * x.clone() != x</pre></blockquote>
 * will be true, and that the expression:
 * <blockquote>
 * <pre>
 * x.clone().getClass() == x.getClass()</pre></blockquote>
 * will be {@code true}, but these are not absolute requirements.
 * While it is typically the case that:
 * <blockquote>
 * <pre>
 * x.clone().equals(x)</pre></blockquote>
 * will be {@code true}, this is not an absolute requirement.
 * <p>
 * By convention, the returned object should be obtained by calling
 * {@code super.clone}.  If a class and all of its superclasses (except
 * {@code Object}) obey this convention, it will be the case that
 * {@code x.clone().getClass() == x.getClass()}.
 * <p>
 * By convention, the object returned by this method should be independent
 * of this object (which is being cloned).  To achieve this independence,
 * it may be necessary to modify one or more fields of the object returned
 * by {@code super.clone} before returning it.  Typically, this means
 * copying any mutable objects that comprise the internal "deep structure"
 * of the object being cloned and replacing the references to these
 * objects with references to the copies.  If a class contains only
 * primitive fields or references to immutable objects, then it is usually
 * the case that no fields in the object returned by {@code super.clone}
 * need to be modified.
 * <p>
 * The method {@code clone} for class {@code Object} performs a
 * specific cloning operation. First, if the class of this object does
 * not implement the interface {@code Cloneable}, then a
 * {@code CloneNotSupportedException} is thrown. Note that all arrays
 * are considered to implement the interface {@code Cloneable} and that
 * the return type of the {@code clone} method of an array type {@code T[]}
 * is {@code T[]} where T is any reference or primitive type.
 * Otherwise, this method creates a new instance of the class of this
 * object and initializes all its fields with exactly the contents of
 * the corresponding fields of this object, as if by assignment; the
 * contents of the fields are not themselves cloned. Thus, this method
 * performs a "shallow copy" of this object, not a "deep copy" operation.
 * <p>
 * The class {@code Object} does not itself implement the interface
 * {@code Cloneable}, so calling the {@code clone} method on an object
 * whose class is {@code Object} will result in throwing an
 * exception at run time.
 *
 * @return     a clone of this instance.
 * @throws  CloneNotSupportedException  if the object's class does not
 *               support the {@code Cloneable} interface. Subclasses
 *               that override the {@code clone} method can also
 *               throw this exception to indicate that an instance cannot
 *               be cloned.
 * @see java.lang.Cloneable
 */
protected native Object clone() throws CloneNotSupportedException;
```

该函数给出了非常详尽的介绍。下面给出一些要点的翻译：

> 该方法是创建对象的副本。这就意味着 “副本” 依赖于该对象的类型。
>
> 对于任何对象而言，一般来说下面的表达式成立：
>
> `x.clone() != x` 的结果为 `true` 。
>
> `x.clone().getClass() == x.getClass()` 的结果为 `true` 。
>
> 但是这些也不是强制的要求。
>
> `x.clone().equals(x)` 的结果也是 `true`。这也不是强制要求。
>
> 按照惯例，返回对象应该通过调用 `super.clone` 函数来构造。如果一个类和它的所有父类（除了 `Object` ）都遵循这个约定，那么 `x.clone().getClass() == x.getClass()` 将成立。
>
> 按照惯例，返回的对象应该和原始对象是独立的。
>
> 为了实现这种独立性，后续应该在调用 `super.clone` 得到拷贝对象并返回之前，应该对内部深层次的可变对象创建副本并指向克隆对象的对应属性的引用。
>
> 如果一个类只包含基本类型的属性或者指向不可变对象的引用，这种情况下，`super.clone` 返回的对象不需要被修改。
>
> 如果调用 `clone` 函数的类没有实现 `Cloneable` 接口将会抛出 `CloneNotSupportedException`。
>
> 注意所有的数组对象都默认实现了 `Cloneable` 接口。
>
> 该函数会创建该类的新实例，并初始化所有属性对象。属性对象本身并不会自动调用 `clone`。
>
> 因此此方法实现的是浅拷贝而不是深拷贝。

因此我们可以了解到，浅拷贝将返回该类的新的实例，该实例的引用类型对象共享。
深拷贝也会返回该类的新的实例，但是该实例的引用类型属性也是拷贝的新对象。

如果用一句话来描述，**浅拷贝和深拷贝的主要区别在于对于引用类型是否共享。**
![图片描述](https://img.mukewang.com/5db65a6400019de012360594.png)
为了更好地理解浅拷贝，我们给出一个示例：

改造订单对象：

```java
@Data
public class Order implements Cloneable {

    private Long id;

    private String orderNo;

    private List<Item> itemList;

    @Override
    public Order clone() {
        try {
            return (Order)super.clone();
        } catch (CloneNotSupportedException ignore) {
            // 不会调到这里
        }
        return null;
    }
}
```

通过 `Object` 类的 `clone` 函数的注释我们了解到：如果调用 `clone` 函数的类没有实现 `Cloneable` 接口将会抛出 `CloneNotSupportedException` 。

因此要实现 `Cloneable` 接口。

重写 `clone` 函数是为了供外部使用，因此定义为 `public` 。

返回值类型定义为客户端直接需要的对象类型（本类）。

这体现了《Effective Java》的 Item 11 中所提到的 [3](https://www.imooc.com/read/55/article/1141#fn3)：

> Never make the client do anything the library can do for the client.
>
> 不要让客户端去做任何类库可以替它完成的事。

我们为上述浅拷贝编写测试代码：

```java
 @Test
  public void shallowClone() {
      Order order = OrderMocker.mock();
      Order cloneOrder = order.clone();

      assertFalse(order == cloneOrder);
      assertTrue(order.getItemList() == cloneOrder.getItemList());
  }
```

该单元测试可以通过，从而证实了 `clone` 函数的注释，证实了浅拷贝的表现。

即浅拷贝后，原对象的订单列表和克隆对象的订单列表地址相同。

**因此如果使用浅拷贝，修改拷贝订单的商品列表，那么原始订单对象的商品列表也会受到影响。**

为了更形象地理解浅拷贝和深拷贝的概念，我们以文件夹进行类比：

> 浅拷贝：同一个文件夹的两个快捷方式，虽然是两个不同的快捷方式，但是指向的文件夹是同一个，不管是通过哪个快捷方式进入，对该文件夹下的文件修改，相互影响。
>
> 深拷贝：我们复制某个文件夹（含里面的内容）在另外一个目录进行粘贴，就可得到具有相同内容的新目录，对新文件夹修改不影响原始文件夹。



### 3. 深拷贝的实现方式

虽然浅拷贝能够实现拷贝的功能，但是浅拷贝的引用类型成员变量是共享的，修改极可能导致相互影响。

业务开发中使用深拷贝更多一些，**那么实现深拷贝有哪些方式呢？**



#### 3.1 手动深拷贝

```java
@Data
public class Order implements Cloneable {

    private Long id;

    private String orderNo;

    private List<Item> itemList;


    @Override
    public Order clone() {
        try {
            Order order = (Order) super.clone();
            if (id != null) {
                order.id = new Long(id);
            }
            if (orderNo != null) {
                order.orderNo = new String(orderNo);
            }

            if (itemList != null) {
                List<Item> items = new ArrayList<>();
                for (Item each : itemList) {
                    Item item = new Item();
                    Long id = each.getId();
                    if(id != null){
                        item.setId(new Long(id));
                    }
                    Long itemId = each.getItemId();
                    if(itemId != null){
                        item.setItemId(new Long(itemId));
                    }
                    String name = each.getName();
                    if(name != null){
                        item.setName(new String(name));
                    }
                    String desc = each.getDesc();
                    if(desc != null){
                        item.setDesc(new String(desc));
                    }
                    items.add(item);
                }
                order.setItemList(items);
            }
            return order;
        } catch (CloneNotSupportedException ignore) {

        }

        return null;
    }
}
```

深拷贝也调用 `super.clone` 是为了支撑 `x.clone().getClass() == x.getClass()` 。

写好代码后，通过调用 `Order` 类的 `clone` 函数即可实现深拷贝。

由于克隆的对象和内部的引用类型的属性全部都是依据原始对象新建的对象，因此如果修改拷贝对象的商品列表，原始订单对象的商品列表并不会受到影响。

通过下面的单元测试来验证：

```java
@Test
public void deepClone() {
    Order order = OrderMocker.mock();
    Order cloneOrder = (Order) order.clone();

    assertFalse(order == cloneOrder);
    assertFalse(order.getItemList() == cloneOrder.getItemList());
}
```

该单测可顺利通过。



#### 3.2 序列化方式

前面章节我们讲到了序列化和反序列化的知识，讲到了序列化的主要使用场景包括深拷贝。

序列化通过将原始对象转化为字节流，再从字节流重建新的 Java 对象，因此原始对象和反序列化后的对象修改互不影响。

因此可以使用之前讲到的序列化和反序列化方式来实现深拷贝。



##### 3.2.1 自定义序列化工具函数

如果我们不想为了深拷贝这一项功能就依赖新的 jar 包，可以在自己项目中借助对象输入和输出流编写拷贝工具函数。

示例代码如下：

```java
  /**
     * JDK序列化方式深拷贝
     */
    public static <T> T deepClone(T origin) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);)        {
            objectOutputStream.writeObject(origin);
            objectOutputStream.flush();
        }
        byte[] bytes = outputStream.toByteArray();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);) {
            return JdkSerialUtil.readObject(inputStream);
        }
    }
```

我们可通过调试查看克隆对象和原始对象。

从下图中我们可以清晰地看到，通过此方法克隆得到的新的对象是一个全新的对象。

![图片描述](https://img.mukewang.com/5db65a8800015e6210170716.png)
需要注意的是：正如前面章节所讲，Java 序列化需要实现 `Serializable` 接口，而且效率不是特别高。



##### 3.2.2 commons-lang3 的序列化工具类

我们可以利用项目中引用的常见工具包的工具类实现深拷贝，避免重复造轮子。

可以使用 commons-lang3 （3.7 版本）的序列化工具类： `org.apache.commons.lang3.SerializationUtils#clone`。

用法非常简单：

```java
@Test
public void serialUtil() {
    Order order = OrderMocker.mock();
   // 使用方式
    Order cloneOrder = SerializationUtils.clone(order);

    assertFalse(order == cloneOrder);
    assertFalse(order.getItemList() == cloneOrder.getItemList());
}
```

前面反复提到过，我们学习知识不仅要知其然，而且要知其所以然。

**那么它是如何实现深拷贝的呢？**

按照惯例我们打开源码：

```java
/**
 * <p>Deep clone an {@code Object} using serialization.</p>
 *
 * <p>This is many times slower than writing clone methods by hand
 * on all objects in your object graph. However, for complex object
 * graphs, or for those that don't support deep cloning this can
 * be a simple alternative implementation. Of course all the objects
 * must be {@code Serializable}.</p>
 *
 * @param <T> the type of the object involved
 * @param object  the {@code Serializable} object to clone
 * @return the cloned object
 * @throws SerializationException (runtime) if the serialization fails
 */
public static <T extends Serializable> T clone(final T object) {
    if (object == null) {
        return null;
    }
    final byte[] objectData = serialize(object);
    final ByteArrayInputStream bais = new ByteArrayInputStream(objectData);

    try (ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais,
            object.getClass().getClassLoader())) {
        /*
         * when we serialize and deserialize an object,
         * it is reasonable to assume the deserialized object
         * is of the same type as the original serialized object
         */
        @SuppressWarnings("unchecked") // see above
        final T readObject = (T) in.readObject();
        return readObject;

    } catch (final ClassNotFoundException ex) {
        throw new SerializationException("ClassNotFoundException while reading cloned object data", ex);
    } catch (final IOException ex) {
        throw new SerializationException("IOException while reading or closing cloned object data", ex);
    }
}
```

通过其返回值的泛型描述 `` 可以断定参数对象需要实现序列化接口。

该函数注释也给出了性能说明，该深拷贝方法性能不如直接手动写 `clone` 方法效率高。

大家可以进到该方法的子函数中查看更多细节。

通过源码的分析我们发现，该克隆函数本质上也是通过 Java 序列化和反序列化方式实现。



##### 3.2.3 JSON 序列化

我们还可以通过 JSON 序列化方式实现深拷贝。

下面我们利用 Google 的 Gson 库（2.8.5 版本），实现基于 JSON 的深拷贝：

首先我们将深拷贝方法封装到拷贝工具类中：

```java
/**
 * Gson方式实现深拷贝
 */
public static <T> T deepCloneByGson(T origin, Class<T> clazz) {
    Gson gson = new Gson();
    return gson.fromJson(gson.toJson(origin), clazz);
}
```

使用时直接调用封装的工具方法即可：

```java
 @Test
 public void withGson() {
      Order order = OrderMocker.mock();
      // gson序列化方式
      Order cloneOrder = CloneUtil.deepCloneByGson(order, Order.class);

      assertFalse(order == cloneOrder);
      assertFalse(order.getItemList() == cloneOrder.getItemList());
  }
```

使用 JSON 序列化方式实现深拷贝的好处是，性能比 Java 序列化方式更好，更重要的是不要求序列化对象以及成员属性（嵌套）都要实现序列化接口。

我们也可以使用前面讲到的 Hessian 和 Kryo 序列化来实现，请大家自行封装。

上面通过 Gson 实现的深拷贝工具方法封装，再次体现了 “不要让客户端去做任何类库可以替它完成的事” 的原则。

这点也和《重构 - 改善既有代码的设计》 第一版 10.13 封装向下转型的重构方案一致。

最后，建议**不管采取哪种或者哪几种深拷贝方式，都尽量将其封装到项目的克隆工具类中，方便复用**。



### 4. 总结

本节重点讲述了浅拷贝和深拷贝的概念，它们的主要区别，以及浅拷贝和深拷贝的实现方式。

下一节将讲述开发常用既熟悉又陌生的几种分层领域模型，讲述它们之间的区别和实际开发中的使用。



### 参考资料

------

1. 阿里巴巴与 Java 社区开发者.《 Java 开发手册 1.5.0》华山版. 2019. 10 [↩︎](https://www.imooc.com/read/55/article/1141#fnref1)
2. [克隆 - 维基百科](https://zh.wikipedia.org/wiki/克隆) [↩︎](https://www.imooc.com/read/55/article/1141#fnref2)
3. [美] Joshua Bloch.《Effective Java : Second Edition》.2008 [↩︎](https://www.imooc.com/read/55/article/1141#fnref3)

## 05 分层领域模型使用解读

更新时间：2019-11-01 11:55:13

![img](https://img.mukewang.com/5db6baaa0001d73106400359.jpg)

![img](https://www.imooc.com/static/img/column/bg-l.png)![img](https://www.imooc.com/static/img/column/bg-r.png)



### 1. 前言

《手册》关于分层模型部分的规约如下 [1](https://www.imooc.com/read/55/article/1142#fn1)：

> 【参考】分层领域模型规约
> DO (Data Object): 此对象与数据库表结构一一对应，通过 DAO 层向上传输数据源对象。
>
> DTO (Data Transfer Object): 数据传输对象，Service 或 Manager 向外传输的对象。
>
> BO (Business Object): 业务对象，由 Service 层输出的封装业务逻辑的对象。
>
> AO (Application Object): 应用对象，在 Web 层与 Service 层之间抽象的复用对象模型，极为贴 近展示层，复用度不高。
>
> VO (View Object): 显示层对象，通常是 Web 向模板渲染引擎层传输的对象。Query: 数据查询对象，各层接收上层的查询请求。
>
> 注意超过 2 个参数的查询封装，禁止使用 Map 类来传输。

那么我们需要思考以下几个问题：

- 为什么需要这些分层领域模型？
- 实际开发中每种分层领域模型都会用到吗？

本小节我们将重点分析和解答这些问题。



### 2. 分层模型



#### 2.1 常见的分层模型有哪些？含义是什么？

学习和工作经常会接触到分层领域模型，如 DO、BO、DTO、VO 等。其中 DO、BO、DTO、AO、Query 在《手册》给出了一些解释，这里给出一些补充。

DTO (Data Transger Object) 为数据传输对象，通常将底层的数据聚合传给外部系统，它通常用作 Service 和 Manager 层向上层返回的对象。需要注意的是：如果作为分布式服务的参数或返回对象，通常要实现序列化接口。

Param 为查询参数对象，适用于各层，通常用作接受前端参数对象。Param 和 Query 的出现是为了避免使用 `Map` 作为接收参数的对象。

BO (Bussiness Object) 即业务对象。该对象中通常包含业务逻辑。此对象在实际使用中有不同的理解，有的团队采用领域驱动设计，BO 含有属性和方法（具体可参考领域驱动设计的相关图书）；有的团队将 BO 当做 Service 返回给上层的 “专用 DTO” 使用；而有的团队则当做 Service 层内保存中间信息数据的 “DTO” 或者上下文对象来使用（本文采用这种理解）。

比如 BO 中可以保存中间状态，放一些逻辑等，这些并不适合放在 `DTO` 中：

```java
@Data
public class ItemBO {

    private Boolean isOnSell;

    private Boolean hasStock;

    private Boolean hasSensitiveWords;

    public Boolean isLegal() {
        if (isOnSell == null || hasStock == null || hasSensitiveWords == null) {
            return false;
        }
        return isOnSell && hasStock && (!hasSensitiveWords);
    }
}
```

VO (View Object) 为视图对象，通常作为控制层通过 JSON 返回给前端然后前端渲染或者加载页面模板在后端进行填充。

AO (Application Object) 应用对象。通常用在控制层和服务层之间。有些团队会将前端查询的属性和保存的属性几乎一致的对象封装为 AO，如读取用户属性传给前端，用户在前端编辑了用户属性后传回后端。这种用法将 AO 用作 Param 和 VO 或 Param 和 DTO 的组合。



#### 2.2 为什么要有分层领域模型？

还有的朋友查询参数喜欢通过 `Map` 或者 `JSONObject` 来封装。有些朋友可能会认为这么多模型没有必要，因为通常各层模型的属性基本相同，而且各种类型的分层模型对象转换非常麻烦。

使用不同的分层领域模型能够让程序更加健壮、更容易拓展，可以降低系统各层的耦合度。

分层模型的优势只有在系统较大时才体现得更加明显。设想一下如果我们不想定义 DTO 和 VO，直接将 DO 用到数据访问层、服务层、控制层和外部访问接口上。此时该表删除或则修改一个字段，DO 必须同步修改，这种修改将会影响到各层，这并不符合高内聚低耦合的原则。通过定义不同的 DTO 可以控制对不同系统暴露不同的属性，通过属性映射还可以实现具体的字段名称的隐藏。不同业务使用不同的模型，当一个业务发生变更需要修改字段时，不需要考虑对其它业务的影响，如果使用同一个对象则可能因为 “不敢乱改” 而产生很多不优雅的兼容性行为。

如果我们不愿意定义 Param 对象，使用 Map 来接收前端的参数，获取时如果采用 JSON 反序列化，则可能出现上一节所讲到的反序列化类型丢失问题。如果我们不使用 Query 对象而是 `Map` 对象来封装 DAO 的参数，设置和获取的 `key` 很可能因为粗心导致设置和获取时的 key 不一致而出现 BUG。



### 3. 开发中的应用

讲完了概念和优势，大家可能会认为文字描述有些抽象，接下来通过查询和返回两个视角为大家展示实际项目中的一种常见的用法（贫血模型）。



#### 3.1 查询视图

我们先从请求访问的视角去了解不同分层数据模型在实际项目中一种常见用法。

![图片描述](https://img.mukewang.com/5db65bbc0001a1da18681000.png)
前端或者其它服务将 `Param` 对象作为参数传给控制层或者对外服务接口，然后调用内部的服务类，服务类内部的中间数据和这些数据相关的逻辑可以封装为 `BO` ，比如根据 `BO` 多个属性判断是否符合某个条件。

如果查询数据则封装为 `Query` 对象作为参数，如果需要查询其它依赖，则可以封装 `Param` 对象作为参数去查询。`DAO` 层一般插入和更新的参数对象使用 `DO` 或 `Param`, 查询参数一般使用 `Query`，删除参数一般使用 `Param`。



#### 3.2 返回视图

接下来我们从数据返回的视角去了解分层领域模型在实际项目中的一种常见用法：

![图片描述](https://img.mukewang.com/5db65bcd0001942c17701038.png)
数据访问层通常将数据封装为 `DO` 对象传给 `Service` 层，`Manager` 或 `Client` 层往往将查询结果封装为 `DTO` 传给 `Service` 层。

通常内部服务层通过 `DTO` 往外传输数据。`Controller` 通常将 `DTO` 组装为前端需要的 `VO` 或者直接将 `DTO` 外传 。

RPC 服务接口将 `DTO` 直接返回或者重新封装为新的 `DTO` 返回给外部服务。

另外即使同一个接口，但是一个对内使用，一个对外暴露，尽量使用不同接口，定义不同的参数和返回值，从而避免因为修改内部或外部的数据结构而导致另外一个受到影响，这也是单一职责原则的要求。

> **单一职责原则**：一个类应该有且只有一个改变的理由。

也有部分团队 RPC 的请求和响应参数都通过 DTO 来承载，通过 `XXRequestDTO` 和 `XXResponseDTO` 来表示。

实践分层领域模型能够提高项目的健壮性、可拓展性和可维护性，降低了系统内部各层的耦合度。

上面只是给出一种参考，很多团队对部分分层模型的理解会有差异，实际的使用过程中根据自己团队的规模可以适当变通。比如有很多团队项目并不是特别大，为了降低复杂度，只用到了 `DTO` 、`VO` 、`DO` 三种分层领域模型。

最后对分层领域模型的规约这里进行补充：

**【参考】不提倡在 DTO 中写逻辑，强制不要在 RPC 返回对象的 DTO 中封装逻辑。**

有些团队的个别成员会将根据成员属性作判断的一些函数写到 DTO 中，最奇葩的是该逻辑还主要供内部系统业务层使用。

如:

```java
public class xxDTO{

// 各种属性


// 逻辑代码
 public static boolean  canXXX(){ 
   // 各种判断
 }

}
```

这样造成系统的耦合性非常强。

如果对方用到了这个函数，未来此函数的内部逻辑必须发生变化，未必能及时通知对方升级，容易造成 BUG。

即使耗费了成本找到了使用方，为了你的功能，让别人被迫升级版本重新上线也是非常不专业的事情。

显然这样做不合理。

> 试想一下今天 A 部门告诉你他们因某个功能被迫修改了某个 RPC 返回值 DTO 的某个方法，你们用到没有？用到升级一下哈…
>
> 然后 B 部门的人明天告诉你同样的话，然后 C 部门，然后…
>
> 你会不会崩溃？

建议如果需要在内部业务中写对实体相关的逻辑，可以考虑封装到工具类 / 帮助类中。



### 4. 总结

本节主要讲分层模型的目的和优势以及在实际开发中的常见用法。给大家一个参考，让大家能够在开发时知道哪些模型应该放到哪一层。

下一节将讲述不同的分层领域模型之间的转换的正确姿势。



### 5. 思考题

在实际项目开发中，不同的分层领域模型之间通常需要转换，你是如何转换的？



### 参考资料

------

1. 阿里巴巴与 Java 社区开发者.《 Java 开发手册 1.5.0：华山版》:2019:38 [↩︎](https://www.imooc.com/read/55/article/1142#fnref1)



##  **06 Java属性映射的正确姿势** 

更新时间：2019-11-01 11:54:54

![img](https://img1.mukewang.com/5db6baca000143a506400359.jpg)

![img](https://www.imooc.com/static/img/column/bg-l.png)![img](https://www.imooc.com/static/img/column/bg-r.png)



### 1. 前言

前一节讲到项目为了更容易维护，易于拓展等原因会使用各种分层领域模型。在多层应用中，常需要对各种不同的分层对象进行转换，这就会存在一个非常棘手的问题即：编写不同的模型之间相互转换的代码非常麻烦。其中最常见和最简单的方式是编写对象属性转换函数，即普通的 Getter/Setter 方法。除此之外各种各种属性映射工具。

- 那么常见的 Java 属性映射工具有哪些？
- 它们的原理以及对其性能怎样？
- 实际开发中该如何选择？

本节将给出解答。



### 2. 常见的 Java 属性映射的工具及其原理



#### 2.1 常见的 Java 属性映射工具

常见的 Java 属性映射工具有以下几种：

1. `org.apache.commons.beanutils.BeanUtils#copyProperties`
2. `org.springframework.beans.BeanUtils#copyProperties(java.lang.Object, java.lang.Object)`
3. `org.dozer.Mapper#map(java.lang.Object, java.lang.Class)`
4. `net.sf.cglib.beans.BeanCopier#copy`
5. `ma.glasnost.orika.MapperFacade#map(S, D)`
6. `mapstruct`



#### 2.2 原理

1、Getter/Setter 方式使用原生的语法，虽然简单但是手动编写非常耗时；

2、通过 [dozer 的 maven 依赖](https://mvnrepository.com/artifact/net.sf.dozer/dozer/5.5.1)可以看出，dozer 并没有使用字节码增强技术，因为并没有引用任何字节码增强技术的 jar 包；

我们再从其核心类 `org.dozer.MappingProcessor` 中寻找线索：

```java
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
...
```

我们可以断定，dozer 使用的是反射机制。

3、同样的 commons 和 Spring 的 `BeanUtil` 工具类也采用的是反射方式。优点是两个是非常常用的类库，不需要引用更多复杂的包；

4、cglib 的 `BeanCopier` 的原理是不是也是反射机制呢？

我们可以通过 [cglib 的 maven 库](https://mvnrepository.com/artifact/cglib/cglib/3.2.12)的编译依赖中找到线索：

![图片描述](https://img.mukewang.com/5db65c1900018c7d18440388.png)
发现该库依赖了 asm ，我们去 [asm 官网](https://asm.ow2.io/)可以看到它的介绍：

> asm 库是一个 Java 字节码操作和分析框架，它可以用来修改已经存在的字节码或者直接二进制形式动态生成 class 文件。asm 的特点是小且快。

、同样的，我们可以通过 [orika 的 maven 库](https://mvnrepository.com/artifact/ma.glasnost.orika/orika-core/1.5.4)得到其实现依赖的核心技术：

![图片描述](https://img.mukewang.com/5db65c2b0001fa9e18260816.png)
其中 javassist 我们知道它是一个字节码操作工具。

我们去[它的](https://asm.ow2.io/)[官网](http://www.javassist.org/)看下介绍：

> javassist 让操作字节码非常容易。javassist 允许 java 程序运行时定义一个新的类，也可以实现在 JVM 加载类文件时修改它。javassist 提供两种级别的 API ，一种是源码级别；一种是字节码级别。使用源码级别的 API，无需对 java 字节码特定知识有深入的了解就可以轻松修改类文件。字节码级别的 API 则允许用户直接修改类文件。

6、通过 [MapStruct 的官网](https://mapstruct.org/)的介绍我们可以看出，mapstruct 采用原生的方法调用，因此更快速，更安全也更容易理解。根据官网的介绍我们知道，使用时只需要使用它的注解，定义好转换接口，转换函数，编译时会自动生成转换工具的实现类、调用属性赋值和取值函数实现转换。mapstruct 还支持通过注解形式定义不同属性名的映射关系等，功能很强大。

转换代码：

```java
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = (UserMapper)Mappers.getMapper(UserMapper.class);

    UserDTO userDo2Dto(UserDO var1);
}
```

编译后生成自动的转换接口的实现类：

```java
public class UserMapperImpl implements UserMapper {
    public UserMapperImpl() {
    }

    public UserDTO userDo2Dto(UserDO userDO) {
        if (userDO == null) {
            return null;
        } else {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(userDO.getName());
            userDTO.setAge(userDO.getAge());
            userDTO.setNickName(userDO.getNickName());
            userDTO.setBirthDay(userDO.getBirthDay());
            return userDTO;
        }
    }
}
```

大大简化了代码。

官方还提供了非常详细的[参考文档](https://mapstruct.org/documentation/reference-guide/) 和使用范例，提供了很多高级用法。



#### 2.3 性能

接下来按照惯例，我们对比一下它们的性能。

我们在 `com.imooc.basic.converter.UserConverterTest` 类中对上面的常见对象转换方式进行单测 `UserDO` 对象：

```java
@Data
public class UserDO {
    private Long id;
    private String name;
    private Integer age;
    private String nickName;
    private Date birthDay;
}
```

目标对象：

```java
@Data
public class UserDTO {
    private String name;
    private Integer age;
    private String nickName;
    private Date birthDay;
}
```

使用 easyrandom（后面的单元测试环节会重点介绍）构造 10 万个 `UserDO` 随机对象进行性能对比。spring 版本为 5.1.8.RELEASE，dozer 版本为 5.5.1，orika-core 版本为 1.5.4，cglib 版本为 3.2.12，commons-lang3 包版本为 3.9，10 次运行取平均值，最终结果如下：

1. 普通 Getter/Setter 耗时 365ms；
2. `org.apache.commons.beanutils.BeanUtils#copyPropertie` 耗时 9s273ms；
3. `org.springframework.beans.BeanUtils#copyProperties(java.lang.Object, java.lang.Object)` 耗时 2s327ms；
4. `org.dozer.Mapper#map(java.lang.Object, java.lang.Class)` 耗时 9s271ms；
5. `ma.glasnost.orika.MapperFacade#map(S, D)` 耗时 837ms；
6. `net.sf.cglib.beans.BeanCopier#copy` 耗时 409ms；
7. MapStruct 393ms。

![图片描述](https://img.mukewang.com/5db65c4100013ce912961116.png)
由于机器的性能不同结果会有偏差，本实验并没有将转换框架的功能发挥到到极致，也没有使用更复杂的对象进行对比，因此本实验的结果仅作为一个大致的参考。

我们仍然可以大致可以得出结论：采用字节码增强技术的 Java 属性转换工具和普通的 Getter/Setter 方法性能相差无几，甚至比 Getter/Setter 效率还高，反射的性能相对较差。

因此从性能来讲首推 Getter/Setter 方式（含 MapStruct），其次是 cglib。



### 3. 用哪个？为什么？怎么用？



#### 3.1 用什么？为什么？

通过以上的分析，我们对 Java 属性转换有了一个基本的了解。

**选择太多往往会比较纠结，实际开发中我们用哪种更好呢？**

我在业务代码中见到同事用的转换工具主要有 Getter/Setter 方式、 orika 和 commons/spring 的属性拷贝工具。

**属性转换工具的优势**：用起来方便，往往一行行代码就实现多属性的转换，而且属性不对应可以通过注解或者修改配置方式自动适配，功能非常强大。

**属性转换工具的缺点**：

1. 多次对象映射（从 A 映射到 B，再从 B 映射到 C）如果属性不完全一致容易出错；
2. 有些转换工具，属性类型不一致自动转换容易出现意想不到的 BUG；
3. 基于反射和字节码增强技术的映射工具实现的映射，对一个类属性的修改不容易感知到对其它转换类的影响。

**我们可以想想这样一个场景**：

> 一个 `UserDO` 如果属性超多，转换到 `UserDTO` 再被转换成 `UserVO` 。如果你修改 `UserDTO` 的一个属性命名，其它类待映射的类新增的对应属性有一个字母写错了，编译期间不容易发现问题，造成 BUG。
>
> 如果使用原始的 Getter/Setter 方式转换，修改了 `UserDO` 的属性，那么转换代码就会报错，编译都不通过，这样就可以逆向提醒我们注意到属性的变动的影响。

**因此强烈建议使用定义转换类和转换函数，使用插件实现转换，不需要引入其它库，降低了复杂性，可以支持更灵活的映射。**

大家可以想想这种场景：

> 如果一个 A 映射到 B，B 有两个属性来自 C，一个属性来自于传参或者计算等。

此时自定义转换函数就更方便。

**如果使用属性映射工具推荐使用 MapStruct，更安全一些，转换效率也很高。**



#### 3.2 怎么用？

每种对象属性映射工具的具体用法，大家可以参考官网文档或源码中的测试类，这里主要讲映射的工具类该如何定义。

为了避免转换函数散落到多个业务类中，不容易复用，我们可以在工具包或者对象包下定义一个专门的转换包（converter 或者 mapper 包），在转换的包下编写转换工具类。

**第一种方式**：可以实现 `org.springframework.core.convert.converter.Converter` 接口。

代码如下：

```java
import org.springframework.core.convert.converter.Converter;

public class UserDO2DTOConverter implements Converter<UserDO, UserDTO> {

    @Override
    public UserDTO convert(UserDO source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(source.getName());
        userDTO.setAge(source.getAge());
        userDTO.setNickName(source.getNickName());
        userDTO.setBirthDay(source.getBirthDay());
        return userDTO;
    }
}
```

上述只能实现单向转换，**我们如果想双向转换该怎么做呢？**

这时候我们可以采用**第二种方式**，可以继承 `com.google.common.base.Converter` 接口实现双向转换。

```java
import com.imooc.basic.converter.entity.UserDO;
import com.imooc.basic.converter.entity.UserDTO;
import com.google.common.base.Converter;

public class UserDO2DTOConverter extends Converter<UserDO, UserDTO> {

    @Override
    protected UserDTO doForward(UserDO userDO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(userDO.getName());
        userDTO.setAge(userDO.getAge());
        userDTO.setNickName(userDO.getNickName());
        userDTO.setBirthDay(userDO.getBirthDay());
        return userDTO;

    }

    @Override
    protected UserDO doBackward(UserDTO userDTO) {
        UserDO userDO = new UserDO();
        userDO.setName(userDTO.getName());
        userDO.setAge(userDTO.getAge());
        userDO.setNickName(userDTO.getNickName());
        userDO.setBirthDay(userDTO.getBirthDay());
        return userDO;

    }
  }
```

我更建议采用以下这种方式，因为上述方式只能实现单向或者双向转换，如果更多种对象类型的转换就无能为力。

此时可以自定义接口或者抽象类，支持更多种对象的转换。

更推荐大家直接定义某个对象的转换器类，在其内部编写该对象各层对象的转换函数：

```java
public class UserConverter {

    public static UserDTO convertToDTO(UserDO source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(source.getName());
        userDTO.setAge(source.getAge());
        userDTO.setNickName(source.getNickName());
        userDTO.setBirthDay(source.getBirthDay());
        return userDTO;
    }

    public static UserDO convertToDO(UserDO source) {
        UserDO userDO = new UserDO();
        userDO.setId(source.getId());
        userDO.setName(source.getName());
        userDO.setAge(source.getAge());
        userDO.setNickName(source.getNickName());
        userDO.setBirthDay(source.getBirthDay());
        return userDO;
    }
    
  // 转换成UserVO等
}
```

有些同学可能会抱怨，**Getter/Setter 方式转换函数编写非常耗时而且容易漏，怎么办？**

这里推荐一个 IDEA 插件：**GenerateAllSetter** 或者 **GenerateO2O**。

定义好转换函数之后，鼠标放在 `convertToDTO` 上使用快捷键，选择 “generate setter getter converter” 即可实现根据目标对象的属性名适配同名源对象自动填充，注意如果有个别属性不对应，需手动转换。

**另外推荐使用 mapstruct 实现对象属性映射**：

```java
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO  userDo2Dto(UserDO userDO);
}
```

使用时一行代码即可搞定：

```java
UserDTO userDTO = UserMapper.INSTANCE.userDo2Dto(userDO);
```

相当于把 IDE 插件自动生成的这部分任务改为了使用注解，通过插件编译时自动生成。



### 4. 总结

本节主要介绍了 Java 属性映射的各种方式，介绍了每种方式背后的原理，并简单对比了各种属性映射方式的耗时。本小节还给出了属性转换工具的推荐定义方式。希望大家在实际的开发中，除了考虑性能外，兼顾考虑安全性和可维护性。

下节将介绍过期代码的正确处理方式。



### 5. 课后练习

自定义一个 OrderDO 和 OrderDTO 两个类，自定义属性，使用 StructMap 实现属性映射。



##  **07 过期类、属性、接口的正确处理姿势** 

 更新时间：2019-11-01 11:54:21

![img](https://img2.mukewang.com/5db6bae400018d5b06400359.jpg)

![img](https://www.imooc.com/static/img/column/bg-l.png)![img](https://www.imooc.com/static/img/column/bg-r.png)



### 1. 前言

《手册》第 7 页对于过时类有这样一句描述 [1](https://www.imooc.com/read/55/article/1144#fn1)：

> 接口过时必须加 @Deprecated 注解，并清晰地说明采用的新接口或者新服务是什么。
> 接口提供方既然明确是过时接口，那么有义务同时提供新的接口；作为调用方来说，有义务去考证过时方法的新实现是什么。

那么我们要思考为什么要这么做呢？这个指导原则如何更好地落地呢？



### 2. 为什么要这么做

如果有机会进入一个大一点的公司，而且你是一个有追求的人，你可能会遇到下面几种情况。

- 当你接手一个服务，看到某个类、属性、函数被标注为 @Deprecated 但是没有注释的时候，内心是崩溃的；
- 当你对接二方服务，升级 jar 包后发现使用的接口被标记为废弃但是没注释时，内心也是崩溃的；
- 当你看到同事封装的一些工具类使用了一些被废弃的类时，你的内心同样同样是崩溃的。不改放在那看着难受，改又无故得耗费自己的时间，而且还怕改出 BUG。

试想一下，如果你接手一个服务里面的类、属性和函数要被废弃了连 @Deprecated 都不加，是不是很容易 “放心” 使用进而被坑？

如果被标注为 `@Deprecated` ，给出注释说明为什么被废弃，新的接口是什么，心里会不会更踏实？

如果对接的二方服务 jar 包升级以后发现，使用的接口被废弃且给出详细的告诉你改用哪个新接口，是不是心里更有底？

试想一下如果我们每个人都能遵守这种规约，封装工具类时遇到过时的类，主动去学习并使用新的替换类，是不是就不会好很多？



### 3. 如何落实

那么，说了这么多，究竟该如何落地呢？
我认为：最好的学习方式之一就是找一些优秀的源码相关的示例进行学习。



#### 3.1 JDK 的类或常见三方库

我们以 JDK 中的 `URLEncoder` 和 `URLDecoder` 为例介绍如何写过期函数的注释和如何替换该过期函数：

```java
String url = "xxx";
String encode = URLEncoder.encode(url);
log.debug("URL编码结果：" + encode);
String decode = URLDecoder.decode(encode);
log.debug("URL解码结果：" + decode);
```

在 IDEA 中编写如上代码时候，`java.net.URLEncoder#encode(java.lang.String)` 和 `java.net.URLDecoder#decode(java.lang.String)` 会有删除的标志，便表示该函数已经过期。

那么如何找到新函数和修改呢？

我们进到源码里查看:

```java
/**
 * Decodes a {@code x-www-form-urlencoded} string.
 * The platform's default encoding is used to determine what characters
 * are represented by any consecutive sequences of the form
 * "<i>{@code %xy}</i>".
 * @param s the {@code String} to decode
 * @deprecated The resulting string may vary depending on the platform's
 *          default encoding. Instead, use the decode(String,String) method
 *          to specify the encoding.
 * @return the newly decoded {@code String}
 */
@Deprecated
public static String decode(String s) {
    String str = null;
    try {
        str = decode(s, dfltEncName);
    } catch (UnsupportedEncodingException e) {
        // The system should always have the platform default
    }
    return str;
}
```

在 `@deprecated` 的注释里我们找到了答案：“The resulting string may vary depending on the platform’s default encoding.（解析结果的字符串和系统的默认字符编码强关联）”，并给出了替代函数的说明 “Instead, use the `decode(String,String)` method to specify the encoding.（使用 `decode(String,String)` 函数来指定字符串编码）”

因此我们提供新的接口，就得接口要废弃时也可以参考这里**写上废弃的原因以及替代的新接口**。

我们还可以通过 [codota](https://www.codota.com/code/query) 来搜索（建议在 IDEA 安装插件，使用更方便）看常见类库的常见函数的用法，甚至可以看到某些函数的使用概率：

![图片描述](https://img.mukewang.com/5db65c9600011e4909680261.png)
搜索我们想要的类和方法：[URLEncoder.encode](https://www.codota.com/code/query/java.net@URLEncoder@encode)，即可得到 github 优秀的开源框架或 stackoverflow 中相关优秀范例。根据相关的优秀代码范例进行修改。

![图片描述](https://img.mukewang.com/5db65c820001b48909150715.png)

我们改用新的函数：

```java
    String url = "xxx";
    String encode = URLEncoder.encode(url, Charsets.UTF_8.name());
    log.debug("URL编码结果：" + encode);
    String decode = URLDecoder.decode(encode, Charsets.UTF_8.name());
    log.debug("URL解码结果：" + decode);
```

对类似废弃的接口的改动，最好要使用单元测试进行验证：

```java
/**
 * 新旧两种接口对比
 *
 * @throws UnsupportedEncodingException
 */
@Test
public void testURLUtil() throws UnsupportedEncodingException {

    String url = "http://www.imooc.com/test?name=张三";
    // 旧的函数
    String encodeOrigin = URLEncoder.encode(url);
    String decodeOrigin = URLDecoder.decode(encodeOrigin);

    // 新的函数
    String encodeNew = URLEncoder.encode(url, Charsets.UTF_8.name());
    String decodeNew = URLDecoder.decode(encodeNew, Charsets.UTF_8.name());

    // 结果对比
    Assert.assertEquals(encodeOrigin, encodeNew);
    Assert.assertEquals(decodeOrigin, decodeNew);
}
```

如果是常见的**三方库**，也可以采用类似的步骤，一般都很快解决问题。

如我们发现下面的函数被废弃，进入到源码中查看：

```
org.springframework.util.Assert#doesNotContain(java.lang.String, java.lang.String)
/**
 * @deprecated as of 4.3.7, in favor of {@link #doesNotContain(String, String, String)}
 */
@Deprecated
public static void doesNotContain(String textToSearch, String substring) {
   doesNotContain(textToSearch, substring,
         "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
}
```

直接通过点击 `{@link #doesNotContain(String, String, String)` 可以快速进入新的替代函数去查看。

从这里例子我们还学到了一个新的技巧，如果是二方库或者三方库，废弃的属性、函数在注释中除了可以写原因和替代函数外，可以标注从哪个版本被标注为废弃。替代函数可以使用 `{@link}` 方式，更便捷和优雅。

再回顾上面 `java.net.URLDecoder#decode(java.lang.String)` 的注释就没有提供这种方式，跳转就不够方便。

另外大家还可以学习一下 `@see` 的用法，以及 `@see` 和 `{@link}` 的区别，后面专栏也会对注释做专门的讲解。

我们从这个例子还可以看到注释中并没有说明废弃的原因，作为读者你会发现有些摸不着头脑，心里嘀咕 “为啥被废弃？”。

通过替换函数以及注释我们可以猜测废弃的原因是：” 默认的提示文本不够优雅 “且即使断言通过，第三个参数字符串拼接仍然会执行，造成不必要字符串连接操作。这点有点类似于日志中不建议使用字符串拼接当做日志内容（可以采用占位符的方式）。

新的替换函数的注释除了给出功能介绍外，也给出了使用的范例：

```java
Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");
```

这里给我们带来的启发是，**写工具类时如果能再注释上添加一些范例和结果，则会极大方便使用者**。

这点在 `commons-lang3` 和 `guava` 等开源工具库中随处可见，值得我们学习。

随手选取一个例子，大家感受一下：

```java
/**
 * <p>Strips whitespace from the start and end of a String  returning
 * {@code null} if the String is empty ("") after the strip.</p>
 *
 * <p>This is similar to {@link #trimToNull(String)} but removes whitespace.
 * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
 *
 * <pre>
 * StringUtils.stripToNull(null)     = null
 * StringUtils.stripToNull("")       = null
 * StringUtils.stripToNull("   ")    = null
 * StringUtils.stripToNull("abc")    = "abc"
 * StringUtils.stripToNull("  abc")  = "abc"
 * StringUtils.stripToNull("abc  ")  = "abc"
 * StringUtils.stripToNull(" abc ")  = "abc"
 * StringUtils.stripToNull(" ab c ") = "ab c"
 * </pre>
 *
 * @param str  the String to be stripped, may be null
 * @return the stripped String,
 *  {@code null} if whitespace, empty or null String input
 * @since 2.0
 */
public static String stripToNull(String str) {
    if (str == null) {
        return null;
    }
    str = strip(str, null);
    return str.isEmpty() ? null : str;
}
```

对于常见的三方库，还有一个不错的技巧：我们可以从 github 上拉取其源代码，然后找到某个类对应的单元测试类中，在单元测试模块可以找到对应的参考用法。还可以在源码中打断点，进行深入研究。希望大家可以亲自实践，会有更加深刻的体会。



#### 3.2 二方库

作为接口的使用者，如果使用二方库，发现使用的功能被标注为废弃。

如果是 maven 项目可以通过 maven 命令拉取其源码和 javadoc。

```
mvn dependency:sources -DdownloadSources=true -DdownloadJavadocs=true
```

如果是 gradle 项目，也可以使用插件下载源码，查看其将被废弃的原因。

如果没有标注原因并给出替代方案，或给出的注释不够详细，建议直接和二方包的提供者联系，及早替换。

二方库的工具类替换成新的接口也必须要通过单测，并对涉及的功能进行回归。



#### 3.3 自己库

作为接口或对象的提供者，废弃的类、属性、函数加上废弃的原因和替代方案。

如 RPC 订单常见接口的 `OrderCreateParam` 参数类的 JSON 类型参数：`orderItemDetail` 要替换成列表 `orderItemParams` 下面的属性类型进行替换：

```java
public class OrderCreateParam {

    /**
     * 对象详情
     * 参考示例：'[{"count":22,"name":"商品1"},{"count":33,"name":"商品2"}]'
     * <p>
     * 废弃原因：订单详情由JSON传参，改为对象传参。
     * 替代方案： {@link com.imooc.basic.deprecated.OrderCreateParam#orderItemParams}
     */
    @Deprecated
    private String orderItemDetail;

    private List<OrderItemParam> orderItemParams;

    // 其他属性
}
```

自己类的变动要通过单元测试进行验证：

```java
@Test
public void testOriginAndNew() {

    OrderCreateParam orderCreateParamOrigin = new OrderCreateParam();
    // 原始JSON属性
    orderCreateParamOrigin.setOrderItemDetail("[{\"count\":22,\"name\":\"商品1\"},{\"count\":33,\"name\":\"商品2\"}]");

    OrderCreateParam orderCreateParamNew = new OrderCreateParam();
    // 新的对象属性
    List<OrderItemParam> orderItemParamList = new ArrayList<>(2);
    OrderItemParam orderItemParam = new OrderItemParam();
    orderItemParam.setName("商品1");
    orderItemParam.setCount(22);
    orderItemParamList.add(orderItemParam);

    OrderItemParam orderItemParam2 = new OrderItemParam();
    orderItemParam2.setName("商品2");
    orderItemParam2.setCount(33);
    orderItemParamList.add(orderItemParam2);
    orderCreateParamNew.setOrderItemParams(orderItemParamList);

    Assert.assertEquals(JSON.toJSONString(orderCreateParamNew.getOrderItemParams()), orderCreateParamOrigin.getOrderItemDetail());
}
```

这里给出一个简单的模拟范例，实际业务代码中参数的接口还要进行 mock 单元测试（后续章节会有相关介绍），对应接口要根据变动传入不同的参数进行功能测试。

如如果实际开发中自己需要改动的功能涉及到废弃的类、属性、函数等，且没有详细地注释，无法获知废弃的原因和替代的方案。可以通过 IDEA 的 “annotate” 菜单，或者 “Git” - ”Show History for Selection“ 等来查看添加废弃注解的人员与之联系。避免自己错代码，如果搞明白问题且仍然不能废弃，最好能够主动将废弃的原因和替代的代码补充到注释中。

如果是三方或二方库，由于作者责任性不强或者职业素养不高，对某个接口标记废弃且没有任何注释时，我们优先在本类中寻找函数签名相似的函数。如果是开源项目或者公司内部可以拉取的项目，可以拉取该项目代码，找到该类查看提交记录，从中寻找线索。

不管是三方、二方还是自己的项目，对替换废弃的类、属性和方法等进行修改后，一定要通过单元测试去验证功能并且对接口使用的功能进行功能测试。

如果要删除废弃的属性或接口，一般先提供新的方案通知使用方修改，此时可以在将废弃的接口上加上日志，新旧接口同时运行一段时间后确认无调用再下一个版本中考虑删除接口。

如果我们能快速找到替代的方案，就可以节省很多时间；如果我们能够充分地测试，就可以平稳替换；如果我们能够介绍清楚废弃的原因，提供新的替代方案，并给出快捷的跳转方式，我们的专业程度就会提高。



### 4. 总结

本节的主要介绍过期类、属性、接口的正确处理姿势，包括添加废弃注解，添加废弃的原因，添加新接口的跳转等方式，还要在替换后对新接口进行测试测试。本小节还介绍了通过查看相关的优秀开源代码、使用 codota 工具来学习相关知识的方法。

下节我们将学习开发中经常碰到的又爱又恨的空指针，了解其产生的主要原因，学习如何尽可能地避免。



### 5. 思考题

从 github 上拉取 Spring 源码，找到 `org.springframework.util.Assert#doesNotContain(java.lang.String, java.lang.String, java.lang.String)` 方法的单元测试代码并运行。



### 参考资料

------

1. 阿里巴巴与 Java 社区开发者.《 Java 开发手册 1.5.0：华山版》:2019:7 [↩︎](https://www.imooc.com/read/55/article/1144#fnref1)



## 08 空指针引发的血案

更新时间：2019-11-04 13:57:07

![img](https://img.mukewang.com/5db6bb080001b51b06400359.jpg)

![img](https://www.imooc.com/static/img/column/bg-l.png)![img](https://www.imooc.com/static/img/column/bg-r.png)



### 1. 前言

《手册》的第 7 页和 25 页有两段关于空指针的描述 [1](https://www.imooc.com/read/55/article/1145#fn1)：

> 【强制】Object 的 equals 方法容易抛空指针异常，应使用常量或确定有值的对象来调用 equals。
>
> 【推荐】防止 NPE，是程序员的基本修养，注意 NPE 产生的场景:
>
> 1. 返回类型为基本数据类型，return 包装数据类型的对象时，自动拆箱有可能产生 NPE。
>
> 反例:public int f () { return Integer 对象}， 如果为 null，自动解箱抛 NPE。
>
> 1. 数据库的查询结果可能为 null。
> 2. 集合里的元素即使 isNotEmpty，取出的数据元素也可能为 null。
> 3. 远程调用返回对象时，一律要求进行空指针判断，防止 NPE。
> 4. 对于 Session 中获取的数据，建议进行 NPE 检查，避免空指针。
> 5. 级联调用 obj.getA ().getB ().getC (); 一连串调用，易产生 NPE。

《手册》对空指针常见的原因和基本的避免空指针异常的方式给了介绍，非常有参考价值。

那么我们思考以下几个问题：

- 如何学习 `NullPointerException`（简称为 NPE）？
- 哪些用法可能造 NPE 相关的 BUG？
- 在业务开发中作为接口提供者和使用者如何更有效地避免空指针呢？



### 2. 了解空指针



#### 2.1 源码注释

前面介绍过源码是学习的一个重要途径，我们一起看看 `NullPointerException` 的源码：

```java
/**
 * Thrown when an application attempts to use {@code null} in a
 * case where an object is required. These include:
 * <ul>
 * <li>Calling the instance method of a {@code null} object.
 * <li>Accessing or modifying the field of a {@code null} object.
 * <li>Taking the length of {@code null} as if it were an array.
 * <li>Accessing or modifying the slots of {@code null} as if it
 *     were an array.
 * <li>Throwing {@code null} as if it were a {@code Throwable}
 *     value.
 * </ul>
 * <p>
 * Applications should throw instances of this class to indicate
 * other illegal uses of the {@code null} object.
 *
 * {@code NullPointerException} objects may be constructed by the
 * virtual machine as if {@linkplain Throwable#Throwable(String,
 * Throwable, boolean, boolean) suppression were disabled and/or the
 * stack trace was not writable}.
 *
 * @author  unascribed
 * @since   JDK1.0
 */
public
class NullPointerException extends RuntimeException {
    private static final long serialVersionUID = 5162710183389028792L;

    /**
     * Constructs a {@code NullPointerException} with no detail message.
     */
    public NullPointerException() {
        super();
    }

    /**
     * Constructs a {@code NullPointerException} with the specified
     * detail message.
     *
     * @param   s   the detail message.
     */
    public NullPointerException(String s) {
        super(s);
    }
}
```

源码注释给出了非常详尽地解释：

> 空指针发生的原因是应用需要一个对象时却传入了 `null`，包含以下几种情况：
>
> 1. 调用 null 对象的实例方法。
> 2. 访问或者修改 null 对象的属性。
> 3. 获取值为 null 的数组的长度。
> 4. 访问或者修改值为 null 的二维数组的列时。
> 5. 把 null 当做 Throwable 对象抛出时。

**实际编写代码时，产生空指针的原因都是这些情况或者这些情况的变种。**

《手册》中的另外一处描述

> “集合里的元素即使 isNotEmpty，取出的数据元素也可能为 `null`。”

和第 4 条非常类似。

如《手册》中的：

> “级联调用 obj.getA ().getB ().getC (); 一连串调用，易产生 NPE。”

和第 1 条很类似，因为每一层都可能得到 `null` 。

当遇到《手册》中和源码注释中所描述的这些场景时，要注意预防空指针。

另外通过读源码注释我们还得到了 “意外发现”，JVM 也可能会通过 `Throwable#Throwable(String, Throwable, boolean, boolean)` 构造函数来构造 `NullPointerException` 对象。



#### 2.2 继承体系

通过源码可以看到 NPE 继承自 `RuntimeException` 我们可以通过 IDEA 的 “Java Class Diagram” 来查看类的继承体系。

![图片描述](https://img.mukewang.com/5db65d0400019f9309360449.png)
可以清晰地看到 NPE 继承自 `RuntimeException` ，另外我们选取 `NoSuchFieldException` 和 `NoSuchFieldError` 和 `NoClassDefFoundError` ，可以看到 `Throwable` 的子类型包括 `Error` 和 `Exception`, 其中 NPE 又是 `Exception` 的子类。

那么为什么 `Exception` 和 `Error` 有什么区别？ `Excption` 又分为哪些类型呢？

我们可以分别去 `java.lang.Exception` 和 `java.lang.Error` 的源码注释中寻找答案。

通过 `Exception` 的源码注释我们了解到， `Exception` 分为两类一种是非受检异常（uncheked exceptions）即 `java.lang.RuntimeException` 以及其子类；而受检异常（checked exceptions）的抛出需要再普通函数或构造方法上通过 `throws` 声明。

通过 `java.lang.Error` 的源码注释我们了解到，`Error` 代表严重的问题，不应该被程序 `try-catch`。编译时异常检测时， `Error` 也被视为不可检异常（uncheked exceptions）。

大家可以在 IDEA 中分别查看 `Exception` 和 `Error` 的子类，了解自己开发中常遇到的异常都属于哪个分类。

我们还可以通过《JLS》[2](https://www.imooc.com/read/55/article/1145#fn2) 第 11 章 [Exceptions](https://docs.oracle.com/javase/specs/jls/se8/html/jls-11.html) 对异常进行学习。

其中在异常的类型这里，讲到：

> 不可检异常（ *unchecked exception* ）包括运行时异常和 error 类。
>
> 可检异常（ *unchecked exception* ）不属于不可检异常的所有异常都是可检异常。除 RuntimeException 和其子类，以及 Error 类以及其子类外的其他 Throwable 的子类。

![图片描述](https://img.mukewang.com/5db65cf800014cea06860267.png)
还有更多关于异常的详细描述，，包括异常的原因、异步异常、异常的编译时检查等，大家可以自己进一步学习。



### 3. 空指针引发的血案



#### 3.1 最常见的错误姿势

```java
 @Test
    public void test() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            List<UserDTO> users = new ArrayList<>();
            users.add(new UserDTO(1L, 3));
            users.add(new UserDTO(2L, null));
            users.add(new UserDTO(3L, 3));
            send(users);
        });

    }

    // 第 1 处
    private void send(List<UserDTO> users) {
        for (UserDTO userDto : users) {
            doSend(userDto);
        }
    }

    private static final Integer SOME_TYPE = 2;

    private void doSend(UserDTO userDTO) {
        String target = "default";
        // 第 2 处
        if (!userDTO.getType().equals(SOME_TYPE)) {
            target = getTarget(userDTO.getType());
        }
        System.out.println(String.format("userNo:%s, 发送到%s成功", userDTO, target));
    }

    private String getTarget(Integer type) {
        return type + "号基地";
    }
```

在第 1 处，如果集合为 `null` 则会抛空指针；

在第 2 处，如果 `type` 属性为 `null` 则会抛空指针异常，导致后续都发送失败。

大家看这个例子觉得很简单，看到输入的参数有 `null` 本能地就会考虑空指针问题，但是自己写代码时你并不知道上游是否会有 `null`。



#### 3. 2 无结果仍返回对象

实际开发中有些同学会有一些非常 “个性” 的写法。

为了避免空指针或避免检查到 null 参数抛异常，直接返回一个空参构造函数创建的对象。

类似下面的做法：

```java
/**
 * 根据订单编号查询订单
 *
 * @param orderNo 订单编号
 * @return 订单
 */
public Order getByOrderNo(String orderNo) {

    if (StringUtils.isEmpty(orderNo)) {
        return new Order();
    }
    // 查询order
    return doGetByOrderNo(orderNo);
}
```

由于常见的单个数据的查询接口，参数检查不符时会抛异常或者返回 `null`。 极少有上述的写法，因此调用方的惯例是判断结果不为 `null` 就使用其中的属性。

这个哥们这么写之后，上层判断返回值不为 `null` , 上层就放心大胆得调用实例函数，导致线上报空指针，就造成了线上 BUG。



#### 3.3 新增 @NonNull 属性反序列化的 BUG

假如有一个订单更新的 RPC 接口，该接口有一个 `OrderUpdateParam` 参数，之前有两个属性一个是 `id` 一个是 `name` 。在某个需求时，新增了一个 extra 属性，且该字段一定不能为 `null` 。

采用 lombok 的 `@NonNull` 注解来避免空指针：

```java
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class OrderUpdateParam implements Serializable {
    private static final long serialVersionUID = 3240762365557530541L;

    private Long id;

    private String name;

     // 其它属性
  
    // 新增的属性
    @NonNull
    private String extra;
}
```

上线后导致没有使用最新 jar 包的服务对该接口的 RPC 调用报错。

我们来分析一下原因，在 IDEA 的 target - classes 目录下找到 DEMO 编译后的 class 文件，IDEA 会自动帮我们反编译：

```java
public class OrderUpdateParam implements Serializable {
    private static final long serialVersionUID = 3240762365557530541L;
    private Long id;
    private String name;
    @NonNull
    private String extra;

    public OrderUpdateParam(@NonNull final String extra) {
        if (extra == null) {
            throw new NullPointerException("extra is marked non-null but is null");
        } else {
            this.extra = extra;
        }
    }

    @NonNull
    public String getExtra() {
        return this.extra;
    }
    public void setExtra(@NonNull final String extra) {
        if (extra == null) {
            throw new NullPointerException("extra is marked non-null but is null");
        } else {
            this.extra = extra;
        }
    }
  // 其他代码

}
```

我们还可以使用反编译工具：[JD-GUI](http://java-decompiler.github.io/) 对编译后的 class 文件进行反编译，查看源码。

由于调用方调用的是不含 `extra` 属性的 jar 包，并且序列化编号是一致的，反序列化时会抛出 NPE。

```java
Caused by: java.lang.NullPointerException: extra

        at com.xxx.OrderUpdateParam.<init>(OrderUpdateParam.java:21)
```

RPC 参数新增 lombok 的 `@NonNull` 注解时，要考虑调用方是否及时更新 jar 包，避免出现空指针。



#### 3.4 自动拆箱导致空指针

前面章节讲到了对象转换，如果我们下面的 `GoodCreateDTO` 是我们自己服务的对象， 而 `GoodCreateParam` 是我们调用服务的参数对象。

```java
@Data
public class GoodCreateDTO {
    private String title;

    private Long price;

    private Long count;
}

@Data
public class GoodCreateParam implements Serializable {

    private static final long serialVersionUID = -560222124628416274L;
    private String title;

    private long price;

    private long count;
}
```

其中 `GoodCreateDTO` 的 `count` 属性在我们系统中是非必传参数，本系统可能为 `null`。

如果我们没有拉取源码的习惯，直接通过前面的转换工具类去转换。

我们潜意识会认为外部接口的对象类型也都是包装类型，这时候很容易因为转换出现 NPE 而导致线上 BUG。

```java
public class GoodCreateConverter {

    public static GoodCreateParam convertToParam(GoodCreateDTO goodCreateDTO) {
        if (goodCreateDTO == null) {
            return null;
        }
        GoodCreateParam goodCreateParam = new GoodCreateParam();
        goodCreateParam.setTitle(goodCreateDTO.getTitle());
        goodCreateParam.setPrice(goodCreateDTO.getPrice());
        goodCreateParam.setCount(goodCreateDTO.getCount());
        return goodCreateParam;
    }
}
```

当转换器执行到 `goodCreateParam.setCount(goodCreateDTO.getCount());` 会自动拆箱会报空指针。

当 `GoodCreateDTO` 的 `count` 属性为 `null` 时，自动拆箱将报空指针。

**再看一个花样踩坑的例子**：

我们作为使用方调用如下的二方服务接口：

```java
public Boolean someRemoteCall();
```

然后自以为对方肯定会返回 `TRUE` 或 `FALSE`，然后直接拿来作为判断条件或者转为基本类型，如果返回的是 `null`，则会报空指针异常：

```java
if (someRemoteCall()) {
   // 业务代码
}
```

大家看示例的时候可能认为这种情况很简单，自己开发的时候肯定会注意，但是往往事实并非如此。

希望大家可以掌握常见的可能发生空指针场景，在开发是注意预防。



#### 3.5 分批调用合并结果时空指针

大家再看下面这个经典的例子。

因为某些批量查询的二方接口在数据较大时容易超时，因此可以分为小批次调用。

下面封装一个将 `List` 数据拆分成每 `size` 个一批数据，去调用 `function` RPC 接口，然后将结果合并。

```java
class a {
    public static <T, V> List<V> partitionCallList(List<T> dataList, int size, Function<List<T>, List<V>> function) {
    
            if (CollectionUtils.isEmpty(dataList)) {
                return new ArrayList<>(0);
            }
            Preconditions.checkArgument(size > 0, "size 必须大于0");
    
            return Lists.partition(dataList, size)
                    .stream()
                    .map(function)
                    .reduce(new ArrayList<>(),
                            (resultList1, resultList2) -> {
                                resultList1.addAll(resultList2);
                                return resultList1;
                            });
    }
}
```

看着挺对，没啥问题，其实则不然。

设想一下，如果某一个批次请求无数据，不是返回空集合而是 null，会怎样？

很不幸，又一个空指针异常向你飞来 …

此时**要根据具体业务场景来判断如何处理这里可能产生的空指针异常**。

如果在某个场景中，返回值为 null 是一定不允许的行为，可以在 function 函数中对结果进行检查，如果结果为 null，可抛异常。

如果是允许的，在调用 map 后，可以过滤 null :

```
// 省略前面代码
.map(function)
.filter(Objects::nonNull)
// 省略后续代码
```



### 4. 预防空指针的一些方法

`NPE` 造成的线上 BUG 还有很多种形式，如何预防空指针很重要。

下面将介绍几种预防 NPE 的一些常见方法：

![图片描述](https://img.mukewang.com/5db65cce0001e2aa16700938.png)



#### 4.1 接口提供者角度



##### 4.1.1 返回空集合

如果参数不符合要求直接返回空集合，底层的函数也使用一致的方式：

```java
class a {
    public List<Order> getByOrderName(String name) {
        if (StringUtils.isNotEmpty(name)) {
            return doGetByOrderName(name);
        }
        return Collections.emptyList();
    }
}
```



##### 4.1.2 使用 Optional

`Optional` 是 Java 8 引入的特性，返回一个 `Optional` 则明确告诉使用者结果可能为空：

```java
class a {
    public Optional<Order> getByOrderId(Long orderId) {
        return Optional.ofNullable(doGetByOrderId(orderId));
    }
}
```

如果大家感兴趣可以进入 `Optional` 的源码，结合前面介绍的 `codota` 工具进行深入学习，也可以结合《Java 8 实战》的相关章节进行学习。



##### 4.1.3 使用空对象设计模式

该设计模式为了解决 NPE 产生原因的第 1 条 “调用 `null` 对象的实例方法”。

在编写业务代码时为了避免 `NPE` 经常需要先判空再执行实例方法：

```java
class a {
    public void doSomeOperation(Operation operation) {
        int a = 5;
        int b = 6;
        if (operation != null) {
            operation.execute(a, b);
        }
    }
}
```

《设计模式之禅》（第二版）554 页在拓展篇讲述了 “空对象模式”。

可以构造一个 `NullXXX` 类拓展自某个接口， 这样这个接口需要为 `null` 时，直接返回该对象即可：

```java
public class NullOperation implements Operation {

    @Override
    public void execute(int a, int b) {
        // do nothing
    }
}
```

这样上面的判空操作就不再有必要， 因为我们在需要出现 `null` 的地方都统一返回 `NullOperation`，而且对应的对象方法都是有的：

```java
public void doSomeOperation(Operation operation) {
    int a = 5;
    int b = 6;
    operation.execute(a, b);
}
```



#### 4.2 接口使用者角度

讲完了接口的编写者该怎么做，我们讲讲接口的使用者该如何避免 `NPE` 。



##### 4.2.1 null 检查

正如《代码简洁之道》第 7.8 节 “别传 null 值” 中所要表达的意义：

> 可以进行参数检查，对不满足的条件抛出异常。

直接在使用前对不能为 `null` 的和不满足业务要求的条件进行检查，是一种最简单最常见的做法。

通过防御性参数检测，可以极大降低出错的概率，提高程序的健壮性：

```java
    @Override
    public void updateOrder(OrderUpdateParam orderUpdateParam) {
        checkUpdateParam(orderUpdateParam);
        doUpdate(orderUpdateParam);
    }

    private void checkUpdateParam(OrderUpdateParam orderUpdateParam) {
        if (orderUpdateParam == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        Long id = orderUpdateParam.getId();
        String name = orderUpdateParam.getName();
        if (id == null) {
            throw new IllegalArgumentException("id不能为空");
        }
        if (name == null) {
            throw new IllegalArgumentException("name不能为空");
        }
    }
```

JDK 和各种开源框架中可以找到很多这种模式，`java.util.concurrent.ThreadPoolExecutor#execute` 就是采用这种模式。

```java
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
     // 其他代码
     }
```

以及 `org.springframework.context.support.AbstractApplicationContext#assertBeanFactoryActive`

```java
protected void assertBeanFactoryActive() {
   if (!this.active.get()) {
      if (this.closed.get()) {
         throw new IllegalStateException(getDisplayName() + " has been closed already");
      }
      else {
         throw new IllegalStateException(getDisplayName() + " has not been refreshed yet");
      }
   }
}
```



##### 4.2.2 使用 Objects

可以使用 Java 7 引入的 Objects 类，来简化判空抛出空指针的代码。

使用方法如下：

```java
private void checkUpdateParam2(OrderUpdateParam orderUpdateParam) {
    Objects.requireNonNull(orderUpdateParam);
    Objects.requireNonNull(orderUpdateParam.getId());
    Objects.requireNonNull(orderUpdateParam.getName());
}
```

原理很简单，我们看下源码；

```java
public static <T> T requireNonNull(T obj) {
    if (obj == null)
        throw new NullPointerException();
    return obj;
}
```



##### 4.2.3 使用 commons 包

我们可以使用 commons-lang3 或者 commons-collections4 等常用的工具类辅助我们判空。

###### 4.2.3.1 使用字符串工具类：org.apache.commons.lang3.StringUtils

```java
public void doSomething(String param) {
    if (StringUtils.isNotEmpty(param)) {
        // 使用param参数
    }
}
```

###### 4.2.3.2 使用校验工具类：org.apache.commons.lang3.Validate

```java
public static void doSomething(Object param) {
    Validate.notNull(param,"param must not null");
}
public static void doSomething2(List<String> parms) {
    Validate.notEmpty(parms);
}
```

该校验工具类支持多种类型的校验，支持自定义提示文本等。

前面已经介绍了读源码是最好的学习方式之一，这里我们看下底层的源码：

```java
public static <T extends Collection<?>> T notEmpty(final T collection, final String message, final Object... values) {
    if (collection == null) {
        throw new NullPointerException(String.format(message, values));
    }
    if (collection.isEmpty()) {
        throw new IllegalArgumentException(String.format(message, values));
    }
    return collection;
}
```

该如果集合对象为 null 则会抛空 `NullPointerException` 如果集合为空则抛出 `IllegalArgumentException`。

通过源码我们还可以了解到更多的校验函数。



##### 4.2.4 使用集合工具类：`org.apache.commons.collections4.CollectionUtils`

```java
public void doSomething(List<String> params) {
    if (CollectionUtils.isNotEmpty(params)) {
        // 使用params
    }
}
```



##### 4.2.5 使用 guava 包

可以使用 guava 包的 `com.google.common.base.Preconditions` 前置条件检测类。

同样看源码，源码给出了一个范例。原始代码如下：

```java
public static double sqrt(double value) {
    if (value < 0) {
        throw new IllegalArgumentException("input is negative: " + value);
    }
    // calculate square root
}
```

使用 `Preconditions` 后，代码可以简化为：

```java
 public static double sqrt(double value) {
   checkArgument(value >= 0, "input is negative: %s", value);
   // calculate square root
 }
 
```

Spring 源码里很多地方可以找到类似的用法，下面是其中一个例子：

```
org.springframework.context.annotation.AnnotationConfigApplicationContext#register
public void register(Class<?>... annotatedClasses) {
    Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
    this.reader.register(annotatedClasses);
}
org.springframework.util.Assert#notEmpty(java.lang.Object[], java.lang.String)
public static void notEmpty(Object[] array, String message) {
    if (ObjectUtils.isEmpty(array)) {
        throw new IllegalArgumentException(message);
    }
}
```

虽然使用的具体工具类不一样，核心的思想都是一致的。



##### 4.2.6 自动化 API

###### 4.2.6.1 使用 lombok 的 `@Nonnull` 注解

```java
 public void doSomething5(@NonNull String param) {
      // 使用param
      proccess(param);
 }
```

查看编译后的代码：

```java
 public void doSomething5(@NonNull String param) {
      if (param == null) {
          throw new NullPointerException("param is marked non-null but is null");
      } else {
          this.proccess(param);
      }
  }
```

###### 4.2.6.2 使用 IntelliJ IDEA 提供的 @NotNull 和 @Nullable 注解

maven 依赖如下：

```xml
<!-- https://mvnrepository.com/artifact/org.jetbrains/annotations -->
<dependency>
    <groupId>org.jetbrains</groupId>
    <artifactId>annotations</artifactId>
    <version>17.0.0</version>
</dependency>
```

@NotNull 在参数上的用法和上面的例子非常相似。

```java
public static void doSomething(@NotNull String param) {
    // 使用param
    proccess(param);
}
```

我们可以去该注解的源码 `org.jetbrains.annotations.NotNull#exception` 里查看更多细节，大家也可以使用 IDEA 插件或者前面介绍的 JD-GUI 来查看编译后的 class 文件，去了解 @NotNull 注解的作用。



### 5. 总结

本节主要讲述空指针的含义，空指针常见的中枪姿势，以及如何避免空指针异常。下一节将为你揭秘 当 switch 遇到空指针，又会发生什么奇妙的事情。



### 参考资料

------

1. 阿里巴巴与 Java 社区开发者.《 Java 开发手册 1.5.0：华山版》.2019:7,25 [↩︎](https://www.imooc.com/read/55/article/1145#fnref1)
2. James Gosling, Bill Joy, Guy Steele, Gilad Bracha, Alex Buckley.《Java Language Specification: Java SE 8 Edition》. 2015 [↩︎](https://www.imooc.com/read/55/article/1145#fnref2)



##  **09 当switch遇到空指针** 

更新时间：2019-11-06 18:58:59

![img](https://img2.mukewang.com/5dc292950001139406400359.jpg)

![img](https://www.imooc.com/static/img/column/bg-l.png)![img](https://www.imooc.com/static/img/column/bg-r.png)



### 1. 前言

《手册》的第 18 页有关于 `switch` 的规约：

> 【强制】当 switch 括号内的变量类型为 String 并且此变量为外部参数时，必须先进行 null
> 判断。[1](https://www.imooc.com/read/55/article/1146#fn1)

在《手册》中，该规约下面还给出了一段反例（此处略）。

最近很火的一篇名为《悬赏征集！5 道题征集代码界前 3% 的超级王者》[2](https://www.imooc.com/read/55/article/1146#fn2) 的文章，也给出了类似的一段代码：

```java
public class SwitchTest {
    public static void main(String[] args) {
        String param = null;
        switch (param) {
            case "null":
                System.out.println("null");
                break;
            default:
                System.out.println("default");
        }
    }
}
```

该文章给出的问题是：“上面这段程序输出的结果是什么？”。

其实，想知道答案很容易，运行一下程序答案就出来了。

**但是如果浅尝辄止，我们就丧失了一次难得的学习机会**，不像是一名优秀程序猿的作风。

我们还需要思考下面几个问题：

- `switch` 除了 `String` 还支持哪种类型？
- 为什么《手册》规定字符串类型参数要先进行 null 判断？
- 为什么可能会抛出异常？
- 该如何分析这类问题呢？

本节将对上述问题进行分析。



### 2. 问题分析



#### 2.1 源码大法

按照我们一贯的风格，我们应该先上 “源码大法”，但是 `switch` 是关键字，无法进入 JDK 源码中查看学习，因此我们暂时放弃通过源码或源码注释来分析解决的手段。



#### 2.2 官方文档

我们去官方文档 JLS[3](https://www.imooc.com/read/55/article/1146#fn3) 查看 `swtich` 语句[相关描述](https://docs.oracle.com/javase/specs/jls/se8/html/jls-14.html#jls-14.11)。

> switch 的表达式必须是 char, byte, short, int, Character, Byte, Short, Integer, String, 或者 enum 类型，否则会发生编译错误
>
> switch 语句必须满足以下条件，否则会出现编译错误：
>
> - 与 switch 语句关联的每个 case 都必须和 switch 的表达式的类型一致；
> - 如果 switch 表达式是枚举类型，case 常量也必须是枚举类型；
> - 不允许同一个 switch 的两个 case 常量的值相同；
> - 和 switch 语句关联的常量不能为 null ；
> - 一个 switch 语句最多有一个 default 标签。

![图片描述](https://img.mukewang.com/5dc28adc000186df15660988.png)

我们了解到 switch 语句支持的类型，以及会出现编译错误的原因。

我们看到关键的一句话：

> When the switch statement is executed, first the Expression is evaluated. If the Expression evaluates to null, a NullPointerException is thrown and the entire switch statement completes abruptly for that reason.
>
> switch 语句执行的时候，首先将执行 switch 的表达式。如果表达式为 null, 则会抛出 NullPointerException，整个 switch 语句的执行将被中断。

这里的表达式就是我们的参数，前言中该参数的值为 `null`, 因此答案就显而易见了：结果会抛出异常，而且是前面章节讲到的 `NullPointerException`。

另外从 JVM[4](https://www.imooc.com/read/55/article/1146#fn4) [3.10 节 “Compiling Switches”](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-3.html#jvms-3.10) ，我们学习到：

> 编译器使用 tableswitch 和 lookupswitch 指令生成 switch 语句的编译代码。tablesswtich 语句用于表示 swtich 结构的 case 语句块，它可以地从索引表中确定 case 语句块的分支偏移量。当 switch 语句的条件值不能对应索引表的任何一个 case 语句块的偏移量时就会用到 default 语句。
>
> Java 虚拟机的 tableswitch 和 lookupswitch 指令只能支持 int 类型的条件值。如果 swich 中使用其他类型的值，那么就必须转化为 int 类型。
>
> 当 switch 语句中的 case 分支条件比较稀疏时， tableswtich 指令的空间利用率较低。 可以使用 lookupswitch 指令来取代。
>
> lookupswitch 指令的索引表项由 int 类型的键（来自于 case 语句后的数值）和对应目标语句的偏移量构成。 当 lookcupswitch 指令执行时， switch 语句的条件值将和索引表中的键进行比对，如果某个键和条件的值相符，那么将转移到这个键对应的分支偏移量的代码行处开始执行，如果没有符合的键值，则执行 default 分支。

因此我们可以推测出，表达式会将 String 的参数转成 int 类型的值和 case 进行比对。

我们去 `String` 源码中寻找可以将字符串转 int 的函数，发现 `hashCode()` 可能是最佳的选择之一（后面会印证）。

因此空指针出现的根源在于：**虚拟机为了实现 switch 的语法，将参数表达式转换成 int。而这里的参数为 null， 从而造成了空指针异常**。

通过官方文档的阅读，我们对 switch 有了一个相对深入的了解。



#### 2.3 Java 反汇编大法

如何印证官方文档的描述？如何进一步分析呢？

按照惯例我们用反汇编大法。



##### 2.3.1 switch 举例

我们先看一个正常的示例：

```java
public static void main(String[] args) {
        String param = "t";
        switch (param) {
            case "a":
                System.out.println("a");
                break;
            case "b":
                System.out.println("b");
                break;
            case "c":
                System.out.println("c");
                break;
            default:
                System.out.println("default");
        }
```

先进入到代码目录，对类文件进行编译：

```
javac SwitchTest2.java
```

然后反汇编的代码如下：

```
javap -c SwitchTest2
```

前方高能预警，先稳住，不要怕，不要方，后面会给出解释并给出简化版：

```java
Compiled from "SwitchTest.java"
public class com.imooc.basic.learn_switch.SwitchTest {
  public com.imooc.basic.learn_switch.SwitchTest();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: aconst_null
       1: astore_1
       2: aload_1
       3: astore_2
       4: iconst_m1
       5: istore_3
       6: aload_2
       7: invokevirtual #2                  // Method java/lang/String.hashCode:()I
      10: lookupswitch  { // 1
               3392903: 28
               default: 39
          }
      28: aload_2
      29: ldc           #3                  // String null
      31: invokevirtual #4                  // Method java/lang/String.equals:(Ljava/lang/Object;)Z
      34: ifeq          39
      37: iconst_0
      38: istore_3
      39: iload_3
      40: lookupswitch  { // 1
                     0: 60
               default: 71
          }
      60: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
      63: ldc           #3                  // String null
      65: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      68: goto          79
      71: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
      74: ldc           #7                  // String default
      76: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      79: return
}
```

首先介绍一个简单的背景知识：

> 字符 a 的 ASCII 码为 97, b 为 98，c 为 99 （我们发现常见英文字母的哈希值为其 ASCII 码）。

tableSwitch 后面的注释显示 case 的哈希值的范围是 97 到 99。

我们讲解核心代码，先看偏移为 8 的指令，调用了参数的 `hashCode()` 函数来获取字符串 "t" 的哈希值。

```java
tableSwitch   { // 97 to 99
           97: 36
           98: 50
           99: 64
           default: 75
}
```

接下来我们看偏移为 11 的指令处： tableSwitch 是跳转引用列表， 如果值小于其中的最小值或者大于其中的最大值，跳转到 `default` 语句。

> 其中 97 为键，36 为对应的目标语句偏移量。

hashCode 和 tableSwitch 的键相等，则跳转到对应的目标偏移量，t 的哈希值为 116，大于条件的最大值 99，因此跳转到 `default` 对应的语句行（即偏移量为 75 的指令处执行）。

从 36 到 74 行，根据哈希值相等跳转到判断是否相等的指令。

然后调用 `java.lang.String#equals` 判断 switch 的字符串是否和对应的 case 的字符串相等。

如果相等则分别根据第几个条件得到条件的索引，然后每个索引对应下一个指定的代码行数。

default 语句对应 137 行，打印 “default” 字符串，然后执行 145 行 `return` 命令返回。

然后再通过 tableswitch 判断执行哪一行打印语句。

**因此整个流程是先计算字符串参数的哈希值，判断哈希值的范围，然后哈希值相等再判断对象是否相等，然后执行对应的代码块。**



##### 2.3.2 分析问题

经过前面的学习我们对 String 为参数的 switch 语句的执行流程有了初步认识。

我们反汇编开篇的示例，得到如下代码：

```java
Compiled from "SwitchTest.java"
public class com.imooc.basic.learn_switch.SwitchTest {
  public com.imooc.basic.learn_switch.SwitchTest();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: aconst_null
       1: astore_1
       2: aload_1
       3: astore_2
       4: iconst_m1
       5: istore_3
       6: aload_2
       7: invokevirtual #2                  // Method java/lang/String.hashCode:()I
      10: lookupswitch  { // 1
               3392903: 28
               default: 39
          }
      28: aload_2
      29: ldc           #3                  // String null
      31: invokevirtual #4                  // Method java/lang/String.equals:(Ljava/lang/Object;)Z
      34: ifeq          39
      37: iconst_0
      38: istore_3
      39: iload_3
      40: lookupswitch  { // 1
                     0: 60
               default: 71
          }
      60: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
      63: ldc           #3                  // String null
      65: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      68: goto          79
      71: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
      74: ldc           #7                  // String default
      76: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      79: return
}
```

**猜想和验证是学习的最佳方式之一**，我们通过猜想来提取知识，通过验证来核实自己的猜想是否正确。

**猜想 1**：根据上面的分析我们可以 “猜想”：3392903 应该是 "null" 字符串的哈希值。

我们可以打印其哈希值去印证：`System.out.println(("null").hashCode());` ，也可以通过编写单元测试来断言，还可以通过调试来执行表达式等方式查看。

在调试模式下，在变量选项卡上右键，选择 “Evaluate Expression…” ，填写想执行想计算的表达式即可：

![图片描述](https://img.mukewang.com/5dc2a76b0001c49814441042.png)

我们将上面的字节码的逻辑反向 “翻译” 成 java 代码大致如下：

```java
class a {
    public static void main(String[] args){
      String param = null;
      int hashCode = param.hashCode();
      if(hashCode == ("null").hashCode() && param.equals("null")){ 
         System.out.println("null");
      } else {
         System.out.println("default");
      }
    }
}
```

对应流程图如下：

![图片描述](https://img.mukewang.com/5dc28a9c0001ee3e10981172.png)

因此空指针的原因就一目了然了。

回忆一下空指针的小节讲到的：

> 空指针异常发生的原因之一：“调用 null 对象的实例方法。”。
>
> 以及 “JVM 也可能会通过 `Throwable#Throwable(String, Throwable, boolean, boolean)` 构造函数来构造 `NullPointerException` 对象。”

此处字节码执行时调用了 `null` 的 `hashCode` 方法，虚拟机可以通过上面的函数构造 NPE 并抛出。

**那么将字符串通过 hashCode 函数转为整型和 case 条件对比后，为什么还需要 equals 再次判断呢？**

这就要回到 hashCode 函数的本质，即将不同的对象（不定长）映射到整数范围（定长）, 而且 java 的 hashCode 函数和 equals 函数默认约定：同一个对象的 hashCode 一定相等， 即 hashCode 不等的对象一定不是同一个对象。

详情参见 `java.lang.Object#hashCode` 和 `java.lang.Object#equals` 的注释。

通过这一特性，可以快速判断对象是否有可能相当，避免不必要的比较。

**另外我们还可以猜想如何提高比较的效率？**

**猜想 2：** 如果编译期能够将 lookupswitch 按照 hash 值升序排序，则运行时就可讲参数的 hash 值（最小）先和第一个和除 default 外的倒数第一个 hash 值（最大）比较，不在这个范围直接走 default 语句即可，在这个范围就可以使用使用二分查找法，将时间复杂度降低到 O (logn) ，从而大大提高效率。

大家可以通过读 jvms 甚至读虚拟机代码去核实和验证上述猜想。

另外，**虽然有些哈希函数设计的比较优良，能够尽可能避免 hash 冲突，但是对象的数量是 “无限” 的，整数的范围是 “有限” 的，将无限的对象映射到有限的范围，必然会产生冲突。**

因此通过上述反汇编代码可以看出：

switch 表达式会先计算字符串的 hashCode （main 函数偏移为 7 处代码），然后根据 hashCode 是否相等快速判断是否要走到某个 case（见 lookupswith），如果不满足，直接执行到 default （main 函数偏移为 39 处代码）；如果满足，则跳转到对应 case 的代码（见 main 函数偏移为 28 之后的代码）再通过 equals 判断值是否相等，来避免 hash 冲突时 case 被误执行。

**这种先判断 hash 值是否相等（有可能是同一个对象 / 两个对象有可能相等）再通过 equals 比较 “对象是否相等” 的做法，在 Java 的很多 JDK 源码中和其他框架中非常常见**。



### 3. 总结

本节我们结合一个简单的案例 和 jvms， 学习了 switch 的基本原理，分析了示例代码产生空指针的原因。本节还介绍了一个简单的调试技巧，以及 “猜想和验证” 的学习方式，希望大家在后面的学习和工作中多加实践。

下一节我们将深入学习枚举并介绍其高级用法。



### 4. 课后题

下面的代码结果是啥呢？

```java
public class SwitchTest {
    public static void main(String[] args) {
        String param = null;
        switch (param="null") {
            case "null":
                System.out.println("null");
                break;
            default:
                System.out.println("default");
        }
    }
}
```

大家可以通过今天学习的知识，自己去实战分析这个问题。



### 参考资料

------

1. 阿里巴巴与 Java 社区开发者.《 Java 开发手册 1.5.0》华山版. 2019.18 [↩︎](https://www.imooc.com/read/55/article/1146#fnref1)
2. [悬赏征集！5 道题征集代码界前 3% 的超级王者](https://developer.aliyun.com/article/705658) [↩︎](https://www.imooc.com/read/55/article/1146#fnref2)
3. James Gosling, Bill Joy, Guy Steele, Gilad Bracha, Alex Buckley.《Java Language Specification: Java SE 8 Edition》. 2015 [↩︎](https://www.imooc.com/read/55/article/1146#fnref3)
4. Tim Lindholm, Frank Yellin, Gilad Bracha, Alex Buckley.《Java Language Specification: Java SE 8 Edition》. 2015 [↩︎](https://www.imooc.com/read/55/article/1146#fnref4)

[](https://www.imooc.com/read/55/article/1145)