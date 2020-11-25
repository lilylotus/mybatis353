DROP DATABASE IF EXISTS `mybatis`;
CREATE DATABASE `mybatis` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE mybatis;

DROP TABLE IF EXISTS flower;
CREATE TABLE flower (
    id VARCHAR(64) NOT NULL ,
    name_english VARCHAR(255) NOT NULL ,
    name_chinese VARCHAR(255) NOT NULL ,
    age INT NOT NULL ,
    add_time DATETIME NOT NULL ,
    update_time DATETIME NOT NULL ,
    PRIMARY KEY (id)
) ENGINE = INNODB ;


INSERT INTO flower (id, name_english, name_chinese, age, add_time, update_time) values ('00001', 'lily', '百合', 10, now(), now());
INSERT INTO flower (id, name_english, name_chinese, age, add_time, update_time) values ('00002', 'rose', '玫瑰', 9, now(), now());

