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

## 程序中的异常

### 1）、项目启动、登录、登出时报 ` Unable to execute 'doFinal' with cipher instance` 异常,但是不影响程序运行,异常详情如下:

```text
[http-nio-8082-exec-4] WARN org.apache.shiro.mgt.AbstractRememberMeManager - There was a failure while trying to retrieve remembered principals.  This could be due to a configuration problem or corrupted principals.  This could also be due to a recently changed encryption key, if you are using a shiro.ini file, this property would be 'securityManager.rememberMeManager.cipherKey' see: http://shiro.apache.org/web.html#Web-RememberMeServices. The remembered identity will be forgotten and not used for this request.
[http-nio-8082-exec-4] WARN org.apache.shiro.mgt.DefaultSecurityManager - Delegate RememberMeManager instance of type [org.apache.shiro.web.mgt.CookieRememberMeManager] threw an exception during getRememberedPrincipals().
org.apache.shiro.crypto.CryptoException: Unable to execute 'doFinal' with cipher instance [javax.crypto.Cipher@49d66385].
	at org.apache.shiro.crypto.JcaCipherService.crypt(JcaCipherService.java:462)
	at org.apache.shiro.crypto.JcaCipherService.crypt(JcaCipherService.java:445)
	at org.apache.shiro.crypto.JcaCipherService.decrypt(JcaCipherService.java:390)
	at org.apache.shiro.crypto.JcaCipherService.decrypt(JcaCipherService.java:382)
	at org.apache.shiro.mgt.AbstractRememberMeManager.decrypt(AbstractRememberMeManager.java:482)
	at org.apache.shiro.mgt.AbstractRememberMeManager.convertBytesToPrincipals(AbstractRememberMeManager.java:419)
	at org.apache.shiro.mgt.AbstractRememberMeManager.getRememberedPrincipals(AbstractRememberMeManager.java:386)
	at org.apache.shiro.mgt.DefaultSecurityManager.getRememberedIdentity(DefaultSecurityManager.java:613)
	at org.apache.shiro.mgt.DefaultSecurityManager.resolvePrincipals(DefaultSecurityManager.java:501)
	at org.apache.shiro.mgt.DefaultSecurityManager.createSubject(DefaultSecurityManager.java:347)
	at org.apache.shiro.subject.Subject$Builder.buildSubject(Subject.java:845)
	at org.apache.shiro.web.subject.WebSubject$Builder.buildWebSubject(WebSubject.java:148)
	at org.apache.shiro.web.servlet.AbstractShiroFilter.createSubject(AbstractShiroFilter.java:292)
	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:359)
	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)
	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:358)
	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:271)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)
	at org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:687)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1723)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)
Caused by: javax.crypto.AEADBadTagException: Tag mismatch!
	at com.sun.crypto.provider.GaloisCounterMode.decryptFinal(GaloisCounterMode.java:620)
	at com.sun.crypto.provider.CipherCore.finalNoPadding(CipherCore.java:1116)
	at com.sun.crypto.provider.CipherCore.fillOutputBuffer(CipherCore.java:1053)
	at com.sun.crypto.provider.CipherCore.doFinal(CipherCore.java:853)
	at com.sun.crypto.provider.AESCipher.engineDoFinal(AESCipher.java:446)
	at javax.crypto.Cipher.doFinal(Cipher.java:2168)
	at org.apache.shiro.crypto.JcaCipherService.crypt(JcaCipherService.java:459)
	... 35 more
```

- 产生原因
  > 由于 `rememberMe` 管理器造成的
- 解决方案

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