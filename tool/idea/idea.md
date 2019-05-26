# Idea快速入门指南

## 1.安装

### 1.1.安装

我们使用的是最新的2017.3.4版本：





双击打开，

 ![1525750779463](assets/1525750779463.png)

选择一个目录，最好不要中文和空格：

 ![1525750813679](assets/1525750813679.png)

然后选择桌面快捷方式，请选择64位：

 ![1525750912229](assets/1525750912229.png)

然后选择安装：

 ![1525750951163](assets/1525750951163.png)

开始安装：

 ![1525750998299](assets/1525750998299.png)

然后勾选安装后运行，Finish：

 ![1525751087357](assets/1525751087357.png)



### 1.2.首次配置

 ![1525751118160](assets/1525751118160.png)

然后是UI界面选择，有白色和黑色两款，总有一款适合你：

![1525751289292](assets/1525751289292.png)

把不需要的组件禁用：

![1525751380547](assets/1525751380547.png)

插件暂时不选择安装，以后有需求还可以来安装：

![1525751420472](assets/1525751420472.png)

然后进入运行界面：

![1525751450753](assets/1525751450753.png)

激活Idea：

 ![1525751624121](assets/1525751624121.png)

这里有三种激活方式：

- 第一种:购买正版用户(如果有资金最好选择正版)
- 第二种:激活码(这种方法在下面有讲解)
- 第三种:在线激活(有一个过期时间，这个时间一过就必须再次联网授权服务器请求激活)

土豪请选择第一种，每年大概不到$700

非土豪，请参考：http://idea.lanyus.com/   中的教程。



激活完成，就可以开始撸代码了：

![1525751864226](assets/1525751864226.png)

## 2.配置

我们在启动界面打开配置页面：

 ![1525769074210](assets/1525769074210.png)

进入idea以后，我们可以进行一系列配置。

### 2.1.字体和主题：

![1525751947796](assets/1525751947796.png)

另外，主题也可以到网上下载，但是建议大家不要去浪费时间了。

### 2.2.启动项：

![1525752058361](assets/1525752058361.png)



### 2.3.快捷键

类名自动补全：

默认并不是Alt + /。而大家玩eclipse比较熟悉了，所以我们改成Alt + /

![1525752736788](assets/1525752736788.png)

代码生成：

默认的代码生成快捷键：`Alt + insert`。很多同学电脑中没有 Insert 按键。

因此这里需要修改，大家自己选择。我设置的是`Alt + I`

![1525752858905](assets/1525752858905.png)



还有快捷弹出 New菜单：

默认是`Alt+Insert`，没有`Insert`按键的同学，可以修改。我设置的也是`Alt+ I`

![1525771386518](assets/1525771386518.png)



### 2.4.代码联想

![1525753057335](assets/1525753057335.png)



### 2.5.编辑器字体：

![1525753256845](assets/1525753256845.png)



### 2.6.编码

![1525753383997](assets/1525753383997.png)

### 2.7.maven

idea自带的maven版本是3.3.9，我们一般不需要指定自己的。不过我们可以指定settings.xml来修改自己的仓库地址。

![525768925360](assets/1525768925360.png)



### 2.8.ES6语法支持

![1525769944733](assets/1525769944733.png)

### 2.9.Vue插件安装

![1525769992634](assets/1525769992634.png)



## 3.常用快捷键

|        快捷键        |                          作用                           |
| :------------------: | :-----------------------------------------------------: |
|       Ctrl + Y       |                        删除一行                         |
|       Ctrl + D       |                        复制一行                         |
|    Ctrl + Alt + L    |                         格式化                          |
|    Ctrl + Alt + O    |                          导包                           |
| Alt+Insert（可修改） | New菜单\代码生成菜单（生成getter和setter，maven依赖等） |
|       Ctrl + /       |                          注释                           |
|   Ctrl + Shift + /   |                        多行注释                         |
|  Ctrl + Alt + 左/右  |    回退到上一次操作的地方，等于eclipse中的 Alt+左/右    |
| Shift + Alt + 上/下  |                  将代码上移或下移一行                   |

Ctry + H ：罗列类的继承关系





## 4.代码补全

idea有很多的代码自动补全功能，有两个地方可以设置：

![1525772543703](assets/1525772543703.png)

还有一个：

![1525772590599](assets/1525772590599.png)

其作用演示：

 ![](assets/idea演示.gif)



通过后缀的方式快速完成一些代码的补全，一般写完后缀，按tab或回车即可。罗列一些比较常用的：

| 代码 |            效果            |
| :--: | :------------------------: |
| psvm |      自动生成main函数      |
| .var |     自动为对象生成声明     |
| sout | 输出：System.out.println() |
| .if  |         生成if判断         |
| .for |  生成循环，默认是高级for   |
| fori |     用普通for进行遍历      |
| .try |     生成try ... catch      |
|      |                            |
|      |                            |

## 5.project与module

### 5.1.idea的maven理念

在Idea中，没有工作空间的概念，每一个Project就是一个独立的文件夹，也是一个独立的窗口。然后我们可以在Project中创建多个Module。

是不是感觉与maven的项目结构完全一致？

说对了，idea就是完全贯彻了maven的理念。

### 5.2.小技巧

熟悉eclipse的同学会觉得很不方便，无法在一个界面中创建很多的工程。

不过有一个取巧的办法：我们可以创建一个empty的工程：

 ![1525773374863](assets/1525773374863.png)

然后选择empty工程：

![1525773425288](assets/1525773425288.png)

然后填写名称：

![1525773537608](assets/1525773537608.png)

点击Finish：

 ![1525773713424](assets/1525773713424.png)

但是接下来，就不要再新建Project了，而是新建Module，Module就类似原来的工程的概念：

 ![1525787088822](assets/1525787088822.png)

然后创建一个maven工程：

![1525787217607](assets/1525787217607.png)



然后填写项目信息：

 ![1525787282673](assets/1525787282673.png)



填写项目位置信息：

 ![1525787370657](assets/1525787370657.png)



界面结构：

![1525787636639](assets/1525787636639.png)





# 6.打开springboot的run dashboard

先看下run dashboard是什么：

![img](assets/1314186-20180413093018664-88251637.png) 

可以看到，这里可以同时显示多个springboot项目，非常方便。



默认情况下，idea的run dashboard是关闭的，当检测到你有多个springboot项目时会弹出提示框，询问是否打开。



如果我们想要自己打开，需要修改配置。

在你的idea的项目目录中，有一个.idea目录：

 ![1526786924641](assets/1526786924641.png)

其中，有一个workspace.xml：

 ![1526786976776](assets/1526786976776.png)



打开，搜索Rundashboard，找到下面这段：

![img](assets/1314186-20180413093746177-931677889.png) 

[IDEA中设置Run Dashboard](https://blog.csdn.net/chinoukin/article/details/80577890)
然后在Component中添加下面的内容：

```xml
<component name="RunDashboard">
    <option name="configurationTypes">
      <set>
        <option value="SpringBootApplicationConfigurationType" />
      </set>
    </option>
    <option name="ruleStates">
      <list>
        <RuleState>
          <option name="name" value="ConfigurationTypeDashboardGroupingRule" />
        </RuleState>
        <RuleState>
          <option name="name" value="StatusDashboardGroupingRule" />
        </RuleState>
      </list>
    </option>
</component>
```

# 7.常用插件
`sonarlint`、`mybatis-helper`、`grep console`、

`alibaba-java-coding-guidelines(阿里巴巴规范)`、`FindBugs-IDEA`

# 8.激活
 使用lanyus大神的注册码（推荐）
 
[IntelliJ IDEA最新2019注册码/可激活至2020年/激活/破解](https://blog.csdn.net/wdy_2099/article/details/89164387)

## 1. 打开路径C:\Windows\System32\drivers\etc，
    修改host文件，在末尾追加域名
```
	0.0.0.0 account.jetbrains.com
	0.0.0.0 www.jetbrains.com
```

## 2. 访问lanyus地址：http://idea.lanyus.com/
    点击获得注册码


