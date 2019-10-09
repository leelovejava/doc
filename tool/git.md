# git

## doc
-[Git常用命令速查表](https://mp.weixin.qq.com/s/k4tU8snvssyKJ2WkvkFrZA)

-[Git 使用规范流程](https://mp.weixin.qq.com/s/_safn17a6C6PP852IFkK-A)

-[20 分钟教你搞懂 Git！](https://mp.weixin.qq.com/s/ShunINXYybKftmPUcQsrxA)

更新
git fetch origin master

还原上一次提交 
> git reset HEAD~

git取消本地提交,未push
> git push origin HEAD --force

git还原远程提交
> git reset --hard commit_id

github SSL_connect: SSL_ERROR_SYSCALL in connection to github.com:443
> git config --global --unset http.proxy

设置全局提交的用户名 

> git config user.name

> git config --global user.name "Jack.Ma"

git忽略idea文件
```
git rm -r –cached .
git add .	
git commit -m "update .gitignore"

git rm -r –cached .idea/ 
git commit -m "remove .idea" 
```

错误1:
git Authentication failed

解决办法: 控制面板\用户帐户\凭据管理器\Windows凭据->删除对应的账号->检出时自动重新输入

Can't update: no tracked branch
git branch --set-upstream-to origin/master master

错误2:
git clone时RPC failed; curl 18 transfer closed with outstanding read data remaining

https://www.cnblogs.com/zjfjava/p/10392150.html
加大缓存区
git config http.postBuffer 524288000
git config --global http.lowSpeedLimit 0
git config --global http.lowSpeedTime 999999

–depth 1的含义是复制深度为1，就是每个文件只取最近一次提交，不是整个历史版本
git clone --depth=1 http://gitlab.xxx.cn/yyy/zzz.git

错误3: RPC failed; curl 56 OpenSSL SSL_read: SSL_ERROR_SYSCALL, errno 10054
 git config  http.sslVerify "false"



错误4: error: RPC failed; curl 18 transfer closed with outstanding read data remaining
 git config --global http.postBuffer 524288000
 git config http.postBuffer 524288000

错误5:fatal: unable to access 'https://github.com/golang/tools.git/': OpenSSL SSL_read: SSL_ERROR_SYSCALL, errno 10054
git config http.postBuffer 524288000

错误6: Push to origin/master was rejected 
在第一次提交到代码仓库的时候非常容易出现，因为初始化的仓库和本地仓库是没有什么关联的，因此，在进行第一次的新代码提交时，通常会出现这个错误
> git pull origin master --allow-unrelated-histories

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