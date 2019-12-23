# 分布式文件系统HDFS

## 大纲

### HDFS入门
* HDFS概述
* HDFS产出背景及定义
* HDFS优缺点
* HDFS组成架构
* HDFS文件块大小
* HDFS的Shell操作
* HDFS客户端操作
* HDFS客户端环境准备
* HDFS的API操作
* HDFS文件上传

### HDFS的API操作
* HDFS文件下载
* HDFS文件夹删除
* HDFS文件名更改
* HDFS文件详情查看
* HDFS文件和文件夹判断
* HDFS的I/O流操作
* HDFS文件上传
* HDFS文件下载

### HDFS的框架原理
* 定位文件读取
* HDFS的数据流
* HDFS写数据流程
* 剖析文件写入
* 网络拓扑-节点距离计算
* 机架感知
* 副本存储节点选择
* HDFS读数据流程

### NN & DN工作机制
* NameNode&2NN工作机制
* Fsimage和Edits解析
* CheckPoint时间设置
* NameNode故障处理
* 集群安全模式
* NameNode多目录配置
* DataNode工作机制
* 数据完整性

### DN工作机制&新特性
* 掉线时限参数设置
* 服役新数据节点
* 退役旧数据节点
* 添加白名单
* 黑名单退役
* Datanode多目录配置
* HDFS 2.X新特性
* 集群间数据拷贝

### 新特性& HA框架原理
* 小文件存档
* 回收站
* 快照管理
* HA概述
* HDFS-HA工作机制
* HDFS-HA工作要点
* HDFS-HA自动故障转移工作机制
* HDFS-HA集群配置

### HDFS-HA集群配置
* 环境准备
* 规划集群
* 配置Zookeeper集群
* 配置HDFS-HA集群
* 启动HDFS-HA集群
* 配置HDFS-HA自动故障转移
* YARN-HA配置
* YARN-HA工作机制
* 配置YARN-HA集群
* HDFS Federation架构设计

## doc
https://www.jianshu.com/p/e35817bdc4a8

[Hadoop如何将TB级大文件的上传性能优化上百倍？](https://mp.weixin.qq.com/s/2HM9NMRHizKTJoYjg8lZ1Q)

## Overview
重点概念:block文件切块，副本存放，元数据

## 特点:
* 分布式文件系统
* 分块存储,默认128M,参数指定块的大小df.blocksize
* 统一的抽象目录树
* namenode管理目录结构及文件分块信息(元信息)
	集群主节点,负责维护整个hdfs文件系统的目录树,以及每个文件/目录对应的block信息(block的id,所在的datanode服务器)
* datanode负责文件的block的存储管理
	集群从节点,副本数(dfs.replication)
* 场景:一次写入,多次读出,不支持的文件的修改	,适合做数据分析，不适合做网盘,因为不便于修改,延迟大,网络开销大,成本高

## 优缺点

### 优点:
1.高容错(副本)
2.批处理
3.适合大数据处理
4.构建在廉价机器上

### 缺点：
1.低延迟的数据访问
2.小文件存储(分块)->FastDFS

### win7 hadoop 本地库
2.8.3
https://download.csdn.net/download/mollywangran/10438972

fileSystem.copyToLocalFile(false,new Path(""),new Path(""),true);					

### 异常
1. 连接拒绝
    1).修改/etc/hosts 0.0.0.0 hadoop001
    2).修改hadoop/etc/hadoop/core-site.xml
        default.fs hdfs://hadoop001:9000/
2.  org.apache.hadoop.hdfs.server.namenode.SafeModeException: Cannot create directory /SpringHDFS. Name node is in safe mode
     关闭安全模式
     bin/hadoop dfsadmin -safemode leave
    
## 架构
1 Master(NameNode/NN)  带 N个Slaves(DataNode/DN)
HDFS/YARN/HBase

1个文件会被拆分成多个Block
blocksize：128M
130M ==> 2个Block： 128M 和 2M

NN：
1）负责客户端请求的响应
2）负责元数据（文件的名称、副本系数、Block存放的DN）的管理

DN：
1）存储用户的文件对应的数据块(Block)
2）要定期向NN发送心跳信息，汇报本身及其所有的block信息，健康状况

A typical deployment has a dedicated machine that runs only the NameNode software. 
Each of the other machines in the cluster runs one instance of the DataNode software.
The architecture does not preclude running multiple DataNodes on the same machine 
but in a real deployment that is rarely the case.

NameNode + N个DataNode
建议：NN和DN是部署在不同的节点上


replication factor：副本系数、副本因子

All blocks in a file except the last block are the same size

## hdfs副本机制
容错(高可用)

factor 副本系数(副本因子)

### 副本存放策略

## 配置

### 伪分布式环境搭建:
[官网单机安装](http://hadoop.apache.org/docs/r3.1.1/hadoop-project-dist/hadoop-common/SingleCluster.html)

#### 1) jdk安装

#### 2) 安装ssh

#### 3) 下载并解压hadoop

#### 4) hadoop配置

##### 4-1） HADOP_HOME/etc/hadoop/hadoop-env.sh

			export JAVA_HOME = /usr/local/jdk
	
##### 4-2） HADOOP_HOME/etc/hadoop/core-site.xml
```
<configuration>
  <!--hdfs临时路径-->
  <property>
      <name>hadoop.tmp.dir</name>
      <value>/usr/local/hadoop/tmp</value>
  </property>
  <!--hdfs 的默认地址、端口 访问地址-->
  <property>
      <name>fs.defaultFS</name>
      <value>hdfs://localhost:9000</value>
  </property>
  <!--关闭文件权限检查-->
  <property>
      <name>dfs.permissions</name>
      <value>false</value>
  </property>
</configuration>
```
####### 4-3）etc/hadoop/hdfs-site.xml

```
<configuration> 
    <!-- 副本数-->
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <!-- namenode的存储路径 -->
    <property>
      <name>dfs.namenode.name.dir</name>
      <value>file:///usr/local/hadoop/data/dfs/name</value>
    </property>
    <!-- datanode的存储路径 -->
    <property>
      <name>dfs.datanode.data.dir</name>
      <value>file:///usr/local/hadoop/data/dfs/data</value>
    </property>
    <!-- 块大小,默认字节-->
    <property>
      <name>dfs.blocksize</name>
      <!--128m-->
      <value>134217728</value>
    </property>
</configuration>
```

##### 5） 启动hdfs

    	格式化 Format the filesystem
    		bin/hdfs namenode -format (第一次)
		启动 Start NameNode daemon and DataNode daemon
			sbin/start-dfs.sh
		验证   jps
		停止   sbin/stop-dfs.sh
		
##### 6) 浏览器访问

		Browse the web interface for the NameNode; by default it is available at
		NameNode - http://localhost:9870/
			
		管理界面
			http://127.0.0.1:9000/  

##### 关闭防火墙:

		service iptables stop
		chkconfig iptables off
		systemctl stop firewalld
		systemctl disable firewalld

## shell命令:	 
 * -ls
  查看目录
 	hadoop fs -ls /
 * -mkdir
  创建目录
 	hadoop fs  -mkdir  -p  /aaa/bbb/cc/dd
 * -rm
  删除文件或文件夹
	hadoop fs -rm -r /aaa/bbb/cc/dd	
 * -rmdir
  删除空目录
	hadoop  fs  -rmdir   /aaa/bbb/cc/dd	
 * -put
 	hadoop  fs  -put  /opt/jdk-8u181-linux-x64.tar.gz  /opt/
 * -get
    hadoop fs -get  /aaa/jdk.tar.gz
 * -cp              
	从hdfs的一个路径拷贝hdfs的另一个路径
	hadoop  fs  -cp  /aaa/jdk.tar.gz  /bbb/jdk.tar.gz.2
 * -count
 	统计一个指定目录下的文件节点数量
 	fs -count /	   		
 * -df
 	统计文件系统的可用空间信息
	hadoop  fs  -df  -h  /
 * -setrep 
 	设置hdfs中文件的副本数量
 	hadoop fs -setrep 3 /aaa/jdk.tar.gz

## hdfs工作机制

 namenode:NN
 1) 负责客户端响应
 2) 负责元数据(文件的名称、副本系数、Block存放的DN)的管理

 datanode:ND:
 1) 存储用户的文件对应的数据块(Block)
 2) 要定期向DN发送心跳信息,汇报本身及其所有的block信息、健康状况

 namenode + N个 DataNode
 建议:NN和DN是部署在不同的节点上

##javaAPI操作HDFS

1. IDEA+MAVEN创建java工程	 

2. 添加HDFS相关的依赖
```
<dependency>
	<groupId>org.apache.hadoop</groupId>
	<artifactId>hadoop-client</artifactId>
</dependency>
```
3. 开发Java API操作HDFS文件
```
pirvate static final String HDFS_PATH= "hdfs://localhost:8020";
private FileSystem fileSystem;
Configuration configuration;
// 创建文件夹
fileSystem.mkdirs("");
fileSystem= FileSystem.get(new URI(HDFS_PATH),configuration,"hadoop");
// 查看hdfs文件的内容
FSDataInputStream in = fileSystem.open(new Path("文件目录"));
IOUtils.copeBytes(in,System.out,1024);
in.close();
``` 

No FileSystem for scheme "hdfs"
core-default.xm
``` l
<property>
     <name>fs.hdfs.impl</name>
     <value>org.apache.hadoop.hdfs.DistributedFileSystem</value>
     <description>The FileSystem for hdfs: uris.</description>
</property>
```

### 异常:
Permission denied: user=Administrator, access=WRITE, inode="/":hadoop:supergroup:drwxr-xr-x
HDFS客户端的权限错误:
解决:
加启动参数(操作hadoop的用户名)
HADOOP_USER_NAME:hadoop

## HDFS文件读写流程	 

[通过漫画轻松掌握HDFS工作原理](https://blog.csdn.net/eric_sunah/article/details/41546863)

1、三个部分: 客户端、nameserver（可理解为主控和文件索引,类似linux的inode）、datanode（存放实际数据）
2、如何写数据过程
3、读取数据过程
4、容错：第一部分：故障类型及其检测方法（nodeserver 故障，和网络故障，和脏数据问题）
5、容错第二部分：读写容错
6、容错第三部分：dataNode 失效
7、备份规则
8、结束语

中文翻译

[翻译经典 HDFS 原理讲解漫画 之一----系统构成和写数据过程](https://blog.csdn.net/hudiefenmu/article/details/37655491)
[翻译经典 HDFS 原理讲解漫画 之二----读数据和容错](https://blog.csdn.net/hudiefenmu/article/details/37694503)
[翻译经典 HDFS 原理讲解漫画 之三---容错和副本布局策略](https://blog.csdn.net/hudiefenmu/article/details/37820789)

## 读写流程 
[HDFS上传和下载原理（有源码解析）](https://blog.51cto.com/kinglab/2442820)
