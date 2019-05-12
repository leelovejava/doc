1.下载mysql
yum install -y mysql-server

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;

flush privileges;

2. 配置
hive/conf/hive-site.xml

3..下载mysql驱动到hive的lib目录
wget http://central.maven.org/maven2/mysql/mysql-connector-java/8.0.16/mysql-connector-java-8.0.16.jar
