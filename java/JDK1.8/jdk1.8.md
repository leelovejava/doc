[optional](https://blog.battcn.com/2017/07/23/java/jdk8/optional/)

## optional方法
| 方法      |      描述      |
|-----------|:-------------:|	        
|empty	    |创建一个空的Optional对象|
|ofNullable	|把指定的值封装为Optional对象，如果指定的值为null，则创建一个空的Optional对象|
|get	    |如果创建的Optional中有值存在，则返回此值，否则抛出NoSuchElementException|
|orElse	    |如果创建的Optional中有值存在，则返回此值，否则返回一个默认值|
|orElseGet	|如果创建的Optional中有值存在，则返回此值，否则返回一个由Supplier接口生成的值|
|orElseThrow|如果创建的Optional中有值存在，则返回此值，否则抛出一个由指定的Supplier接口生成的异常|
|filter	    |如果创建的Optional中的值满足filter中的条件，则返回包含该值的Optional对象，否则返回一个空的Optional对象|
|map        |如果创建的Optional中的值存在，对该值执行提供的Function函数调用|
|flagMap    |如果创建的Optional中的值存在，就对该值执行提供的Function函数调用，返回一个Optional类型的值，否则就返回一个空的Optional对象|
|isPresent  |如果创建的Optional中的值存在，返回true，否则返回false|
|ifPresent  |如果创建的Optional中的值存在，则执行该方法的调用，否则什么也不做|

### of
```
//创建一个值为张三的String类型的Optional
Optional<String> ofOptional = Optional.of("张三");
//如果我们用of方法创建Optional对象时，所传入的值为null，则抛出NullPointerException
Optional<String> nullOptional = Optional.of(null);
```

### ofNullable
```
//为指定的值创建Optional对象，不管所传入的值为null不为null，创建的时候都不会报错
Optional<String> nullOptional = Optional.ofNullable(null);
Optional<String> nullOptional = Optional.ofNullable("lisi");
```

### empty
```
//创建一个空的String类型的Optional对象
Optional<String> emptyOptional = Optional.empty()
```

### get
```
// 如果创建的Optional对象中有值存在则返回此值，如果没有值存在，则会抛出 
   NoSuchElementException异常
Optional<String> stringOptional = Optional.of("张三");
// 输出 张三
System.out.println(stringOptional.get());
```

### orElse
```
// 如果创建的Optional中有值存在，则返回此值，否则返回一个默认值
Optional<String> stringOptional = Optional.of("张三");
// 输出 张三
System.out.println(stringOptional.orElse("zhangsan"));

// 输出 李四
Optional<String> emptyOptional = Optional.empty();
System.out.println(emptyOptional.orElse("李四"));
```

### orElseGet
```
// 如果创建的Optional中有值存在，则返回此值，否则返回一个由Supplier接口生成的值
Optional<String> stringOptional = Optional.of("张三");
System.out.println(stringOptional.orElseGet(() -> "zhangsan"));

Optional<String> emptyOptional = Optional.empty();
System.out.println(emptyOptional.orElseGet(() -> "orElseGet"));
```