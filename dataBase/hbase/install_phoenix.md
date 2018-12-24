# phoenix的安装和配置

## 1、下载
> http://mirror.bit.edu.cn/apache/phoenix/

和hbase对应版本

## 2、复制jar
phoenix-core-5.0.0-HBase-2.0.jar和phoenix-5.0.0-HBase-2.0-server.jar到hbase regionServer的lib目录

> cp phoenix-4.14.1-HBase-1.2- ~/app/hbase-1.2.0-cdh5.7.0/lib/
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