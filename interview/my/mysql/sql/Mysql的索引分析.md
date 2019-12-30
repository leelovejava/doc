# MySQL索引分析

## 案例

```sql
CREATE TABLE `employees` (  
    `id` int(11) NOT NULL AUTO_INCREMENT,  
    `name` varchar(24) NOT NULL DEFAULT '' COMMENT '姓名',  
    `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',  
    `position` varchar(20) NOT NULL DEFAULT '' COMMENT '职位',  
    `hire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入职时间',  
    PRIMARY KEY (`id`),  
    KEY `idx_name_age_position` (`name`,`age`,`position`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='员工记录表';

INSERT INTO employees(name,age,position,hire_time) VALUES('LiLei',22,'manager',NOW());
INSERT INTO employees(name,age,position,hire_time) VALUES('HanMeimei', 23,'dev',NOW());
INSERT INTO employees(name,age,position,hire_time) VALUES('Lucy',23,'dev',NOW());
```

分析以下几条sql的索引使用情况

```sql
SELECT * FROM employees WHERE name= 'LiLei';
SELECT * FROM employees WHERE name= 'LiLei' AND age = 22 AND position ='manager';
SELECT * FROM employees WHERE age = 22 AND position = 'manager';
SELECT * FROM employees WHERE name= 'LiLei' AND age > 22 AND position = 'manager';
SELECT * FROM employees WHERE name != 'LiLei';
```



# Mysql的索引分析

MySQL官方对索引的定义为：索引（Index）是帮助MySQL高效获取数据的数据结构。

索引的本质：索引是数据结构，而且是实现了高级查找算法的数据结构, 索引一般以文件形式存储在磁盘上，索引检索需要磁盘I/O操作

## 磁盘存取原理

- 寻道时间(速度慢，费时)
- 旋转时间(速度较快) 预读：长度为页的整倍数（ 主存和磁盘以页为单位交换数据，一页4K）

## 索引的结构

- 二叉树
- 红黑树
- HASH
- BTREE

数据结构教学网站

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9eRocLNJ4xlXibGCFCH1RP08IFRicfianib7ibGWkwbnhYiahwN4EwQ5l2Bgw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

## 索引底层数据结构与算法

### Hash索引

如果是等值查询，哈希索引明显有绝对优势， 前提：键值唯一

哈希索引没办法完成范围查询检索 哈希索引也没办法利用索引完成排序，以及like ‘xxx%’ 这样的部分模糊查询 哈希索引也不支持多列联合索引的 在有大量重复键值情况下，哈希索引的效率也最左前缀原则是极低的，因为存在哈希碰撞问题

### B-Tree

- 度(Degree)-节点的数据存储个数
- 叶子节点具有相同的深度
- 叶子节点的指针为空
- 节点中的数据key从左到右递增排列

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9QhuNOykbCwBossDxfx6QCrh1BDpTRkz8KKIJpUosbA4Dy7FlX0BImA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### B+Tree

- 非叶子节点不存储data，只存储key，可以增大度
- 叶子节点不存储指针
- 顺序访问指针，提高区间访问的性能

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9Xtgb6RB0PYPdjqHwnylv2rtNN9XOwQxhLL4j0lpqAicGVXSRibkYaRXQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### MyISAM索引实现(非聚集)

MyISAM索引文件和数据文件是分离的

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W921Wau5ahrzJ4jaHv6lM7OILOc0uTJpibuzuicmLfubUXLZNAIZ2vt0tQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9zk5nbZiap0Au8cs1ymjzyvDyFgicRPbeAMhux3TuM4IyqicicL00Ok2Lpw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### InnoDB索引实现(聚集)

- 数据文件本身就是索引文件
- 表数据文件本身就是按B+Tree组成的一个索引结构文件
- 聚集索引-叶节点包含了完整的数据记录
- 为什么InnoDB表要求有主键，并且推荐使用整型的自增主键？

没有主键：https://dev.mysql.com/doc/refman/5.6/en/innodb-index-types.html

- 为什么非主键索引结构叶子节点存储的是主键值？(一致性和节省存储空间)

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W97318WdpAF761GExNNJt4HhVUj5d4DFSMD7bdRY2CDx3r4Ut2ZV0wpg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9icQbor2QsSgn9YJ1H7BtPVOJnvPXuYqrs5ETtU0uNXuT53K69P31Wag/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### 联合索引数据结构

联合索引的底层存储结构长什么样？



------

# EXPLAIN详解与索引最佳实践

------

# EXPLAIN执行计划

使用EXPLAIN关键字可以模拟优化器执行SQL语句，从而知道MySQL是 如何处理你的SQL语句的，分析你的查询语句或者表结构的性能瓶颈。

语法 ：Explain + SQL语句

在 select 语句之前增加 explain 关键字，MySQL 会在查询上设置一个标记，执行查询时，会返回执行计划的信息，而不是执行这条SQL（如果 from 中包含子查询，仍会执行该子查询，将结果放入临时表中）

## 执行计划作用

- 表的读取顺序
- 数据读取操作的操作类型
- 哪些索引可以使用
- 哪些索引被实际使用
- 表之间的引用
- 每张表有多少行被优化器查询

## explain 案例

```sql
DROP TABLE IF EXISTS `actor`;
CREATE TABLE `actor` (  
    `id` int(11) NOT NULL,  
    `name` varchar(45) DEFAULT NULL,  
    `update_time` datetime DEFAULT NULL,  
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8; 
INSERT INTO `actor` (`id`, `name`, `update_time`) VALUES (1,'a','2017-12-22 15:27:18'), (2,'b','2017-12-22 15:27:18'), (3,'c','2017-12-22 15:27:18');

DROP TABLE IF EXISTS `film`;
CREATE TABLE `film` (  
    `id` int(11) NOT NULL AUTO_INCREMENT,  
    `name` varchar(10) DEFAULT NULL,  
    PRIMARY KEY (`id`), 
    KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `film` (`id`, `name`) VALUES (3,'film0'),(1,'film1'),(2,'film2');

DROP TABLE IF EXISTS `film_actor`;
CREATE TABLE `film_actor` (  
    `id` int(11) NOT NULL,  
    `film_id` int(11) NOT NULL,  
    `actor_id` int(11) NOT NULL,  
    `remark` varchar(255) DEFAULT NULL,  
 	PRIMARY KEY (`id`),  
    KEY `idx_film_actor_id` (`film_id`,`actor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `film_actor` (`id`, `film_id`, `actor_id`) VALUES (1,1,1),(2,1,2),(3,2,1); 

explain select * from actor; 
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9nyyGPHQFichkpWmwsWWAa3UMLIiapiakWZReHMbibJJy613MZgmblzPHVA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

在查询中的每个表会输出一行，如果有两个表通过 join 连接查询，那么会输出两行。表的意义相当广泛：可以是子查询、一个 union 结果等。

## explain 两个变种

- **explain extended**

会在 explain  的基础上额外提供一些查询优化的信息。紧随其后通过 show warnings 命令可以 得到优化后的查询语句，从而看出优化器优化了什么。额外还有 filtered 列，是一个半分比的值，rows * filtered/100 可以估算出将要和 explain 中前一个表进行连接的行数（前一个表指 explain 中的id值比当前表id值小的表）。

```
explain extended select * from film where id = 1;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9KtqJpVC5iaZsXugrNWDupNs2m2gvka3kKxIgcvNRTNia6h6FxjIl78ew/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```
show warnings;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9DXqic3bycImVRogwMCT149uAqRtsl7cEaj8UF6NFVg8m6J4hHDu4new/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

- **explain partitions**

相比 explain 多了个 partitions 字段，如果查询是基于分区表的话，会显示查询将访问的分区。

## explain 中的列

接下来我们将展示 explain 中每个列的信息。

### **1. id**

id列的编号是 select 的序列号，有几个 select 就有几个id，并且id的顺序是按 select 出现的顺序增长的。MySQL将 select 查询分为简单查询(SIMPLE)和复杂查询(PRIMARY)。复杂查询分为三类：简单子查询、派生表（from语句中的子查询）、union 查询。id列越大执行优先级越高，id相同则从上往下执行，id为NULL最后执行

1）简单子查询

```
explain select (select 1 from actor limit 1) from film;
```



2）from子句中的子查询

```
explain select id from (select id from film) as der;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9ZS49dp6JIjbIWKo8tZib3DC1x0lmWgPllRunjPVCpOrEEjecsbOzjzA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)这个查询执行时有个临时表别名为der，外部 select 查询引用了这个临时表

3）union查询

```
explain select 1 union all select 1;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W923fvZb7qSyzPIujQSUhwCRB5CTRNeB5zmcKFibqBdRN3cvoE3HfUCYA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)union结果总是放在一个匿名临时表中，临时表不在SQL中出现，因此它的id是NULL。

### **2. select_type列**

select_type 表示对应行是简单还是复杂的查询，如果是复杂的查询，又是上述三种复杂查询中的哪一种。1）**simple**：简单查询。查询不包含子查询和union

```sql
explain select * from film where id = 2;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9A7SDodxebOmVibianl6QWtddKtrKxgAmAHzfGmeVbRZbq6kBjibgicnnow/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

2）**primary**：复杂查询中最外层的 select 3）**subquery**：包含在 select 中的子查询（不在 from 子句中） 4）**derived**：包含在 from 子句中的子查询。MySQL会将结果存放在一个临时表中，也称为派生表（derived的英文含义） 用这个例子来了解 primary、subquery 和 derived 类型

```sql
explain select (select 1 from actor where id = 1) from (select * from film where id = 1) der;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9xCuYibMHEpXLD8jUK3TkqX0HAspozKJI2iabjWKiaib9nEaWrjKFzatX2A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

5）**union**：在 union 中的第二个和随后的 select 6）**union result**：从 union 临时表检索结果的 select 用这个例子来了解 union 和 union result 类型：

```sql
explain select 1 union all select 1;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9UmlxRxZtjzABkQ2qOvsYJOREzmoJlJLC4OKNerNyf8OdzvJq02NdUw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **3. table列**

这一列表示 explain 的一行正在访问哪个表。当 from 子句中有子查询时，table列是 格式，表示当前查询依赖 id=N 的查询，于是先执行 id=N 的查询。当有 union 时，UNION RESULT 的 table 列的值为，1和2表示参与 union 的 select 行id。

### **4. type列**

这一列表示关联类型或访问类型，即MySQL决定如何查找表中的行，查找数据行记录的大概范围。

```
完整的结果值从最优到最差分别为：system>const>eq_ref>ref>fulltext>ref_or_null>index_merge>unique_subquery>index_subquery>range>index>ALL
```

需要记忆的：system > const > eq_ref > ref > range > index > ALL 一般来说，得保证查询达到range级别，最好达到ref**NULL**：mysql能够在优化阶段分解查询语句，在执行阶段用不着再访问表或索引。例如：在索引列中选取最小值，可以单独查找索引来完成，不需要在执行时访问表

```sql
explain select min(id) from film;  
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9o593jzNPbZt7bic4avnm1JZawHnxbg5yIKnUHvZBsFaqkfat2icjJ83A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**const, system**：mysql能对查询的某部分进行优化并将其转化成一个常量（可以看show warnings 的结果）。用于 primary key 或 unique key 的所有列与常数比较时，所以**表最多有一个匹配行**，读取1次，速度比较快。**system是const**的特例，表里只有一条元组匹配时为**system**

```
explain extended select * from (select * from film where id = 1) tmp;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9WA9QpEpCibSc5hu9hGVRUjQpCvjDGazdwictNeyeiaB5endXL055wVfpA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```
show warnings; 
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9DLbSiaF5unEu1mibbIv6RGEZSXKNS6ebNH9iauNTzX1PD5r7PkyNj9Yag/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**eq_ref**：primary key 或 unique key 索引的所有部分被连接使用 ，最多只会返回一条符合条件的记录。这可能是在 const 之外最好的联接类型了，简单的 select 查询不会出现这种 type。

```sql
explain select * from film_actor left join film on film_actor.film_id = film.id; 
```



**ref**：相比 eq_ref，不使用唯一索引，而是使用普通索引或者唯一性索引的部分前缀，索引要和某个值相比较，可能会找到多个符合条件的行。

1. 简单 select 查询，name是普通索引（非唯一索引）

```sql
explain select * from film where name = "film1"; 
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9vjh10zEWbCwuDJLx3QicrRnSme9dA3Wd3Z9mXFfUMDKGBPmFxSEGzIA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**2**.关联表查询，idxfilmactorid是filmid和actorid的联合索引，这里使用到了filmactor的左边前缀film_id部分。

```
explain select film_id from film left join film_actor on film.id = film_actor.film_id;
```



**range**：范围扫描通常出现在 **in(), between ,> ,<, >=** 等操作中。使用一个索引来检索给定范围的行。

```
explain select * from actor where id > 1;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9j0bAEjkUvpjfwyrAlV92GIbo2ZmPAjR8J2mkDWicYfA84Ou2g5dZQUw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**index**：扫描全表索引，这通常比ALL快一些。

```sql
explain select * from film; 
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W90gr6dqpoJsPg85ibsDBPpgwvFNdVcP6k4VriagmEGenDWBxL5zLMCWBg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**ALL**：即全表扫描，意味着mysql需要从头到尾去查找所需要的行。通常情况下这需要增加索引来进行优化了

```sql
explain select * from actor;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W98Aw7dpBCDahicxNzMIFY07ibkfDcDGSzibwQVoGUuNbQ4bg95DRmyLOcA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **5. possible_keys列**

这一列显示查询可能使用哪些索引来查找。 explain 时可能出现 possible_keys 有列，而 key 显示 NULL 的情况，这种情况是因为表中数据不多，mysql认为索引对此查询帮助不大，选择了全表查询。 如果该列是NULL，则没有相关的索引。在这种情况下，可以通过检查 where 子句看是否可以创造一个适当的索引来提高查询性能，然后用 explain 查看效果。

### **6. key列**

这一列显示mysql实际采用哪个索引来优化对该表的访问。如果没有使用索引，则该列是 NULL。如果想强制mysql使用或忽视possible_keys列中的索引，在查询中使用 force index、ignore index。

```sql
explain select  * from film  ignore index(idx_name); 
```



### **7. key_len列**

这一列显示了mysql在索引里使用的字节数，通过这个值可以算出具体使用了索引中的哪些列。 举例来说，filmactor的联合索引 idxfilmactorid 由 filmid 和 actorid 两个int列组成，并且每个int是4字节。通过结果中的keylen=4可推断出查询使用了第一个列：filmid列来执行索引查找。

```sql
mysql> explain select * from film_actor where film_id = 2;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9bckIh03lwOBUJMfaibjsm5gOeMI7WAZsOAcdHMXqHFmJw30AWEF3kiag/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

key_len计算规则如下：

- 字符串
- char(n)：n字节长度
- varchar(n)：2字节存储字符串长度，如果是utf-8，则长度 3n + 2
- 数值类型
- tinyint：1字节
- smallint：2字节
- int：4字节
- bigint：8字节
- 时间类型　
- date：3字节
- timestamp：4字节
- datetime：8字节
- 如果字段允许为 NULL，需要1字节记录是否为 NULL

索引最大长度是768字节，当字符串过长时，mysql会做一个类似左前缀索引的处理，将前半部分的字符提取出来做索引。

### **8. ref列**

这一列显示了在key列记录的索引中，表查找值所用到的列或常量，常见的有：const（常量），字段名（例：film.id）

### **9. rows列**

这一列是mysql估计要读取并检测的行数，注意这个不是结果集里的行数。

### **10. Extra列**

这一列展示的是额外信息。常见的重要值如下： **Using index**：查询的列被索引覆盖，并且where筛选条件是索引的前导列，是性能高的表现。一般是使用了**覆盖索引**(索引包含了所有查询的字段)。对于innodb来说，如果是辅助索引性能会有不少提高

```sql
explain select film_id from film_actor where film_id = 1;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W93U9SiaWxbNzxxYUJ31GR3wdU2nco3icgvoC643Iv2QqCpu2KgUEr98Pg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**Using where**：查询的列未被索引覆盖，where筛选条件非索引的前导列

```sql
explain select * from actor where name = 'a';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9n7rokaVTmJFTXX5bDFYHanEc0VhpSwiayuuHW9tibl2aFo8KnNKg3jfQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**Using where Using index**：查询的列被索引覆盖，并且where筛选条件是索引列之一但是不是索引的前导列，意味着无法直接通过索引查找来查询到符合条件的数据

```
explain select film_id from film_actor where actor_id = 1;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9YAy3zvah6U2YNhgSQg7xyRiaQjkT1QZ3XNpVZMF4rcsy0p9HRt626aQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**NULL**：查询的列未被索引覆盖，并且where筛选条件是索引的前导列，意味着用到了索引，但是部分字段未被索引覆盖，必须通过“回表”来实现，不是纯粹地用到了索引，也不是完全没用到索引

```sql
explain select * from film_actor where film_id = 1;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W95ZsoeicEwX7O6SFPlQXJSLBuspJ95xHKvh9oPps1mTCulMZkiaM6dLlw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**Using index condition**：与Using where类似，查询的列不完全被索引覆盖，where条件中是一个前导列的范围；

```sql
explain select * from film_actor where film_id > 1;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9wRn67KAXSJ6WWkiazto9VRqyA4kPfGzATvO5p1ibDpMwhhR6f6LukbBA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**Using temporary**：mysql需要创建一张临时表来处理查询。出现这种情况一般是要进行优化的，首先是想到用索引来优化。**1**. actor.name没有索引，此时创建了张临时表来distinct

```sql
explain select distinct name from actor;  
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9qLmQsicUMrKoTvaMvM10QSaOFpJ8sJkXjMDuJian52pkL1kuicatH2PTA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

1. film.name建立了idx_name索引，此时查询时extra是using index,没有用临时表

```sql
explain select distinct name from film; 
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9a1lYtV204Y9lhqsR9ljRNTsxzu4IhSAicqtJezDicBUuXicDfBickoBa7w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**Using filesort**：mysql 会对结果使用一个外部索引排序，而不是按索引次序从表里读取行。此时mysql会根据联接类型浏览所有符合条件的记录，并保存排序关键字和行指针，然后排序关键字并按顺序检索行信息。这种情况下一般也是要考虑使用索引来优化的。**1**. actor.name未创建索引，会浏览actor整个表，保存排序关键字name和对应的id，然后排序name并检索行记录

```sql
explain select * from actor order by name;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W93eDWRqfPz7ohvR0rezuHjEiaAK1FrmP7oATUFiceDZibINhpfs7Loj9Ew/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**2**. film.name建立了idx_name索引,此时查询时extra是using index

```
explain select * from film order by name; 
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9L0zwhghQDr4z6AaAs2m4whIhPhPDvaiarOFzbXghVDctxcaua9UFG1A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

# **索引最佳实践**

## 使用的表

```sql
CREATE TABLE `employees` (  
    `id` int(11) NOT NULL AUTO_INCREMENT,  
    `name` varchar(24) NOT NULL DEFAULT '' COMMENT '姓名',  
    `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',  
    `position` varchar(20) NOT NULL DEFAULT '' COMMENT '职位',  
    `hire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入职时间',  
    PRIMARY KEY (`id`),  
    KEY `idx_name_age_position` (`name`,`age`,`position`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='员工记录表';

INSERT INTO employees(name,age,position,hire_time) VALUES('LiLei',22,'manager',NOW());
INSERT INTO employees(name,age,position,hire_time) VALUES('HanMeimei', 23,'dev',NOW());
INSERT INTO employees(name,age,position,hire_time) VALUES('Lucy',23,'dev',NOW());
```

## 最佳实践

### **1. 全值匹配**

```sql
EXPLAIN SELECT * FROM employees WHERE name= 'LiLei';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9BkmC9pwC60xOhr0h53bQkFYqyCZccREjItuCrlIZygia23ZNK0dtBUA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```
EXPLAIN SELECT * FROM employees WHERE name= 'LiLei' AND age = 22;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9f2KmtxhGf7ic35kuFDgxEr0ibdeTd4ibnibMrraBJVy019JHpibxl8qJqdQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```sql
EXPLAIN SELECT * FROM employees WHERE name= 'LiLei' AND age = 22 AND position ='manager';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9bS65zYXlmczKMJtJknxgCaTFOYMeqoYl979PibTxOx7Gib2L8QO9clUg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **2.最左前缀法则**

 如果索引了多列，要遵守最左前缀法则。指的是查询从索引的最左前列开始并且不跳过索引中的列。

```sql
EXPLAIN SELECT * FROM employees WHERE age = 22 AND position ='manager';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9QHsibps0wvMmD0cInXs4Z7kHdia5YugBOQumKuibkKicmjcf3DQpErO8Ow/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```sql
EXPLAIN SELECT * FROM employees WHERE position = 'manager';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W905Ec3VQkYLQNleNWibo5E4icyJgB1z56iaKpOcZOOiciaMp12VrzKeQH69A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```sql
EXPLAIN SELECT * FROM employees WHERE name = 'LiLei';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W94MBxicBJQP0202B5gh9un9u4WLIsFOx5hOz1GgK90mNxASIxOHX4Libw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **3.不要在索引列上做任何操作（计算、函数、（自动or手动）类型转换），会导致索引失效而转向全表扫描**

```sql
EXPLAIN SELECT * FROM employees WHERE name = 'LiLei';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W98iaPyg8yLIQjXs0ibTWzttf0goibbKYpUBjJS7EPENT596SCfLsjH3zDg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```sql
EXPLAIN SELECT * FROM employees WHERE left(name,3) = 'LiLei';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9mVTEJpYbJQsjL4kbYXqI2SxuXlrpvocW3PrgOAgaOTP4NqLnOR6BHQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **4.存储引擎不能使用索引中范围条件右边的列**

```
EXPLAIN SELECT * FROM employees WHERE name= 'LiLei' AND age = 22 AND position ='manager';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9DC9YtKWdztT5mbicWCeqMEvgNB1c6VAicy55aFxicOTr9bib9YLRTXZb6A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```
EXPLAIN SELECT * FROM employees WHERE name= 'LiLei' AND age > 22 AND position ='manager';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9MAwZL247G6B2ibQPNkFsI0wGbtEcEH9R7Jtew3JRX6me3kLTMVU55gQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **5.尽量使用覆盖索引（只访问索引的查询（索引列包含查询列）），减少select \*语句**

```
EXPLAIN SELECT name,age FROM employees WHERE name= 'LiLei' AND age = 23 AND position ='manager';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9w5VjwSKIFe36cXERdmGq6BFIAjv4aH214faU5mE9qtvQHyAxUI4lvw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```
EXPLAIN SELECT * FROM employees WHERE name= 'LiLei' AND age = 23 AND position ='manager';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9KMXiaaHzXAibic1z009vdrsibsJU4DWPRuAOWCGOZwFUFRWm9k2o3VGLdg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **6.mysql在使用不等于（！=或者<>）的时候无法使用索引会导致全表扫描**

```
EXPLAIN SELECT * FROM employees WHERE name != 'LiLei';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9F4HpWRMr9aTN9vZU9SNqJ34HtSUxcn9crpTtVgb916pWbd7xSIxYnA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **7.is null,is not null 也无法使用索引**

```
EXPLAIN SELECT * FROM employees WHERE name is null;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9x2r9LuQTp0PlxuAlASia6G7KYjncjA473uEPIccsKzX673otcNbZA3A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **8.like以通配符开头（'$abc...'）mysql索引失效会变成全表扫描操作**

```
EXPLAIN SELECT * FROM employees WHERE name like '%Lei';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9sYGX8HtUhaP4lKJCt6Qhj3DFabo6DRPicIktWQrDzSVAg7F9oocU5Qg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```
EXPLAIN SELECT * FROM employees WHERE name like 'Lei%';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9ZpheBSWQibBSjvvtoZEucWibw00cyVFKMDvunNicy6R0wtdlsmVkIichSQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

问题：解决like'%字符串%'索引不被使用的方法？a）使用覆盖索引，查询字段必须是建立覆盖索引字段

```
EXPLAIN SELECT name,age,position FROM employees WHERE name like '%Lei%';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9qicjakhGxq8yXGz0eiaxh2Jtycj26JtYfJI85mVXN5AmV2QD09xel20Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)b）当覆盖索引指向的字段是varchar(380)及380以上的字段时，覆盖索引会失效！

### **9.字符串不加单引号索引失效**

```
EXPLAIN SELECT * FROM employees WHERE name = '1000';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9vaRxnfVodU1ufeaypsvHnjYlkcPfONqfN1QicETicIg8QAHbquggkd4g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```
EXPLAIN SELECT * FROM employees WHERE name = 1000;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9FWYcIo6ssbQwCdzZO6ZZBibb2Un5KgAo3s1V7AuibojILPan0LFxln0Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **10.少用or,用它连接时很多情况下索引会失效**

```
EXPLAIN SELECT * FROM employees WHERE name = 'LiLei' or name = 'HanMeimei';
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9F02MMHWyHkFVSX4eevSl2xrcnNIgGVfN1yJV2lziaZee0aOzl63uqgw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### **11.in和exsits优化**

原则：**小表驱动大表**，即小的数据集驱动大的数据集

- in：当B表的数据集必须小于A表的数据集时，in优于exists

  ```sql
  select * from A where id in (select id from B);
  explain select * fromfilmwhereidin(select film_id from film_actor);
  ```

- exists：当A表的数据集小于B表的数据集时，exists优于in 将主查询A的数据，放到子查询B中做条件验证，根据验证结果（true或false）来决定主查询的数据是否保留 select * from A where exists (select 1 from B where B.id = A.id) #A表与B表的ID字段应建立索引

```sql
explain select * from film where exists (select 1 from film_actor where film_actor.film_id = film.id)
```

1、EXISTS (subquery)只返回TRUE或FALSE,因此子查询中的SELECT * 也可以是SELECT 1或select X,官方说法是实际执行时会忽略SELECT清单,因此没有区别

 2、EXISTS子查询的实际执行过程可能经过了优化而不是我们理解上的逐条对比 3、EXISTS子查询往往也可以用JOIN来代替，何种最优需要具体问题具体分析

### **总结**

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W93FXhlGDjzQScVusU1jHa2MFDw08rCIfxHEIwI7GJ359eAQQmx6Ty3g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)**like KK%相当于=常量，%KK和%KK% 相当于范围**

------

# Mysql索引优化

------

## *创建test表（测试表） *

```sql
CREATE TABLE `test` (  
    `id` int(11) NOT NULL AUTO_INCREMENT,  
    `c1` varchar(10) DEFAULT NULL,  
    `c2` varchar(10) DEFAULT NULL,  
    `c3` varchar(10) DEFAULT NULL,  
    `c4` varchar(10) DEFAULT NULL,  
    `c5` varchar(10) DEFAULT NULL,  
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into test(c1,c2,c3,c4,c5) values('a1','a2','a3','a4','a5');
insert into test(c1,c2,c3,c4,c5) values('b1','b2','b3','b4','b5');
insert into test(c1,c2,c3,c4,c5) values('c1','c2','c3','c4','c5');
insert into test(c1,c2,c3,c4,c5) values('d1','d2','d3','d4','d5');
insert into test(c1,c2,c3,c4,c5) values('e1','e2','e3','e4','e5');
```

## **创建索引**

```sql
create index idx_test_c1234 on test(c1,c2,c3,c4);show index from test;
```

![img](https://mmbiz.qpic.cn/mmbiz_png/TlAAiakeZ5C1EnvxP2tFGPyVfLWKtib7W9BhT2ZBq3fNibp0j00bib2gFpkWX364bzfVqBfRV8QFmPnYPqnIqblQicg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

## **分析以下Case索引使用情况**

### Case 1：

```sql
explain select * from test where c1='a1' and c2='a2' and c3='a3' and c4='a4';
explain select * from test where c1='a1' and c4='a4' and c2='a2' and c3='a3' ;
```



分析：①创建复合索引的顺序为c1,c2,c3,c4。②上述explain执行的结果都一样：type=ref，key_len=132，ref=const,const,const,const。结论：在执行常量等值查询时，改变索引列的顺序并不会更改explain的执行结果，因为mysql底层优化器会进行优化，但是推荐按照索引顺序列编写sql语句。

### Case 2：

```sql
explain select * from test where c1='a1' and c2='a2';
```



```sql
explain select * from test where c1='a1' and c2='a2' and c3>'a3' and c4='a4';
```



分析：当出现范围的时候，type=range，keylen=99，比不用范围keylen=66增加了，说明使用上了索引，但对比Case1中执行结果，说明c4上索引失效。结论：范围右边索引列失效，但是范围当前位置（c3）的索引是有效的，从key_len=99可证明。

#### Case 2.1：

```sql
explain select * from test where c1='a1' and c2='a2' and c4>'a4' and c3='a3' ;
```



分析：与上面explain执行结果对比，key_len=132说明索引用到了4个，因为对此sql语句mysql底层优化器会进行优化：范围右边索引列失效（c4右边已经没有索引列了），注意索引的顺序（c1,c2,c3,c4），所以c4右边不会出现失效的索引列，因此4个索引全部用上。结论：范围右边索引列失效，是有顺序的：c1,c2,c3,c4，如果c3有范围，则c4失效；如果c4有范围，则没有失效的索引列，从而会使用全部索引。

#### Case 2.2：

```sql
explain select * from test where c1>'a1' and c2='a2' and c3='a3' and c4='a4';
```



分析：如果在c1处使用范围，则type=ALL，key=Null，索引失效，全表扫描，这里违背了最左前缀法则，带头大哥已死，因为c1主要用于范围，而不是查询。解决方式使用覆盖索引。

结论：在最左前缀法则中，如果最左前列（带头大哥）的索引失效，则后面的索引都失效。

### Case 3：

```sql
explain select * from test where c1='a1' and c2='a2'  and c4='a4' order by c3;
```



分析：利用最左前缀法则：中间兄弟不能断，因此用到了c1和c2索引（查找），从key_len=66，ref=const,const，c3索引列用在排序过程中。

#### Case 3.1：

```sql
explain select * from test where c1='a1' and c2='a2' order by c3;
```



分析：从explain的执行结果来看：key_len=66，ref=const,const，从而查找只用到c1和c2索引，c3索引用于排序。

#### Case 3.2：

```sql
explain select * from test where c1='a1' and c2='a2' order by c4;
```



分析：从explain的执行结果来看：key_len=66，ref=const,const，查询使用了c1和c2索引，由于用了c4进行排序，跳过了c3，出现了Using filesort。

### Case 4：

```sql
explain select * from test where c1='a1' and c5='a5' order by c2,c3;
```



分析：查找只用到索引c1，c2和c3用于排序，无Using filesort。

#### Case 4.1：



分析：和Case 4中explain的执行结果一样，但是出现了Using filesort，因为索引的创建顺序为c1,c2,c3,c4，但是排序的时候c2和c3颠倒位置了。

#### Case 4.2：

```sql
explain select * from test where c1='a1' and c2='a2' order by c2,c3;
```





分析：在查询时增加了c5，但是explain的执行结果一样，因为c5并未创建索引。

#### Case 4.3：

```sql
explain select * from test where c1='a1' and c2='a2' and c5='a5' order by c3,c2;
```



分析：与Case 4.1对比，在Extra中并未出现Using filesort，因为c2为常量，在排序中被优化，所以索引未颠倒，不会出现Using filesort。

### Case 5：

```sql
explain select * from test where c1='a1' and c4='a4' group by c2,c3;
```



分析：只用到c1上的索引，因为c4中间间断了，根据最左前缀法则，所以key_len=33，ref=const，表示只用到一个索引。

#### Case 5.1：

```sql
explain select * from test where c1='a1' and c4='a4' group by c3,c2;
```



分析：对比Case 5，在group by时交换了c2和c3的位置，结果出现Using temporary和Using filesort，极度恶劣。原因：c3和c2与索引创建顺序相反。

### Case 6：

```sql
explain select * from test where c1>'a1' order by c1;
```



分析：①在c1,c2,c3,c4上创建了索引，直接在c1上使用范围，导致了索引失效，全表扫描：type=ALL，ref=Null。因为此时c1主要用于排序，并不是查询。②使用c1进行排序，出现了Using filesort。③解决方法：使用覆盖索引。



### Case 7：

```sql
explain select c1 from test order by c1 asc,c2 desc;
```



分析：虽然排序的字段列与索引顺序一样，且order by默认升序，这里c2 desc变成了降序，导致与索引的排序方式不同，从而产生Using filesort。

### Case 8：

```sql
EXPLAIN extended select c1 from test where c1 in ('a1','b1') ORDER BY c2,c3;
```



分析：对于排序来说，多个相等条件也是范围查询

## **总结：**

①MySQL支持两种方式的排序filesort和index，Using index是指MySQL扫描索引本身完成排序。index效率高，filesort效率低。②order by满足两种情况会使用Using index。

- order by语句使用索引最左前列。
- 使用where子句与order by子句条件列组合满足索引最左前列。

③尽量在索引列上完成排序，遵循索引建立（索引创建的顺序）时的最左前缀法则。④如果order by的条件不在索引列上，就会产生Using filesort。⑤group by与order by很类似，其实质是先排序后分组，遵照索引创建顺序的最左前缀法则。注意where高于having，能写在where中的限定条件就不要去having限定了。

**通俗理解口诀：**  全值匹配我最爱，最左前缀要遵守；  带头大哥不能死，中间兄弟不能断；  索引列上少计算，范围之后全失效；  LIKE百分写最右，覆盖索引不写星；  不等空值还有or，索引失效要少用