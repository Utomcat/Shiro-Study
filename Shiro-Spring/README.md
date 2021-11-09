# `Shiro` + `Spring` 集成 (此工程项目为 `web` 工程)

[TOC]

## 一、流程步骤

### 1）、配置拦截器

> 在 `webapp\WEB-INF\web.xml` 文件中配置有关拦截器信息,具体配置如下:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
    <!--1. 配置 Spring 的配置文件路径 -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:application.xml</param-value>
    </context-param>
    <!--2. 配置 Spring 的监听器 -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <!--3. 配置 Spring的拦截器,以及 Spring-mvc 的配置路径 -->
    <servlet>
        <servlet-name>spring</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-mvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <!--4. 配置 Spring 拦截器 -->
    <servlet-mapping>
        <servlet-name>spring</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <!--5. 配置 Shiro 的过滤器 -->
    <filter>
        <filter-name>shiroFilter</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
        <init-param>
            <param-name>targetFilterLifecycle</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <!--6. 配置 Shiro 的过滤器映射,过滤所有请求 -->
    <filter-mapping>
        <filter-name>shiroFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

### 2）、配置 `Spring` 配置

> 在 `resources` 目录下,新建一个 `application.xml`,进行 `Spring` 的配置,具体配置如下:
<br/>
> 注意文件名 `application.xml` 不定,具体的文件名是要和 `web.xml` 中的 `contextConfigLocation` 节点配置的文件名一致

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--1. 配置 securityManager bean-->
    <bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
        <property name="cacheManager" ref="cacheManager"/>
        <property name="realm" ref="jdbcRealm"/>
    </bean>
    <!--2. 配置 cacheManager-->
    <bean id="cacheManager" class="org.apache.shiro.cache.ehcache.EhCacheManager">
        <property name="cacheManagerConfigFile" value="classpath:shiro/ehCache.xml"/>
    </bean>
    <!--3. 配置 Realm, 通过实现 Realm 接口的类作为配置类  -->
    <bean id="jdbcRealm" class="com.ranyk.www.config.ShiroRealm"/>
    <!-- 4. 配置 lifecycleBeanPostProcessor, 可以自动调用配置在 Spring IOC 容器中的 shiro bean 的生命周期方法 -->
    <bean id="lifecycleBeanPostProcessor" class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/>
    <!-- 5. 启用 IOC 容器中使用 shiro 注解, 但必须是要在配置了 lifecycleBeanPostProcessor 之后才能生效  -->
    <bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"
          depends-on="lifecycleBeanPostProcessor"/>
    <bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
        <property name="securityManager" ref="securityManager"/>
    </bean>
    <!-- 6. 配置 shiroFilter,其中配置的 shiroFilter ID 需要和 web.xml 中的 shiro 拦截器的 filter-name 一致 -->
    <bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
        <property name="securityManager" ref="securityManager"/>
        <property name="loginUrl" value="/login.jsp"/>
        <property name="successUrl" value="/index.jsp"/>
        <property name="unauthorizedUrl" value="unauthorized.jsp"/>
        <!--
            配置有关页面是否需要认证后才能访问:
            1）、anon: 不需要认证就能访问
            2）、authc: 需要认证才能访问
            3）、/** : 通配符,所有页面或请求
         -->
        <property name="filterChainDefinitions">
            <value>
                /login.jsp = anon
                /** = authc
            </value>
        </property>
    </bean>
</beans>
```

### 3）、配置 `Spring MVC`

> 在 `resources` 目录下,新建一个 `spring-mvc.xml`,进行 `Spring MVC` 的配置,具体配置如下:
<br/>
> 注意文件名 `application.xml` 不定,具体的文件名是要和 `web.xml` 中的 `spring` 拦截器配置 `init-param` 节点配置 `contextConfigLocation` 参数值一致

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/mvc
        https://www.springframework.org/schema/mvc/spring-mvc.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>
    <context:component-scan base-package="com.ranyk"/>

    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
    <mvc:annotation-driven/>
    <mvc:default-servlet-handler/>
</beans>
```

### 4）、配置 `cacheManager` 的配置文件

> 在 `resources` 目录下,新建路径为 `shiro/ehCache.xml` 的文件,进行 `ehcache` 的配置,具体配置如下:
<br/>
> 注意文件路径 `shiro/ehCache.xml` 为, `Spring` 配置文件中配置的  `cacheManager bean` 的 `cacheManagerConfigFile` 参数属性值一致

### 5）、创建实现 `Realm` 接口的实现类

> 其实现类路径为 `Spring` 配置文件中 `jdbcRealm bean` 的 `class` 值,具体配置如下:

```java
package com.ranyk.www.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;

/**
 * CLASS_NAME: ShiroRealm.java <br/>
 * Description: Shiro Realm 配置 <br/>
 *
 * @author ranyk <br/>
 * @version V1.0 <br/>
 * @date 2021 - 11 - 08
 */
public class ShiroRealm implements Realm {

    public String getName() {
        return null;
    }

    public boolean supports(AuthenticationToken authenticationToken) {
        return false;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
```

### 6）、新建有关页面,配置服务器,测试

> 在 `webapp` 目录下新建 `jsp` 页面

- home.jsp

```
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <link type="text/css" rel="stylesheet"/>
    <title>Apache Shiro Quickstart</title>
</head>
<body>
<h1>Apache Shiro Quickstart</h1>
</body>
</html>
```

- index.jsp

```
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <link type="text/css" rel="stylesheet"/>
    <title>index</title>
</head>
<body>
<h1>index page</h1>
</body>
</html>
```

- login.jsp

```
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <link type="text/css" rel="stylesheet"/>
    <title>login</title>
</head>
<body>
<h1>login page</h1>
</body>
</html>
```

- unauthorized.jsp

```
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <link type="text/css" rel="stylesheet"/>
    <title>unauthorized</title>
</head>
<body>
<h1>unauthorized page</h1>
</body>
</html>
```

## 二、`Shiro` 的拦截器配置中注意事项

### 1）、在 `web.xml` 中配置的 `filter` 中 `filter-name` 要和 `Spring` 中配置的 `bean` 的 `id` 一致

> 如果不一致则会抛出 `NoSuchBeanDefinitionException` 异常,原因是初始化过滤器的时候会先调用 `org.springframework.web.filter.DelegatingFilterProxy.java` 中
> 的 `initFilterBean()` 方法.在该方法中会调用 `initDelegate(WebApplicationContext wac)` 方法,从 `Spring Context` 中获取 `targetBeanName` 属性.默认情况下会将 `web.xml`
> 中定义的 `Shiro` 的 `filter` 节点 `filter-name` 属性设置为 `targetBeanName` 的值.故如果不单独设置 `targetBeanName` 时,就需要保证 `Shiro` 的拦截器 `filter-name` 要和配置在
> `Spring` 中 `bean` 的 `id` 一致. 否则需要单独配置 `targetBeanName` 属性.即在 `web.xml` 和 `Spring` 配置文件中的配置如下:

- `web.xml` 配置

```xml

<filter>
    <filter-name>shiroFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    <init-param>
        <param-name>targetFilterLifecycle</param-name>
        <param-value>true</param-value>
    </init-param>
    <!-- 增加如下初始化参数 targetBeanName 属性值配置,此值需要和 Spring 中定义的 Shiro 拦截器 Bean id 一致 -->
    <init-param>
        <param-name>targetBeanName</param-name>
        <param-value>shiroFilter1</param-value>
    </init-param>
</filter>
```

- `Spring` 配置文件

```xml

<bean id="shiroFilter1" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
    <property name="securityManager" ref="securityManager"/>
    <property name="loginUrl" value="/login.jsp"/>
    <property name="successUrl" value="/index.jsp"/>
    <property name="unauthorizedUrl" value="unauthorized.jsp"/>
    <!--
        配置有关页面是否需要认证后才能访问:
        1）、anon: 不需要认证就能访问
        2）、authc: 需要认证才能访问
        3）、/** : 通配符,所有页面或请求
     -->
    <property name="filterChainDefinitions">
        <value>
            /login.jsp = anon
            /** = authc
        </value>
    </property>
</bean>
```

## 三、`Shiro` 中对 `URL` 拦截的定义

> `Shiro` 对所有 `URL` 进行拦截方式的定义在 `Spring` 中定义 `Shiro` 拦截器 `bean` 中的 `filterChainDefinitions` 属性中.

- 示例

```xml

<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
    <property name="filterChainDefinitions">
        <value>
            /login.jsp = anon
            /** = authc
        </value>
    </property>
</bean>
```

### 1）、定义 `Shiro` 拦截的 `URL` 格式

> `url = 拦截器[参数]` , 如: /login = anon , /** = authc

### 2）、默认的 `Shiro` 拦截器

| Filter | Name	Class | Description | Example |
| ------ | ------ | ------ | ------ |
| anon | org.apache.shiro.web.filter.authc.AnonymousFilter | 没有参数,表示指定的 `URL` 可以不用认证就可以访问 | /admin/** = anon |
| authc | org.apache.shiro.web.filter.authc.FormAuthenticationFilter | 没有参数,表示指定的 `URL` 需要认证后才能访问 | /user/** = authc |
| authcBasic | org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter | 没有参数,表示需通过 `httpBasic` 验证,如果不能通过,则跳转至登录页面 | /userBasic/** = authcBasic |
| authcBearer | org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter |  |  |
| invalidRequest | org.apache.shiro.web.filter.InvalidRequestFilter |  |  |
| logout | org.apache.shiro.web.filter.authc.LogoutFilter | 注销登录时,完成一定的功能,所有已经保存的 `session` 都将会失效.而且任何身份都将会失去关联,包括在 `web` 应用程序中 `Remember Me` 的 `cookie` 也将会被删除 |  |
| noSessionCreation | org.apache.shiro.web.filter.session.NoSessionCreationFilter | 阻止在请求期间创建新的会话 |  |
| perms | org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter | 有参数,参数可以写多个,存在多个参数时,每个参数都需要用 `“”` ,每个参数间用 `,` 分隔.同时在访问时,需要满足所有的参数都通过才能访问,等同于 `isPermitedAll()` 方法 | `/admins/** = perms[user:add:*]`  或 `/userAdmins/** = perms["user:add:*,user:modify:*"]` |  
| port | org.apache.shiro.web.filter.authz.PortFilter | 有参数,指定请求访问的端口,如果不匹配规则,则跳转至登录界面 | /admins/** = port[8081] |
| rest | org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter | 有参数,配置有关请求需要哪种请求方法 | /admin/** = rest[get], 其中 `get` 可替换为 `post` `put` 等 |
| roles | org.apache.shiro.web.filter.authz.RolesAuthorizationFilter | 有参数,角色过滤器,判断当前用户是否存在指定角色,可写多个角色.存在多个角色时,需要用 `“”` ,每个参数间用 `,` 分隔.相当于 `hasAllRoles()` | /admins/** = roles["admin,guest"] |
| ssl | org.apache.shiro.web.filter.authz.SslFilter | 没有参数,表示安全的 `URL` 请求,协议为 `https` |  |
| user | org.apache.shiro.web.filter.authc.UserFilter | 没有参数,表示必须存在用户 |  |

### 3）、`Shiro` 拦截器 `URL` 通配符

> `Shiro` 中使用 `Ant` 风格通配符,具体通配符如下:

- `?` : 匹配一个字符,如: /admin? 可以匹配 /admin1 ,但不能匹配 /admin 或 /admin/
- `*` : 匹配零个或多个字符串,如: /admin* 可以匹配 /admin 、 /admin123 ,但不能匹配 /admin/1
- `**` : 匹配路径中的零个或多个路径,如 /admin/** 可以匹配 /admin/ 或 /admin/1

> 注意, 通配符是不匹配目录的分隔符( `windows` 中目录分隔符为 `\` ; `Linux` 中目录分隔符为: `/` ;)

### 4）、`Shiro` 多个 `URL` 拦截配置的匹配规则

> `URL` 权限采用第一次匹配优先的方式,即从配置中读取,使用第一次匹配上的拦截器链,如存在如下的 `URL` 拦截定义:

- /bb/**  ==> filter1
- /bb/aa ==> filter2
- /**     ==> filter3

> 如果请求为: /bb/aa ,根据声明顺序,进行匹配,则会使用 filter1 进行处理拦截 

## 四、`Shiro` 认证
