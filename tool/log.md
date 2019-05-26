[正确的打日志姿势](https://mp.weixin.qq.com/s/b8Wxr-lYGxPXJfUlgSv1rw)

1. 对于debug日志，必须判断是否为debug级别后，才进行使用
```
if (logger.isDebugEnabled()) {
   logger.debug("Processing trade with id: " +id + " symbol: " + symbol);
}
```   

2. 不要进行字符串拼接,那样会产生很多String对象，占用空间，影响性能
反例:
```
logger.debug("Processing trade with id: " + id + " symbol: " + symbol);
```

3. 使用[]进行参数变量隔离
```
logger.debug("Processing trade with id:[{}] and symbol : [{}] ", id, symbol);
```

4. 正确使用
```
@Override
@Transactional
public void createUserAndBindMobile(@NotBlank String mobile, @NotNull User user) throws CreateConflictException{
    boolean debug = log.isDebugEnabled();
    if(debug){
        log.debug("开始创建用户并绑定手机号. args[mobile=[{}],user=[{}]]", mobile, LogObjects.toString(user));
    }
    try {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userRepository.insertSelective(user);
        if(debug){
            log.debug("创建用户信息成功. insertedUser=[{}]",LogObjects.toString(user));
        }
        UserMobileRelationship relationship = new UserMobileRelationship();
        relationship.setMobile(mobile);
        relationship.setOpenId(user.getOpenId());
        relationship.setCreateTime(new Date());
        relationship.setUpdateTime(new Date());
        userMobileRelationshipRepository.insertOnDuplicateKey(relationship);
        if(debug){
            log.debug("绑定手机成功. relationship=[{}]",LogObjects.toString(relationship));
        }
        log.info("创建用户并绑定手机号. userId=[{}],openId=[{}],mobile=[{}]",user.getId(),user.getOpenId(),mobile);
    }catch(DuplicateKeyException e){
        log.info("创建用户并绑定手机号失败,已存在相同的用户. openId=[{}],mobile=[{}]",user.getOpenId(),mobile);
        throw new CreateConflictException("创建用户发生冲突, openid=[%s]",user.getOpenId());
    }
}
```
