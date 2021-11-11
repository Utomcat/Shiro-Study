package com.ranyk.www;

import org.junit.jupiter.api.Test;

/**
 * CLASS_NAME: Test.java <br/>
 * Description: 测试方法 <br/>
 *
 * @author ranyk <br/>
 * @version V1.0 <br/>
 * @date 2021 - 11 - 11
 */
public class TestClass {

    @Test
    public void test0() {
        String a = "";
        String[] b = new String[10];
        b[0] = a;
        System.out.println(isEmpty(b));

    }

    public boolean isEmpty(Object[] obj) {
        return null == obj || obj.length == 0;
    }

}
