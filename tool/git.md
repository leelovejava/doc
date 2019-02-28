# git

## doc
-[Git常用命令速查表](https://mp.weixin.qq.com/s/k4tU8snvssyKJ2WkvkFrZA)

-[Git 使用规范流程](https://mp.weixin.qq.com/s/_safn17a6C6PP852IFkK-A)

-[20 分钟教你搞懂 Git！](https://mp.weixin.qq.com/s/ShunINXYybKftmPUcQsrxA)

还原上一次提交 
> git reset HEAD~

git取消本地提交,未push
> git push origin HEAD --force

git忽略idea文件
```
git rm -r –cached .
git add .	
git commit -m "update .gitignore"

git rm -r –cached .idea/ 
git commit -m "remove .idea" 
```
---------

git提交规范:
> <type>(<scope>): <subject>

type
    用于说明 commit 的类别，只允许使用下面7个标识
    
    feat：新功能（feature）
    fix：修补bug
    docs：文档（documentation）
    style： 格式（不影响代码运行的变动）
    refactor：重构（即不是新增功能，也不是修改bug的代码变动）
    test：增加测试
    chore：构建过程或辅助工具的变动

scope
    用于说明 commit 影响的范围，比如数据层、控制层、视图层等等，视项目不同而不同。

subject
    是 commit 目的的简短描述，不超过50个字符。    
    
1. 以动词开头，使用第一人称现在时，比如change，而不是changed或changes
2. 第一个字母小写
3. 结尾不加句号（.）    