### jmeter
### 简介
### 下载&安装
### 设置
 #### 中文
 ##### 临时设置
  Options->Choose Language->Chinese(Simplified)
 ##### 永久设置
  bin/jmeter.properties,37行,默认language=en,修改zh_CN 
### 使用
  #### 前置控制器
  #### 获取上一请求的参数作为下一请求参数的参数
   ##### 正则提取器
    http://www.cnblogs.com/0201zcr/p/5089620.html
   * 下载插件:https://jmeter-plugins.org/wiki/JSONPathExtractor/
   * 下载后解压以后将lib和lib/ext中的jar包放到安装目录对应位置，重启即可
   * 添加后置处理器->JSON Extractor
   * 参数 
     Variable names : 名称 
     JSONPath Expression：JSON表达式 
     Match Numbers：匹配哪个，可为空即默认第一个 
     Default Value：未取到值的时候默认值
   * 参数传递
    三种方式:csv、cookie、正则/JSON提取上一次请求输入的参数
    ${variableNames}  
   ##### JSON Extractor
    http://blog.csdn.net/lluozh2015/article/details/54097449