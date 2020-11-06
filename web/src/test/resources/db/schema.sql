CREATE TABLE `wechat_openid`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT,
    `openid`       varchar(64) NOT NULL COMMENT 'openid',
    `account`      int(32)     NOT NULL COMMENT '学号',
    `is_bind`      tinyint(1)  NOT NULL DEFAULT '1' COMMENT '0为未绑定，1为已绑定',
    `appid`        varchar(32) NOT NULL COMMENT '来源信息',
    `gmt_create`   timestamp   NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `gmt_modified` timestamp   NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`, `appid`),
    KEY `idx_account` (`account`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `student`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT,
    `account`      int(32)     NOT NULL,
    `password`     varchar(64) NOT NULL,
    `name`         varchar(32) NOT NULL,
    `sex`          varchar(32) NOT NULL,
    `ethnic`       varchar(32) NOT NULL,
    `urpclass_num` int(11)     NOT NULL,
    `is_correct`   tinyint(1)  NOT NULL DEFAULT '1',
    `academy_name` varchar(255)         DEFAULT NULL,
    `subject_name` varchar(255)         DEFAULT NULL,
    `class_name`   varchar(255)         DEFAULT NULL,
    `has_check`    tinyint(1)  NOT NULL DEFAULT '1',
    `gmt_create`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `pk_account` (`account`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `urp_class`
(
    `id`              int(11)      NOT NULL AUTO_INCREMENT,
    `admission_grade` varchar(255) NOT NULL COMMENT '年级',
    `class_num`       varchar(255) NOT NULL,
    `class_name`      varchar(255) NOT NULL,
    `academy_name`    varchar(255) NOT NULL,
    `academy_num`     varchar(255) NOT NULL,
    `subject_name`    varchar(255) NOT NULL,
    `subject_num`     varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `class_num` (`class_num`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `user`
(
    `id`                   int(11)      NOT NULL AUTO_INCREMENT,
    `user_name`            varchar(64)  NOT NULL,
    `nick_name`            varchar(64)  NOT NULL,
    `password`             varchar(255) NOT NULL,
    `salt`                 varchar(255) NOT NULL,
    `avatar_path`          varchar(255) NOT NULL,
    `email`                varchar(255) NOT NULL DEFAULT '',
    `mobile`               varchar(64)  NOT NULL,
    `gender`               int(16)      NOT NULL,
    `introduction`         varchar(255) NOT NULL,
    `user_type`            int(16)      NOT NULL,
    `use_default_password` tinyint(255) NOT NULL,
    `life_cycle_status`    int(16)      NOT NULL,
    `gmt_create`           timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `gmt_modify`           timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_name` (`user_name`),
    UNIQUE KEY `mobile` (`mobile`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `user_student`
(
    `id`              int(11)     NOT NULL AUTO_INCREMENT,
    `student_account` varchar(64) NOT NULL,
    `user_name`       varchar(64) NOT NULL,
    `gmt_create`      timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify`      timestamp   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_student_user_name` (`user_name`),
    UNIQUE KEY `user_student_student_account` (`student_account`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `schedule_task`
(
    `id`           int(11)      NOT NULL AUTO_INCREMENT,
    `openid`       varchar(64)           DEFAULT '0' COMMENT 'openid',
    `scene`        int(11)               DEFAULT '0' COMMENT '场景值',
    `task_count`   int(11)               DEFAULT '0' COMMENT '发送次数',
    `send_status`  tinyint(4)            DEFAULT '0' COMMENT '是否发送成功',
    `is_subscribe` tinyint(4)            DEFAULT '0' COMMENT '是否订阅',
    `appid`        varchar(50)           DEFAULT '0' COMMENT 'appid',
    `gmt_create`   timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `gmt_modify`   timestamp(3) NULL     DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改日期',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8;


CREATE TABLE `role`
(
    `id`   int(11)      NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `code` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `role_permission`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT,
    `role_id`       int(11) NOT NULL,
    `permission_id` int(11) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `permission`
(
    `id`              int(11)      NOT NULL AUTO_INCREMENT,
    `permission_name` varchar(255) NOT NULL,
    `permission_code` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `user_role`
(
    `id`         int(11)      NOT NULL AUTO_INCREMENT,
    `user_name`  varchar(255) NOT NULL,
    `role_code`  varchar(255) NOT NULL,
    `gmt_create` timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify` timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_role_name` (`user_name`, `role_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `user_register_record`
(
    `id`                bigint(20)  NOT NULL AUTO_INCREMENT,
    `appId`             varchar(64) NOT NULL,
    `openId`            varchar(64) NOT NULL,
    `phone_number`      varchar(64) NOT NULL,
    `user_name`         varchar(64) NOT NULL,
    `life_cycle_status` int(16)     NOT NULL,
    `gmt_create`        timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify`        timestamp   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `user_logout_record`
(
    `id`               bigint(20)   NOT NULL AUTO_INCREMENT,
    `logout_record_id` bigint(20)   NOT NULL,
    `logout_type`      int(16)      NOT NULL,
    `logout_reason`    varchar(255) NOT NULL,
    `operator`         varchar(64)  NOT NULL,
    `gmt_create`       timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify`       timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `wechat_openid_student`
(
    `id`      int(11)     NOT NULL AUTO_INCREMENT,
    `appid`   varchar(32) NOT NULL,
    `openid`  varchar(32) NOT NULL,
    `account` int(16)     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_bind` (`appid`, `account`),
    UNIQUE KEY `unique_bind_wechat` (`appid`, `openid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `wechat_action_record`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `appid`      varchar(32) NOT NULL,
    `openid`     varchar(32) NOT NULL,
    `account`    varchar(32) NOT NULL,
    `action`     int(16)     NOT NULL,
    `gmt_create` timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify` timestamp   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `post`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `user_name`     varchar(128) NOT NULL,
    `content`       varchar(255) NOT NULL,
    `allow_comment` tinyint(255) NOT NULL,
    `identity_code` int(255)     NOT NULL,
    `post_time`     datetime     NOT NULL,
    `record_status` int(255)     NOT NULL,
    `comment_count` int(255)     NOT NULL DEFAULT '0',
    `like_count`    int(255)     NOT NULL DEFAULT '0',
    `view_count`    int(255)     NOT NULL DEFAULT '0',
    `equipment`     varchar(255) NOT NULL DEFAULT '',
    `last_reply_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `gmt_create`    timestamp    NULL     DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify`    timestamp    NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



CREATE TABLE `post_image`
(
    `id`       bigint(20) NOT NULL AUTO_INCREMENT,
    `post_id`  bigint(20) NOT NULL,
    `image_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `image_info`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `url`           varchar(512) NOT NULL,
    `file_id`       varchar(512) NOT NULL,
    `record_status` int(20)      NOT NULL,
    `gmt_create`    timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify`    timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



CREATE TABLE `message`
(
    `id`                              bigint(20)  NOT NULL AUTO_INCREMENT,
    `sender_user_name`                varchar(64) NOT NULL,
    `sender_identity_category_code`   int(16)     NOT NULL,
    `receiver_user_name`              varchar(64) NOT NULL,
    `receiver_identity_category_code` int(16)     NOT NULL,
    `message_trigger_source_code`     int(16)     NOT NULL,
    `message_source_id`               bigint(20)  NOT NULL,
    `message_type_code`               int(16)     NOT NULL,
    `has_read`                        tinyint(4)  NOT NULL,
    `gmt_create`                      timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify`                      timestamp   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `comment`
(
    `id`               bigint(20)   NOT NULL AUTO_INCREMENT,
    `post_id`          bigint(20)   NOT NULL,
    `post_user_name`   varchar(64)  NOT NULL,
    `user_name`        varchar(255) NOT NULL,
    `content`          varchar(255) NOT NULL,
    `like_count`       int(255)     NOT NULL DEFAULT '0',
    `reply_comment_id` bigint(20)   NOT NULL,
    `reply_user_name`  varchar(64)  NOT NULL,
    `root_comment_id`  bigint(20)   NOT NULL,
    `identity_code`    int(255)     NOT NULL,
    `record_status`    int(255)     NOT NULL,
    `post_time`        datetime(6)  NOT NULL,
    `gmt_create`       timestamp    NULL     DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify`       timestamp    NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `appreciate`
(
    `id`              bigint(20)  NOT NULL AUTO_INCREMENT,
    `like_type`       int(16)     NOT NULL,
    `type_content_id` bigint(20)  NOT NULL,
    `user_name`       varchar(64) NOT NULL,
    `reply_user_name` varchar(64) NOT NULL,
    `like_time`       datetime(6) NOT NULL,
    `show`            tinyint(4)  NOT NULL,
    `gmt_create`      timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modify`      timestamp   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unq_like` (`type_content_id`, `user_name`, `like_type`, `show`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `class_course_timetable`
(
    `id`                  int(11)      NOT NULL AUTO_INCREMENT,
    `class_id`            varchar(11)  NOT NULL,
    `course_timetable_id` int(11)      NOT NULL,
    `term_year`           varchar(255) NOT NULL,
    `term_order`          int(255)     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_course` (`class_id`, `term_year`, `term_order`, `course_timetable_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `course_timetable`
(
    `id`                     int(11)     NOT NULL AUTO_INCREMENT,
    `room_name`              varchar(20)          DEFAULT NULL COMMENT '关联教室名称',
    `room_number`            varchar(20)          DEFAULT '',
    `campus_name`            varchar(16) NOT NULL DEFAULT '""' COMMENT '校区名',
    `continuing_session`     int(11)     NOT NULL DEFAULT '0' COMMENT '持续节数',
    `course_id`              varchar(16) NOT NULL DEFAULT '“”' COMMENT '课程号',
    `attend_class_teacher`   varchar(16) NOT NULL DEFAULT '""' COMMENT '任课教师',
    `student_count`          int(255)    NOT NULL DEFAULT '0' COMMENT '上课得学生数',
    `class_day`              int(11)     NOT NULL DEFAULT '0' COMMENT '星期',
    `class_order`            int(11)     NOT NULL DEFAULT '0' COMMENT '节数',
    `start_week`             int(11)     NOT NULL DEFAULT '0' COMMENT '开始周',
    `end_week`               int(11)     NOT NULL DEFAULT '0' COMMENT '结束周',
    `class_in_school_week`   varchar(24) NOT NULL DEFAULT '""' COMMENT '课程在教学周的上课标识',
    `course_sequence_number` varchar(24) NOT NULL DEFAULT '1' COMMENT '课序号',
    `week_description`       varchar(64) NOT NULL DEFAULT '""' COMMENT '周数描述，如1-5周',
    `class_distinct`         int(4)      NOT NULL DEFAULT '0' COMMENT '单、双周标识，0为正常，1为单周，2为双周',
    `term_year`              char(9)     NOT NULL DEFAULT '0' COMMENT '学年，如2018-2019',
    `term_order`             int(11)     NOT NULL DEFAULT '0' COMMENT '学期',
    `gmt_create`             datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uc_course_timetable` (`course_id`, `course_sequence_number`, `class_day`, `class_order`, `start_week`,
                                      `end_week`, `term_year`, `term_order`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `course`
(
    `id`               int(11)      NOT NULL AUTO_INCREMENT,
    `name`             varchar(255) NOT NULL,
    `num`              varchar(255) NOT NULL COMMENT '课程号',
    `course_order`     varchar(255) NOT NULL COMMENT '课序号，课程号相同时作为标识',
    `term_year`        varchar(255) NOT NULL,
    `term_order`       int(255)     NOT NULL,
    `teacher_account`  varchar(255) NOT NULL DEFAULT '',
    `teacher_name`     varchar(255) NOT NULL DEFAULT '',
    `exam_type`        varchar(255) NOT NULL DEFAULT '',
    `exam_type_code`   varchar(255) NOT NULL DEFAULT '',
    `academy_name`     varchar(255) NOT NULL DEFAULT '',
    `academy_code`     varchar(255) NOT NULL DEFAULT '',
    `course_type`      varchar(255) NOT NULL DEFAULT '',
    `course_type_code` varchar(255) NOT NULL DEFAULT '',
    `credit`           varchar(255) NOT NULL DEFAULT '' COMMENT '学分',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uc_course` (`num`, `course_order`, `term_year`, `term_order`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;