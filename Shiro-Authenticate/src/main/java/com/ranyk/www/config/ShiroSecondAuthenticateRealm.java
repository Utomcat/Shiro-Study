package com.ranyk.www.config;

import com.ranyk.www.entity.User;
import com.ranyk.www.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * CLASS_NAME: ShiroRealm.java <br/>
 * Description: Shiro Realm 配置 <br/>
 *
 * @author ranyk <br/>
 * @version V1.0 <br/>
 * @date 2021 - 11 - 08
 */
@SuppressWarnings("all")
public class ShiroSecondAuthenticateRealm extends AuthenticatingRealm {

    @SuppressWarnings("unused")
    private static final transient Logger log = LoggerFactory.getLogger(ShiroSecondAuthenticateRealm.class);

    @Resource
    private UserMapper userMapper;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("Second Realm doGetAuthenticationInfo");

        //1. 获取 UsernamePasswordToken 对象
        UsernamePasswordToken currentUser = (UsernamePasswordToken) token;

        //2. 获取传入的用户信息
        String userName = currentUser.getUsername();

        //3. 从数据库中查询对应的用户信息,判断有关数据是否存在
        User user = userMapper.selectUserByLogin(userName);

        //4. 判断如果用户不存在则抛出未知用户的异常
        if(null == user){
            throw new UnknownAccountException("未知的账户,请查实!");
        }

        //5. 其他异常处理
        if (user.isLocked()){
            throw new LockedAccountException("用户被锁定,请核实!");
        }

        //6. 根据用户情况,构建 AuthenticationInfo 对象返回,常用的对象有
        return new SimpleAuthenticationInfo(userName,user.getShaPassword(), ByteSource.Util.bytes(userName),getName());
    }
}
