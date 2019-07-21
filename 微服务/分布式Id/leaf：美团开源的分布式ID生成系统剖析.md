## leaf：美团开源的分布式ID生成系统剖析

> 说明，本文基于谢照东的《Leaf：美团点评分布式ID生成系统》，之所以有这样文章，是因为笔者发现谢照东的这篇文章和美团开源的leaf（GitHub地址：https://github.com/Meituan-Dianping/Leaf）是有一些非常重要的出入的，尤其在涉及时钟回拨等问题。所以，笔者根据美团开源的leaf源码，写下了这篇文章。

# 简介

为什么叫`leaf`？因为天底下没有两片完全一样的树叶（德国哲学家、数学家莱布尼茨：There are no two identical leaves in the world），意味着每次通过leaf获取的ID肯定是唯一的。

首先，简单介绍一下如何使用leaf。

- 配置

leaf是基于springboot、以HTTP协议的方式提供获取分布式唯一ID的服务。总计有两种模式：**Snowflake**和**Segment**。我们通过它的核心配置文件leaf.properties可知：

```properties
# segment(号段)模式开关，这种模式依赖数据库
# leaf 服务名
leaf.name=afei
# 是否开启号段模式,默认false
leaf.segment.enable=true
# mysql 库地址
leaf.jdbc.url=jdbc:mysql://localhost:3306/leaf
# mysql 用户名
leaf.jdbc.username=afei
# mysql 密码
leaf.jdbc.password=afei

# snowflake模式，这种模式依赖zookeeper
# 是否开启snowflake模式,默认false
leaf.snowflake.enable=true
# snowflake模式下的zk地址
leaf.snowflake.zk.address=127.0.0.1:2181
# snowflake模式下的服务注册端口
leaf.snowflake.port=8686
```



- DML

首先创建表leaf_alloc（DDL语句在GitHub首页可以找到）：

```sql
CREATE DATABASE leaf
CREATE TABLE `leaf_alloc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `biz_tag` varchar(128)  NOT NULL DEFAULT '',
  `max_id` bigint(20) NOT NULL DEFAULT '1',
  `step` int(11) NOT NULL,
  `description` varchar(256)  DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`biz_tag`)
) ENGINE=InnoDB;
```

然后需要插入几条初始化数据，假设支付服务和账户服务需要调用leaf获取唯一ID，那么初始化数据的SQL如下：

```sql
insert into leaf_alloc(biz_tag, max_id, step, description) values('pay', 1, 2000, 'leaf'),('account', 1, 2000, 'leaf');
```



- 启动

由于leaf基于springboot开发，所以启动它非常简单，运行`com.sankuai.inf.leaf.server.LeafServerApplication`即可。

- 访问

Snowflake和Segment两种获取唯一ID的模式的访问方式如下：

```properties
# Segment模式获取唯一ID
http://localhost:8080/api/segment/get/pay
http://localhost:8080/api/segment/get/account

# Snowflake模式获取唯一ID
http://localhost:8080/api/snowflake/get/pay
http://localhost:8080/api/snowflake/get/account
```

如果使用号段模式，需要建立DB表，并配置`leaf.jdbc.url, leaf.jdbc.username`, `leaf.jdbc.password`

如果不想使用该模式配置`leaf.segment.enable=false`即可

- 监控

leaf提供了非常简单的监控页面，能够让用户看到Segment模式下有哪些服务，以及这些服务当前获取唯一ID的位置。这个监控页面事实上就是可视化本地内存中的数据，所以一定要在leaf启动后**获取过至少一次唯一ID**，其对应的数据才能正常展示，否则都是0。访问地址是 http://localhost:8080/cache，访问结果如下：

![leaf-cache监控](https://mmbiz.qpic.cn/mmbiz_png/4o22OFcmzHm6GN35B8qVXerALKEqE73IrRal0icz9icZFicn3ok5SkNzYLoheNqoWnicotqWXP2HUjCicicCSDk7luQg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)leaf-cache监控

接下来分别对leaf提供的两种模式进行深入剖析。

# Segment模式

前面已经提到，Segment模式依赖数据库。认识leaf的Segment模式之前，我们假设在不考虑并发能力的情况下如何通过数据库获取唯一ID：**利用MySQL的自增主键**，而且MyBatis可以获取每次insert的逐渐ID。这种方案的

**优点**如下：

- 非常简单，利用现有数据库系统的功能实现，成本小，有DBA专业维护。
- ID号单调自增，可以实现一些对ID有特殊要求的业务。

**缺点**如下：

- 强依赖DB，当DB异常时整个系统不可用，属于致命问题。配置主从复制可以尽可能的增加可用性，但是数据一致性在特殊情况下难以保证。主从切换时的不一致可能会导致重复发号。
- ID发号性能瓶颈限制在单台MySQL的读写性能。

Segment有“**段**”的意思，leaf这种模式意味着并不是每次获取唯一ID都需要操作数据库。所以，Segment模式就是在前面提到的数据库方案基础之上进行了优化：既然每次操作数据库性能有问题，那么我就**每过N次才操作一次数据库**。在这N次以内的访问，都只需要通过操作本地缓存获取。如此一来，性能就很高了。这个N值表示的范围就是Segment的意思。

不过还是有点瑕疵，因为每过N次都要操作一次数据库。如果恰好在这个时候并发较高，那么数据库操作就会阻塞，甚至出现超时，从而形成性能毛刺甚至降低SLA。怎么办？leaf的做法是将这个步骤**提前并异步化**，leaf的Segment模式用一张图表示如下：

![leaf segment](https://mmbiz.qpic.cn/mmbiz_png/4o22OFcmzHm6GN35B8qVXerALKEqE73Ic13lmcPEuZstNM2MtGWWk9WVTmHYQYKJs7qmeuWpBxMVmSCian1uhBA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)leaf segment

如图所示：

1. 设置step为1000（可通过DML配置）；
2. 当发号期已经分发到100的时候（即取了步长step的10%），就会触发一个异步操作去初始化下一个号段；
3. 当1～1000用尽并且分配到1100的时候，又会触发异步操作去初始化又1个号段（2001～3000），以此类推；

通过这种方案设计，每次获取唯一ID都只需要操作内存即可。至于对数据库的操作，完全异步完成。

- 一些疑问点

**这种方案是如何避免重启后分配重复的ID呢？**我们假设已经分配到1122，这个1122又没有持久化到任何一个地方，数据库中只保存了（max_id=2001, step=1000），如果这时候leaf宕机。leaf的做法是在第一次获取唯一ID的时候，会首先更新数据库跳到下一个号段（max_id=3001, step=1000），那么这时候获取的唯一ID就是2001，至于1123～2000之前的ID全部被抛弃，不会被分配了。

**Segment模式有时钟回拨问题吗？**很明显没有，因为通过这种模式获取的ID没有任何时间属性，所以不存在时钟回拨问题。

**新增服务需要多久生效？**假设我们通过SQL语句插入一个订单服务，那么要过多久订单服务才能通过请求地址http://localhost:8080/api/segment/get/order向leaf获取唯一ID呢？

```
insert into afei.leaf_alloc values('order', 1, 1000, 'leaf', now());
```

答案是**最多60s**，因为leaf有一个schedule任务，间隔60s刷新本地缓存中的服务信息，假设刷新前只有[pay，account]两个服务，那么刷新后就有[pay，account，order]三个服务。核心源码如下：

```java
private void updateCacheFromDbAtEveryMinute() {
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("check-idCache-thread");
            t.setDaemon(true);
            return t;
        }
    });
    service.scheduleWithFixedDelay(new Runnable() {
        @Override
        public void run() {
            updateCacheFromDb();
        }
    }, 60, 60, TimeUnit.SECONDS);
}

private void updateCacheFromDb() {
    logger.info("update cache from db");
    try {
        List<String> dbTags = dao.getAllTags();
        // 这里就会更新本地缓存中的接入服务列表信息（即leaf_alloc表中的biz_tag字段）
        ...
    }
    ...
}
```

# Snowflake模式

leaf的Snowflake模式，顾名思义，源自于twitter的Snowflake算法，其64位构成如下，leaf的Snowflake模式与原生Snowflake模式**完全一致**，都是采用1+41+10+12的模式，且不可配置，除非修改源码：

![snowflake mode](https://mmbiz.qpic.cn/mmbiz_png/4o22OFcmzHm6GN35B8qVXerALKEqE73IKSPc59HX491kMv10wMlbpAXa51QLicuqwbkRHud8hnm3kxEt9YKR1AA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)snowflake mode

leaf的Snowflake模式核心源码在com.sankuai.inf.leaf.snowflake.SnowflakeIDGenImpl中。接下来，我们看看该模式下，获取唯一id时调用的方法get(String)的核心源码（部分省略）：

```java
// synchronized保证线程安全问题
public synchronized Result get(String key) {
    long timestamp = System.currentTimeMillis();
    // 如果时钟发生了回拨
    if (timestamp < lastTimestamp) {
        long offset = lastTimestamp - timestamp;
        if (offset <= 5) {
            // 如果回拨的时间在5ms以内，那么直接等待
            wait(offset << 1);
            timestamp = System.currentTimeMillis();
        } else {
            // 如果超过5ms，那么直接抛出异常
            return new Result(-3, Status.EXCEPTION);
        }
    }
    // 如果和上一次请求是同一毫秒以内，那么sequence+1
    if (lastTimestamp == timestamp) {
        sequence = (sequence + 1) & sequenceMask;
        if (sequence == 0) {
            //sequence为0的时候表示这一毫秒请求量超过1024，那么自旋等待下一毫秒
            sequence = RANDOM.nextInt(100);
            timestamp = tilNextMillis(lastTimestamp);
        }
    } else {
        //如果是新的一毫秒，那么从一个[0, 100)的随机数开始，之所以不是每次都从0开始，是因为防止低并发时获取的唯一ID都是偶数，如果用唯一ID作为分片键，可能导致数据倾斜
        sequence = RANDOM.nextInt(100);
    }
    lastTimestamp = timestamp;
    // 通过位运算计算此次生成的唯一ID
    long id = ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
    return new Result(id, Status.SUCCESS);

}
```

由这段源码我们可知，**leaf的Snowflake模式并没有彻底解决时钟回拨的问题**。当运行过程中，如果时钟回拨超过5ms，依然会抛出异常。

那么，Snowflake模式主要解决什么问题？很明显，是**snowflake中的workerId部分**。当需要启动的leaf服务越来越多时，对其分配workerId是一件非常令人头疼的事情。我们要做的是，尽量让一件事情简单化，让用户无感知。百度的UID做到了（文末有相关阅读链接），leaf也做到了！

leaf的Snowflake模式是怎么做到的呢？很简单，通过zookeeper的**PERSISTENT_SEQUENTIAL**类型节点为每一个leaf实例生成一个递增的workerId。以总计部署4个leaf实例为例：第1个leaf实例的workerId为0，且根据该实例的IP地址和配置的Port值，即使接下来重启，workerId仍然为0；第2个leaf实例的workerId为1；第3个leaf实例的workerId为2；第4个leaf实例的workerId为3，以此类推。leaf持久化在zookeeper中的数据如下所示：

```shell
/snowflake/afei/forever/
    |--192.168.0.1
        |--0     
    |--192.168.0.2
        |--1 
    |--192.168.0.3
        |--2 
```

我们可以看到这些数据的路径是：`/snowflake/${leaf.name}/forever/${ip}:${port}`。如此一来，对于所有部署的leaf实例，其获取到的workerId只跟它的ip和port有关。当然，由于其workerId占10位，所以，理论上Leaf服务实例数可以达到1024个（很明显，这个实例数上限几乎能够满足任何业务场景）。

最后，leaf会定期（间隔周期是3秒）上报更新timestamp。并且上报时，如果发现当前时间戳少于最后一次上报的时间戳，那么会放弃上报。之所以这么做的原因是，**防止在leaf实例重启过程中，由于时钟回拨导致可能产生重复ID的问题**：

```java
private void ScheduledUploadData(final CuratorFramework curator, final String zk_AddressNode) {
    Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "schedule-upload-time");
            thread.setDaemon(true);
            return thread;
        }
    }).scheduleWithFixedDelay(new Runnable() {
        @Override
        public void run() {
            updateNewData(curator, zk_AddressNode);
        }
    }, 1L, 3L, TimeUnit.SECONDS);//每3s上报数据

}
```

产生重复ID的场景：**假设**leaf不定期上报当前时间戳，那么可能会发生这种情况导致产生重复的ID。假设leaf重启前最后一次生成的唯一ID是（2019-05-31 08:15:00 + 12 + 168），如果这时候leaf重启，并且在启动之前，发生了时钟回拨（假设回拨了20s，并且leaf实例启动花了10s），那么在该leaf实例重启后，生成的ID是（2019-05-31 08:14:50 + 12 + 168）。很明显的是，这个ID以及接下来10秒内生成的ID，都**很可能**是之前已经生成过的ID。

我们再看一下谢照东在文章《Leaf：美团点评分布式ID生成系统》中，leaf在snowflake模式下的流程图：

![leaf with zk](https://mmbiz.qpic.cn/mmbiz_png/4o22OFcmzHm6GN35B8qVXerALKEqE73IvGZom5tkeHKWPWVBDZjy3vzlAyOdGVuzGEicRAZUCFq688wzZvzia4xw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)leaf with zk

前面一部分逻辑（即leaf启动时，根据自己的ip和port从zk上获取workerId）是和GitHub上开源代码一致的，但是笔者在图中用红色框住的就略有不同的：

1. 校验每次周期性上传的本机时间必须大于以前上传的时间，如果失败，并没有任何动作（只是简单的return）。
2. 校验每次周期性上传的本机时间必须大于以前上传的时间，如果成功，直接上报。并不会得到leaf_temporary下所有节点，然后得到各个正在服务的机器时间并做平均值校验。

这块逻辑对应的源码就是ScheduledUploadData()中调用的方法updateNewData(curator, zk_AddressNode)，其源码如下：

```java
private void updateNewData(CuratorFramework curator, String path) {
    try {
        if (System.currentTimeMillis() < lastUpdateTime) {
            return;
        }
        curator.setData().forPath(path, buildData().getBytes());
        lastUpdateTime = System.currentTimeMillis();
    } catch (Exception e) {
        LOGGER.info("update init data error path is {} error is {}", path, e);
    }
}
```

笔者尝试找**谢照东**找到存在这样差异性的答案，因为笔者猜测可能是开源版本和美团内部版本存在出入。但是，毕竟时间过了很久，谢照东大神也记不大清晰了，可惜可惜！

剖析leaf，就不得不提百度的uid，uid是开源分布式ID方案中，最彻底解决时钟回拨的方案。我和东皇探讨过程中，东皇也很欣赏和推荐使用该方案。想要了解uid的同学，请戳链接：[UidGenerator：百度开源的分布式ID服务（解决了时钟回拨问题）](https://mp.weixin.qq.com/s/8NsTXexf03wrT0tsW24EHA)

# Leaf Core

当然，为了追求更高的性能，需要通过RPC Server来部署Leaf 服务，那仅需要引入leaf-core的包，把生成ID的API封装到指定的RPC框架中即可