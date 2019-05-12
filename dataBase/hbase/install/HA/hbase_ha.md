# hbase ha高可用搭建

## conf/hbase-site.xml
```
<configuration>
	<property>
		<!-- hbase在hdfs存放路径 -->
		<name>hbase.rootdir</name>
		<!--hdfs:hadoop000;8020/hbase-->
		<value>hdfs://cluster/hbase</value>
	</property>
	<property>
		<!-- 打开完全分布式模式 -->
		<name>hbase.cluster.distributed</name>
		<value>true</value>
	</property>
	<property>
		<name>hbase.zookeeper.quorum</name>
		<value>node01,node02,node03</value>
	</property>
	<property>
		<name>hbase.zookeeper.property.clientPort</name>
		<value>2181</value>
	</property>
	<!--
	协处理加载错误导致regionserver挂掉
	https://blog.csdn.net/u013709332/article/details/52414999
	-->
	<property>
          <name>hbase.coprocessor.abortonerror</name>
          <value>true</value>
          <description>Set to true to cause the hosting server (master or regionserver)
          to abort if a coprocessor fails to load, fails to initialize, or throws an
          unexpected Throwable object. Setting this to false will allow the server to
          continue execution but the system wide state of the coprocessor in question
          will become inconsistent as it will be properly executing in only a subset
          of servers, so this is most useful for debugging only.</description> 
    </property>
</configuration>
```
## 配置regionservers,把HRegionServer对应的host添加进去
vim conf/regionservers
```   
node01
node02
node03
```

## 配置backup-masters文件
只需在master和备用主机配置

vim conf/backup-masters
```
node02
```

## 启动
启动集群
> bin/start-hbase.sh

启动从节点
> bin/hbase-daemon.sh start master