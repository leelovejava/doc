# redis5.0集群安装

## doc
[redis集群官网](https://redis.io/topics/cluster-tutorial)

[Redis5.0安装与集群配置](https://blog.csdn.net/u013206433/article/details/83659237)

### 编译源码
```
yum install gcc-c++
make MALLOC=libc
make install PREFIX=/usr/local/redis-cluster
```

### 修改redis.conf
```
# 端口  
port 7000
# 后台启动 yes,默认no
daemonize yes
# 修改为主机名
bind hadoop001
# 启动集群服务
cluster-enabled yes
# 集群配置文件  注意不同端口节点修改不同文件名称
cluster-config-file nodes-7000.conf
# 集群节点超时是指一个节点必须达到的毫秒数
cluster-node-timeout 5000
appendonly yes
```

### 搭建集群
```
> mkdir cluster-conf
> cd cluster-conf
> mkdir 7000 7001 7002 7003 7004 7005
# 将对应的配置文件放入进去后 启动每个服务
> src/redis-server cluster-conf/7000/redis.conf 
# 7000-7005服务启动成功后，使用5.0特有的redis-cli功能开启集群
bin/redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 1
```

连接
-c 连接集群
> bin/redis-cli -h 127.0.0.1 -p 7000 -c

关闭集群
> pkill -9 redis

> bin/redis-cli --cluster check 127.0.0.1:7000

查看集群信息
> cluster info

问题:
[redis集群报错Node is not empty](https://www.cnblogs.com/huxinga/p/6644226.html)