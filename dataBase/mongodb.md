# mongodb

## 概述:

## doc

[百度云MongoDB产品的六大特性](https://baijiahao.baidu.com/s?id=1618343308382561681)

[springboot集成mongodb](https://blog.csdn.net/u011095110/article/details/77887149)

[golang的mongodb操作(mgo)](https://studygolang.com/articles/1737)

[双刃剑MongoDB的学习和避坑](https://mp.weixin.qq.com/s/fTG-Wff0ZsWqakXtVDXsqg)

[MongoDB 4.2 新特性解读](http://www.mongoing.com/archives/26774)

## 安装:
https://www.mongodb.com/download-center/community

tgz
>> wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel62-3.4.3.tgz

>> tar -xf mongodb-linux-x86_64-rhel62-3.4.3.tgz -C ~/app/

mongodb.conf
```
# 绑定ip
#bind_ip:0.0.0.0
#端口号
port = 27017
#数据目录
dbpath = /usr/local/mongodb/data/db
#日志目录
logpath = /usr/local/mongodb/data/logs/mongodb.log
#设置后台运行
fork = true
#日志输出方式
logappend = true
#开启认证
#auth = true
```

升级glibc
>> sudo yum update glibc

启动
>> sudo /home/hadoop/app/mongodb-linux-x86_64-rhel70-3.4.7/bin/mongod -config /home/hadoop/app/mongodb-linux-x86_64-rhel70-3.4.7/data/mongodb.conf

```启动错误
/lib64/libc.so.6: version `GLIBC_2.15' not found

wget http://ftp.gnu.org/gnu/glibc/glibc-2.15.tar.gz

解压： 
tar xvf glibc-2.15.tar.gz
进入glibc-2.15目录：
cd glibc-2.15
创建build文件夹：
mkdir build
进入build目录：
cd build

../configure --prefix=/home/hadoop/app/glibc-2.15
make编译
    sudo make & install
```

yum安装

安装必要插件
>> sudo yum -y install gcc make gcc-c++ openssl-devel wget
>> sudo yum install net-tools

1).创建仓库文件
>> sudo vi /etc/yum.repos.d/mongodb-org-3.4.repo

```
[mongodb-org-3.4]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/3.4/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-3.4.asc
```

2).yum安装
>> sudo yum install -y mongodb-org

3).修改配置,允许外网访问
vi /etc/mongod.conf
bind_ip:0.0.0.0

4).命令

启动:
>> service mongod start

>> sudo bin/mongod -config data/mongodb.conf

停止:
>> service mongod stop

重启:
>> service mongod stop

查看日志:
>> cat /var/log/mongodb/mongod.log

chkconfig mongod on

杀死进程
>> pkill mongo

## 使用
>> mongo
>> bin/mongo

查看数据库
>> show dbs;

查看数据库版本
>> db.version();

命令帮助
>> db.help();

## 卸载

卸载移除mongo
>> yum erase $(rpm -qa | grep mongodb-org)

移除数据库文件
>> rm -r /var/log/mongodb

rm -r /var/lib/mongo
>> rm -r /var/lib/mongo


tar安装包安装
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel62-3.4.3.tgz
// 在安装目录下创建data文件夹用于存放数据和日志
// 在data文件夹下创建db文件夹，用于存放数据
// 在data文件夹下创建logs文件夹，用于存放日志
// 在logs文件夹下创建log文件
>> touch /usr/local/mongodb/data/logs/ mongodb.log
// 在data文件夹下创建mongodb.conf配置文件
>> touch /usr/local/mongodb/data/mongodb.conf
vim ./data/mongodb.conf
```
#端口号port = 27017
#数据目录
dbpath = /usr/local/mongodb/data/db
#日志目录
logpath = /usr/local/mongodb/data/logs/mongodb.log
#设置后台运行
fork = true
#日志输出方式
logappend = true
#开启认证
#auth = true
```

启动
>> sudo /usr/local/mongodb/bin/mongod -config /usr/local/mongodb/data/mongodb.conf
```
about to fork child process, waiting until server is ready for connections.
forked process: 2316
child process started successfully, parent exiting
```
访问
>> /usr/local/mongodb/bin/mongo
停止
>> sudo /usr/local/mongodb/bin/mongod -config /usr/local/mongodb/data/mongodb.conf

## 使用

创建用户

>> bin/mongo
>> use admin
>> db.createUser({user:"userAdmin",pwd:"admin123456",roles:["userAdminAnyDatabase"]})
>> db.createUser({user:"admin",pwd:"admin",roles:[{"role":"userAdminAnyDatabase","db":"admin"},{"role":"readWrite","db":"test"}]})