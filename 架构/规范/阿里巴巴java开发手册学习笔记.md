## 阿里巴巴java开发手册的学习笔记
 ### 编程规约
   #### 命名风格
    * 禁止符号开头
    * 禁止中英文混合
    * UpperCamelCase命名,例外:DO、BO、DTO、VO、AO、PO、UID
    * lowerCamelCase:方法名、参数名、成员变量、局部变量
    * 常量大写,单词用下划线分开,力求表达清楚，不要嫌单词长
    * 抽象类用Abstract开头;异常类用Exception结尾;测试类用Test结尾
    * POJO类布尔类型的变量禁止is前缀,例如:isActive是否激活
    * package名小写,单数形式
    * 避免不规范的缩写
    * 模块、接口、类、方法使用了代理模式，命名时体现具体的设计模式,例如:OrderFactory
    * 接口中的方法和属性不加修饰符(public也不要加),加上Javadoc注释
    * SOA的理念,接口实现类用Impl后缀
    * 枚举后缀名Enum,成员名称全部大写,单词用下划线分开
    * 各层命名规约
      **Service/DAO层方法规约
        1. 获取单个对象 get前缀
        2. 获取多个对象 list前缀,复数形式结尾如:listObjects
        3. 统计         count前缀
        4. 插入         insert/save前缀
        5. 删除         remove/delete前缀
        6. 修改         update前缀
      **领域模型规约
        1. 数据对象:    表名DO
        2. 数据传输对象:业务名DTO
        3. 展示对象:    网页名称VO
        4. POJO是DO/DTO/BO/VO的统称,禁止命名XXXPOJO
   #### 常量定义
    * 常量类按功能划分，禁止一个类维护所有常量类
    * Long代替long,避免和1进行混淆
   #### 代码格式
    * if/for/while/switch/do 等保留字与括号之间都必须加空格
    * 任何二目、 三目运算符的左右两边都需要加一个空格
    * 注释的双斜线与注释内容之间有且仅有一个空格,例如:// 这是示例注释，请注意在双斜线之后有一个空格
    * 单行字符数限制不超过 120 个，超出需要换行
    * 单个方法的总行数不超过 80 行(包括注释、空格、换行)
    * 方法参数在定义和传入时，多个参数逗号后边必须加空格,例如:method(args1, args2, args3);
    * 文件编码为UTF-8
    * 不同逻辑之间,插入一个空行分割
   #### OOP规约
    * 重写方法,加@Override
    * 参数列表,不可变参数放最后,不推荐使用不可变参数
    * 不能用过时的类或方法
    * 判断相等,不为空放左边,推荐使用java.util.Objects.equals("1","2")
    * 定义 DO/DTO/VO 等 POJO 类时，不要设定任何属性默认值
    * 序列化类新增属性时，请不要修改 serialVersionUID 字段，避免反序列失败
    * 构造方法里面禁止加入任何业务逻辑，如果有初始化逻辑，请放在 init 方法中
    * POJO 类必须写 toString 方法
    * 使用索引访问用 String 的 split 方法得到的数组时，需做最后一个分隔符后有无
      内容的检查,例如:String str = "a,b,c,,";
   #### 集合处理
    * 集合初始化时， 指定集合初始值大小
      initialCapacity = (需要存储的元素个数 / 负载因子) + 1。
      注意负载因子（即 loader factor） 默认为 0.75， 如果暂时无法确定初始值大小，请设置为 16（即默认值）
    * map遍历,entrySet,拒绝keySet,推荐Map.foreach
   #### 并发处理
   #### 控制语句
    * 禁止单行,例如:if (condition) statements;
    * 少用if-else,禁止if-else三层,使用卫语句、策略模式、状态模式解决
   #### 注释规约
    * 类、类属性、类方法的注释必须使用 Javadoc 规范，使用/**内容*/格式
    * 所有的抽象方法（包括接口中的方法） 必须要用 Javadoc 注释、除了返回值、参数、
      异常说明外，还必须指出该方法做什么事情，实现什么功能  
    * 所有的类都必须添加创建者和创建日期
    * 代码修改的同时，注释也要进行相应的修改，尤其是参数、返回值、异常、核心逻辑
      等的修改
   #### 其他
    * 获取当前毫秒数 System.currentTimeMillis(); 而不是 new Date().getTime();统计时间等场景，推荐使用 Instant 类
    * 关闭资源 try-with-resource
        try (FileInputStream fileInput = new FileInputStream(one);
                        FileOutputStream fileOutput = new FileOutputStream(two);){
        } catch
        
        可关闭的资源必须实现 java.lang.AutoCloseable 接口
 ### 异常日志
    * 推荐slf4j
    * 条件输出&占位符
       if (logger.isDebugEnabled()) {
          logger.debug("Processing trade with id: " + id + " and symbol: " + symbol);
       }
    * 谨慎记录日志,避免记录敏感数据   
 ### 单元测试
    * 独立性,不能调用其他单元测试用例
    * 自动化,不能用System.out,而用assert验证
    * 核心业务、应用、模块增量更新,确保单元测试通过,覆盖率100%
 ### 安全规约
    * 用户个人数据,做权限校验
    * 禁止敏感数据直接展示,如支付密码
    * 参数校验
    * 有限资源做疲劳校验、数量控制,如短信发送
    * 敏感词汇过滤,等风控策略
 ### mysql数据库
    * 表示是否的字段,用is_xxx方式命名,数据类型unsigned tinyint,（1 表示是， 0 表示否）
      POJO类不要加is前缀
    * 表名和字段名小写
    * 禁止使用保留字
    * 小数类型为 decimal，禁止使用 float 和 double
    * 字段长度大于5000,使用text,拆表,用id对应,避免影响其他表的效率
      选择合适的数据类型
    * 表必备字段,id 创建时间 修改时间
    * 字段适当冗余,提高查询效率
    * 单表5000行,或者单表容量超过2GB,才分库分表
    * 禁止3表join
    * count(*)会统计值为 NULL 的行，而 count(列名)不会统计此列为 NULL 值的行
    * 分页查询逻辑时，若 count 为 0 应直接返回，避免执行后面的分页语句
    * 禁止使用存储过程,可移植性和可调试性差
    * 避免in查询,如无法避免，控制in后边集合数量在1000以内
    * 字符集为utf-8,存储表情用utf8mb4
   #### ORM映射
    * 不更新无需改动的列  
 ### 工程结构
   #### (一).应用分层
     划分规则:业务、模块
   ![avatar](https://github.com/leelovejava/boot-demo/blob/master/src/main/resources/doc/view.jpg?raw=true)
   * 开发接口层   rpc接口暴露,进行网关控制、流量控制
   * 终端显示层   模板渲染
   * Web层       访问控制转发、参数校验、不复用的业务简单处理
   * Service层   相对具体的业务逻辑服务层
   * Manager层  
    1). 对第三方平台接口的封装,预处理返回结果及转化异常信息
    2). 封装中间件、缓存方案的通用处理,如redis、mq
    3). dao层交互
   * dao层       数据库访问层
   * 外部接口或第三方平台:包括其他部门的RPC开放接口,基础平台,其他公司的HTTP接口
   #### (二).分层异常处理规约
    不重复打印日志,throw Exception时不需要记录日志,异常不抛到web层
   #### (三).分层领域模型规约
    * DO(Data Object):和数据库表结构一一对应
    * DTO(Data Transfer Object):数据传输对象,Service或Manager向外传输的对象
    * BO(Business Object):业务对象,由Service层输出的封装业务逻辑层的对象
    * AO(Application Object):应用对象,web层和Service层之前抽象的复用对象模型
    * VO(View Object):视图层对象
    * Query:数据查询对象
  #### (四).第三方库
    * 统一定义版本号
    * 禁止GroupId和ArtifactId相同,但版本号不同   
 ### 设计规约
    * 状态超过4个,使用状态图表达状态变化
    * 抽取公共模块、代码、配置、方法,避免重复代码