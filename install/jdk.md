jdk8安装

安装方式：1.yum 2.rpm 3.源码编译安装

jdk rpm安装

yum -y install java

http://download.csdn.net/download/aqtata/9410831
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

wget http://download.oracle.com/otn-pub/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-linux-x64.tar.gz

tar -xvf jdk-8u171-linux-x64.tar.gz -C /usr/local/jdk

① vi /etc/profile
 
② 在末尾行添加
        #set java environment
        export JAVA_HOME=/usr/local/jdk
        export PATH=$PATH:$JAVA_HOME/bin
③source /etc/profile  使更改的配置立即生效
④java -version  查看JDK版本信息