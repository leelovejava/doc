# python

## 大纲

###  Python基础
* 认识python
* 编写第一个python程序
* 注释
* 变量以及类型
* 标示符和关键字
* 输入&输出
* 运算符
* 数据类型转换
* 判断语句介绍
* if判断语句
* if嵌套
* while循环
* for循环

###  Python基础
* break和continue
* 字符串输入&输出
* 下标和切片
* 元组(tuple)
* 函数
* 文件操作
* 类和对象
* 封装、继承、多态
* 设计模式、异常、模块
* 列表推导式
* 集合
* 垃圾回收
* 编码风格

###  数据结构（一）
* 数据结构和算法基本介绍
* 数据结构和算法几个实际问题
* 稀疏数组介绍
* 稀疏数组压缩实现
* 稀疏数组解压实现
* 队列介绍
* 单向队列实现
* 单向队列问题分析
* 环形队列

###  数据结构（二）
* 链表说明和应用场景
* 单向链表-人员管理系统说明
* 单向链表-添加和遍历
* 单向链表-有序插入节点
* 单向链表-修改节点
* 单向链表-删除节点
* 双向链表基本介绍
* 双向链表的实现
* 链表的经典应用-约瑟夫问题
* 

###  数据结构（三）
* 约瑟夫问题-形成环形和遍历
* 约瑟夫问题-算法思路分析
* 约瑟夫问题-算法的实现
* 栈的基本介绍
* 栈的基本使用
* 使用栈计算表达式的思路
* 编写数栈和符号栈
* 完成单数表达式运算
* 递归的应用(迷宫回溯)

###  数据结构（四）
* 递归的机制分析
* 递归能解决的问题
* 迷宫解决思路和创建地图
* 递归回溯解决迷宫问题
* 冒泡排序
* 选择排序分析和实现
* 插入排序的思路分析
* 插入排序的实现
* 快速排序思路分析

### 数据结构（五）
* 快速排序代码实现
* 归并排序的思路分析
* 查找的基本介绍
* 二分查找实现和分析
* 二分查找所有相同值
* 哈希(散列)表实现机制分析
* 哈希(散列)表的添加、遍历、查找
* 树常用术语
* 二叉树的遍历 

## doc
[用Python告诉你深圳房租有多高](http://www.imooc.com/article/258729)

[神器！视频网站一行Python代码任意（下载）爬](https://mp.weixin.qq.com/s/DAvL-IqZ4RM72Sl69nUlwg)

[《中餐厅》弹幕数据分析](https://mp.weixin.qq.com/s/M--ABOJ55CiNkq6sb7PK9g)

从图片提取经纬度简易版
```python
import exifread
import re
  
def  imageread():
 
    GPS = {}
date = ''
f = open('./2018*****59171.jpg', 'rb')
imagetext = exifread.process_file(f)
 
for key in imagetext:                           #打印键值对
    print(key,":",imagetext[key])
 
print('********************************************************\n')
 
for q in imagetext:                             #打印该图片的经纬度 以及拍摄的时间
        if q == "GPS GPSLongitude":
            print("GPS经度 =", imagetext[q],imagetext['GPS GPSLatitudeRef'])
        elif q =="GPS GPSLatitude":
            print("GPS纬度 =",imagetext[q],imagetext['GPS GPSLongitudeRef'])
        elif q =='Image DateTime':
            print("拍摄时间 =",imagetext[q])
imageread()
```