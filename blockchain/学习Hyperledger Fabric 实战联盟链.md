
# 学习Hyperledger Fabric 实战联盟链

## 1. 扩展

比特币白皮书一种点对点的电子现金系统 中本聪
区块链技术指南 杨宝华yeasy
Hyperledger Fabric官方文档

## 2. hyperledger开源项目

* explorder 区块浏览器

* composer 快速构建

## 3. 环境搭建

* 操作系统(centos)
* 基于docker

步骤:

1. docker环境支持 (docker、docker-compose)

2. fabric组件的docker官方镜像

	fabric-peer

	fabric-orderer

	fabric-tools

	fabric-ca

3. 按照go环境

4. fabric源码库 https://github.com/hyperledger/fabric

5. 源码库版本切换 -> release-1.0

> git checkout release-1.0

6. crptogen、configtxgen工具编译

进入fabric的目录安装

> go install --tags=nopkcs11



7. 下载 fabric-samples https://github.com/hyperledger/fabric-samples

8. 运行

 第一个Fabric网络 
 	
> byfn.sh -m generate 生成证书和创世区块
 	
> byfn.sh -m up

> byfn.sh -m down

> byfn.sh -m restart

```shell
# 可选参数
- c <channel name> 通道名称,默认`mychannel`
- t <timeout> 超时时间，默认10s
- s <dbtype> 数据库引擎,goleveldb、couchdb,默认`goleveldb`
```

> byfn.sh -m generate -c mychannel