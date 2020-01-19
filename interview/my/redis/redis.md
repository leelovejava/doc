[Redis面试连环问](https://mp.weixin.qq.com/s/jc-RCNSvbOykSsoKmCJLow)

[为什么RedisCluster会设计成16384个槽呢?](https://blog.csdn.net/u013256816/article/details/103707927)

45.redis 是什么？都有哪些使用场景？

46.redis 有哪些功能？

47.redis 和 memecache 有什么区别？

48.redis 为什么是单线程的？

49.什么是缓存穿透？怎么解决？

50.redis 支持的数据类型有哪些？

51.redis 支持的 java 客户端都有哪些？

52.jedis 和 redisson 有哪些区别？

53.怎么保证缓存和数据库数据的一致性？

54.redis 持久化有几种方式？

55.redis 怎么实现分布式锁？

56.redis 分布式锁有什么缺陷？

57.redis 如何做内存优化？

58.redis 淘汰策略有哪些？

59.redis 常见的性能问题有哪些？该如何解决？

## 1.redis用来做什么？  模型等，频繁调用的放在redis中，取其快

Redis的特点
使用Redis有哪些好处？

## 2.redis的常用数据类型？

redis单线程为什么快?

3.redis 工作原理？

读写分离模型
数据分片模型

4.redis缓存机制?

## 5.redis支持的最大数据量是多少？redis集群下怎么从某一台集群查key-value。

## 6. redis，查看list的全量数据命令是?查看hash里边的全量数据命令?。你们用的redis什什么集群，简单的注册没有卡槽的概集群模式和哨兵模式的区别？

## 7. redis的持久化的方式有几种。java jdk 自带计数线程安全。

## 8. 缓存的淘汰策略。Redis的回收策略
MySQL里有2000w数据，redis中只存20w的数据，如何保证redis中的数据都是热点数据245

## 9. redis分布式锁是什么？怎么实现?和zookeeper相比优缺点?

## 10. Redis中key的设计特性原理？

## 11. Redis架构原理、搭建？

## 12. `memecache`与Redis的区别都有哪些？

## 13. 虚拟内存

## 14. redis常见的性能问题都有哪些？如何解决？
redis数据倾斜

## 15. Redis 最适合的场景

## 16. 多级缓存架构
[一个牛逼的多级缓存实现方案](https://mp.weixin.qq.com/s/SIv-vtMpSQqod3ou0CuGkQ)

17. [为什么RedisCluster会设计成16384个槽呢](https://github.com/antirez/redis/issues/2576)

1).如果槽位为65536，发送心跳信息的消息头达8k，发送的心跳包过于庞大。

在消息头中，最占空间的是 myslots[CLUSTER_SLOTS/8]。当槽位为65536时，这块的大小是: 65536÷8=8kb因为每秒钟，redis节点需要发送一定数量的ping消息作为心跳包，如果槽位为65536，这个ping消息的消息头太大了，浪费带宽。

2).redis的集群主节点数量基本不可能超过1000个。

如上所述，集群节点越多，心跳包的消息体内携带的数据越多。如果节点过1000个，也会导致网络拥堵。因此redis作者，不建议redis cluster节点数量超过1000个。那么，对于节点数在1000以内的redis cluster集群，16384个槽位够用了。没有必要拓展到65536个。

3).槽位越小，节点少的情况下，压缩率高。

Redis主节点的配置信息中，它所负责的哈希槽是通过一张bitmap的形式来保存的，在传输过程中，会对bitmap进行压缩，但是如果bitmap的填充率slots / N很高的话(N表示节点数)，bitmap的压缩率就很低。如果节点数很少，而哈希槽数量很多的话，bitmap的压缩率就很低。而16384÷8=2kb，怎么样，神奇不！

PS: Redis Cluster 是Redis的集群实现，内置数据自动分片机制，集群内部将所有的key映射到16384个Slot中，集群中的每个Redis Instance负责其中的一部分的Slot的读写。集群客户端连接集群中任一Redis Instance即可发送命令，当Redis Instance收到自己不负责的Slot的请求时，会将负责请求Key所在Slot的Redis Instance地址返回给客户端，客户端收到后自动将原请求重新发往这个地址，对外部透明。一个Key到底属于哪个Slot由crc16(key) % 16384 决定。