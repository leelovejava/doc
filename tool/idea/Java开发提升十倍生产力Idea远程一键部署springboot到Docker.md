# Java开发提升十倍生产力:Idea远程一键部署springboot到Docker

Idea是Java开发利器，springboot是Java生态中最流行的微服务框架，docker是时下最火的容器技术，那么它们结合在一起会产生什么化学反应呢？

## 一、开发前准备

#### 1. Docker的安装可以参考https://docs.docker.com/install/

#### 2. 配置docker远程连接端口

```
  vi /usr/lib/systemd/system/docker.service
复制代码
```

找到 **ExecStart**，在最后面添加 **-H tcp://0.0.0.0:2375**，如下图所示



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5152965b0277a?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



#### 3. 重启docker

```
 systemctl daemon-reload
 systemctl start docker
复制代码
```

#### 4. 开放端口

```
firewall-cmd --zone=public --add-port=2375/tcp --permanent  
复制代码
```

#### 5. Idea安装插件,重启



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5155b235261e1?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



#### 6. 连接远程docker

####    (1) 编辑配置



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5156207039d0e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



####    (2) 填远程docker地址



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b51568a1728820?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



####    (3) 连接成功，会列出远程docker容器和镜像



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5156db1f07008?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



## 二、新建项目

#### 1. 创建springboot项目

     项目结构图



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b51572f7be11e0?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



####   (1) 配置pom文件

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>docker-demo</groupId>
    <artifactId>com.demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.2.RELEASE</version>
        <relativePath />
    </parent>

    <properties>
         <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
         <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
         <docker.image.prefix>com.demo</docker.image.prefix>
         <java.version>1.8</java.version>
    </properties>
    <build>
        <plugins>
          <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
        <plugin>
           <groupId>com.spotify</groupId>
           <artifactId>docker-maven-plugin</artifactId>
           <version>1.0.0</version>
           <configuration>
              <dockerDirectory>src/main/docker</dockerDirectory>
              <resources>
                <resource>
                    <targetPath>/</targetPath>
                    <directory>${project.build.directory}</directory>
                    <include>${project.build.finalName}.jar</include>
                </resource>
              </resources>
           </configuration>
        </plugin>
        <plugin>
            <artifactId>maven-antrun-plugin</artifactId>
            <executions>
                 <execution>
                     <phase>package</phase>
                    <configuration>
                        <tasks>
                            <copy todir="src/main/docker" file="target/${project.artifactId}-${project.version}.${project.packaging}"></copy>
                        </tasks>
                     </configuration>
                    <goals>
                        <goal>run</goal>
                    </goals>
                    </execution>
            </executions>
        </plugin>

       </plugins>
    </build>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
  <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>
</dependencies>
</project>
复制代码
```

####   (2) 在src/main目录下创建docker目录，并创建Dockerfile文件

```
FROM openjdk:8-jdk-alpine
ADD *.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
复制代码
```

####   (3) 在resource目录下创建application.properties文件

```
logging.config=classpath:logback.xml
logging.path=/home/developer/app/logs/
server.port=8990
复制代码
```

####   (4) 创建DockerApplication文件

```
@SpringBootApplication
public class DockerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DockerApplication.class, args);
    }
}
复制代码
```

####   (5) 创建DockerController文件

```
@RestController
public class DockerController {
    static Log log = LogFactory.getLog(DockerController.class);

    @RequestMapping("/")
    public String index() {
        log.info("Hello Docker!");
        return "Hello Docker!";
    }
}
复制代码
```

####   (6) 增加配置



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5161dab9cb6e4?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)





![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5161faed2393a?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)





![img](https://user-gold-cdn.xitu.io/2019/6/13/16b51621eaf8680f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



  命令解释
       **Image tag :** 指定镜像名称和**tag**，镜像名称为 **docker-demo**，**tag**为**1.1**
       **Bind ports :** 绑定宿主机端口到容器内部端口。格式为[宿主机端口]:[容器内部端口]
       **Bind mounts :** 将宿主机目录挂到到容器内部目录中。格式为[宿主机目录]:[容器内部目录]。这个springboot项目会将日志打印在容器 **/home/developer/app/logs/** 目录下，将宿主机目录挂载到容器内部目录后，那么日志就会持久化容器外部的宿主机目录中。

####   (7) Maven打包



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5167788e14ee1?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



####   (8) 运行



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b51679f663afe8?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)





![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5167bec448fe4?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 先pull基础镜像，然后再打包镜像，并将镜像部署到远程docker运行





![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5168992f0d1f4?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 这里我们可以看到镜像名称为docker-demo:1.1，docker容器为docker-server



####   (9) 运行成功



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5168d1c05f997?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



####   (10) 浏览器访问



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b5168ed469a871?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



####   (11) 日志查看



![img](https://user-gold-cdn.xitu.io/2019/6/13/16b516908d72a340?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)