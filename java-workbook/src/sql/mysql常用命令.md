# 数据库相关mysql
```sql
  -- 连接数据库
  mysql -uroot -p
  -- 查看数据库
  show databases;
  -- 使用数据库
  use XXXX;
  -- 查看当前数据库
  select database();
  -- 创建数据库，不存在才创建
  create database if not exists xxxx default character set utf8mb4 default collate utf8mb4_unicode_ci; 
  -- 删除数据库
  drop database if exists xxxx;
```

# 表操作
```sql
  -- 创建表
  -- PRIMARY 主键
  -- auto_increment 自增id
  -- unique 唯一键
  -- not null 不能为空
  -- default 默认值
  -- CURRENT_TIMESTAMP 当前时间
  create table admin_user (
    id bigint primary key auto_increment COMMENT '管理员ID',
    user_name varchar(50) not null unique COMMENT '管理员用户名'
  );

  -- 查看表结构
  desc admin_user;

  -- 修改表名
  alter table admin_user rename to ad_user;

  -- 添加字段
  alter table admin_user add email varchar(100) after id;

  -- 修改字段类型/约束
  alter table admin_user modify email int;

  -- 修改字段名
  alter table admin_user change email email_u varchar(100);

  -- 删除字段名
  alter table admin_user drop email;

  -- 删除表
  drop table IF EXISTS admin_user;
```

# 常用CRUD
```sql
  -- 插入
  insert into admin_user(name, age) values ('小米', 10);
  -- 批量插入
  insert into admin_user(name, age) values ('李四',19),('王五',20);

  -- 查询
  -- 查询全部
  select * from admin_user;
  -- 指定列
  select id, name from admin_user;
  -- 指定条件匹配
  select * from admin_user where age=18 and name='张三';
  -- 模糊查询
  select * from admin_user where name LIKE '%张%';
  
  -- 排序 desc 降序，asc 升序
  select * from admin_user order by age desc;
  
  -- 分页 limit offset, size
  select * from admin_user limit 10, 20; 

  -- 更新
  update admin_user set age=21 where id=1;

  -- 更新一列替换另一列
  update admin_use set age=password;
  
  -- 删除
  delete from admin_user where id=1;
```

# 聚会&分组
```sql
   -- 总条数
   select count(*) from admin_user; 
   -- 
```

# 多表联查
```sql
  -- 内连接：只有两边匹配到的数据才显示
  select * from admin_user au inner join user u on au.id == u.admin_id;
  -- 左连接：左表全部保留，右表匹配不到显示null
  select * from admin_user left join user on admin_user.id == user.admin_id;
  -- 右连接：右边表全部保留，左边匹配不到填 NULL
  select * from admin_user right join user on admin_user.id == user.admin_id;
```