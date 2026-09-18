-- 创建业务库
CREATE DATABASE IF NOT EXISTS clock_in DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建业务表
create table user (
    id bigint primary key auto_increment COMMENT '用户ID',
    user_name varchar(50) not null COMMENT '用户名',
    password varchar(100) not null COMMENT '密码',
    phone varchar(11) default null COMMENT '手机号',
    age int default null COMMENT '年龄',
    sex int default 0 COMMENT '性别',
    avatar_url varchar(100) default null COMMENT '头像URL',
    create_time datetime not null default current_timestamp COMMENT '创建时间',
    update_time datetime not null default current_timestamp on update current_timestamp COMMENT '更新时间'
);

-- 创建打卡记录表
create table clock_in_record (
    id bigint primary key auto_increment COMMENT '打卡记录ID',
    user_id bigint not null COMMENT '用户ID',
    remark TEXT default null COMMENT '备注',
    clock_in_time datetime not null COMMENT '打卡时间',
    clock_out_time datetime default null COMMENT '打卡结束时间',
    create_time datetime not null default current_timestamp COMMENT '创建时间',
    update_time datetime not null default current_timestamp on update current_timestamp COMMENT '更新时间'
);

-- 创建管理员
insert into user (user_name, password, age, sex) values ('admin', '123456', 0, 0);