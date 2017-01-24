INSERT INTO t_admin_menu (text, order_id, parent_id, is_leaf, is_use) VALUES ('接口管理', 45, 0, 0, 1);
INSERT INTO t_admin_menu (text, url, order_id, parent_id, is_leaf, is_use) VALUES ('接口书单管理', 'distributeBookList', 5, 35, 1, 1);
INSERT INTO t_admin_menu (text, url, order_id, parent_id, is_leaf, is_use) VALUES ('推广渠道管理', 'distributeList', 10, 35, 1, 1);

insert into t_platform (id,name,create_date,creator_id,edit_date,editor_id,is_use) values(3,'wap',sysdate(),1,sysdate(),1,1);

INSERT INTO t_admin_menu (text, order_id, parent_id, is_leaf, is_use) VALUES ('支付管理', 41, 0, 0, 1);
INSERT INTO t_admin_menu (text, url, order_id, parent_id, is_leaf, is_use) VALUES ('支付开关', 'paySwitchList', 42, 38, 1, 1);
INSERT INTO t_admin_menu (text, url, order_id, parent_id, is_leaf, is_use) VALUES ('支付配置', 'payConfigList', 43, 38, 1, 1);
INSERT INTO t_admin_menu (text, url, order_id, parent_id, is_leaf, is_use) VALUES ('订阅配置', 'subConfigList', 44, 38, 1, 1);
