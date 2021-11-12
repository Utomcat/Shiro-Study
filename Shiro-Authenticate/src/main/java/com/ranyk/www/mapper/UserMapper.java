package com.ranyk.www.mapper;

import com.ranyk.www.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * CLASS_NAME: UserMapper.java <br/>
 * Description: 用户数据库操作 Mapper 接口 <br/>
 *
 * @author ranyk <br/>
 * @version V1.0 <br/>
 * @date 2021 - 11 - 12
 */
public interface UserMapper {

    /**
     * 通过登录用户名查询用户信息
     *
     * @param login 需要查询的用户名
     * @return 返回查询到的用户对象 {@link User}
     */
    User selectUserByLogin(@Param("login") String login);

}
