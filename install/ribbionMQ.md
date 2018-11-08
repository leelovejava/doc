## 安装RabbitMQ

## 1.1.  安装Erlang
### 1.1.1.   添加yum支持
cd /usr/local/src/
mkdir rabbitmq
cd rabbitmq
 
wget http://packages.erlang-solutions.com/erlang-solutions-1.0-1.noarch.rpm

rpm -Uvh erlang-solutions-1.0-1.noarch.rpm
 
rpm --import http://packages.erlang-solutions.com/rpm/erlang_solutions.asc
 
yum install erlang
 
或者：
上传esl-erlang_17.3-1~centos~6_amd64.rpm

执行 yum install esl-erlang_17.3-1~centos~6_amd64.rpm
 
上传：esl-erlang-compat-R14B-1.el6.noarch.rpm

yum install esl-erlang-compat-R14B-1.el6.noarch.rpm
 
## 1.2.  安装RabbitMQ
上传rabbitmq-server-3.4.1-1.noarch.rpm文件到/usr/local/src/rabbitmq/

安装：
rpm -ivh rabbitmq-server-3.4.1-1.noarch.rpm
 
### 1.2.1.   启动、停止
service rabbitmq-server start

service rabbitmq-server stop

service rabbitmq-server restart


### 1.2.2.   设置开机启动
chkconfig rabbitmq-server on

### 1.2.3.   设置配置文件
cd /etc/rabbitmq
cp /usr/share/doc/rabbitmq-server-3.4.1/rabbitmq.config.example /etc/rabbitmq/
 
mv rabbitmq.config.example rabbitmq.config

### 1.2.4.   开启用户远程访问
vi /etc/rabbitmq/rabbitmq.config

注意要去掉后面的逗号。

### 1.2.5.   开启web界面管理工具
rabbitmq-plugins enable rabbitmq_management

service rabbitmq-server restart

### 1.2.6.   防火墙开放15672端口
/sbin/iptables -I INPUT -p tcp --dport 15672 -j ACCEPT

/sbin/iptables -I INPUT -p tcp --dport 5672 -j ACCEPT

/etc/rc.d/init.d/iptables save