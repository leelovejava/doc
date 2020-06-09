## 如何使用 Flink ParameterTool 读取配置？

在使用 Flink 中不知道你有没有觉得配置的管理很不方便，比如像算子的并行度配置、Kafka 数据源的配置（broker 地址、topic 名、group.id）、Checkpoint 是否开启、状态后端存储路径、数据库地址、用户名和密码等，反正各种各样的配置都杂乱在一起，当然你可能说我就在代码里面写死不就好了，但是你有没有想过你的作业是否可以不修改任何配置就直接在各种环境（开发、测试、预发、生产）运行呢？可能每个环境的这些配置对应的值都是不一样的，如果你是直接在代码里面写死的配置，那这下子就比较痛苦了，每次换个环境去运行测试你的作业，你都要重新去修改代码中的配置，然后编译打包，提交运行，这样你就要花费很多时间在这些重复的劳动力上了。有没有什么办法可以解决这种问题呢？

### Flink Job 配置

在 Flink 中其实是有几种方法来管理配置，下面分别来讲解一下。

#### 使用 Configuration

Flink 提供了 withParameters 方法，它可以传递 Configuration 中的参数给，要使用它，需要实现那些 Rich 函数，比如实现 RichMapFunction，而不是 MapFunction，因为 Rich 函数中有 open 方法，然后可以重写 open 方法通过 Configuration 获取到传入的参数值。

```java
ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
// Configuration 类来存储参数
Configuration configuration = new Configuration();
configuration.setString("name", "zhisheng");

env.fromElements(WORDS)
        .flatMap(new RichFlatMapFunction<String, Tuple2<String, Integer>>() {

            String name;

            @Override
            public void open(Configuration parameters) throws Exception {
                //读取配置
                name = parameters.getString("name", "");
            }

            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] splits = value.toLowerCase().split("\\W+");

                for (String split : splits) {
                    if (split.length() > 0) {
                        out.collect(new Tuple2<>(split + name, 1));
                    }
                }
            }
        }).withParameters(configuration)    //将参数传递给函数
        .print();
```

但是要注意这个 withParameters 只在批程序中支持，流程序中是没有该方法的，并且这个 withParameters 要在每个算子后面使用才行，并不是一次使用就所有都可以获取到，如果所有算子都要该配置，那么就重复设置多次就会比较繁琐。

### ParameterTool 管理配置

上面通过 Configuration 的局限性很大，其实在 Flink 中还可以通过使用 ParameterTool 类读取配置，它可以读取环境变量、运行参数、配置文件，下面分别讲下每种如何使用。

#### 读取运行参数

我们知道 Flink UI 上是支持为每个 Job 单独传入 arguments（参数）的，它的格式要求是如下这种。

```
--brokers 127.0.0.1:9200
--username admin
--password 123456
```

或者这种

```
-brokers 127.0.0.1:9200
-username admin
-password 123456
```

然后在 Flink 程序中你可以直接使用 `ParameterTool.fromArgs(args)` 获取到所有的参数，然后如果你要获取某个参数对应的值的话，可以通过 `parameterTool.get("username")` 方法。那么在这个地方其实你就可以将配置放在一个第三方的接口，然后这个参数值中传入一个接口，拿到该接口后就能够通过请求去获取更多你想要的配置。

#### 读取系统属性

ParameterTool 还支持通过 `ParameterTool.fromSystemProperties()` 方法读取系统属性。

#### 读取配置文件

除了上面两种外，ParameterTool 还支持 `ParameterTool.fromPropertiesFile("/application.properties")` 读取 properties 配置文件。你可以将所有要配置的地方（比如并行度和一些 Kafka、MySQL 等配置）都写成可配置的，然后其对应的 key 和 value 值都写在配置文件中，最后通过 ParameterTool 去读取配置文件获取对应的值。

#### ParameterTool 获取值

ParameterTool 类提供了很多便捷方法去获取值。

![img](http://zhisheng-blog.oss-cn-hangzhou.aliyuncs.com/img/2019-10-09-134119.png)

你可以在应用程序的 main() 方法中直接使用这些方法返回的值，例如：你可以按如下方法来设置一个算子的并行度：

```java
ParameterTool parameters = ParameterTool.fromArgs(args);
int parallelism = parameters.get("mapParallelism", 2);
DataStream<Tuple2<String, Integer>> counts = data.flatMap(new Tokenizer()).setParallelism(parallelism);
```

因为 ParameterTool 是可序列化的，所以你可以将它当作参数进行传递给自定义的函数。

```java
ParameterTool parameters = ParameterTool.fromArgs(args);
DataStream<Tuple2<String, Integer>> counts = dara.flatMap(new Tokenizer(parameters));
```

然后在函数内部使用 ParameterTool 来获取命令行参数，这样就意味着你在作业任何地方都可以获取到参数，而不是像 withParameters 一样需要每次都设置。

#### 注册全局参数

在 ExecutionConfig 中可以将 ParameterTool 注册为全作业参数的参数，这样就可以被 JobManager 的 web 端以及用户自定义函数中以配置值的形式访问。

```java
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env.getConfig().setGlobalJobParameters(ParameterTool.fromArgs(args));
```

然后就可以在用户自定义的 Rich 函数中像如下这样获取到参数值了。

```java
env.addSource(new RichSourceFunction<String>() {
    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        while (true) {
            ParameterTool parameterTool = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
            sourceContext.collect(System.currentTimeMillis() + parameterTool.get("os.name") + parameterTool.get("user.home"));
        }
    }

    @Override
    public void cancel() {
    }
})
```

在笔者公司内通常是以 Job 运行的环境变量为准，比如我们是运行在 K8s 上面，那么我们会为我们的这个 Flink Job 设置很多环境变量，设置的环境变量的值就得通过 ParameterTool 类去获取，我们是会优先根据环境变量的值为准，如果环境变量的值没有就会去读取应用运行参数，如果应用运行参数也没有才会去读取之前已经写好在配置文件中的配置。大概代码如下：

```java
public static ParameterTool createParameterTool(final String[] args) throws Exception {
    return ParameterTool
            .fromPropertiesFile(ExecutionEnv.class.getResourceAsStream("/application.properties"))
            .mergeWith(ParameterTool.fromArgs(args))
            .mergeWith(ParameterTool.fromSystemProperties())
            .mergeWith(ParameterTool.fromMap(getenv()));// mergeWith 会使用最新的配置
}

//获取 Job 设置的环境变量
private static Map<String, String> getenv() {
    Map<String, String> map = new HashMap<>();
    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
        map.put(entry.getKey().toLowerCase().replace('_', '.'), entry.getValue());
    }
    return map;
}
```

这样如果 Job 要更改一些配置，直接在 Job 在 K8s 上面的环境变量进行配置就好了，修改配置后然后重启 Job 就可以运行起来了，整个过程都不需要再次将作业重新编译打包的。但是这样其实也有一定的坏处，重启一个作业的代价很大，因为在重启后你又要去保证状态要恢复到之前未重启时的状态，尽管 Flink 中的 Checkpoint 和 Savepoint 已经很强大了，但是对于复杂的它来说我们多一事不如少一事，所以其实更希望能够直接动态的获取配置，如果配置做了更改，作业能够感知到。在 Flink 中有的配置是不能够动态设置的，但是比如应用业务配置却是可以做到动态的配置，这时就需要使用比较强大的广播变量，广播变量在之前 3.4 节已经介绍过了，如果忘记可以再回去查看，另外在 11.4 节中会通过一个实际案例来教你如何使用广播变量去动态的更新配置数据。

### ParameterTool 源码剖析

ParameterTool 这个类还是比较简单的，它继承自 ExecutionConfig.GlobalJobParameters 类，然后提供了上面讲的哪几种方法去获取配置数据：

- fromArgs(String[] args)
- fromPropertiesFile(String path)
- fromPropertiesFile(File file)
- fromPropertiesFile(InputStream inputStream)
- fromSystemProperties()

还可以传入的一个 `Map` 配置进去，这样最后也是返回一个 ParameterTool 对象。另外就是提供了好些个 get() 方法去获取不同类型的参数值，也支持通过 mergeWith 方法来将两个不同的 ParameterTool 类进行合并，优先以新传入的参数为准，因为内部是使用的 Map 来存储的，mergeWith 操作会将新的 ParameterTool 数据全部 putAll 进一个 Map 集合中，所以会覆盖前一个相同 key 的值。

### 小结与反思

本节讲了 Flink 作业中通常怎么去管理配置，然后使用 ParameterTool 去读取配置变量和获取配置变量，对 ParameterTool 的使用做了个完整的讲解。