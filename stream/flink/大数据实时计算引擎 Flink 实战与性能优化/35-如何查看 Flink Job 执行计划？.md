## 如何查看 Flink Job 执行计划？

当一个应用程序需求比较简单的情况下，数据转换涉及的 operator（算子）可能不多，但是当应用的需求变得越来越复杂时，可能在一个 Job 里面算子的个数会达到几十个、甚至上百个，在如此多算子的情况下，整个应用程序就会变得非常复杂，所以在编写 Flink Job 的时候要是能够随时知道 Job 的执行计划那就很方便了。

刚好，Flink 是支持可以获取到整个 Job 的执行计划的，另外 Flink 官网还提供了一个可视化工具 visualizer（可以将执行计划 JSON 绘制出执行图）。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093014.jpg)

### 如何获取执行计划 JSON？

既然知道了将执行计划 JSON 绘制出可查看的执行图的工具，那么该如何获取执行计划 JSON 呢？方法很简单，你只需要在你的 Flink Job 的 Main 方法 里面加上这么一行代码：

```java
System.out.println(env.getExecutionPlan());
```

然后就可以在 IDEA 中右键 Run 一下你的 Flink Job，从打印的日志里面可以查看到执行计划的 JSON 串，例如下面这种：

```json
{"nodes":[{"id":1,"type":"Source: Custom Source","pact":"Data Source","contents":"Source: Custom Source","parallelism":5},{"id":2,"type":"Sink: flink-connectors-kafka","pact":"Data Sink","contents":"Sink: flink-connectors-kafka","parallelism":5,"predecessors":[{"id":1,"ship_strategy":"FORWARD","side":"second"}]}]}
```

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-23-154219.png)

### 生成执行计划图

获取到执行计划 JSON 了，那么利用 Flink 自带的工具来绘出执行计划图，将获得到的 JSON 串复制粘贴到刚才那网址去。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093059.jpg)

点击上图的 `Draw` 按钮，就会生成下图的执行流程图了：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093114.jpg)

从图中我们可以看到哪些内容呢？

- operator name（算子）：比如 source、sink
- 每个 operator 的并行度：比如 Parallelism: 5
- 数据下发的类型：比如 FORWARD

你还可以点击下图中的 `Data Source(ID = 1)` 查看具体详情：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093132.jpg)

随着需求的不段增加，可能算子的个数会增加，所以执行计划也会变得更为复杂。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093151.jpg)

看到上图是不是觉得就有点很复杂了，笔者相信可能你自己的业务需求比这还会复杂得更多，不过从这图我们可以看到比上面那个简单的执行计划图多了一种数据下发类型就是 HASH。但是大家可能会好奇的说：为什么我平时从 Flink UI 上查看到的 Job ”执行计划图“ 却不是这样子的呀？

这里我们复现一下这个问题，我们把这个稍微复杂的 Flink Job 提交到 Flink UI 上去查看一下到底它在 UI 上的执行计划图是个什么样子？我们提交 Jar 包后不运行，直接点击 show plan 试下：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093209.jpg)

我们再运行一下，查看运行的时候的展示的 “执行计划图” 是什么样的呢？

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093230.jpg)

### 深入探究 Flink Job 执行计划

我们可以发现这两个 “执行计划图” 都和在 Flink 官网提供的 visualizer 工具生成的执行计划图是不一样的。粗略观察可以发现：在 Flink UI 上面的 “执行计划图” 变得更加简洁了，有些算子合在一起了，所以整体看起来就没这么复杂了。其实，这是 Flink 内部做的一个优化。我们先来看下 env.getExecutionPlan() 这段代码它背后的逻辑：

```java
/**
 * Creates the plan with which the system will execute the program, and
 * returns it as a String using a JSON representation of the execution data
 * flow graph. Note that this needs to be called, before the plan is
 * executed.
 *
 * @return The execution plan of the program, as a JSON String.
 */
public String getExecutionPlan() {
    return getStreamGraph().getStreamingPlanAsJSON();
}
```

代码注释的大概意思是：

> 创建程序执行计划，并将执行数据流图的 JSON 作为 String 返回，请注意，在执行计划之前需要调用此方法。

这个 getExecutionPlan 方法有两步操作：

1、获取到 Job 的 StreamGraph

关于如何获取到 StreamGraph，笔者在博客里面写了篇源码解析 [源码解析——如何获取 StreamGraph？](https://t.zsxq.com/qRFIm6I) 。

2、将 StreamGraph 转换成 JSON

```java
public String getStreamingPlanAsJSON() {
    try {
        return new JSONGenerator(this).getJSON();
    }
    catch (Exception e) {
        throw new RuntimeException("JSON plan creation failed", e);
    }
}
```

跟进 getStreamingPlanAsJSON 方法看见它构造了一个 JSONGenerator 对象（含参 StreamGraph），然后调用 getJSON 方法，我们来看下这个方法：

```java
public String getJSON() {
    ObjectNode json = mapper.createObjectNode();
    ArrayNode nodes = mapper.createArrayNode();
    json.put("nodes", nodes);
    List<Integer> operatorIDs = new ArrayList<Integer>(streamGraph.getVertexIDs());
    Collections.sort(operatorIDs, new Comparator<Integer>() {
        @Override
        public int compare(Integer idOne, Integer idTwo) {
            boolean isIdOneSinkId = streamGraph.getSinkIDs().contains(idOne);
            boolean isIdTwoSinkId = streamGraph.getSinkIDs().contains(idTwo);
            // put sinks at the back
            ...
        }
    });
    visit(nodes, operatorIDs, new HashMap<Integer, Integer>());
    return json.toString();
}
```

一开始构造外部的对象，然后调用 visit 方法继续构造内部的对象，visit 方法如下：

```java
private void visit(ArrayNode jsonArray, List<Integer> toVisit,
        Map<Integer, Integer> edgeRemapings) {

    Integer vertexID = toVisit.get(0);
    StreamNode vertex = streamGraph.getStreamNode(vertexID);

    if (streamGraph.getSourceIDs().contains(vertexID)
            || Collections.disjoint(vertex.getInEdges(), toVisit)) {

        ObjectNode node = mapper.createObjectNode();
        decorateNode(vertexID, node);

        if (!streamGraph.getSourceIDs().contains(vertexID)) {
            ArrayNode inputs = mapper.createArrayNode();
            node.put(PREDECESSORS, inputs);

            for (StreamEdge inEdge : vertex.getInEdges()) {
                int inputID = inEdge.getSourceId();

                Integer mappedID = (edgeRemapings.keySet().contains(inputID)) ? edgeRemapings
                        .get(inputID) : inputID;
                decorateEdge(inputs, inEdge, mappedID);
            }
        }
        jsonArray.add(node);
        toVisit.remove(vertexID);
    } else {
        Integer iterationHead = -1;
        for (StreamEdge inEdge : vertex.getInEdges()) {
            int operator = inEdge.getSourceId();

            if (streamGraph.vertexIDtoLoopTimeout.containsKey(operator)) {
                iterationHead = operator;
            }
        }

        ObjectNode obj = mapper.createObjectNode();
        ArrayNode iterationSteps = mapper.createArrayNode();
        obj.put(STEPS, iterationSteps);
        obj.put(ID, iterationHead);
        obj.put(PACT, "IterativeDataStream");
        obj.put(PARALLELISM, streamGraph.getStreamNode(iterationHead).getParallelism());
        obj.put(CONTENTS, "Stream Iteration");
        ArrayNode iterationInputs = mapper.createArrayNode();
        obj.put(PREDECESSORS, iterationInputs);
        toVisit.remove(iterationHead);
        visitIteration(iterationSteps, toVisit, iterationHead, edgeRemapings, iterationInputs);
        jsonArray.add(obj);
    }

    if (!toVisit.isEmpty()) {
        visit(jsonArray, toVisit, edgeRemapings);
    }
}
```

最后就将这个 StreamGraph 构造成一个 JSON 串返回出去，所以其实这里返回的执行计划图就是 Flink Job 的 StreamGraph，然而我们在 Flink UI 上面看到的 "执行计划图" 是对应 Flink 中的 JobGraph，同样，笔者在博客里面也写了篇源码解析的文章 [源码解析——如何获取 JobGraph？](https://t.zsxq.com/naaMf6y)。

### Flink Job Operator chain 的条件

Flink 在内部会将多个算子串在一起作为一个 operator chain（执行链）来执行，每个执行链会在 TaskManager 上的一个独立线程中执行，这样不仅可以减少线程的数量及线程切换带来的资源消耗，还能降低数据在算子之间传输序列化与反序列化带来的消耗。

举个例子，拿一个 Flink Job （算子的并行度都设置为 5）生成的 StreamGraph JSON 渲染出来的执行流程图是下图这样的：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093302.jpg)

提交到 Flink UI 上的 JobGraph 是下图这样的：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093318.jpg)

可以看到 Flink 它内部将三个算子（source、filter、sink）都串成在一个执行链里。但是我们修改一下 filter 这个算子的并行度为 4，我们再次提交到 Flink UI 上运行，效果如下图：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093342.jpg)

你会发现它竟然拆分成三个了，我们继续将 sink 的并行度也修改成 4，继续打包运行后的效果如下图：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-08-27-093356.jpg)

神奇不，它变成了 2 个了，将 filter 和 sink 算子串在一起了执行了。经过简单的测试，我们可以发现其实如果想要把两个不一样的算子串在一起执行确实还不是那么简单的，的确，它背后的条件可是比较复杂的，这里笔者给出源码出来，感兴趣的可以独自阅读下源码。

```java
public static boolean isChainable(StreamEdge edge, StreamGraph streamGraph) {
    //获取StreamEdge的源和目标StreamNode
    StreamNode upStreamVertex = edge.getSourceVertex();
    StreamNode downStreamVertex = edge.getTargetVertex();

    //获取源和目标StreamNode中的StreamOperator
    StreamOperator<?> headOperator = upStreamVertex.getOperator();
    StreamOperator<?> outOperator = downStreamVertex.getOperator();

    return downStreamVertex.getInEdges().size() == 1
            && outOperator != null
            && headOperator != null
            && upStreamVertex.isSameSlotSharingGroup(downStreamVertex)
            && outOperator.getChainingStrategy() == ChainingStrategy.ALWAYS
            && (headOperator.getChainingStrategy() == ChainingStrategy.HEAD ||
                headOperator.getChainingStrategy() == ChainingStrategy.ALWAYS)
            && (edge.getPartitioner() instanceof ForwardPartitioner)
            && upStreamVertex.getParallelism() == downStreamVertex.getParallelism()
            && streamGraph.isChainingEnabled();
}
```

从源码最后的 return 可以看出它有九个条件：

- 下游节点只有一个输入
- 下游节点的操作符不为 null
- 上游节点的操作符不为 null
- 上下游节点在一个槽位共享组（slotsharinggroup）内，默认是 default
- 下游节点的连接策略是 ALWAYS（可以与上下游节点连接）
- 上游节点的连接策略是 HEAD 或者 ALWAYS
- edge 的分区函数是 ForwardPartitioner 的实例（没有 keyby 等操作）
- 上下游节点的并行度相等
- 允许进行节点连接操作（默认允许）

所以看到上面的这九个条件，你是不是在想如果我们代码能够合理的写好，那么就有可能会将不同的算子串在一个执行链中，这样也就可以提高代码的执行效率了。

### 如何禁止 Operator chain？

将算子 chain 起来可以获得不少性能的提高，如果可以的话，Flink 也会默认进行算子的 chain，那么如果要禁止算子的 chain 你该怎么做呢？

```java
env.disableOperatorChaining()
```

如下图是 word count 程序默认的执行图：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-06-114825.png)

设置禁止 chain 后的执行图：

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-06-115100.png)

可以看到设置禁止 chain 后的执行图中每个算子都是隔离的，另外可以看到禁止 chain 后整个 Job 的 task 变多了，并且整个 Job 的执行时间也要更久了。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-06-115150.png)

除了设置全局的算子不可以 chain 在一起，也可以单独设置某个算子不能 chain 在一起，如下设置后 flatMap 算子则不会和前面和后面的算子 chain 在一起。

```java
dataStream.flatMap(...).disableChaining();
```

另外还可以设置开启新的 chain，如下这种情况会将 flatMap 和 map 算子进行 chain 在一起，但是 filter 算子不会 chain 在一起。

```java
dataStream.filter(...).flatMap(...).startNewChain().map(...);
```

除了上面这几种，还可以设置共享的 Slot 组，比如将两个相隔的算子设置相同的 Slot 共享组，那么它会将该两个算子 chain 在一起，这样可以用来进行 Slot 隔离，如下这种情况 filter 算子会和 flatMap 算子 chain 在同一个 Slot 里面，而 map 算子则会在另一个 Slot 里面。注意：这种情况下要两个相邻的算子设置同一个 Slot 共享组才会进行算子 chain，如果是隔开的算子即使设置相同的 Slot 共享组也是不会将两个算子进行 Chain 在一起。

```java
dataStream.filter(...).slotSharingGroup("zhisheng").flatMap(...).slotSharingGroup("zhisheng").map(...).slotSharingGroup("zhisheng01");
```

### 小结与反思

本节内容从查看作业的执行计划来分析作业的执行情况，接着分析了作业算子 chain 起来的条件，并通过程序演示来验证，最后讲解了如何禁止算子 chain 起来。

备注：本节代码在 https://github.com/zhisheng17/flink-learning/tree/master/flink-learning-examples/src/main/java/com/zhisheng/examples/streaming/chain

Job visualizer 工具：https://flink.apache.org/visualizer/

源码解析——如何获取 StreamGraph：http://www.54tianzhisheng.cn/2019/03/20/Flink-code-StreamGraph/

[下一](https://gitbook.cn/gitchat/column/5dad4a20669f843a1a37cb4f/topic/5db6bf5cf6a6211cb961664b)