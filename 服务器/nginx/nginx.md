# nginx

## doc
-[使用Nginx+Lua开发高性能Web应用](https://mp.weixin.qq.com/s/yTNSCTOvSsBZVYOKoedPpw)

-[顺风详解Nginx系列—Ngx中的变量](https://mp.weixin.qq.com/s/_83bFalgPnngTX2PfPqtrQ)

-[顺风详解Nginx系列—nginx变量实现原理(上)](https://mp.weixin.qq.com/s/wSnd-7XXFqElwGspuZltdQ)

- [Nginx 搭建图片服务器](https://mp.weixin.qq.com/s/BKmFjFEsDXUx1voEPz5TvA)

- [1分钟搞定 Nginx 版本的平滑升级与回滚](https://mp.weixin.qq.com/s/8cUtSPiVK_8KuCGROifG6Q)

- [基于 Nginx 的 HTTPS 性能优化实践](https://mp.weixin.qq.com/s/ZPF6rSJ9jjjCGXl99Q8sqQ)

## 配置

include 引用vhost目录下所有以conf结尾的
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