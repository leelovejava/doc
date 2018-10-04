# 分布式协调服务Zookeeper
<http://zookeeper.apache.org/>

## Welcome to Apache ZooKeeper
Apache ZooKeeper is an effort to develop and maintain an open-source server which enables highly reliable distributed coordination.

## What is ZooKeeper?
ZooKeeper is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services. All of these kinds of services are used in some form or another by distributed applications. Each time they are implemented there is a lot of work that goes into fixing the bugs and race conditions that are inevitable. Because of the difficulty of implementing these kinds of services, applications initially usually skimp on them ,which make them brittle in the presence of change and difficult to manage. Even when done correctly, different implementations of these services lead to management complexity when the applications are deployed.

 ZooKeeper 是一个开源的分布式协调服务，由雅虎创建，是 Google Chubby 的开源实现。
分布式应用程序可以基于 ZooKeeper 实现诸如数据发布/订阅、负载均衡、命名服务、分布式协
调/通知、集群管理、Master 选举、配置维护，名字服务、分布式同步、分布式锁和分布式队列
等功能。

## 基本概念
### 集群角色
Leader、Follower和Observer
![image](https://images2015.cnblogs.com/blog/581813/201706/581813-20170626010114336-754141744.jpg)
### 会话
会话就是一个客户端与服务器之间的一个TCP长连接。客户端和服务器的一切交互都是通过这个长连接进行的；

会话会在客户端与服务器断开链接后，如果经过了设点的sessionTimeout时间内没有重新链接后失效。

### 节点
节点在ZeeKeeper中包含两层含义：

集群中的一台机器，我们成为机器节点；
ZooKeeper数据模型中的数据单元，我们成为数据节点（ZNode）。
ZooKeeper的数据模型是内存中的一个ZNode数，由斜杠(/)进行分割的路径，就是一个ZNode，每个ZNode上除了保存自己的数据内容，还保存一系列属性信息；

ZooKeeper中的数据节点分为两种：持久节点和临时节点。

所谓的持久节点是指一旦这个ZNode创建成功，除非主动进行ZNode的移除操作，节点会一直保存在ZooKeeper上；而临时节点的生命周期是跟客户端的会话相关联的，一旦客户端会话失效，这个会话上的所有临时节点都会被自动移除。

### 版本
ZooKeeper为每一个ZNode节点维护一个叫做Stat的数据结构，在Stat中维护了节点相关的三个版本：

当前ZNode的版本 version
当前ZNode子节点的版本 cversion
当前ZNode的ACL(Access Control Lists)版本 aversion

### 监听器Watcher
ZooKeeper允许用户在指定节点上注册一些Watcher，并且在一些特定事件触发的时候，ZooKeeper会通过事件通知到感兴趣的客户端上。

#### ACL（Access Control Lists）
ZooKeeper中定义了5中控制权限：
    
    CREATE：创建子节点的权限
    
    READ：获取节点数据和子节点列表的权限
    
    WRITE：跟新节点数据的权限
    
    DELETE：删除子节点的权限
    
    ADMIN：设置节点ACL的权限。
    
其中CREATE和DELETE这两种权限都是针对子节点的权限控制。

## 1、Zookeeper的角色
领导者（leader），负责进行投票的发起和决议，更新系统状态。
学习者（learner），包括跟随者（follower）和观察者（observer），follower用于接受客户端请求并想客户端返回结果，在选主过程中参与投票Observer可以接受客户端连接，将写请求转发给leader，但observer不参加投票过程，只同步leader的状态，observer的目的是为了扩展系统，提高读取速度。
观察者(observer)

![image](https://mmbiz.qpic.cn/mmbiz_jpg/tuSaKc6SfPricyGrecDibXhlxebC6xeh64HMc6z4Z3paKpPZiatsWfe3icUAr0WMdZw7QwOI9BaI8dRGCodGia9IoYg/640?tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![image](https://mmbiz.qpic.cn/mmbiz_jpg/tuSaKc6SfPricyGrecDibXhlxebC6xeh64viadYdFdWdhN6Ry5uPz3E6xJ3QhCVib6zHzAic92P7M9lFjPcm7ox2bww/640?tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

• Zookeeper的核心是**原子广播**，这个机制保证了各个Server之间的同步。实现这个机制的协议叫做Zab协议。Zab协议有两种模式，它们分别是恢复模式（选主）和广播模式（同步）。当服务启动或者在领导者崩溃后，Zab就进入了恢复模式，当领导者被选举出来，且大多数Server完成了和leader的状态同步以后，恢复模式就结束了。状态同步保证了leader和Server具有相同的系统状态。

• 为了保证事务的顺序一致性，zookeeper采用了递增的事务id号（zxid）来标识事务。所有的提议（proposal）都在被提出的时候加上了zxid。实现中zxid是一个64位的数字，它高32位是epoch用来标识leader关系是否改变，每次一个leader被选出来，它都会有一个新的epoch，标识当前属于那个leader的统治时期。低32位用于递增计数。

• 每个Server在工作过程中有三种状态：

LOOKING：当前Server不知道leader是谁，正在搜寻。

LEADING：当前Server即为选举出来的leader。

FOLLOWING：leader已经选举出来，当前Server与之同步。

其他文档：http://www.cnblogs.com/lpshou/archive/2013/06/14/3136738.html



## 2、Zookeeper 的读写机制

Zookeeper是一个由多个server组成的集群

 一个leader，多个follower

每个server保存一份数据副本

全局数据一致

分布式读写

更新请求转发，由leader实施



## 3、Zookeeper 的保证　

更新请求顺序进行，来自同一个client的更新请求按其发送顺序依次执行。

 数据更新原子性，一次数据更新要么成功，要么失败。

全局唯一数据视图，client无论连接到哪个server，数据视图都是一致的。

 实时性，在一定事件范围内，client能读到最新数据。



## 4、Zookeeper节点数据操作流程
![image](https://mmbiz.qpic.cn/mmbiz_jpg/tuSaKc6SfPricyGrecDibXhlxebC6xeh64veia83nxMNQzUd7JbxqejoZIu8yiaUpQictAcxZB60qS9GpVSyGibCRqng/640?tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
注：1.在Client向Follwer发出一个写的请求

2.Follwer把请求发送给Leader

3.Leader接收到以后开始发起投票并通知Follwer进行投票

4.Follwer把投票结果发送给Leader

5.Leader将结果汇总后如果需要写入，则开始写入同时把写入操作通知给Leader，然后commit;

6.Follwer把请求结果返回给Client

• Follower主要有四个功能：

• 1. 向Leader发送请求（PING消息、REQUEST消息、ACK消息、REVALIDATE消息）；

• 2 .接收Leader消息并进行处理；

• 3 .接收Client的请求，如果为写请求，发送给Leader进行投票；

• 4 .返回Client结果。

• Follower的消息循环处理如下几种来自Leader的消息：

• 1 .PING消息： 心跳消息；

• 2 .PROPOSAL消息：Leader发起的提案，要求Follower投票；

• 3 .COMMIT消息：服务器端最新一次提案的信息；

• 4 .UPTODATE消息：表明同步完成；

• 5 .REVALIDATE消息：根据Leader的REVALIDATE结果，关闭待revalidate的session还是允许其接受消息；

• 6 .SYNC消息：返回SYNC结果到客户端，这个消息最初由客户端发起，用来强制得到最新的更新。



## 5、Zookeeper leader 选举　

• 半数通过

– 3台机器 挂一台 2>3/2

– 4台机器 挂2台 2！>4/2

• A提案说，我要选自己，B你同意吗？C你同意吗？B说，我同意选A；C说，我同意选A。(注意，这里超过半数了，其实在现实世界选举已经成功了。但是计算机世界是很严格，另外要理解算法，要继续模拟下去。)

• 接着B提案说，我要选自己，A你同意吗；A说，我已经超半数同意当选，你的提案无效；C说，A已经超半数同意当选，B提案无效。

• 接着C提案说，我要选自己，A你同意吗；A说，我已经超半数同意当选，你的提案无效；B说，A已经超半数同意当选，C的提案无效。

• 选举已经产生了Leader，后面的都是follower，只能服从Leader的命令。而且这里还有个小细节，就是其实谁先启动谁当头。
![image](https://mmbiz.qpic.cn/mmbiz_jpg/tuSaKc6SfPricyGrecDibXhlxebC6xeh64oPUtLLHFzqv90LnCgsVUHL9g9rNrH3JQcZBEWmajKnFQAwnhdj9UeQ/640?tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
![image](https://mmbiz.qpic.cn/mmbiz_jpg/tuSaKc6SfPricyGrecDibXhlxebC6xeh64icHiaJT8mibvCBt631JWBqpWAwBBXjq8RNKtu26ic91ZxfLTeKQVzuWhaQ/640?tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

## 6、zxid
• znode节点的状态信息中包含czxid, 那么什么是zxid呢?

• ZooKeeper状态的每一次改变, 都对应着一个递增的Transaction id, 该id称为zxid. 由于zxid的递增性质, 如果zxid1小于zxid2, 那么zxid1肯定先于zxid2发生.

创建任意节点, 或者更新任意节点的数据, 或者删除任意节点, 都会导致Zookeeper状态发生改变, 从而导致zxid的值增加.



## 7、Zookeeper工作原理

Zookeeper的核心是原子广播，这个机制保证了各个server之间的同步。实现这个机制的协议叫做Zab协议。Zab协议有两种模式，它们分别是恢复模式和广播模式。

当服务启动或者在领导者崩溃后，Zab就进入了恢复模式，当领导者被选举出来，且大多数server的完成了和leader的状态同步以后，恢复模式就结束了。

状态同步保证了leader和server具有相同的系统状态，一旦leader已经和多数的follower进行了状态同步后，他就可以开始广播消息了，即进入广播状态。这时候当一个server加入zookeeper服务中，它会在恢复模式下启动，

发现leader，并和leader进行状态同步。待到同步结束，它也参与消息广播。Zookeeper服务一直维持在Broadcast状态，直到leader崩溃了或者leader失去了大部分的followers支持。

广播模式需要保证proposal被按顺序处理，因此zk采用了递增的事务id号(zxid)来保证。所有的提议(proposal)都在被提出的时候加上zxid。

实现中zxid是一个64为的数字，它高32位是epoch用来标识leader关系是否改变，每次一个leader被选出来，它都会有一个新的epoch。低32位是个递增计数。

当leader崩溃或者leader失去大多数的follower，这时候zk进入恢复模式，恢复模式需要重新选举出一个新的leader，让所有的server都恢复到一个正确的状态。

每个Server启动以后都询问其它的Server它要投票给谁。

对于其他server的询问，server每次根据自己的状态都回复自己推荐的leader的id和上一次处理事务的zxid（系统启动时每个server都会推荐自己）

收到所有Server回复以后，就计算出zxid最大的哪个Server，并将这个Server相关信息设置成下一次要投票的Server。

计算这过程中获得票数最多的的sever为获胜者，如果获胜者的票数超过半数，则改server被选为leader。否则，继续这个过程，直到leader被选举出来。

 leader就会开始等待server连接。

Follower连接leader，将最大的zxid发送给leader。

 Leader根据follower的zxid确定同步点。

 完成同步后通知follower 已经成为uptodate状态。

Follower收到uptodate消息后，又可以重新接受client的请求进行服务了。



## 8、数据一致性与paxos 算法
• 据说Paxos算法的难理解与算法的知名度一样令人敬仰，所以我们先看如何保持数据的一致性，这里有个原则就是：

• 在一个分布式数据库系统中，如果各节点的初始状态一致，每个节点都执行相同的操作序列，那么他们最后能得到一个一致的状态。

• Paxos算法解决的什么问题呢，解决的就是保证每个节点执行相同的操作序列。好吧，这还不简单，master维护一个全局写队列，所有写操作都必须 放入这个队列编号，那么无论我们写多少个节点，只要写操作是按编号来的，就能保证一致性。没错，就是这样，可是如果master挂了呢。

• Paxos算法通过投票来对写操作进行全局编号，同一时刻，只有一个写操作被批准，同时并发的写操作要去争取选票，只有获得过半数选票的写操作才会被 批准（所以永远只会有一个写操作得到批准），其他的写操作竞争失败只好再发起一轮投票，就这样，在日复一日年复一年的投票中，所有写操作都被严格编号排 序。编号严格递增，当一个节点接受了一个编号为100的写操作，之后又接受到编号为99的写操作（因为网络延迟等很多不可预见原因），它马上能意识到自己 数据不一致了，自动停止对外服务并重启同步过程。任何一个节点挂掉都不会影响整个集群的数据一致性（总2n+1台，除非挂掉大于n台）。



总结

 Zookeeper 作为 Hadoop 项目中的一个子项目，是 Hadoop 集群管理的一个必不可少的模块，它主要用来控制集群中的数据，如它管理 Hadoop 集群中的 NameNode，还有 Hbase 中 Master Election、Server 之间状态同步等。

关于Paxos算法可以查看文章 Zookeeper全解析——Paxos作为灵魂



## 9、Observer　
• Zookeeper需保证高可用和强一致性；

• 为了支持更多的客户端，需要增加更多Server；

• Server增多，投票阶段延迟增大，影响性能；

• 权衡伸缩性和高吞吐率，引入Observer

• Observer不参与投票；

• Observers接受客户端的连接，并将写请求转发给leader节点；

• 加入更多Observer节点，提高伸缩性，同时不影响吞吐率



## 10、 为什么zookeeper集群的数目，一般为奇数个？
•Leader选举算法采用了Paxos协议；

•Paxos核心思想：当多数Server写成功，则任务数据写成功如果有3个Server，则两个写成功即可；如果有4或5个Server，则三个写成功即可。

•Server数目一般为奇数（3、5、7）如果有3个Server，则最多允许1个Server挂掉；如果有4个Server，则同样最多允许1个Server挂掉由此，

我们看出3台服务器和4台服务器的的容灾能力是一样的，所以为了节省服务器资源，一般我们采用奇数个数，作为服务器部署个数。



## 11、Zookeeper 的数据模型　
![image](https://images2015.cnblogs.com/blog/581813/201706/581813-20170626005312757-1275647224.jpg)
层次化的目录结构，命名符合常规文件系统规范。

每个节点在zookeeper中叫做znode,并且其有一个唯一的路径标识。

节点Znode可以包含数据和子节点，但是EPHEMERAL类型的节点不能有子节点。

Znode中的数据可以有多个版本，比如某一个路径下存有多个数据版本，那么查询这个路径下的数据就需要带上版本。

客户端应用可以在节点上设置监视器。

节点不支持部分读写，而是一次性完整读写。



## 12、Zookeeper 的节点
Znode有两种类型，短暂的（ephemeral）和持久的（persistent）
Znode的类型在创建时确定并且之后不能再修改
短暂znode的客户端会话结束时，zookeeper会将该短暂znode删除，短暂znode不可以有子节点
持久znode不依赖于客户端会话，只有当客户端明确要删除该持久znode时才会被删除
- - -
Znode有四种形式的节点
* PERSISTENT（永久）
* EPHEMERAL(短暂)
* PERSISTENT_SEQUENTIAL（永久顺序）
* EPHEMERAL_SEQUENTIAL（短暂顺序）

## 13、应用场景
### 1.数据发布与订阅（配置中心）
 数据发布与订阅，即所谓的配置中心，顾名思义就是发布者将数据发布到 ZooKeeper 节点上,
供订阅者进行数据订阅，进而达到动态获取数据的目的，实现配置信息的集中式管理和动态更新。

    对于：数据量通常比较小。数据内容在运行时动态变化。集群中各机器共享，配置一致。
这样的全局配置信息就可以发布到 ZooKeeper上，让客户端（集群的机器）去订阅该消息。

发布/订阅系统一般有两种设计模式，分别是推（Push）和拉（Pull）模式。
      - 推模式
          服务端主动将数据更新发送给所有订阅的客户端
      - 拉模式
          客户端主动发起请求来获取最新数据，通常客户端都采用定时轮询拉取的方式

ZooKeeper 采用的是推拉相结合的方式：
    客户端想服务端注册自己需要关注的节点，一旦该节点的数据发生变更，那么服务端就会向相应
的客户端发送Watcher事件通知，客户端接收到这个消息通知后，需要主动到服务端获取最新的数据
### 2.命名服务
 命名服务也是分布式系统中比较常见的一类场景。在分布式系统中，通过使用命名服务，客户端
应用能够根据指定名字来获取资源或服务的地址，提供者等信息。被命名的实体通常可以是集群中的
机器，提供的服务，远程对象等等——这些我们都可以统称他们为名字。

其中较为常见的就是一些分布式服务框架（如RPC）中的服务地址列表。通过在ZooKeepr里
创建顺序节点，能够很容易创建一个全局唯一的路径，这个路径就可以作为一个名字。

ZooKeeper 的命名服务即生成全局唯一的ID
### 3.分布式协调服务/通知
ZooKeeper 中特有 Watcher 注册与异步通知机制，能够很好的实现分布式环境下不同机器，
甚至不同系统之间的通知与协调，从而实现对数据变更的实时处理。使用方法通常是不同的客户端
如果 机器节点 发生了变化，那么所有订阅的客户端都能够接收到相应的Watcher通知，并做出相应
的处理。

ZooKeeper的分布式协调/通知，是一种通用的分布式系统机器间的通信方式。
### 4.Master选举

![image](http://www.aboutyun.com/data/attachment/forum/201608/20/184729rximw9ziz9zeclxd.png)

![image](http://www.aboutyun.com/data/attachment/forum/201608/20/184755snznkmmh7at9nti9.png)

Master 选举可以说是 ZooKeeper 最典型的应用场景了。比如 HDFS 中 Active NameNode 的选举、YARN 中 Active ResourceManager 的选举和 HBase 中 Active HMaster 的选举等
 针对 Master 选举的需求，通常情况下，我们可以选择常见的关系型数据库中的主键特性来
实现：希望成为 Master 的机器都向数据库中插入一条相同主键ID的记录，数据库会帮我们进行
主键冲突检查，也就是说，只有一台机器能插入成功——那么，我们就认为向数据库中成功插入数据
的客户端机器成为Master。

    依靠关系型数据库的主键特性确实能够很好地保证在集群中选举出唯一的一个Master。

    但是，如果当前选举出的 Master 挂了，那么该如何处理？谁来告诉我 Master 挂了呢？
显然，关系型数据库无法通知我们这个事件。但是，ZooKeeper 可以做到！

    利用 ZooKeepr 的强一致性，能够很好地保证在分布式高并发情况下节点的创建一定能够
保证全局唯一性，即 ZooKeeper 将会保证客户端无法创建一个已经存在的 数据单元节点。
    也就是说，如果同时有多个客户端请求创建同一个临时节点，那么最终一定只有一个客户端
请求能够创建成功。利用这个特性，就能很容易地在分布式环境中进行 Master 选举了。

    成功创建该节点的客户端所在的机器就成为了 Master。同时，其他没有成功创建该节点的
客户端，都会在该节点上注册一个子节点变更的 Watcher，用于监控当前 Master 机器是否存
活，一旦发现当前的Master挂了，那么其他客户端将会重新进行 Master 选举。

    这样就实现了 Master 的动态选举。
### 5.分布式锁
分布式锁是控制分布式系统之间同步访问共享资源的一种方式 
分布式锁又分为排他锁和共享锁两种
![image](http://www.aboutyun.com/data/attachment/forum/201608/20/184557iv77xzbyas7bc99o.png)
#### 排它锁 
   ZooKeeper如何实现排它锁？
定义锁 
ZooKeeper 上的一个 机器节点 可以表示一个锁
获得锁 
 把ZooKeeper上的一个节点看作是一个锁，获得锁就通过创建临时节点的方式来实现。 
 ZooKeeper 会保证在所有客户端中，最终只有一个客户端能够创建成功，那么就可以 
 认为该客户端获得了锁。同时，所有没有获取到锁的客户端就需要到/exclusive_lock 
 节点上注册一个子节点变更的Watcher监听，以便实时监听到lock节点的变更情况。
释放锁 
 因为锁是一个临时节点，释放锁有两种方式
    当前获得锁的客户端机器发生宕机或重启，那么该临时节点就会被删除，释放锁
    正常执行完业务逻辑后，客户端就会主动将自己创建的临时节点删除，释放锁
无论在什么情况下移除了lock节点，ZooKeeper 都会通知所有在 /exclusive_lock 节点上注册了节点变更 Watcher 监听的客户端。这些客户端在接收到通知后，再次重新发起分布式锁获取，即重复『获取锁』过程

#### 共享锁
共享锁在同一个进程中很容易实现，但是在跨进程或者在不同 Server 之间就不好实现了。Zookeeper 却很容易实现这个功能，实现方式也是需要获得锁的 Server 创建一个 EPHEMERAL_SEQUENTIAL 目录节点，然后调用 getChildren方法获取当前的目录节点列表中最小的目录节点是不是就是自己创建的目录节点，如果正是自己创建的，那么它就获得了这个锁，如果不是那么它就调用 exists(String path, boolean watch) 方法并监控 Zookeeper 上目录节点列表的变化，一直到自己创建的节点是列表中最小编号的目录节点，从而获得锁，释放锁很简单，只要删除前面它自己所创建的目录节点就行了