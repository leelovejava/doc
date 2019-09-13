# hdfs ha启动
# 1.	在各个JournalNode节点上，输入以下命令启动journalnode服务
sbin/hadoop-daemon.sh start journalnode

# 2.	在[nn1]上，对其进行格式化，并启动
bin/hdfs namenode -format
sbin/hadoop-daemon.sh start namenode

# 3.	在[nn2]上，同步nn1的元数据信息
bin/hdfs namenode -bootstrapStandby

# 4.	启动[nn2]
sbin/hadoop-daemon.sh start namenode

# 5.	查看web页面显示

# 6.	在[nn1]上，启动所有datanode
sbin/hadoop-daemons.sh start datanode

# 7.	将[nn1]切换为Active
bin/hdfs haadmin -transitionToActive nn1

# 8. 查看是否Active
bin/hdfs haadmin -getServiceState nn1