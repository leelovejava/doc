#!/bin/bash
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7000/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7001/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7002/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7003/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7004/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7005/redis.conf
/usr/local/redis-cluster/bin/redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 1