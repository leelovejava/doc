1. 单参数List的类型
```xml
<select id="dynamicForeachTest" resultType="Blog">
    select * from t_blog where id in
    <foreach collection="list" index="index" item="item" open="(" separator="," close=")">
        #{item}
    </foreach>
</select>
```

2. 单参数array数组的类型
```xml
<select id="dynamicForeach2Test" parameterType="java.util.ArrayList" resultType="Blog">
     select * from t_blog where id in
     <foreach collection="array" index="index" item="item" open="(" separator="," close=")">
          #{item}
     </foreach>
 </select>  
```

3. Map
```xml
 <select id="dynamicForeach3Test" parameterType="java.util.HashMap" resultType="Blog">
         select * from t_blog where title like "%"#{title}"%" and id in
          <foreach collection="ids" index="index" item="item" open="(" separator="," close=")">
               #{item}
          </foreach>
 </select>
```

4. 