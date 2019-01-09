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



 关闭防火墙
  https://blog.csdn.net/Post_Yuan/article/details/78603212  
  systemctl stop firewalld
  systemctl disable firewalld

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
> yum install wget

安装tar
> yum install -y tar

安装vim
> yum -y install vim*

安装netstat
> yum install net-tools

安装telnet
https://www.cnblogs.com/happyflyingpig/p/8127885.html
> yum -y install telnet-server.x86_64 
> yum -y install telnet.x86_64
> yum -y install xinetd.x86_64

安装gcc
> yum install gcc-c++

授予root权限
https://www.linuxidc.com/Linux/2012-07/64530.htm

查看文件大小
> ls -lht

按日期截取日志
> sed -n '/2018-11-14 10:00:/,/2018-11-14 12:00:/p' intellif_monitor_info.log > 2018111414.log

telnet

当前用户的环境变量

> vim ~/.bash_profile

全局的环境变量
> vim /etc/profile

rz/sz 文件上传/下载
> sudo yum install lrzsz -y 

错误:
1).
gzip: stdin: not in gzip format
tar: Child returned status 1
tar: Error is not recoverable: exiting now

https://www.cnblogs.com/llxx07/p/6409384.html

---------------------------
xshell/xftp 永不过期
https://zhangjia.tv/506.html
官网注册个人Home and school use
https://www.netsarang.com/products/xsh_overview.html
---------------------------


-[阿里java问题排查单](http://www.jiangxinlingdu.com/thought/2018/11/17/javatools.html)

-[系统性能优化系列](http://www.jiangxinlingdu.com/thought/2018/09/15/linuxsys.html)

-[常用性能监控指南](http://blog.720ui.com/2018/linux_performance_command/)

-[线上操作与线上问题排查实战](http://www.jiangxinlingdu.com/practice/2018/09/13/operation.html)

-[不停机图片升级迁移](http://www.jiangxinlingdu.com/thought/2018/08/15/images.html)