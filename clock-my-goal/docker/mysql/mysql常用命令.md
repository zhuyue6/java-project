# 数据库相关mysql
```sql
  -- 连接数据库
  mysql -uroot -p
  -- 查看数据库
  show databases;
  -- 使用数据库
  use XXXX;
  -- 创建数据库，不存在才创建
  create database if not exists xxxx default character set utf8mb4 default collate utf8mb4_unicode_ci; 
  -- 删除数据库
  drop database if exists xxxx;
```