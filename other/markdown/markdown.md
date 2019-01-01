## [Markdown 语法整理大集合2017](https://www.jianshu.com/p/b03a8d7b1719)

# java开发规范
## 二级标题
### 三级标题
#### 四级标题
##### 五级标题

这是一级标题
==========
这是二级标题
----------

# 一级标题 #
## 二级标题 ##

###无序列表
* 1
* 2
* 3
+ 1
+ 2
+ 3
- 1
- 2
- 3

### 有序列表
1. 列表1
2. 列表2
3. 列表3

### 区块引用
* 不以结婚为目的的谈恋爱都是耍流氓
    > 这是毛泽东说的
    

#### 嵌套引用
* 前方高能
    > 一级引用
    >> 二级引用
    >>> 三级引用
    
### 华丽分割线
 ***
 ---
 - - -

### 链接
   #### 行内式链接
   * [不知道写什么](http://www.baidu.com)
   #### 参数式
   [name]: http://www.baidu.com/name "名称"
   [home]: http://www.baidu.com/home "首页"
   [也支持中文]: /home/name "瞎写的"  
   
   这里是[name],这里是home,这里是[也支持中文]          
   
### 图片
   ![avatar](https://github.com/leelovejava/boot-demo/blob/master/src/main/resources/doc/view.jpg?raw=true)
#### 代码框
   ```
   public class test {
       public static void main(String[] args) {
           System.out.println("hello world");
       }
   }
   ```  
### 强调
 *字体倾斜*
 _字体倾斜_
 **字体加粗**
 __字体加粗__
 
### 转义
* \\
* \`
* \~
* \_
* \-
* \+

### 删除线
 ~~请删除我吧~~  
 
### 链接
[印象笔记markdown](https://mp.weixin.qq.com/s/1AUUThovTjQKdaK1Z34spg)   


### 行内标记
上面是 `JavaScript`，下面是 `php`：

```php
echo 'hello,world'
```

### 格式化文本
保持输入排版格式不变
<pre>
hello world 
         hi
  hello world 
</pre>