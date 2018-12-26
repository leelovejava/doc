# phoenix的安装和配置

## 1、下载
> http://mirror.bit.edu.cn/apache/phoenix/

和hbase对应版本

## 2、复制jar
phoenix-core-5.0.0-HBase-2.0.jar和phoenix-5.0.0-HBase-2.0-server.jar到hbase regionServer的lib目录

> cp phoenix-core-4.14.1-HBase-1.2.jar ~/app/hbase-1.2.0-cdh5.7.0/lib/
> cp phoenix-4.14.1-HBase-1.2-server.jar ~/app/hbase-1.2.0-cdh5.7.0/lib/

## 3、增加hbase-site.xml 配置
```
<property>
    <name>hbase.table.sanity.checks</name>
    <value>false</value>
</property>
```

## 进入
> bin/sqlline.py

如果出现连接连接拒绝 或者 RetriesExhaustedException: Can't get the locations

原因:版本不同导致hbase的端口号不同
查看phoenix-client.jar 的hbase-default.xml,获取端口号

修改hbase_home/conf/hbase-site.xml

```
    <!--HBase Master web界面绑定的端口,默认为0.0.0.0-->
    <property>
            <name>hbase.master.info.port</name>
            <value>16010</value>
    </property>
    <!--HBase RegionServer绑定的端口-->
    <property>
            <name>hbase.regionserver.port</name>
            <value>16020</value>
    </property>
    <!--HBase RegionServer web 界面绑定的端口     设置为 -1 意味这你不想与运行 RegionServer 界面-->
    <property>
            <name>hbase.regionserver.info.port</name>
            <value>16030</value>
    </property>
```