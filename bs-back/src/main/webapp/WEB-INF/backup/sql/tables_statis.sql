DROP TABLE IF EXISTS t_log_day;
CREATE TABLE `t_log_day` (
  `day`     DATE                NOT NULL,
  `book_id` BIGINT(18) UNSIGNED NOT NULL DEFAULT '0',
  `amount`  INT(11) UNSIGNED DEFAULT '0'
)
  ENGINE =INFINIDB
  DEFAULT CHARSET =utf8;

DROP TABLE IF EXISTS t_log_book;
CREATE TABLE `t_log_book` (
  `book_id` BIGINT(18) UNSIGNED  NOT NULL DEFAULT '0',
  `type_id` INT(11) UNSIGNED     NOT NULL DEFAULT '0'
  COMMENT '分类id',
  `type`    TINYINT(1)  UNSIGNED NOT NULL DEFAULT '0'
  COMMENT '分类,1:tag,2:category',
  `amount`  INT(11) UNSIGNED     NOT NULL DEFAULT '0'
)
  ENGINE =INFINIDB
  DEFAULT CHARSET =utf8;

/**
DROP TABLE IF EXISTS t_log_day;
CREATE TABLE `t_log_day` (
  `day`     DATE  NOT NULL,
  `book_id` BIGINT(18) NOT NULL,
  `amount`  INT(8) DEFAULT NULL,
  PRIMARY KEY (`day`, `book_id`)
)
  ENGINE =MyISAM
  DEFAULT CHARSET =utf8;

DROP TABLE IF EXISTS t_log_book;
CREATE TABLE `t_log_book` (
  `book_id` BIGINT(18) NOT NULL,
  `type_id` INT(11) UNSIGNED     NOT NULL DEFAULT '0'
  COMMENT '分类id',
  `type`        TINYINT(1)  UNSIGNED NOT NULL DEFAULT '0'
  COMMENT '分类,1:tag,2:category',
  `amount`      INT(11) UNSIGNED     NOT NULL DEFAULT '0',
  PRIMARY KEY (`book_id`)
)
  ENGINE =MyISAM
  DEFAULT CHARSET =utf8;
 */