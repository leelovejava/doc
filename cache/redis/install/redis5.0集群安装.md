# redis5.0集群安装

## doc
[redis集群官网](https://redis.io/topics/cluster-tutorial)

[Redis5.0安装与集群配置](https://blog.csdn.net/u013206433/article/details/83659237)

### 编译源码
```
# 安装依赖
yum -y install gcc gcc-c++ tcl
make & make install PREFIX=/usr/local/redis-cluster
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
# 开启aof的持久化
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


#### 集群模式的得与失

redis cluster的出现，并不意味着其完全碾压redis sentinel模式。相反，如果不到万不得已，更建议使用redis sentinel而不是redis cluster

集群模式和哨兵模式对比:

| 模式     | 高可用| 存储能力|吞吐能力|运维成本|弹性扩展|命令覆盖率|
|----------|:-----:|--------:|-------:|-------:|-------:|---------:|
| sentinel |  满足 | 弱      |一般    | 低     | 不支持 | 所有 |
| cluster  |  满足 | 强      | 大     | 高     | 支持   | 阉割 |

选集群模式还是哨兵模式，还是取决于你的业务。如果你的业务的天花板完全可预见在20G以内，那么建议哨兵模式