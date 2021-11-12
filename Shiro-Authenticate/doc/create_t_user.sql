create table t_user
(
    id            int auto_increment comment '数据主键ID'
        primary key,
    login_name    varchar(50)  default 'admin'           not null comment '用户登录名',
    user_name     varchar(50)  default '默认'              not null comment '用户姓名',
    user_password varchar(100) default '123456'          not null comment '用户密码',
    sex           tinyint      default 2                 not null comment '性别: 0:男;1:女;2:其他;默认 2;',
    locked        tinyint(1)   default 0                 not null comment '用户是否被锁定: true:用户已经被锁定;false:用户未被锁定;默认为false',
    deleted       tinyint(1)   default 0                 not null comment '数据删除标志: true:已删除;false:未删除;默认 false;',
    create_time   timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间,默认当前时间',
    update_time   timestamp                              null comment '数据更新时间',
    deleted_time  timestamp                              null comment '数据删除时间',
    create_id     int          default 0                 not null comment '数据创建人ID',
    update_id     int                                    null comment '数据更新id',
    deleted_id    int                                    null comment '数据删除ID'
)
    comment 'shiro 框架测试用户表';

