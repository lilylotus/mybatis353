DROP DATABASE IF EXISTS `mybatis`;
CREATE DATABASE `mybatis` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;


DROP TABLE IF EXISTS flower;
CREATE TABLE flower (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT ,
    name_english VARCHAR(255) NOT NULL ,
    name_chinese VARCHAR(255) NOT NULL ,
    age INT NOT NULL ,
    add_time DATETIME NOT NULL ,
    update_time DATETIME NOT NULL
) ENGINE = INNODB ;


INSERT INTO flower (name_english, name_chinese, age, add_time, update_time) values ('lily', '百合', 10, now(), now());
INSERT INTO flower (name_english, name_chinese, age, add_time, update_time) values ('rose', '玫瑰', 9, now(), now());

