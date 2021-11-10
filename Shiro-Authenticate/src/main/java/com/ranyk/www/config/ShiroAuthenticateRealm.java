package com.ranyk.www.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CLASS_NAME: ShiroRealm.java <br/>
 * Description: Shiro Realm 配置 <br/>
 *
 * @author ranyk <br/>
 * @version V1.0 <br/>
 * @date 2021 - 11 - 08
 */
public class ShiroAuthenticateRealm extends AuthenticatingRealm {

    private static final transient Logger log = LoggerFactory.getLogger(ShiroAuthenticateRealm.class);

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1. 获取 UsernamePasswordToken 对象
        UsernamePasswordToken currentUser = (UsernamePasswordToken) token;

        //2. 获取传入的用户信息
        String userName = currentUser.getUsername();
        String passWord = String.valueOf(currentUser.getPassword());

        //3. 从数据库中查询对应的用户信息,判断有关数据是否存在
        log.info("从数据库中查询有关账号数据,username: {}",userName);

        //4. 判断如果用户不存在则抛出未知用户的异常
        if("unknown".equals(userName)){
            throw new UnknownAccountException("未知的账户,请查实!");
        }

        //5. 其他异常处理
        if ("locked".equals(userName)){
            throw new LockedAccountException("用户被锁定,请核实!");
        }

        if (!"123456".equals(passWord)){
            throw new RuntimeException("密码不正确,请核实!");
        }

        //6. 根据用户情况,构建 AuthenticationInfo 对象返回,常用的对象有
        Object credentials = "123456";
        String realmName = getName();
        return new SimpleAuthenticationInfo(userName,credentials,realmName);
    }
}
