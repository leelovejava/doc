## 如何设置 Flink Job RestartStrategy（重启策略）？

从使用 Flink 到至今，遇到的 Flink 有很多，解决的问题更多（含帮助微信好友解决问题），所以对于 Flink 可能遇到的问题及解决办法都比较清楚，那么在这章就给大家讲解下几个 Flink 中比较常遇到的问题的解决办法。

### Flink Job 常见重启错误

不知道大家是否有遇到过这样的问题：整个 Job 一直在重启，并且还会伴随着一些错误（可以通过 UI 查看 Exceptions 日志），以下是笔者遇到过的一些问题截图：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-04-152844.png)

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-06-140519.png)

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-09-26-2019-05-14_00-59-25.png)

其实遇到上面这种问题比较常见的，比如有时候因为数据的问题（不合规范、为 null 等），这时在处理这些脏数据的时候可能就会遇到各种各样的异常错误，比如空指针、数组越界、数据类型转换错误等。可能你会说只要过滤掉这种脏数据就行了，或者进行异常捕获就不会导致 Job 不断重启的问题了。

确实如此，如果做好了脏数据的过滤和异常的捕获，Job 的稳定性确实有保证，但是复杂的 Job 下每个算子可能都会产生出脏数据（包含源数据可能也会为空或者不合法的数据），你不可能在每个算子里面也用一个大的 try catch 做一个异常捕获，所以脏数据和异常简直就是防不胜防，不过我们还是要尽力的保证代码的健壮性，但是也要配置好 Flink Job 的 RestartStrategy（重启策略）。

### RestartStrategy 介绍

RestartStrategy，重启策略，在遇到机器或者代码等不可预知的问题时导致 Job 或者 Task 挂掉的时候，它会根据配置的重启策略将 Job 或者受影响的 Task 拉起来重新执行，以使得作业恢复到之前正常执行状态。Flink 中的重启策略决定了是否要重启 Job 或者 Task，以及重启的次数和每次重启的时间间隔。

### 为什么需要 RestartStrategy？

重启策略会让 Job 从上一次完整的 Checkpoint 处恢复状态，保证 Job 和挂之前的状态保持一致，另外还可以让 Job 继续处理数据，不会出现 Job 挂了导致消息出现大量堆积的问题，合理的设置重启策略可以减少 Job 不可用时间和避免人工介入处理故障的运维成本，因此重启策略对于 Flink Job 的稳定性来说有着举足轻重的作用。

### 怎么配置 RestartStrategy？

既然 Flink 中的重启策略作用这么大，那么该如何配置呢？其实如果 Flink Job 没有单独设置重启重启策略的话，则会使用集群启动时加载的默认重启策略，如果 Flink Job 中单独设置了重启策略则会覆盖默认的集群重启策略。默认重启策略可以在 Flink 的配置文件 `flink-conf.yaml` 中设置，由 `restart-strategy` 参数控制，有 fixed-delay（固定延时重启策略）、failure-rate（故障率重启策略）、none（不重启策略）三种可以选择，如果选择的参数不同，对应的其他参数也不同。下面分别介绍这几种重启策略和如何配置。

#### FixedDelayRestartStrategy（固定延时重启策略）

FixedDelayRestartStrategy 是固定延迟重启策略，程序按照集群配置文件中或者程序中额外设置的重启次数尝试重启作业，如果尝试次数超过了给定的最大次数，程序还没有起来，则停止作业，另外还可以配置连续两次重启之间的等待时间，在 `flink-conf.yaml` 中可以像下面这样配置。

```yaml
restart-strategy: fixed-delay
restart-strategy.fixed-delay.attempts: 3  #表示作业重启的最大次数，启用 checkpoint 的话是 Integer.MAX_VALUE，否则是 1。
restart-strategy.fixed-delay.delay: 10 s  #如果设置分钟可以类似 1 min，该参数表示两次重启之间的时间间隔，当程序与外部系统有连接交互时延迟重启可能会有帮助，启用 checkpoint 的话，延迟重启的时间是 10 秒，否则使用 akka.ask.timeout 的值。
```

在程序中设置固定延迟重启策略的话如下：

```java
ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
  3, // 尝试重启的次数
  Time.of(10, TimeUnit.SECONDS) // 延时
));
```

#### FailureRateRestartStrategy（故障率重启策略）

FailureRateRestartStrategy 是故障率重启策略，在发生故障之后重启作业，如果固定时间间隔之内发生故障的次数超过设置的值后，作业就会失败停止，该重启策略也支持设置连续两次重启之间的等待时间。

```yaml
restart-strategy: failure-rate
restart-strategy.failure-rate.max-failures-per-interval: 3  #固定时间间隔内允许的最大重启次数，默认 1
restart-strategy.failure-rate.failure-rate-interval: 5 min  #固定时间间隔，默认 1 分钟
restart-strategy.failure-rate.delay: 10 s #连续两次重启尝试之间的延迟时间，默认是 akka.ask.timeout 
```

可以在应用程序中这样设置来配置故障率重启策略：

```java
ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
env.setRestartStrategy(RestartStrategies.failureRateRestart(
  3, // 固定时间间隔允许 Job 重启的最大次数
  Time.of(5, TimeUnit.MINUTES), // 固定时间间隔
  Time.of(10, TimeUnit.SECONDS) // 两次重启的延迟时间
));
```

#### NoRestartStrategy（不重启策略）

NoRestartStrategy 作业不重启策略，直接失败停止，在 `flink-conf.yaml` 中配置如下：

```yaml
restart-strategy: none
```

在程序中如下设置即可配置不重启：

```java
ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
env.setRestartStrategy(RestartStrategies.noRestart());
```

#### Fallback（备用重启策略）

如果程序没有启用 Checkpoint，则采用不重启策略，如果开启了 Checkpoint 且没有设置重启策略，那么采用固定延时重启策略，最大重启次数为 Integer.MAX_VALUE。

在应用程序中配置好了固定延时重启策略，可以测试一下代码异常后导致 Job 失败后重启的情况，然后观察日志，可以看到 Job 重启相关的日志：

```text
[flink-akka.actor.default-dispatcher-5] INFO org.apache.flink.runtime.executiongraph.ExecutionGraph - Try to restart or fail the job zhisheng default RestartStrategy example (a890361aed156610b354813894d02cd0) if no longer possible.
[flink-akka.actor.default-dispatcher-5] INFO org.apache.flink.runtime.executiongraph.ExecutionGraph - Job zhisheng default RestartStrategy example (a890361aed156610b354813894d02cd0) switched from state FAILING to RESTARTING.
[flink-akka.actor.default-dispatcher-5] INFO org.apache.flink.runtime.executiongraph.ExecutionGraph - Restarting the job zhisheng default RestartStrategy example (a890361aed156610b354813894d02cd0).
```

最后重启次数达到配置的最大重启次数后 Job 还没有起来的话，则会停止 Job 并打印日志：

```text
[flink-akka.actor.default-dispatcher-2] INFO org.apache.flink.runtime.executiongraph.ExecutionGraph - Could not restart the job zhisheng default RestartStrategy example (a890361aed156610b354813894d02cd0) because the restart strategy prevented it.
```

Flink 中几种重启策略的设置如上，大家可以根据需要选择合适的重启策略，比如如果程序抛出了空指针异常，但是你配置的是一直无限重启，那么就会导致 Job 一直在重启，这样无非再浪费机器资源，这种情况下可以配置重试固定次数，每次隔多久重试的固定延时重启策略，这样在重试一定次数后 Job 就会停止，如果对 Job 的状态做了监控告警的话，那么你就会收到告警信息，这样也会提示你去查看 Job 的运行状况，能及时的去发现和修复 Job 的问题。

### RestartStrategy 源码剖析

再介绍重启策略应用程序代码配置的时候不知道你有没有看到设置重启策略都是使用 RestartStrategies 类，通过该类的方法就可以创建不同的重启策略，在 RestartStrategies 类中提供了五个方法用来创建四种不同的重启策略（有两个方法是创建 FixedDelay 重启策略的，只不过方法的参数不同），如下图所示：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-151745.png)

在每个方法内部其实调用的是 RestartStrategies 中的内部静态类，分别是 NoRestartStrategyConfiguration、FixedDelayRestartStrategyConfiguration、FailureRateRestartStrategyConfiguration、FallbackRestartStrategyConfiguration，这四个类都继承自 RestartStrategyConfiguration 抽象类。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-151617.png)

上面是定义的四种重启策略的配置类，在 Flink 中是靠 RestartStrategyResolving 类中的 resolve 方法来解析 RestartStrategies.RestartStrategyConfiguration，然后根据配置使用 RestartStrategyFactory 创建 RestartStrategy。RestartStrategy 是一个接口，它有 canRestart 和 restart 两个方法，它有四个实现类： FixedDelayRestartStrategy、FailureRateRestartStrategy、ThrowingRestartStrategy、NoRestartStrategy。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-151311.png)

### Failover Strategies（故障恢复策略）

Flink 通过重启策略和故障恢复策略来控制 Task 重启：重启策略决定是否可以重启以及重启的间隔；故障恢复策略决定哪些 Task 需要重启。在 Flink 中支持两种不同的故障重启策略，该策略可以在 flink-conf.yaml 中的配置，默认为：

```yaml
jobmanager.execution.failover-strategy: region
```

该配置有两个可选值，full（重启所有的 Task）和 region（重启 pipelined region），在 Flink 1.9 中默认设置的恢复策略变成 region 了。

参考 Flink Issue：https://issues.apache.org/jira/browse/FLINK-13223

#### 重启所有的任务

在 full 故障恢复策略下，Task 发生故障时会重启作业中的所有 Task 来恢复，会造成一定的资源浪费，但却是恢复作业一致性的最安全策略，会在其他 Failover 策略失败时作为保底策略使用。

#### 基于 Region 的局部故障重启策略

基于 Region 的局部故障恢复策略会将作业中的 Task 划分为数个 Region，根据数据传输决定的，有数据传输的 Task 会被放在同一个 Region，不同 Region 之间无数据交换。如果有 Task 发生故障的时候，它会重启发生错误的 Task 所在 Region 的所有 Task，这种策略相对于重启所有的 Task 策略来说重启的 Task 数量会变少。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-131936.png)

如上图如果 C2 Task 因为错误挂了，它会根据数据流往上找到 Source，然后根据 Source 可以知道数据流到下游的所有 Task，进而将这些 Task 重启（见下图）。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-140828.png)

当然你会发现上面这种重启方式其实重启的 Task 数量还是不少，为了进一步减少需要重新启动的 Task 数量，可以使用某些类型的数据流交换，将 Task 运算的结果暂存在中间，然后如果有 Task 失败了，那么就往前去找中间结果，然后重启中间结果到数据流向的最后 Task 之间所有的 Task。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-144622.png)

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-144713.png)

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-145101.png)

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-08-150015.png)

从上面四个图可以看到这样的话，故障恢复的需要重启的 Task 数量就降低了，但是适合这种的策略的场景是有限的，详情可以参考：

> [https://cwiki.apache.org/confluence/display/FLINK/FLIP-1+%3A+Fine+Grained+Recovery+from+Task+Failures](https://cwiki.apache.org/confluence/display/FLINK/FLIP-1+:+Fine+Grained+Recovery+from+Task+Failures)

在查看源码的时候还看到一种恢复策略是 RestartIndividualStrategy，这种策略只会重启挂掉的那个 Task，如果该 Task 没有包含数据源，这会导致它不能重流数据而导致一部分数据丢失，所以这种策略的使用是有局限性的，不能保证数据的一致性。

### 小结与反思

本节通过 Flink 中因常见错误导致的作业重启引出 RestartStrategy，接着介绍 RestartStrategy 的使用方式和 Flink 支持的 RestartStrategy，并对 RestartStrategy 做了简单的源码分析，接着讲了下故障恢复策略。

你们公司的 Flink 作业通常是怎么配置重启策略的呢？有什么你们的技巧吗？

本节涉及的代码地址：https://github.com/zhisheng17/flink-learning/blob/master/flink-learning-examples/src/main/java/com/zhisheng/examples/streaming/restartStrategy/