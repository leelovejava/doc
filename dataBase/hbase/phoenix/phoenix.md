##Phoenix-HBase中间层

## doc
-[Apache Phoenix 官方站点](https://phoenix.apache.org/)
-[Phoenix支持的sql语句](https://phoenix.apache.org/language/index.html)
-[Phoenix 支持的DataTypes](https://phoenix.apache.org/language/datatypes.html)
-[Phoenix 支持的函数](https://phoenix.apache.org/language/functions.html)
-[Phoenix综述（史上最全Phoenix中文文档）](https://www.cnblogs.com/linbingdong/p/5832112.html)
-[使用Phoenix连接HBASE，squirrel使用，代码连接使用Phoenix](https://blog.csdn.net/tototuzuoquan/article/details/81506285)

#### 简介
* 构建于Apache Hbase之上的SQL中间层
* 可以在Apache HBase上执行SQL查询,性能强劲
* 较完善的查询支持,支持二级索引,查询效率较高

![avatar](http://phoenix.apache.org/images/using/all.png)

#### 优势
* Put the SQL back in NoSQL
* 具有完整的ACID事务功能的标准SQL和JDBC API的强大功能
* 完全可以和其他Hadoop产品,例如Spark、Hive、Pig、Flume以及MapReduce集成

####Phoenix vs Hive
HBase的查询工具，如：Hive、Tez、Impala、Spark SQL、Phoenix

![image](http://phoenix.apache.org/images/PhoenixVsHive.png）
#### Phoenix比HBase快的原因
* 通过HBase协处理器,在服务端进行操作,从而最大限度的减少客户端和服务端的数据传输
* 通过定制的过滤器对数据的处理
* 使用本地的HBase Api而不是通过MapReduce框架,从而最大限度的降低启动成本

#### 功能特性
* 多租户
* 二级索引
* 用户定义函数
* 行时间戳列
* 分页查询
* 视图

#### 安装&部署
1、下载&解压
http://phoenix.apache.org/download.html
[镜像站点](http://www.apache.org/dyn/closer.lua/phoenix/)
http://archive.apache.org/dist/phoenix/
和hbase对应版本
wget http://mirrors.hust.edu.cn/apache/phoenix/apache-phoenix-5.0.0-HBase-2.0/bin/apache-phoenix-5.0.0-HBase-2.0-bin.tar.gz
2、复制phoenix-core-5.0.0-HBase-2.0.jar/phoenix-5.0.0-HBase-2.0-server.jar到hbase regionServer的lib
3、增加hbase-site.xml 配置
```
<property>
    <name>hbase.table.sanity.checks</name>
    <value>false</value>
</property>
```

编译源码安装
1) 安装git maven
2) 下载CDH版的Phoenix
https://github.com/chiastic-security/phoenix-for-cloudera/tree/4.8-HBase-1.2-cdh5.8
3)  编译
mvn clean package -DskipTests

https://www.cnblogs.com/zlslch/p/7096402.html
#### shell命令
##### 进入
bin/sqlline.py
bin/sqlline.py hadoop000
bin/sqlline.py 127.0.0.1:2181

##### 导入数据

bin/psql.py hadoop001:2181 examples/WEB_STAT.sql examples/WEB_STAT.csv examples/WEB_STAT_QUERIES.sql

##### 常用命令
创建表
> create table if not exists PERSON(ID INTEGER NOT NULL PRIMARY KEY,NAME VARCHAR(20),AGE INTEGER);

查看所有表
> !tables

查询
> select * from PERSON;                   

插入数据    
    
> upsert into PERSON(ID,NAME,AGE) values(1,'Bella',27); 
> upsert into PERSON(ID,NAME,AGE) values(2,'Anne',18);
> upsert into PERSON(ID,NAME,AGE) values(3,'Colin',25);
> upsert into PERSON(ID,NAME,AGE) values(4,'Jerry',30);

查看表信息
> !describe tables_name

修改表结构  
> alter table PERSON add sex varchar(10);         

删除表
> DROP TABLE tables_name

修改数据
> update PERSON set sex='男' where ID=1;              
  
删除表结构
> drop table "person";  

创建表某一列索引
> create index "person_index" on "person"("cf"."name");   
模糊查找
> select count(*) from table_name where TIMESTAMP  like '2016-07-03%';

删除索引
> drop index "person_index" on "person"                   

删除表中数据
> delete from "person" where name='zhangsan';

修改表中数据              

> upsert into "person"(id,sex) values(1, '女');            

> case when
    select (case name when 'zhangsan' then 'sansan' when 'lisi' then 'sisi' else name end)as showname from "person";

退出
> !quit                 

关联hbase中已经存在的表( phoenix表映射)),默认情况下，直接在hbase中创建的表，通过phoenix是查看不到的,视图映射、表映射
    create view "test"(id varchar not null primary key, "cf1"."name" varchar, "cf1"."age" varchar, "cf1"."sex" varchar);
    注意：
    （1）如果不加列族会报错如下：
    Error: ERROR 505 (42000): Table is read only. (state=42000,code=505)
    （2）如果不加双引号则会匹配不到hbase表中的字段，结果就是虽然关联上数据库但是没有值！！！
    （3）关联的时候，Phoenix建表最好都是varchar类型，不容易出错
    （4）最好创建view视图，不要创建table表格。因为Phoenix端删除table会连带删除hbase表格，如果是view则不会。  
    
    1） 当HBase中已经存在表时，可以以类似创建视图的方式创建关联表，只需要将create view改为create table即可。
    2）当HBase中不存在表时，可以直接使用create table指令创建需要的表，并且在创建指令中可以根据需要对HBase表结构进行显示的说明。
    第1）种情况下，如在之前的基础上已经存在了test表，则表映射的语句如下：
    create table "test"(empid varchar primarykey,"name"."firstname"varchar,"name"."lastname"varchar,"company"."name" varchar,"company"."address"varchar);
    第2）种情况下，直接使用与第1）种情况一样的create table语句进行创建即可，这样系统将会自动在Phoenix和HBase中创建person_infomation的表，并会根据指令内的参数对表结构进行初始化。
    使用create table创建的关联表，如果对表进行了修改，源数据也会改变，同时如果关联表被删除，源表也会被删除。但是视图就不会，如果删除视图，源数据不会发生改变。

Phoenix中不存在update的语法关键字，而是upsert ，功能上替代了Insert+update

官网描述
> UPSERT VALUES
  Inserts if not present and updates otherwise the value in the table. The list of columns is optional and if not present, the values will map to the column in the order they are declared in the schema. The values must evaluate to constants. 

####java api操作
java调用Hbase java.net.SocketTimeoutException
 将java连接hbase的IP地址添加到windows下C:\Windows\System32\drivers\etc\hosts文件中   
 
### squirrel
#### 简介
 windows的Phoenix可视化工具,可以通过一个统一的用户界面来操作MySQL 、PostgreSQL 、MSSQL、 Oracle等任何支持JDBC访问的数据库
#### 使用 
[下载地址](http://www.squirrelsql.org/)
https://www.jianshu.com/p/9d3e938081d2
https://phoenix.apache.org/installation.html

#### SQuirrel的安装步骤（参考）：

1）移除SQuirrel的lib文件夹下的phoenix-[oldversion]-client.jar(如果有的话），然后拷贝phoenix-[newversion]-client.jar到SQuirrel的lib文件夹下，phoenix-[newversion]-client.jar须与欲连接的hbase的lib下的phoenix版本一致。

java -jar squirrel-sql-3.8.1-standard.jar
![image](https://img-blog.csdn.net/20180808123410192?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3RvdG8xMjk3NDg4NTA0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![image](https://img-blog.csdn.net/20180808123430920?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3RvdG8xMjk3NDg4NTA0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![image](https://img-blog.csdn.net/20180808123451315?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3RvdG8xMjk3NDg4NTA0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![image](https://img-blog.csdn.net/20180808123509420?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3RvdG8xMjk3NDg4NTA0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


2）windows下，运行squirrel-sql.bat启动SQuirrel，在启动界面下，切换到Drivers选项卡，点击＋号添加新的驱动。

![image](https://img-blog.csdn.net/20170727102737524?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemxqX2Jsb2c=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

![image](https://img-blog.csdn.net/20170727102755088?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemxqX2Jsb2c=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

3）在添加驱动对话框中，设置name为Phoenix,设置Example URL为 jdbc:phoenix:localhost，其中的localhost为hbase使用的Zookeeper主机名。

4）设置Class Name文本框的内容为 “org.apache.phoenix.jdbc.PhoenixDriver”， 如图4.1，然后点击“OK”关闭。


5）切换到Aliases选项卡，点击+新建一个alias。
![image](https://img-blog.csdn.net/20170727102818723?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemxqX2Jsb2c=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

6）在对话框中，name：任何名称，Driver:选择phoenix，username、password可省略，或者填任意值均可。

7）URL的内容为：jdbc:phoenix: zookeeperquorum server，例如，要连接本机的hbase，URL为：jdbc:phoenix:localhost，如图4.2。

8）点击Test,在新对话框中选择connect,如果一切设置正确的话，应该连接成功，然后点击OK关闭对话框。

9）双击新建的phoenix alias,点击connect，然后就可以通过phoenix的sql语句操作hbase了
