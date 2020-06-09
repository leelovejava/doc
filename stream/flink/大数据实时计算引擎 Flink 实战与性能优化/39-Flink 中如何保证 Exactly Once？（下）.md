## Flink 中如何保证 Exactly Once？（下）

### 分析 FlinkKafkaConsumer 的设计思想

FlinkKafkaConsumer 做为 Source，从 Kafka 读取数据到 Flink 中，首先想一下设计 FlinkKafkaConsumer，需要考虑哪些？

- Flink 中 kafka 的 offset 保存在哪里，具体如何保存呢？任务重启恢复时，如何读取之前消费的 offset？
- 如果 Source 端并行度改变了，如何来恢复 offset？
- 如何保证每个 FlinkKafkaConsumer 实例消费的 partition 负载均衡？如何保证不出现有的实例消费 5 个 kafka partition，有的实例仅消费 1 个 kafka partition？
- 当前消费的 topic 如果动态增加了 partition，Flink 如何实现自动发现并消费？

带着这些问题来看一看 FlinkKafkaConsumer 是怎么解决上述问题的。

#### Kafka offset 存储及如何实现 Consumer 实例消费 partition 的负载均衡

Flink 将任务恢复需要的信息都保存在状态中，当然 Kafka 的 offset 信息也保存在 Flink 的状态中，当任务从状态中恢复时会从状态中读取相应的 offset，并从 offset 位置开始消费。

在 Flink 中有两个基本的 State：Keyed State 和 Operator State。

- Keyed State 只能用于 KeyedStream 的 function 和 Operator 中，一个 Key 对应一个 State；
- 而 Operator State 可以用于所有类型的 function 和 Operator 中，一个 Operator 实例对应一个 State，假如一个算子并行度是 5 且使用 Operator State，那么这个算子的每个并行度都对应一个 State，总共 5 个 State。

FlinkKafkaConsumer 做为 Source 只能使用 Operator State，Operator State 只支持一种数据结构 ListState，可以当做 List 类型的 State。所以 FlinkKafkaConsumer 中，将状态保存在 Operator State 对应的 ListState 中。具体如何保存呢？需要先了解每个 FlinkKafkaConsumer 具体怎么消费 Kafka。

对于同一个消费者组，Kafka 要求 topic 的每个 partition 只能被一个 Consumer 实例消费，相反一个 Consumer 实例可以去消费多个 partition。当 Flink 消费 Kafka 时，出现了以下三种情况：

| 情况                                                | 现象                                                         |
| :-------------------------------------------------- | :----------------------------------------------------------- |
| FlinkKafkaConsumer 并行度大于 topic 的 partition 数 | 有些 FlinkKafkaConsumer 不会消费 Kafka                       |
| FlinkKafkaConsumer 并行度等于 topic 的 partition 数 | 每个 FlinkKafkaConsumer 消费 1 个 partition                  |
| FlinkKafkaConsumer 并行度小于 topic 的 partition 数 | 每个 FlinkKafkaConsumer 至少消费 1 个 partition，可能会消费多个 partition |

Flink 是如何为每个 Consumer 实例合理地分配去消费哪些 partition 呢？源码中 KafkaTopicPartitionAssigner 类的 assign 方法，负责分配 partition 给 Consumer 实例。assign 方法的输入参数为 KafkaTopicPartition 和 Consumer 的并行度，KafkaTopicPartition 主要包含两个字段：String 类型的 topic 和 int 类型的 partition。assign 方法返回该 KafkaTopicPartition 应该分配给哪个 Consumer 实例去消费。假如 Consumer 的并行度为 5，表示包含了 5 个 subtask，assign 方法的返回值范围为 0~4，分别表示该 partition 分配给 subtask0-subtask4。

```java
/**
 * @param partition Kafka 中 topic 和 partition 信息
 * @param numParallelSubtasks subtask 的数量
 * @return 该 KafkaTopicPartition 分配给哪个 subtask 去消费
 */
public static int assign(KafkaTopicPartition partition, int numParallelSubtasks) {
    int startIndex = ((partition.getTopic().hashCode() * 31) & 0x7FFFFFFF) % numParallelSubtasks;
    return (startIndex + partition.getPartition()) % numParallelSubtasks;
}
```

assign 方法是如何给 KafkaTopicPartition 分配 Consumer 实例的呢？

第一行代码根据 topic name 的 hashCode 运算后对 subtask 的数量求余生成一个 startIndex，第二行代码用 startIndex + partition 编号对 subtask 的数量求余，可以保证该 topic 的 0 号 partition 分配给 startIndex 对应的 subtask，后续的 partition 依次分配给后续的 subtask。

例如，名为 "test-topic" 的 topic 有 11 个 partition 分别为 partition0-partition10，Consumer 有 5 个并行度分别为 subtask0-subtask4。计算后的 startIndex 为 1，表示 partition0 分配给 subtask1，partition1 分配给 subtask2 以此类推，subtask 与 partition 的对应关系如下图所示。

assign 方法给 partition 分配 subtask 实际上是轮循的策略，首先计算一个起点 startIndex 分配给 partition0，后续的 partition 轮循地分配给 subtask，从而使得每个 subtask 消费的 partition 得以均衡。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-19-122937.jpg)

每个 subtask 只负责一部分 partition，所以在维护 partition 的 offset 信息时，每个 subtask 只需要将自己消费的 partition 的 offset 信息保存到状态中即可。

保存的格式理论来讲应该是 kv 键值对，key 为 KafkaTopicPartition，value 为 Long 类型的 offset 值。但 Flink 的 Operator State 只支持 ListState 一种数据结构，不支持 kv 格式，可以将 KafkaTopicPartition 和 Long 封装为 Tuple2<KafkaTopicPartition, Long> 存储到 ListState 中。如下所示，Flink 源码中确实如此，使用 ListState<Tuple2<KafkaTopicPartition, Long>> 类型的 unionOffsetStates 来保存 Kafka 的 offset 信息。

```java
/** Accessor for state in the operator state backend. */
private transient ListState<Tuple2<KafkaTopicPartition, Long>> unionOffsetStates;
```

当 Flink 应用从 Checkpoint 恢复任务时，会从 unionOffsetStates 中读取上一次 Checkpoint 保存的 offset 信息，并从 offset 的位置开始继续消费，从而实现 Flink 任务的故障容错。例如，任务重启后，Operator State 是一个 Operator 实例对应一个 State，subtask0 依然消费 partition4 和 partition9，subtask0 从自己的 State 中可以读取到 partition4 和 partition9 消费的 offset，从 offset 位置接着往后消费即可。问题来了，若 FlinkKafkaConsumer 的并行度改变后，offset 信息如何恢复呢？

#### Source 端并行度改变了，如何来恢复 offset

subtask1 当前消费了 3 个 partition，而其他 subtask 仅消费 2 个 partition，当发现 subtask1 读取 Kafka 成为瓶颈后，需要调大 Consumer 的并行度，使得每个 subtask 最多仅消费 2 个 partition。将 Consumer 实例的并行度增大到 6 以后，分配器对 partition 重新分配给 6 个 subtask，计算后的 startIndex 为 0，表示 partition0 分配给 subtask0，后续的 partition 采用轮循策略，partition 与 subtask 的对应关系如下。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-19-122939.jpg)

之前 subtask0 消费 partition 4 和 9，并行度调大以后，subtask0 被分配消费 partition 0 和 6。但是 Flink 任务从 Checkpoint 恢复后，能保证 subtask0 读取到 partition 0 和 6 的 offset 吗？这个就需要深入了解当 Flink 算子并行度改变后，Operator State 的 ListState 两种恢复策略。两种策略如下所示，在 initializeState 方法中执行相应 API 来恢复。

```java
OperatorStateStore stateStore = context.getOperatorStateStore();

// 通过 getListState 获取 ListState
stateStore.getListState(ListStateDescriptor<S> var1);

// 通过 getUnionListState 获取 ListState
stateStore.getUnionListState(ListStateDescriptor<S> var1);
```

当并行度改变后，getListState 恢复策略是均匀分配，将 ListState 中保存的所有元素均匀地分配到所有并行度中，每个 subtask 获取到其中一部分状态信息。

getUnionListState 策略是将所有的状态信息合并后，每个 subtask 都获取到全量的状态信息。在 FlinkKafkaConsumer 中，假如使用 getListState 来获取 ListState，采用均匀分配状态信息的策略，Flink 可能给 subtask0 分配了 partition0 和 partition1 的 offset 信息，但实际上分配器让 subtask0 去消费 partition0 和 partition6，此时 subtask0 并拿不到 partition 6 的 offset 信息，不知道该从 partition 6 哪个位置消费，所以均匀分配状态信息的策略并不能满足需求。

这里应该使用 getUnionListState 来获取 ListState，也就是说每个 subtask 都可以获取到所有 partition 的 offset 信息，然后根据分配器让 subtask 0 去消费 partition0 和 partition6 时，subtask0 只需要从全量的 offset 中拿到 partition0 和 partition6 的状态信息即可。

这么做会使得每个 subtask 获取到一些无用的 offset 的信息，但实际上这些 offset 信息占用的空间会比较小，所以该方案成本比较低。关于 OperatorState 的 ListState 两种获取方式请参考代码：

> https://github.com/zhisheng17/flink-learning/blob/master/flink-learning-state/src/main/java/com/zhisheng/state/operator/state/UnionListStateExample.java

FlinkKafkaConsumer 初始化时，恢复 offset 相关的源码如下：

```java
// initializeState  方法中用于恢复 offset 状态信息
public final void initializeState(FunctionInitializationContext context) throws Exception {
    OperatorStateStore stateStore = context.getOperatorStateStore();
    // 此处省略了兼容 Flink 1.2 之前状态 API 的场景
    ...
    // 此处使用的 getUnionListState，而不是 getListState。因为重启后，可能并行度被改变了
    this.unionOffsetStates = stateStore.getUnionListState(new ListStateDescriptor<>(
            OFFSETS_STATE_NAME,
            TypeInformation.of(new TypeHint<Tuple2<KafkaTopicPartition, Long>>() {})));

    if (context.isRestored() && !restoredFromOldState) {
        restoredState = new TreeMap<>(new KafkaTopicPartition.Comparator());

        // 将状态中恢复的 offset 信息 put 到 TreeMap 类型的 restoredState 中，方便查询
        for (Tuple2<KafkaTopicPartition, Long> kafkaOffset : unionOffsetStates.get()) {
            restoredState.put(kafkaOffset.f0, kafkaOffset.f1);
        }
    }
}


// open 方法对 FlinkKafkaConsumer 做初始化
public void open(Configuration configuration) throws Exception {
    // 创建 Kafka partition 的发现器，用于检测该 subtask 应该去消费哪些 partition
    this.partitionDiscoverer = createPartitionDiscoverer(
            topicsDescriptor,
            getRuntimeContext().getIndexOfThisSubtask(),
            getRuntimeContext().getNumberOfParallelSubtasks());
    this.partitionDiscoverer.open();
    // subscribedPartitionsToStartOffsets 存储当前 subtask 需要消费的 partition 及对应的 offset 初始信息
    subscribedPartitionsToStartOffsets = new HashMap<>();
    //用 partition 发现器获取该 subtask 应该消费且新发现的 partition
    final List<KafkaTopicPartition> allPartitions = partitionDiscoverer.discoverPartitions();
    // restoredState 在 initializeState 时初始化，所以 != null 表示任务从 Checkpoint 处恢复
    if (restoredState != null) {
        for (KafkaTopicPartition partition : allPartitions) {
            // 若分配给该 subtask 的 partition 在 restoredState 中不包含
            // 说明该 partition 是新创建的 partition，默认从 earliest 开始消费
              // 并添加到 restoredState 中
            if (!restoredState.containsKey(partition)) {
                restoredState.put(partition, KafkaTopicPartitionStateSentinel.EARLIEST_OFFSET);
            }
        }

        for (Map.Entry<KafkaTopicPartition, Long> restoredStateEntry : restoredState.entrySet()) {
            // 遍历 restoredState，使用分配器检测当前的 partition 是否分配给当前的 subtask
            // assign 方法返回当前 partition 应该分配的 subtask index 编号
            // getRuntimeContext().getIndexOfThisSubtask()  返回当前 subtask 的 index 编号
            if (KafkaTopicPartitionAssigner.assign(
                restoredStateEntry.getKey(), getRuntimeContext().getNumberOfParallelSubtasks())
                    == getRuntimeContext().getIndexOfThisSubtask()){
                // 如果当前遍历的 partition 分配给当前 subtask 来消费，则将 partition 信息加到  subscribedPartitionsToStartOffsets 中
                subscribedPartitionsToStartOffsets.put(restoredStateEntry.getKey(), restoredStateEntry.getValue());
            }
        }
    } else {
        // else 表示任务不是从 Checkpoint 处恢复，本次源码主要分析状态恢复，不考虑该情况
    }
}
```

对 offset 信息快照相关的源码如下：

```java
public final void snapshotState(FunctionSnapshotContext context) throws Exception {
    // 把旧的 offset 信息从 unionOffsetStates 清除掉
    unionOffsetStates.clear();

    final AbstractFetcher<?, ?> fetcher = this.kafkaFetcher;
    // 通过提取器从 Kafka 读取数据，若 fetcher == null 表示提取器还未初始化
    if (fetcher == null) {
        // Kafka 提取器还未初始化，说明还未从 Kafka 中读取数据
                // 所以遍历 subscribedPartitionsToStartOffsets，将 offset 的初始信息写入到状态中
        for (Map.Entry<KafkaTopicPartition, Long> subscribedPartition : subscribedPartitionsToStartOffsets.entrySet()) {
            unionOffsetStates.add(Tuple2.of(subscribedPartition.getKey(), subscribedPartition.getValue()));
        }

        if (offsetCommitMode == OffsetCommitMode.ON_CHECKPOINTS) {
            // 将 offset put 到 pendingOffsetsToCommit，后续 Commit 到 Kafka 
            pendingOffsetsToCommit.put(context.getCheckpointId(), restoredState);
        }
    } else {
        // 从 Kafka 提取器中获取该 subtask 订阅的 partition 当前消费的 offset 信息
        HashMap<KafkaTopicPartition, Long> currentOffsets = fetcher.snapshotCurrentState();
        if (offsetCommitMode == OffsetCommitMode.ON_CHECKPOINTS) {
            // 将 offset put 到 pendingOffsetsToCommit，后续 Commit 到 Kafka 
            pendingOffsetsToCommit.put(context.getCheckpointId(), currentOffsets);
        }

        for (Map.Entry<KafkaTopicPartition, Long> kafkaTopicPartitionLongEntry : currentOffsets.entrySet()) {
            // 将该 subtask 订阅的 partition 以及当前 partition 消费到的 offset 写入到状态中
            unionOffsetStates.add(
                    Tuple2.of(kafkaTopicPartitionLongEntry.getKey(), kafkaTopicPartitionLongEntry.getValue()));
        }
    }
}
```

上述源码分析描述了，当 Checkpoint 时 FlinkKafkaConsumer 如何将 offset 信息保存到状态中，当任务从 Checkpoint 处恢复时 FlinkKafkaConsumer 如何从状态中获取相应的 offset 信息，并解答了当 Source 并行度改变时 FlinkKafkaConsumer 如何来恢复 offset 信息。

#### 如何实现自动发现当前消费 topic 下新增的 partition

当 FlinkKafkaConsumer 初始化时，每个 subtask 会订阅一批 partition，但是当 Flink 任务运行过程中，如果被订阅的 topic 创建了新的 partition，FlinkKafkaConsumer 如何实现动态发现新创建的 partition 并消费呢？

在使用 FlinkKafkaConsumer 时，可以通过 Properties 传递一些配置参数，当配置了参数FlinkKafkaConsumerBase.KEY_PARTITION*DISCOVERY_INTERVAL*MILLIS 时，就会开启 partition 的动态发现，该参数表示间隔多久检测一次是否有新创建的 partition。那具体实现原理呢？相关源码的 UML 图如下所示：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-11-15-132311.png)

笔者生产环境使用的 FlinkKafkaConsumer011，FlinkKafkaConsumer011 继承 FlinkKafkaConsumer09，FlinkKafkaConsumer09 继承 FlinkKafkaConsumerBase。将参数 KEY_PARTITION*DISCOVERY_INTERVAL_MILLIS 传递给 FlinkKafkaConsumer011 时，在 FlinkKafkaConsumer09 的构造器中会调用 getLong(checkNotNull(props, "props"), KEY_PARTITION_DISCOVERY_INTERVAL*MILLIS, PARTITION_DISCOVERY_DISABLED) 解析该参数，并最终赋值给 FlinkKafkaConsumerBase 的 discoveryIntervalMillis 属性。后续相关源码如下所示：

```java
// FlinkKafkaConsumerBase 的 run 方法
public void run(SourceContext<T> sourceContext) throws Exception {
      ...
    if (discoveryIntervalMillis == PARTITION_DISCOVERY_DISABLED) {
            kafkaFetcher.runFetchLoop();
        } else {
            // discoveryIntervalMillis 被设置了，则开启 PartitionDiscovery
            runWithPartitionDiscovery();
        }
}

// runWithPartitionDiscovery 方法会调用 createAndStartDiscoveryLoop 方法
// createAndStartDiscoveryLoop 方法内创建了一个线程去循环检测发现新分区
private void createAndStartDiscoveryLoop(AtomicReference<Exception> discoveryLoopErrorRef) {
    //  创建一个线程去循环检测发现新分区
    discoveryLoopThread = new Thread(() -> {
        while (running) {
            final List<KafkaTopicPartition> discoveredPartitions;
            //  用 partition 发现器获取该 subtask 应该消费且新发现的 partition
            discoveredPartitions = partitionDiscoverer.discoverPartitions();

            // 发现了新的 partition，则添加到 Kafka 提取器
            if (running && !discoveredPartitions.isEmpty()) {
                //  kafkaFetcher 添加 新发现的 partition
                kafkaFetcher.addDiscoveredPartitions(discoveredPartitions);
            }

            if (running && discoveryIntervalMillis != 0) {
                //  sleep 设置的间隔时间
                Thread.sleep(discoveryIntervalMillis);
            }
        }
    }, "Kafka Partition Discovery for " + getRuntimeContext().getTaskNameWithSubtasks());

    discoveryLoopThread.start();
}
```

discoveryLoopThread 线程中每间隔 discoveryIntervalMillis 时间会调用 partition 发现器获取该 subtask 应该消费且新发现的 partition，在 open 方法初始化时，同样也调用 partitionDiscoverer.discoverPartitions() 方法来获取新发现的 partition，partition 发现器的 discoverPartitions 方法第一次调用时，会返回该 subtask 所有的 partition，后续调用只会返回新发现的且应该被当前 subtask 消费的 partition。discoverPartitions 方法源码如下：

```java
public List<KafkaTopicPartition> discoverPartitions() throws WakeupException, ClosedException {
    List<KafkaTopicPartition> newDiscoveredPartitions;
    // 获取订阅的 Topic 的所有 partition 
    newDiscoveredPartitions = getAllPartitionsForTopics(topicsDescriptor.getFixedTopics());

    // 剔除 旧的 partition 和 不应该被该 subtask 去消费的 partition
    Iterator<KafkaTopicPartition> iter = newDiscoveredPartitions.iterator();
    KafkaTopicPartition nextPartition;
    while (iter.hasNext()) {
        nextPartition = iter.next();
        // setAndCheckDiscoveredPartition 方法设计比较巧妙，
          // 将旧的 partition 和 不应该被该 subtask 消费的 partition，返回 false
        // 将这些partition 剔除，就是新发现的 partition
        if (!setAndCheckDiscoveredPartition(nextPartition)) {
            iter.remove();
        }
    }
    return newDiscoveredPartitions;
}

// discoveredPartitions 中存放着所有发现的 partition
private Set<KafkaTopicPartition> discoveredPartitions = new HashSet<>();

// setAndCheckDiscoveredPartition 方法实现
// 当参数的 partition 是新发现的 partition 且应该被当前 subtask 消费时，返回 true
// 旧的 partition 和 不应该被该 subtask 消费的 partition，返回 false
public boolean setAndCheckDiscoveredPartition(KafkaTopicPartition partition) {
    // discoveredPartitions 中不存在，表示发现了新的 partition，将其加入到 discoveredPartitions  
    if (!discoveredPartitions.contains(partition)) {
        discoveredPartitions.add(partition);
        // 再通过分配器来判断该 partition 是否应该被当前 subtask 去消费
        return KafkaTopicPartitionAssigner.assign(partition, numParallelSubtasks) == indexOfThisSubtask;
    }

    return false;
}
```

上述代码中依赖 Set 类型的 discoveredPartitions 来判断 partition 是否是新的 partition，刚开始 discoveredPartitions 是一个空的 Set，所以任务初始化第一次调用发现器的 discoverPartitions 方法时，会把所有属于当前 subtask 的 partition 都返回，来保证所有属于当前 subtask 的 partition 都能被消费到。之后任务运行过程中，若创建了新的 partition，则新 partition 对应的那一个 subtask 会自动发现并从 earliest 位置开始消费，新创建的 partition 对其他 subtask 并不会产生影响。

### 小结与反思

本节分为三部分来讲述 Flink 如何保证 Exactly Once，第一部分讲了 Flink 内部如何保证 Exactly Once 并着重介绍了 barrier 对齐。第二部分讲了端对端如何保证 Exactly Once，主要通过幂等性和两阶段提交两种方案。当出现故障时 Flink 任务要从 Checkpoint 处恢复，所以在第三部分分析 FlinkKafkaConsumer 的实现原理，讲述了 FlinkKafkaConsumer 是如何维护 offset 并从之前保存的 offset 处开始消费。你们平时设计的 Connector 能保证 Exactly Once 吗？