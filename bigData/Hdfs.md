# 分布式文件系统HDFS

##doc
https://www.jianshu.com/p/e35817bdc4a8

[Hadoop如何将TB级大文件的上传性能优化上百倍？](https://mp.weixin.qq.com/s/2HM9NMRHizKTJoYjg8lZ1Q)

##Overview
重点概念:block文件切块，副本存放，元数据

##特点:
* 分布式文件系统
* 分块存储,默认128M,参数指定块的大小df.blocksize
* 统一的抽象目录树
* namenode管理目录结构及文件分块信息(元信息)
	集群主节点,负责维护整个hdfs文件系统的目录树,以及每个文件/目录对应的block信息(block的id,所在的datanode服务器)
* datanode负责文件的block的存储管理
	集群从节点,副本数(dfs.replication)
* 场景:一次写入,多次读出,不支持的文件的修改	,适合做数据分析，不适合做网盘,因为不便于修改,延迟大,网络开销大,成本高

##优缺点

###优点:
1.高容错(副本)
2.批处理
3.适合大数据处理
4.构建在廉价机器上

###缺点：
1.低延迟的数据访问
2.小文件存储(分块)->FastDFS

###win7 hadoop 本地库
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

##配置
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