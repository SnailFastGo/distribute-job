
create table XXL_JOB_QRTZ_JOB_DETAILS
  (
    sched_name varchar(120) not null,
    job_name  varchar(200) not null,
    job_group varchar(200) not null,
    description varchar(250) null,
    job_class_name   varchar(250) not null,
    is_durable varchar(1) not null,
    is_nonconcurrent varchar(1) not null,
    is_update_data varchar(1) not null,
    requests_recovery varchar(1) not null,
    job_data blob null
);

create table XXL_JOB_QRTZ_TRIGGERS
  (
    sched_name varchar(120) not null,
    trigger_name varchar(200) not null,
    trigger_group varchar(200) not null,
    job_name  varchar(200) not null,
    job_group varchar(200) not null,
    description varchar(250) null,
    next_fire_time bigint(13) null,
    prev_fire_time bigint(13) null,
    priority integer null,
    trigger_state varchar(16) not null,
    trigger_type varchar(8) not null,
    start_time bigint(13) not null,
    end_time bigint(13) null,
    calendar_name varchar(200) null,
    misfire_instr smallint(2) null,
    job_data blob null
);

create table XXL_JOB_QRTZ_SIMPLE_TRIGGERS
  (
    sched_name varchar(120) not null,
    trigger_name varchar(200) not null,
    trigger_group varchar(200) not null,
    repeat_count bigint(7) not null,
    repeat_interval bigint(12) not null,
    times_triggered bigint(10) not null
);

create table XXL_JOB_QRTZ_CRON_TRIGGERS
  (
    sched_name varchar(120) not null,
    trigger_name varchar(200) not null,
    trigger_group varchar(200) not null,
    cron_expression varchar(200) not null,
    time_zone_id varchar(80)
);

create table XXL_JOB_QRTZ_SIMPROP_TRIGGERS
  (          
    sched_name varchar(120) not null,
    trigger_name varchar(200) not null,
    trigger_group varchar(200) not null,
    str_prop_1 varchar(512) null,
    str_prop_2 varchar(512) null,
    str_prop_3 varchar(512) null,
    int_prop_1 int null,
    int_prop_2 int null,
    long_prop_1 bigint null,
    long_prop_2 bigint null,
    dec_prop_1 numeric(13,4) null,
    dec_prop_2 numeric(13,4) null,
    bool_prop_1 varchar(1) null,
    bool_prop_2 varchar(1) null
);

create table XXL_JOB_QRTZ_BLOB_TRIGGERS
  (
    sched_name varchar(120) not null,
    trigger_name varchar(200) not null,
    trigger_group varchar(200) not null,
    blob_data blob null
);

create table XXL_JOB_QRTZ_CALENDARS
  (
    sched_name varchar(120) not null,
    calendar_name  varchar(200) not null,
    calendar blob not null
);

create table XXL_JOB_QRTZ_PAUSED_TRIGGER_GRPS
  (
    sched_name varchar(120) not null,
    trigger_group  varchar(200) not null
);

create table XXL_JOB_QRTZ_FIRED_TRIGGERS
  (
    sched_name varchar(120) not null,
    entry_id varchar(95) not null,
    trigger_name varchar(200) not null,
    trigger_group varchar(200) not null,
    instance_name varchar(200) not null,
    fired_time bigint(13) not null,
    sched_time bigint(13) not null,
    priority integer not null,
    state varchar(16) not null,
    job_name varchar(200) null,
    job_group varchar(200) null,
    is_nonconcurrent varchar(1) null,
    requests_recovery varchar(1) null
);

create table XXL_JOB_QRTZ_SCHEDULER_STATE
  (
    sched_name varchar(120) not null,
    instance_name varchar(200) not null,
    last_checkin_time bigint(13) not null,
    checkin_interval bigint(13) not null
);

create table XXL_JOB_QRTZ_LOCKS
  (
    sched_name varchar(120) not null,
    lock_name  varchar(40) not null
);



create table `XXL_JOB_QRTZ_TRIGGER_INFO` (
  `id` int(11) not null auto_increment,
  `job_group` int(11) not null comment '执行器主键id',
  `job_cron` varchar(128) not null comment '任务执行cron',
  `job_desc` varchar(255) not null,
  `add_time` datetime default null,
  `update_time` datetime default null,
  `author` varchar(64) default null comment '作者',
  `alarm_email` varchar(255) default null comment '报警邮件',
  `executor_route_strategy` varchar(50) default null comment '执行器路由策略',
  `executor_handler` varchar(255) default null comment '执行器任务handler',
  `executor_param` varchar(255) default null comment '执行器任务参数',
  `executor_block_strategy` varchar(50) default null comment '阻塞处理策略',
  `executor_fail_strategy` varchar(50) default null comment '失败处理策略',
  `glue_type` varchar(50) not null comment 'glue类型',
  `glue_source` text comment 'glue源代码',
  `glue_remark` varchar(128) default null comment 'glue备注',
  `glue_updatetime` datetime default null comment 'glue更新时间',
  `child_jobkey` varchar(255) default null comment '子任务key',
  primary key (`id`)
) engine=innodb default charset=utf8;

create table `XXL_JOB_QRTZ_TRIGGER_LOG` (
  `id` int(11) not null auto_increment,
  `job_group` int(11) not null comment '执行器主键id',
  `job_id` int(11) not null comment '任务，主键id',
  `glue_type` varchar(50) default null comment 'glue类型',
  `executor_address` varchar(255) default null comment '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) default null comment '执行器任务handler',
  `executor_param` varchar(255) default null comment 'executor_param',
  `trigger_time` datetime default null comment '调度-时间',
  `trigger_code` varchar(255) not null default '0' comment '调度-结果',
  `trigger_msg` varchar(2048) default null comment '调度-日志',
  `handle_time` datetime default null comment '执行-时间',
  `handle_code` varchar(255) not null default '0' comment '执行-状态',
  `handle_msg` varchar(2048) default null comment '执行-日志',
  primary key (`id`)
) engine=innodb default charset=utf8;

create table `XXL_JOB_QRTZ_TRIGGER_LOGGLUE` (
  `id` int(11) not null auto_increment,
  `job_id` int(11) not null comment '任务，主键id',
  `glue_type` varchar(50) default null comment 'glue类型',
  `glue_source` text comment 'glue源代码',
  `glue_remark` varchar(128) not null comment 'glue备注',
  `add_time` timestamp null default null,
  `update_time` timestamp null default null on update current_timestamp,
  primary key (`id`)
) engine=innodb default charset=utf8;

create table XXL_JOB_QRTZ_TRIGGER_REGISTRY (
  `id` int(11) not null auto_increment,
  `registry_group` varchar(255) not null,
  `registry_key` varchar(255) not null,
  `registry_value` varchar(255) not null,
  `update_time` timestamp not null default current_timestamp,
  primary key (`id`)
) engine=innodb default charset=utf8;

create table `XXL_JOB_QRTZ_TRIGGER_GROUP` (
  `id` int(11) not null auto_increment,
  `app_name` varchar(64) not null comment '执行器appname',
  `title` varchar(12) not null comment '执行器名称',
  `order` tinyint(4) not null default '0' comment '排序',
  `address_type` tinyint(4) not null default '0' comment '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` varchar(200) default null comment '执行器地址列表，多地址逗号分隔',
  primary key (`id`)
) engine=innodb default charset=utf8;

insert into `XXL_JOB_QRTZ_TRIGGER_GROUP` ( `app_name`, `title`, `order`, `address_type`, `address_list`) values ( 'xxl-job-executor-example', '示例执行器', '1', '0', null);

commit;

