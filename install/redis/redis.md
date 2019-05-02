# redis安装
yum -y install cpp binutils glibc glibc-kernheaders glibc-common glibc-devel gcc make gcc-c++ libstdc++-devel tcl
 

wget http://download.redis.io/releases/redis-4.0.10.tar.gz  或者 rz 上传

tar -zxvf redis-4.0.10.tar.gz -C /usr/local

cd /usr/local/redis-4.0.10

make install PREFIX=/usr/local/redis    # 无需 make & install
 
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

bind 127.0.0.1 # bind 0.0.0.0 允许远程访问

## 2.把yes修改为no
protected-mode yes # protected-mode no 关闭保护模式
daemonize no # 允许后台启动 改为 yes

## 3.重启服务
redis-cli -h 127.0.0.1 -p 6379 shutdown

[ubuntu安装redis](https://www.cnblogs.com/langtianya/p/5187681.html)

sudo apt-get update
sudo apt-get install redis-server

```
Redis的配置

daemonize：如需要在后台运行，把该项的值改为yes

pdifile：把pid文件放在/var/run/redis.pid，可以配置到其他地址

bind：指定redis只接收来自该IP的请求，如果不设置，那么将处理所有请求，在生产环节中最好设置该项

port：监听端口，默认为6379

timeout：设置客户端连接时的超时时间，单位为秒

loglevel：等级分为4级，debug，revbose，notice和warning。生产环境下一般开启notice

logfile：配置log文件地址，默认使用标准输出，即打印在命令行终端的端口上

database：设置数据库的个数，默认使用的数据库是0

save：设置redis进行数据库镜像的频率

rdbcompression：在进行镜像备份时，是否进行压缩

dbfilename：镜像备份文件的文件名

dir：数据库镜像备份的文件放置的路径

slaveof：设置该数据库为其他数据库的从数据库

masterauth：当主数据库连接需要密码验证时，在这里设定

requirepass：设置客户端连接后进行任何其他指定前需要使用的密码

maxclients：限制同时连接的客户端数量

maxmemory：设置redis能够使用的最大内存

appendonly：开启appendonly模式后，redis会把每一次所接收到的写操作都追加到appendonly.aof文件中，当redis重新启动时，会从该文件恢复出之前的状态

appendfsync：设置appendonly.aof文件进行同步的频率

vm_enabled：是否开启虚拟内存支持

vm_swap_file：设置虚拟内存的交换文件的路径

vm_max_momery：设置开启虚拟内存后，redis将使用的最大物理内存的大小，默认为0

vm_page_size：设置虚拟内存页的大小

vm_pages：设置交换文件的总的page数量

vm_max_thrrads：设置vm IO同时使用的线程数量
```