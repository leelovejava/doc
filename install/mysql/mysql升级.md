[centOS 6.5下升级mysql，从5.1升级到5.7](https://www.cnblogs.com/vickygu2007/p/5066409.html)

1、备份数据库，升级MySQL通常不会丢失数据,保险起见,备份数据

>> mysqldump -u root -P 3306 -p --all-databases > databases.sql

2、 停止MySQL服务
>> sudo service stop mysqld

3、 卸载旧版MySQL
>> sudo yum remove mysql mysql-*

查看已安装的软件：
>> rpm -qa|grep mysql

卸载mysql：
>> sudo yum remove mysql mysql-server mysql-libs compat-mysql51

4、 移除命令执行后，可再看看是否有残余的mysql
>> sudo yum list installed | grep mysql

>> sudo yum remove mysql－libs

5、 下载安装rpm文件

>> sudo rpm -Uvh http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm

>> wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm

>> sudo rpm -ivh mysql-community-release-el7-5.noarch.rpm

6、 安装MySQL

>> sudo yum install mysql-community-server -y

7、 安装完成后，输入命令查看MySQL版本号

>> mysql -V

8 、 启动MySQL
>> service mysqld start