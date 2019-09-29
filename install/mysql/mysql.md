# mysql安装

[windows装mysql8](https://www.cnblogs.com/tangyb/p/8971658.html)

[linux安装mysql8](https://www.cnblogs.com/wlwl/p/9686809.html)

[Navicat连接Mysql8.0.11出现1251错误](https://blog.csdn.net/qq_36068954/article/details/80175755)

> sudo mysqld --skip-grant-tables  --skip-networking &

mysql8设置时区更改为东八区
> set global time_zone = '+8:00';

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
```bash
yum list | grep mysql 或 yum -y list mysql*
yum remove

# rpm
rpm -qa|grep mysql
rpm -e --nodeps mysql-libs-5.1.73-7.el6.x86_64 
```

wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm

rpm -ivh mysql-community-release-el7-5.noarch.rpm

yum install mysql-server

yum install mysql-devel

mysql配置文件/etc/my.cnf的
[mysqld]中加入
character-set-server=utf8
validate_password=off

## 启动

service mysqld start

## 登录mysql

mysql -u root -p

use mysql;

## 修改密码

UPDATE user SET password=password("你的密码") WHERE user='root';

## 密码强度
> set global validate_password_policy=0;

--validate_password_length(密码长度)参数默认为8，修改为1
> set global validate_password_length=1;

## 刷新权限

> FLUSH PRIVILEGES;

## 忘记root密码后重置密码
https://www.cnblogs.com/gumuzi/p/5711495.html

## 开启远程访问
grant all privileges on *.* to 'root'@'%' identified by '密码'; 

## 刷新权限 重启服务

## 开放3306端口或者关闭防火墙


-------------------
### centos7
yum localinstall https://repo.mysql.com//mysql80-community-release-el7-1.noarch.rpm
yum clean all
yum makecache

yum install mysql-community-server

systemctl start mysqld.service

### centos6
wget http://repo.mysql.com/mysql57-community-release-el6-10.noarch.rpm
yum -y localinstall mysql57-community-release-el6-10.noarch.rpm
yum -y install mysql-community-server

卸载
rpm -qa|grep -i mysql
rm -rf /etc/my.cnf


rpm -ev mysql80-community-release-el7-1.noarch --nodeps
rpm -ev mysql-community-server-8.0.16-1.el7.x86_64 --nodeps
rpm -ev mysql-community-common-8.0.16-1.el7.x86_64 --nodeps
rpm -ev mysql-community-client-8.0.16-1.el7.x86_64 --nodeps
rpm -ev mysql-community-libs-8.0.16-1.el7.x86_64 --nodeps


grep "password" /var/log/mysqld.log


rpm -ev mysql-devel-5.1.73-7.el6.x86_64 --nodeps
rpm -ev perl-DBD-MySQL-4.013-3.el6.x86_64 --nodeps
rpm -ev mysql-5.1.73-7.el6.x86_64 --nodeps
rpm -ev mysql-libs-5.1.73-7.el6.x86_64 --nodeps
rpm -ev mysql-server-5.1.73-7.el6.x86_64 --nodeps

### mysql升级
wget http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm
sudo yum install mysql-community-release-el7-5.noarch.rpm
sudo yum update mysql-server

### 安装下载地址

https://dev.mysql.com/downloads/mysql/5.7.html#downloads

--国内中科大镜像
http://mirrors.ustc.edu.cn/mysql-ftp/Downloads/
http://mirrors.ustc.edu.cn/mysql-ftp/Downloads/MySQL-5.7/mysql-5.7.26-1.el7.x86_64.rpm-bundle.tar

### rpm离线安装
rpm -ivh mysql-community-client-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-embedded-compat-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-server-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-common-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-embedded-devel-5.7.26-1.el7.x86_64.rpm  
rpm -ivh mysql-community-test-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-devel-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-embedded-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-compat-5.7.26-1.el7.x86_64.rpm

### 修改mysql yum源
vim /etc/yum.repos.d/mysql-community.repo
```bash
# Enable to use MySQL 5.7
[mysql57-community]
name=MySQL 5.7 Community Server
baseurl=http://repo.mysql.com/yum/mysql-5.7-community/el/7/$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql
```

离线
wget -i -c http://dev.mysql.com/get/mysql57-community-release-el7-10.noarch.rpm
yum -y install mysql57-community-release-el7-10.noarch.rpm
yum -y install mysql-server

### mysql5.7忘记密码

> cat /var/log/mysqld.log | grep password


```bash
# 1.修改配置文件my.cnf
> vim /etc/my.cnf
#mysqld之后添加
> skip-grant-tables

# 2. 重启服务
> service mysqld restart

# 3.直接登陆mysql而不需要密码
> mysql -u root
# (直接点击回车)

# 4. 修改密码
> update mysql.user set authentication_string=password('123456') where user='root' ;

# 5. 刷新权限
> flush privileges;

# 6. 删除/etc/my.cnf中的`skip-grant-tables`配置
```

#### 错误
ERROR 1820 (HY000): You must reset your password using ALTER USER statement before executing this statement

set password=password("youpassword");


windows
注册成window服务
mysqld.exe --install MySql --defaults-file="D:\setup\mysql\my.ini"

启动服务
net start mysql