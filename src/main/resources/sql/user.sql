CREATE TABLE `AIDOOTECH_USER` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID' ,
`name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名' ,
`dob`  date NULL DEFAULT NULL COMMENT '出生日期' ,
`address`  varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址' ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简介' ,
`createdAt`  date NULL DEFAULT NULL COMMENT '创建日期' ,
`type`  enum('普通用户','管理员') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户类型' ,
`password`  varchar(20) NULL COMMENT '密码' ,
PRIMARY KEY (`id`)
)
;

