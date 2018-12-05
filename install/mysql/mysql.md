# mysql安装

wget http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm 

rpm -ivh mysql-community-release-el7-5.noarch.rpm

sudo yum -y install mysql mysql-server mysql-devel

service mysqld start

mysql -u root -p 输入密码，默认为空(root)

show databases;

--------------------------------------
rpm -qa | grep mysql  

如果已经安装了，将其卸载，如：
rpm -e --nodeps  mysql-libs-5.1.71-1.el6.x86_64

yum卸载
yum list | grep mysql 或 yum -y list mysql*
yum remove 

wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm

rpm -ivh mysql-community-release-el7-5.noarch.rpm

yum install mysql-server

yum install mysql-devel

mysql配置文件/etc/my.cnf的[mysqld]中加入character-set-server=utf8

## 启动

service mysqld start

## 登录mysql

mysql -u root -p

use mysql;

## 修改密码

UPDATE user SET password=password("你的密码") WHERE user='root';

## 刷新权限

FLUSH PRIVILEGES;

## 忘记root密码后重置密码
https://www.cnblogs.com/gumuzi/p/5711495.html

## 开启远程访问
grant all privileges on *.* to 'root'@'%' identified by '密码'; 

## 刷新权限 重启服务

## 开放3306端口或者关闭防火墙