一、环境要求
JDK 1.6+
java -version
Python 2.6.6+
python -V

ZooKeeper3.4.5+
storm 0.9.4+

--------------------------------------------------------------------
二、单机模式
上传解压
$ tar xf apache-storm-0.9.4.tar.gz 
$ cd apache-storm-0.9.4

$ storm安装目录下创建log：  mkdir logs
$ ./bin/storm --help
下面分别启动ZooKeeper、Nimbus、UI、supervisor、logviewer
$ ./bin/storm dev-zookeeper >> ./logs/zk.out 2>&1 &
$ ./bin/storm nimbus >> ./logs/nimbus.out 2>&1 &
$ ./bin/storm ui >> ./logs/ui.out 2>&1 &
$ ./bin/storm supervisor >> ./logs/supervisor.out 2>&1 &
$ ./bin/storm logviewer >> ./logs/logviewer.out 2>&1 &
需要等一会儿
$ jps
6966 Jps
6684 logviewer
6680 dev_zookeeper
6681 nimbus
6682 core
6683 supervisor

修改ui的端口
```
vim conf/storm.yaml

添加
ui.port: 8080
```

http://node01:8080
提交任务到Storm集群当中运行：
$ ./bin/storm jar examples/storm-starter/storm-starter-topologies-0.9.4.jar storm.starter.WordCountTopology wordcount
$ ./bin/storm jar examples/storm-starter/storm-starter-topologies-0.9.4.jar storm.starter.WordCountTopology test

-------------------------------------------------------------------------------------------



三、完全分布式安装部署
各节点分配：
         Nimbus    Supervisor   Zookeeper
node1      1                       1
node2                 1            1
node3                 1            1

node1作为nimbus，
开始配置
$ vim conf/storm.yaml
storm.zookeeper.servers:
  - "node1"
  - "node2"
  - "node3"

storm.local.dir: "/tmp/storm"

nimbus.host: "node1"

supervisor.slots.ports:
    - 6700
    - 6701
    - 6702
    - 6703


在storm目录中创建logs目录
$ mkdir logs

集群当中所有服务器，同步所有配置！（分发）

启动ZooKeeper集群

node1上启动Nimbus
$ ./bin/storm nimbus >> ./logs/nimbus.out 2>&1 &
$ tail -f logs/nimbus.log
$ ./bin/storm ui >> ./logs/ui.out 2>&1 &
$ tail -f logs/ui.log

节点node2和node3启动supervisor，按照配置，每启动一个supervisor就有了4个slots
$ ./bin/storm supervisor >> ./logs/supervisor.out 2>&1 &
$ tail -f logs/supervisor.log
（当然node1也可以启动supervisor）

http://node1:8080/
提交任务到Storm集群当中运行：
$ ./bin/storm jar examples/storm-starter/storm-starter-topologies-0.9.4.jar storm.starter.WordCountTopology test


环境变量可以配置也可以不配置
export STORM_HOME=/opt/sxt/storm
export PATH=$PATH:$STORM_HOME/bin


观察关闭一个supervisor后，nimbus的重新调度
再次启动一个新的supervisor后，观察，并rebalance



集群drpc
---------------------------------------------------
修改
$ vi conf/storm.yaml
drpc.servers:
	- "node06"

分发配置storm.yaml文件给其他节点

启动zk
主节点启动 nimbus,supervisor,drpc
从启动 supervisor