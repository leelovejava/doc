## 掌握 HashMap 看这一篇文章就够了



最近几天，一直在学习 HashMap 的底层实现，发现关于 HashMap 实现的博客文章还是很多的，对比了一些，都没有一个很全面的文章来做总结，本篇文章也断断续续结合源码写了一下，如果有理解不当之处，欢迎指正！

### 01、摘要

在集合系列的第一章，咱们了解到，Map 的实现类有 HashMap、LinkedHashMap、TreeMap、IdentityHashMap、WeakHashMap、Hashtable、Properties 等等。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/laEmibHFxFw69P7lqqnHgjoy7tABoPYAl6ky3K5qsGaUkVrz5eTVqJqPDVkictC2gGw3vrjyWgRNkoaSzMg2Biaag/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

关于 HashMap，一直都是一个非常热门的话题，只要你出去面试，我保证一定少不了它！

本文主要结合 JDK1.7 和 JDK1.8 的区别，就 HashMap 的数据结构和实现功能，进行深入探讨，废话也不多说了，直奔主题！

### 02、简介

> “
>
> 在程序编程的时候，HashMap 是一个使用非常频繁的容器类，它允许键值都放入 null 元素。除该类方法未实现同步外，其余跟 Hashtable 大致相同，但跟 TreeMap 不同，该容器不保证元素顺序，根据需要该容器可能会对元素重新哈希，元素的顺序也会被重新打散，因此不同时间迭代同一个 HashMap 的顺序可能会不同。

HashMap 容器，实质还是一个哈希数组结构，但是在元素插入的时候，存在发生 hash 冲突的可能性；

对于发生 Hash 冲突的情况，冲突有两种实现方式，**一种开放地址方式（当发生 hash 冲突时，就继续以此继续寻找，直到找到没有冲突的 hash 值），另一种是拉链方式（将冲突的元素放入链表）**。**Java HashMap 采用的就是第二种方式，拉链法。**

在 jdk1.7 中，HashMap 主要是由数组+链表组成，当发生 hash 冲突的时候，就将冲突的元素放入链表中。

从 jdk1.8 开始，HashMap 主要是由数组+链表+红黑树实现的，相比 jdk1.7 而言，多了一个红黑树实现。当链表长度超过 8 的时候，就将链表变成红黑树，如图所示。

![img](https://mmbiz.qpic.cn/mmbiz_png/laEmibHFxFw69P7lqqnHgjoy7tABoPYAlia2ibg0Bxb9VZRCwlO9719jV88UfvONH75zhwYTePG3m4URZUOKZVxrw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

关于红黑树的实现，因为篇幅太长，在《集合系列》文章中红黑树设计，也有所介绍，这里就不在详细介绍了。

### 03、源码解析

直接打开 HashMap 的源码分析，可以看到，主要有 5 个关键参数：

- **threshold：表示容器所能容纳的 key-value 对极限。**
- **loadFactor：负载因子。**
- **modCount：记录修改次数。**
- **size：表示实际存在的键值对数量。**
- **table：一个哈希桶数组，键值对就存放在里面。**

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {

	//所能容纳的key-value对极限
	int threshold;

	//负载因子
	final float loadFactor;

	//记录修改次数
	int modCount;

	//实际存在的键值对数量
	int size;

	//哈希桶数组
	transient Node<K,V>[] table;
}
```

接着来看看`Node`这个类，`Node`是`HashMap`的一个内部类，实现了`Map.Entry`接口，本质是就是一个映射(键值对)

```java
static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;//hash值
        final K key;//k键
        V value;//value值
        Node<K,V> next;//链表中下一个元素
}
```

在 HashMap 的数据结构中，有两个参数可以影响 HashMap 的性能：**初始容量（inital capacity）和负载因子（load factor）**。

**初始容量（inital capacity）是指 table 的初始长度 length（默认值是 16）；**

**负载因子（load factor）用指自动扩容的临界值（默认值是 0.75）；**

`threshold`是`HashMap`所能容纳的最大数据量的`Node`(键值对)个数，计算公式`threshold = capacity * Load factor`。当 entry 的数量超过`capacity*load_factor`时，容器将自动扩容并重新哈希，扩容后的`HashMap`容量是之前容量的**两倍**，**所以数组的长度总是 2 的 n 次方**。

**初始容量**和**负载因子**也可以修改，具体实现方式，可以在对象初始化的时候，指定参数，比如：

```
Map map = new HashMap(int initialCapacity, float loadFactor);
```

但是，默认的负载因子 0.75 是对空间和时间效率的一个平衡选择，建议大家不要修改，除非在时间和空间比较特殊的情况下，如果内存空间很多而又对时间效率要求很高，可以降低负载因子 Load factor 的值；相反，如果内存空间紧张而对时间效率要求不高，可以增加负载因子 loadFactor 的值，这个值可以大于 1。同时，对于插入元素较多的场景，可以将初始容量设大，减少重新哈希的次数。

HashMap 的内部功能实现有很多，本文主要从以下几点，进行逐步分析。

- **通过 K 获取数组下标；**
- **put 方法的详细执行；**
- **resize 扩容过程；**
- **get 方法获取参数值；**
- **remove 删除元素；**

#### 3.1、通过 K 获取数组下标

不管增加、删除还是查找键值对，定位到数组的位置都是很关键的第一步，打开 hashMap 的任意一个增加、删除、查找方法，从源码可以看出，通过`key`获取数组下标，主要做了 3 步操作，其中`length`指的是容器数组的大小。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/laEmibHFxFw69P7lqqnHgjoy7tABoPYAlxDwLQia6emKZfDZSmZd4chCDFzlb8H9eqCQqqtW14CqV3L7Rw3NFnlw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

源码部分：

```java
/**获取hash值方法*/
static final int hash(Object key) {
     int h;
     // h = key.hashCode() 为第一步 取hashCode值（jdk1.7）
     // h ^ (h >>> 16)  为第二步 高位参与运算（jdk1.7）
     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);//jdk1.8
}
/**获取数组下标方法*/
static int indexFor(int h, int length) {
	//jdk1.7的源码，jdk1.8没有这个方法，但是实现原理一样的
     return h & (length-1);  //第三步 取模运算
}
```

#### 3.2、put 方法的详细执行

put(K key, V value)方法是将指定的 key, value 对添加到 map 里。该方法首先会对 map 做一次查找，看是否包含该 K，如果已经包含则直接返回；如果没有找到，则将元素插入容器。具体插入过程如下：

![img](https://mmbiz.qpic.cn/mmbiz_png/laEmibHFxFw69P7lqqnHgjoy7tABoPYAlMjOoNvcl8ANI4H1uUy2BqIF23FQ4JAIY8Rzzk99zPxy1m9rPVe2qfQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

具体执行步骤

- **1、判断键值对数组 table[i]是否为空或为 null，否则执行 resize()进行扩容；**
- **2、根据键值 key 计算 hash 值得到插入的数组索引 i，如果 table[i]==null，直接新建节点添加；**
- **3、当 table[i]不为空，判断 table[i]的首个元素是否和传入的 key 一样，如果相同直接覆盖 value；**
- **4、判断 table[i] 是否为 treeNode，即 table[i] 是否是红黑树，如果是红黑树，则直接在树中插入键值对；**
- **5、遍历 table[i]，判断链表长度是否大于 8，大于 8 的话把链表转换为红黑树，在红黑树中执行插入操作，否则进行链表的插入操作；遍历过程中若发现 key 已经存在直接覆盖 value 即可；**
- **6、插入成功后，判断实际存在的键值对数量 size 是否超多了最大容量 threshold，如果超过，进行扩容操作；**

**put 方法源码部分**

```java
/**
 * put方法
 */
public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
}
```

插入元素方法

```java
/**
 * 插入元素方法
 */
 final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
		//1、判断数组table是否为空或为null
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
		//2、判断数组下标table[i]==null
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
			//3、判断table[i]的首个元素是否和传入的key一样
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
			//4、判断table[i] 是否为treeNode
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
				//5、遍历table[i]，判断链表长度是否大于8
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
						//长度大于8，转红黑树结构
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
			//传入的K元素已经存在，直接覆盖value
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
		//6、判断size是否超出最大容量
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
}
```

其中，与 jdk1.7 有区别的地方，第 4 步新增了红黑树插入方法，源码部分：

```java
/**
   * 红黑树的插入操作
   */
final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab,
                                       int h, K k, V v) {
            Class<?> kc = null;
            boolean searched = false;
            TreeNode<K,V> root = (parent != null) ? root() : this;
            for (TreeNode<K,V> p = root;;) {
				//dir:遍历的方向， ph:p节点的hash值
                int dir, ph; K pk;
				//红黑树是根据hash值来判断大小
				// -1:左孩子方向 1:右孩子方向
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
				//如果key存在的话就直接返回当前节点
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
				//如果当前插入的类型和正在比较的节点的Key是Comparable的话，就直接通过此接口比较
                else if ((kc == null &&
                          (kc = comparableClassFor(k)) == null) ||
                         (dir = compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        TreeNode<K,V> q, ch;
                        searched = true;
						//尝试在p的左子树或者右子树中找到了目标元素
                        if (((ch = p.left) != null &&
                             (q = ch.find(h, k, kc)) != null) ||
                            ((ch = p.right) != null &&
                             (q = ch.find(h, k, kc)) != null))
                            return q;
                    }
					//获取遍历的方向
                    dir = tieBreakOrder(k, pk);
                }
				//上面的所有if-else判断都是在判断下一次进行遍历的方向，即dir
                TreeNode<K,V> xp = p;
				//当下面的if判断进去之后就代表找到了目标操作元素,即xp
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    Node<K,V> xpn = xp.next;
					//插入新的元素
                    TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
					//因为TreeNode今后可能退化成链表，在这里需要维护链表的next属性
                    xp.next = x;
					//完成节点插入操作
                    x.parent = x.prev = xp;
                    if (xpn != null)
                        ((TreeNode<K,V>)xpn).prev = x;
					//插入操作完成之后就要进行一定的调整操作了
                    moveRootToFront(tab, balanceInsertion(root, x));
                    return null;
                }
       }
}
```

#### 3.3、resize 扩容过程

在说 jdk1.8 的 HashMap 动态扩容之前，我们先来了解一下 jdk1.7 的 HashMap 扩容实现，因为 jdk1.8 代码实现比 Java1.7 复杂了不止一倍，主要是 Java1.8 引入了红黑树设计，但是实现思想大同小异！

##### 3.3.1、jdk1.7 的扩容实现源码部分

![img](https://mmbiz.qpic.cn/mmbiz_jpg/laEmibHFxFw69P7lqqnHgjoy7tABoPYAlChFcb5yNicSDtIoNngvJVeeNK3yzwnxhYPkFPq9muuOPEDlFo3vpiaDQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

源码部分

```java
/**
  * JDK1.7扩容方法
  * 传入新的容量
  */
void resize(int newCapacity) {
    //引用扩容前的Entry数组
    Entry[] oldTable = table;
    int oldCapacity = oldTable.length;
	//扩容前的数组大小如果已经达到最大(2^30)了
    if (oldCapacity == MAXIMUM_CAPACITY) {
		//修改阈值为int的最大值(2^31-1)，这样以后就不会扩容了
        threshold = Integer.MAX_VALUE;
        return;
    }
	//初始化一个新的Entry数组
    Entry[] newTable = new Entry[newCapacity];
	//将数据转移到新的Entry数组里，这里包含最重要的重新定位
    transfer(newTable);
	//HashMap的table属性引用新的Entry数组
    table = newTable;
    threshold = (int) (newCapacity * loadFactor);//修改阈值
}
```

transfer 复制数组方法，源码部分：

```java
//遍历每个元素，按新的容量进行rehash，放到新的数组上
void transfer(Entry[] newTable) {
	//src引用了旧的Entry数组
    Entry[] src = table;
    int newCapacity = newTable.length;
    for (int j = 0; j < src.length; j++) {
		//遍历旧的Entry数组
        Entry<K, V> e = src[j];
		//取得旧Entry数组的每个元素
        if (e != null) {
			//释放旧Entry数组的对象引用（for循环后，旧的Entry数组不再引用任何对象）
            src[j] = null;
            do {
                Entry<K, V> next = e.next;
				//重新计算每个元素在数组中的位置
				//实现逻辑，也是上文那个取模运算方法
                int i = indexFor(e.hash, newCapacity);
				//标记数组
                e.next = newTable[i];
				//将元素放在数组上
                newTable[i] = e;
				//访问下一个Entry链上的元素，循环遍历
                e = next;
            } while (e != null);
        }
    }
}
```

**jdk1.7 扩容总结：** newTable[i]的引用赋给了 e.next，也就是使用了单链表的头插入方式，同一位置上新元素总会被放在链表的头部位置；这样先放在一个索引上的元素终会被放到 Entry 链的尾部(如果发生了 hash 冲突的话），这一点和 Jdk1.8 有区别。在旧数组中同一条 Entry 链上的元素，通过重新计算索引位置后，有可能被放到了新数组的不同位置上。

##### 3.3.2、jdk1.8 的扩容实现

![img](https://mmbiz.qpic.cn/mmbiz_jpg/laEmibHFxFw69P7lqqnHgjoy7tABoPYAlljHuRLWObdV6icypfMC1NouHV48JeWkJKajhSOrofLKADJnrXXARvGw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

源码如下

```java
final Node<K,V>[] resize() {
		//引用扩容前的node数组
        Node<K,V>[] oldTab = table;
		//旧的容量
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
		//旧的阈值
        int oldThr = threshold;
		//新的容量、阈值初始化为0
        int newCap, newThr = 0;
        if (oldCap > 0) {
		    //如果旧容量已经超过最大容量，让阈值也等于最大容量，以后不再扩容
                threshold = Integer.MAX_VALUE;
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
			// 没超过最大值，就扩充为原来的2倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
				//如果旧容量翻倍没有超过最大值，且旧容量不小于初始化容量16，则翻倍
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
			//初始化容量设置为阈值
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
			//0的时候使用默认值初始化
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
		//计算新阈值，如果新容量或新阈值大于等于最大容量，则直接使用最大值作为阈值，不再扩容
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
		//设置新阈值
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
		//创建新的数组，并引用
        table = newTab;
		//如果老的数组有数据，也就是是扩容而不是初始化，才执行下面的代码，否则初始化的到这里就可以结束了
        if (oldTab != null) {
		    //轮询老数组所有数据
            for (int j = 0; j < oldCap; ++j) {
				//以一个新的节点引用当前节点，然后释放原来的节点的引用
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
					//如果e没有next节点，证明这个节点上没有hash冲突，则直接把e的引用给到新的数组位置上
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
						//！！！如果是红黑树，则进行分裂
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else {
					    // 链表优化重hash的代码块
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
						//从这条链表上第一个元素开始轮询，如果当前元素新增的bit是0，则放在当前这条链表上，如果是1，则放在"j+oldcap"这个位置上，生成“低位”和“高位”两个链表
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
									//元素是不断的加到尾部的，不会像1.7里面一样会倒序
                                    loTail.next = e;
								//新增的元素永远是尾元素
                                loTail = e;
                            }
                            else {
								//高位的链表与低位的链表处理逻辑一样，不断的把元素加到链表尾部
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
						//低位链表放到j这个索引的位置上
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
						//高位链表放到(j+oldCap)这个索引的位置上
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
}
```

1.7 与 1.8 处理逻辑大同小异，区别主要还是在树节点的分裂`((TreeNode<K,V>)e).split()`这个方法上

```java
/**
 * 红黑树分裂方法
 */
final void split(HashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
			//当前这个节点的引用，即这个索引上的树的根节点
            TreeNode<K,V> b = this;
            // Relink into lo and hi lists, preserving order
            TreeNode<K,V> loHead = null, loTail = null;
            TreeNode<K,V> hiHead = null, hiTail = null;
			//高位低位的初始树节点个数都设成0
            int lc = 0, hc = 0;
            for (TreeNode<K,V> e = b, next; e != null; e = next) {
                next = (TreeNode<K,V>)e.next;
                e.next = null;
				//bit=oldcap,这里判断新bit位是0还是1，如果是0就放在低位树上，如果是1就放在高位树上，这里先是一个双向链表
                if ((e.hash & bit) == 0) {
                    if ((e.prev = loTail) == null)
                        loHead = e;
                    else
                        loTail.next = e;
                    loTail = e;
                    ++lc;
                }
                else {
                    if ((e.prev = hiTail) == null)
                        hiHead = e;
                    else
                        hiTail.next = e;
                    hiTail = e;
                    ++hc;
                }
            }

            if (loHead != null) {
                //！！！如果低位的链表长度小于阈值6，则把树变成链表，并放到新数组中j索引位置
                if (lc <= UNTREEIFY_THRESHOLD)
                    tab[index] = loHead.untreeify(map);
                else {
                    tab[index] = loHead;
					//高位不为空，进行红黑树转换
                    if (hiHead != null) // (else is already treeified)
                        loHead.treeify(tab);
                }
            }
            if (hiHead != null) {
                if (hc <= UNTREEIFY_THRESHOLD)
                    tab[index + bit] = hiHead.untreeify(map);
                else {
                    tab[index + bit] = hiHead;
                    if (loHead != null)
                        hiHead.treeify(tab);
                }
            }
}
```

`untreeify`方法，将树转变为单向链表

```java
/**
 * 将树转变为单向链表
 */
final Node<K,V> untreeify(HashMap<K,V> map) {
            Node<K,V> hd = null, tl = null;
            for (Node<K,V> q = this; q != null; q = q.next) {
                Node<K,V> p = map.replacementNode(q, null);
                if (tl == null)
                    hd = p;
                else
                    tl.next = p;
                tl = p;
            }
            return hd;
}
```

`treeify`方法，将链表转换为红黑树，会根据红黑树特性进行颜色转换、左旋、右旋等

```java
/**
 * 链表转换为红黑树，会根据红黑树特性进行颜色转换、左旋、右旋等
 */
final void treeify(Node<K,V>[] tab) {
            TreeNode<K,V> root = null;
            for (TreeNode<K,V> x = this, next; x != null; x = next) {
                next = (TreeNode<K,V>)x.next;
                x.left = x.right = null;
                if (root == null) {
                    x.parent = null;
                    x.red = false;
                    root = x;
                }
                else {
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;
                    for (TreeNode<K,V> p = root;;) {
                        int dir, ph;
                        K pk = p.key;
                        if ((ph = p.hash) > h)
                            dir = -1;
                        else if (ph < h)
                            dir = 1;
                        else if ((kc == null &&
                                  (kc = comparableClassFor(k)) == null) ||
                                 (dir = compareComparables(kc, k, pk)) == 0)
                            dir = tieBreakOrder(k, pk);

                        TreeNode<K,V> xp = p;
                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
                            x.parent = xp;
                            if (dir <= 0)
                                xp.left = x;
                            else
                                xp.right = x;
							//进行左旋、右旋调整
                            root = balanceInsertion(root, x);
                            break;
                        }
                    }
                }
            }
            moveRootToFront(tab, root);
}
```

jdk1.8 在进行重新扩容之后，会重新计算 hash 值，因为 n 变为 2 倍，假设初始 tableSize = 4 要扩容到 8 来说就是 0100 到 1000 的变化（左移一位就是 2 倍），在扩容中只用判断原来的 hash 值与左移动的一位（newtable 的值）按位与操作是 0 或 1 就行，0 的话索引就不变，1 的话索引变成原索引 + oldCap；

其实现如下流程图所示：

![img](https://mmbiz.qpic.cn/mmbiz_jpg/laEmibHFxFw69P7lqqnHgjoy7tABoPYAlGCUpM2ewrznU7GldFhPvPBQDDUO6zOz31zdia9hALBz4kokd3iccVDjg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

可以看见，因为 hash 值本来就是随机性的，所以 hash 按位与上 newTable 得到的 0（扩容前的索引位置）和 1（扩容前索引位置加上扩容前数组长度的数值索引处）就是随机的，所以扩容的过程就能把之前哈希冲突的元素再随机的分布到不同的索引去，这算是 JDK1.8 的一个优化点。

此外，JDK1.7 中 rehash 的时候，旧链表迁移新链表的时候，如果在新表的数组索引位置相同，则链表元素会倒置，但是从上图可以看出，JDK1.8 不会倒置。

同时，由于 JDK1.7 中发生哈希冲突时仅仅采用了链表结构存储冲突元素，所以扩容时仅仅是重新计算其存储位置而已。而 JDK1.8 中为了性能在同一索引处发生哈希冲突到一定程度时，链表结构会转换为红黑数结构存储冲突元素，故在扩容时如果当前索引中元素结构是红黑树且元素个数小于链表还原阈值时就会把树形结构缩小或直接还原为链表结构（其实现就是上面代码片段中的 split() 方法）。

#### 3.4、get 方法获取参数值

get(Object key)方法根据指定的 key 值返回对应的 value，`getNode(hash(key), key))`得到相应的 Node 对象 e，然后返回 e.value。因此 getNode()是算法的核心。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/laEmibHFxFw69P7lqqnHgjoy7tABoPYAlf0uehr8iah9tbuBhZiawN2ZHLRKhtJbcrX1vRruBornA6G6p1XWXCTHA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

get 方法源码部分：

```java
/**
  * JDK1.8 get方法
  * 通过key获取参数值
  */
public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
}
```

通过 hash 值和 key 获取节点 Node 方法，源码部分：

```java
final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
			//1、判断第一个元素是否与key匹配
            if (first.hash == hash &&
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
				//2、判断链表是否红黑树结构
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
				//3、如果不是红黑树结构，直接循环判断
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
}
```

在红黑树中找到指定 k 的 TreeNode，源码部分：

```java
/**
  * 这里面情况分很多中，主要是因为考虑了hash相同但是key值不同的情况，查找的最核心还是落在key值上
  */
final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
            TreeNode<K,V> p = this;
            do {
                int ph, dir; K pk;
                TreeNode<K,V> pl = p.left, pr = p.right, q;
				//判断要查询元素的hash是否在树的左边
                if ((ph = p.hash) > h)
                    p = pl;
				//判断要查询元素的hash是否在树的右边
                else if (ph < h)
                    p = pr;
				//查询元素的hash与当前树节点hash相同情况
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
				//上面的三步都是正常的在二叉查找树中寻找对象的方法
				//如果hash相等，但是内容却不相等
                else if (pl == null)
                    p = pr;
                else if (pr == null)
                    p = pl;
				 //如果可以根据compareTo进行比较的话就根据compareTo进行比较
                else if ((kc != null ||
                          (kc = comparableClassFor(k)) != null) &&
                         (dir = compareComparables(kc, k, pk)) != 0)
                    p = (dir < 0) ? pl : pr;
				//根据compareTo的结果在右孩子上继续查询
                else if ((q = pr.find(h, k, kc)) != null)
                    return q;
				//根据compareTo的结果在左孩子上继续查询
                else
                    p = pl;
            } while (p != null);
            return null;
}
```

get 方法，首先通过 hash()函数得到对应数组下标，然后依次判断。

- 1、判断第一个元素与 key 是否匹配，如果匹配就返回参数值；
- 2、判断链表是否红黑树，如果是红黑树，就进入红黑树方法获取参数值；
- 3、如果不是红黑树结构，直接循环判断，直到获取参数为止；

#### 3.5、remove 删除元素

remove(Object key)的作用是删除 key 值对应的 Node，该方法的具体逻辑是在`removeNode(hash(key), key, null, false, true)`里实现的。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/laEmibHFxFw69P7lqqnHgjoy7tABoPYAlb22Fo7hlP4QChtStbMaMZgbibF6Vqx9HAXNQZqwl2AHgbOLmb5O5VPg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

remove 方法，源码部分：

```java
/**
  * JDK1.8 remove方法
  * 通过key移除对象
  */
public V remove(Object key) {
        Node<K,V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
            null : e.value;
}
```

通过 key 移除 Node 节点方法，源码部分：

```java
/**
  * 通过key移除Node节点
  */
final Node<K,V> removeNode(int hash, Object key, Object value,
                               boolean matchValue, boolean movable) {
        Node<K,V>[] tab; Node<K,V> p; int n, index;
		//1、判断要删除的元素，是否存在
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (p = tab[index = (n - 1) & hash]) != null) {
            Node<K,V> node = null, e; K k; V v;
			//2、判断第一个元素是不是我们要找的元素
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
				//3、判断当前冲突链表是否红黑树结构
                if (p instanceof TreeNode)
                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                else {
					//4、循环在链表中找到需要删除的元素
                    do {
                        if (e.hash == hash &&
                            ((k = e.key) == key ||
                             (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
			//上面的逻辑，基本都是为了找到要删除元素的节点
            if (node != null && (!matchValue || (v = node.value) == value ||
                                 (value != null && value.equals(v)))) {
				//5、如果当前冲突链表结构是红黑树，执行红黑树删除方法
                if (node instanceof TreeNode)
                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                else if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
}
```

removeTreeNode 移除红黑树节点方法，源码部分：

```java
final void removeTreeNode(HashMap<K,V> map, Node<K,V>[] tab,
                                  boolean movable) {
            int n;
            if (tab == null || (n = tab.length) == 0)
                return;
            int index = (n - 1) & hash;
            TreeNode<K,V> first = (TreeNode<K,V>)tab[index], root = first, rl;
            TreeNode<K,V> succ = (TreeNode<K,V>)next, pred = prev;
            if (pred == null)
                tab[index] = first = succ;
            else
                pred.next = succ;
            if (succ != null)
                succ.prev = pred;
            if (first == null)
                return;
            if (root.parent != null)
                root = root.root();
            if (root == null || root.right == null ||
                (rl = root.left) == null || rl.left == null) {
                tab[index] = first.untreeify(map);  // too small
                return;
            }
            TreeNode<K,V> p = this, pl = left, pr = right, replacement;
            if (pl != null && pr != null) {
                TreeNode<K,V> s = pr, sl;
                while ((sl = s.left) != null) // find successor
                    s = sl;
                boolean c = s.red; s.red = p.red; p.red = c; // swap colors
                TreeNode<K,V> sr = s.right;
                TreeNode<K,V> pp = p.parent;
                if (s == pr) { // p was s's direct parent
                    p.parent = s;
                    s.right = p;
                }
                else {
                    TreeNode<K,V> sp = s.parent;
                    if ((p.parent = sp) != null) {
                        if (s == sp.left)
                            sp.left = p;
                        else
                            sp.right = p;
                    }
                    if ((s.right = pr) != null)
                        pr.parent = s;
                }
                p.left = null;
                if ((p.right = sr) != null)
                    sr.parent = p;
                if ((s.left = pl) != null)
                    pl.parent = s;
                if ((s.parent = pp) == null)
                    root = s;
                else if (p == pp.left)
                    pp.left = s;
                else
                    pp.right = s;
                if (sr != null)
                    replacement = sr;
                else
                    replacement = p;
            }
            else if (pl != null)
                replacement = pl;
            else if (pr != null)
                replacement = pr;
            else
                replacement = p;
            if (replacement != p) {
                TreeNode<K,V> pp = replacement.parent = p.parent;
                if (pp == null)
                    root = replacement;
                else if (p == pp.left)
                    pp.left = replacement;
                else
                    pp.right = replacement;
                p.left = p.right = p.parent = null;
            }
			//判断是否需要进行红黑树结构调整
            TreeNode<K,V> r = p.red ? root : balanceDeletion(root, replacement);

            if (replacement == p) {  // detach
                TreeNode<K,V> pp = p.parent;
                p.parent = null;
                if (pp != null) {
                    if (p == pp.left)
                        pp.left = null;
                    else if (p == pp.right)
                        pp.right = null;
                }
            }
            if (movable)
                moveRootToFront(tab, r);
}
```

jdk1.8 的删除逻辑实现比较复杂，相比 jdk1.7 而言，多了红黑树节点删除和调整：

- 1、默认判断链表第一个元素是否是要删除的元素；
- 2、如果第一个不是，就继续判断当前冲突链表是否是红黑树，如果是，就进入红黑树里面去找；
- 3、如果当前冲突链表不是红黑树，就直接在链表中循环判断，直到找到为止；
- 4、将找到的节点，删除掉，如果是红黑树结构，会进行颜色转换、左旋、右旋调整，直到满足红黑树特性为止；

### 04、总结

1、如果 key 是一个对象，记得在对象实体类里面，要重写 equals 和 hashCode 方法，不然在查询的时候，无法通过对象 key 来获取参数值！

2、相比 JDK1.7，JDK1.8 引入红黑树设计，当链表长度大于 8 的时候，链表会转化为红黑树结构，发生冲突的链表如果很长，红黑树的实现很大程度优化了 HashMap 的性能，使查询效率比 JDK1.7 要快一倍！

3、对于大数组的情况，可以提前给 Map 初始化一个容量，避免在插入的时候，频繁的扩容，因为扩容本身就比较消耗性能！

### 05、参考资料

1、美团技术团队 - Java 8系列之重新认识HashMap: *https://zhuanlan.zhihu.com/p/21673805*

2、简书 - JDK1.8红黑树实现分析-此鱼不得水: *https://www.jianshu.com/p/34b6878ae6de*

3、简书 - JJDK 1.8 中 HashMap 扩容: https://www.jianshu.com/p/bdfd5f98cc31

4、Java HashMap 基础面试常见问题: *https://www.rabbitwfly.com/articles/2019/04/23/1556021848567.html*

## 面试题

### 1、为什么用HashMap？

- HashMap是一个散列桶（数组和链表），它存储的内容是键值对(key-value)映射

  

- HashMap采用了数组和链表的数据结构，能在查询和修改方便继承了数组的线性查找和链表的寻址修改

  

- HashMap是非synchronized，所以HashMap很快

  

- HashMap可以接受null键和值，而Hashtable则不能（原因就是equlas()方法需要对象，因为HashMap是后出的API经过处理才可以）

### 2、HashMap的工作原理是什么？

- HashMap是基于hashing的原理，我们使用put(key, value)存储对象到HashMap中，使用get(key)从HashMap中获取对象。

  当我们给put()方法传递键和值时，我们先对键调用hashCode()方法，计算并返回的hashCode是用于找到Map数组的bucket位置来储存Node 对象。

  这里关键点在于指出，HashMap是在bucket中储存键对象和值对象，作为Map.Node 。

  

  ![img](https://mmbiz.qpic.cn/mmbiz_png/vnOqylzBGCR46r2EZfzq27ibD0MSL4ZdAS5k2ZF2TdibzuL8JPJL5EF4aSfsLoH5A08k2JExeqQCTXOiaLRxlw60Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

  

- 以下是HashMap初始化 ，简单模拟数据结构

```
Node[] table=new Node[16]  散列桶初始化，tableclass Node { hash;//hash值      key;//键　value;//值　node next;//用于指向链表的下一层（产生冲突，用拉链法）}
```



- 以下是具体的put过程（JDK1.8版）

1、对Key求Hash值，然后再计算下标

2、如果没有碰撞，直接放入桶中（碰撞的意思是计算得到的Hash值相同，需要放到同一个bucket中）

3、如果碰撞了，以链表的方式链接到后面

4、如果链表长度超过阀值( TREEIFY THRESHOLD==8)，就把链表转成红黑树，链表长度低于6，就把红黑树转回链表

5、如果节点已经存在就替换旧值

6、如果桶满了(容量16*加载因子0.75)，就需要 resize（扩容2倍后重排）

- 以下是具体get过程(考虑特殊情况如果两个键的hashcode相同，你如何获取值对象？)

当我们调用get()方法，HashMap会使用键对象的hashcode找到bucket位置

找到bucket位置之后，会调用keys.equals()方法去找到链表中正确的节点，最终找到要找的值对象。

　

![img](https://mmbiz.qpic.cn/mmbiz_png/vnOqylzBGCR46r2EZfzq27ibD0MSL4ZdAHCDiaSPFfYXeJic0wVlQ5QcoFpPz6NDBTbT5ek2zYxgdVqycP5M1iabPQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### 3、有什么方法可以减少碰撞？

- 扰动函数可以减少碰撞，原理是如果两个不相等的对象返回不同的hashcode的话，那么碰撞的几率就会小些

  

  这就意味着存链表结构减小，这样取值的话就不会频繁调用equal方法，这样就能提高HashMap的性能

  

  （扰动即Hash方法内部的算法实现，目的是让不同对象返回不同hashcode）

  

- 使用不可变的、声明作final的对象，并且采用合适的equals()和hashCode()方法的话，将会减少碰撞的发生。

  

  不可变性使得能够缓存不同键的hashcode，这将提高整个获取对象的速度，使用String，Interger这样的wrapper类作为键是非常好的选择。

  

  为什么String, Interger这样的wrapper类适合作为键？因为String是final的，而且已经重写了equals()和hashCode()方法了。

  

  不可变性是必要的，因为为了要计算hashCode()，就要防止键值改变，如果键值在放入时和获取时返回不同的hashcode的话，那么就不能从HashMap中找到你想要的对象。

### 4、HashMap中hash函数怎么是是实现的?

我们可以看到在hashmap中要找到某个元素，需要根据key的hash值来求得对应数组中的位置。

如何计算这个位置就是hash算法？

前面说过hashmap的数据结构是数组和链表的结合，所以我们当然希望这个hashmap里面的元素位置尽量的分布均匀些，尽量使得每个位置上的元素数量只有一个

这样当我们用hash算法求得这个位置的时候，马上就可以知道对应位置的元素就是我们要的，而不用再去遍历链表。

所以我们首先想到的就是把hashcode对数组长度取模运算，这样一来，元素的分布相对来说是比较均匀的。

但是，“模”运算的消耗还是比较大的，能不能找一种更快速，消耗更小的方式，我们来看看JDK1.8的源码是怎么做的（被楼主修饰了一下）

```
static final int hash(Object key) {    if (key == null){        return 0;    }     int h;     h=key.hashCode()；返回散列值也就是hashcode      // ^ ：按位异或      // >>>:无符号右移，忽略符号位，空位都以0补齐      //其中n是数组的长度，即Map的数组部分初始化长度     return  (n-1)&(h ^ (h >>> 16));}
```

![img](https://mmbiz.qpic.cn/mmbiz_png/vnOqylzBGCR46r2EZfzq27ibD0MSL4ZdAlfLMzQbCKQTECv0TEFq30ayicg8ue0spzCuQduq9ibeZteLLmjfg33Pw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

简单来说就是

1、高16bt不变，低16bit和高16bit做了一个异或(得到的HASHCODE转化为32位的二进制，前16位和后16位低16bit和高16bit做了一个异或)

2、(n·1)&hash=->得到下标



### **5、拉链法导致的链表过深问题为什么不用二叉查找树代替，而选择红黑树？为什么不一直使用红黑树？**

之所以选择红黑树是为了解决二叉查找树的缺陷，二叉查找树在特殊情况下会变成一条线性结构（这就跟原来使用链表结构一样了，造成很深的问题），遍历查找会非常慢。

而红黑树在插入新数据后可能需要通过左旋，右旋、变色这些操作来保持平衡，引入红黑树就是为了查找数据快，解决链表查询深度的问题

我们知道红黑树属于平衡二叉树，但是为了保持“平衡”是需要付出代价的，但是该代价所损耗的资源要比遍历线性链表要少

所以当长度大于8的时候，会使用红黑树，如果链表长度很短的话，根本不需要引入红黑树，引入反而会慢。

### 6、说说你对红黑树的见解？

![img](https://mmbiz.qpic.cn/mmbiz_png/vnOqylzBGCR46r2EZfzq27ibD0MSL4ZdABp0hBAwFbXQqRBhvopK27T2ZicDYIOiaOgEOOic0ib4ESxU4DFqrWDx7Pg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

1、每个节点非红即黑

2、根节点总是黑色的

3、如果节点是红色的，则它的子节点必须是黑色的（反之不一定）

4、每个叶子节点都是黑色的空节点（NIL节点）

5、从根节点到叶节点或空子节点的每条路径，必须包含相同数目的黑色节点（即相同的黑色高度）

### 7、解决hash 碰撞还有那些办法？

开放定址法。

当冲突发生时，使用某种探查技术在散列表中形成一个探查(测)序列。沿此序列逐个单元地查找，直到找到给定的地址。

按照形成探查序列的方法不同，可将开放定址法区分为线性探查法、二次探查法、双重散列法等。

下面给一个线性探查法的例子　　

问题：已知一组关键字为(26，36，41，38，44，15，68，12，06，51)，用除余法构造散列函数，用线性探查法解决冲突构造这组关键字的散列表。

解答：为了减少冲突，通常令装填因子α由除余法因子是13的散列函数计算出的上述关键字序列的散列地址为(0，10，2，12，5，2，3，12，6，12)。

前5个关键字插入时，其相应的地址均为开放地址，故将它们直接插入T[0]，T[10)，T[2]，T[12]和T[5]中。

当插入第6个关键字15时，其散列地址2(即h(15)=15％13=2)已被关键字41(15和41互为同义词)占用。故探查h1=(2+1)％13=3，此地址开放，所以将15放入T[3]中。

当插入第7个关键字68时，其散列地址3已被非同义词15先占用，故将其插入到T[4]中。

当插入第8个关键字12时，散列地址12已被同义词38占用，故探查hl=(12+1)％13=0，而T[0]亦被26占用，再探查h2=(12+2)％13=1，此地址开放，可将12插入其中。

类似地，第9个关键字06直接插入T[6]中；而最后一个关键字51插人时，因探查的地址12，0，1，…，6均非空，故51插入T[7]中。

### 8、如果HashMap的大小超过了负载因子(load factor)定义的容量，怎么办？

默认的负载因子大小为0.75

也就是说，当一个map填满了75%的bucket时候，和其它集合类(如ArrayList等)一样，将会创建原来HashMap大小的两倍的bucket数组来重新调整map的大小，并将原来的对象放入新的bucket数组中。

这个过程叫作rehashing，因为它调用hash方法找到新的bucket位置。这个值只可能在两个地方，一个是原下标的位置，另一种是在下标为<原下标+原容量>的位置　　

### 9、重新调整HashMap大小存在什么问题吗？

- 当重新调整HashMap大小的时候，确实存在条件竞争，因为如果两个线程都发现HashMap需要重新调整大小了，它们会同时试着调整大小。

  

  在调整大小的过程中，存储在链表中的元素的次序会反过来，因为移动到新的bucket位置的时候，HashMap并不会将元素放在链表的尾部，而是放在头部

  

  这是为了避免尾部遍历(tail traversing)。如果条件竞争发生了，那么就死循环了。(多线程的环境下不使用HashMap）

  

- 为什么多线程会导致死循环，它是怎么发生的？

HashMap的容量是有限的。当经过多次元素插入，使得HashMap达到一定饱和度时，Key映射位置发生冲突的几率会逐渐提高。

这时候，HashMap需要扩展它的长度，也就是进行Resize。

1.扩容：创建一个新的Entry空数组，长度是原数组的2倍。

2.ReHash：遍历原Entry数组，把所有的Entry重新Hash到新数组。

达摩：哎呦，小老弟不错嘛~~意料之外呀

小鲁班：嘿嘿，优秀吧，中场休息一波，我先喝口水

达摩：不仅仅是这些哦，面试官还会问你相关的集合类对比，比如：

### 10、HashTable

- 数组 + 链表方式存储
- 默认容量：11(质数 为宜)
- put:
- \- 索引计算 : （key.hashCode() & 0x7FFFFFFF）% table.length
- 若在链表中找到了，则替换旧值，若未找到则继续
- 当总元素个数超过容量*加载因子时，扩容为原来 2 倍并重新散列。
- 将新元素加到链表头部
- 对修改 Hashtable 内部共享数据的方法添加了 synchronized，保证线程安全。

### 11、HashMap ，HashTable 区别

- 默认容量不同。扩容不同
- 线程安全性，HashTable 安全
- 效率不同 HashTable 要慢因为加锁

### 12、ConcurrentHashMap 原理

1、最大特点是引入了 CAS（借助 Unsafe 来实现【native code】）

- CAS有3个操作数，内存值V，旧的预期值A，要修改的新值B。当且仅当预期值A和内存值V相同时，将内存值V修改为B，否则什么都不做。

  

- Unsafe 借助 CPU 指令 cmpxchg 来实现

  

- 使用实例：

> 1、对 sizeCtl 的控制都是用 CAS 来实现的
>
> 2、sizeCtl ：默认为0，用来控制 table 的初始化和扩容操作。
>
> - -1 代表table正在初始化
> - N 表示有 -N-1 个线程正在进行扩容操作
> - 如果table未初始化，表示table需要初始化的大小。
> - 如果table初始化完成，表示table的容量，默认是table大小的0.75倍，居然用这个公式算0.75（n - (n >>> 2)）

2、CAS 会出现的问题：ABA

- 对变量增加一个版本号，每次修改，版本号加 1，比较的时候比较版本号。

### 13、我们可以使用CocurrentHashMap来代替Hashtable吗？

我们知道Hashtable是synchronized的，但是ConcurrentHashMap同步性能更好，因为它仅仅根据同步级别对map的一部分进行上锁。



ConcurrentHashMap当然可以代替HashTable，但是HashTable提供更强的线程安全性。



它们都可以用于多线程的环境，但是当Hashtable的大小增加到一定的时候，性能会急剧下降，因为迭代时需要被锁定很长的时间。



因为ConcurrentHashMap引入了分割(segmentation)，不论它变得多么大，仅仅需要锁定map的某个部分，而其它的线程不需要等到迭代完成才能访问map。



简而言之，在迭代的过程中，ConcurrentHashMap仅仅锁定map的某个部分，而Hashtable则会锁定整个map。

此时躺着床上的张飞哄了一声：睡觉了睡觉了~

见此不太妙：小鲁班立马回到床上（泉水），把被子盖过头，心里有一丝丝愉悦感，不对。好像还没洗澡。。。

by the way

**CocurrentHashMap在JAVA8中存在一个bug，会进入死循环，原因是递归创建ConcurrentHashMap 对象**

但是在1.9已经修复了,场景重现如下

```java
public class ConcurrentHashMapDemo{

    private Map cache =new ConcurrentHashMap<>(15);

    public static void main(String[]args){
        ConcurrentHashMapDemo ch =    new ConcurrentHashMapDemo();
        System.out.println(ch.fibonaacci(80));
    }

    public int fibonaacci(Integer i){
        if(i==0||i ==1) {
            return i;
    }

    return cache.computeIfAbsent(i,(key) -> {
        System.out.println("fibonaacci : "+key);
        return fibonaacci(key -1)+fibonaacci(key - 2);
    });

    }
}
```