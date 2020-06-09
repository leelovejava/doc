## 如何使用 Flink ProcessFunction 处理宕机告警?

### ProcessFunction 介绍

在 1.2.5 节中讲了 Flink 的 API 分层，其中可以看见 Flink 的底层 API 就是 ProcessFunction，它是一个低阶的流处理操作，它可以访问流处理程序的基础构建模块：Event、State、Timer。ProcessFunction 可以被认为是一种提供了对 KeyedState 和定时器访问的 FlatMapFunction。每当数据源中接收到一个事件，就会调用来此函数来处理。对于容错的状态，ProcessFunction 可以通过 RuntimeContext 访问 KeyedState。

定时器可以对处理时间和事件时间的变化做一些处理。每次调用 processElement() 都可以获得一个 Context 对象，通过该对象可以访问元素的事件时间戳以及 TimerService。TimerService 可以为尚未发生的事件时间/处理时间实例注册回调。当定时器到达某个时刻时，会调用 onTimer() 方法。在调用期间，所有状态再次限定为定时器创建的 key，允许定时器操作 KeyedState。如果要访问 KeyedState 和定时器，那必须在 KeyedStream 上使用 KeyedProcessFunction，比如在 keyBy 算子之后使用：

```java
dataStream.keyBy(...).process(new KeyedProcessFunction<>(){

})
```

KeyedProcessFunction 是 ProcessFunction 函数的一个扩展，它可以在 onTimer 和 processElement 方法中获取到分区的 Key 值，这对于数据传递是很有帮助的，因为经常有这样的需求，经过 keyBy 算子之后可能还需要这个 key 字段，那么在这里直接构建成一个新的对象（新增一个 key 字段），然后下游的算子直接使用这个新对象中的 key 就好了，而不在需要重复的拼一个唯一的 key。

```java
public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
    System.out.println(ctx.getCurrentKey());
    out.collect(value);
}

@Override
public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
    System.out.println(ctx.getCurrentKey());
    super.onTimer(timestamp, ctx, out);
}
```

### CoProcessFunction 介绍

如果要在两个输入流上进行操作，可以使用 CoProcessFunction，这个函数可以传入两个不同的数据流输入，并为来自两个不同数据源的事件分别调用 processElement1() 和 processElement2() 方法。可以按照下面的步骤来实现一个典型的 Join 操作：

- 为一个数据源的数据建立一个状态对象
- 从数据源处有新数据流过来的时候更新这个状态对象
- 在另一个数据源接收到元素时，关联状态对象并对其产生出连接的结果

比如，将监控的 metric 数据和告警规则数据进行一个连接，在流数据的状态中存储了告警规则数据，当有监控数据过来时，根据监控数据的 metric 名称和一些 tag 去找对应告警规则计算表达式，然后通过规则的表达式对数据进行加工处理，判断是否要告警，如果是要告警则会关联构造成一个新的对象，新对象中不仅有初始的监控 metric 数据，还有含有对应的告警规则数据以及通知策略数据，组装成这样一条数据后，下游就可以根据这个数据进行通知，通知还会在状态中存储这个告警状态，表示它在什么时间告过警了，下次有新数据过来的时候，判断新数据是否是恢复的，如果属于恢复则把该状态清除。

### Timer 介绍

Timer 提供了一种定时触发器的功能，通过 TimerService 接口注册 timer。TimerService 在内部维护两种类型的定时器（处理时间和事件时间定时器）并排队执行。处理时间定时器的触发依赖于 ProcessingTimeService，它负责管理所有基于处理时间的触发器，内部使用 ScheduledThreadPoolExecutor 调度定时任务；事件时间定时器的触发依赖于系统当前的 Watermark。需要注意的一点就是：**Timer 只能在 KeyedStream 中使用**。

TimerService 会删除每个 Key 和时间戳重复的定时器，即每个 Key 在同一个时间戳上最多有一个定时器。如果为同一时间戳注册了多个定时器，则只会调用一次 onTimer（） 方法。Flink 会同步调用 onTimer() 和 processElement() 方法，因此不必担心状态的并发修改问题。TimerService 不仅提供了注册和删除 Timer 的功能，还可以通过它来获取当前的系统时间和 Watermark 的值。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-14-160008.png)

#### 容错

定时器具有容错能力，并且会与应用程序的状态一起进行 Checkpoint，如果发生故障重启会从 Checkpoint／Savepoint 中恢复定时器的状态。如果有处理时间定时器原本是要在恢复起来的那个时间之前触发的，那么在恢复的那一刻会立即触发该定时器。定时器始终是异步的进行 Checkpoint（除 RocksDB 状态后端存储、增量的 Checkpoint、基于堆的定时器外）。因为定时器实际上也是一种特殊状态的状态，在 Checkpoint 时会写入快照中，所以如果有大量的定时器，则无非会增加一次 Checkpoint 所需要的时间，必要的话得根据实际情况合并定时器。

#### 合并定时器

由于 Flink 仅为每个 Key 和时间戳维护一个定时器，因此可以通过降低定时器的频率来进行合并以减少定时器的数量。对于频率为 1 秒的定时器（基于事件时间或处理时间），可以将目标时间向下舍入为整秒数，则定时器最多提前 1 秒触发，但不会迟于我们的要求，精确到毫秒。因此，每个键每秒最多有一个定时器。

```java
long coalescedTime = ((ctx.timestamp() + timeout) / 1000) * 1000;
ctx.timerService().registerProcessingTimeTimer(coalescedTime);
```

由于事件时间计时器仅在 Watermark 到达时才触发，因此可以将当前 Watermark 与下一个 Watermark 的定时器一起调度和合并：

```java
long coalescedTime = ctx.timerService().currentWatermark() + 1;
ctx.timerService().registerEventTimeTimer(coalescedTime);
```

定时器也可以类似下面这样移除：

```java
//删除处理时间定时器
long timestampOfTimerToStop = ...
ctx.timerService().deleteProcessingTimeTimer(timestampOfTimerToStop);

//删除事件时间定时器
long timestampOfTimerToStop = ...
ctx.timerService().deleteEventTimeTimer(timestampOfTimerToStop);
```

如果没有该时间戳的定时器，则删除定时器无效。

### 如果利用 ProcessFunction 处理宕机告警？

前面介绍了 ProcessFunction 和 Timer，那么这里讲下笔者公司生产环境的一个案例 —— 利用 ProcessFunction 处理宕机告警？

#### 宕机告警需求分析

首先大家应该知道生产环境的服务器一般都是有部署各种各种的服务或者中间件的，那么如果一台机器突然发生了一些突发情况，比如断电、自然灾害、人为因素、服务把机器跑宕机等，那么机器一宕机，原先跑在该机器的服务都会掉线，导致服务出现短暂不可用（可能应用会调度到其他机器）或者直接不可用（没有调度策略并且是运行的单实例），这对于生产环境来说，就麻烦比较大，可能会出现很大的损失，所以这种紧急情况就特别需要实时性非常高的告警。

在面对这个需求时首先得想一想怎么去判定一台机器是否处于宕机，因为会在机器上部署采集机器信息的 Agent，如果机器是正常的，每隔一定时间（假设时间间隔为 10 秒） Agent 会将数据进行上传，所有的监控数据上传至消息队列后，接下来就需要对这些监控数据处理。那么当机器处于宕机的状态，则运行在机器的 Agent 就已经停止工作了，则它就不会继续上传监控信息来了，所以这里就可以根据判定是否有这台机器的监控数据上来，如果持续有，那么说明机器在线，如果持续一段时间没有收到该机器的数据，则意味着该机器宕机了，那么可能就有人想问了，这个持续时间设置多少合适呢？这个得根据实际情况去做大量的测试和调优了，如果设置的过短，假设数据在消息队列中堆积了一会，那么也会出现误判的宕机告警；如果设置的过长，那么可能机器中途宕机过然后重启了，但是时间还是在设置的预定时间之内，这种情况就出现了宕机告警漏报，也是不允许的（告警延迟性增大并且可能告警漏报），所以就得根据实际情况两者之间做一个权衡。

在分析完需求后，接下来就得看如何去实现这种需求，怎么去判断机器是否一直有数据上来？那么这里就利用了 Timer 机制。

#### 宕机告警实现

机器监控数据有很多的指标，这里列几种比较常见的比如 Mem、CPU、Load、Swap 等，那么这几种数据采集上来的结构都是 MetricEvent 类型。

```java
public class MetricEvent {

    //指标名
    private String name;

    //数据时间
    private Long timestamp;

    //指标具体字段
    private Map<String, Object> fields;

    //指标的标识
    private Map<String, String> tags;
}
```

就拿 CPU 来举个例子，它发上来的数据是下面这种的：

```json
{
    "name": "cpu",
    "timestamp": 1571108814142,
    "fields": {
        "usedPercent": 93.896484375,
        "max": 2048,
        "used": 1923
    },
    "tags": {
        "cluster_name": "zhisheng",
        "host_ip": "121.12.17.11"
    }
}
```

这里笔者写了个模拟 Mem、CPU、Load、Swap 监控数据的工具类：

```java
public class BuildMachineMetricDataUtil {
    public static final String BROKER_LIST = "localhost:9092";
    public static final String METRICS_TOPIC = "zhisheng_metrics";
    public static Random random = new Random();

    public static List<String> hostIps = Arrays.asList("121.12.17.10", "121.12.17.11", "121.12.17.12", "121.12.17.13");

    public static void writeDataToKafka() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER_LIST);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer<String, String>(props);

        while (true) {
            long timestamp = System.currentTimeMillis();
            for (int i = 0; i < hostIps.size(); i++) {
                MetricEvent cpuData = buildCpuData(hostIps.get(i), timestamp);
                MetricEvent loadData = buildLoadData(hostIps.get(i), timestamp);
                MetricEvent memData = buildMemData(hostIps.get(i), timestamp);
                MetricEvent swapData = buildSwapData(hostIps.get(i), timestamp);
                ProducerRecord cpuRecord = new ProducerRecord<String, String>(METRICS_TOPIC, null, null, GsonUtil.toJson(cpuData));
                ProducerRecord loadRecord = new ProducerRecord<String, String>(METRICS_TOPIC, null, null, GsonUtil.toJson(loadData));
                ProducerRecord memRecord = new ProducerRecord<String, String>(METRICS_TOPIC, null, null, GsonUtil.toJson(memData));
                ProducerRecord swapRecord = new ProducerRecord<String, String>(METRICS_TOPIC, null, null, GsonUtil.toJson(swapData));
                producer.send(cpuRecord);
                producer.send(loadRecord);
                producer.send(memRecord);
                producer.send(swapRecord);
            }
            producer.flush();
            Thread.sleep(10000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        writeDataToKafka();
    }

    public static MetricEvent buildCpuData(String hostIp, Long timestamp) {
        MetricEvent metricEvent = new MetricEvent();
        Map<String, String> tags = new HashMap<>();
        Map<String, Object> fields = new HashMap<>();
        int used = random.nextInt(2048);
        int max = 2048;
        metricEvent.setName("cpu");
        metricEvent.setTimestamp(timestamp);
        tags.put("cluster_name", "zhisheng");
        tags.put("host_ip", hostIp);
        fields.put("usedPercent", (double) used / max * 100);
        fields.put("used", used);
        fields.put("max", max);
        metricEvent.setFields(fields);
        metricEvent.setTags(tags);
        return metricEvent;
    }

    public static MetricEvent buildLoadData(String hostIp, Long timestamp) {
        //构建 load 数据，和构建 CPU 数据类似
    }

    public static MetricEvent buildSwapData(String hostIp, Long timestamp) {
        //构建swap数据，和构建 CPU 数据类似
    }

    public static MetricEvent buildMemData(String hostIp, Long timestamp) {
        //构建内存的数据，和构建 CPU 数据类似
    }
}
```

然后 Flink 应用程序实时的去消费 Kafka 中的机器监控数据，先判断数据能够正常消费到。

```java
final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);

Properties properties = KafkaConfigUtil.buildKafkaProps(parameterTool);
FlinkKafkaConsumer011<MetricEvent> consumer = new FlinkKafkaConsumer011<>(
        parameterTool.get("metrics.topic"),
        new MetricSchema(),
        properties);
env.addSource(consumer)
        .assignTimestampsAndWatermarks(new MetricWatermark())
        .print();
```

再确定能够消费到机器监控数据之后，接下来需要对数据进行构造成 OutageMetricEvent 对象：

```java
public class OutageMetricEvent {
    //机器集群名
    private String clusterName;
    //机器 host ip
    private String hostIp;
    //事件时间
    private Long timestamp;
    //机器告警是否恢复
    private Boolean recover;
    //机器告警恢复时间
    private Long recoverTime;
    //系统时间
    private Long systemTimestamp;
    //机器 CPU 使用率
    private Double cpuUsePercent;
    //机器内存使用率
    private Double memUsedPercent;
    //机器 SWAP 使用率
    private Double swapUsedPercent;
    //机器 load5
    private Double load5;
    //告警数量
    private int counter = 0;
}
```

通过 FlatMap 算子转换：

```java
new FlatMapFunction<MetricEvent, OutageMetricEvent>() {
    @Override
    public void flatMap(MetricEvent metricEvent, Collector<OutageMetricEvent> collector) throws Exception {
        Map<String, String> tags = metricEvent.getTags();
        if (tags.containsKey(CLUSTER_NAME) && tags.containsKey(HOST_IP)) {
            OutageMetricEvent outageMetricEvent = OutageMetricEvent.buildFromEvent(metricEvent);
            if (outageMetricEvent != null) {
                collector.collect(outageMetricEvent);
            }
        }
    }
}
```

将数据转换后，需要将监控数据按照机器的 IP 进行 KeyBy，因为每台机器可能都会出现错误，所以都要将不同机器的状态都保存着，然后使用 process 算子，在该算子中，使用 ValueState 保存 OutageMetricEvent 和机器告警状态信息，另外还有一个 delay 字段定义的是持续多久没有收到监控数据的时间，alertCountLimit 表示的是告警的次数，如果超多一定的告警次数则会静默。

```java
public class OutageProcessFunction extends KeyedProcessFunction<String, OutageMetricEvent, OutageMetricEvent> {

    private ValueState<OutageMetricEvent> outageMetricState;
    private ValueState<Boolean> recover;

    private int delay;
    private int alertCountLimit;

    public OutageProcessFunction(int delay, int alertCountLimit) {
        this.delay = delay;
        this.alertCountLimit = alertCountLimit;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        TypeInformation<OutageMetricEvent> outageInfo = TypeInformation.of(new TypeHint<OutageMetricEvent>() {
        });
        TypeInformation<Boolean> recoverInfo = TypeInformation.of(new TypeHint<Boolean>() {
        });
        outageMetricState = getRuntimeContext().getState(new ValueStateDescriptor<>("outage_zhisheng", outageInfo));
        recover = getRuntimeContext().getState(new ValueStateDescriptor<>("recover_zhisheng", recoverInfo));
    }

    @Override
    public void processElement(OutageMetricEvent outageMetricEvent, Context ctx, Collector<OutageMetricEvent> collector) throws Exception {
        OutageMetricEvent current = outageMetricState.value();
        if (current == null) {
            current = new OutageMetricEvent(outageMetricEvent.getClusterName(), outageMetricEvent.getHostIp(),
                    outageMetricEvent.getTimestamp(), outageMetricEvent.getRecover(), System.currentTimeMillis());
        } else {
            if (outageMetricEvent.getLoad5() != null) {
                current.setLoad5(outageMetricEvent.getLoad5());
            }
            if (outageMetricEvent.getCpuUsePercent() != null) {
                current.setCpuUsePercent(outageMetricEvent.getCpuUsePercent());
            }
            if (outageMetricEvent.getMemUsedPercent() != null) {
                current.setMemUsedPercent(outageMetricEvent.getMemUsedPercent());
            }
            if (outageMetricEvent.getSwapUsedPercent() != null) {
                current.setSwapUsedPercent(outageMetricEvent.getSwapUsedPercent());
            }
            current.setSystemTimestamp(System.currentTimeMillis());
        }

        if (recover.value() != null && !recover.value() && outageMetricEvent.getTimestamp() > current.getTimestamp()) {
            OutageMetricEvent recoverEvent = new OutageMetricEvent(outageMetricEvent.getClusterName(), outageMetricEvent.getHostIp(),
                    current.getTimestamp(), true, System.currentTimeMillis());
            recoverEvent.setRecoverTime(ctx.timestamp());
            log.info("触发宕机恢复事件:{}", recoverEvent);
            collector.collect(recoverEvent);
            current.setCounter(0);
            outageMetricState.update(current);
            recover.update(true);
        }

        current.setTimestamp(outageMetricEvent.getTimestamp());
        outageMetricState.update(current);
        ctx.timerService().registerEventTimeTimer(current.getSystemTimestamp() + delay);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<OutageMetricEvent> out) throws Exception {
        OutageMetricEvent result = outageMetricState.value();

        if (result != null && timestamp >= result.getSystemTimestamp() + delay && System.currentTimeMillis() - result.getTimestamp() >= delay) {
            if (result.getCounter() > alertCountLimit) {
                log.info("宕机告警次数大于:{} :{}", alertCountLimit, result);
                return;
            }
            log.info("触发宕机告警事件:timestamp = {}, result = {}", System.currentTimeMillis(), result);
            result.setRecover(false);
            out.collect(result);
            ctx.timerService().registerEventTimeTimer(timestamp + delay);
            result.setCounter(result.getCounter() + 1);
            result.setSystemTimestamp(timestamp);
            outageMetricState.update(result);
            recover.update(false);
        }
    }
}
```

在 processElement 方法中不断的处理数据，在处理的时候会从状态中获取看之前状态是否存在数据，在该方法内部最后通过 `ctx.timerService().registerEventTimeTimer(current.getSystemTimestamp() + delay);` 去注册一个事件时间的定时器，时间戳是当前的系统时间加上 delay 的时间。

在 onTimer 方法中就是具体的定时器，在定时器中获取到状态值，然后将状态值中的时间与 delay 的时间差是否满足，如果满足则表示一直没有数据过来，接着对比目前告警的数量与定义的限制数量，如果大于则不告警了，如果小于则表示触发了宕机告警并且打印相关的日志，然后更新状态中的值。

```java
public void onTimer(long timestamp, OnTimerContext ctx, Collector<OutageMetricEvent> out) throws Exception {
    OutageMetricEvent result = outageMetricState.value();

    if (result != null && timestamp >= result.getSystemTimestamp() + delay && System.currentTimeMillis() - result.getTimestamp() >= delay) {
        if (result.getCounter() > alertCountLimit) {
            log.info("宕机告警次数大于:{} :{}", alertCountLimit, result);
            return;
        }
        log.info("触发宕机告警事件:timestamp = {}, result = {}", System.currentTimeMillis(), result);
        result.setRecover(false);
        out.collect(result);
        ctx.timerService().registerEventTimeTimer(timestamp + delay);
        result.setCounter(result.getCounter() + 1);
        result.setSystemTimestamp(timestamp);
        outageMetricState.update(result);
        recover.update(false);
    }
}
```

这样就完成了告警事件的判断了，接下来的算子就可以将告警事件转换成告警消息，然后将告警消息发送到下游去通知。那么就这样可以完成一个机器宕机告警的需求。

### 小结与反思

本节介绍了 Flink 中的 ProcesFunction 和 Timer，介绍完之后通过一个真实的需求讲解如何 ProcesFunction 和 Timer 的使用来解决问题的。这个需求你有什么更好的方案吗？

本节涉及的代码地址：https://github.com/zhisheng17/flink-learning/blob/master/flink-learning-monitor/flink-learning-monitor-alert/src/main/java/com/zhisheng/alert/alert/OutageAlert.java