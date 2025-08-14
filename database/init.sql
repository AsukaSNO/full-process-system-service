-- 创建数据库
CREATE DATABASE IF NOT EXISTS `fulldb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE `fulldb`;

-- 创建用户信息表
CREATE TABLE IF NOT EXISTS `user_info` (
  `uid` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'momo' COMMENT '昵称',
  `gender` tinyint NOT NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `organization` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机构',
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- 创建用户认证表
CREATE TABLE IF NOT EXISTS `user_auth` (
  `uid` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
  `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名，唯一',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码盐值',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：-1-删除，0-禁用，1-启用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE KEY `uk_login_name` (`login_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户认证表' ROW_FORMAT = Dynamic;

-- 创建用户会话表
CREATE TABLE IF NOT EXISTS `user_session` (
  `session_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `uid` int UNSIGNED NOT NULL COMMENT '用户ID',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话令牌',
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '刷新令牌',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_active_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后活跃时间',
  `client_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-无效，1-有效',
  PRIMARY KEY (`session_id`) USING BTREE,
  UNIQUE KEY `uk_token` (`token`) USING BTREE,
  KEY `idx_uid` (`uid`) USING BTREE,
  KEY `idx_refresh_token` (`refresh_token`) USING BTREE,
  KEY `idx_expire_time` (`expire_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户会话表' ROW_FORMAT = Dynamic;

-- 插入测试用户信息数据
INSERT INTO `user_info` (`nickname`, `gender`, `mobile`, `email`, `avatar`, `organization`, `signature`, `remark`) VALUES
('张三', 1, '13800138001', 'zhangsan@example.com', 'https://example.com/avatar1.jpg', '技术部', '热爱编程的程序员', '技术专家'),
('李四', 2, '13800138002', 'lisi@example.com', 'https://example.com/avatar2.jpg', '产品部', '产品经理', '产品设计专家'),
('王五', 1, '13800138003', 'wangwu@example.com', 'https://example.com/avatar3.jpg', '运营部', '运营专员', '运营专家'),
('赵六', 0, '13800138004', 'zhaoliu@example.com', 'https://example.com/avatar4.jpg', '市场部', '市场专员', '市场专家');

-- 插入测试用户认证数据（密码为123456，盐值随机生成）
INSERT INTO `user_auth` (`uid`, `login_name`, `password`, `salt`, `status`, `create_time`, `update_time`) VALUES
(1, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', 'a1b2c3d4e5f6g7h8', 1, NOW(), NOW()),
(2, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', 'h8g7f6e5d4c3b2a1', 1, NOW(), NOW()),
(3, 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', 'i9j8k7l6m5n4o3p2', 1, NOW(), NOW()),
(4, 'zhaoliu', 'e10adc3949ba59abbe56e057f20f883e', 'q1w2e3r4t5y6u7i8o', 1, NOW(), NOW());
