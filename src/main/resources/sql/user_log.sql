CREATE TABLE `USER_LOG` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '日志编号' ,
`userid`  int NOT NULL COMMENT '用户ID',
`operation`  varchar(20) NULL COMMENT '操作类型' ,
`operationtime`  datetime NULL COMMENT '操作时间' ,
PRIMARY KEY (`id`)
)
;

