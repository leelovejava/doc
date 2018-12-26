1.修改 hadoop_home/env/hadoop/hadoop-env.sh

注释 export JAVA_HOME=${JAVA_HOME}
export JAVA_HOME=/usr/local/jdk
		
1.修改 hadoop_home/env/hadoop/core-site.xml
```
    <!--默认文件系统-->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop000:8020</value>
    </property>
    <!--hadoop的临时目录-->
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/usr/local/tmp/hadoop</value>
    </property>
```

2.修改 hadoop_home/env/hadoop/hdfs-stie.xml
```
<property>
    <name>dfs.replication</name>
    <value>1</value>
</property>
```

3.修改 hadoop_home/env/hadoop/yarn-stie.xml
```
<property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
</property>
```

4.修改 hadoop_home/env/hadoop/mapred-site.xml.template
```
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
```

> mv mapred-site.xml.template.xml mapred-site.xml