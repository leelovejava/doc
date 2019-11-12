## ElasticSearch 重写IK分词器源码设置mysql热词更新词库

## 常用热词词库的配置方式

1.采用IK 内置词库
优点：部署方便，不用额外指定其他词库位置
缺点：分词单一化，不能指定想分词的词条

2.IK 外置静态词库
优点：部署相对方便，可以通过编辑指定文件分词文件得到想要的词条
缺点：需要指定外部静态文件，每次需要手动编辑整个分词文件，然后放到指定的文件目录下，重启ES后才能生效

3.IK 远程词库
优点：通过指定一个静态文件代理服务器来设置IK分词的词库信息
缺点：需要手动编辑整个分词文件来进行词条的添加， IK源码中判断头信息Last-Modified  ETag 标识来判断是否更新，有时不生效

结合上面的优缺点，决定采用Mysql作为外置热词词库，定时更新热词 和 停用词。





## 准备工作

1.下载合适的ElasticSearch对应版本的IK分词器：https://github.com/medcl/elasticsearch-analysis-ik
2.我们来查看它config文件夹下的文件：
![img](https://oscimg.oschina.net/oscnet/a6bc2e8437f4314a42641746e0bc291ca29.jpg)因为我本地安装的是ES是5.5.0版本，所以下载的IK为5.5.0的适配版
3.分析IKAnalyzer.cfg.xml 配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<comment>IK Analyzer 扩展配置</comment>
	<!--用户可以在这里配置自己的扩展字典 -->
	<entry key="ext_dict">custom/mydict.dic;custom/single_word_low_freq.dic</entry>
	 <!--用户可以在这里配置自己的扩展停止词字典-->
	<entry key="ext_stopwords">custom/ext_stopword.dic</entry>
	<!--用户可以在这里配置远程扩展字典 -->
	<!-- <entry key="remote_ext_dict">words_location</entry> -->
	<!--用户可以在这里配置远程扩展停止词字典-->
	<!-- <entry key="remote_ext_stopwords">words_location</entry> -->
</properties>
```

ext_dict：对应的扩展热词词典的位置，多个热词文件之间使用分号来进行间隔
ext_stopwords:对应扩展停用词词典位置，多个之间用分号进行间隔
remote_ext_dict：远程扩展热词位置 如：https://xxx.xxx.xxx.xxx/ext_hot.txt
remote_ext_stopwords：远程扩展停用词位置 如：https://xxx.xxx.xxx.xxx/ext_stop.txt

4.除了config/ 文件夹中IKAnalyzer.cfg.xml 文件，我们开下config文件夹下其他文件的作用：
Dictionary中单例方法public static synchronized Dictionary initial(Configuration cfg)
 

```java
private DictSegment _MainDict;

private DictSegment _SurnameDict;

private DictSegment _QuantifierDict;

private DictSegment _SuffixDict;

private DictSegment _PrepDict;

private DictSegment _StopWords;
...
public static synchronized Dictionary initial(Configuration cfg) {
	if (singleton == null) {
		synchronized (Dictionary.class) {
			if (singleton == null) {
				singleton = new Dictionary(cfg);
				singleton.loadMainDict();
				singleton.loadSurnameDict();
				singleton.loadQuantifierDict();
				singleton.loadSuffixDict();
				singleton.loadPrepDict();
				singleton.loadStopWordDict();
				if(cfg.isEnableRemoteDict()){
					// 建立监控线程
					for (String location : singleton.getRemoteExtDictionarys()) {
						// 10 秒是初始延迟可以修改的 60是间隔时间 单位秒
						pool.scheduleAtFixedRate(new Monitor(location), 10, 60, TimeUnit.SECONDS);
					}
					for (String location : singleton.getRemoteExtStopWordDictionarys()) {
						pool.scheduleAtFixedRate(new Monitor(location), 10, 60, TimeUnit.SECONDS);
					}
				}
				
				return singleton;
			}
		}
	}
	return singleton;
}
```

initial中 load*中方法是利用config中其他文本文件来初始化Dictionary中的上面声明的成员变量：
_MainDict ： 主词典对象，也是用来存储热词的对象
_SurnameDict ： 姓氏词典
_QuantifierDict ： 量词词典，例如1个中的 个 2两种的两
_SuffixDict ： 后缀词典
_PrepDict ： 副词/介词词典
_StopWords ： 停用词词典





## 修改Dictionary源码

Dictionary类：更新词典 this.loadMySQLExtDict()

```java
private void loadMySQLExtDict() {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
		Path file = PathUtils.get(getDictRoot(), "jdbc-loadext.properties");
		prop.load(new FileInputStream(file.toFile()));

		logger.info("jdbc-reload.properties");
		for(Object key : prop.keySet()) {
			logger.info(key + "=" + prop.getProperty(String.valueOf(key)));
		}

		logger.info("query hot dict from mysql, " + prop.getProperty("jdbc.reload.sql") + "......");

		conn = DriverManager.getConnection(
				prop.getProperty("jdbc.url"),
				prop.getProperty("jdbc.user"),
				prop.getProperty("jdbc.password"));
		stmt = conn.createStatement();
		rs = stmt.executeQuery(prop.getProperty("jdbc.reload.sql"));

		while(rs.next()) {
			String theWord = rs.getString("word");
			logger.info("hot word from mysql: " + theWord);
			_MainDict.fillSegment(theWord.trim().toCharArray());
		}

	} catch (Exception e) {
		logger.error("erorr", e);
	} finally {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("error", e);
			}
		}
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("error", e);
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("error", e);
			}
		}
	}
}
```

Dictionary类：更新停用词 this.loadMySQLStopwordDict()

```java
private void loadMySQLStopwordDict() {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;

	try {
		Path file = PathUtils.get(getDictRoot(), "jdbc-loadext.properties");
		prop.load(new FileInputStream(file.toFile()));

		logger.info("jdbc-reload.properties");
		for(Object key : prop.keySet()) {
			logger.info(key + "=" + prop.getProperty(String.valueOf(key)));
		}

		logger.info("query hot stopword dict from mysql, " + prop.getProperty("jdbc.reload.stopword.sql") + "......");

		conn = DriverManager.getConnection(
				prop.getProperty("jdbc.url"),
				prop.getProperty("jdbc.user"),
				prop.getProperty("jdbc.password"));
		stmt = conn.createStatement();
		rs = stmt.executeQuery(prop.getProperty("jdbc.reload.stopword.sql"));

		while(rs.next()) {
			String theWord = rs.getString("word");
			logger.info("hot stopword from mysql: " + theWord);
			_StopWords.fillSegment(theWord.trim().toCharArray());
		}

	} catch (Exception e) {
		logger.error("erorr", e);
	} finally {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("error", e);
			}
		}
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("error", e);
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("error", e);
			}
		}
	}
}
```

对外暴露方法：

```java
public void reLoadSQLDict() {
	this.loadMySQLExtDict();
	this.loadMySQLStopwordDict();
}
```

MySQLDictReloadThread Runnable实现类，去执行reLoadSQLDict() 加载热词：

```java
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;


/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: 定时执行
 * @time: 2018年07月22日 13:05:24
 * @modifytime:
 */
public class MySQLDictReloadThread implements Runnable {

    private static final Logger logger = ESLoggerFactory.getLogger(MySQLDictReloadThread.class.getName());

    @Override
    public void run() {
        logger.info("reloading hot_word and stop_worddict from mysql");
        Dictionary.getSingleton().reLoadSQLDict();
    }
}
```

最后代码为定时调用：
![img](https://oscimg.oschina.net/oscnet/52da9ca9c7ad254bb9e2bcc7e0734d1db2d.jpg)

其中一些细节就不讲述了。

jdbc-loadext.properties

```properties
jdbc.url=jdbc:mysql://xxx.xxx.xxx.xxx:3306/stop_word?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8
jdbc.user=xxxxxx
jdbc.password=xxxxxxx
jdbc.reload.sql=select word from hot_words
jdbc.reload.stopword.sql=select stopword as word from hot_stopwords
```

![img](https://oscimg.oschina.net/oscnet/31612a526439c8abba90563b07ce027300e.jpg)

文件存放于此位置





## 打包

因为我们链接的是mysql数据库，所以maven项目要引入mysql驱动：

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>6.0.6</version>
</dependency>
```

仅仅这样还不够，还需要修改plugin.xml文件（遇到了这个坑，修改pom好久新引入的依赖打包总打不进去）：

![img](https://oscimg.oschina.net/oscnet/6a38f72a0e7b8b9eddc72b8e39f1ef6e907.jpg)

准备完毕：执行打包。 mvn clean package

![img](https://oscimg.oschina.net/oscnet/d5f94d2805234590a1ae432c4f5af8d619f.jpg)

打包完毕。 上传，重启进行实验啦。^_^



## 实验结果

数据库插入记录

![img](https://oscimg.oschina.net/oscnet/eb8d9480f5d788a398e09ea12d1e96e5d30.jpg)

GET http://127.0.0.1:9200/g_index/_analyze?text=真是山炮&analyzer=ik_smart

```json
{
    "tokens": [
        {
            "token": "真是山炮",
            "start_offset": 0,
            "end_offset": 4,
            "type": "CN_WORD",
            "position": 0
        }
    ]
}
```

GET http://172.16.11.119:9200/g_index/_analyze?text=大耳朵兔子&analyzer=ik_smart

```json
{
    "tokens": [
        {
            "token": "大耳朵兔子",
            "start_offset": 0,
            "end_offset": 5,
            "type": "CN_WORD",
            "position": 0
        }
    ]
}
```

GET http://172.16.11.119:9200/g_index/_analyze?text=大耳朵兔子你真是山炮&analyzer=ik_smart

```json
{
    "tokens": [
        {
            "token": "大耳朵兔子",
            "start_offset": 0,
            "end_offset": 5,
            "type": "CN_WORD",
            "position": 0
        },
        {
            "token": "你",
            "start_offset": 5,
            "end_offset": 6,
            "type": "CN_CHAR",
            "position": 1
        },
        {
            "token": "真是山炮",
            "start_offset": 6,
            "end_offset": 10,
            "type": "CN_WORD",
            "position": 2
        }
    ]
}
```

GET http://172.16.11.119:9200/g_index/_analyze?text=大耳朵兔子你真是山炮&analyzer=ik_max_word

```json
{
    "tokens": [
        {
            "token": "大耳朵兔子",
            "start_offset": 0,
            "end_offset": 5,
            "type": "CN_WORD",
            "position": 0
        },
        {
            "token": "耳朵",
            "start_offset": 1,
            "end_offset": 3,
            "type": "CN_WORD",
            "position": 1
        },
        {
            "token": "耳",
            "start_offset": 1,
            "end_offset": 2,
            "type": "CN_WORD",
            "position": 2
        },
        {
            "token": "朵",
            "start_offset": 2,
            "end_offset": 3,
            "type": "CN_WORD",
            "position": 3
        },
        {
            "token": "兔子",
            "start_offset": 3,
            "end_offset": 5,
            "type": "CN_WORD",
            "position": 4
        },
        {
            "token": "兔",
            "start_offset": 3,
            "end_offset": 4,
            "type": "CN_WORD",
            "position": 5
        },
        {
            "token": "子",
            "start_offset": 4,
            "end_offset": 5,
            "type": "CN_CHAR",
            "position": 6
        },
        {
            "token": "你",
            "start_offset": 5,
            "end_offset": 6,
            "type": "CN_CHAR",
            "position": 7
        },
        {
            "token": "真是山炮",
            "start_offset": 6,
            "end_offset": 10,
            "type": "CN_WORD",
            "position": 8
        },
        {
            "token": "真是",
            "start_offset": 6,
            "end_offset": 8,
            "type": "CN_WORD",
            "position": 9
        },
        {
            "token": "山炮",
            "start_offset": 8,
            "end_offset": 10,
            "type": "CN_WORD",
            "position": 10
        },
        {
            "token": "炮",
            "start_offset": 9,
            "end_offset": 10,
            "type": "CN_WORD",
            "position": 11
        }
    ]
}
```

