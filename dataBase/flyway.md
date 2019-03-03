## flyway学习笔记
### blog
  * http://blog.didispace.com/spring-boot-flyway-db-version/
  * http://hao.jobbole.com/flyway/
### 1.简介
 简单开源数据库版本控制器（约定大于配置），功能上像是git、svn这种代码版本控制,
 主要提供migrate、clean、info、validate、baseline、repair等命令。
 它支持SQL（PL/SQL、T-SQL）方式和Java方式，支持命令行客户端等，还提供一系列的插件支持（Maven、Gradle、SBT、ANT等）
 
 官网:https://flywaydb.org/
 
 官方简介:
   * Version control for your database.
   * Robust schema evolution across all your environments.
   * With ease, pleasure and plain SQL.
   
   * 数据库的版本控制
   * 跨所有环境的强大架构演变
   * 轻松，愉快和简单的SQL
 
### 2.支持的数据库:
* Oracle
* SQL Server
* MySQL
* MariaDB
* PostgreSQL

### 3.Why database migrations?为什么要迁移数据库？
   #### 问题:
   * What state is the database in on this machine?
    这台机器上的数据库是什么状态？
   * Has this script already been applied or not?
    是否已应用此脚本？
   * Has the quick fix in production been applied in test afterwards?
    生产中的快速修复是否已经在测试中应用？
   * How do you set up a new database instance?
    你如何设置一个新的数据库实例？
   
   #### 好处： 
   * Recreate a database from scratch
     从头开始重新创建数据库
   * Make it clear at all times what state a database is in
     始终明确数据库所处的状态
   * Migrate in a deterministic way from your current version of the database to a newer one
     以确定的方式从当前版本的数据库迁移到较新的版本
     
   ##### 特效
   * 普通SQL：纯SQL脚本(包括占位符替换)没有专有的XML格式，没有锁定。
   * 无限制：使用Java 代码来进行一些高级数据操作
   * 零依赖：只需运行在Java６(及以上)和数据库所需的JDBC驱动
   * 约定优于配置：迁移时，自动查找系统文件和类路径中的SQL文件或Java类
   * 高可靠性：在集群环境下进行数据库升级是安全可靠的
   * 云支持：完全支持 Microsoft SQL Azure(微软云SQL), Google Cloud SQL& App Engine、Heroku Postgres 和 Amazon RDS
   * 自动迁移：使用Fly提供的API，让应用启动和迁移同时工作
   * 快速失败：损坏的数据库或失败的迁移可以防止应用程序启动
   * 数据库清理：在一个数据库中删除所有的表、视图、触发器，而不是删除数据库本身 
     
 ### 4.flyway原理 
   1. It will try to locate its schema history table. As the database is empty, Flyway won't find it and will create it instead.
       找scheam历史表,由于数据库是空的,flyway将不会查找，而是创建一个新元数据表
       默认有flyway_schema_history的空表
       
       This table will be used to track the state of the database
       此表用来跟踪数据库的状态
    2. 
       之后，Flyway将开始扫描系统文件或应用程序的类路径以进行迁移。它们可以用Sql或Java编写。
       然后根据版本号对迁移进行排序 
       
       Immediately afterwards Flyway will begin scanning the filesystem or the classpath of the application for migrations. They can be written in either Sql or Java.
       The migrations are then sorted based on their version number and applied in order:  
       
       随着每次执行，对应地更新元数据表
    3. flyway_schema_history表
    <table>
      <tr>
        <th>installed_rank</th>
        <th>1</th>
        <th>2</th>
      </tr>
      <tr>
        <td>version</td>
        <td>1</td>
        <td>2</td>
      </tr>
      <tr>
        <td>description</td>
        <td>Initial Setup</td>
        <td>First Changes</td>
      </tr>
      <tr>
        <td>type</td>
        <td>SQL</td>
        <td>SQL</td>
      </tr>
      <tr>
        <th>script</th>
        <td>V1__Initial_Setup.sql</td>
        <td>V2__First_Changes.sql</td>
      </tr>
      <tr>
        <th>checksum</th>
        <td>1996767037</td>
        <td>1279644856</td>
      </tr>
      <tr>
        <th>installed_by</th>
        <td>axel</td>
        <td>axel</td>
      </tr>
      <tr>
        <th>installed_on</th>
        <td>2016-02-04 22:23:00.0</td>
        <td>2016-02-06 09:18:00.0</td>
      </tr>
       <tr>
          <th>execution_time</th>
          <td>546</td>
          <td>127</td>
        </tr>
         <tr>
            <th>success</th>
            <td>true</td>
            <td>true</td>
         </tr>
    </table>
   4. Fly进行迁移时会重新扫描系统文件或者应用的类路径中特定的文件，并且与元数据表进行校验，如果它们的版本号低于或等于当前标记的版本,它们将被忽略
    而高于标记的文件将等待迁移：状态为可用(available)，但是未执行
    Flyway会将它们按照版本号进行排序并依次执行
    5. 重要概念:
     1). 迁移
     2). 回调
### 5.使用:
 ~~~ java
 Flyway flyway = new Flyway();
 flyway.setDataSource(url, user, password);
 flyway.migrate();
 ~~~ 
 
```xml
  <dependency>
      <groupId>org.flywaydb</groupId>
      <artifactId>flyway-core</artifactId>
      <version>4.0.3</version>
  </dependency>
```    

http://hao.jobbole.com/flyway/
http://blog.didispace.com/spring-boot-flyway-db-version/
https://www.cnblogs.com/logsharing/p/7808713.html
### 现有数据库设置
#### 清理包含您不介意丢失的数据的所有数据库(删除配置的模式中的所有对象),不要使用于生产环境
flyway clean
#### Give these databases a baseline version 为数据库提供基准版本
flyway baseline
#### 迁移
flyway migrate
#### 消息,打印有关所有迁移的详细信息和状态信息,信息可以让您知道自己的位置。一目了然，您将看到已经应用了哪些迁移，哪些迁移尚未处理，何时执行以及它们是否成功
flyway info

### 配置文件
```
# Settings are simple key-value pairs
flyway.key=value
# Single line comment start with a hash

# These are some example settings
flyway.url=jdbc:mydb://mydatabaseurl
flyway.schemas=schema1,schema2
flyway.placeholders.keyABC=valueXYZ
```


flyway的配置
flyway.enabled:false
    禁用
flyway.baseline-description
    对执行迁移时基准版本的描述.
flyway.baseline-on-migrate
    当迁移时发现目标schema非空，而且带有没有元数据的表时，是否自动执行基准迁移，默认false.
flyway.baseline-version
    开始执行基准迁移时对现有的schema的版本打标签，默认值为1.
flyway.check-location
    检查迁移脚本的位置是否存在，默认false.
flyway.clean-on-validation-error
    当发现校验错误时是否自动调用clean，默认false.
flyway.enabled
    是否开启flywary，默认true.
flyway.encoding
    设置迁移时的编码，默认UTF-8.
flyway.ignore-failed-future-migration
    当读取元数据表时是否忽略错误的迁移，默认false.
flyway.init-sqls
    当初始化好连接时要执行的SQL.
flyway.locations
    迁移脚本的位置，默认db/migration.
flyway.out-of-order
    是否允许无序的迁移，默认false.
flyway.password
    目标数据库的密码.
flyway.placeholder-prefix
    设置每个placeholder的前缀，默认${.
flyway.placeholder-replacementplaceholders
    是否要被替换，默认true.
flyway.placeholder-suffix
    设置每个placeholder的后缀，默认}.
flyway.placeholders.[placeholder name]
    设置placeholder的value
flyway.schemas
    设定需要flywary迁移的schema，大小写敏感，默认为连接默认的schema.
flyway.sql-migration-prefix
    迁移文件的前缀，默认为V.
flyway.sql-migration-separator
    迁移脚本的文件名分隔符，默认__
flyway.sql-migration-suffix
    迁移脚本的后缀，默认为.sql
flyway.tableflyway
    使用的元数据表名，默认为schema_version
flyway.target
    迁移时使用的目标版本，默认为latest version
flyway.url
    迁移时使用的JDBC URL，如果没有指定的话，将使用配置的主数据源
flyway.user
    迁移数据库的用户名
flyway.validate-on-migrate
    迁移时是否校验，默认为true.