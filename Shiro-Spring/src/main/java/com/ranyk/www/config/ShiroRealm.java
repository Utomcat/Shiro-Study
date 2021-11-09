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
