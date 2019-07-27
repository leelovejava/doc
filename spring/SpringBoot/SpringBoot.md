[一起来学SpringBoot | 第二十八篇：JDK8 日期格式化](https://blog.battcn.com/2018/10/01/springboot/v2-localdatetime/)

数据验证

[轻松搞定数据验证（一）](https://blog.battcn.com/2018/06/05/springboot/v2-other-validate1/)
[轻松搞定数据验证（二）](https://blog.battcn.com/2018/06/06/springboot/v2-other-validate2/)
[轻松搞定数据验证（三）](https://blog.battcn.com/2018/06/07/springboot/v2-other-validate3/)

[轻松搞定全局异常](https://blog.battcn.com/2018/06/01/springboot/v2-other-exception/)

[轻松搞定文件上传](https://blog.battcn.com/2018/05/31/springboot/v2-other-upload/)

[定时任务详解](https://blog.battcn.com/2018/05/29/springboot/v2-other-scheduling/)

[服务监控与管理](https://blog.battcn.com/2018/05/24/springboot/v2-actuator-introduce/)
[actuator与spring-boot-admin](https://blog.battcn.com/2018/05/24/springboot/v2-actuator-monitor/)

[集成Swagger在线调试](https://blog.battcn.com/2018/05/16/springboot/v2-config-swagger/)

[使用Spring Cache集成Redis](https://blog.battcn.com/2018/05/13/springboot/v2-cache-redis/)

[整合Mybatis](https://blog.battcn.com/2018/05/09/springboot/v2-orm-mybatis/)
[通用Mapper与分页插件的集成](https://blog.battcn.com/2018/05/10/springboot/v2-orm-mybatis-plugin/)
[整合SpringDataJpa](https://blog.battcn.com/2018/05/08/springboot/v2-orm-jpa/)
[使用JdbcTemplate访问数据库](https://blog.battcn.com/2018/05/07/springboot/v2-orm-jdbc/)

[整合Thymeleaf模板](https://blog.battcn.com/2018/04/28/springboot/v2-web-thymeleaf/)

[SpringBoot日志配置](https://blog.battcn.com/2018/04/23/springboot/v2-config-logs/)

[SpringBoot配置详解](https://blog.battcn.com/2018/04/22/springboot/v2-config-properties/)

[Spring Boot配置多个DataSource](https://www.liaoxuefeng.com/article/001484212576147b1f07dc0ab9147a1a97662a0bd270c20000)

[Spring Boot应用迁移到Java最新版（Java 11）](https://mp.weixin.qq.com/s/avhIEa0mSzj4qepai-hJcA)

[spring boot 配置文件配置项前缀为0的数字特殊处理](https://blog.csdn.net/ly20116/article/details/86608152)

---------------------------
```
spring:
    # jackson时间格式化
    jackson:****
        time-zone: GMT+8
        date-format: yyyy-MM-dd HH:mm:ss
```        

```
spring.mvc.view.suffix=.jsp
# jsp放在 webapp/WEB-INF/jsp/
spring.mvc.view.prefix=/WEB-INF/jsp/
```

添加jsp支持
```
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.tomcat</groupId>
    <artifactId>tomcat-jsp-api</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```

[SpringBoot配置jsp](https://github.com/spring-projects/spring-boot/tree/v2.1.3.RELEASE/spring-boot-samples/spring-boot-sample-web-jsp)

---------------------------
```

// Springboot 集成jsp 以及多模块下jsp页面找不到问题解决
// https://gitee.com/iBase4J/iBase4J/tree/master/iBase4J-SYS-Web   
@Configuration
public class WebJSPConfig  extends WebMvcConfigurerAdapter{
	private static final Logger logger= Logger.getLogger(WebJSPConfig.class);

	/**
	 * 多模块的jsp访问，默认是src/main/webapp，但是多模块的目录只设置yml文件或propeerties文件不行
	 * @return
	*/
	@Bean
	public InternalResourceViewResolver viewResolver(){
	        InternalResourceViewResolver viewResolver=new InternalResourceViewResolver();
			viewResolver.setViewClass(JstlView.class);
			viewResolver.setPrefix("/WEB-INF/jsp/");
			viewResolver.setSuffix(".jsp");
			logger.info("****************/WEB-INF/jsp/*****************************************");
	        return viewResolver;
	}


    /** 指定默认文件的地址，jsp页面引入js和css的时候就不用管项目路径了 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/static/**")
	                .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
	        super.addResourceHandlers(registry);
	}
}     


https://blog.csdn.net/herojuice/article/details/86527198
@Configuration
public class GlobalConfig {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
        return (factory) -> {
            factory.addContextCustomizers((context) -> {
                        //模块中webapp相对路径
                        String relativePath = "tt-web/src/main/webapp";
                        File docBaseFile = new File(relativePath);
                        // 路径是否存在
                        if (docBaseFile.exists()) {
                            context.setDocBase(docBaseFile.getAbsolutePath());
                        }
                    }
            );
        };
    }
}


<build>
  <resources>
      <!-- 打包时将jsp文件拷贝到META-INF目录下-->
      <resource>
          <!-- 指定resources插件处理哪个目录下的资源文件 -->
          <directory>src/main/webapp</directory>
          <!--注意此次必须要放在此目录下才能被访问到-->
          <targetPath>META-INF/resources</targetPath>
          <includes>
              <include>**/**</include>
          </includes>
      </resource>
      <resource>
          <directory>src/main/resources</directory>
          <includes>
              <include>**/**</include>
          </includes>
          <filtering>false</filtering>
      </resource>
  </resources>
</build>
```