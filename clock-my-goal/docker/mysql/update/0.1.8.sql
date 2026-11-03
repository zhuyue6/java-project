alter table clock_my_goal.goal add clock_in_cycle_count int not null default 0 after target_clock_in_count, COMMENT '时间周期已打卡计数';
alter table clock_my_goal.goal add target_clock_in_cycle_count int not null default 1 after clock_in_cycle_count, COMMENT '目标打卡时间周期总计数';
alter table clock_my_goal.goal add start_check_time time default null default '00:00:00' after target_clock_in_cycle_count, COMMENT '可打卡段开始时间';
alter table clock_my_goal.goal add end_check_time time default null default '23:59:59' after start_check_time, COMMENT '可打卡段结束时间';

alter table clock_my_goal.goal add unique key uk_userid_goalname (user_id, goal_name);