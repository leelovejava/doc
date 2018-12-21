# 不会的SQL

## 使用group,解决distinct与order by 的冲突
```sql
select source_id from  personfile.t_timing_face_person group by source_id order by max(time) desc
```