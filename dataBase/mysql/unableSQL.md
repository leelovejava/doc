# 不会的SQL

## 使用group,解决distinct与order by 的冲突
```sql
select source_id from  personfile.t_timing_face_person group by source_id order by max(time) desc
```

## [查询用逗号分隔的字段-字符串函数 FIND_IN_SET()](https://www.cnblogs.com/zxmceshi/p/5479892.html)

>> select * from t_sys_user where find_in_set('111', openids);

## [MySQL忽略主键冲突，避免重复插入数据的三种方式](https://blog.csdn.net/u014745069/article/details/80233374)

## 多条记录,取最新的一条
> SELECT
  	id,
  	accountId,
  	mark,
  	createTime,
  	price 
  FROM
  	accountmark t 
  WHERE
  	( createTime = ( SELECT MAX( createTime ) FROM accountmark WHERE accountId = t.accountId ) );