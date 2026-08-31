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
    `status`         varchar(100) DEFAULT NULL COMMENT '字典状态',
    `sort`           int          DEFAULT NULL COMMENT '排序',
    `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人用户ID',
    `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
    `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人用户ID',
    `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',
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
    `update_user_id` varchar(32)  DEFAULT NULL COMMENT '更新人用户ID',
    `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
    `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人用户ID',
    `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`type`) USING BTREE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC COMMENT ='字典类型';