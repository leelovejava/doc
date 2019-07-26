[Kubernetes 真的很复杂吗？](https://mp.weixin.qq.com/s/ElD_nbf5Eav8ZfKRHiLudw)

[10分钟教你看懂 Docker 和 K8S](https://mp.weixin.qq.com/s/IxOtpmAUv6kEVSLLJgF0ZA)

[2019年最火的容器、K8S和DevOps入门都在这了](https://mp.weixin.qq.com/s/ExmE8zejcH70Erry0aozSQ)

---------mysql------------------
创建mysql容器
--e 指定配置文件
--name 指定容器名 
sudo docker run --name mysql -e MYSQL_ROOT_PASSWORD=introcks1234 -p 3306:3306 -d mysql:5.7




mysql8 native
MySQL 2059错误


alter user root@localhost identified by 'introcks1234' password expire never;
alter user root@localhost identified with mysql_native_password by 'introcks1234';
flush privileges;


docker exec -it mysql5.7 bash

docker删除镜像

docker rmi <image id>

docker删除容器

docker rm <容器id>
```
# 复制
docker cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH|-
docker cp [OPTIONS] SRC_PATH|- CONTAINER:DEST_PATH

# 将主机/www/runoob目录拷贝到容器96f7f14e99ab的/www目录下。
docker cp /www/runoob 96f7f14e99ab:/www/
# 将容器96f7f14e99ab的/www目录拷贝到主机的/tmp目录中。
docker cp  96f7f14e99ab:/www /tmp/
```

docker exec -i [nginx容器名/id] nginx -s reload

查看日志
docker log 容器名 --tail=1000


docker log 容器名 -f --tail=1000

-----------jdk----------------


jdk环境变量配置


vim /etc/profile
export JAVA_HOME=/usr/lib/jvm/jdk1.8.0_144
export PATH=$JAVA_HOME/bin:$PATH

source /etc/profile

java -version


-----------apollo----------------

https://github.com/ctripcorp/apollo/wiki/Quick-Start

8080-eureka
8090
8070


修改demo.sh的数据库

Caused by: org.hibernate.HibernateException: Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect


--------kong---------------------
https://www.jianshu.com/p/02b106b16e61

1).设置网络
docker network create kong-net

2).安装数据库，使用postgres

docker run -d --name kong-database \
              --network=kong-net \
              -p 5432:5432 \
              -e "POSTGRES_USER=kong" \
              -e "POSTGRES_DB=kong" \
              postgres:9.6

3).准备kong数据
docker run --rm \
    --network=kong-net \
    -e "KONG_DATABASE=postgres" \
    -e "KONG_PG_HOST=kong-database" \
    -e "KONG_CASSANDRA_CONTACT_POINTS=kong-database" \
    kong:latest kong migrations up

4).启动kong，设置postgres数据库
--network 网络

8000：此端口是Kong用来监听来自客户端的HTTP请求的，并将此请求转发到您的上游服务。这也是本教程中最主要用到的端口。

8443：此端口是Kong监听HTTP的请求的端口。该端口具有与8000端口类似的行为，但是它只监听HTTPS的请求，并不会产生转发行为。可以通过配置文件来禁用此端口。

8001：用于管理员对KONG进行配置的端口。

8444：用于管理员监听HTTPS请求的端口


docker run -d --name kong \
    --network=kong-net \
    -e "KONG_DATABASE=postgres" \
    -e "KONG_PG_HOST=kong-database" \
    -e "KONG_CASSANDRA_CONTACT_POINTS=kong-database" \
    -e "KONG_PROXY_ACCESS_LOG=/dev/stdout" \
    -e "KONG_ADMIN_ACCESS_LOG=/dev/stdout" \
    -e "KONG_PROXY_ERROR_LOG=/dev/stderr" \
    -e "KONG_ADMIN_ERROR_LOG=/dev/stderr" \
    -e "KONG_ADMIN_LISTEN=0.0.0.0:8001, 0.0.0.0:8444 ssl" \
    -p 8000:8000 \
    -p 8443:8443 \
    -p 8001:8001 \
    -p 8444:8444 \
    kong:latest


5).ui(kong-dashboard)
   -p 修改的端口:映射的端口


    docker run -d --name kong-dashboard \
    --network=kong-net \
    --link kong:kong \
    -p 8002:8080 \
    pgbi/kong-dashboard:v2 migrations up

6).插件

Basic Authentication    


----------lua------------------

# 1.安装依赖
sudo apt-get install libreadline-dev

# 2.源码编译
curl -R -O http://www.lua.org/ftp/lua-5.3.5.tar.gz
tar zxf lua-5.3.5.tar.gz
cd lua-5.3.5
make linux test    

# 3.配置环境变量
vim /etc/profile

export LUA_HOME=/usr/lib/lua-5.3.5
export PATH=$LUA_HOME/src:$PATH

source /etc/prfile


错误:
The program 'make' can be found in the following packages:
 * make
 * make-guile
Try: apt install <selected package>

解决:
sudo apt-get install build-essential


----
查看容器端口映射信息
docker container port CONTAINER_ID