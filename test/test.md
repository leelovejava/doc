# 问题排查

[快速测试 API 接口的新技能](http://blog.720ui.com/2018/restclient_use/)


fiddler调试

> urlreplace 192.168.11.140:25010 127.0.0.1:25010

[Fiddler 高级用法：Fiddler Script 与 HTTP 断点调试](https://blog.csdn.net/qq_21051503/article/details/50678030)

[一份超详细的 Java 问题排查工具单](https://mp.weixin.qq.com/s/9LqlzIqg0fFUcgOIzzblUg)

### jar包冲突

1. 打出所有依赖

> mvn dependency:tree > ~/dependency.txt

2. 只打出指定groupId和artifactId的依赖关系

> mvn dependency:tree -Dverbose -Dincludes=groupId:artifactId

### [RateLimiter限流](https://www.cnblogs.com/yeyinfu/p/7316972.html)   