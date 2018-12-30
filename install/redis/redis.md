# redis安装
yum -y install cpp binutils glibc glibc-kernheaders glibc-common glibc-devel gcc make gcc-c++ libstdc++-devel tcl
 

wget http://download.redis.io/releases/redis-4.0.10.tar.gz  或者 rz 上传

tar -zxvf redis-4.0.10.tar.gz -C /usr/local

cd /usr/local/redis-4.0.10

make & install
 
cp redis.conf /etc/
vi /etc/redis.conf
## 修改如下，默认为no
daemonize yes
  
## 启动
redis-server /etc/redis.conf
## 测试
redis-cli

redis远程连接
https://blog.csdn.net/lsm135/article/details/72472189
## 1.注释
bind 127.0.0.1
## 2.把yes修改为no
protected-mode yes 
## 3.重启服务
redis-cli -h 127.0.0.1 -p 6379 shutdown

[ubuntu安装redis](https://www.cnblogs.com/langtianya/p/5187681.html)

sudo apt-get update
sudo apt-get install redis-server