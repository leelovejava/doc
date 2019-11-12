# linux

## doc

-[逼格高又实用的 Linux 命令，开发、运维一定要懂！](https://mp.weixin.qq.com/s/K0E0eU9gAFqfrlyohhnwzA)

-[阿里java问题排查单](http://www.jiangxinlingdu.com/thought/2018/11/17/javatools.html)

-[系统性能优化系列](http://www.jiangxinlingdu.com/thought/2018/09/15/linuxsys.html)

-[常用性能监控指南](http://blog.720ui.com/2018/linux_performance_command/)

-[线上操作与线上问题排查实战](http://www.jiangxinlingdu.com/practice/2018/09/13/operation.html)

-[不停机图片升级迁移](http://www.jiangxinlingdu.com/thought/2018/08/15/images.html)

-[shell在手分析服务器日志不愁?](https://segmentfault.com/a/1190000009745139)

-[优雅停机的正确姿势，应该这样才对](https://mp.weixin.qq.com/s/BCJ7jaO6-R_Dr72wJsXn4A)

- [Linux 问题故障定位](https://mp.weixin.qq.com/s/5HpRa__Swn-qSyewXLpxPA)

- [大数据工程师必须掌握的性能优化技术](https://mp.weixin.qq.com/s/MOIYKgovsxTllz-EQXGvMg)

- [Linux进程内存用量分析之内存映射篇](https://mp.weixin.qq.com/s/u_S32WoBmBjbfhfg3N7GCQ)

## windows
windows的hosts目录
c:\windows\system32\drivers\etc

------github------------
192.30.253.112 github.com
151.101.0.133 assets-cdn.github.com
151.101.1.194  github.global.ssl.fastly.net
------github------------

windows 修改hosts 立即生效的方法
cmd 

// 显示所有 dns内容
ipconfig /displaydns

// 刷新所有 dns内容
ipconfig /flushdns

[win10修改hosts](https://www.cnblogs.com/lwh-note/p/9005953.html)

[Windows 下的文件被占用解决](https://www.cnblogs.com/lmsthoughts/p/8085931.html)

bat清除maven下载失败
```bash
set REPOSITORY_PATH=C:\Users\Administrator\.m2\repository
rem 正在搜索...

for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    
	del /s /q %%i

)

rem 搜索完毕

pause
```

修改主机名
查看
> hostname

临时修改 
> Hostnamectl set-hostname hadoop

永久修改
> vim /etc/hostname
> hadoop

>hostname hadoop

### 切换yum源

[linux yum下载慢问题解决](https://blog.csdn.net/baiyan3212/article/details/81175192)

--备份
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bak

--下载
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

--清除缓存
yum clean all
 
--生成缓存
yum makecache

### 其他
### 关闭防火墙
  
  centos7 firewalld
      
      https://blog.csdn.net/Post_Yuan/article/details/78603212  
      
      systemctl stop firewalld
      
      systemctl disable firewalld
  
  centos6 iptables
     
     //临时关闭
     service iptables stop
     
     //禁止开机启动
     chkconfig iptables off

开放端口
 // --permanent 永久生效,没有此参数重启后失效
firewall-cmd --zone=public --add-port=80/tcp --permanent 

firewall-cmd --zone=public --add-port=1000-2000/tcp --permanent 


centos7安装网卡关闭的解决办法
https://blog.csdn.net/dancheren/article/details/73611878

> vi /etc/sysconfig/network-scripts/ifcfg-ens33

安装wget
> yum install wget -y

安装tar
> yum install -y tar

安装vim
> yum -y install vim*

安装netstat
> yum install net-tools -y

安装telnet
https://www.cnblogs.com/happyflyingpig/p/8127885.html
> yum -y install telnet-server.x86_64 telnet.x86_64 xinetd.x86_64

安装gcc
> yum install gcc-c++ -y

授予root权限
https://www.linuxidc.com/Linux/2012-07/64530.htm

查看文件大小
> ls -lht

按日期截取日志
> sed -n '/2018-11-14 10:00:/,/2018-11-14 12:00:/p' intellif_monitor_info.log > 2018111414.log
> sed -n '/2019-07-16 15:00:00/,/2019-07-16 18:00:00/p'  nohup.out > 20190716.log

按行数截取日志

> tail -2000 catalina.out > catalina-log-hbp.txt

telnet

当前用户的环境变量

> vim ~/.bash_profile

全局的环境变量
> vim /etc/profile

刷新环境变量
> source /etc/profile

rz/sz 文件上传/下载
> sudo yum install lrzsz -y 

错误:
1).
gzip: stdin: not in gzip format
tar: Child returned status 1
tar: Error is not recoverable: exiting now

https://www.cnblogs.com/llxx07/p/6409384.html

[虚拟机能ping通物理机，物理机无法ping通虚拟机](https://jingyan.baidu.com/article/a378c960f083f0b3282830ca.html)

------xshell---------------------

- [xshell/xftp 永不过期](https://zhangjia.tv/506.html)

- [官网注册个人Home and school use](https://www.netsarang.com/products/xsh_overview.html)

---------------------------
查看有多少个IP访问：

> awk '{print $1}' log_file|sort|uniq|wc -l

查看访问前十个ip地址

> awk '{print $1}' |sort|uniq -c|sort -nr |head -10 access_log

ps和kill组合使用

> ps -ef |grep hello |awk '{print $2}'|xargs kill -9

找出当前系统内存使用量较高的进程

> ps -aux | sort -rnk 4  | head -20

找出当前系统CPU使用量较高的进程

> ps -aux | sort -rnk 3  | head -20

```bash
# 设置行号
:set nu

# 取消行号
:set nonu
```

根据进程查询服务
> pwdx pid

#### SpringBoot `nohup`日志分割
日志分割`cronlog`
yum install cronolog -y

```shell script
#!/bin/sh
nohup java -jar caimombox-1.0-SNAPSHOT.jar | /usr/sbin/cronolog /caimom/log/box_dev/app-%Y-%m-%d.log &
```


### centos6.5升级
1. 解压： 
> tar xvf glibc-2.14.tar.gz

2. 进入glibc-2.14目录：
cd glibc-2.14

3. 创建build文件夹：
mkdir build

4. 进入build目录：
> cd build
> ../configure --prefix=/app/glibc-2.14

5. 执行：make编译
> make install

6. 重建软件
LD_PRELOAD=/app/glibc-2.14/lib/libc-2.14.so ln -s /app/glibc-2.14/lib/libc-2.14.so/lib64/libc.so.6

１、 cd /lib64
２、 LD_PRELOAD=/lib64/libc-2.12.so rm libc.so.6
３、 LD_PRELOAD=/lib64/libc-2.2.5.so ln -s /lib64/libc-2.2.5.so libc.so.6


export LD_LIBRARY_PATH=/app/glibc-2.14/lib

7. 失败还原
LD_PRELOAD=/lib64/libc-2.12.so ln-s/lib64/libc-2.12.so/lib64/libc.so.6/libc-2.12.so 

### rpm安装
ldd --version

rpm -Uvh glibc-2.14.1-6.x86_64.rpm --nodeps
rpm -Uvh glibc-common-2.14.1-6.x86_64.rpm  --nodeps
rpm -Uvh glibc-devel-2.14.1-6.x86_64.rpm --nodeps
rpm -Uvh glibc-headers-2.14.1-6.x86_64.rpm
rpm -Uvh glibc-static-2.14.1-6.x86_64.rpm
rpm -Uvh glibc-utils-2.14.1-6.x86_64.rpm
rpm -Uvh glibc-utils-2.14.1-6.x86_64.rpm --nodeps
rpm -Uvh nscd-2.14.1-6.x86_64.rpm --nodeps

### [centos7配置IP地址](https://www.cnblogs.com/yhongji/p/9336247.html)
vi /etc/sysconfig/network-scripts/ifcfg-ens32
```properties
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=dhcp
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=ens32
UUID=1ca0cb94-dab6-4021-8292-2ba293791535
DEVICE=ens32
ONBOOT=yes
```

（1）bootproto=static

（2）onboot=yes

（3）在最后加上几行，IP地址、子网掩码、网关、dns服务器
```properties
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=ens32
UUID=1ca0cb94-dab6-4021-8292-2ba293791535
DEVICE=ens32
ONBOOT=yes

IPADDR=192.168.1.160
NETMASK=255.255.255.0
GATEWAY=192.168.1.1
DNS1=119.29.29.29
DNS2=8.8.8.8
```

> systemctl restart network