视频教程
https://pan.baidu.com/s/1mFMPIXTSR1-wuRaLm7y6hw

[Hyperledger Fabric 踩坑汇总](https://www.cnblogs.com/biaogejiushibiao/p/12290728.html)

# 手把手教你hyperledger fabirc v2.0.1 

> 网上大多数hyperledger fabric的教程都是基于0.6或者1.0等比较老的版本, 主要采用go语言开发chaincode, 采用java-sdk去调用链码.

>从fabirc1.1开始,官方推荐使用nodejs去开发链码,node-sdk调用代码. 
传智播客物联网+区块链学院带您使用nodejs开发hyperledger.

## 0.环境搭建准备工作

安装工具依赖

* curl
* nodejs
* npm package manager
* docker
* docker Compose
* git

建议使用ubuntu服务器,这里我直接使用了阿里云的乞丐版服务器,配置如下:

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/421F84B5F00E4D1AADD0DDA109FEDA9D/136)


操作系统为Ubuntu 14.04(64位)为保证后续步骤一致,请使用跟我相同的版本.



## 1. 远超登录终端准备
用putty或者xshell远超连接进去

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/F34684F24A1541C49EDAEE8E98D5D5A9/147)



## 2. 安装git
``` shell
sudo apt-get update
sudo apt-get install git
```

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/B81421F12EF749859012A426ED493736/159)

```shell
# git更新到最新版
apt install software-properties-common -y
add-apt-repository ppa:git-core/ppa -y
apt install git -y
# 查看git版本 2.26.1
git version
```

## 安装go

```shell
# 卸载旧版本
sudo apt-get remove golang-go
sudo apt-get remove --auto-remove golang-go
# 下载
wget https://studygolang.com/dl/golang/go1.14.2.linux-amd64.tar.gz
# 解压
tar -zxvf go1.14.2.linux-amd64.tar.gz -C /usr/local
# 修改环境变量
sudo vim /etc/profile
# 添加go环境变量
export PATH=$PATH:/usr/local/go/bin
# 刷新变量
source /etc/profile
#----------------------
# 使用七牛云 go module 镜像
go env -w GO111MODULE=on
go env -w GOPROXY=https://goproxy.cn,direct
```



## 3. 安装docker-ce

请不要直接apt安装旧版本的docker

[阿里云安装docker教程](https://yq.aliyun.com/articles/110806?spm=5176.8351553.0.0.5d4e1991URD8Ia)

```shell
# step 1: 安装必要的一些系统工具
sudo apt-get update
sudo apt-get -y install apt-transport-https ca-certificates curl software-properties-common
# step 2: 安装GPG证书
curl -fsSL http://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | sudo apt-key add -
# Step 3: 写入软件源信息
sudo add-apt-repository "deb [arch=amd64] http://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"
# Step 4: 更新并安装 Docker-CE
sudo apt-get -y update
sudo apt-get -y install docker-ce
# Step 5: 查看docker版本
docker version
```

安装完毕后效果如下: 
查看版本
![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/8B8AA89DF747458CBAA67B3A0925CF19/195)


## 4. 设置阿里云docker加速服务

从docker官方的镜像服务器里面下载image非常慢, 使用阿里云的好处是可以拥有阿里的镜像加速服务, 速度可以达到百兆级别.
如果不配置也没有问题~ 多等一段时间就行了.

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/9418AB5F29C24A3195155A9994471D2A/179)

```shell
# 该加速器地址需要登陆阿里后查看（和个人阿里账号唯一绑定）
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://j8bshe1p.mirror.aliyuncs.com"]
}
EOF

sudo systemctl daemon-reload
sudo systemctl restart docker
```



ubuntu14.04系统采用不支持systemctl

``` shell
# 重启docker服务
service docker restart
```


## 5. 安装hyperledger的工具和docker镜像

点击官方[参考文档](http://hyperledger-fabric.readthedocs.io/en/release-1.1/samples.html#binaries)

[2.x](https://hyperledger-fabric.readthedocs.io/en/release-2.0/install.html)

```shell
# 下载bootstrap.sh
wget https://github.com/hyperledger/fabric/blob/release-2.0/scripts/bootstrap.sh
# 文件授权
chmod 777 bootstrap.sh
# 执行脚本
./bootstrap.sh
```



bootstrap.sh

```sh
#!/bin/bash
#
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#

# if version not passed in, default to latest released version
VERSION=2.0.1
# if ca version not passed in, default to latest released version
CA_VERSION=1.4.6
# current version of thirdparty images (couchdb, kafka and zookeeper) released
THIRDPARTY_IMAGE_VERSION=0.4.18
ARCH=$(echo "$(uname -s|tr '[:upper:]' '[:lower:]'|sed 's/mingw64_nt.*/windows/')-$(uname -m | sed 's/x86_64/amd64/g')")
MARCH=$(uname -m)

printHelp() {
    echo "Usage: bootstrap.sh [version [ca_version [thirdparty_version]]] [options]"
    echo
    echo "options:"
    echo "-h : this help"
    echo "-d : bypass docker image download"
    echo "-s : bypass fabric-samples repo clone"
    echo "-b : bypass download of platform-specific binaries"
    echo
    echo "e.g. bootstrap.sh 2.0.1 1.4.6 0.4.18 -s"
    echo "would download docker images and binaries for Fabric v2.0.1 and Fabric CA v1.4.6"
}

# dockerPull() pulls docker images from fabric and chaincode repositories
# note, if a docker image doesn't exist for a requested release, it will simply
# be skipped, since this script doesn't terminate upon errors.

dockerPull() {
    #three_digit_image_tag is passed in, e.g. "1.4.6"
    three_digit_image_tag=$1
    shift
    #two_digit_image_tag is derived, e.g. "1.4", especially useful as a local tag for two digit references to most recent baseos, ccenv, javaenv, nodeenv patch releases
    two_digit_image_tag=$(echo $three_digit_image_tag | cut -d'.' -f1,2)
    while [[ $# -gt 0 ]]
    do
        image_name="$1"
        echo "====> hyperledger/fabric-$image_name:$three_digit_image_tag"
        docker pull "hyperledger/fabric-$image_name:$three_digit_image_tag"
        docker tag "hyperledger/fabric-$image_name:$three_digit_image_tag" "hyperledger/fabric-$image_name"
        docker tag "hyperledger/fabric-$image_name:$three_digit_image_tag" "hyperledger/fabric-$image_name:$two_digit_image_tag"
        shift
    done
}

cloneSamplesRepo() {
    # clone (if needed) hyperledger/fabric-samples and checkout corresponding
    # version to the binaries and docker images to be downloaded
    if [ -d first-network ]; then
        # if we are in the fabric-samples repo, checkout corresponding version
        echo "===> Checking out v${VERSION} of hyperledger/fabric-samples"
        git checkout v${VERSION}
    elif [ -d fabric-samples ]; then
        # if fabric-samples repo already cloned and in current directory,
        # cd fabric-samples and checkout corresponding version
        echo "===> Checking out v${VERSION} of hyperledger/fabric-samples"
        cd fabric-samples && git checkout v${VERSION}
    else
        echo "===> Cloning hyperledger/fabric-samples repo and checkout v${VERSION}"
        git clone -b master https://github.com/hyperledger/fabric-samples.git && cd fabric-samples && git checkout v${VERSION}
    fi
}

# This will download the .tar.gz
download() {
    local BINARY_FILE=$1
    local URL=$2
    echo "===> Downloading: " "${URL}"
    wget "${URL}" || rc=$?
    tar xvzf "${BINARY_FILE}" || rc=$?
    rm "${BINARY_FILE}"
    if [ -n "$rc" ]; then
        echo "==> There was an error downloading the binary file."
        return 22
    else
        echo "==> Done."
    fi
}

pullBinaries() {
    echo "===> Downloading version ${FABRIC_TAG} platform specific fabric binaries"
    download "${BINARY_FILE}" "https://github.com/hyperledger/fabric/releases/download/v${VERSION}/${BINARY_FILE}"
    if [ $? -eq 22 ]; then
        echo
        echo "------> ${FABRIC_TAG} platform specific fabric binary is not available to download <----"
        echo
        exit
    fi

    echo "===> Downloading version ${CA_TAG} platform specific fabric-ca-client binary"
    download "${CA_BINARY_FILE}" "https://github.com/hyperledger/fabric-ca/releases/download/v${CA_VERSION}/${CA_BINARY_FILE}"
    if [ $? -eq 22 ]; then
        echo
        echo "------> ${CA_TAG} fabric-ca-client binary is not available to download  (Available from 1.1.0-rc1) <----"
        echo
        exit
    fi
}

pullDockerImages() {
    command -v docker >& /dev/null
    NODOCKER=$?
    if [ "${NODOCKER}" == 0 ]; then
        FABRIC_IMAGES=(peer orderer ccenv tools)
        case "$VERSION" in
        1.*)
            FABRIC_IMAGES+=(javaenv)
            shift
            ;;
        2.*)
            FABRIC_IMAGES+=(nodeenv baseos javaenv)
            shift
            ;;
        esac
        echo "FABRIC_IMAGES:" "${FABRIC_IMAGES[@]}"
        echo "===> Pulling fabric Images"
        dockerPull "${FABRIC_TAG}" "${FABRIC_IMAGES[@]}"
        echo "===> Pulling fabric ca Image"
        CA_IMAGE=(ca)
        dockerPull "${CA_TAG}" "${CA_IMAGE[@]}"
        echo "===> Pulling thirdparty docker images"
        THIRDPARTY_IMAGES=(zookeeper kafka couchdb)
        dockerPull "${THIRDPARTY_TAG}" "${THIRDPARTY_IMAGES[@]}"
        echo
        echo "===> List out hyperledger docker images"
        docker images | grep hyperledger
    else
        echo "========================================================="
        echo "Docker not installed, bypassing download of Fabric images"
        echo "========================================================="
    fi
}

DOCKER=true
SAMPLES=true
BINARIES=true

# Parse commandline args pull out
# version and/or ca-version strings first
if [ -n "$1" ] && [ "${1:0:1}" != "-" ]; then
    VERSION=$1;shift
    if [ -n "$1" ]  && [ "${1:0:1}" != "-" ]; then
        CA_VERSION=$1;shift
        if [ -n  "$1" ] && [ "${1:0:1}" != "-" ]; then
            THIRDPARTY_IMAGE_VERSION=$1;shift
        fi
    fi
fi

# prior to 1.2.0 architecture was determined by uname -m
if [[ $VERSION =~ ^1\.[0-1]\.* ]]; then
    export FABRIC_TAG=${MARCH}-${VERSION}
    export CA_TAG=${MARCH}-${CA_VERSION}
    export THIRDPARTY_TAG=${MARCH}-${THIRDPARTY_IMAGE_VERSION}
else
    # starting with 1.2.0, multi-arch images will be default
    : "${CA_TAG:="$CA_VERSION"}"
    : "${FABRIC_TAG:="$VERSION"}"
    : "${THIRDPARTY_TAG:="$THIRDPARTY_IMAGE_VERSION"}"
fi

BINARY_FILE=hyperledger-fabric-${ARCH}-${VERSION}.tar.gz
CA_BINARY_FILE=hyperledger-fabric-ca-${ARCH}-${CA_VERSION}.tar.gz

# then parse opts
while getopts "h?dsb" opt; do
    case "$opt" in
        h|\?)
            printHelp
            exit 0
            ;;
        d)  DOCKER=false
            ;;
        s)  SAMPLES=false
            ;;
        b)  BINARIES=false
            ;;
    esac
done

if [ "$SAMPLES" == "true" ]; then
    echo
    echo "Clone hyperledger/fabric-samples repo"
    echo
    cloneSamplesRepo
fi
if [ "$BINARIES" == "true" ]; then
    echo
    echo "Pull Hyperledger Fabric binaries"
    echo
    pullBinaries
fi
if [ "$DOCKER" == "true" ]; then
    echo
    echo "Pull Hyperledger Fabric docker images"
    echo
    pullDockerImages
fi
```



```text
window直接上传,报错:
-bash: ./bootstrap.sh: /bin/bash^M: bad interpreter: No such file or directory
vim bootstrap.sh
set ff=unix
```



* 注意: 按照官方文档执行, 需要全局翻墙才行

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/BB783CDD52B842A4A1BDAE19923FBA5E/225)

* 安装完检查目录结构和docker镜像:

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/AC3A8997B51C4C65AF9E1D4CF7F3FCD1/232)


## ~~6. 下载官方的示例代码 fabric sample~~

``` shell
# v2.0.1 已下载
git clone https://github.com/hyperledger/fabric-samples.git
git checkout v2.0.1
```



![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/DA434F1251A342F99E1BACC24C208524/240)



## 7. 切换到first-network目录

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/0F0DA979549046F488AF91EDAC4D4CB5/247)

```shell
cd fabric-smples/first-network
```



## 8. 启动fabric ledger的第一个网络
运行如下命令:
```shell
# https://docs.docker.com/compose/install/
#1. 配置环境变量, fabirc的二进制工具
export PATH=/root/bin:$PATH
#2. 生成hyperledger fabric的各种区块链配置
./byfn.sh -m generate
#3. 安装docker-compose
sudo curl -L "https://github.com/docker/compose/releases/download/1.25.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
# 修改执行权限
sudo chmod +x /usr/local/bin/docker-compose
# 参考文档  https://github.com/docker/compose/releases
#4. 启动first-network
./byfn.sh -m up
```


## 9. 修复阿里云服务器网络错误的问题
(腾讯云不存在这个问题,自己装ubuntu也不存在这个问题)

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/A34332D869CC472C97B03FEFACCECD66/272)

>vim /etc/resolv.conf 
>注释掉 options timeout:2 attempts:3 rotate single-request-reopen
>
>重新执行
```shell
# 停止
./byfn.sh -m down
# 执行
./byfn.sh -m up
# ./byfn.sh -m up -c mychannel -s couchdb -a
```

就能够正常启动了.



```shell
Error: error getting chaincode bytes: failed to calculate dependencies: incomplete package: github.com/hyperledger/fabric-contract-api-go/contractapi
# 原因: 安装github上go的依赖包的时候,长时间无响应
# 使用七牛云 go module 镜像
go env -w GO111MODULE=on
go env -w GOPROXY=https://goproxy.cn,direct
```





## 10. 验证网络搭建
接下来你能看到,peer节点启动,通道建立, 加入通道, 设置锚节点,实例化链码,调用链码等一系列的操作.如果你看到下面的图,恭喜你!
你的网络搭建好了.
![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/BDA5E5DA03D14EF499B1D5896B673B19/283)

>看到上面的截图,说明你的开发环境已经准备好, 接下来我们就可以搭建自己的组织结构,编写nodejs的链码了.



## 11. 停止网络请使用命令

``` shell
./byfn.sh -m down
```



## 12. 切换到basic-network目录

来到fabirc-sample目录的basic-network文件夹



## 13. 修改 basic-network的docker-compose.yml

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/14483191ED1C41A7991A62E954BE79BD/298)
> 说明: 启用开发者模式,这样加快调试部署,减少资源开销
> 开启7052端口, 开发模式下不使用tls会减少出错的概率,生产环境需要启用tls



## 14. 修改 脚本 `start.sh`

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/106A58A051F846CEACDE25E84C92C1B2/313)
增加cli节点, cli是方面我们执行控制指令的终端. 我们会使用他与各个peer节点进行交互.  后面这些手动的命令,会通过nodejs的api来调用



## 15. 启动脚本 'start.h'
![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/EAD0F21FA9394D5CB8E6394758C2BB26/326)

>  可以看到启动了ca节点,peer节点,order节点,cli节点和couchdb,创建了channel,peer加入了channel


## 16. 查看状态
![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/FAF8C3A10DF14B47B8AF757A6FBE8989/337)

```shell
docker ps
#可以看到当前运行的docker容器, peer,order,couchdb,ca,cli节点
docker exec -it bash 
#进入cli容器
peer channel list
#查看当前peer加入的channel
```



## 17. chaincode编写需要使用nodejs

请安装>8.0版本的nodejs

官网连接 [点我直达](https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions)
```shell
curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
sudo apt-get install -y nodejs
```



## 18.编写nodejs的chaincode
```shell
#1.创建mycc文件夹
mkdir mycc
#2. 初始化package.json文件
npm init 
#3. 修改package.json文件
npm install --save fabric-shim --registry=https://registry.npm.taobao.org
```
成功后 目录结构如下:
终于package.json文件
![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/91A4EDDF81814883A9EE7572AB93DC6E/374)




## 19. 编写nodejs链码
```javascript
const shim = require('fabric-shim');
const Chaincode = class{
    //链码初始化操作
    async Init(stub){
        var ret = stub.getFunctionAndParameters();
        var args  = ret.params;
        var a = args[0];
        var aValue = args[1];
        var b = args[2];
        var bValue = args[3];
        await  stub.putState(a,Buffer.from(aValue));
        await  stub.putState(b,Buffer.from(bValue));
        return shim.success(Buffer.from('heima chaincodinit successs'));
    }
    
    async Invoke(stub){
        let ret = stub.getFunctionAndParameters();
        let fcn = this[ret.fcn];
        return fcn(stub,ret.params);
    }
    //查询操作
    async query(stub,args){
        let a = args[0];
        let balance = await stub.getState(a);
        return shim.success(balance);
    }

};
shim.start(new Chaincode());
```


## 20. 把chaincode注册给peer
他们之间通过grcp协议通信
```shell
CORE_CHAINCODE_ID_NAME="mycc:v0"  npm start -- --peer.address grpc://192.168.0.1:7052
```
![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/94BCB93C2A2C4187865814C0844C15AD/390)



## 21. 在peer上install安装链码
这是peer上chaincode的生命周期

```shell
CORE_PEER_LOCALMSPID=Org1MSP CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp peer chaincode install -l node -n mycc -v v0 -p /opt/gopath/src/github.com/mycc/

```
![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/5BB063BA7E4449F2BCE3752F44F2FA86/406)



## 22. 在peer上实例化链码

```shell
CORE_PEER_LOCALMSPID=Org1MSP CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp peer chaincode instantiate -l node -n mycc -v v0 -C mychannel -c '{"args":["init","zzh","100","czbk","100"]}' -o 192.168.0.1:7050
```

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/41BD7FC27F664B6D972E7C6BF048042F/413)


## 23. 测试链码调用


```shell
CORE_PEER_LOCALMSPID=Org1MSP CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp peer chaincode invoke -n mycc -C mychannel -c '{"args":["query","zzh"]}' -o 192.168.0.1:7050
```

![image](https://note.youdao.com/yws/public/resource/95c087db9c2f4249616a4058c521ca13/xmlnote/6E1AB1E615674B20AF6025CB40526D15/422)

可以查看到zzh账户上有100块钱.



## 24. 同理大家可以实现转账的操作.

试着自己实现一下transfer方法吧

## 25. 停止网络使用
``` shell
./stop.sh ./teardown.sh
```




## 26. 查看环境是否清理干净
``` shell
docker ps
```

> 无内容就说明环境清理干净
