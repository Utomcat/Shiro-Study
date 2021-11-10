# `Shiro Authenticate` 子工程

[TOC]

## `Shiro` 认证流程

1. 获取当前的 `Subject`, 调用 `SecurityUtils.getSubject();` 方法
2. 调用 `Subject.isAuthenticated()` 方法判断当前用户是否已经认证
3. 若没有认证,则将用户名和密码封装为 UsernamePasswordToken 对象
    1. 用户名和密码均由前端界面提交到后端
4. 执行登录操作,调用 `Subject.login(AuthenticationToken token)` 方法
5. 自定义 `Realm` 方法,从数据库中获取有关用户信息,返回给 `Shiro`
    1. 通过继承 `org.apache.shiro.realm.AuthenticatingRealm` 类,实现 `doGetAuthenticationInfo(AuthenticationToken)` 方法
6. 由 `Shiro` 完成密码的比对

## 注意事项

### 1）、使用 `SLF4J` 进行控制台日志输出的配置,需要注意要引入的依赖,否则可能导致日志不能打印输出

- `pom` 依赖

```xml

<dependencies>
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
    </dependency>
</dependencies>
```

- `log4j.properties` 配置

```properties
log4j.rootLogger=DEBUG,stdout
log4j.appender.stdout.Threshold=DEBUG
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.ImmediateFlush=true
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m%n
```