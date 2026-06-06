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