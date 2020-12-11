DROP DATABASE IF EXISTS `mybatis`;
CREATE DATABASE `mybatis` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE mybatis;

DROP TABLE IF EXISTS flower;
CREATE TABLE flower
(
    id           VARCHAR(64)  NOT NULL,
    name_english VARCHAR(255) NOT NULL,
    name_chinese VARCHAR(255) NOT NULL,
    age          INT          NOT NULL,
    add_time     DATETIME     NOT NULL,
    update_time  DATETIME     NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

DROP TABLE IF EXISTS gardener;
CREATE TABLE gardener
(
    id          VARCHAR(64)  NOT NULL,
    name        VARCHAR(255) NOT NULL,
    age         INT          NOT NULL,
    add_time    DATETIME     NOT NULL,
    update_time DATETIME     NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

DROP TABLE IF EXISTS gardener_flower;
CREATE TABLE gardener_flower
(
    id          VARCHAR(64) NOT NULL,
    flower_id   VARCHAR(64) NOT NULL,
    gardener_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

DROP TABLE IF EXISTS partner;
CREATE TABLE partner
(
    id          VARCHAR(64) NOT NULL,
    gardener_id VARCHAR(64) NOT NULL,
    partner_id  VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

DROP TABLE IF EXISTS auto_increment_id;
CREATE TABLE auto_increment_id
(
    id          INT         NOT NULL AUTO_INCREMENT,
    name        VARCHAR(64) NOT NULL,
    add_time    DATETIME    NOT NULL,
    update_time DATETIME    NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

INSERT INTO flower (id, name_english, name_chinese, age, add_time, update_time)
values ('00001', 'lily', '百合', 10, now(), now());
INSERT INTO flower (id, name_english, name_chinese, age, add_time, update_time)
values ('00002', 'rose', '玫瑰', 9, now(), now());
INSERT INTO flower (id, name_english, name_chinese, age, add_time, update_time)
values ('00003', 'daffodil', '水仙', 10, now(), now());

INSERT INTO gardener(id, name, age, add_time, update_time)
VALUES ('00001', '小李', 20, now(), now());
INSERT INTO gardener(id, name, age, add_time, update_time)
VALUES ('00002', '小红', 22, now(), now());

INSERT INTO gardener_flower(id, flower_id, gardener_id)
VALUES ('00001', '00001', '00001');
INSERT INTO gardener_flower(id, flower_id, gardener_id)
VALUES ('00002', '00002', '00001');
INSERT INTO gardener_flower(id, flower_id, gardener_id)
VALUES ('00003', '00003', '00001');

INSERT INTO partner(id, gardener_id, partner_id)
VALUES ('00001', '00001', '00002');
