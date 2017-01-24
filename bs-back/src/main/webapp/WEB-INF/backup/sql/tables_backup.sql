-- -----------------------------------------------------
-- Schema rs
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `t_book`
-- -----------------------------------------------------
/*
DROP TABLE IF EXISTS `t_book`;

CREATE TABLE IF NOT EXISTS `t_book` (
  `id`                   BIGINT(18) UNSIGNED    NOT NULL AUTO_INCREMENT COMMENT '书籍id',
  `cp_book_id`           VARCHAR(32)            NOT NULL,
  `name`                 VARCHAR(200)           NOT NULL,
  `author`               VARCHAR(32)            NOT NULL,
  `category_ids`         VARCHAR(32)            NULL
  COMMENT '分类id，通过逗号分隔',
  `is_serial`            TINYINT(1) UNSIGNED    NOT NULL DEFAULT 0,
  `words`                INT(11) UNSIGNED       NULL DEFAULT 0,
  `chapters`             INT(11) UNSIGNED       NOT NULL DEFAULT 0
  COMMENT '章节数',
  `publish_chapters`     INT(11) UNSIGNED       NOT NULL DEFAULT 0
  COMMENT '已发布的章节数',
  `update_chapter_date`  TIMESTAMP              NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '最新章节更新时间',
  `update_chapter_id`    BIGINT(18) UNSIGNED    NULL DEFAULT 0,
  `update_chapter`       VARCHAR(200) DEFAULT NULL,
  `small_pic`            VARCHAR(200)           NOT NULL,
  `large_pic`            VARCHAR(200)           NOT NULL,
  `online_date`          DATE                   NULL DEFAULT '0000-00-00',
  `offline_date`         DATE                   NULL DEFAULT '0000-00-00',
  `memo`                 VARCHAR(5000)          NOT NULL
  COMMENT '简介',
  `check_level`          TINYINT(1) UNSIGNED    NULL DEFAULT 0
  COMMENT '审核等级，15：正常上线状态，具备所有功能；10：可阅读，可搜索，可加入书架，不可人工运营（包括栏目、专题、书单、广告位、模版管理、页面配置），不可自动运营（包括排行榜、关联推荐、分类淘书）；9：可阅读，不可搜索，不可加入书架，不可人工运营（包括栏目、专题、书单、广告位、模版管理、页面配置），不可自动运营（包括排行榜、关联推荐、分类淘书）；下线：不具备任何功能，只保存在管理库中',
  `tag_classify_id`      INT(11) UNSIGNED       NULL DEFAULT 0,
  `tag_sex_id`           INT(11) UNSIGNED       NULL DEFAULT 0,
  `tag_content_ids`      VARCHAR(32)            NULL,
  `tag_supply_ids`       VARCHAR(32)            NULL,
  `short_recommend`      VARCHAR(100)           NULL
  COMMENT '短推推荐语',
  `long_recommend`       VARCHAR(200)           NULL
  COMMENT '长推荐语',
  `provider_id`          INT(11) UNSIGNED       NOT NULL DEFAULT 0
  COMMENT '版权方id',
  `isbn`                 VARCHAR(32)            NULL,
  `batch_id`             INT(11) UNSIGNED       NOT NULL DEFAULT 0,
  `is_fee`               TINYINT(1) UNSIGNED    NULL DEFAULT 0,
  `is_whole_fee`         TINYINT(1) UNSIGNED    NULL DEFAULT 0
  COMMENT '是否全本收费',
  `operate_platform_ids` VARCHAR(100)           NULL,
  `fee_platform_ids`     VARCHAR(100)           NULL,
  `fee_type_ids`         VARCHAR(100)           NULL,
  `fee_chapter`          INT(11) UNSIGNED       NULL DEFAULT 0
  COMMENT '收费起始章节',
  `day_publish_chapters` INT(11) UNSIGNED       NULL DEFAULT 0
  COMMENT '每日发布章数',
  `whole_price`          DECIMAL(5, 2) UNSIGNED NULL DEFAULT 0,
  `thousand_words_price` DECIMAL(5, 2) UNSIGNED NULL DEFAULT 0,
  `status`               TINYINT(1) UNSIGNED    NULL DEFAULT 0
  COMMENT '状态',
  `create_date`          TIMESTAMP              NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id`           INT(11) UNSIGNED       NULL DEFAULT 0,
  `edit_date`            TIMESTAMP              NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id`            INT(11) UNSIGNED       NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `provider` (`provider_id` ASC),
  INDEX `batch` (`batch_id` ASC),
  INDEX `serial` (`is_serial` ASC))
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '书籍表';


-- -----------------------------------------------------
-- Table `t_chapter`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_chapter`;

CREATE TABLE IF NOT EXISTS `t_chapter` (
  `id`             BIGINT(18) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `cp_chapter_id`  VARCHAR(32)            NOT NULL,
  `name`           VARCHAR(100)           NOT NULL,
  `volume`         VARCHAR(100)           NULL,
  `book_id`        BIGINT(18) UNSIGNED    NOT NULL DEFAULT 0,
  `order_id`       INT(11) UNSIGNED       NOT NULL DEFAULT 0,
  `is_fee`         TINYINT(1) UNSIGNED    NULL DEFAULT 0,
  `price`          DECIMAL(5, 2) UNSIGNED NULL DEFAULT 0,
  `words`          INT(11) UNSIGNED       NOT NULL DEFAULT 0,
  `filtered_words` INT(11) UNSIGNED       NOT NULL DEFAULT 0,
  `filter_words`   VARCHAR(300)           NULL,
  `publish_date`   TIMESTAMP              NULL DEFAULT '0000-00-00 00:00:00',
  `status`         TINYINT(1) UNSIGNED    NULL DEFAULT 0 COMMENT '状态,0入库；1已审核；2上线；3下线',
  `create_date`    TIMESTAMP              NOT NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id`     INT(11) UNSIGNED       NULL DEFAULT 0,
  `edit_date`      TIMESTAMP              NOT NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id`      INT(11) UNSIGNED       NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '章节表';


-- -----------------------------------------------------
-- Table `t_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_category` ;

CREATE TABLE IF NOT EXISTS `t_category` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `parent_id` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类表',
  `is_leaf` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `logo` VARCHAR(200) NULL,
  `recommend` VARCHAR(200) NULL,
  `order_id` VARCHAR(45) NOT NULL DEFAULT 0,
  `is_use` TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
  `book_count` INT(1) UNSIGNED NOT NULL DEFAULT 0,
  `tag_classify_id` INT(11) UNSIGNED NOT NULL DEFAULT 1 COMMENT '类别属性标签',
  `tag_sex_id` INT(11) UNSIGNED NOT NULL DEFAULT 1 COMMENT '性别属性标签',
  `tag_content_ids` VARCHAR(200) NULL COMMENT '内容属性标签，以逗号开始和结束',
  `tag_supply_ids` VARCHAR(200) NULL COMMENT '补充属性标签，以逗号开始和结束',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '分类表';

*/

-- -----------------------------------------------------
-- Table `t_category_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_category_book`;

CREATE TABLE IF NOT EXISTS `t_category_book` (
  `id`          BIGINT(18) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_id` INT(11) UNSIGNED    NOT NULL DEFAULT 0,
  `book_id`     BIGINT(18) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '书籍分类关联表';

-- -----------------------------------------------------
-- Table `t_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_tag` ;

CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `parent_id` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '标签表',
  `is_leaf` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `type_id` TINYINT(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '1：类型属性，2：性别属性，3：内容属性，4：补充属性',
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 1,
  `book_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `scope` VARCHAR(32) NULL,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '标签表';

-- -----------------------------------------------------
-- Table `t_tag_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_tag_book`;

CREATE TABLE IF NOT EXISTS `t_tag_book` (
  `id`      BIGINT(18) UNSIGNED NOT NULL AUTO_INCREMENT,
  `type_id` TINYINT(1) UNSIGNED NOT NULL DEFAULT 1
  COMMENT '1：类型属性，2：性别属性，3：内容属性，4：补充属性',
  `tag_id`  INT(11) UNSIGNED    NOT NULL DEFAULT 0,
  `book_id` BIGINT(18) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '书籍标签关联表';


-- -----------------------------------------------------
-- Table `t_provider`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_provider` ;

CREATE TABLE IF NOT EXISTS `t_provider` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `ip` VARCHAR(100) NULL,
  `secret_key` VARCHAR(32) NULL,
  `book_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `online_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `batch_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `status` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '版权表';


-- -----------------------------------------------------
-- Table `t_batch`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_batch` ;

CREATE TABLE IF NOT EXISTS `t_batch` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `contract_id` VARCHAR(32) NULL,
  `provider_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `platform_ids` VARCHAR(32) NULL,
  `authorize_start_date` DATE NULL DEFAULT '0000-00-00',
  `authorize_end_date` DATE NULL DEFAULT '0000-00-00',
  `book_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `save_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `online_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `offline_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `del_count` INT(11) UNSIGNED NULL DEFAULT 0,
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 0 COMMENT '版权批次表',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
   divide   INT(3) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '版权批次表';;


-- -----------------------------------------------------
-- Table `t_batch_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_batch_book`;

CREATE TABLE IF NOT EXISTS `t_batch_book` (
  `id`          BIGINT(18) UNSIGNED NOT NULL AUTO_INCREMENT,
  `batch_id`    INT(11) UNSIGNED    NOT NULL DEFAULT 0,
  `cp_book_id`  VARCHAR(32)         NOT NULL,
  `provider_id` INT(11) UNSIGNED    NOT NULL DEFAULT 0
  COMMENT '版权方id',
  `book_name`   VARCHAR(200)        NOT NULL,
  `author`      VARCHAR(32)         NOT NULL,
  `create_date` TIMESTAMP           NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id`  INT(11) UNSIGNED    NULL DEFAULT 0,
  `edit_date`   TIMESTAMP           NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id`   INT(11) UNSIGNED    NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `batch` (`batch_id`, `cp_book_id`, `provider_id`))
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '批次书单表';


-- -----------------------------------------------------
-- Table `t_comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_comment` ;

CREATE TABLE IF NOT EXISTS `t_comment` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `book_id` BIGINT(18) UNSIGNED NULL DEFAULT 0,
  `platform_id` INT(11) UNSIGNED NULL DEFAULT 0 COMMENT '平台id',
  `user_id` BIGINT(18) UNSIGNED NULL DEFAULT 0,
  `content` VARCHAR(200) NULL,
  `is_top` TINYINT(1) UNSIGNED NULL DEFAULT 0 COMMENT '是否置顶，每本书最多三个',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '书籍评论表';;


-- -----------------------------------------------------
-- Table `t_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_group` ;

CREATE TABLE IF NOT EXISTS `t_group` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `recommend` VARCHAR(200) NULL,
  `logo` VARCHAR(200) NULL,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '书单表';


-- -----------------------------------------------------
-- Table `t_group_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_group_book` ;

CREATE TABLE IF NOT EXISTS `t_group_book` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `group_id` INT(11) UNSIGNED NOT NULL DEFAULT 0,
  `book_id` BIGINT(18) UNSIGNED NOT NULL DEFAULT 0,
  `order_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '书单书籍关联表';


-- -----------------------------------------------------
-- Table `t_component_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_component_type` ;

CREATE TABLE IF NOT EXISTS `t_component_type` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `is_use` TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
  `data_limit` INT(11) UNSIGNED NOT NULL DEFAULT 0,
  `data_group` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `template` VARCHAR(200) NULL,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '组件类型表';


-- -----------------------------------------------------
-- Table `t_component`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_component` ;

CREATE TABLE IF NOT EXISTS `t_component` (
  `id` INT(11) UNSIGNED NOT NULL,
  `container_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `name` VARCHAR(32) NOT NULL,
  `is_use` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `status` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `type_id` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `order_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `memo` VARCHAR(32) NULL,
  `title` VARCHAR(32) NULL,
  `entry_title` VARCHAR(32) NULL,
  `entry_data_type` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `entry_data` VARCHAR(200) NULL,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '组件表';


-- -----------------------------------------------------
-- Table `t_component_data_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_component_data_group` ;

CREATE TABLE IF NOT EXISTS `t_component_data_group` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(32) NULL,
  `component_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `order_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '组件数据分组表';


-- -----------------------------------------------------
-- Table `t_component_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_component_data` ;

CREATE TABLE IF NOT EXISTS `t_component_data` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `component_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `group_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `data_type` TINYINT(1) UNSIGNED NULL DEFAULT 0 COMMENT '目标类型，0无，1分类，2页面，3书籍，4url',
  `data` VARCHAR(32) NULL,
  `logo` VARCHAR(200) NULL,
  `title` VARCHAR(32) NULL,
  `memo` VARCHAR(32) NULL,
  `order_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '组件数据表';


-- -----------------------------------------------------
-- Table `t_container`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_container` ;

CREATE TABLE IF NOT EXISTS `t_container` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NULL,
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `status` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '页面表';


-- -----------------------------------------------------
-- Table `t_site`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_site` ;

CREATE TABLE IF NOT EXISTS `t_site` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NULL,
  `home_container_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `platform_id` INT(11) UNSIGNED NULL DEFAULT 0 COMMENT '平台id',
  `version` varchar(32) NOT NULL COMMENT '版本号，一个',
  `ditch_id` INT(11) UNSIGNED NULL DEFAULT 0 COMMENT '渠道号，',
  `status` tinyint(1) DEFAULT '0' COMMENT '0下线，1上线',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '站点表';


-- ----------------------------
-- Table structure for `t_client_shelf`
-- ----------------------------
DROP TABLE IF EXISTS `t_client_shelf`;
CREATE TABLE `t_client_shelf` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `bookIds` varchar(200) NOT NULL COMMENT '预置书id,多本书用;分隔,最多9本',
  `chapters` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '预置章节数，1-10',
  `platform_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '平台id',
  `version` varchar(32) NOT NULL COMMENT '版本号，一个',
  `is_use_ditch` tinyint(1) DEFAULT '0' COMMENT '0否，1是',
  `ditch_ids` varchar(200) DEFAULT NULL COMMENT '渠道号，通过;分隔',
  `status` tinyint(1) DEFAULT '0' COMMENT '0下线，1上线',
  `create_date` varchar(20) DEFAULT '0000-00-00 00:00:00',
  `creator_id` int(11) unsigned DEFAULT NULL,
  `edit_date` varchar(20) DEFAULT '0000-00-00 00:00:00',
  `editor_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
  COMMENT = '书架表';

-- -----------------------------------------------------
-- Table `t_filter_word`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_filter_word` ;

CREATE TABLE IF NOT EXISTS `t_filter_word` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `word` VARCHAR(32) NULL,
  `type_id` TINYINT(1) UNSIGNED NULL DEFAULT 0 COMMENT '类型',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '敏感词表';


-- -----------------------------------------------------
-- Table `t_admin_menu`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_admin_menu` ;

CREATE TABLE IF NOT EXISTS `t_admin_menu` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(32) NULL,
  `url` VARCHAR(32) NULL,
  `order_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `parent_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `is_leaf` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `memo` VARCHAR(32) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '后台菜单表';


-- -----------------------------------------------------
-- Table `t_admin_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_admin_role` ;

CREATE TABLE IF NOT EXISTS `t_admin_role` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NULL,
  `memo` VARCHAR(32) NULL,
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '后台用户角色表';


-- -----------------------------------------------------
-- Table `t_admin_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_admin_user` ;

CREATE TABLE IF NOT EXISTS `t_admin_user` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NULL,
  `password` VARCHAR(32) NULL,
  `nickname` VARCHAR(32) NULL,
  `email` VARCHAR(32) NULL,
  `role_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '后台用户表';


-- -----------------------------------------------------
-- Table `t_admin_role_menu`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_admin_role_menu` ;

CREATE TABLE IF NOT EXISTS `t_admin_role_menu` (
  `id` INT(11) UNSIGNED NOT NULL,
  `menu_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `role_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '后台角色菜单关联表';



-- -----------------------------------------------------
-- Table `t_recommend_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_recommend_log` ;

CREATE TABLE IF NOT EXISTS `t_recommend_log` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `book_id` BIGINT(18) NOT NULL,
  `target_type` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '推荐栏目类型',
  `target_id` VARCHAR(45) NOT NULL COMMENT '推荐栏目id',
  `platform_id` INT(11) UNSIGNED NULL DEFAULT 0 COMMENT '平台id',
  `is_use` TINYINT(1) NOT NULL DEFAULT 0,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) NULL,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) NULL COMMENT '推荐记录表',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8
  COMMENT = '书籍推荐记录表';


-- -----------------------------------------------------
-- Table `t_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_user` ;

CREATE TABLE IF NOT EXISTS `t_user` (
  `id` INT(11) UNSIGNED NOT NULL,
  `imei` VARCHAR(50) NULL,
  `imsi` VARCHAR(64) NULL,
  `platform_id` INT(11) UNSIGNED NULL DEFAULT 0 COMMENT '平台id',
  `ditch_id` INT(11) UNSIGNED NULL DEFAULT 0 COMMENT '渠道号，',
  `name` VARCHAR(32) NULL,
  `password` VARCHAR(32) NULL,
  `mobile` VARCHAR(32) NULL,
  `email` VARCHAR(32) NULL,
  `nickname` VARCHAR(32) NULL,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `level` INT(11) UNSIGNED NULL DEFAULT 0,
  `sex` TINYINT(1) UNSIGNED NULL DEFAULT 0 COMMENT '0保密，1男，2女',
  `age` INT(11) UNSIGNED NULL DEFAULT 0,
  `experience` INT(11) UNSIGNED NULL DEFAULT 0,
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `birthday` DATE NULL DEFAULT '0000-00-00',
  `app_version` VARCHAR(32) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickname_unique` (`nickname`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '客户端用户表';


-- -----------------------------------------------------
-- Table `t_adv`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv`;

CREATE TABLE IF NOT EXISTS `t_adv` (
  `id`            INT(11) UNSIGNED             NOT NULL AUTO_INCREMENT,
  `type_id`       TINYINT(1) UNSIGNED          NULL DEFAULT 1
  COMMENT '1纯文字，2图文',
  `book_id`       BIGINT(18)                   NOT NULL,
  `chapter_id`    BIGINT(18)                   NOT NULL,
  `title`         VARCHAR(100)                 NULL,
  `position_type` TINYINT(1) UNSIGNED          NULL DEFAULT 1
  COMMENT '1书架，2正文',
  `term_type`     TINYINT(1) UNSIGNED ZEROFILL NULL DEFAULT 0
  COMMENT '投放类型，0无，1按分类，2按标签，3按书籍',
  `platform_id`   INT(11) UNSIGNED             NOT NULL DEFAULT '0'
  COMMENT '平台id',
  `version`       VARCHAR(32)                  NOT NULL
  COMMENT '版本号，一个',
  `is_use_ditch`  TINYINT(1) DEFAULT '0'
  COMMENT '0否，1是',
  `ditch_ids`     VARCHAR(200) DEFAULT NULL
  COMMENT '渠道号，通过;分隔',
  `status`        TINYINT(1) DEFAULT '0'
  COMMENT '0下线，1上线',
  `create_date`   TIMESTAMP                    NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id`    INT(11) UNSIGNED             NULL DEFAULT 0,
  `edit_date`     TIMESTAMP                    NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id`     INT(11) UNSIGNED             NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '广告表';

-- -----------------------------------------------------
-- Table `t_adv_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv_item`;

CREATE TABLE IF NOT EXISTS `t_adv_item` (
  `id`       INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `adv_id`   INT(18)          NOT NULL,
  `content`  VARCHAR(100)     NULL,
  `url`      VARCHAR(200)     NULL,
  `order_id` INT(11) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- -----------------------------------------------------
-- Table `t_adv_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv_pic`;

CREATE TABLE IF NOT EXISTS `t_adv_pic` (
  `id`       INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `adv_id`   INT(18)          NOT NULL,
  `pic`      VARCHAR(200)     NULL,
  `width` INT(11) UNSIGNED NOT NULL DEFAULT 0,
  `height` INT(11) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '广告图片表';


-- -----------------------------------------------------
-- Table `t_platform`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_platform` ;

CREATE TABLE IF NOT EXISTS `t_platform` (
  `id` INT(11) UNSIGNED NOT NULL,
  `name` VARCHAR(32) NULL,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '平台表';


-- -----------------------------------------------------
-- Table `t_ditch`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_ditch` ;

CREATE TABLE IF NOT EXISTS `t_ditch` (
  `id` INT(11) UNSIGNED NOT NULL COMMENT '渠道表',
  `name` VARCHAR(32) NULL,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `is_use` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '渠道表';


-- -----------------------------------------------------
-- Table `t_feedback`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_feedback` ;

CREATE TABLE IF NOT EXISTS `t_feedback` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) DEFAULT NULL,
  `ditch_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `platform_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `os` VARCHAR(32) NULL,
  `content` VARCHAR(200) NULL,
  `contact` VARCHAR(32) NULL COMMENT '联系方式',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '用户反馈表';


-- -----------------------------------------------------
-- Table `t_push`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_push`;

CREATE TABLE IF NOT EXISTS `t_push` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `platform_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `target_type` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `versions` VARCHAR(200) NULL,
  `ditch_ids` VARCHAR(200) NULL,
  `title` VARCHAR(100) NULL,
  `content` VARCHAR(200) NULL,
  `url` VARCHAR(200) NULL,
  `status` TINYINT(1) UNSIGNED NULL DEFAULT 0 COMMENT '0下线，1上线，2待发布',
  `publish_date` varchar(20) NULL DEFAULT '0000-00-00 00:00:00' COMMENT '发布时间',
  `create_date` varchar(20) NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `creator_id` INT(11) NULL DEFAULT 0,
  `edit_date`  varchar(20) NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '推送消息表';


-- -----------------------------------------------------
-- Table `t_upgrade`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_upgrade` ;

CREATE TABLE IF NOT EXISTS `t_upgrade` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `version_name` VARCHAR(100) NULL,
  `major_version` VARCHAR(100) NULL,
  `minor_version` VARCHAR(100) NULL,
  `ditch_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `platform_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `content` VARCHAR(200) NULL,
  `package_url` VARCHAR(200) NULL,
  `publish_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `is_publish` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `is_force` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '版本升级表';


-- -----------------------------------------------------
-- Table `t_client_entry`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_client_entry` ;

CREATE TABLE IF NOT EXISTS `t_client_entry` (
  `id` INT(11) UNSIGNED NOT NULL,
  `entry_type` TINYINT(1) UNSIGNED NULL DEFAULT 1,
  `platform_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `versions` VARCHAR(100) NULL,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '启动配置表';

-- -----------------------------------------------------
-- Table `t_client_tab`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_client_tab` ;

CREATE TABLE IF NOT EXISTS `t_client_tab` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NULL,
  `order_id` INT(11) UNSIGNED NULL,
  `url` VARCHAR(200) NULL,
  `create_date` varchar(20) NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL,
  `edit_date` varchar(20) NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL,
  status int(1) NULL DEFAULT 0 COMMENT '0下线，1上线',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '书城tab表';


-- -----------------------------------------------------
-- Table `t_adv_startup`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv_startup` ;

CREATE TABLE IF NOT EXISTS `t_adv_startup` (
  `id` INT(11) UNSIGNED NOT NULL,
  `name` VARCHAR(100) NULL,
  `platform_id` INT(11) UNSIGNED NULL,
  `versions` VARCHAR(200) NOT NULL COMMENT '版本号,多个用;隔开',
  `is_use_ditch` TINYINT(1) NULL DEFAULT 0,
  `ditch_ids` VARCHAR(200) NULL,
  `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0下线，1上线',
  `online_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `offline_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '启动页广告表';

-- -----------------------------------------------------
-- Table `t_adv_shelf`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv_shelf` ;

CREATE TABLE IF NOT EXISTS `t_adv_shelf` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL,
  `type_id` TINYINT(1) NULL DEFAULT 0 comment '书架广告类型，1：纯文字，2：图文',
  `platform_id` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '平台id',
  `versions` VARCHAR(200) NOT NULL COMMENT '版本号,多个用;隔开',
  `is_use_ditch` TINYINT(1) NULL DEFAULT 0 COMMENT '0否，1是',
  `ditch_ids` VARCHAR(200) NULL COMMENT '渠道号，通过;分隔',
  `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0下线，1上线',
  `content_1` VARCHAR(200) NULL COMMENT '广告内容1',
  `url_1` VARCHAR(200) NULL COMMENT '链接1',
  `content_2` VARCHAR(200) NULL COMMENT '广告内容2',
  `url_2` VARCHAR(200) NULL COMMENT '链接1',
  `content_3` VARCHAR(200) NULL COMMENT '广告内容3',
  `url_3` VARCHAR(200) NULL COMMENT '链接3',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '书架广告表';

-- -----------------------------------------------------
-- Table `t_adv_chapter`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv_chapter` ;

CREATE TABLE IF NOT EXISTS `t_adv_chapter` (
  `id` INT(11) UNSIGNED NOT NULL,
  `name` VARCHAR(100) NULL COMMENT '标题',
  `book_id` BIGINT(18) NOT NULL COMMENT '书籍id',
  `chapter_order_id` INT(11) NOT NULL DEFAULT 0 COMMENT '章节序号',
  `platform_id` INT(11) UNSIGNED NULL,
  `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0下线，1上线',
  `content` VARCHAR(200) NULL COMMENT '广告内容',
  `url` VARCHAR(200) NULL COMMENT '链接',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '固定章节广告表';

-- -----------------------------------------------------
-- Table `t_adv_host`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv_host` ;

CREATE TABLE IF NOT EXISTS `t_adv_host` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `status` TINYINT(1) UNSIGNED NULL DEFAULT 0,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '广告主表';

-- -----------------------------------------------------
-- Table `t_adv_general`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv_general` ;

CREATE TABLE IF NOT EXISTS `t_adv_general` (
  `id` INT(11) UNSIGNED NOT NULL,
  `adv_host_id` INT(11) NOT NULL DEFAULT 0 COMMENT '广告主id',
  `name` VARCHAR(100) NULL COMMENT '标题',
  `weight` DECIMAL(5,2) NOT NULL COMMENT '权重',
  `platform_id` INT(11) UNSIGNED NULL,
  `tag_sex_id` INT(11) UNSIGNED NULL,
  `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0下线，1上线',
  `content` VARCHAR(200) NULL COMMENT '广告内容',
  `url` VARCHAR(200) NULL COMMENT '链接',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '通用广告表';

-- -----------------------------------------------------
-- Table `t_adv_precise`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_adv_precise` ;

CREATE TABLE IF NOT EXISTS `t_adv_precise` (
  `id` INT(11) UNSIGNED NOT NULL,
  `adv_host_id` INT(11) NOT NULL DEFAULT 0 COMMENT '广告主id',
  `name` VARCHAR(100) NULL COMMENT '标题',
  `weight` DECIMAL(5,2) NOT NULL COMMENT '权重',
  `platform_id` INT(11) UNSIGNED NULL,
  `tag_classify_id` INT(11) UNSIGNED NULL,
  `tag_sex_id` INT(11) UNSIGNED NULL,
  `tag_content_ids` VARCHAR(200) NULL COMMENT '内容属性',
  `tag_supply_ids` VARCHAR(200) NULL COMMENT '内容属性',
  `keywords` VARCHAR(200) NULL COMMENT '关键词',
  `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0下线，1上线',
  `content` VARCHAR(200) NULL COMMENT '广告内容',
  `url` VARCHAR(200) NULL COMMENT '链接',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '精确广告表';

-- -----------------------------------------------------
-- Table `t_pic`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_pic` ;

CREATE TABLE IF NOT EXISTS `t_pic` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `target_type` INT(11) UNSIGNED NULL DEFAULT 0 comment '目标类型，1：启动页广告，2：书架广告，3：固定章节广告，4：精确广告，5：通用广告',
  `target_id` VARCHAR(32) NOT NULL DEFAULT '0',
  `url` VARCHAR(200) NULL,
  `width` INT(11) UNSIGNED NULL DEFAULT 0,
  `height` INT(11) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '图片表';


-- -----------------------------------------------------
-- Table `t_last_login`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_last_login` ;

CREATE TABLE IF NOT EXISTS `t_last_login` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户最后一次登陆设备',
  `cid` varchar(128) DEFAULT NULL COMMENT '设备信息',
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cid_unic` (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Table `t_user_favorite`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_user_favorite` ;

CREATE TABLE IF NOT EXISTS `t_user_favorite` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid` int(11) NULL DEFAULT null,
  `book_id` BIGINT(18) UNSIGNED NOT NULL DEFAULT 0,
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '用户收藏表';;

-- -----------------------------------------------------
-- Table `t_user_attend`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_user_attend` ;

CREATE TABLE IF NOT EXISTS `t_user_attend` (
  `id` INT(11) UNSIGNED NOT NULL,
  `user_id` INT(11) UNSIGNED NULL DEFAULT 0,
  `attend_date` DATE NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '签到表';

-- -----------------------------------------------------
-- Table `t_client_shelf`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_client_shelf` ;

CREATE TABLE IF NOT EXISTS `t_client_shelf` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bookIds` VARCHAR(200) NOT NULL COMMENT '预置书id,多本书用;分隔,最多9本',
  `chapters` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预置章节数，1-10',
  `platform_id` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '平台id',
  `version` VARCHAR(32) NOT NULL COMMENT '版本号，一个',
  `is_use_ditch` TINYINT(1) NULL DEFAULT 0 COMMENT '0否，1是',
  `ditch_ids` VARCHAR(200) NULL COMMENT '渠道号，通过;分隔',
  `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0下线，1上线',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8
  COMMENT = '书架资源配置表';


-- -----------------------------------------------------
-- Table `t_ranking_backup`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_ranking_backup` ;

CREATE TABLE IF NOT EXISTS `t_ranking_backup` (
  `id`          INT(11) UNSIGNED     NOT NULL AUTO_INCREMENT,
  `book_id`     BIGINT(18) UNSIGNED NOT NULL DEFAULT '0',
  `list_type`   tinyint(1)  UNSIGNED NOT NULL DEFAULT '0'
  COMMENT '榜单类型，1：人气；2：飙升；3：最新；4：搜索',
  `date_type`   tinyint(1)  UNSIGNED NOT NULL DEFAULT '0'
  COMMENT '时间类型，1：天；2：周；3：月',
  `backup_type` tinyint(1)  UNSIGNED NOT NULL DEFAULT '0'
  COMMENT '备份类型，1：排除；2：保留',
  `order_id`    INT(11) UNSIGNED DEFAULT '0',
  `create_date` TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id`  INT(11) UNSIGNED DEFAULT '0',
  `edit_date`   TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id`   INT(11) UNSIGNED DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

-- -----------------------------------------------------
-- Table `t_user_recommend`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_user_recommend` ;

CREATE TABLE IF NOT EXISTS `t_user_recommend` (
  `id`          INT(11) UNSIGNED     NOT NULL AUTO_INCREMENT,
  `book_id`     BIGINT(18) UNSIGNED NOT NULL DEFAULT '0',
  `platform_id`    INT(11) UNSIGNED DEFAULT '0',
  `is_use`      TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否可用，0否，1是',
  `create_date` TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id`  INT(11) UNSIGNED DEFAULT '0',
  `edit_date`   TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id`   INT(11) UNSIGNED DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

-- -----------------------------------------------------
-- Table `t_client_shelf`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_client_shelf` ;

CREATE TABLE IF NOT EXISTS `t_client_shelf` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bookIds` VARCHAR(200) NOT NULL COMMENT '预置书id,多本书用;分隔,最多9本',
  `chapters` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预置章节数，1-10',
  `platform_id` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '平台id',
  `version` VARCHAR(32) NOT NULL COMMENT '版本号，一个',
  `is_use_ditch` TINYINT(1) NULL DEFAULT 0 COMMENT '0否，1是',
  `ditch_ids` VARCHAR(200) NULL COMMENT '渠道号，通过;分隔',
  `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0下线，1上线',
  `create_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` INT(11) UNSIGNED NULL,
  `edit_date` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00',
  `editor_id` INT(11) UNSIGNED NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET = utf8;

