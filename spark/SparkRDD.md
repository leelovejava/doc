# Spark计算模型

## 1.课程目标
### 1.1.熟练使用RDD的算子完成计算
### 1.2.掌握RDD的原理

## 2.弹性分布式数据集RDD
### 2.1.RDD概述

### 2.1.1.什么是RDD
RDD（Resilient Distributed Dataset）叫做分布式数据集，是Spark中最基本的数据抽象，它代表一个不可变、可分区、里面的元素可并行计算的集合。RDD具有数据流模型的特点：自动容错、位置感知性调度和可伸缩性。
RDD允许用户在执行多个查询时显式地将工作集缓存在内存中，后续的查询能够重用工作集，这极大地提升了查询速度。
[rdd-programming-guide](http://spark.apache.org/docs/latest/rdd-programming-guide.html)

### 2.1.2.RDD的属性

1）一组分片（Partition），即数据集的基本组成单位。对于RDD来说，每个分片都会被一个计算任务处理，并决定并行计算的粒度。用户可以在创建RDD时指定RDD的分片个数，如果没有指定，那么就会采用默认值。默认值就是程序所分配到的CPU Core的数目。

2）一个计算每个分区的函数。Spark中RDD的计算是以分片为单位的，每个RDD都会实现compute函数以达到这个目的。compute函数会对迭代器进行复合，不需要保存每次计算的结果。

3）RDD之间的依赖关系。RDD的每次转换都会生成一个新的RDD，所以RDD之间就会形成类似于流水线一样的前后依赖关系。在部分分区数据丢失时，Spark可以通过这个依赖关系重新计算丢失的分区数据，而不是对RDD的所有分区进行重新计算。

4）一个Partitioner，即RDD的分片函数。当前Spark中实现了两种类型的分片函数，一个是基于哈希的HashPartitioner，另外一个是基于范围的RangePartitioner。只有对于于key-value的RDD，才会有Partitioner，非key-value的RDD的Parititioner的值是None。Partitioner函数不但决定了RDD本身的分片数量，也决定了parent RDD Shuffle输出时的分片数量。

5）一个列表，存储存取每个Partition的优先位置（preferred location）。对于一个HDFS文件来说，这个列表保存的就是每个Partition所在的块的位置。按照“移动数据不如移动计算”的理念，Spark在进行任务调度的时候，会尽可能地将计算任务分配到其所要处理数据块的存储位置。

### 2.2.创建RDD
1）由一个已经存在的Scala集合创建。
    val rdd1 = sc.parallelize(Array(1,2,3,4,5,6,7,8))

2）由外部存储系统的数据集创建，包括本地的文件系统，还有所有Hadoop支持的数据集，比如HDFS、Cassandra、HBase等
    val rdd2 = sc.textFile("hdfs://node1.itcast.cn:9000/words.txt")

3）查看该rdd的分区数量，默认是程序所分配的cpu core的数量，也可以在创建的时候指定
     rdd1.partitions.length
    创建的时候指定分区数量：
      val rdd1 = sc.parallelize(Array(1,2,3.4),3)
      
### 2.3.RDD编程API

#### 2.3.1.Transformation
RDD中的所有转换都是*延迟加载*的，也就是说，它们并不会直接计算结果。
相反的，它们只是记住这些应用到基础数据集（例如一个文件）上的转换动作。
只有当发生一个要求返回结果给Driver的动作时，这些转换才会真正运行。这种设计让Spark更加有效率地运行。

#### 常用的Transformation：
    
* map(func)	返回一个新的RDD，该RDD由每一个输入元素经过func函数转换后组成
```
val rdd1 = sc.parallelize(List(5,6,4,7,3,8,2,9,1,10))
```

* filter(func)	返回一个新的RDD，该RDD由经过func函数计算后返回值为true的输入元素组成
```
val rdd2 = sc.parallelize(List(5,6,4,7,3,8,2,9,1,10)).map(_*2).sortBy(x=>x,true)
val rdd3 = rdd2.filter(_>10)
```

* flatMap(func)	类似于map，但是每一个输入元素可以被映射为0或多个输出元素（所以func应该返回一个序列，而不是单一元素）(所以func应该返回一个序列，而不是单一元素）。类似于先map，然后再flatten)
```
val rdd4 = sc.parallelize(Array("a b c", "d e f", "h i j"))
rdd4.flatMap(_.split(' ')).collect
// abcdefhij
------------------------------------------------------------------
val rdd5 = sc.parallelize(List(List("a b c", "a b b"),List("e f g", "a f g"), List("h i j", "a a b")))
rdd5.flatMap(_.flatMap(_.split(" "))).collect
```

* sample(withReplacement, fraction, seed)	根据fraction指定的比例对数据进行采样，可以选择是否使用随机数进行替换，seed用于指定随机数生成器种子

* union(otherDataset)	对源RDD和参数RDD求并集后返回一个新的RDD

* intersection(otherDataset)	对源RDD和参数RDD求交集后返回一个新的RDD

* join、leftOuterJoin、rightOuterJoin
```
val rdd1 = sc.parallelize(List(("tom", 1), ("jerry", 2), ("kitty", 3)))
val rdd2 = sc.parallelize(List(("jerry", 9), ("tom", 8), ("shuke", 7)))
--------------------------------------------------------------------------
val rdd3 = rdd1.join(rdd2).collect
rdd3: Array[(String, (Int, Int))] = Array((tom,(1,8)), (jerry,(2,9)))
---------------------------------------------------------------------------
val rdd3 = rdd1.leftOuterJoin(rdd2).collect
rdd3: Array[(String, (Int, Option[Int]))] = Array((tom,(1,Some(8))), (jerry,(2,Some(9))), (kitty,(3,None)))
---------------------------------------------------------------------------
val rdd3 = rdd1.rightOuterJoin(rdd2).collect
rdd3: Array[(String, (Option[Int], Int))] = Array((tom,(Some(1),8)), (jerry,(Some(2),9)), (shuke,(None,7)))
```

* distinct([numTasks]))	对源RDD进行去重后返回一个新的RDD
```
val rdd6 = sc.parallelize(List(5,6,4,7))
val rdd7 = sc.parallelize(List(1,2,3,4))
val rdd8 = rdd6.union(rdd7)
rdd8.distinct.sortBy(x=>x).collect
```

* groupBy：传入一个参数的函数，按照传入的参数为key，返回一个新的RDD[(K, Iterable[T])]，value是所有可以相同的传入数据组成的迭代器
```
/**
* Return an RDD of grouped items. Each group consists of a key and a sequence of elements
* mapping to that key. The ordering of elements within each group is not guaranteed, and
* may even differ each time the resulting RDD is evaluated.
*
* @note This operation may be very expensive. If you are grouping in order to perform an
* aggregation (such as a sum or average) over each key, using `PairRDDFunctions.aggregateByKey`
* or `PairRDDFunctions.reduceByKey` will provide much better performance.
*/
def groupBy[K](f: T => K)(implicit kt: ClassTag[K]): RDD[(K, Iterable[T])] = withScope {
  groupBy[K](f, defaultPartitioner(this))
}
```
```
scala> val rdd1=sc.parallelize(List(("a",1,2),("b",1,1),("a",4,5)))
rdd1: org.apache.spark.rdd.RDD[(String, Int, Int)] = ParallelCollectionRDD[47] at parallelize at <console>:24

scala> rdd1.groupBy(_._1).collect
res18: Array[(String, Iterable[(String, Int, Int)])] = Array((a,CompactBuffer((a,1,2), (a,4,5))), (b,CompactBuffer((b,1,1))))
```

* groupByKey([numTasks])		在一个(K,V)的RDD上调用，返回一个(K, Iterator[V])的RDD,只针对数据是对偶元组的
```
val rdd1 = sc.parallelize(List(("tom", 1), ("jerry", 2), ("kitty", 3)))
val rdd2 = sc.parallelize(List(("jerry", 9), ("tom", 8), ("shuke", 7)))
val rdd3 = rdd1 union rdd2
val rdd4 = rdd3.groupByKey.collect
rdd4: Array[(String, Iterable[Int])] = Array((tom,CompactBuffer(8, 1)), (shuke,CompactBuffer(7)), (kitty,CompactBuffer(3)), (jerry,CompactBuffer(9, 2)))
-----------------------------------------------------------------------------------
val rdd5 = rdd4.map(x=>(x._1,x._2.sum))
rdd5: Array[(String, Int)] = Array((tom,9), (shuke,7), (kitty,3), (jerry,11))
```

* reduceByKey(func, [numTasks])	在一个(K,V)的RDD上调用，返回一个(K,V)的RDD，使用指定的reduce函数，将相同key的值聚合到一起，与groupByKey类似，reduce任务的个数可以通过第二个可选的参数来设置
```
val rdd1 = sc.parallelize(List(("tom", 1), ("jerry", 2), ("kitty", 3)))
val rdd2 = sc.parallelize(List(("jerry", 9), ("tom", 8), ("shuke", 7)))
val rdd3 = rdd1 union rdd2
val rdd6 = rdd3.reduceByKey(_+_).collect
rdd6: Array[(String, Int)] = Array((tom,9), (shuke,7), (kitty,3), (jerry,11))
```

* sortByKey([ascending], [numTasks])	在一个(K,V)的RDD上调用，K必须实现Ordered接口，返回一个按照key进行排序的(K,V)的RDD

* sortBy(func,[ascending], [numTasks])	与sortByKey类似，但是更灵活(默认方式为false，升序；true是降序)
```
val rdd2 = sc.parallelize(List(5,6,4,7,3,8,2,9,1,10)).map(_*2).sortBy(x=>x,true)
val rdd2 = sc.parallelize(List(5,6,4,7,3,8,2,9,1,10)).map(_*2).sortBy(x=>x+"",true)
val rdd2 = sc.parallelize(List(5,6,4,7,3,8,2,9,1,10)).map(_*2).sortBy(x=>x.toString,true)
```
* join(otherDataset, [numTasks])	在类型为(K,V)和(K,W)的RDD上调用，返回一个相同key对应的所有元素对在一起的(K,(V,W))的RDD

* cogroup(otherDataset, [numTasks])	在类型为(K,V)和(K,W)的RDD上调用，返回一个(K,(Iterable<V>,Iterable<W>))类型的RDD
```
val rdd1 = sc.parallelize(List(("tom", 1), ("tom", 2), ("jerry", 3), ("kitty", 2)))
val rdd2 = sc.parallelize(List(("jerry", 2), ("tom", 1), ("shuke", 2)))
val rdd3 = rdd1.cogroup(rdd2).collect
rdd3: Array[(String, (Iterable[Int], Iterable[Int]))] = Array((tom,(CompactBuffer(2, 1),CompactBuffer(1))), (jerry,(CompactBuffer(3),CompactBuffer(2))), (shuke,(CompactBuffer(),CompactBuffer(2))), (kitty,(CompactBuffer(2),CompactBuffer())))
----------------------------------------------------------------------------------------
val rdd4 = rdd3.map(x=>(x._1,x._2._1.sum+x._2._2.sum))
rdd4: Array[(String, Int)] = Array((tom,4), (jerry,5), (shuke,2), (kitty,2))
```

* cartesian(otherDataset)	笛卡尔积
```
val rdd1 = sc.parallelize(List("tom", "jerry"))
val rdd2 = sc.parallelize(List("tom", "kitty", "shuke"))
val rdd3 = rdd1.cartesian(rdd2).collect
rdd3: Array[(String, String)] = Array((tom,tom), (tom,kitty), (tom,shuke), (jerry,tom), (jerry,kitty), (jerry,shuke))
```

* pipe(command, [envVars])		

##### 高级API

* mapPartitions(func)	针对每个分区进行操作,类似于map，但独立地在RDD的每一个分片上运行，因此在类型为T的RDD上运行时，func的函数类型必须是Iterator[T] => Iterator[U]
```
/**
* Return a new RDD by applying a function to each partition of this RDD.
*
* `preservesPartitioning` indicates whether the input function preserves the partitioner, which
* should be `false` unless this is a pair RDD and the input function doesn't modify the keys.
*/
def mapPartitions[U: ClassTag](
    f: Iterator[T] => Iterator[U],
    preservesPartitioning: Boolean = false): RDD[U] = withScope {
  val cleanedF = sc.clean(f)
  new MapPartitionsRDD(
    this,
    (context: TaskContext, index: Int, iter: Iterator[T]) => cleanedF(iter),
    preservesPartitioning)
}
```

* mapPartitionsWithIndex(func)	针对每个partition操作，把每个partition中的分区号和对应的值拿出来;类似于mapPartitions，但func带有一个整数参数表示分片的索引值，因此在类型为T的RDD上运行时，func的函数类型必须是
(Int, Interator[T]) => Iterator[U]
```
/**
* Return a new RDD by applying a function to each partition of this RDD, while tracking the index
* of the original partition.
*
* `preservesPartitioning` indicates whether the input function preserves the partitioner, which
* should be `false` unless this is a pair RDD and the input function doesn't modify the keys.
preservesPartitioning表示返回RDD是否留有分区器。仅当RDD为K-V型RDD，且key没有被修饰的情况下，可设为true。非K-V型RDD一般不存在分区器；K-V RDD key被修改后，元素将不再满足分区器的分区要求。这些情况下，须设为false，表示返回的RDD没有被分区器分过区。
*/
def mapPartitionsWithIndex[U: ClassTag](-------要求传入一个函数
    f: (Int, Iterator[T]) => Iterator[U],------函数要求传入两个参数
    preservesPartitioning: Boolean = false): RDD[U] = withScope {
  val cleanedF = sc.clean(f)
  new MapPartitionsRDD(
    this,
    (context: TaskContext, index: Int, iter: Iterator[T]) => cleanedF(index, iter),
    preservesPartitioning)
}
```
```
（1）首先自定义一个函数，符合mapPartitionsWithIndex参数要求的函数
scala> val func = (index : Int,iter : Iterator[Int]) => {
     | iter.toList.map(x=>"[PartID:" + index + ",val:" + x + "]").iterator
     | }
func: (Int, Iterator[Int]) => Iterator[String] = <function2>
(2)定义一个算子，分区数为2
scala> val rdd1 = sc.parallelize(List(1,2,3,4,5,6,7,8,9),2)
rdd1: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[0] at parallelize at <console>:24
（3）调用方法，传入自定义的函数
scala> rdd1.mapPartitionsWithIndex(func).collect
res0: Array[String] = Array([PartID:0,val:1], [PartID:0,val:2], [PartID:0,val:3], [PartID:0,val:4], [PartID:1,val:5], [PartID:1,val:6], [PartID:1,val:7], [PartID:1,val:8], [PartID:1,val:9])
```

*  partitionBy：按照传入的参数进行分区，传入的参数为分区的实例对象，可以传入之定义分区的实例或者默认的HashPartitioner;
```
  /**
  * Return a copy of the RDD partitioned using the specified partitioner.
  */
  def partitionBy(partitioner: Partitioner): RDD[(K, V)] = self.withScope {
    if (keyClass.isArray && partitioner.isInstanceOf[HashPartitioner]) {
      throw new SparkException("HashPartitioner cannot partition array keys.")
    }
    if (self.partitioner == Some(partitioner)) {
      self
    } else {
      new ShuffledRDD[K, V, V](self, partitioner)
    }
  }
```

* repartition(numPartitions)	
```
                返回一个新的RDD
               按指定分区数重新分区RDD，存在shuffle。
               当指定的分区数比当前分区数目少时，考虑使用coalesce，这样能够避免shuffle。
scala> val rdd1 = sc.parallelize(Array(1,2,3,4,5,6,7,8),3)
rdd1: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[0] at parallelize at <console>:24

scala> val rdd2 = rdd1.repartition(6)
rdd2: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[4] at repartition at <console>:26

scala> rdd2.partitions.length
res0: Int = 6

scala> val rdd3 = rdd2.coalesce(2,true)
rdd3: org.apache.spark.rdd.RDD[Int] = MapPartitionsRDD[8] at coalesce at <console>:28

scala> rdd3.partitions.length
res1: Int = 2
```

* repartitionAndSortWithinPartitions(partitioner)	

* aggregate 聚合操作
```
/**
* Aggregate the elements of each partition, and then the results for all the partitions, using
* given combine functions and a neutral "zero value". This function can return a different result
* type, U, than the type of this RDD, T. Thus, we need one operation for merging a T into an U
* and one operation for merging two U's, as in scala.TraversableOnce. Both of these functions are
* allowed to modify and return their first argument instead of creating a new U to avoid memory
* allocation.
将RDD中元素聚集，须提供0初值（因为累积元素，所有要提供累积的初值）。先在分区内依照seqOp函数聚集元素（把T类型元素聚集为U类型的分区“结果”），再在分区间按照combOp函数聚集分区计算结果，最后返回这个结果
*
* @param zeroValue the initial value for the accumulated result of each partition for the
*                  `seqOp` operator, and also the initial value for the combine results from
*                  different partitions for the `combOp` operator - this will typically be the
*                  neutral element (e.g. `Nil` for list concatenation or `0` for summation)
* @param seqOp an operator used to accumulate results within a partition
* @param combOp an associative operator used to combine results from different partitions
第一个参数是初始值, 第二个参数:是两个函数[每个函数都是2个参数(第一个参数:先对个个分区进行合并, 第二个:对个个分区合并后的结果再进行合并), 输出一个参数]
*/
def aggregate[U: ClassTag](zeroValue: U)(seqOp: (U, T) => U, combOp: (U, U) => U): U = withScope {
  // Clone the zero value since we will also be serializing it as part of tasks
  var jobResult = Utils.clone(zeroValue, sc.env.serializer.newInstance())
  val cleanSeqOp = sc.clean(seqOp)
  val cleanCombOp = sc.clean(combOp)
  val aggregatePartition = (it: Iterator[T]) => it.aggregate(zeroValue)(cleanSeqOp, cleanCombOp)
  val mergeResult = (index: Int, taskResult: U) => jobResult = combOp(jobResult, taskResult)
  sc.runJob(this, aggregatePartition, mergeResult)
  jobResult
}
```

```
scala> val rdd1 = sc.parallelize(List(1,2,3,4,5,6,7,8,9), 2)
rdd1: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[0] at parallelize at <console>:24
//这里先对连个分区分别进行相加，然后两个的分区相加后的结果再相加得出最后的结果
scala> rdd1.aggregate(0)(_+_,_+_)
res0: Int = 45                                                                 
//先对每个分区比较求出最大值，然后每个分区求出的最大值再相加得出最后的结果
scala> rdd1.aggregate(0)(math.max(_,_),_+_)
res1: Int = 13
//这里需要注意，初始值是每次都要参与运算的，例如下面的代码：分区1是1,2,3,4；初始值为5，则他们比较最大值就是5，分区2是5,6,7,8,9；初始值为5，则他们比较结果最大值就是9；然后再相加，这里初始值也要参与运算，5+（5+9）=19
scala> rdd1.aggregate(5)(math.max(_,_),_+_)
res0: Int = 19
-----------------------------------------------------------------------------------------------
scala> val rdd2 = sc.parallelize(List("a","b","c","d","e","f"),2)
rdd2: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[0] at parallelize at <console>:24
//这里需要注意，由于每个分区计算是并行计算，所以计算出的结果有先后顺序，所以结果会出现两种情况：如下
scala> rdd2.aggregate("")(_+_,_+_)
res0: String = defabc                                                                                                                    

scala> rdd2.aggregate("")(_+_,_+_)
res2: String = abcdef
//这里的例子更能说明上面提到的初始值参与计算的问题，我们可以看到初始值=号参与了三次计算
scala> rdd2.aggregate("=")(_+_,_+_)
res0: String = ==def=abc
--------------------------------------------------------------------------------------
scala> val rdd3 = sc.parallelize(List("12","23","345","4567"),2)
rdd3: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[1] at parallelize at <console>:24

scala> rdd3.aggregate("")((x,y)=>math.max(x.length,y.length).toString,_+_)
res1: String = 42                                                               

scala> rdd3.aggregate("")((x,y)=>math.max(x.length,y.length).toString,_+_)
res3: String = 24
-------------------------------------------------------------------------------------------
scala> val rdd4 = sc.parallelize(List("12","23","345",""),2)
rdd4: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[2] at parallelize at <console>:24
//这里需要注意：第一个分区加上初始值元素为"","12","23",两两比较，最小的长度为1；第二个分区加上初始值元素为"","345","",两两比较，最小的长度为0
scala> rdd4.aggregate("")((x,y)=>math.min(x.length,y.length).toString,_+_)
res4: String = 10                                                               

scala> rdd4.aggregate("")((x,y)=>math.min(x.length,y.length).toString,_+_)
res9: String = 01                                                               
------------------------------------------------------------------------------------
//注意与上面的例子的区别，这里定义的rdd里的元素的顺序跟上面不一样，导致结果不一样
scala> val rdd5 = sc.parallelize(List("12","23","","345"),2)
rdd5: org.apache.spark.rdd.RDD[String] = ParallelCollectionRDD[0] at parallelize at <console>:24

scala> rdd5.aggregate("")((x,y)=>math.min(x.length,y.length).toString,(x,y)=>x+y)
res1: String = 11 
```

* aggregateByKey(zeroValue)(seqOp, combOp, [numTasks])	 按照key值进行聚合
```
//定义RDD
scala> val pairRDD = sc.parallelize(List( ("cat",2), ("cat", 5), ("mouse", 4),("cat", 12), ("dog", 12), ("mouse", 2)), 2)
pairRDD: org.apache.spark.rdd.RDD[(String, Int)] = ParallelCollectionRDD[1] at parallelize at <console>:24
//自定义方法，用于传入mapPartitionsWithIndex
scala> val func=(index:Int,iter:Iterator[(String, Int)])=>{
     | iter.toList.map(x => "[partID:" +  index + ", val: " + x + "]").iterator
     | }
func: (Int, Iterator[(String, Int)]) => Iterator[String] = <function2>
//查看分区情况
scala> pairRDD.mapPartitionsWithIndex(func).collect
res2: Array[String] = Array([partID:0, val: (cat,2)], [partID:0, val: (cat,5)], [partID:0, val: (mouse,4)], [partID:1, val: (cat,12)], [partID:1, val: (dog,12)], [partID:1, val: (mouse,2)])
//注意：初始值为0和其他值的区别
scala> pairRDD.aggregateByKey(0)(_+_,_+_).collect
res4: Array[(String, Int)] = Array((dog,12), (cat,19), (mouse,6))               

scala> pairRDD.aggregateByKey(10)(_+_,_+_).collect
res5: Array[(String, Int)] = Array((dog,22), (cat,39), (mouse,26))
//下面三个的区别：，第一个比较好理解，由于初始值为0，所以每个分区输出不同动物中个数最多的那个，然后在累加
scala> pairRDD.aggregateByKey(0)(math.max(_,_),_+_).collect
res6: Array[(String, Int)] = Array((dog,12), (cat,17), (mouse,6))

//下面两个：由于有初始值，就需要考虑初始值参与计算，这里第一个分区的元素为("cat",2), ("cat", 5), ("mouse", 4)，初始值是10，不同动物之间两两比较value的大小，都需要将初始值加入比较，所以第一个分区输出为("cat", 10), ("mouse", 10)；第二个分区同第一个分区，输出结果为(dog,12), (cat,12), (mouse,10)；所以最后累加的结果为(dog,12), (cat,22), (mouse,20)，注意最后的对每个分区结果计算的时候，初始值不参与计算
scala> pairRDD.aggregateByKey(10)(math.max(_,_),_+_).collect
res7: Array[(String, Int)] = Array((dog,12), (cat,22), (mouse,20))
//这个和上面的类似
scala> pairRDD.aggregateByKey(100)(math.max(_,_),_+_).collect
res8: Array[(String, Int)] = Array((dog,100), (cat,200), (mouse,200))
```

* coalesce(numPartitions)	
```
          返回一个新的RDD
          重新给RDD的元素分区。
          当适当缩小分区数时，如1000->100，spark会把之前的10个分区当作一个分区，并行度变为100，不会引起数据shuffle。
          当严重缩小分区数时，如1000->1，运算时的并行度会变成1。为了避免并行效率低下问题，可将shuffle设为true。shuffle之前的运算和之后的运算分为不同stage，它们的并行度分别为1000,1。
          当把分区数增大时，必会存在shuffle，shuffle须设为true
```

#### 2.3.2.Action
动作	含义

* reduce(func)	通过func函数聚集RDD中的所有元素，这个功能必须是课交换且可并联的

* collect()	在驱动程序中，以数组的形式返回数据集的所有元素

* count()	返回RDD的元素个数

* first()	返回RDD的第一个元素（类似于take(1)）

* take(n)	返回一个由数据集的前n个元素组成的数组

* takeSample(withReplacement,num, [seed])	返回一个数组，该数组由从数据集中随机采样的num个元素组成，可以选择是否用随机数替换不足的部分，seed用于指定随机数生成器种子

* takeOrdered(n, [ordering])	

* saveAsTextFile(path)	将数据集的元素以textfile的形式保存到HDFS文件系统或者其他支持的文件系统，对于每个元素，Spark将会调用toString方法，将它装换为文件中的文本

* saveAsSequenceFile(path) 	将数据集中的元素以Hadoop sequencefile的格式保存到指定的目录下，可以使HDFS或者其他Hadoop支持的文件系统。

* saveAsObjectFile(path) 	

* countByKey()	针对(K,V)类型的RDD，返回一个(K,Int)的map，表示每一个key对应的元素个数。

* foreach(func)	在数据集的每一个元素上，运行函数func进行更新。

#### 2.3.3.WordCount中的RDD

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-rdd/01.png?raw=true)

#### 2.3.4.练习
启动spark-shell
/usr/local/spark-1.5.2-bin-hadoop2.6/bin/spark-shell --master spark://node1.itcast.cn:7077 

练习1：
```
//通过并行化生成rdd
val rdd1 = sc.parallelize(List(5, 6, 4, 7, 3, 8, 2, 9, 1, 10))

//对rdd1里的每一个元素乘2然后排序
val rdd2 = rdd1.map(_ * 2).sortBy(x => x, true)
// res0: Array[Int] = Array(2, 4, 6, 8, 10, 12, 14, 16, 18, 20) 

//过滤出大于等于十的元素
val rdd3 = rdd2.filter(_ >= 10)
//将元素以数组的方式在客户端显示
rdd3.collect
// res1: Array[Int] = Array(10, 12, 14, 16, 18, 20)
```

练习2：
```
val rdd1 = sc.parallelize(Array("a b c", "d e f", "h i j"))
//将rdd1里面的每一个元素先切分在压平
val rdd2 = rdd1.flatMap(_.split(' '))
rdd2.collect
// res2: Array[String] = Array(a, b, c, d, e, f, h, i, j)
```

练习3：
```
val rdd1 = sc.parallelize(List(5, 6, 4, 3))
val rdd2 = sc.parallelize(List(1, 2, 3, 4))
//求并集
val rdd3 = rdd1.union(rdd2)
//求交集
val rdd4 = rdd1.intersection(rdd2)
//去重
rdd3.distinct.collect
rdd4.collect
// res4: Array[Int] = Array(4, 3)
```

练习4：
```
val rdd1 = sc.parallelize(List(("tom", 1), ("jerry", 3), ("kitty", 2)))
// res5: Array[(String, Int)] = Array((tom,1), (jerry,3), (kitty,2))

val rdd2 = sc.parallelize(List(("jerry", 2), ("tom", 1), ("shuke", 2)))
//求jion
val rdd3 = rdd1.join(rdd2)
rdd3.collect
// res6: Array[(String, (Int, Int))] = Array((tom,(1,1)), (jerry,(3,2)))

//求并集
val rdd4 = rdd1 union rdd2
// res7: Array[(String, Int)] = Array((tom,1), (jerry,3), (kitty,2), (jerry,2), (tom,1), (shuke,2))

//按key进行分组
rdd4.groupByKey
rdd4.collect
// res9: Array[(String, Int)] = Array((tom,1), (jerry,3), (kitty,2), (jerry,2), (tom,1), (shuke,2))
```

练习5：
```
val rdd1 = sc.parallelize(List(("tom", 1), ("tom", 2), ("jerry", 3), ("kitty", 2)))
val rdd2 = sc.parallelize(List(("jerry", 2), ("tom", 1), ("shuke", 2)))
//cogroup
val rdd3 = rdd1.cogroup(rdd2)
//注意cogroup与groupByKey的区别
rdd3.collect
// res10: Array[(String, (Iterable[Int], Iterable[Int]))] = Array((tom,(CompactBuffer(1, 2),CompactBuffer(1))), (jerry,(CompactBuffer(3),CompactBuffer(2))), (shuke,(CompactBuffer(),CompactBuffer(2))), (kitty,(CompactBuffer(2),CompactBuffer())))
```

练习6：
```
val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5))
//reduce聚合
val rdd2 = rdd1.reduce(_ + _)
// rdd2: Int = 15

rdd2.collect
```

练习7：
```
val rdd1 = sc.parallelize(List(("tom", 1), ("jerry", 3), ("kitty", 2),  ("shuke", 1)))
val rdd2 = sc.parallelize(List(("jerry", 2), ("tom", 3), ("shuke", 2), ("kitty", 5)))
val rdd3 = rdd1.union(rdd2)
// rdd3: Array[(String, Int)] = Array((tom,1), (jerry,3), (kitty,2), (shuke,1), (jerry,2), (tom,3), (shuke,2), (kitty,5))

//按key进行聚合
val rdd4 = rdd3.reduceByKey(_ + _)
rdd4.collect
// res14: Array[(String, Int)] = Array((tom,4), (jerry,5), (shuke,3), (kitty,7))

//按value的降序排序
val rdd5 = rdd4.map(t => (t._2, t._1)).sortByKey(false).map(t => (t._2, t._1))
rdd5.collect
//res15: Array[(String, Int)] = Array((kitty,7), (jerry,5), (tom,4), (shuke,3))
```

练习8：flatMap
```
val a = sc.parallelize(1 to 10, 5)
// a: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

a.flatMap(1 to _).collect
//res47: Array[Int] = Array(1, 1, 2, 1, 2, 3, 1, 2, 3, 4, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

sc.parallelize(List(1, 2, 3), 2).flatMap(x => List(x, x, x)).collect
//res85: Array[Int] = Array(1, 1, 1, 2, 2, 2, 3, 3, 3)

// The program below generates a random number of copies (up to 10) of the items in the list.
val x  = sc.parallelize(1 to 10, 3)
x.flatMap(List.fill(scala.util.Random.nextInt(10))(_)).collect
//res1: Array[Int] = Array(1, 2, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10)
```

//想要了解更多，访问下面的地址
http://homepage.cs.latrobe.edu.au/zhe/ZhenHeSparkRDDAPIExamples.html

### 2.4.RDD的依赖关系
RDD和它依赖的父RDD（s）的关系有两种不同的类型，即窄依赖（narrow dependency）和宽依赖（wide dependency）。

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-rdd/02.png?raw=true)

#### 2.4.1.窄依赖
窄依赖指的是每一个父RDD的Partition最多被子RDD的一个Partition使用
总结：窄依赖我们形象的比喻为独生子女

#### 2.4.2.宽依赖
宽依赖指的是多个子RDD的Partition会依赖同一个父RDD的Partition
总结：窄依赖我们形象的比喻为超生

#### 2.4.3.Lineage
RDD只支持粗粒度转换，即在大量记录上执行的单个操作。将创建RDD的一系列Lineage（即血统）记录下来，以便恢复丢失的分区。RDD的Lineage会记录RDD的元数据信息和转换行为，当该RDD的部分分区数据丢失时，它可以根据这些信息来重新运算和恢复丢失的数据分区。
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-rdd/03.png?raw=true)

### 2.5.RDD的缓存
Spark速度非常快的原因之一，就是在不同操作中可以在内存中持久化或缓存个数据集。当持久化某个RDD后，每一个节点都将把计算的分片结果保存在内存中，并在对此RDD或衍生出的RDD进行的其他动作中重用。这使得后续的动作变得更加迅速。RDD相关的持久化和缓存，是Spark最重要的特征之一。可以说，缓存是Spark构建迭代式算法和快速交互式查询的关键。

#### 2.5.1.RDD缓存方式
RDD通过persist方法或cache方法可以将前面的计算结果缓存，但是并不是这两个方法被调用时立即缓存，而是触发后面的action时，该RDD将会被缓存在计算节点的内存中，并供后面重用。

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-rdd/05-storage-level.png?raw=true)

通过查看源码发现cache最终也是调用了persist方法，默认的存储级别都是仅在内存存储一份，Spark的存储级别还有好多种，存储级别在object StorageLevel中定义的。

![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-rdd/06-persist.png?raw=true)

缓存有可能丢失，或者存储存储于内存的数据由于内存不足而被删除，RDD的缓存容错机制保证了即使缓存丢失也能保证计算的正确执行。通过基于RDD的一系列转换，丢失的数据会被重算，由于RDD的各个Partition是相对独立的，因此只需要计算丢失的部分即可，并不需要重算全部Partition。

### 2.6.DAG的生成
DAG(Directed Acyclic Graph)叫做有向无环图，原始的RDD通过一系列的转换就就形成了DAG，根据RDD之间的依赖关系的不同将DAG划分成不同的Stage，对于窄依赖，partition的转换处理在Stage中完成计算。对于宽依赖，由于有Shuffle的存在，只能在parent RDD处理完成后，才能开始接下来的计算，因此宽依赖是划分Stage的依据。
![image](https://github.com/leelovejava/doc/blob/master/img/spark/spark-rdd/04.png?raw=true)