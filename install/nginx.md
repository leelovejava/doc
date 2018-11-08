#nginx

## 安装依赖
yum -y install gcc zlib zlib-devel pcre-devel openssl openssl-devel
## 下载并解压 
wget http://nginx.org/download/nginx-1.7.7.tar.gz 或 rz上传
tar -xvf nginx-1.7.7.tar.gz
cd nginx-1.7.7

## 安装
./configure --prefix=/usr/localhost/nginx --user=ucenter --group=ucenter
make 
make install
 
防火墙打开80端口
service iptables stop //关闭防火墙
 
/sbin/iptables -I INPUT -p tcp --dport 80 -j ACCEPT
/etc/rc.d/init.d/iptables save
/etc/init.d/iptables status

安装路径下的/nginx/sbin/nginx
//停止命令
安装路径下的/nginx/sbin/nginx -s stop
或者 : nginx -s quit
//重启命令
安装路径下的/nginx/sbin/nginx -s reload

Nginx配置负载均衡
在http节点添加：
    upstream taotao-manage {
      server 127.0.0.1:18080;
      server 127.0.0.1:18081;
  }
 
修改代理指向upstream
proxy_pass http://taotao-manage;