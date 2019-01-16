# 不会的SQL

## 使用group,解决distinct与order by 的冲突
```sql
select source_id from  personfile.t_timing_face_person group by source_id order by max(time) desc
```

## [查询用逗号分隔的字段-字符串函数 FIND_IN_SET()](https://www.cnblogs.com/zxmceshi/p/5479892.html)

>> select * from t_sys_user where find_in_set('111', openids);