# redis
## doc

[一文读懂 Redis 基础、高级特性与性能调优](https://mp.weixin.qq.com/s/JLlMkCLxTQA4GU9j9g5Xew)

[同程凤凰缓存系统基于Redis的设计与实践](https://mp.weixin.qq.com/s/2FEbkas_m1WnYUqjVpMkWw)

[Redis 开发设计规范及案例分享](https://mp.weixin.qq.com/s/NICQmkAoe7jEo6vAEZJQRQ)

[史上最全 50 道 Redis 面试题](https://mp.weixin.qq.com/s/gB7oMjygGFF5M7WwTgrn4g)

[Redis5：抛弃ruby基于redis-cli创建集群&集群模式的得与失](https://mp.weixin.qq.com/s/csQE3RV17uZsAXwS4mI1Tw)


## redis延迟队列

https://blog.csdn.net/wizard_rp/article/details/79303623

https://github.com/Yampery/rdsmq

[基于Redis实现延时队列服务](https://www.cnblogs.com/lylife/p/7881950.html)

[分布式之延时任务方案解析](https://www.cnblogs.com/rjzheng/p/8972725.html)

rabbitMQ延迟队列
https://blog.csdn.net/zhu_tianwei/article/details/53563311


延时队列就是一种带有延迟功能的消息队列。下面会介绍几种目前已有的延时队列：
1.Java中java.util.concurrent.DelayQueue
优点：JDK自身实现，使用方便，量小适用
缺点：队列消息处于jvm内存，不支持分布式运行和消息持久化
2.Rocketmq延时队列
优点：消息持久化，分布式
缺点：不支持任意时间精度，只支持特定level的延时消息
3.Rabbitmq延时队列（TTL+DLX实现）
优点：消息持久化，分布式
缺点：延时相同的消息必须扔在同一个队列

根据自身业务和公司情况，如果实现一个自己的延时队列服务需要考虑一下几点:

* 消息存储
* 过期延时消息实时获取
* 高可用性