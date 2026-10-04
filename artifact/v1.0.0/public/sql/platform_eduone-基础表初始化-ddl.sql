CREATE TABLE `AssociationValueEntry`
(
    `id`               int NOT NULL AUTO_INCREMENT,
    `associationKey`   varchar(255) DEFAULT NULL,
    `associationValue` varchar(255) DEFAULT NULL,
    `sagaId`           varchar(255) DEFAULT NULL,
    `sagaType`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_association` (`associationKey`, `associationValue`) USING BTREE,
    KEY `idx_saga_id` (`sagaId`) USING BTREE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `DomainEventEntry`
(
    `globalIndex`         bigint       NOT NULL AUTO_INCREMENT,
    `aggregateIdentifier` varchar(255) NOT NULL,
    `sequenceNumber`      bigint       NOT NULL,
    `type`                varchar(255) DEFAULT NULL,
    `eventIdentifier`     varchar(255) NOT NULL,
    `payload`             longtext,
    `metaData`            longtext,
    `payloadRevision`     varchar(255) DEFAULT NULL,
    `payloadType`         varchar(255) NOT NULL,
    `timeStamp`           varchar(255) NOT NULL,
    PRIMARY KEY (`globalIndex`, `aggregateIdentifier`) USING BTREE,
    UNIQUE KEY `aggregateIdentifier` (`aggregateIdentifier`, `sequenceNumber`) USING BTREE,
    UNIQUE KEY `eventIdentifier` (`eventIdentifier`, `aggregateIdentifier`) USING BTREE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `SagaEntry`
(
    `sagaId`          varchar(255) NOT NULL,
    `revision`        varchar(255) DEFAULT NULL,
    `sagaType`        varchar(255) DEFAULT NULL,
    `serializedSaga`  longtext,
    `eventIdentifier` varchar(255) DEFAULT NULL,
    `metaData`        longtext,
    PRIMARY KEY (`sagaId`) USING BTREE,
    KEY `idx_eventIdentifier` (`eventIdentifier`) USING BTREE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `SnapshotEventEntry`
(
    `aggregateIdentifier` varchar(255) NOT NULL,
    `sequenceNumber`      bigint       NOT NULL,
    `type`                varchar(255) NOT NULL,
    `eventIdentifier`     varchar(255) NOT NULL,
    `payload`             longtext,
    `metaData`            longtext,
    `payloadRevision`     varchar(255) DEFAULT NULL,
    `payloadType`         varchar(255) NOT NULL,
    `timeStamp`           varchar(255) NOT NULL,
    PRIMARY KEY (`aggregateIdentifier`, `sequenceNumber`) USING BTREE,
    UNIQUE KEY `eventIdentifier` (`eventIdentifier`) USING BTREE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `TokenEntry`
(
    `processorName` varchar(255) NOT NULL,
    `segment`       int          NOT NULL,
    `token`         longtext,
    `tokenType`     varchar(255) DEFAULT NULL,
    `timestamp`     varchar(255) DEFAULT NULL,
    `owner`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`processorName`, `segment`) USING BTREE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `dd_region_tree`
(
    `id`          varchar(32)  NOT NULL COMMENT '主键ID',
    `code`        varchar(10)           DEFAULT NULL COMMENT '地区编码',
    `parent_id`   varchar(32)           DEFAULT NULL COMMENT '上级ID，-1表示顶级地区',
    `parent_code` varchar(10)           DEFAULT NULL COMMENT '上级地区code',
    `level`       int                   DEFAULT NULL COMMENT '地区层级',
    `path`        varchar(50)           DEFAULT NULL COMMENT '地区编码路径',
    `name`        varchar(20)           DEFAULT NULL COMMENT '地区名称',
    `sort`        int                   DEFAULT NULL COMMENT '地区排序',
    `status`      varchar(100) NOT NULL DEFAULT '1' COMMENT '地区状态',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_code` (`code`) USING BTREE,
    KEY `idx_parent_code` (`parent_code`) USING BTREE,
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC COMMENT ='地区树';

CREATE TABLE `dd_business_data_dictionary`
(
    `id`             varchar(32) NOT NULL COMMENT '字典ID',
    `type`           varchar(128) DEFAULT NULL COMMENT '字典类型',
    `code`           varchar(255) DEFAULT NULL COMMENT '字典编码',
    `name`           varchar(255) DEFAULT NULL COMMENT '字典名称',
    `parent_id`      varchar(32)  DEFAULT NULL COMMENT '上级字典ID',
    `status`         varchar(100) DEFAULT NULL COMMENT '字典状态',
    `sort`           int          DEFAULT NULL COMMENT '排序',
    `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime(3)  DEFAULT NULL COMMENT '更新时间',
    `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`    datetime(3)  DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_type` (`type`, `sort`) USING BTREE,
    KEY `idx_code` (`type`, `code`) USING BTREE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC COMMENT ='字典-业务数据字典';

CREATE TABLE `dd_business_data_dictionary_type`
(
    `type`           varchar(128) NOT NULL COMMENT '字典类型',
    `name`           varchar(255) DEFAULT NULL COMMENT '字典名称',
    `desc`           varchar(500) DEFAULT NULL COMMENT '字典说明',
    `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime(3)  DEFAULT NULL COMMENT '更新时间',
    `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`    datetime(3)  DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`type`) USING BTREE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC COMMENT ='字典类型';

CREATE TABLE `bds_person`
(
    `id`             varchar(32) NOT NULL COMMENT '人员ID',
    `person_no`      varchar(64)  DEFAULT NULL COMMENT '人员编号',
    `real_name`      varchar(64)  DEFAULT NULL COMMENT '真实姓名',
    `nick_name`      varchar(64)  DEFAULT NULL COMMENT '昵称',
    `gender`         varchar(100) DEFAULT NULL COMMENT '性别',
    `mobile`         varchar(20)  DEFAULT NULL COMMENT '手机号',
    `email`          varchar(128) DEFAULT NULL COMMENT '邮箱',
    `remark`         varchar(255) DEFAULT NULL COMMENT '备注',
    `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`    datetime(3)  DEFAULT NULL COMMENT '创建时间',
    `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime(3)  DEFAULT NULL COMMENT '更新时间',
    `is_deleted`     tinyint      DEFAULT '0' COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='人员信息表';

CREATE TABLE `bds_user`
(
    `id`              varchar(32) NOT NULL COMMENT '用户ID',
    `user_no`         varchar(64)  DEFAULT NULL COMMENT '用户编号',
    `person_id`       varchar(32)  DEFAULT NULL COMMENT '关联人员ID',
    `user_type`       varchar(100) DEFAULT NULL COMMENT '用户类型',
    `user_name`       varchar(64)  DEFAULT NULL COMMENT '用户名',
    `avatar`          varchar(255) DEFAULT NULL COMMENT '头像',
    `status`          varchar(100) DEFAULT NULL COMMENT '状态',
    `last_login_time` datetime(3)  DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip`   varchar(64)  DEFAULT NULL COMMENT '最后登录IP',
    `create_user_id`  varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`     datetime(3)  DEFAULT NULL COMMENT '创建时间',
    `update_user_id`  varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time`     datetime(3)  DEFAULT NULL COMMENT '更新时间',
    `is_deleted`      tinyint      DEFAULT '0' COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='用户表';

CREATE TABLE `bds_account`
(
    `id`             varchar(32) NOT NULL COMMENT '账号ID',
    `user_id`        varchar(32)  DEFAULT NULL COMMENT '关联用户ID',
    `channel`        varchar(32)  DEFAULT NULL COMMENT '注册渠道',
    `status`         varchar(32)  DEFAULT NULL COMMENT '状态',
    `expire_time`    datetime(3)  DEFAULT NULL COMMENT '账号过期时间',
    `lock_time`      datetime(3)  DEFAULT NULL COMMENT '锁定时间',
    `lock_reason`    varchar(255) DEFAULT NULL COMMENT '锁定原因',
    `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`    datetime(3)  DEFAULT NULL COMMENT '创建时间',
    `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime(3)  DEFAULT NULL COMMENT '更新时间',
    `is_deleted`     tinyint      DEFAULT '0' COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='账号信息表';

CREATE TABLE `bds_role`
(
    `id`             varchar(32) NOT NULL COMMENT '角色ID',
    `role_code`      varchar(64)  DEFAULT NULL COMMENT '角色编码',
    `role_name`      varchar(64)  DEFAULT NULL COMMENT '角色名称',
    `role_type`      varchar(32)  DEFAULT NULL COMMENT '角色类型',
    `data_scope`     varchar(32)  DEFAULT NULL COMMENT '数据范围',
    `sort`           int          DEFAULT NULL COMMENT '排序',
    `status`         varchar(32)  DEFAULT NULL COMMENT '状态',
    `description`    varchar(255) DEFAULT NULL COMMENT '描述',
    `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`    datetime(3)  DEFAULT NULL COMMENT '创建时间',
    `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime(3)  DEFAULT NULL COMMENT '更新时间',
    `is_deleted`     tinyint      DEFAULT '0' COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='角色表';

CREATE TABLE `bds_account_role`
(
    `id`          varchar(32) NOT NULL COMMENT '主键',
    `account_id`  varchar(32)  DEFAULT NULL COMMENT '账号ID',
    `role_id`     varchar(32)  DEFAULT NULL COMMENT '角色ID',
    `grant_by`    varchar(32)  DEFAULT NULL COMMENT '授权人',
    `grant_time`  datetime(3)  DEFAULT NULL COMMENT '授权时间',
    `expire_time` datetime(3)  DEFAULT NULL COMMENT '授权过期时间',
    `status`      varchar(100) DEFAULT '1' COMMENT '状态',
    `create_time` datetime(3)  DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(3)  DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='账号角色关联表';

CREATE TABLE `bds_account_auth`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `account_id`     varchar(32)  DEFAULT NULL COMMENT '账号ID',
    `auth_type`      varchar(32)  DEFAULT NULL COMMENT '认证类型',
    `auth_key`       varchar(128) DEFAULT NULL COMMENT '认证标识',
    `auth_secret`    varchar(255) DEFAULT NULL COMMENT '认证凭据',
    `fail_count`     int          DEFAULT NULL COMMENT '连续失败次数',
    `max_fail_count` int          DEFAULT NULL COMMENT '最大失败次数',
    `lock_until`     datetime(3)  DEFAULT NULL COMMENT '锁定截止时间',
    `last_auth_time` datetime(3)  DEFAULT NULL COMMENT '最后认证时间',
    `last_auth_ip`   varchar(64)  DEFAULT NULL COMMENT '最后认证IP',
    `status`         varchar(100) DEFAULT NULL COMMENT '状态',
    `expire_time`    datetime(3)  DEFAULT NULL COMMENT '凭据过期时间',
    `create_time`    datetime(3)  DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime(3)  DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='账号认证信息表';

CREATE TABLE `bds_auth_refresh_tokens`
(
    `id`           varchar(36) NOT NULL COMMENT '主键',
    `token_hash`   varchar(64)  DEFAULT NULL COMMENT 'RefreshToken的SHA-256哈希',
    `user_id`      varchar(32)  DEFAULT NULL COMMENT '所属用户 ID',
    `expires_time` datetime(3)  DEFAULT NULL COMMENT '过期时间',
    `used`         tinyint      DEFAULT '0' COMMENT '是否已被轮换使用',
    `used_time`    datetime(3)  DEFAULT NULL COMMENT '被轮换使用的时间',
    `created_time` datetime(3)  DEFAULT NULL COMMENT '创建时间',
    `revoked`      tinyint      DEFAULT NULL COMMENT '是否已撤销',
    `revoked_time` datetime(3)  DEFAULT NULL COMMENT '撤销时间',
    `replaced_by`  varchar(36)  DEFAULT NULL COMMENT '轮换后的新Token ID，用于追踪Token家族',
    `device_id`    varchar(128) DEFAULT NULL COMMENT '设备标识',
    `user_agent`   varchar(512) DEFAULT NULL COMMENT '客户端 UA',
    `ip_address`   varchar(45)  DEFAULT NULL COMMENT '客户端 IP',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_token_hash` (`token_hash`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_token_hash` (`token_hash`)
) ENGINE = InnoDB COMMENT ='认证刷新令牌记录表';