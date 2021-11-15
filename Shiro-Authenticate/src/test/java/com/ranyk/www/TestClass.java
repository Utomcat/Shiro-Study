package com.ranyk.www;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;

/**
 * CLASS_NAME: Test.java <br/>
 * Description: 测试方法 <br/>
 *
 * @author ranyk <br/>
 * @version V1.0 <br/>
 * @date 2021 - 11 - 11
 */
@Slf4j
public class TestClass {

    @Test
    public void test0() {
        String a = "";
        String[] b = new String[10];
        b[0] = a;
        System.out.println(isEmpty(b));

    }

    /**
     * 使用 MD5 进行对指定字符串进行加密,同时加密 1024 次
     */
    @Test
    public void test1() {
        String hashAlgorithmName = "MD5";
        Object credentials = "123456";
        Object salt = "admin";
        int hashIterations = 1024;
        SimpleHash hash = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        log.info("" + hash);
    }

    /**
     * 使用 SHA-256 密码加密,同时加密 1024 次
     */
    @Test
    public void test2() {
        String hashAlgorithmName = "SHA-256";
        Object credentials = "123456";
        Object salt = "admin";
        int hashIterations = 1024;
        SimpleHash hash = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        log.info("" + hash);
    }

    public boolean isEmpty(Object[] obj) {
        return null == obj || obj.length == 0;
    }

}
