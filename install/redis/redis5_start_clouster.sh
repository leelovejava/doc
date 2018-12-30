#!/bin/bash
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7000/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7001/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7002/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7003/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7004/redis.conf
/usr/local/redis-cluster/bin/redis-server /usr/local/redis-cluster/cluster-conf/7005/redis.conf
/usr/local/redis-cluster/bin/redis-cli --cluster create 192.168.124.134:7000 192.168.124.134:7001 192.168.124.134:7002 192.168.124.134:7003 192.168.124.134:7004 192.168.124.134:7005 --cluster-replicas 1