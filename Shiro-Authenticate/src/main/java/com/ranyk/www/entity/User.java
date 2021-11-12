package com.ranyk.www.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * CLASS_NAME: User.java <br/>
 * Description: 用户数据表实体对象 <br/>
 *
 * @author ranyk <br/>
 * @version V1.0 <br/>
 * @date 2021 - 11 - 12
 */
@Data
@ToString
@Component
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;
    private String loginName;
    private String name;
    private String password;
    private int sex;
    private boolean locked;
    private boolean deleted;
    private Date createTime;
    private Date updateTime;
    private Date deletedTime;
    private int createId;
    private int updateId;
    private int deletedId;

}
