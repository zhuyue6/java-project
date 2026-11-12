-- 创建打卡流水表
create table goal_clock_in (
    id bigint primary key  not null auto_increment,
    goal_id bigint not null,
    clock_in_time datetime not null,
    create_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp
);

-- 2. 统计老表数据，灌入新表（INSERT ... SELECT 语法）
insert into goal_clock_in (goal_id, clock_in_time) select id, clock_in_time from goal where clock_in_time is not null;