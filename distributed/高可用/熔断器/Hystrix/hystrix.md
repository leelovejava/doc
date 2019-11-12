
单个接口,设置超时时间,加在方法上
```java
// 1).execution.isolation.thread.timeoutInMilliseconds
// 设置调用者等待命令执行的超时限制，超过此时间，HystrixCommand被标记为TIMEOUT，并执行回退逻辑
// 2).execution.timeout.enabled:设置HystrixCommand.run()的执行是否有超时限制,默认值：true
@HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1800000"),
            @HystrixProperty(name = "execution.timeout.enabled", value = "false")},fallbackMethod = "handleSingleZipFileUploadOutTime")
```