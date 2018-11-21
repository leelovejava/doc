https://blog.csdn.net/Ahri_J/article/details/79609444

### elasticsearch

>> wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.3.1.tar.gz

Caused by: java.lang.RuntimeException: can not run elasticsearch as root
https://blog.csdn.net/napoay/article/details/53237471
bin/elasticsearch -Des.insecure.allow.root=true

Elasticsearch默认只允许本机访问，通过远程无法访问？
解决方案：修改 Elastic 安装目录的config/elasticsearch.yml文件，去掉network.host的注释，将它的值改成0.0.0.0，然后重新启动 Elastic。
network.host: 0.0.0.0
上面代码中，设成0.0.0.0让任何人都可以访问。线上服务不要这样设置，要设成具体的 IP。
http://www.ruanyifeng.com/blog/2017/08/elasticsearch.html

ERROR: [3] bootstrap checks failed
[1]: max file descriptors [4096] for elasticsearch process is too low, increase to at least [65536]
解决办法:https://www.cnblogs.com/yidiandhappy/p/7714481.html
elasticsearch用户拥有的可创建文件描述的权限太低，至少需要65536
[2]: max number of threads [3802] for user [admin] is too low, increase to at least [4096]
[3]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
解决办法:https://blog.csdn.net/jiankunking/article/details/65448030

常用错误及解决办法
https://blog.csdn.net/weini1111/article/details/60468068


### logstsh
wget https://artifacts.elastic.co/downloads/logstash/logstash-6.3.1.tar.gz
#### filebeat
wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-6.3.2-linux-x86_64.tar.gz
### kibana
wget https://artifacts.elastic.co/downloads/kibana/kibana-6.3.1-linux-x86_64.tar.gz

127.0.0.1:5601
192.168.6.185:5601

新建用户
adduser admin
* 设置密码
passwd admin
13657442242tiaN
1.更改所有者:
chown -R 用户 目录
2.更改权限:
chmod -R 755 目录
切换用户
su 用户名
su admin

### elasticsearch
 * 安装ik分词器
  https://github.com/medcl/elasticsearch-analysis-ik/releases
  https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.3.2/elasticsearch-analysis-ik-6.3.2.zip
### kibana 使用
 #### 文档
   * 用户手册:https://www.elastic.co/guide/en/kibana/current/upgrade-standard.html
   * 配置文件:https://www.elastic.co/guide/cn/kibana/current/settings.html
 #### 加载实例数据
 https://www.elastic.co/guide/cn/kibana/current/tutorial-load-dataset.html
  1. 威廉·莎士比亚全集，解析成合适的字段。点击这里下载这个数据集： shakespeare.json.
  2. 一组虚构的账户与随机生成的数据。点击这里下载这个数据集： accounts.zip.
  3. 一组随机生成的日志文件。点击这里下载这个数据集： logs.jsonl.gz.
 
 #### 定义索引模式
  https://blog.csdn.net/ming_311/article/details/50619859
  https://www.elastic.co/guide/cn/kibana/current/tutorial-define-index.html
  Management->Kibana->Index Patterns->Configure settings
 #### Discover(数据探索,搜索和过滤数据)
  搜索框中输入elasticsearcg查询语句
    account_number:<100 AND balance:>47500
 #### Visualize(可视化控件种类)可视化数据
  Visualize->Create a visualization->定义每个区间桶->Split Slices桶类别
  * Vertical Bar
  * Pie
  * Coordinate Maps 坐标地图
    https://www.elastic.co/guide/en/kibana/current/tilemap.html#tilemap-configuration
  
  ##### Configuration 配置
   ##### Metrics 指标
   * count:该聚合返回数字字段的平均值 
   * 该聚合返回数字字段的平均值 
   * Sum:求和
   * Min:总和 聚合返回数字字段的总和
   * Max:最大值 聚合返回数字字段的最大值大
   * Unique Count:基数 聚合返回字段中唯一值的数量
   * Standard Deviation:扩展统计 聚合返回数字字段中数据的标准偏差
   * Percentiles:百分数 聚合将数字字段中的值分成您指定的百分数区间
   * Percentile Rank:百分位等级 聚合返回指定的数值字段中的值的百分位等级
   #### Buckets 聚合    
    * Coordinate maps use the geohash aggregation. Select a field, typically coordinates, from the drop-down.
    坐标图使用geohash聚合。从下拉列表中选择一个字段，通常是坐标
    此处有问题,地图未能正常显示,待解决
    https://www.cnblogs.com/ahaii/p/7410421.html
    
    Kibana使用高德地图
    https://www.jianshu.com/p/07b82092d4af
   ##### Exclude Pattern 排除模式
   ##### Include Pattern 包含模式
   ##### JSON Input      JSON输入
  #### Dashboard 使用仪表盘汇总数据
  #### Management(配置 Kibana 和管理已保存的对象)
  ### 插件安装
   #### 插件兼容性:
   Kibana 插件接口在不断的发展变化。由于插件更新很快，因此很难向后兼容。Kibana 强制要求安装的插件版本必须和 Kibana 版本一致。插件开发者必须为每个新的 Kibana 版本发布新的插件版本。
   #### 插件安装
   bin/kibana-plugin install <package name or URL>
   ##### 1.x-pack
   * bin/kibana-plugin install x-pack
   * https://artifacts.elastic.co/downloads/packs/x-pack/x-pack-6.2.4.zip
   * 说明:以下安装说明仅适用于6.2及更早版本。 在6.3及更高版本中，X-Pack包含在Elastic Stack的默认发行版中，默认情况下启用所有免费功能。 可以使用选择加入试用版来启用订阅功能。
   * 离线安装:
     https://www.elastic.co/guide/en/x-pack/6.2/installing-xpack.html#xpack-installing-offline
    * 其他插件结合使用:
     https://www.elastic.co/guide/en/x-pack/current/watcher-getting-started.html 
   ##### 2.Alerting
   * 描述:Get notifications about changes in your data.
      可视化监控报警
   ##### 3.sentinl邮件告警
   * KAAE改名叫sentinl
   * 可视化监控报警插件 KAAE：Kibi + Kibana Alert & Report App for Elasticsearch
      https://blog.csdn.net/whg18526080015/article/details/73812400
   * 下载地址:
      https://github.com/sirensolutions/sentinl/releases
    * window安装
       kibana-plugin.bat install https://github.com/sirensolutions/sentinl/releases/download/tag-6.2.2/sentinl-v6.2.2.zip  
      linux安装
       /opt/kibana/bin/kibana-plugin install https://github.com/sirensolutions/sentinl/releases/download/tag-6.2.2/sentinl-v6.2.2.zip
      离线安装
       bin/kibana-plugin install file:///some/local/path/x-pack.zip -d path/to/directory
       kibana-plugin.bat install file:D:/setup/elk/sentinl-v6.3.2.zip -d D:/setup/elk/kibana-6.3.2-windows-x86_64/plugins/
       kibana-plugin.bat install file:D:/setup/elk/sentinl-tag-6.2.4-pre-0.zip -d D:/setup/elk/kibana-6.3.2-windows-x86_64/plugins/
       
       错误:Plugin installation was unsuccessful due to error "Incorrect Kibana version in plugin [sentinl]. Expected [6.3.2]; found [6.2.2]"
       版本号和kibana版本号不一致
       解决办法:自己构建，修改版本号
       npm view <packagename> versions --json
       npm view kibana versions --json
       
       错误二:
       uiExports.app.injectVars has been removed. Use server() instead
       修改sentinl源码时语法修改,参考其他插件最新的语法
        ~~~ index.js
              uiExports: {
                app: {
                  title: 'Sentinl',
                  description: 'Kibana Alert App for Elasticsearch',
                  main: 'plugins/sentinl/app',
                  icon: 'plugins/sentinl/style/sentinl.svg'
                }
              },
        ~~~
       修改package-lock.json中"version":"6.3.2",升级kibana和elasticsearch的npm插件的版本 
    * 卸载
      /opt/kibana/bin/kibana-plugin remove sentinl
    * 已知插件
      https://www.elastic.co/guide/en/kibana/master/known-plugins.html
    * 已知插件
      https://www.cnblogs.com/kellyJAVA/p/8953928.html
    * ELK日志监控平台告警升级(邮件+钉钉)
      https://blog.52itstyle.com/archives/3137/    
#### start
>> bin/elasticsearch

cmd执行filebeat.exe

>> logstash.bat -f ../first.conf --config.reload.automatic

>> bin/kibana 

127.0.0.1:5601

# gradle
https://gradle.org/releases/
https://gradle.org/next-steps/?version=4.9&format=bin
 