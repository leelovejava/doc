# zookeeper

## doc

[ZooKeeper基本原理](https://www.cnblogs.com/luxiaoxun/p/4887452.html)

## 大纲

### Zookeeper原理
* Zookeeper概述、特点
* Zookeeper数据结构
* Zookeeper应用场景
* Zookeeper下载地址
* Zookeeper本地模式安装
* 配置参数解读
* Zookeeper内部原理
* Zookeeper选举机制
* Zookeeper节点类型
* Stat结构体
 
### Zookeeper原理&实战
* 监听器原理、写数据流程
* 分布式安装部署
* 客户端命令行操作
* API应用环境搭建
* 创建ZooKeeper客户端案例
* 创建子节点案例
* 获取子节点并监听节点变化案例
* 判断Znode是否存在案例
* 监听服务器节点动态上下线案例
* 企业高频真题讲解3道
 
## 安装

zookeeper有单机、伪集群、集群三种部署方式
 
> wget https://apache.org/dist/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz
 
一、单机安装

### 1.1 下载
进入要下载的版本的目录，选择.tar.gz文件下载
下载链接：http://archive.apache.org/dist/zookeeper/
 
### 1.2 安装
使用tar解压要安装的目录即可，以3.4.5版本为例
这里以解压到/usr/local，实际安装根据自己的想安装的目录修改（注意如果修改，那后边的命令和配置文件中的路径都要相应修改）
tar -zxf zookeeper-3.4.5.tar.gz -C /usr/local
 
### 1.3 配置
在主目录下创建data和logs两个目录用于存储数据和日志：
 cd /usr/local/zookeeper-3.4.5
 mkdir data
 mkdir logs
在conf目录下新建zoo.cfg文件，写入以下内容保存：
tickTime=2000
dataDir=/usr/local/zookeeper-3.4.5/data
dataLogDir=/usr/local/zookeeper-3.4.5/logs
clientPort=2181
 
### 1.4 启动和停止
进入bin目录，启动、停止、重启分和查看当前节点状态（包括集群中是何角色）别执行：
./zkServer.sh start
./zkServer.sh stop
./zkServer.sh restart
./zkServer.sh status
 
## 二、伪集群模式
伪集群模式就是在同一主机启动多个zookeeper并组成集群，下边以在192.168.220.128主机上创3个zookeeper组集群为例。
将通过第一大点安装的zookeeper，复制成zookeeper1/zookeeper2/zookeeper3三份
 奇数个，原因：zookeepr选举机制(如果选举的票数为偶数，又得重新选举)，过半的zookeeper正常运行，才能正常工作 
 2888:3888 =>客户端连接端口：集群通信端口

### 2.1 zookeeper1配置
zookeeper1配置文件conf/zoo.cfg修改如下：
tickTime=2000
dataDir=/usr/local/zookeeper-cluster/zookeeper1/data
dataLogDir=/usr/local/zookeeper-cluster/zookeeper1/logs
clientPort=2181
initLimit=5
syncLimit=2
server.1=192.168.220.128:2888:3888
server.2=192.168.220.128:4888:5888
server.3=192.168.220.128:6888:7888
zookeeper1的data/myid配置如下：
 echo 1 > data/myid

### 2.2 zookeeper2配置
zookeeper2配置文件conf/zoo.cfg修改如下：
tickTime=2000
dataDir=/usr/local/zookeeper2/data
dataLogDir=/usr/local/zookeeper2/logs
clientPort=3181
initLimit=5
syncLimit=2
server.1=192.168.220.128:2888:3888
server.2=192.168.220.128:4888:5888
server.3=192.168.220.128:6888:7888
zookeeper2的data/myid配置如下：
 echo 2 > data/myid

### 2.3 zookeeper3配置
zookeeper3配置文件conf/zoo.cfg修改如下：
tickTime=2000
dataDir=/usr/local/zookeeper-cluster/zookeeper3/data
dataLogDir=/usr/local/zookeeper-cluster/zookeeper3/logs
clientPort=4181
initLimit=5
syncLimit=2
server.1=192.168.220.128:2888:3888
server.2=192.168.220.128:4888:5888
server.3=192.168.220.128:6888:7888
 zookeeper3的data/myid配置如下：
echo 3 > data/myid
最后使用1.4的命令把三个zookeeper都启动即可，启动顺序随意没要求。
 
## 三、集群模式
集群模式就是在不同主机上安装zookeeper然后组成集群的模式；下边以在192.168.220.128/129/130三台主机为例。
将第1.1到1.3步中安装好的zookeeper打包复制到129和130上，并都解压到同样的目录下。
 
### 3.1 conf/zoo.cfg文件修改
三个zookeeper的conf/zoo.cfg修改如下：
tickTime=2000
dataDir=/usr/local/zookeeper-cluster/zookeeper-3.4.5/data
dataLogDir=/usr/local/zookeeper-cluster/zookeeper-3.4.5/logs
clientPort=2181
initLimit=5
syncLimit=2
server.1=192.168.220.128:2888:3888
server.2=192.168.220.129:2888:3888
server.3=192.168.220.130:2888:3888
对于129和130，由于安装目录都是zookeeper-3.4.5所以dataDir和dataLogDir不需要改变，又由于在不同机器上所以clientPort也不需要改变
所以此时129和130的conf/zoo.cfg的内容与128一样即可。
 
### 3.2 data/myid文件修改
128 data/myid修改如下：
echo 1 > data/myid
129 data/myid修改如下：
echo 2 > data/myid
130 data/myid修改如下：
echo 3 > data/myid
最后使用1.4的命令把三个zookeeper都启动即可，启动顺序随意没要求。

id约大,成为leader几率越大
 
## 四、报错及处理
应用连接zookeepr报错：Session 0x0 for server 192.168.220.128/192.168.220.128:2181,unexpected error,closing socket connection and attempting reconnect；
                                        先看端口能否telnet通，如果通则使用./zkServer.sh status查看zk是否确实已启动，没启查看bin/zookeeper.out中的报错。
bin/zookeeper.out中报错：“zookeeper address already in use”；显然端口被占用，要么是其他进程占用了配置的端口，要么是上边配置的clientPort和server中的端口有重复。
bin/zookeeper.out中报错：Cannot open channel to 2 at election address /192.168.220.130:3888；这应该只是组成集群的130节点未启动，到130启动起来zk即会正常。
参考：
http://coolxing.iteye.com/blog/1871009
https://zookeeper.apache.org/doc/r3.4.10/zookeeperStarted.html

## 应用

分布式锁 [基于 Zookeeper 的分布式锁实现](https://mp.weixin.qq.com/s/CUReuX_rLb4IfQOtTwS_yQ)