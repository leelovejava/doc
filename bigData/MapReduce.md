# 分布式计算框架MapReduce

## 大纲

### MapReduce框架原理
* MapReduce核心思想
* MapReduce进程
* MapReduce编程规范（八股文）
* MapReduce程序运行流程分析
* MapReduce工作流程
* 常用数据序列化类型
* 自定义bean对象实现序列化接口
* FileInputFormat切片机制
* CombineTextInputFormat切片机制
* 自定义InputFormat

### Shuffle机制
* MapTask工作机制
* 并行度决定机制
* Shuffle机制
* Partition分区
* WritableComparable排序
* GroupingComparator分组（辅助排序）
* Combiner合并
* 数据倾斜&Distributedcache
* ReduceTask工作机制
* 自定义OutputFormat

### 数据压缩 & Yarn
* MapReduce支持的压缩编码
* 采用压缩的位置
* 压缩配置参数
* 计数器应用、数据清洗
* Yarn基本架构、工作机制
* Yarn资源调度器、任务推测执行
* MapReduce作业提交全过程
* MapReduce开发总结
* MapReduce参数优化
* 企业高频真题讲解20道

### MapReduce案例（一）
* 案例一：统计一堆文件中单词出现的个数
* 案例二：把单词按照ASCII码奇偶分区
* 案例三：对每一个maptask的输出局部汇总
* 案例四：大量小文件的切片优化
* 案例五：统计手机号耗费的流量
* 案例六：按照手机归属地不同省份输出到不同文件中
* 案例七：按照总流量倒序排序
* 案例八：不同省份输出文件内部排序
* 案例九：求每个订单中最贵的商品
* 案例十：Reduce端表合并（数据倾斜）

### MapReduce案例（二）
* 案例十一：Map端表合并（Distributedcache）
* 案例十二：小文件处理（自定义InputFormat）
* 案例十三：自定义日志输出路径（自定义OutputFormat）
* 案例十四：日志清洗（数据清洗）
* 案例十五：倒排索引（多job串联）
* 案例十六：找博客共同好友分析
* 案例十七：对数据流的压缩和解压缩
* 案例十八：在Map输出端采用压缩
* 案例十九：在Reduce输出端采用压缩
* 案例二十：TopN案例

## Overview
spark性能更好,更流行

## 特点
* 易于扩展
* 高扩展性
* 高容错性(宕机,转移节点运行)
* 海量数据的离线处理

### 不适用场景
* 实时计算
* 流式计算(MapReduce输入的数据是静态)->Spark/Storm
* DAG计算(工作流,上一个作业的输出作为下一个作业的输入)

### 缺点
1）代码繁琐；
2）只能够支持map和reduce方法；
3）执行效率低下；
4）不适合迭代多次、交互式、流式的处理；