-- 用户表
-- auto-generated definition
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(256)                       null comment '用户姓名',
    idNumber     varchar(256)                       null comment '身份证号',
    userAccount  varchar(256)                       null comment '登录账号',
    avatarUrl    varchar(1024)                      null comment '头像',
    userPassword varchar(256)                       null comment '密码',
    gender       tinyint                            null comment '性别',
    age          int                                null comment '年龄',
    phone        varchar(256)                       null comment '电话',
    email        varchar(256)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态(默认0：正常 1：在岗 2：离职)',
    department   varchar(256)                       null comment '科室',
    position     varchar(256)                       null comment '职位',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '逻辑删除（1：删除）',
    userRole     int                                null comment '用户角色（0：管理员 1：老人 2：护士 3：医生）'
)
    comment '用户表';

