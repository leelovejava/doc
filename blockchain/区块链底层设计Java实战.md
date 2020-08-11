

# 区块链Java底层实战

## 1. 序

### 初衷

降低区块链底层学习和开发的门槛，缩小学习区块链原理和理论到进入实战的鸿沟

### 目录

* 第1章是区块链简介，从研发维度戏说、正说区块链，评说区块链的应用前景。
* 第2章介绍区块链的底层架构。
* 第3章讲区块链中所用的密码学原理及Java实现。
* 第4章讲P2P网络原理及Java实现。
* 第5章讲分布式一致性算法及Java实现。
* 第6章讲知名公链的区块设计及Java实现。
* 第7章讲知名公链的区块存储技术及Java实现。
* 第8章讲知名公链币的设计及Java实现。
* 第9章讲联盟链管理后台的原理及实现。
* 第10章讲联盟链的运营。

### 使用场景:

* 游戏互动/小程序

  游戏及互动小程序，一直存在虚拟资产丢失或无法流通和被盗，或游戏/小程序投入后初期流量不足，开放联盟链可保护资产，还可对接支付宝小程序获得流量

* 公益溯源
  结合物联网技术，将捐赠/产品等数据的源头、交易、流转等，进行全流程记录，并将该记录展示给开放联盟链内生态各方，通过透明度赢得市场和更多的认可

* 版权合同
  数字作品/合同等涉及多方协同时，往往面临取证和认定问题，通过开放联盟链存证或查询，可以构建超越多方的登记、监测、数据等可信服务，促进交易进行

* 票据民生类
  票据民生类因为历史等原因，往往处于信息孤岛状态，而又因跨地域和中小企业多方，效率不高、透明度和改进动力不足，通过开放联盟链的低成本可快速串联

* 组织/朋友圈
  社会中的行业、组织或朋友圈，由于讲求私密性且存在临时性，导致关系中段，通过构建校友圈、行业圈、区域圈、党政圈（如退军人）、族谱等可永续关系

* 协作共建
  开放联盟链是一个小型的生态集市，链内需求方、技术方、服务方的三方可以自行匹配需求，同时也可以协同对外提供上链服务，共建低门槛的上链生态
  
* 跨境支付

  区块链技术通过分布式账本技术将原来像接力赛一样逐个节点确认传递的汇款模式，改变为业务节点实时同步并行确认，提升了效率，改变了运营模式。在汇出端钱包发起汇款的同时所有参与方同时收到该信息，在完成合规等所需的审核后，区块链上协同各方同时完成这一笔汇款交易。

### 专业术语

| 中文               | 英文                                        | 说明                                                         |
| ------------------ | ------------------------------------------- | ------------------------------------------------------------ |
| 标识               | Identity                                    | 在区块链中唯一标识一个账户或者智能合约，长度为 256 位。一般为一个唯一可读内容的哈希值。 |
| 存证数据           | Ledger data                                 | 区块链本身具有不可篡改的特性，写入区块链的数据都是可信任、不可篡改的，为了让数据具备公正力，写入区块链的数据可称为存证数据，存证数据可以是一个字符串、一个文件的哈希等，以表示文本、文件等存证数据。 |
| 根哈希             | Root hash                                   | 默克尔树的根哈希值，根据当前区块链交易算出。                 |
| 共识算法           | Consensus algorithm                         | 一种分布式系统数据一致性保证的算法，通过一定的协议交互来确保分布式系统的多个参与方达成数据的一致性。常见的算法包括 PBFT、RAFT、POW、POS 等。 |
| 共识证明           | Consensus proof                             | 用以证明目标数据经过共识算法一致性确认的数据结构。           |
| 交易               | Transaction                                 | 区块链上的一个事务请求，用来承载具体业务操作数据的结构。区块链上所有针对世界状态的变化操作均是基于交易来完成的。 |
| 交易个数           | Transaction count                           | 某一区块的交易数目。                                         |
| 交易哈希           | Transaction hash                            | 交易上链成功后，产生的唯一哈希值。                           |
| 交易回执           | Transaction receipt                         | 是交易的执行结果。区块链是异步的系统，交易执行后需要共识，与传统架构不同，不能直接返回交易执行是否成功，因此需在回执中查看最终交易结果。 |
| 交易类型           | Transaction type                            | 该交易的类型，如引用存证、内容存证、哈希存证、密文存证、隐私分享、纯密文存证。 |
| 交易量汇总         | Transactions                                | 交易总量，当前区块链账本上已有保存的交易总数量。             |
| 节点信息           | Node information                            | 区块链节点的相关信息。一个区块链一般由多节点组成，节点数目为 3F+1，其中 F 为正整数。 |
| 可信执行环境       | Trusted Execution Enviorment (TEE)          | 可信执行环境，提供硬件级别的资源隔离和信任度量功能。近年来在服务器及终端领域，TEE 技术及其应用越发引人关注，其中最具代表性的是 Intel SGX。 |
| 联盟               | Consortium                                  | 不同的机构为了共同合作完成某个业务而结成的联合。             |
| 联盟机构           | Organization                                | 组成联盟的机构。机构指联盟中租户所属的企业。联盟仅支持以租户为单位加入，联盟中不同的租户可能属于同一个机构。 |
| 签名证书           | Certificate                                 | 由支付宝合作的第三方 CA 机构根据用户提交的证书请求签发认证后的证书。 |
| 区块               | Block                                       | 区块链上存储打包交易数据以及交易执行结果数据的一种组织形式。区块彼此之间通过前向的应用彼此链接形成区块链。 每个区块记录着上一个区块的哈希值、本区块中的交易集合、本区块的哈希等基础数据。 |
| 区块高度           | Block height                                | 区块高度，简称块高，用来识别区块在区块链中的位置，并据此找到和这个区块相关的所有基础属性和交易记录。 |
| 区块链 ID          | Blockchain identification                   | 某一区块链的唯一标识，对应区块链这个底层唯一物理资源。       |
| 区块链高度         | Blockchain height                           | 当前区块链上出块（Block）的最大数目。                        |
| 区块链技术         | Blockchain                                  | 也被称之为分布式账本技术，是一种去中心化的分布式数据库技术，其特点是去中心化、公开透明、不可篡改、可信任。区块链的每笔数据，都会广播全网的区块链节点，每个节点都有全量的、一致的数据。 |
| 区块链应用         | Application                                 | 基于区块链 SDK 开发的应用。                                  |
| 去中心化应用       | Decentralized applications（DApp）          | 与传统中心化应用的主要区别是，DApp 通过客户端直接连接区块链节点，通过智能合约计算和访问数据，没有中心化的后端服务。 |
| 燃料               | Gas                                         | 智能合约在虚拟机中执行计算和存储的消耗度量，通过燃料可防止一些恶意攻击和计算、存储的浪费。 |
| 上一块哈希         | Previous block hash                         | 当前区块的上一区块哈希。                                     |
| 世界状态           | World state                                 | 区块链账户的存储状态，包含所有账户的基本存储状态和合约账户的内部存储状态。可以将合约平台理解为一种交易的“状态机”，世界状态描述当前的基本存储状态，经过执行智能合约，世界状态可能发生改变进入另外一个新的世界状态。 |
| 私钥               | Private key                                 | 私钥文件，通过 OpenSSL 等工具生成。生成过程中会产生 2 个密钥，一个是公钥，即是证书签名请求文件，另外一个是用户私钥，用户需保存好私钥和私钥密码。 |
| 虚拟机             | Virtual machine（VM）                       | 执行智能合约的沙箱环境。                                     |
| 业务 ID            | Business identification                     | 业务唯一标识，该区块链应用于哪种业务场景，如溯源、租房等。   |
| 业务分类           | Category                                    | 该交易上链的数据的业务数据格式类型。                         |
| 业务时间           | Business time                               | 该交易的提交生成时间。                                       |
| 英特尔软件保护扩展 | Intel Software Guard Extensions (Intel SGX) | Intel SGX 是 Intel CPU 上的一组扩展指令集，支持应用程序创建所谓的“安全区”，即应用地址空间中受保护的区域，它可确保应用程序安全区中数据的机密性和完整性，能够有效抵御任意特权级别软件的窥探和攻击。更多内容，详见 [英特尔官方文档](https://software.intel.com/zh-cn/articles/intel-software-guard-extensions-tutorial-part-1-foundation) 和 阿里云 SGX 基础介绍。 |
| 账户               | Account                                     | 区块链上的基本操作对象，一个用户主体在区块链上的逻辑表示。区块链上的所有交易操作均需要基于一个链上已经存在的账户来完成。可分为普通账户和合约账户。 |
| 证书颁发机构       | Certificate Authority（CA）                 | 数字证书颁发机构是受信任的第三方机构，颁发的数字证书是为最终用户数据加密的公共密钥。 |
| 证书申请           | Certificate request                         | 证书签名请求文件（Certificate Signing Request，CSR），通过 OpenSSL 等工具生成。生成过程中会产生 2 个密钥，一个是公钥，即该 CSR 文件，另外一个是用户私钥，用户需保存好私钥和私钥密码。 |
| 智能合约           | Smart Contract                              | 一种旨在以信息化方式传播、验证或执行合同的计算机协议。一个智能合约是一套以数字形式定义的承诺（promises），包括合约参与方可以在上面执行这些承诺的协议。智能合约允许在没有第三方的情况下进行可信交易，这些交易可追踪且不可逆转。 |

## 第1章 区块链简介

### 1.1戏说区块链

当笔者奉调出任区块链研发负责人之初，加班相对之前又多了些。加班多了，自然陪家里小宝宝玩儿的时间就少了。为此，小宝宝有点儿不开心。

家里三岁的小宝宝和笔者有过这样一段对话。

小宝宝：“爸爸，你怎么不回来陪我玩儿啊，我睡觉的时候你还没回来！

”笔者：“宝宝，爸爸去做区块链了。

事情很多，所以加班多啦。

”小宝宝：“什么是区块链啊？好玩不？

”笔者：“区块链是一个游戏，这个游戏可好玩了！

”小宝宝：“我也想玩，怎么玩啊？

”笔者：“比如，过年的时候，你会收到什么呀？

”小宝宝：“压岁钱！

”笔者：“对，那爸爸妈妈还会说什么呢？”

小宝宝：“爸爸妈妈先把毛爷爷帮我收起来，我长大了再花！

”笔者：“对。可是，如果等你长大了，爸爸妈妈没给你曾经收到的这么多压岁钱花，你怎么办？”

小宝宝：……

笔者：“有了区块链就不会出现这种假设的问题啦。比如过年的时候，爷爷给了你1000块压岁钱，爷爷就在自己的小本本上写：今天给了宝宝1000块压岁钱。然后爷爷大声告诉奶奶、爸爸、妈妈：‘我今天给了宝宝1000块压岁钱。’奶奶、爸爸、妈妈听到之后都在自己的小本本上写：爷爷今天给了宝宝1000块压岁钱。”“然后奶奶给了你2000块压岁钱，奶奶就在自己的小本本上写：今天给了宝宝2000块压岁钱。然后奶奶大声告诉爷爷、爸爸、妈妈：‘我今天给了宝宝2000块压岁钱。’爷爷、爸爸、妈妈听到之后都在自己的小本本上写：奶奶今天给了宝宝2000块压岁钱。”“之后爸爸给了你3000块压岁钱，爸爸就在自己的小本本上写：今天给了宝宝3000块压岁钱。然后爸爸大声告诉爷爷、奶奶、妈妈：‘我今天给了宝宝3000块压岁钱。’爷爷、奶奶、妈妈听到之后都在自己的小本本上写：爸爸今天给了宝宝3000块压岁钱。”“最后妈妈给了你4000块压岁钱，妈妈就在自己的小本本上写：今天给了宝宝4000块压岁钱。然后妈妈大声告诉爷爷、奶奶、爸爸：‘我今天给了宝宝4000块压岁钱。’爷爷、奶奶、爸爸听到之后都在自己的小本本上写：妈妈今天给了宝宝4000块压岁钱。”

“每年过年大家给完宝宝压岁钱之前后都这样写在小本本上，然后告诉其他人也写在自己的小本本上，这就是区块链。”

我们还可以约定宝宝10岁时就可以拿出1000块钱买好吃的，到你10岁的时候呢，1000块钱就会送到你手上。这就是智能合约。”

小宝宝：“区块链这个游戏真好玩，快给我拿纸和笔，我也要写！”

### 1.2正说区块链

2018年年初以来，区块链一词火遍了大街小巷，出租车司机、程序员、培训机构等都在谈论区块链。区块链在网络上也成了热门搜索词汇，在百度指数上搜索区块链可以看到，区块链从2018年1月陡然成为热词，之后持续保持“网红”趋势，搜索指数居高不下，如图1-1所示。

![图1-1 区块链百度搜索指数](assets/区块链java底层实战/图1-1 区块链百度搜索指数.png)

什么是区块链呢？区块链一词最早出现在中本聪（Satoshi Nakamoto）的“比特币：一种点对点的电子现金系统（Bitcoin: A Peer-to-Peer Electronic Cash System）”一文中。

在比特币系统中，区块链作为存储底层，承载了上层众多认同比特币相关协议并严格遵守及维护协议的节点（个人或组织）的记录各类信息的行为。

区块链的存储是一种链式存储，区块按照生成的时间顺序前后链接，区块的链接基于区块存储内容的哈希值构建。区块生成后会在区块链系统的各个节点（个人或组织）中同步，因此各个节点最终均保存了一份完整且一致的数据。也就是说，区块链系统会保持最终一致性，但不保证实时一致性。

比特币系统中理论上不支持数据的删除和修改。数据的删除和修改意味着其修改所在区块的哈希值也需要随之修改，而该区块之后的所有区块的前向链接哈希值均需改变。随着比特币系统中节点（个人或组织）数量的增多，前向哈希值的修改和同步工作会愈发繁重。在比特币全网络中对数据删除和修改变得愈发困难，因此比特币系统中区块数据的不可篡改性就体现了出来。



由于加入比特币网络的节点（个人或组织）严格遵守比特币协议，因此区块链从理论上具备了承载信用的特性。



从技术上来说，区块链技术并非凭空产生，而是基于已有技术演变而来的。



区块链的**链式**构成和我们上学时所学的《数据结构和算法》这类书籍中的单链表有一定的相似性，链表中的各部分内容均通过指针（哈希值也是泛化指针的一种形式）相连。不同之处在于，单链表是从前向后构建关联关系，即新插入一个内容时，需从链表的表头开始移动指针至链表尾部，再将尾部的指针指向新建的内容，新建的内容自动成为链表的尾部。而区块链是从后向前构建关联关系，即新生成的区块通过前面区块的哈希值与前面区块建立连接。



区块中header+body的结构设计与Web开发中常见的HTTP协议有一定的相似性。HTTP协议的request和response均由三部分组成：request/response line、request/response body、request/response header。



区块内容的**加密**就更常见了，无论是对称加密，抑或是非对称加密，在MySQL敏感数据存储、接口参数加密、OAuth授权等无处不在。而区块内容中多条交易信息对应的Merkle树源于《数据结构和算法》中的二叉树。



区块链中P2P网络是区块链系统的基础。共识的达成、数据的同步均依赖于高效的P2P网络系统。P2P网络的构建源于少见的Java网络编程部分。在生活中，QQ、微信、微博等场景均有类似应用。如果读者研究过ElasticSearch、Redis集群、Kafka集群等数据同步的代码，对开发和构建P2P网络应该更有信心。不过随着技术的发展，Java开发小伙伴可以不再基于原始的Socket进行开发，可以选用WebSocket或t-io等组件进行开发。

区块链上共识的达成基于**共识算法**。共识算法的起源算法很多读者都熟知，如Paxos一致性算法，分布式数据库的二、三阶段提交协议、ZooKeeper的快速选举算法等。当然，不同的公链、联盟链的共识算法在不同场景有不同的选择，但算法基础都是一脉相承的。

区块的存储和常见的分布式应用开发略有不同。在分布式应用开发中，我们熟悉的分库分表、分布式缓存集群等并不适用。区块链的**每个节点是一个单机**，存储也随本机进行。因此，区块的存储往往选用高性能的文件系统或高性能的单机版SQL/NoSQL数据库，如SQLite、LevelDB、RocksDB等。当然，单机的存储空间始终是有限的，随着存储承载压力的增加，后续区块链存储的扩容也可能成为区块链领域的另一种创新。

看到这么多相似的技术，是不是已经觉得区块链底层的研发不再高不可攀了呢？来吧，笔者将带你走进区块链底层开发世界，曾经以为的高门槛将不再遥不可及！

### 1.3 区块链的未来:联盟链

2018年伊始，区块链在国内开始火热起来。各个行业各个领域都开始寻找自身业务和区块链的结合，不断有一些尝试性的落地应用上线，其中以金融、游戏领域为主，内容、房产信息、商品溯源等场景也开始引入区块链。



不同场景下，区块链类型的选型也不尽相同。



在区块链世界中，一般划分为公链、联盟链、私链。从技术视角而言，公链、联盟链、私链的底层技术大抵相同，但适用场景则大相径庭。我们可以从不同的视角来看待公链、联盟链、私链。



从去中心化这一区块链系统最大的特色来说，公链是完全去中心化的，公链网络的各个节点都可以读取和写入数据；联盟链则是部分去中心化，节点由联盟内成员部署，读写权限也可以根据联盟内的协议来定制；私链本质上还是中心化的，数据的写入由私链所属组织控制，数据的读取和使用则由私链所属组织的应用场景来定。



从代码开源程度而言，公链的开放程度最大，全世界的个人和组织均可获取其完整代码，个人和组织可以直接部署，亦可修改为己所用。联盟链则是对联盟内部用户开源其最核心代码，代码的写权限可以在联盟内部分级管理。私链的代码归属于个人或组织，一般不对外开放源码。



从激励体系角度而言，激励体系是公链的灵魂，不可或缺；联盟链可以根据场景选择是否使用激励体系；私链则更加灵活。



那么公链、联盟链、私链谁代表了区块链未来的发展方向呢？特别是谁代表了企业级区块链的未来呢？笔者判断是联盟链。



激励体系作为公链的灵魂，其对应的优秀经济模型的设计比较困难。同时，激励体系往往是为了吸引更多的节点进入公链挖矿，挖矿在现有的共识算法体系下是一种耗费计算资源的低效行为，并不经济。而且企业相关的业务信息往往私密性较强，不适合进入公链，即便有部分信息可以在公链落地，数据落地的经济成本也不便宜，毕竟矿工打包数据是需要付费的。



而私链既可以使用公链开源代码，也可以基于联盟链开源代码实现，只是节点数量要少得多。私链的应用往往是在组织内部，并不会对外产生多大影响。



联盟链则不然。各个联盟链组建之初往往都立足于行业，着眼于解决行业共性问题，是能促进行业效率和发展的底层支撑技术。由于企业级应用往往涉及业务逻辑甚至商业机密，因此公链目前并不太适合企业级区块链的应用场景，而联盟链给业务逻辑和商业信息限定了范围，使得区块链技术应用的普适性大大增加。



作为研发，在学会驳接各类公链、联盟链、私链的同时，更应知晓区块链的底层实现技术。本书以联盟链为主线，以区块链底层技术为支撑展开内容，一步步引导读者构建区块链底层平台。

### 1.4 总结

本章主要从戏说和正说两个维度向读者展示了区块链的概况，以期降低读者心里对区块链底层技术的认知高度，客观而轻松地了解区块链的底层实现概览。

同时向读者分享了笔者对区块链未来的判断，供读者思考和研究。

区块链的研究和落地，我们仍然在路上！



## 第2章 区块链架构

会当凌绝顶

一览众山小

正如开篇所言：会当凌绝顶，一览众山小。进入区块链底层开发前，我们需要了解区块链底层的通用架构是如何设计的，从上而下地审视区块链底层的结构，做到了然于胸，才能胸有成竹。

他山之石，可以攻玉。在介绍区块链底层通用架构之前，我们不妨先从比特币、以太坊、Hyperledger的架构解读开始。

### 2.1 比特币架构

根据中本聪的论文“Bitcoin: A Peer-to-Peer Electronic Cash System”中对比特币系统的描述，我们可以整理出如图2-1所示的比特币系统架构。

![图2-1 比特币系统架构](assets\区块链java底层实战\图2-1 比特币系统架构.png)

如图2-1所示，比特币系统分为6层，由下至上依次是存储层、数据层、网络层、共识层、RPC层、应用层。



其中，存储层主要用于存储比特币系统运行中的日志数据及区块链元数据，存储技术主要使用文件系统和LevelDB。



数据层主要用于处理比特币交易中的各类数据，如将数据打包成区块，将区块维护成链式结构，区块中内容的加密与哈希计算，区块内容的数字签名及增加时间戳印记，将交易数据构建成Merkle树，并计算Merkle树根节点的哈希值等。



区块构成的链有可能分叉，在比特币系统中，节点始终都将最长的链条视为正确的链条，并持续在其后增加新的区块。

网络层用于构建比特币底层的P2P网络，支持多节点动态加入和离开，对网络连接进行有效管理，为比特币数据传输和共识达成提供基础网络支持服务。



共识层主要采用了PoW（Proof Of Work）共识算法。在比特币系统中，每个节点都不断地计算一个随机数（Nonce），直到找到符合要求的随机数为止。在一定的时间段内，第一个找到符合条件的随机数将得到打包区块的权利，这构建了一个工作量证明机制。从PoW的角度，是不是发现PoW和分布式锁有异曲同工之妙呢？

RPC层实现了RPC服务，并提供JSON API供客户端访问区块链底层服务。



应用层主要承载各种比特币的应用，如比特币开源代码中提供了bitcoin client。该层主要是作为RPC客户端，通过JSON API与bitcoin底层交互。除此之外，比特币钱包及衍生应用都架设在应用层上。

### 2.2 以太坊架构

根据以太坊白皮书 A Next-Generation Smart Contract and Decentralized ApplicationPlatform的描述，以太坊架构如图2-2所示。

![图2-2 以太坊架构](assets\区块链java底层实战\图2-2 以太坊架构.png)

如图2-2所示，以太坊架构分为7层，由下至上依次是存储层、数据层、网络层、协议层、共识层、合约层、应用层。



其中存储层主要用于存储以太坊系统运行中的日志数据及区块链元数据，存储技术主要使用文件系统和LevelDB。



数据层主要用于处理以太坊交易中的各类数据，如将数据打包成区块，将区块维护成链式结构，区块中内容的加密与哈希计算，区块内容的数字签名及增加时间戳印记，将交易数据构建成Merkle树，并计算Merkle树根节点的hash值等。



与比特币的不同之处在于以太坊引入了交易和交易池的概念。交易指的是一个账户向另一个账户发送被签名的数据包的过程。而交易池则存放通过节点验证的交易，这些交易会放在矿工挖出的新区块里。



以太坊的Event（事件）指的是和以太坊虚拟机提供的日志接口，当事件被调用时，对应的日志信息被保存在日志文件中。



与比特币一样，以太坊的系统也是基于P2P网络的，在网络中每个节点既有客户端角色，又有服务端角色。



协议层是以太坊提供的供系统各模块相互调用的协议支持，主要有HTTP、RPC协议、LES、ETH协议、Whipser协议等。



以太坊基于HTTP Client实现了对HTTP的支持，实现了GET、POST等HTTP方法。外部程序通过JSON RPC调用以太坊的API时需通过RPC（远程过程调用）协议。

Whisper协议用于DApp间通信。

LES的全称是轻量级以太坊子协议（Light Ethereum Sub-protocol），允许以太坊节点同步获取区块时仅下载区块的头部，在需要时再获取区块的其他部分。



共识层在以太坊系统中有PoW（Proof of Work）和PoS（Proof of Stake）两种共识算法。



合约层分为两层，底层是EVM（Ethereum Virtual Machine，即以太坊虚拟机），上层的智能合约运行在EVM中。智能合约是运行在以太坊上的代码的统称，一个智能合约往往包含数据和代码两部分。智能合约系统将约定或合同代码化，由特定事件驱动触发执行。因此，在原理上适用于对安全性、信任性、长期性的约定或合同场景。在以太坊系统中，智能合约的默认编程语言是Solidity，一般学过JavaScript语言的读者很容易上手Solidity。



应用层有DApp（Decentralized Application，分布式应用）、以太坊钱包等多种衍生应用，是目前开发者最活跃的一层。

### 2.3 Hyperledger架构

超级账本（Hyperledger）是Linux基金会于2015年发起的推进区块链数字技术和交易验证的开源项目，该项目的目标是推进区块链及分布式记账系统的跨行业发展与协作。

目前该项目最著名的子项目是Fabric，由IBM主导开发。按官方网站描述，Hyperledger Fabric是分布式记账解决方案的平台，以模块化体系结构为基础，提供高度的弹性、灵活性和可扩展性。它旨在支持不同组件的可插拔实现，并适应整个经济生态系统中存在的复杂性。

Hyperledger Fabric提供了一种独特的弹性和可扩展的体系结构，使其不同于其他区块链解决方案。我们必须在经过充分审查的开源架构之上对区块链企业的未来进行规划。超级账本是企业级应用快速构建的起点。

目前，Hyperledger Fabric经历了两大版本架构的迭代，分别是0.6版和1.0版。其中，0.6版的架构相对简单，Peer节点集众多功能于一身，模块化和可拓展性较差。1.0版对0.6版的Peer节点功能进行了模块化分解。目前最新的1.1版本处于Alpha阶段。

在1.0版中，Peer节点可分为peers节点和orderers节点。peers节点用于维护状态（State）和账本（Ledger）, orderers节点负责对账本中的各条交易达成共识。

目前，Hyperledger Fabric经历了两大版本架构的迭代，分别是0.6版和1.0版。其中，0.6版的架构相对简单，Peer节点集众多功能于一身，模块化和可拓展性较差。1.0版对0.6版的Peer节点功能进行了模块化分解。目前最新的1.1版本处于Alpha阶段。

在1.0版中，Peer节点可分为peers节点和orderers节点。peers节点用于维护状态（State）和账本（Ledger）, orderers节点负责对账本中的各条交易达成共识。

系统中还引入了认证节点（Endorsing Peers），认证节点是一类特殊的peers节点，负责同时执行链码（Chaincode）和交易的认证（Endorsing Transactions）。

Hyperledger Fabric的分层架构设计如图2-3所示。

![图2-3 Hyperledger Fabric的分层架构设计](assets\区块链java底层实战\图2-3 Hyperledger Fabric的分层架构设计.png)

Hyperledger Fabric可以分为7层，分别是存储层、数据层、通道层、网络层、共识层、合约层、应用层。

其中存储层主要对账本和交易状态进行存储。账本状态存储在数据库中，存储的内容是所有交易过程中出现的键值对信息。比如，在交易处理过程中，调用链码执行交易可以改变状态数据。状态存储的数据库可以使用LevelDB或者CouchDB。LevelDB是系统默认的内置的数据库，CouchDB是可选的第三方数据库。区块链的账本则在文件系统中保存。

数据层主要由交易（Transaction）、状态（State）和账本（Ledger）三部分组成。

其中，交易有两种类型：

* 部署交易：以程序作为参数来创建新的交易。部署交易成功执行后，链码就被安装到区块链上。

* 调用交易：在上一步部署好的链码上执行操作。链码执行特定的函数，这个函数可能会修改状态数据，并返回结果。

状态对应了交易数据的变化。在Hyperledger Fabric中，区块链的状态是版本化的，用key/value store（KVS）表示。其中key是名字，value是任意的文本内容，版本号标识这条记录的版本。这些数据内容由链码通过PUT和GET操作来管理。如存储层的描述，状态是持久化存储到数据库的，对状态的更新是被文件系统记录的。

账本提供了所有成功状态数据的改变及不成功的尝试改变的历史。

账本是由Ordering Service构建的一个完全有序的交易块组成的区块哈希链（Hash Chain）。

账本既可以存储在所有的peers节点上，又可以选择存储在几个orderers节点上。

此外，账本允许重做所有交易的历史记录，并且重建状态数据。

通道层指的是通道（Channel），通道是一种Hyperledger Fabric数据隔离机制，用于保证交易信息只有交易参与方可见。每个通道都是一个独立的区块链，因此多个用户可以共用同一个区块链系统，而不用担心信息泄漏问题。

网络层用于给区块链网络中各个通信节点提供P2P网络支持，是保障区块链账本一致性的基础服务之一。

在Hyperledger Fabric中，Node是区块链的通信实体。Node仅仅是一个逻辑上的功能，多个不同类型的Node可以运行在同一个物理服务器中。Node有三种类型，分别是客户端、peers节点和Ordering Service。

其中，客户端用于把用户的交易请求发送到区块链网络中。

peers节点负责维护区块链账本，peers节点可以分为endoring peers和committing peers两种。endoring peers为交易作认证，认证的逻辑包含验证交易的有效性，并对交易进行签名；committing peers接收打包好的区块，并写入区块链中。与Node类似，peers节点也是逻辑概念，endoring peers和committing peers可以同时部署在一台物理机上。

Ordering Service会接收交易信息，并将其排序后打包成区块，然后，写入区块链中，最后将结果返回给committing peers。

共识层基于Kafka、SBTF等共识算法实现。Hyperledger Fabric利用Kafka对交易信息进行排序处理，提供高吞吐、低延时的处理能力，并且在集群内部支持节点故障容错。相比于Kafka,SBFT（简单拜占庭算法）能提供更加可靠的排序算法，包括容忍节点故障以及一定数量的恶意节点。

合约层是Hyperledger Fabric的智能合约层Blockchain, Blockchain默认由Go语言实现。Blockchain运行的程序叫作链码，持有状态和账本数据，并负责执行交易。在HyperledgerFabric中，只有被认可的交易才能被提交。而交易是对链码上的操作的调用，因此链码是核心内容。同时还有一类称之为系统链码的特殊链码，用于管理函数和参数。

应用层是Hyperledger Fabric的各个应用程序。

此外，既然是联盟链，在Hyperledger Fabric中还有一个模块专门用于对联盟内的成员进行管理，即Membership Service Provider（MSP）, MSP用于管理成员认证信息，为客户端和peers节点提供成员授权服务。

### 2.4 区块链通用架构

至此，我们已经了解了比特币、以太坊和Hyperledger的架构设计，三者根据使用场景的不同而有不同的设计，但还是能抽象出一些共同点，我们可以基于这些共同点设计企业级联盟链的底层架构。

本书提供的联盟链底层架构如图2-4所示。

![图2-4 聪盟链底层架构](assets\区块链java底层实战\图2-4 聪盟链底层架构.png)

在图2-4中，我们将区块链底层分为6层，从下至上分别是存储层、数据层、网络层、共识层、激励层和应用层。

存储层主要存储交易日志和交易相关的内容。其中，交易日志基于LogBack实现。交易的内容由内置的SQLite数据库存储，读写SQLite数据库可以基于JPA实现；交易的上链元数据信息由RocksDB或LevelDB存储。

数据层由区块和区块“链”（区块的链式结构）组成。其中，区块中还会涉及交易列表在Merkle树中的存储及根节点哈希值的计算。交易的内容也需要加密处理。由于在联盟链中有多个节点，为有效管理节点数据及保障数据安全，建议为不同节点分配不同的公、私钥，以便加密使用。

网络层主要提供共识达成及数据通信的底层支持。在区块链中，每个节点既是数据的发送方，又是数据的接收方。可以说每个节点既是客户端，又是服务端，因此需要基于长连接来实现。在本书中，我们可以基于WebSocket用原生方式建立长连接，也可以基于长连接第三方工具包实现。

共识层采用PBFT（Practical Byzantine Fault Tolerance）共识算法。不同于公链的挖矿机制，联盟链中更注重各节点信息的统一，因此可以省去挖矿，直奔共识达成的目标。

激励层主要是币（Coin）和Token的颁发和流通。在公链中，激励是公链的灵魂；但在联盟链中不是必需的。本书不对联盟链中币或Token如何建立经济模型和高效使用，甚至是增值进行说明，仅从技术维度实现币或Token的相关逻辑。

应用层主要是联盟链中各个产品的落地。一般联盟链的应用层都是面向行业的，解决行业内的问题。

Java版联盟链的部署架构如图2-5所示。

![图2-5 Java版联盟链的部署架构](assets\区块链java底层实战\图2-5 Java版联盟链的部署架构.png)

联盟链由1个超级节点和若干个普通节点组成，超级节点除具备普通节点的功能外，还具备在联盟中实施成员管理、权限管理、数据监控等工作。因此相较于完全去中心化的公链，联盟链是部分去中心化的，或者说联盟的“链”是去中心化的，但是联盟链的管理是中心化的。

整个开发环境建议基于Spring Boot 2.0实现。基于Spring Boot开发，可以省去大量的xml配置文件的编写，能极大简化工程中在POM文件配置的复杂依赖。Spring Boot还提供了各种starter，可以实现自动化配置，提高开发效率。

### 2.5 小结

本章介绍了比特币、以太坊、Hyperledger的架构设计，并归纳了通用联盟链底层架构设计方案。后续章节将对每一部分的实现进行理论阐述和Java实操介绍。

## 第3章 密码学

九层之台，起于累土

千里之行，始于足下

密码在日常生活中屡见不鲜，购物支付用支付密码、在ATM机取款要用取款密码、手机屏幕解锁要用解锁密码，等等。

密码学一词源自希腊文kryptos及logos，在希腊语中意为隐藏及消息。世界上最早的密码是在公元前405年，古希腊雅典和斯巴达之间的伯罗奔尼撒战争末期。在斯巴达军队准备对雅典发动最后一击之际，战前与斯巴达联盟的波斯帝国突然准备反戈一击。为此斯巴达急需摸清波斯帝国的行动部署。恰巧，斯巴达军队捕获了一名从波斯帝国回雅典送信的雅典信使。从信使身上搜出一条布满杂乱无章的希腊字母的腰带，斯巴达军队统帅莱桑德无意中把腰带缠绕在手中的剑鞘上时，竟然发现腰带上那些杂乱无章的字母组成了一段文字。这便是雅典间谍送回的情报，原来波斯军队准备在斯巴达军队发起最后攻击时，对斯巴达军队进行突袭。斯巴达军队根据这份情报马上改变了作战计划，以迅雷不及掩耳之势击溃了毫无戒备的波斯军队，从而解除了后顾之忧。随后，斯巴达军队回师征伐雅典，最后赢得了战争的胜利。

而在我国古代，藏头诗可谓是密码学的另一种浪漫应用了。

时代车轮滚滚向前，密码学的发展也蒸蒸日上。随着现代信息社会的到来，密码学的作用也愈发重要。特别是很多信息都必须经过加密之后才能在互联网上传送，这都离不开现代密码技术。现代密码技术在信息加密、信息认证、数字签名和密钥管理方面都有很多应用。

区块链技术也离不开密码学，可以说密码学是区块链系统的基石之一。

从本章起，我们将逐步介绍区块链系统的各个核心模块的实现逻辑。

### 3.1 加密与解密

#### 3.1.1 加密与解密简介

加密与解密技术是对信息进行编码和解码的技术。编码的过程即加密的过程，加密模块把可读信息（即明文）处理成代码形式（即密文）。解码的过程即解密的过程，解密模块把代码形式（即密文）转换回可读信息（即明文）。在加密和解密的过程中，密钥是非常关键的角色。

目前，加密技术主要分为对称加密、不对称加密和不可逆加密三类算法。

对称加密算法是应用较早的加密算法，技术十分成熟。在对称加密算法中，加密和解密的密钥相同。对称密钥技术有两种基本类型：分组密码和序列密码。

对称加密算法的特点是算法公开、计算量小、加密速度快、加密效率高。不足之处是，交易双方使用相同的密钥，安全性得不到保证。目前，广泛使用的对称加密算法有DES、3DES、IDEA和AES等，其中，美国国家标准局倡导的AES即将作为新标准取代DES。

不对称加密算法使用两把完全不同但又完全匹配的一对钥匙，称之为公钥和私钥，公钥和私钥成对配合使用。目前，广泛应用的不对称加密算法有RSA算法和美国国家标准局提出的DSA。我们常见的数字签名（Digital Signature）技术就是以不对称加密算法为基础的。虽然不对称加密算法的安全性得到了提高，但相较于对称加密算法，加密速度慢、效率低。

不可逆加密算法的使用和上述两类算法略有不同，加密过程中不需要使用密钥，输入明文后由系统直接经过加密算法处理成密文，加密后的数据是无法被解密的，只有重新输入明文，并再次经过同样不可逆的加密算法处理，得到相同的加密密文并被系统重新识别后，才能真正解密。目前，应用较多的不可逆加密算法有RSA公司发明的MD5算法和由美国国家标准局建议的不可逆加密标准SHS（Secure Hash Standard，安全Hash标准）等。

#### 3.1.2 Java实现

我们既可以基于Java原生实现加密和解密，又可以基于第三方的工具包实现。下面我们首先介绍基于Java原生API的实现方法，接着介绍第三方工具包hutool和Tink的加密解密API使用方法。

##### 1.Cipher类

Java中的Cipher类主要提供加密和解密的功能，该类位于javax.crypto包下，声明为publicclass Cipher extends Object，它构成了Java Cryptographic Extension (JCE)框架的核心。

使用Cipher类时，需构建Cipher对象，再调用Cipher的getInstance方法来实现。需要注意的是，这里可以由用户自定义传参。在Cipher的概念中称之为“转换”，“转换”用于描述生成对象使用的算法，其格式如下：

“算法/模式/填充”或“算法”

例如：Cipher c=Cipher.getInstance("DES/CBC/PKCS5Padding");Cipher类中常用的常量字段主要有：

* public static final int ENCRYPT_MODE：用于将Cipher初始化为加密模式的常量。
* public static final int DECRYPT_MODE：用于将Cipher初始化为解密模式的常量。
* public static final int WRAP_MODE：用于将Cipher初始化为密钥包装模式的常量。
* public static final int UNWRAP_MODE：用于将Cipher初始化为密钥解包模式的常量。
* public static final int PUBLIC_KEY：用于表示要解包的密钥为“公钥”的常量。
* public static final int PRIVATE_KEY：用于表示要解包的密钥为“私钥”的常量。
*  public static final int SECRET_KEY：用于表示要解包的密钥为“秘密密钥”的常量。

Cipher类中提供了构造方法，供用户自定义使用，构造方法如下所示：

```java
protected Cipher(CipherSpi cipherSpi, Provider provider, String transformation)
```

Cipher类中的核心方法总结如下：

1）public static final Cipher getInstance(String transformation)

​	该方法返回实现指定转换的Cipher对象。transformation为转换的名称，例如，DES/CBC/PKCS5Padding。

2）public static final Cipher getInstance(String transformation, String provider)

​	该方法用于返回实现指定转换的Cipher对象。

3）public static final Cipher getInstance(String transformation, Provider provider)

​	该方法同样用于返回实现指定转换的Cipher对象。

4）public final Provider getProvider()

​	该方法用于返回Cipher对象的提供者。

5）public final String getAlgorithm()

​	该方法用于返回此Cipher对象的算法名称。

6）public final int getBlockSize()

​	该方法用于返回块的大小（以字节为单位）。

7）public final int getOutputSize(int inputLen)

 该方法根据给定的输入长度inputLen（以字节为单位），返回保存下一个update或doFinal操作结果所需的输出缓冲区长度（以字节为单位）。

8）public final byte[] getIV()

该方法用于返回新缓冲区中的初始化向量 (IV)。

9）public final AlgorithmParameters getParameters()

该方法返回此Cipher使用的参数。返回的参数可能与初始化此Cipher所使用的参数相同。如果此Cipher需要算法参数，但却未使用任何参数进行初始化，则返回的参数将由默认值和底层Cipher实现所使用的随机参数值组成。

10）public final ExemptionMechanism getExemptionMechanism()

该方法返回此Cipher使用的豁免（exemption）机制对象。



11）public final void init(int opmode, Key key)

该方法用密钥初始化此Cipher。

12）public final void init(int opmode, Key key, SecureRandom random)

该方法用密钥和随机源初始化此Cipher。

13）public final void init(int opmode, Key key, AlgorithmParameterSpec params)

该方法用密钥和一组算法参数初始化此Cipher。

14）public final void init(int opmode, Key key, AlgorithmParameterSpec params,SecureRandom random)

该方法用一个密钥、一组算法参数和一个随机源初始化此Cipher。

15）public final void init(int opmode, Key key, AlgorithmParameters params)

该方法用密钥和一组算法参数初始化此Cipher。

16）public final void init(int opmode, Key key, AlgorithmParameters params,SecureRandom random)

该方法用一个密钥、一组算法参数和一个随机源初始化此Cipher。

17）public final void init(int opmode, Certificate certificate)

该方法用取自给定证书的公钥初始化此Cipher。

18）public final void init(int opmode, Certificate certificate, SecureRandom random)

该方法用取自给定证书的公钥和随机源初始化此Cipher。

19）public final byte[] update(byte[] input)

20）public final byte[] update(byte[] input, int inputOffset, int inputLen)

21）public final int update(byte[] input, int inputOffset, int inputLen, byte[] output)

22）public final int update(byte[] input, int inputOffset, int inputLen, byte[] output, intoutputOffset)

23）public final int update(ByteBuffer input, ByteBuffer output)

上述几种方法用于大量数据需要进行加密或解密的场景，此时需将大量数据分批次进行加密或解密，对每一个批次数据的加密或解密都需要调用update方法。

24）public final byte[] doFinal() throws IllegalBlockSizeException, BadPaddingException

该方法用于大量数据需要进行加密或解密的场景，此时需将大量数据分批次进行加密或解密，doFinal()方法用于完成多批次加密或解密的收尾工作。比如，待加密或解密的大量数据可以分为9份，每批次4份，则第三批次就剩1份，这1份数据由doFinal()方法执行对应的加密或解密操作。

25）public final int doFinal(byte[] output, int outputOffset)

26）public final byte[] doFinal(byte[] input)27）public final byte[] doFinal(byte[] input, int inputOffset, int inputLen)

28）public final int doFinal(byte[] input, int inputOffset, int inputLen, byte[] output)

29）public final int doFinal(byte[] input, int inputOffset, int inputLen, byte[] output, intoutputOffset)
30）public final int doFinal(ByteBuffer input, ByteBuffer output)
上述几种方法用于大量数据和少量数据进行加密或解密的场景。少量数据时，按单一部分操作加密或解密数据；大量数据需要进行加密或解密的场景中，需将大量数据分批次进行加密或解密，doFinal()方法用于完成多批次加密或解密的收尾工作。比如，待加密或解密的大量数据可以分为9份，每批次4份，则第三批次就剩1份，这1份数据由doFinal()方法执行对应的加密或解密操作。
31）public final byte[] wrap(Keykey) throws IllegalBlockSizeException,InvalidKeyException
该方法用于包装密钥。
32）public final Key unwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, intwrappedKeyType)throws InvalidKeyException, NoSuchAlgorithmException该方法用于解包一个以前包装的密钥。
参数列表释义如下：·
* wrappedKey：待解包的密钥。
* wrappedKeyAlgorithm：与此包装密钥关联的算法。
*  wrappedKeyType：已包装密钥的类型。此类型必须为SECRET_KEY、PRIVATE_KEY或PUBLIC_KEY之一。

##### 2．基于Cipher实现加密和解密
在熟悉了Cipher的主要API后，我们基于Cipher编写加密和解密代码如下：

```java
package com.niudong.demo.util;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.testng.util.Strings;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 基于Cipher实现的加密和解密工具类
 *
 * @author牛冬
 *
 */
public class DeEnCoderCipherUtil {
  // 加密、解密模式
  private final static String CIPHER_MODE = "DES";

  // DES密钥
  public static String DEFAULT_DES_KEY =
      "区块链是分布式数据存储、点对点传输、共识机制、加密算法等计算机技术的新型应用模式。";
  /**
    * function加密通用方法
    *
    * @param originalContent：明文
    * @param key加密密钥
    * @return密文
    */
  public static String encrypt(String originalContent, String key) {
  // 明文或加密密钥为空时
  if(Strings.isNullOrEmpty(originalContent) || Strings.isNullOrEmpty
    (key)){
    return null;
  }

  // 明文或加密密钥不为空时
  try {
    byte[] byteContent = encrypt(originalContent.getBytes(),
        key.getBytes());
    return new BASE64Encoder().encode(byteContent);
  } catch (Exception e) {
    e.printStackTrace();
  }
  return null;
}

  /**
    * function解密通用方法
    *
    * @param ciphertext密文
    * @param key DES解密密钥（同加密密钥）
    * @return明文
    */
   public static String decrypt(String ciphertext, String key) {
    // 密文或加密密钥为空时
    if(Strings.isNullOrEmpty(ciphertext)||Strings.isNullOrEmpty(key)){
        return null;
    }

// 密文或加密密钥不为空时
try {
    BASE64Decoder decoder = new BASE64Decoder();
    byte[] bufCiphertext = decoder.decodeBuffer(ciphertext);
    byte[] contentByte = decrypt(bufCiphertext, key.getBytes());
    return new String(contentByte);
  } catch (Exception e) {
    e.printStackTrace();
  }
  return null;
}

/**
  * function字节加密方法
  *
  * @param originalContent：明文
  * @param key加密密钥的byte数组
  * @return密文的byte数组
  */
  private static byte[] encrypt(byte[] originalContent, byte[] key)
      throws Exception {
    // 步骤1：生成可信任的随机数源
    SecureRandom secureRandom = new SecureRandom();

    // 步骤2：基于密钥数据创建DESKeySpec对象
    DESKeySpec desKeySpec = new DESKeySpec(key);

    // 步骤3：创建密钥工厂，将DESKeySpec转换成SecretKey对象来保存对称密钥
    SecretKeyFactory keyFactory=SecretKeyFactory.getInstance
        (CIPHER_MODE);
    SecretKey securekey = keyFactory.generateSecret(desKeySpec);

    // 步骤4:Cipher对象实际完成加密操作，指定其支持指定的加密和解密算法
    Cipher cipher = Cipher.getInstance(CIPHER_MODE);
    // 步骤5：用密钥初始化Cipher对象，ENCRYPT_MODE表示加密模式
    cipher.init(Cipher.ENCRYPT_MODE, securekey, secureRandom);

    // 返回密文
    return cipher.doFinal(originalContent);
  }

  /**
    * function字节解密方法
    *
    * @param ciphertextByte：字节密文
    * @param key解密密钥（同加密秘钥）byte数组
    * @return明文byte数组
    */
  private static byte[] decrypt(byte[] ciphertextByte, byte[] key)
      throws Exception {
    // 步骤1：生成可信任的随机数源
    SecureRandom secureRandom = new SecureRandom();

    // 步骤2：从原始密钥数据创建DESKeySpec对象
    DESKeySpec desKeySpec = new DESKeySpec(key);

    // 步骤3：创建密钥工厂，将DESKeySpec转换成SecretKey对象来保存对称密钥
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance
        (CIPHER_MODE);
    SecretKey securekey = keyFactory.generateSecret(desKeySpec);

    // 步骤4:Cipher对象实际完成解密操作，指定其支持响应的加密和解密算法
    Cipher cipher = Cipher.getInstance(CIPHER_MODE);

    // 步骤5：用密钥初始化Cipher对象，DECRYPT_MODE表示解密模式
    cipher.init(Cipher.DECRYPT_MODE, securekey, secureRandom);

    // 返回明文
    return cipher.doFinal(ciphertextByte);
  }

}
```
我们基于TestNG和Mockito（使用说明详见附录A和附录B）编写DeEnCoderCipherUtil对应的单元测试代码DeEnCoderCipherUtilTest如下：

```java
package com.niudong.demo.util;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * DeEnCoderCipherUtil的单元测试类
 *
 * @author牛冬
 *
  */
 public class DeEnCoderCipherUtilTest {
   private static String ciphertextGlobal;

   @Test
   public void testEncrypt() {
    // case1:originalContent = null; key = null;
    String originalContent = null;
    String key = null;
    Assert.assertEquals(DeEnCoderCipherUtil.encrypt(originalContent,
        key), null);

    // case2:originalContent ! = null; key = null;
    originalContent = "2019届校园招聘开启啦！ ";
    key = null;
    Assert.assertEquals(DeEnCoderCipherUtil.encrypt(originalContent,
        key), null);

    // case3:originalContent = null; key ! = null;
    originalContent = null;
    key = " 2019届校园招聘开启啦！内推简历扔过来呀！";
    Assert.assertEquals(DeEnCoderCipherUtil.encrypt(originalContent,
        key), null);

    // case3:originalContent = null; key ! = null;
    originalContent = " 2019届校园招聘开启啦！ ";
    key = " 2019届校园招聘开启啦！内推简历扔过来呀！";
    ciphertextGlobal = DeEnCoderCipherUtil.encrypt(originalContent, key);
    Assert.assertEquals(ciphertextGlobal,
        " Jd/2DCl5MX6g8EKfqR/kGzy9OUSBxsfoQKMlpR3FCaE= ");
 }
 @Test(dependsOnMethods = {"testEncrypt"})
 public void testDecrypt() {
   // case1:String ciphertext = null, String key =null
   String ciphertext = null, key = null;
   Assert.assertEquals(DeEnCoderCipherUtil.decrypt(ciphertext, key), null);

   // case2:String ciphertext ! = null, String key =null
   ciphertext = ciphertextGlobal;
   Assert.assertEquals(DeEnCoderCipherUtil.decrypt(ciphertext, key),null);

    // case3:String ciphertext = null, String key ! =null
    ciphertext = null;
    key = " 2019届校园招聘开启啦！内推简历扔过来呀！";
    Assert.assertEquals(DeEnCoderCipherUtil.decrypt(ciphertext,
        key), null);

    // case4:String ciphertext ! = null, String key ! =null
    ciphertext = ciphertextGlobal;
    key = " 2019届校园招聘开启啦！内推简历扔过来呀！";
    Assert.assertEquals(DeEnCoderCipherUtil.decrypt(ciphertext,
        key), "2019届校园招聘开启啦！ ");
  }
}
```

##### 3.Hutool简介
在Java世界中，AES、DES的加密解密需要使用Cipher对象构建加密解密系统，有没有更好的封装工具类来简化开发呢？当然有！Hutool就是一个好助手。

Hutool是一个实用的Java工具包，有pom.jar、javadoc.jar和sources.jar等文件，可对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装，组成各种Util工具类。适用于Web开发，与其他框架无耦合，高度可替换。

在加密解密这部分，Hutool针对JDK支持的所有对称加密算法都做了封装，封装为SymmetricCrypto类，AES和DES是此类的简化表示。通过实例化这个类，传入相应的算法枚举即可使用相同方法加密解密字符串或对象。

当前Hutool支持的对称加密算法枚举有：
* AES
* ARCFOUR
* Blowfish
* DES
* DESede
* RC2
* PBEWithMD5AndDES
* PBEWithSHA1AndDESede
* PBEWithSHA1AndRC2_40

这些枚举全部在SymmetricAlgorithm中被列举。对于不对称加密，最常用的就是RSA和DSA，在Hutool中使用AsymmetricCrypto对象来负责加密解密。

##### 4．基于Hutool
实现加密解密使用Hutool之前，在工程中需引入Hutool的依赖配置，配置代码如下所示：

```xml
<dependency>
    <groupId>com.xiaoleilu</groupId>
    <artifactId>hutool-all</artifactId>
    <version>3.0.9</version>
</dependency>
```

基于Hutool工具类的加密解密类DeEnCoderHutoolUtil的代码如下：

```java
package com.niudong.demo.util;

import java.security.PrivateKey;
import java.security.PublicKey;
import org.testng.util.Strings;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;

/**
 * 基于Hutool工具类的加密解密类
 *
 * @author牛冬
 *
 */
public class DeEnCoderHutoolUtil {

  // 构建RSA对象
  private static RSA rsa = new RSA();
  // 获得私钥
  private static PrivateKey privateKey = rsa.getPrivateKey();
  // 获得公钥
  private static PublicKey publicKey = rsa.getPublicKey();

  /**
    * function RSA加密通用方法：对称加密解密
    *
    * @param originalContent：明文
    * @return密文
    */
  public static String rsaEncrypt(String originalContent) {
    // 明文或加密密钥为空时
    if (Strings.isNullOrEmpty(originalContent)) {
      return null;
    }

    // 公钥加密，之后私钥解密
    return rsa.encryptBase64(originalContent, KeyType.PublicKey);
  }

  /**
    * function RSA解密通用方法：对称加密解密
    *
    * @param ciphertext密文
    * @param key RSA解密密钥（同加密密钥）
    * @return明文
    */
  public static String rsaDecrypt(String ciphertext) {
    // 密文或加密密钥为空时
    if (Strings.isNullOrEmpty(ciphertext)) {
      return null;
    }

    return  rsa.decryptStr(ciphertext, KeyType.PrivateKey);
  }
  /**
  * function DES加密通用方法：对称加密解密
  *
  * @param originalContent：明文
  * @param key加密密钥
  * @return密文
  */
public static String desEncrypt(String originalContent, String key) {
  // 明文或加密密钥为空时
  if (Strings.isNullOrEmpty(originalContent) ||
      Strings.isNullOrEmpty(key)) {
    return null;
  }

  // 还可以随机生成密钥
  // byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.
  // getValue()).getEncoded();

  // 构建
  DES des = SecureUtil.des(key.getBytes());

  // 加密
  return des.encryptHex(originalContent);

}
/**
  * function DES解密通用方法：对称加密解密
  *
  * @param ciphertext密文
  * @param key DES解密密钥（同加密秘钥）
  * @return明文
  */
public static String desDecrypt(String ciphertext, String key) {
  // 密文或加密密钥为空时
  if(Strings.isNullOrEmpty(ciphertext)||Strings.isNullOrEmpty(key)){
      return null;
  }

  // 还可以随机生成密钥
  // byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.
  // getValue()).getEncoded();

  // 构建
    DES des = SecureUtil.des(key.getBytes());
    // 解密

    return des.decryptStr(ciphertext);
  }

}
```

同样为了方便测试，我们基于TestNG和Mockito框架编写单元测试代码如下：

```java
package com.niudong.demo.util;

import org.testng.Assert;
import org.testng.annotations.Test;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * 基于Hutool工具的加密解密的单元测试类
 *
 * @author 牛冬
 *
 */
public class DeEnCoderHutoolUtilTest {

  @Test
  public void testDesEncrypt() {
    // case1:String originalContent=null, String key=null
    String originalContent = null, key = null;
    Assert.assertEquals(DeEnCoderHutoolUtil.desEncrypt
        (originalContent, key), null);

    // case2:String originalContent! =null, String key=null
    originalContent = "2019届校园招聘开启啦！ ";
    Assert.assertEquals(DeEnCoderHutoolUtil.desEncrypt
        (originalContent, key), null);

    // case2:String originalContent=null, String key! =null
    originalContent = null;
    key = " 2019届校园招聘开启啦！内推简历扔过来呀！";
    Assert.assertEquals(DeEnCoderHutoolUtil.desEncrypt
      (originalContent, key), null);

  // case4:String originalContent! =null, String key! =null
  originalContent = "2019届校园招聘开启啦！";
  key = new String(SecureUtil.generateKey(SymmetricAlgorithm.DES.
      getValue()).getEncoded());
  Assert.assertNotNull(DeEnCoderHutoolUtil.desEncrypt
      (originalContent, key));
}

@Test
public void testDesDecrypt() {
  // case1:String ciphertext =null, String key = null
  String ciphertext = null, key = null;
  Assert.assertEquals(DeEnCoderHutoolUtil.desDecrypt(ciphertext,
      key), null);

  // case2:String ciphertext ! =null, String key = null
  String originalContent = "2019届校园招聘开启啦！";
  String keyTmp =
        new String(SecureUtil.generateKey(SymmetricAlgorithm.DES.
            getValue()).getEncoded());
  ciphertext = DeEnCoderHutoolUtil.desEncrypt(originalContent,
      keyTmp);
  Assert.assertEquals(DeEnCoderHutoolUtil.desDecrypt(ciphertext,
      key), null);

  // case3:String ciphertext =null, String key ! = null
  ciphertext = null;
  key = new String(SecureUtil.generateKey(SymmetricAlgorithm.DES.
      getValue()).getEncoded());
Assert.assertEquals(DeEnCoderHutoolUtil.desDecrypt(ciphertext, key),
  null);

  // case4:String ciphertext ! =null, String key ! = null
  ciphertext = DeEnCoderHutoolUtil.desEncrypt(originalContent,
      key);
  Assert.assertNotNull(DeEnCoderHutoolUtil.desDecrypt(ciphertext,
      key));
  }
}
```
##### 5.Tink
简介Tink是由谷歌的一群密码学家和安全工程师编写的密码库。GitHub开源地址：https://github.com/google/tink/。

Tink的诞生融合了Google产品团队的丰富工程经验，现已修复了原有实现中的缺陷，并提供了简单的API，用户可以安全使用，无须具备密码学知识背景。

Tink提供了易于正确使用且不易被误用的安全API。Tink通过以用户为中心的设计、严谨的代码实现和代码审查以及广泛的测试，显著减少了工程开发中常见的密码陷阱。在谷歌，Tink已经被用来保护许多产品的数据，如广告、谷歌薪酬、谷歌助理、Firebase、Android搜索应用等。

使用Tink最简单的方法是安装Bazel，然后构建、运行和播放GitHub中预设的hello world示例。

Tink通过原语执行加密任务，每个原语都是通过指定原语功能的相应接口定义的。例如，对称密钥加密是通过AEAD原语（带有关联数据的认证加密）提供的，它支持两种操作：

1）加密（明文，associated _ data），加密给定明文（使用associated _ data作为额外的AEAD输入），并返回结果密文。

2）解密（密文，associated _ data），它解密给定的密文（使用associated _ data作为额外的AEAD输入），并返回结果明文。

目前，Tink的Java语言版、Android语言版、C++语言版和Obj-C语言版都已通过了严格的测试，并已投入生产部署。当前Tink的最新版本是1.2.0，发布于2018年8月9日。此外，Tink的Go语言版本和JavaScript语言版本正在积极开发中。

##### 6.Tink的使用
Tink可以通过Maven或Gradle来实现依赖管理。在Maven中，Tink的group ID是`com.google.crypto.tink`, artifact ID是tink。

Java开发人员基于Maven在工程的POM文件中添加Tink的依赖配置如下：

```xml
<dependency>
  <groupId>com.google.crypto.tink</groupId>
  <artifactId>tink</artifactId>
  <version>1.2.0</version>
</dependency>
```
1）Tink的初始化

Tink提供了可定制的初始化，允许用户选择所需原语的特定实现（由键类型标识）。Tink的初始化是通过注册来实现的，以便Tink“知道”用户期望的实现方式。

例如，要想使用Tink中的所有原语来实现初始化，则初始化的代码逻辑如下所示：
```java
import com.google.crypto.tink.config.TinkConfig;

public void register() {
          try {
                TinkConfig.register();
                } catch (Exception e) {
                        e.printStackTrace();
                }
}
```
如果仅使用AEAD原语实现，则可以执行以下操作：
```java
import com.google.crypto.tink.aead.AeadConfig;

public void register() {
          try {
                    AeadConfig.register();
                } catch (Exception e) {
                        e.printStackTrace();
                }
}
```
当然，Tink还支持用户自定义初始化，即直接通过注册表类进行注册，代码如下所示：
```java
import com.google.crypto.tink.Registry;
import com.niudong.demo.util.MyAeadKeyManager;

public void register(){
    // Register a custom implementation of AEAD.
    Registry.registerKeyManager(new MyAeadKeyManager());
}
```

其中MyAeadKeyManager为自定义的KeyManager。

注册原语实现后，Tink的基本使用分三步进行：

（1）加载或生成加密密钥（Tink术语中的密钥集）。

（2）使用key获取所选原语的实例。

（3）使用原语完成加密任务。

例如，使用Java中的AEAD原语加密解密时的代码示例如下：

```java
 import com.google.crypto.tink.Aead;
    import com.google.crypto.tink.KeysetHandle;
    import com.google.crypto.tink.aead.AeadFactory;
    import com.google.crypto.tink.aead.AeadKeyTemplates;

public void aead(byte[] plaintext, byte[] aad){
try {
    // 1. 配置生成密钥集
    KeysetHandle keysetHandle = KeysetHandle.generateNew(
        AeadKeyTemplates.AES128_GCM);

    // 2. 使用key获取所选原语的实例
    Aead aead = AeadFactory.getPrimitive(keysetHandle);

    // 3. 使用原语完成加密任务
    byte[] ciphertext = aead.encrypt(plaintext, aad);
    } catch (Exception e) {
            e.printStackTrace();
    }
}
```
2）生成新密钥（组）
每个密钥管理器KeyManager的实现都提供了新密钥的生成接口newKey(...)，该接口根据用户设置的密钥类型生成新密钥。然而，为了避免敏感密钥信息的意外泄漏，开发人员在代码中应该小心将密钥（集合）生成与密钥（集合）使用混合。为了支持这些工作之间的分离，Tink包提供了一个名为Tinkey的命令行工具，可用于公共密钥的管理。

如果用户需要在Java代码中直接用新的密钥生成KeysetHandle，则用户可以使用keysteadle工具类。

例如，用户可以生成包含随机生成的AES 128- GCM密钥的密钥集，代码如下所示：

```java
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadKeyTemplates;

public void createKeySet(){
  try {
    KeyTemplate keyTemplate = AeadKeyTemplates.AES128_GCM;
    KeysetHandle keysetHandle = KeysetHandle.generateNew(keyTemplate);
  } catch (Exception e) {
        e.printStackTrace();
  }
}
```
3）存储密钥
生成密钥后，用户可以将其保存到存储系统中，例如写入文件，代码如下：
```java
 import com.google.crypto.tink.CleartextKeysetHandle;
    import com.google.crypto.tink.KeysetHandle;
    import com.google.crypto.tink.aead.AeadKeyTemplates;
    import com.google.crypto.tink.JsonKeysetWriter;
    import java.io.File;

public void save2File(){
    try {
    // 创建AES对应的keysetHandle
    KeysetHandle keysetHandle = KeysetHandle.generateNew(
        AeadKeyTemplates. AES128_GCM);

    // 写入json文件
    String keysetFilename = "my_keyset.json";
    CleartextKeysetHandle.write(keysetHandle,
    JsonKeysetWriter.withFile(
    new File(keysetFilename)));
    } catch (Exception e) {
              e.printStackTrace();
    }
}
```
用户还可以使用Google Cloud KMS key来对key加密，其中Google Cloud KMS key位于gcp-kms:/projects/tink-examples/locations/global/keyRings/foo/cryptoKeys/bar asfollows，保存在文件中的代码示例如下：
```java
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.integration.gcpkms.GcpKmsClient;
import java.io.File;

public void save2FileBaseKMS(){
  try {
    // 创建AES对应的keysetHandle
    KeysetHandle keysetHandle = KeysetHandle.generateNew(
      AeadKeyTemplates.AES128_GCM);

    // 写入json文件
    String keysetFilename = "my_keyset.json";
    // 使用gcp-kms方式对密钥加密
    String masterKeyUri = "gcp-kms:
    //projects/tink-examples/locations/global/keyRings/foo/
    cryptoKeys/bar";
    keysetHandle.write(JsonKeysetWriter.withFile(new
    File(keysetFilename)),
      new GcpKmsClient().getAead(masterKeyUri));
    } catch (Exception e) {
              e.printStackTrace();
    }
}
```
4）加载密钥可以使用KeysetHandle加载加密的密钥集，代码示例如下：

```java
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import java.io.File;
public void loadKeySet(){
  try {
      String keysetFilename = "my_keyset.json";
      // 使用aws-kms方式对密钥加密
      String masterKeyUri = "aws-kms:
      //arn:aws:kms:us-east-1:007084425826:key/84a65985-f868-4bfc-
      83c2-366618acf147";
      KeysetHandle keysetHandle = KeysetHandle.read(
      JsonKeysetReader.withFile(new File(keysetFilename)),
      new AwsKmsClient().getAead(masterKeyUri));
      } catch (Exception e) {
          e.printStackTrace();
    }
}
```
如果加载明文的密钥集，则需要使用CleartextKeysetHandle类：
```java
import com.google.crypto.tink.CleartextKeysetHandle;
    import com.google.crypto.tink.KeysetHandle;
    import java.io.File;

public void loadCleartextKeySet(){
  try {
    String keysetFilename = "my_keyset.json";
    KeysetHandle keysetHandle = CleartextKeysetHandle.read(
        JsonKeysetReader.withFile(new File(keysetFilename)));
  } catch (Exception e) {
            e.printStackTrace();
  }
}
```
5）原语的使用和获取原语在Tink中指的是加密操作，因此它们构成了Tink API的核心。
原语是一个接口，它指定了原语能提供的基本操作。
一个原语可以有多个实现，用户可以通过使用某种类型的键来设定想要的实现。表3-1总结了当前可用或计划中的原语的Java实现。

![表3-1 原语的Java实现映射关系表](assets\区块链java底层实战\表3-1 原语的Java实现映射关系表.png)

6）对称密钥加密获得和使用AEAD（通过认证的加密，以及加密或解密数据）代码示例如下：

```java
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;

public void aeadAES(byte[] plaintext, byte[] aad){
  try {
    // 1. 创建AES对应的keysetHandle
    KeysetHandle keysetHandle = KeysetHandle.generateNew(
AeadKeyTemplates.AES128_GCM);

    // 2. 获取私钥
    Aead aead = AeadFactory.getPrimitive(keysetHandle);

    // 3. 用私钥加密明文
    byte[] ciphertext = aead.encrypt(plaintext, aad);

    // 解密密文
    byte[] decrypted = aead.decrypt(ciphertext, aad);
  } catch (Exception e) {
        e.printStackTrace();
  }
}
```

7）数字签名数字签名的签名或验证,示例代码如下所示：

```java
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.PublicKeySign;
import com.google.crypto.tink.PublicKeyVerify;
import com.google.crypto.tink.signature.PublicKeySignFactory;
import com.google.crypto.tink.signature.PublicKeyVerifyFactory;
import com.google.crypto.tink.signature.SignatureKeyTemplates;

    // 签名
    public  void signatures(byte[] data){
    try{
    // 1. 创建ESCSA对应的KeysetHandle对象
    KeysetHandle privateKeysetHandle = KeysetHandle.generateNew(
        SignatureKeyTemplates.ECDSA_P256);

    // 2. 获取私钥
    PublicKeySign signer = PublicKeySignFactory.getPrimitive(
        privateKeysetHandle);

    // 3. 用私钥签名
    byte[] signature = signer.sign(data);

    // 签名验证

    // 1. 获取公钥对应的KeysetHandle对象
    KeysetHandle publicKeysetHandle =
        privateKeysetHandle.getPublicKeysetHandle();

    // 2. 获取私钥
    PublicKeyVerify verifier = PublicKeyVerifyFactory.getPrimitive(
        publicKeysetHandle);
    // 3. 使用私钥校验签名
    verifier.verify(signature, data);
  } catch (Exception e) {
          e.printStackTrace();
  }
}
```

使用Tink的注意事项如下：

（1）不要使用标有`@Alpha`注释的字段和方法的API。这些API可以以任何方式修改，甚至可以随时删除。它们仅用于测试，不是官方生产发布的。（

2）不要在`com.google.crypto.tink.subtle`上使用API。虽然这些API通常使用起来是安全的，但并不适合公众消费，因为可以随时以任何方式修改甚至删除它们。

### 3.2 哈希

### 3.3 Merkle树

### 3.4 小结

本章主要从理论角度解释了区块链中使用的密码学的几个分支，如加密、哈希、Merkle树，并从Java实战的角度为读者展示了如何用代码实现加密、哈希和Merkle树。

## 第4章 P2P网络构建

叫嚣乎东西

隳突乎南北

第3章讲述了区块链系统中的基石之一密码学在区块链系统中的应用，本章将介绍区块链系统的另一个基石——P2P网络的构建。

在常见的分布式服务中，实现不同节点间的高效通信是一项基础技术。同理，区块链系统作为分布式服务的一种，不同区块链节点间的高效通信同样非常重要。P2P通信网络是区块链系统中数据传输、共识达成一致的基础。本章将从P2P的概念和历史发展讲起，逐步介绍比特币和以太坊中P2P网络的构建，最后重点介绍类似比特币的P2P网络构建方法。

### 4.1 P2P简介

### 4.2 区块链P2P网络实现技术总结

### 4.3 基于WebSocket构建P2P网络

### 4.4 基于t-io构建P2P网络

## 第6章 区块设计

莫愁前路无知己

天下谁人不识君

区块是区块链中核心的数据结构。好比行驶在马路上的汽车，虽然大小、颜色、形态各异，但核心的组成是相近的。与之类似，虽然各个公链、联盟链的区块设计不同，但区块的核心字段都是相似的。本章从比特币、以太坊、超级账本（Hyperledger）的区块设计开始介绍，随后介绍Java版区块链中的区块应如何设计。

### 6.1 比特币的区块设计

### 6.2 以太坊的区块设计

### 6.3 Hyperledger的区块设计

Hyperledger系统的源代码同样是开源的。Hyperledger系统包含的fabric、composer、sawtooth-core、indy-node、burrow和iroha 6个子项目。

Hyperledger系统的区块设计在fabric子项目的block.go和common.proto文件中，由文件中的代码逻辑可知，Hyperledger区块由Header、Body和Metadata三部分组成。

其中，Block的代码结构如下所示：

```java
Block
message Block {
        BlockHeader header = 1;
        BlockData data = 2;
        BlockMetadata metadata = 3;
    }
message BlockHeader {
        uint64 number = 1; // 区块高度
        bytes previous_hash = 2; // 前一区块的哈希值
        bytes data_hash = 3; //交易Merkle树根节点生成的哈希值
    }
message BlockData {
        repeated bytes data = 1;
    }
message BlockMetadata {
        repeated bytes metadata = 1;
    }
```



Block的字段释义如下：Number为区块编号，previous_hash为指向前一个区块的指针，data_hash为当前区块的哈希值。

### 6.4 Java版区块设计

根据前面的介绍，读者应该对目前主流区块链系统的区块设计有了一些了解。

如果对HTTP比较清楚，就会发现区块的结构设计和Request/HTTP Response结构设计类似，如HTTP Request也是由三部分组成的，分别是Request Line、Request Header和RequestBody; HTTP Response也是由三部分组成的，分别是Response Line、Response Header和Response Body。类似的区块一般也由Header和Body组成。

下面我们借鉴这些区块链系统的区块设计方案来设计Java版区块。Java版区块Block由BlockHeader、BlockBody和blockHash组成，其中，BlockHeader指的是区块头，BlockBody指的是区块body, blockHash指的是区块的哈希，由区块所有内容，也就是BlockHeader和BlockBody根据SHA256计算得到。

其中，Block的代码设计如下：

```java
package com.niudong.demo.blockchain.block;
import cn.hutool.crypto.digest.DigestUtil;

/**
 * 区块
 *
 * @author niudong.
 */
public class Block {
  /**
  * 区块头
  */
  private BlockHeader blockHeader;
  /**
  * 区块body
  */
  private BlockBody blockBody;
  /**
  * 该区块的哈希
  */
  private String blockHash;

  /**
  * 根据该区块所有属性计算SHA256
  *
  * @return sha256hex
  */
  private String getBlockHash() {
    return DigestUtil.sha256Hex(blockHeader.toString() +
        blockBody.toString());
  }

  public BlockHeader getBlockHeader() {
    return blockHeader;
  }

  public void setBlockHeader(BlockHeader blockHeader) {
    this.blockHeader = blockHeader;
  }

  public BlockBody getBlockBody() {
    return blockBody;
  }

  public void setBlockBody(BlockBody blockBody) {
    this.blockBody = blockBody;
  }

/**
   * 根据该区块所有属性计算SHA256
   *
   * @return sha256hex
   */

  public String getBlockHash() {
      return DigestUtil.sha256Hex(blockHeader.toString() +
              blockBody.toString());

  }
  public void setBlockHash(String blockHash) {
    this. blockHash = blockHash;
  }

}
```

BlockBody主要用于存取交易信息列表，代码设计如下：

```java
package com.niudong.demo.blockchain.block;

import java.util.List;

/**
 * 区块body，里面存放交易的数组
 *
 * @author niudong.
 */
public class BlockBody {
  private List<ContentInfo> contentInfos;

  public List<ContentInfo> getContentInfos () {
    return contentInfos;
  }
  public void setContentInfos (List< ContentInfo > contentInfos) {
    this. contentInfos = contentInfos;
  }
}
```

交易信息ContentInfo包含时间戳字段、交易签名信息、交易信息的哈希值、交易发起方的公钥（私钥加密，公钥解密）和交易信息内容的JSON化字符串。这里交易信息内容采用String字符串，可以承载各个交易实体的JSON化数据。JSON化数据使用起来不仅方便，而且通用性更强。

区块中承载的交易信息ContentInfo代码设计如下：

```java
package com.niudong.demo.blockchain.block;

/**
 * 区块body内的一条内容实体
 *
 * @author niudong.
 */
public class ContentInfo {
  /**
  * 新的JSON内容
  */
  private String jsonContent;
  /**
  * 时间戳
  */
  private Long timeStamp;
  /**
  * 公钥
  */
  private String publicKey;
  /**
  * 签名
  */
  private String sign;
  /**
  * 该操作的哈希
  */
  private String hash;
  public String getJson() {
    return jsonContent;
  }

  public void setJsonContent(String jsonContent) {
    this. jsonContent = jsonContent;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public Long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(Long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }
}
```

BlockHeader包含当前区块运行的版本、上一区块的哈希值、Merkle树根节点哈希值、区块的序号、时间戳、32位随机数Nonce、交易信息的哈希集合等。

BlockHeader的设计如下：

```java
package com.niudong.demo.blockchain.block;

import java.util.List;

/**
 * 区块头
 *
 * @author niudong.
 */
public class BlockHeader {
  /**
  * 版本号
  */
  private int version;
  /**
  * 上一区块的哈希
  */
  private String hashPreviousBlock;
  /**
  * Merkle树根节点哈希值
  */
  private String hashMerkleRoot;
  /**
  * 生成该区块的公钥
  */
  private String publicKey;
  /**
  * 区块的序号
  */
  private int number;
  /**
  * 时间戳
  */
  private long timeStamp;
  /**
  * 32位随机数
  */
  private long nonce;
  /**
   * 该区块里每条交易信息的哈希集合，按顺序来的，通过该哈希集合能算出根节点哈希值
   */
  private List<String> hashList;

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public String getHashPreviousBlock() {
    return hashPreviousBlock;
  }

  public void setHashPreviousBlock(String hashPreviousBlock) {
    this.hashPreviousBlock = hashPreviousBlock;
  }

  public String getHashMerkleRoot() {
    return hashMerkleRoot;
  }

  public void setHashMerkleRoot(String hashMerkleRoot) {
    this.hashMerkleRoot = hashMerkleRoot;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public int getNumber() {
    return number;
  }
  public void setNumber(int number) {
    this.number = number;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public long getNonce() {
    return nonce;
  }

  public void setNonce(long nonce) {
    this.nonce = nonce;
  }

  public List<String> getHashList() {
    return hashList;
  }

  public void setHashList(List<String> hashList) {
    this.hashList = hashList;
  }
}
```

### 6.5 小结

本章主要介绍了区块链中的核心数据结构——区块。分别从比特币、以太坊、超级账本的维度，先后介绍了不同区块链系统中的区块设计，随后总结了三类区块链系统区块设计的共同点，并引出了Java版区块链中区块设计的方法。

## 第7章 区块存储

海纳百川

有容乃大

上一章介绍了区块设计，区块借助区块链中的P2P网络，在区块链系统的各个节点中穿梭，最终在各个节点落地持久化。与分布式服务研发过程中常见的MySQL集群、数据库分库分表、NoSQL型数据库集群的使用习惯不同，区块链的存储系统设计强调本地高效存储。目前市场上主流的区块链系统有比特币系统、Ripple币系统、以太坊系统和超级账本，这些区块链中所使用的存储技术各不相同。

### 7.1 区块存储技术

比特币、Ripple币、以太坊和超级账本的存储设计各不相同，各区块链系统所使用的存储方案分别介绍如下。

比特币存储是系统由普通文件和LeveldB数据库组成的。普通文件用于存储区块数据，LevelDB数据库用于存储区块元数据。区块数据每个文件的大小是128MB。每个区块的数据（区块头和区块里的所有交易）都会序列成字节码的形式写入dat文件中。

比特币存储系统的缺点主要在于区块数据文件大小受限制，当超过128MB时要分割文件，在文件中检索数据相对慢一些。

Ripple币存储系统是由SQLite关系型数据库和RocksDB数据库组成的，其中，关系型数据库用来存储区块头信息和每笔交易的具体信息，RocksDB数据库主要存储区块头、交易和状态表序列化后的数据。

Ripple币存储系统的缺点在于，在RocksDB存储的value数据字段多而复杂。

以太坊存储系统（区块）主要由区块头和交易组成。区块在存储过程中分别将区块头和交易体经过RLP编码后存入LevelDB数据当中。以太坊在数据存储的过程中，每个value对应的key都有相对应的前缀，不同类型的value对应不同的前缀。同时，以太坊系统允许日志跟踪各种交易和信息。

以太坊存储系统的缺点是使用了文件系统，日志文件大小的阈值默认为1MB，文件个数较多，文件管理较复杂。

超级账本的存储系统和比特币一样，也是由普通文件和KV数据库（LevelDB/CouchDB）组成。在超级账本中，每个channel对应一个账本目录，账本目录是由blockfile_000000、blockfile_000001命名格式的文件名组成的。区块数据每个文件的大小是64MB。每个区块的数据（区块头和区块里的所有交易）都会序列成字节码的形式写入blockfile文件中。

超级账本存储系统的缺点在于区块数据文件大小受限制，当超过64MB时要分割文件，在文件中检索数据相对慢一些。

至此，比特币系统、Ripple币系统、以太坊系统和Hyperledger Fabric的存储设计简要介绍完毕。下面将分别介绍文件存储、SQLite存储、LevelDB存储、RocksDB存储和CouchDB存储的Java代码实现。

### 7.2 用Java实现文件存储

文件存储数据是Java开发中常用的方式之一，我们可以借助Guava来简化开发。

Guava是Google开源的Java库，其中包含了Google内部很多项目使用的核心库。Guava是为了方便编码，并减少编码错误而设立的，它提供了用于集合、缓存、并发、字符串处理、I/O流处理的多种API接口，同时支持原语，并提供了注解的使用方式。

由于本章介绍的是存储相关的内容，因此仅仅介绍涉及文件操作相关的API