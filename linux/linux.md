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