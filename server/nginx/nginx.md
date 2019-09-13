# Nginx

## doc
- [使用Nginx+Lua开发高性能Web应用](https://mp.weixin.qq.com/s/yTNSCTOvSsBZVYOKoedPpw)

- [顺风详解Nginx系列—Ngx中的变量](https://mp.weixin.qq.com/s/_83bFalgPnngTX2PfPqtrQ)

- [顺风详解Nginx系列—nginx变量实现原理(上)](https://mp.weixin.qq.com/s/wSnd-7XXFqElwGspuZltdQ)

- [Nginx 搭建图片服务器](https://mp.weixin.qq.com/s/BKmFjFEsDXUx1voEPz5TvA)

- [1分钟搞定 Nginx 版本的平滑升级与回滚](https://mp.weixin.qq.com/s/8cUtSPiVK_8KuCGROifG6Q)

- [基于 Nginx 的 HTTPS 性能优化实践](https://mp.weixin.qq.com/s/ZPF6rSJ9jjjCGXl99Q8sqQ)

## 安装
安装依赖
yum -y install gcc zlib zlib-devel pcre-devel openssl openssl-devel

下载
wget http://nginx.org/download/nginx-1.7.7.tar.gz 或 rz上传
tar -xvf nginx-1.7.7.tar.gz
cd nginx-1.7.7



./configure --prefix=/usr/localhost/nginx --user=ucenter --group=ucenter
make 
make install
 
防火墙打开80端口
关闭防火墙
>> service iptables stop
 
>> /sbin/iptables -I INPUT -p tcp --dport 80 -j ACCEPT
>> /etc/rc.d/init.d/iptables save
>> /etc/init.d/iptables status


停止命令
>> nginx/sbin/nginx -s stop
或者
>> nginx -s quit

重启命令
>> nginx/sbin/nginx -s reload

Nginx配置负载均衡
在http节点添加：
upstream taotao-manage {
  server 127.0.0.1:18080;
  server 127.0.0.1:18081;
}
 
修改代理指向upstream
proxy_pass http://taotao-manage;

## 配置

### 参数详解
```
########### 每个指令必须有分号结束。#################
# 配置用户或者组，默认为nobody nobody。
#user administrator administrators;  

# 允许生成的进程数，默认为1
#worker_processes 2;  
# 指定nginx进程运行文件存放地址
#pid /nginx/pid/nginx.pid;  
# 指定日志路径，级别。这个设置可以放入全局块，http块，server块，级别以此为：debug|info|notice|warn|error|crit|alert|emerg
error_log log/error.log debug;  
events {
    # 设置网路连接序列化，防止惊群现象发生，默认为on
    accept_mutex on;   
    # 设置一个进程是否同时接受多个网络连接，默认为off
    multi_accept on;  
    # 事件驱动模型，select|poll|kqueue|epoll|resig|/dev/poll|eventport
    #use epoll;    
    # 最大连接数，默认为512  
    worker_connections  1024;    
}
http {
    # 文件扩展名与文件类型映射表
    include       mime.types;   
    default_type  application/octet-stream; #默认文件类型，默认为text/plain
    # 取消服务日志
    #access_log off; 
    log_format myFormat '$remote_addr–$remote_user [$time_local] $request $status $body_bytes_sent $http_referer $http_user_agent $http_x_forwarded_for'; #自定义格式
    # 日志位置
    access_log log/access.log myFormat;  #combined为日志格式的默认值
    sendfile on;   #允许sendfile方式传输文件，默认为off，可以在http块，server块，location块。
    sendfile_max_chunk 100k;  #每个进程每次调用传输数量不能大于设定的值，默认为0，即不设上限。
    keepalive_timeout 65;  #连接超时时间，默认为75s，可以在http，server，location块。

    upstream mysvr {   
      server 127.0.0.1:7878;
      # backup 热备
      server 192.168.10.121:3333 backup;  
    }
    # 错误页
    error_page 404 https://www.baidu.com; 
    server {
        # 单连接请求上限次数。
        keepalive_requests 120; 
        # 监听端口
        listen       4545;  
        # 监听地址 
        server_name  127.0.0.1;         
        # 请求的url过滤，正则匹配，~为区分大小写，~*为不区分大小写。
        location  ~*^.+$ {       
           #root path;  #根目录
           #index vv.txt;  #设置默认页
           # 请求转向mysvr 定义的服务器列表
           proxy_pass  http://mysvr;  
           # 拒绝的ip
           deny 127.0.0.1;  
           # 允许的ip  
           allow 172.18.5.54;          
        } 
    }
}
```

### 1. include 引用vhost目录下所有以conf结尾的

```
http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;
    
    include vhost/*.conf;
}
```



### 2. 静态资源配置

```
server {
    listen       81;
    server_name  localhost;
    location / {
        root   cart;
        index  cart.html;
    }      
}
server {
    listen       82;
    server_name  localhost;
    location / {
        root   search;
        index  search.html;
    }        
}
```



 ### 3.域名绑定

```
server {
    listen       80;
    server_name  cart.pinyougou.com;
    location / {
        root   cart;
        index  cart.html;
    }
}
server {
    listen       80;
    server_name  search.pinyougou.com;
    location / {
        root   search;
        index  search.html;
    }
}
```

### 4.反向代理

```
   upstream tomcat-portal {
   	server 192.168.25.141:8080;
   }
   server {
       listen       80;
       server_name  www.pinyougou.com;   
       location / {
       	proxy_pass   http://tomcat-portal;
       	index  index.html;
       }
   }
```

   

### 5.负载均衡

```
upstream tomcat-portal {
  server 192.168.25.141:8080;
  server 192.168.25.141:8180;
  server 192.168.25.141:8280;
}
server {
listen       80;
server_name  www.pinyougou.com;

location / {
    proxy_pass   http://tomcat-portal;
    index  index.html;
  }       

}
```

### 6.Https
```
server {
    # 1.1版本后这样写
    listen 443 ssl;  
    # 填写绑定证书的域名
    server_name www.domain.com; 
    # 指定证书的位置，绝对路径
    ssl_certificate 1_www.domain.com_bundle.crt;  
    # 绝对路径，同上
    ssl_certificate_key 2_www.domain.com.key;  
    ssl_session_timeout 5m;
    # 按照这个协议配置
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2; 
    # 按照这个套件配置
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
    ssl_prefer_server_ciphers on;
    location / {
        root   html; #站点目录，绝对路径
        index  index.html index.htm;
    }
}
```   

## Nginx+keepalived高可用配置实战

**1、整体架构图如下**

![img](https://mmbiz.qpic.cn/mmbiz_jpg/tuSaKc6SfPpicC9OvKItnqMAc5NicawN8ymOertxUI96E3PQ8vLVFNticQf6kFopAlAKa5AwzYlBCl0licuKAuUuVA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



**2、环境准备**

今天所配置的是keepalived+nginx 的负载均衡

下载keepalived软件

[root@LB01 tools]# wget http://www.keepalived.org/software/keepalived-1.1.17.tar.gz

注意安装前检查内核的link文件

root@LB02 tools]# ll /usr/src/

total 8

drwxr-xr-x. 2 root root 4096 Sep 23  2011 debug

drwxr-xr-x. 3 root root 4096 Oct 19 02:03 kernels

lrwxrwxrwx. 1 root root 43 Oct 19 02:05 linux -> /usr/src/kernels/2.6.32-642.6.1.el6.x86_64/

安装keepalived之前，安装几个依赖包

yum install openssl-devel -y

yum install popt* -y

然后进行编译安装keepalived,前面介绍了安装过程，这里就不演示了

./configure得出下面的结果

Keepalived configuration

\------------------------

Keepalived version    : 1.1.17

Compiler         : gcc

Compiler        : -g -O2

Extra Lib      : -lpopt -lssl -lcrypto

Use IPVS Framework   : Yes

IPVS sync daemon support : Yes

Use VRRP Framework    : Yes

Use LinkWatch      : No

Use Debug flags     : No

注意./configure之后的结果，没有错误就可以了

make && make install

之后规范配置、启动文件路径

/bin/cp /usr/local/etc/rc.d/init.d/keepalived /etc/init.d/

/bin/cp /usr/local/etc/sysconfig/keepalived /etc/sysconfig/

mkdir /etc/keepalived -p

/bin/cp /usr/local/etc/keepalived/keepalived.conf /etc/keepalived/

/bin/cp /usr/local/sbin/keepalived /usr/sbin/

/etc/init.d/keepalived start

注：nginx负载均衡相关配置请参考前面的文章

[**LNMP架构应用实战—Nginx反向代理负载均衡配置**](http://mp.weixin.qq.com/s?__biz=MzI0MDQ4MTM5NQ==&mid=2247484430&idx=1&sn=a81bb9b328eedc96fb0c60c40b32c68f&chksm=e91b6112de6ce80466be1de37c8502cca822fcf0dadeee5a8d2a7e470465d85f971e011454b1&scene=21#wechat_redirect)



**3、实战配置keepalived**

[root@LB01 keepalived]# vi keepalived.conf 

! Configuration File for keepalived 

global_defs {

   notification_email {

   abc@qq.com

   }

   notification_email_from Alexandre.Cassen@firewall.loc

   smtp_server 1.1.1.1

   smtp_connect_timeout 30

   router_id LVS_3

}

vrrp_instance VI_1 {

​    state MASTER

​    interface eth0

​    virtual_router_id 19

​    priority 150

​    advert_int 1

​    authentication {

​        auth_type PASS

​        auth_pass 1111

​    }

​    virtual_ipaddress {

​        192.168.1.254/24

​    }

}

[root@LB02 keepalived]# vi keepalived.conf

! Configuration File for keepalived

global_defs {

   notification_email {

   abc@qq.com

   }

   notification_email_from Alexandre.Cassen@firewall.loc

   smtp_server 1.1.1.1

   smtp_connect_timeout 30

   router_id LVS_6

}

vrrp_instance VI_1 {

​    state BACKUP

​    interface eth0

​    virtual_router_id 19

​    priority 100

​    advert_int 1

​    authentication {

​        auth_type PASS

​        auth_pass 1111

​    }

​    virtual_ipaddress {

​        192.168.1.254/24

​    }

}

[root@LB01 keepalived]# /etc/init.d/keepalived start

Starting keepalived:    [  OK  ]

[root@LB02 keepalived]# /etc/init.d/keepalived start

Starting keepalived    [  OK  ]

[root@LB01 keepalived]# ip add|grep 192.168.1.254

​    inet 192.168.1.254/24 scope global secondary eth0

[root@LB02 keepalived]# ip add|grep 192.168.1.254

测试访问

![img](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)

表明可以正常切换



现在我们模拟keepalived主宕机，再测试

[root@LB01 conf]# /etc/init.d/keepalived stop

Stopping keepalived:   [  OK  ]

[root@LB02 ~]# ip add|grep 254

​    inet 192.168.1.254/24 scope global secondary eth0



![img](https://mmbiz.qpic.cn/mmbiz_jpg/tuSaKc6SfPpicC9OvKItnqMAc5NicawN8yYFL5kVKEXzs8hGEPa4BfGy9X4xxibYnJR4RYU4BPhocvLgrhM4KIP1Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)





**4、反向代理服务故障自动切换**

如果实际生产环境中当keeplived主的服务器nginx服务宕机，但是主又有VIP，这时就出现无法访问的现象，因此可以做如下的配置，使得这种情况可自已切换

vi check_nginx.sh

\#!/bin/sh

white true

do

PNUM=`ps -ef|grep nginx|wc -l`

**#这里也可使用nmap 192.168.1.3 -p 80|grep open|wc -l来判断个数**

if [ $PNUM -lt 3 ];then

/etc/init.d/keepalived stop >/dec/null 2>&1

kill -9 keealived >/dec/null 2>&1

kill -9 keealived >/dec/null 2>&1

fi

sleep 5

done

sh check_nginx.sh &

**启动个守护进程进行检查（或者加入定时任务定时执行检查），如果nginx服务出现故障，就立马停掉keepalived的服务，让它自动切换到备节点上去，这样就实现了自动切换的工作**