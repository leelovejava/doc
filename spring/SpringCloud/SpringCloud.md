# SpringCloud

## 性能参数配置
```
#性能测试 start
# tomcat最大排队数
server.tomcat.accept-count = 2000
# tomcat最大线程数
server.tomcat.max-threads = 2000
# tomcat最大连接数
server.tomcat.max-connections = 2000
# 调用线程允许请求HystrixCommand.GetFallback()的最大数量，默认10
# 超出时将会有异常抛出，注意：该项配置对于THREAD隔离模式也起作用
hystrix.command.default.fallback.isolation.semaphore.maxConcurrentRequests = 200
# hystrix线程池核心线程数,默认为10
hystrix.threadpool.default.coreSize = 500  
#性能测试 end
```