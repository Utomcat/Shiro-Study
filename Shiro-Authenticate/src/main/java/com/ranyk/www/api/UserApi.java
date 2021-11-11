package com.ranyk.www.api;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CLASS_NAME: UserApi.java <br/>
 * Description: 用户操作 API <br/>
 *
 * @author ranyk <br/>
 * @version V1.0 <br/>
 * @date 2021 - 11 - 10
 */
@Controller
@RequestMapping("shiro")
public class UserApi {

    private static final transient Logger log = LoggerFactory.getLogger(UserApi.class);

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String userName, @RequestParam("password") String passWord) {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, passWord);
            token.setRememberMe(false);
            try {
                currentUser.login(token);
            } catch (AuthenticationException ae) {
                log.info("认证发生异常,异常信息为: " + ae.getMessage());
            }
        }
        return "redirect:index";
    }

    @GetMapping(value = "index")
    public String index() {
        return "index";
    }
}
