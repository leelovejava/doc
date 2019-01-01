
[java百万级别excel导出（easyExcel）](https://blog.csdn.net/roger_coderlife/article/details/83748963)

easyexcel核心功能
读任意大小的03、07版Excel不会OOM
读Excel自动通过注解，把结果映射为java模型
读Excel支持多sheet
读Excel时候是否对Excel内容做trim()增加容错
写小量数据的03版Excel（不要超过2000行）
写任意大07版Excel不会OOM
写Excel通过注解将表头自动写入Excel
写Excel可以自定义Excel样式 如：字体，加粗，表头颜色，数据内容颜色
写Excel到多个不同sheet
写Excel时一个sheet可以写多个Table
写Excel时候自定义是否需要写表头

快速使用
```pom
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>{latestVersion}</version>
</dependency>
```

读取Excel
ExcelListener