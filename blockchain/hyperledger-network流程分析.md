







# network流程分析

启动区块链测试网络

```shell
./byfn.sh -m up
```

创建证书使用crytogen的工具

```
Generate certificates using cryptogen tool
```

创建orderer节点的创世区块

```
Generating Orderer Genesis block
```

创建channel

```
Generating channel configuration
```

创建锚节点

```
Generating anchor peer update for Org1MSP
Generating anchor peer update for Org2MSP
```

docker启动

```
Creating network "net_byfn" with the default driver
Creating volume "net_orderer.example.com" with default driver
Creating volume "net_peer0.org1.example.com" with default driver
Creating volume "net_peer1.org1.example.com" with default driver
Creating volume "net_peer0.org2.example.com" with default driver
Creating volume "net_peer1.org2.example.com" with default driver
Creating volume "net_orderer2.example.com" with default driver
Creating volume "net_orderer3.example.com" with default driver
Creating volume "net_orderer4.example.com" with default driver
Creating volume "net_orderer5.example.com" with default driver
Creating orderer5.example.com   ... done
Creating peer0.org2.example.com ... done
Creating peer1.org2.example.com ... done
Creating orderer4.example.com   ... done
Creating peer0.org1.example.com ... done
Creating orderer3.example.com   ... done
Creating orderer.example.com    ... done
Creating peer1.org1.example.com ... done
Creating orderer2.example.com   ... done
Creating cli                    ... done
```

在org1里面的peer0 创建了一个channel

```
Channel 'mychannel' created
```

org1的peer0加入了mychannel

```
peer0.org1 joined channel 'mychannel'
```

org2的peer1加入了mychannel

```
peer1.org2 joined channel 'mychannel'
```

org2的peer0加入了mychannel

```
peer0.org2 joined channel 'mychannel'
```

org1的peer2加入了mychannel

```
peer1.org2 joined channel 'mychannel'
```

创建锚节点,用来在不同的组织之间数据通信(gossip协议)

```
Anchor peers updated for org 'Org1MSP' on channel 'mychannel'
Anchor peers updated for org 'Org2MSP' on channel 'mychannel'
```

安装chaincode,有两台电脑安装了智能合约的

```
Chaincode is packaged on peer0.org1
Chaincode is packaged on peer0.org2
```

~~实例化chaincode 背书策略,一个人同意即可~~

```
Chaincode is installed on peer0.org1
Chaincode is installed on peer0.org2
```

在org2上实例化的chaincode可以在org1里面查询出来

```
Query installed successful on peer0.org1 on channel
Query Result:100
```

在Org1组织的peer0上执行invoke操作

```
"invoke","a","b","10"
# a转10元给b
```

![流程分析](assets\test\network\network流程分析.png)