/*
 Navicat Premium Data Transfer

 Source Server         : mall-电商项目开发服务器
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 192.168.190.132:3307
 Source Schema         : mall_admin

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 05/05/2022 10:59:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', '0 0/30 * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', NULL, 'io.renren.modules.job.utils.ScheduleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002E696F2E72656E72656E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000178A53BA1E07874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000174000672656E72656E74000CE58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000007800);

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
INSERT INTO `QRTZ_LOCKS` VALUES ('RenrenScheduler', 'STATE_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('RenrenScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('RenrenScheduler', 'LJL1650858081429', 1650858373040, 15000);

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_TRIGGERS` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', 'TASK_1', 'DEFAULT', NULL, 1650859200000, -1, 5, 'WAITING', 'CRON', 1617709585000, 0, NULL, 2, 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002E696F2E72656E72656E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000178A53BA1E07874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000174000672656E72656E74000CE58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000007800);

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES (1, 'testTask', 'renren', '0 0/30 * * * ?', 0, '参数测试', '2021-04-06 11:30:52');

-- ----------------------------
-- Table structure for schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job_log`;
CREATE TABLE `schedule_job_log`  (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` bigint(20) NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `status` tinyint(4) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `error` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败信息',
  `times` int(11) NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `job_id`(`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 367 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_job_log
-- ----------------------------
INSERT INTO `schedule_job_log` VALUES (1, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-06 20:00:00');
INSERT INTO `schedule_job_log` VALUES (2, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-06 20:30:00');
INSERT INTO `schedule_job_log` VALUES (3, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-12 17:00:00');
INSERT INTO `schedule_job_log` VALUES (4, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-12 17:30:00');
INSERT INTO `schedule_job_log` VALUES (5, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-12 18:00:00');
INSERT INTO `schedule_job_log` VALUES (6, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-12 19:00:00');
INSERT INTO `schedule_job_log` VALUES (7, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-12 19:30:00');
INSERT INTO `schedule_job_log` VALUES (8, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-12 20:00:00');
INSERT INTO `schedule_job_log` VALUES (9, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-12 21:00:00');
INSERT INTO `schedule_job_log` VALUES (10, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-12 21:30:00');
INSERT INTO `schedule_job_log` VALUES (11, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 14:00:00');
INSERT INTO `schedule_job_log` VALUES (12, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 14:30:00');
INSERT INTO `schedule_job_log` VALUES (13, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 15:00:00');
INSERT INTO `schedule_job_log` VALUES (14, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 15:30:00');
INSERT INTO `schedule_job_log` VALUES (15, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 16:00:00');
INSERT INTO `schedule_job_log` VALUES (16, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-13 16:30:00');
INSERT INTO `schedule_job_log` VALUES (17, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 17:00:00');
INSERT INTO `schedule_job_log` VALUES (18, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-13 17:30:00');
INSERT INTO `schedule_job_log` VALUES (19, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 18:00:00');
INSERT INTO `schedule_job_log` VALUES (20, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 18:30:00');
INSERT INTO `schedule_job_log` VALUES (21, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-13 19:00:00');
INSERT INTO `schedule_job_log` VALUES (22, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 19:30:00');
INSERT INTO `schedule_job_log` VALUES (23, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-13 21:30:00');
INSERT INTO `schedule_job_log` VALUES (24, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-14 13:30:00');
INSERT INTO `schedule_job_log` VALUES (25, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-14 14:00:00');
INSERT INTO `schedule_job_log` VALUES (26, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-14 14:30:00');
INSERT INTO `schedule_job_log` VALUES (27, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-14 15:00:00');
INSERT INTO `schedule_job_log` VALUES (28, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-14 15:30:00');
INSERT INTO `schedule_job_log` VALUES (29, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-14 16:00:00');
INSERT INTO `schedule_job_log` VALUES (30, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-14 16:30:00');
INSERT INTO `schedule_job_log` VALUES (31, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-14 17:00:00');
INSERT INTO `schedule_job_log` VALUES (32, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-14 17:30:00');
INSERT INTO `schedule_job_log` VALUES (33, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-14 18:00:00');
INSERT INTO `schedule_job_log` VALUES (34, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-14 18:30:00');
INSERT INTO `schedule_job_log` VALUES (35, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-14 19:00:00');
INSERT INTO `schedule_job_log` VALUES (36, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-14 19:30:00');
INSERT INTO `schedule_job_log` VALUES (37, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-14 20:00:00');
INSERT INTO `schedule_job_log` VALUES (38, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-14 20:30:00');
INSERT INTO `schedule_job_log` VALUES (39, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 15:30:00');
INSERT INTO `schedule_job_log` VALUES (40, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-15 16:00:00');
INSERT INTO `schedule_job_log` VALUES (41, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 16:30:00');
INSERT INTO `schedule_job_log` VALUES (42, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 17:00:00');
INSERT INTO `schedule_job_log` VALUES (43, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-15 17:30:00');
INSERT INTO `schedule_job_log` VALUES (44, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-15 18:00:00');
INSERT INTO `schedule_job_log` VALUES (45, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 18:30:00');
INSERT INTO `schedule_job_log` VALUES (46, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 19:00:00');
INSERT INTO `schedule_job_log` VALUES (47, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 19:30:00');
INSERT INTO `schedule_job_log` VALUES (48, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 20:00:00');
INSERT INTO `schedule_job_log` VALUES (49, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 20:30:00');
INSERT INTO `schedule_job_log` VALUES (50, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 21:00:00');
INSERT INTO `schedule_job_log` VALUES (51, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 21:30:00');
INSERT INTO `schedule_job_log` VALUES (52, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-15 22:00:00');
INSERT INTO `schedule_job_log` VALUES (53, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-16 20:30:00');
INSERT INTO `schedule_job_log` VALUES (54, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 15:00:00');
INSERT INTO `schedule_job_log` VALUES (55, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-19 15:30:00');
INSERT INTO `schedule_job_log` VALUES (56, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 16:00:00');
INSERT INTO `schedule_job_log` VALUES (57, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 16:30:00');
INSERT INTO `schedule_job_log` VALUES (58, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 17:00:00');
INSERT INTO `schedule_job_log` VALUES (59, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 17:30:00');
INSERT INTO `schedule_job_log` VALUES (60, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 18:00:00');
INSERT INTO `schedule_job_log` VALUES (61, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-19 18:30:00');
INSERT INTO `schedule_job_log` VALUES (62, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 19:00:00');
INSERT INTO `schedule_job_log` VALUES (63, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 19:30:00');
INSERT INTO `schedule_job_log` VALUES (64, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-19 20:00:00');
INSERT INTO `schedule_job_log` VALUES (65, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 20:30:00');
INSERT INTO `schedule_job_log` VALUES (66, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 21:00:00');
INSERT INTO `schedule_job_log` VALUES (67, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-19 21:30:00');
INSERT INTO `schedule_job_log` VALUES (68, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-19 22:00:00');
INSERT INTO `schedule_job_log` VALUES (69, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-19 22:30:00');
INSERT INTO `schedule_job_log` VALUES (70, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-20 14:30:00');
INSERT INTO `schedule_job_log` VALUES (71, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-20 15:00:00');
INSERT INTO `schedule_job_log` VALUES (72, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-20 15:30:00');
INSERT INTO `schedule_job_log` VALUES (73, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-20 16:00:00');
INSERT INTO `schedule_job_log` VALUES (74, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 16:30:00');
INSERT INTO `schedule_job_log` VALUES (75, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 17:00:00');
INSERT INTO `schedule_job_log` VALUES (76, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 17:30:00');
INSERT INTO `schedule_job_log` VALUES (77, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 18:00:00');
INSERT INTO `schedule_job_log` VALUES (78, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 18:30:00');
INSERT INTO `schedule_job_log` VALUES (79, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-20 19:00:00');
INSERT INTO `schedule_job_log` VALUES (80, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 19:30:00');
INSERT INTO `schedule_job_log` VALUES (81, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 20:00:00');
INSERT INTO `schedule_job_log` VALUES (82, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-20 20:30:00');
INSERT INTO `schedule_job_log` VALUES (83, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 21:00:00');
INSERT INTO `schedule_job_log` VALUES (84, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 21:30:00');
INSERT INTO `schedule_job_log` VALUES (85, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-20 22:00:00');
INSERT INTO `schedule_job_log` VALUES (86, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-20 22:30:00');
INSERT INTO `schedule_job_log` VALUES (87, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-20 23:00:00');
INSERT INTO `schedule_job_log` VALUES (88, 1, 'testTask', 'renren', 0, NULL, 3, '2021-04-20 23:30:00');
INSERT INTO `schedule_job_log` VALUES (89, 1, 'testTask', 'renren', 0, NULL, 9, '2021-04-21 00:00:00');
INSERT INTO `schedule_job_log` VALUES (90, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-21 00:30:00');
INSERT INTO `schedule_job_log` VALUES (91, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-21 14:30:00');
INSERT INTO `schedule_job_log` VALUES (92, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-21 15:00:00');
INSERT INTO `schedule_job_log` VALUES (93, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-21 15:30:00');
INSERT INTO `schedule_job_log` VALUES (94, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-21 16:00:00');
INSERT INTO `schedule_job_log` VALUES (95, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-21 16:30:00');
INSERT INTO `schedule_job_log` VALUES (96, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-21 17:00:00');
INSERT INTO `schedule_job_log` VALUES (97, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-21 17:30:00');
INSERT INTO `schedule_job_log` VALUES (98, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-21 18:00:00');
INSERT INTO `schedule_job_log` VALUES (99, 1, 'testTask', 'renren', 0, NULL, 4, '2021-04-21 18:30:00');
INSERT INTO `schedule_job_log` VALUES (100, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-21 19:00:00');
INSERT INTO `schedule_job_log` VALUES (101, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-21 19:30:00');
INSERT INTO `schedule_job_log` VALUES (102, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-21 20:00:00');
INSERT INTO `schedule_job_log` VALUES (103, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-21 20:30:00');
INSERT INTO `schedule_job_log` VALUES (104, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-21 21:00:00');
INSERT INTO `schedule_job_log` VALUES (105, 1, 'testTask', 'renren', 0, NULL, 3, '2021-04-21 21:30:00');
INSERT INTO `schedule_job_log` VALUES (106, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-21 22:00:00');
INSERT INTO `schedule_job_log` VALUES (107, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-21 22:30:00');
INSERT INTO `schedule_job_log` VALUES (108, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 11:00:00');
INSERT INTO `schedule_job_log` VALUES (109, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 11:30:00');
INSERT INTO `schedule_job_log` VALUES (110, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 12:00:00');
INSERT INTO `schedule_job_log` VALUES (111, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 12:30:00');
INSERT INTO `schedule_job_log` VALUES (112, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-22 13:00:00');
INSERT INTO `schedule_job_log` VALUES (113, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 13:30:00');
INSERT INTO `schedule_job_log` VALUES (114, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 14:00:00');
INSERT INTO `schedule_job_log` VALUES (115, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-22 14:30:00');
INSERT INTO `schedule_job_log` VALUES (116, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 15:00:00');
INSERT INTO `schedule_job_log` VALUES (117, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 15:30:00');
INSERT INTO `schedule_job_log` VALUES (118, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 16:00:00');
INSERT INTO `schedule_job_log` VALUES (119, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 16:30:00');
INSERT INTO `schedule_job_log` VALUES (120, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 17:00:00');
INSERT INTO `schedule_job_log` VALUES (121, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 17:30:00');
INSERT INTO `schedule_job_log` VALUES (122, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 18:00:00');
INSERT INTO `schedule_job_log` VALUES (123, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 18:30:00');
INSERT INTO `schedule_job_log` VALUES (124, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 19:00:00');
INSERT INTO `schedule_job_log` VALUES (125, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 19:30:00');
INSERT INTO `schedule_job_log` VALUES (126, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 20:00:00');
INSERT INTO `schedule_job_log` VALUES (127, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 20:30:00');
INSERT INTO `schedule_job_log` VALUES (128, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 21:00:00');
INSERT INTO `schedule_job_log` VALUES (129, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-22 21:30:00');
INSERT INTO `schedule_job_log` VALUES (130, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 22:00:00');
INSERT INTO `schedule_job_log` VALUES (131, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 22:30:00');
INSERT INTO `schedule_job_log` VALUES (132, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-22 23:00:00');
INSERT INTO `schedule_job_log` VALUES (133, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-22 23:30:00');
INSERT INTO `schedule_job_log` VALUES (134, 1, 'testTask', 'renren', 0, NULL, 25, '2021-04-23 00:00:00');
INSERT INTO `schedule_job_log` VALUES (135, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 00:30:00');
INSERT INTO `schedule_job_log` VALUES (136, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 01:00:00');
INSERT INTO `schedule_job_log` VALUES (137, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-23 01:30:00');
INSERT INTO `schedule_job_log` VALUES (138, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 09:30:00');
INSERT INTO `schedule_job_log` VALUES (139, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 10:00:00');
INSERT INTO `schedule_job_log` VALUES (140, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 10:30:00');
INSERT INTO `schedule_job_log` VALUES (141, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 11:00:00');
INSERT INTO `schedule_job_log` VALUES (142, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 11:30:00');
INSERT INTO `schedule_job_log` VALUES (143, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 12:00:00');
INSERT INTO `schedule_job_log` VALUES (144, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 12:30:00');
INSERT INTO `schedule_job_log` VALUES (145, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 13:00:00');
INSERT INTO `schedule_job_log` VALUES (146, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 13:30:00');
INSERT INTO `schedule_job_log` VALUES (147, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 14:00:00');
INSERT INTO `schedule_job_log` VALUES (148, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 15:00:00');
INSERT INTO `schedule_job_log` VALUES (149, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 15:30:00');
INSERT INTO `schedule_job_log` VALUES (150, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 16:00:00');
INSERT INTO `schedule_job_log` VALUES (151, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 16:30:00');
INSERT INTO `schedule_job_log` VALUES (152, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 17:00:00');
INSERT INTO `schedule_job_log` VALUES (153, 1, 'testTask', 'renren', 0, NULL, 6, '2021-04-23 17:30:00');
INSERT INTO `schedule_job_log` VALUES (154, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 18:00:00');
INSERT INTO `schedule_job_log` VALUES (155, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 18:30:00');
INSERT INTO `schedule_job_log` VALUES (156, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 19:00:00');
INSERT INTO `schedule_job_log` VALUES (157, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-23 19:30:00');
INSERT INTO `schedule_job_log` VALUES (158, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 20:00:00');
INSERT INTO `schedule_job_log` VALUES (159, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-23 20:30:00');
INSERT INTO `schedule_job_log` VALUES (160, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-24 00:00:00');
INSERT INTO `schedule_job_log` VALUES (161, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 00:30:00');
INSERT INTO `schedule_job_log` VALUES (162, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 01:00:00');
INSERT INTO `schedule_job_log` VALUES (163, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 01:30:00');
INSERT INTO `schedule_job_log` VALUES (164, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 02:00:00');
INSERT INTO `schedule_job_log` VALUES (165, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 13:00:00');
INSERT INTO `schedule_job_log` VALUES (166, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 13:30:00');
INSERT INTO `schedule_job_log` VALUES (167, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 14:00:00');
INSERT INTO `schedule_job_log` VALUES (168, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 14:30:00');
INSERT INTO `schedule_job_log` VALUES (169, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 15:00:00');
INSERT INTO `schedule_job_log` VALUES (170, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 15:30:00');
INSERT INTO `schedule_job_log` VALUES (171, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 16:00:00');
INSERT INTO `schedule_job_log` VALUES (172, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 16:30:00');
INSERT INTO `schedule_job_log` VALUES (173, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 17:00:00');
INSERT INTO `schedule_job_log` VALUES (174, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 17:30:00');
INSERT INTO `schedule_job_log` VALUES (175, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 18:00:00');
INSERT INTO `schedule_job_log` VALUES (176, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 18:30:00');
INSERT INTO `schedule_job_log` VALUES (177, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 19:00:00');
INSERT INTO `schedule_job_log` VALUES (178, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 19:30:00');
INSERT INTO `schedule_job_log` VALUES (179, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 20:00:00');
INSERT INTO `schedule_job_log` VALUES (180, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-24 20:30:00');
INSERT INTO `schedule_job_log` VALUES (181, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 21:00:00');
INSERT INTO `schedule_job_log` VALUES (182, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 21:30:00');
INSERT INTO `schedule_job_log` VALUES (183, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-24 22:00:00');
INSERT INTO `schedule_job_log` VALUES (184, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 22:30:00');
INSERT INTO `schedule_job_log` VALUES (185, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 23:00:00');
INSERT INTO `schedule_job_log` VALUES (186, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-24 23:30:00');
INSERT INTO `schedule_job_log` VALUES (187, 1, 'testTask', 'renren', 0, NULL, 59, '2021-04-25 00:00:00');
INSERT INTO `schedule_job_log` VALUES (188, 1, 'testTask', 'renren', 0, NULL, 4, '2021-04-25 00:30:00');
INSERT INTO `schedule_job_log` VALUES (189, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 01:00:00');
INSERT INTO `schedule_job_log` VALUES (190, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 13:00:00');
INSERT INTO `schedule_job_log` VALUES (191, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 13:30:00');
INSERT INTO `schedule_job_log` VALUES (192, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 14:00:00');
INSERT INTO `schedule_job_log` VALUES (193, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-25 14:30:00');
INSERT INTO `schedule_job_log` VALUES (194, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 15:00:00');
INSERT INTO `schedule_job_log` VALUES (195, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 15:30:00');
INSERT INTO `schedule_job_log` VALUES (196, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 16:00:00');
INSERT INTO `schedule_job_log` VALUES (197, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 16:30:00');
INSERT INTO `schedule_job_log` VALUES (198, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-25 17:00:00');
INSERT INTO `schedule_job_log` VALUES (199, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 17:30:00');
INSERT INTO `schedule_job_log` VALUES (200, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-25 18:00:00');
INSERT INTO `schedule_job_log` VALUES (201, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-25 18:30:00');
INSERT INTO `schedule_job_log` VALUES (202, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-25 19:00:00');
INSERT INTO `schedule_job_log` VALUES (203, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-25 19:30:00');
INSERT INTO `schedule_job_log` VALUES (204, 1, 'testTask', 'renren', 0, NULL, 3, '2021-04-25 23:30:00');
INSERT INTO `schedule_job_log` VALUES (205, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 14:00:00');
INSERT INTO `schedule_job_log` VALUES (206, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 14:30:00');
INSERT INTO `schedule_job_log` VALUES (207, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-26 15:00:00');
INSERT INTO `schedule_job_log` VALUES (208, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-26 15:30:00');
INSERT INTO `schedule_job_log` VALUES (209, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 16:00:00');
INSERT INTO `schedule_job_log` VALUES (210, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-26 16:30:00');
INSERT INTO `schedule_job_log` VALUES (211, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 17:00:00');
INSERT INTO `schedule_job_log` VALUES (212, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 17:30:00');
INSERT INTO `schedule_job_log` VALUES (213, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 18:00:00');
INSERT INTO `schedule_job_log` VALUES (214, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-26 18:30:00');
INSERT INTO `schedule_job_log` VALUES (215, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-26 19:00:00');
INSERT INTO `schedule_job_log` VALUES (216, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-26 19:30:00');
INSERT INTO `schedule_job_log` VALUES (217, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 20:00:00');
INSERT INTO `schedule_job_log` VALUES (218, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 20:30:00');
INSERT INTO `schedule_job_log` VALUES (219, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-26 21:00:00');
INSERT INTO `schedule_job_log` VALUES (220, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-26 23:30:00');
INSERT INTO `schedule_job_log` VALUES (221, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-27 11:30:00');
INSERT INTO `schedule_job_log` VALUES (222, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-27 12:00:00');
INSERT INTO `schedule_job_log` VALUES (223, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-27 12:30:00');
INSERT INTO `schedule_job_log` VALUES (224, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-27 13:00:00');
INSERT INTO `schedule_job_log` VALUES (225, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-27 13:30:00');
INSERT INTO `schedule_job_log` VALUES (226, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-27 14:00:00');
INSERT INTO `schedule_job_log` VALUES (227, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-27 14:30:00');
INSERT INTO `schedule_job_log` VALUES (228, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-28 18:30:00');
INSERT INTO `schedule_job_log` VALUES (229, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-28 19:00:00');
INSERT INTO `schedule_job_log` VALUES (230, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-28 19:30:00');
INSERT INTO `schedule_job_log` VALUES (231, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 15:30:00');
INSERT INTO `schedule_job_log` VALUES (232, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 16:00:00');
INSERT INTO `schedule_job_log` VALUES (233, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-29 16:30:00');
INSERT INTO `schedule_job_log` VALUES (234, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 17:00:00');
INSERT INTO `schedule_job_log` VALUES (235, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 17:30:00');
INSERT INTO `schedule_job_log` VALUES (236, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 18:00:00');
INSERT INTO `schedule_job_log` VALUES (237, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 18:30:00');
INSERT INTO `schedule_job_log` VALUES (238, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 19:00:00');
INSERT INTO `schedule_job_log` VALUES (239, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 19:30:00');
INSERT INTO `schedule_job_log` VALUES (240, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-29 20:00:00');
INSERT INTO `schedule_job_log` VALUES (241, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 20:30:00');
INSERT INTO `schedule_job_log` VALUES (242, 1, 'testTask', 'renren', 0, NULL, 1, '2021-04-29 21:00:00');
INSERT INTO `schedule_job_log` VALUES (243, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-29 21:30:00');
INSERT INTO `schedule_job_log` VALUES (244, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-29 22:00:00');
INSERT INTO `schedule_job_log` VALUES (245, 1, 'testTask', 'renren', 0, NULL, 0, '2021-04-29 22:30:00');
INSERT INTO `schedule_job_log` VALUES (246, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-29 23:00:00');
INSERT INTO `schedule_job_log` VALUES (247, 1, 'testTask', 'renren', 0, NULL, 2, '2021-04-29 23:30:00');
INSERT INTO `schedule_job_log` VALUES (248, 1, 'testTask', 'renren', 0, NULL, 7, '2021-04-30 00:00:00');
INSERT INTO `schedule_job_log` VALUES (249, 1, 'testTask', 'renren', 0, NULL, 5, '2021-04-30 00:30:00');
INSERT INTO `schedule_job_log` VALUES (250, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-03 14:30:00');
INSERT INTO `schedule_job_log` VALUES (251, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-03 15:00:00');
INSERT INTO `schedule_job_log` VALUES (252, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-03 15:30:00');
INSERT INTO `schedule_job_log` VALUES (253, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-03 17:30:00');
INSERT INTO `schedule_job_log` VALUES (254, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-03 18:00:00');
INSERT INTO `schedule_job_log` VALUES (255, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-03 18:30:00');
INSERT INTO `schedule_job_log` VALUES (256, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-03 19:00:00');
INSERT INTO `schedule_job_log` VALUES (257, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-03 19:30:00');
INSERT INTO `schedule_job_log` VALUES (258, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-03 20:00:00');
INSERT INTO `schedule_job_log` VALUES (259, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-03 20:30:00');
INSERT INTO `schedule_job_log` VALUES (260, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-05 19:00:00');
INSERT INTO `schedule_job_log` VALUES (261, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-05 19:30:00');
INSERT INTO `schedule_job_log` VALUES (262, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-05 20:00:00');
INSERT INTO `schedule_job_log` VALUES (263, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-05 20:30:00');
INSERT INTO `schedule_job_log` VALUES (264, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-05 21:00:00');
INSERT INTO `schedule_job_log` VALUES (265, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-05 21:30:00');
INSERT INTO `schedule_job_log` VALUES (266, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-05 22:00:00');
INSERT INTO `schedule_job_log` VALUES (267, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-05 22:30:00');
INSERT INTO `schedule_job_log` VALUES (268, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-05 23:00:00');
INSERT INTO `schedule_job_log` VALUES (269, 1, 'testTask', 'renren', 0, NULL, 2, '2021-05-05 23:30:00');
INSERT INTO `schedule_job_log` VALUES (270, 1, 'testTask', 'renren', 0, NULL, 16, '2021-05-06 00:00:00');
INSERT INTO `schedule_job_log` VALUES (271, 1, 'testTask', 'renren', 0, NULL, 3, '2021-05-06 00:30:00');
INSERT INTO `schedule_job_log` VALUES (272, 1, 'testTask', 'renren', 0, NULL, 2, '2021-05-06 01:00:00');
INSERT INTO `schedule_job_log` VALUES (273, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 01:30:00');
INSERT INTO `schedule_job_log` VALUES (274, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 13:00:00');
INSERT INTO `schedule_job_log` VALUES (275, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 13:30:00');
INSERT INTO `schedule_job_log` VALUES (276, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-06 14:00:00');
INSERT INTO `schedule_job_log` VALUES (277, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 14:30:00');
INSERT INTO `schedule_job_log` VALUES (278, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-06 15:00:00');
INSERT INTO `schedule_job_log` VALUES (279, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 15:30:00');
INSERT INTO `schedule_job_log` VALUES (280, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 19:00:00');
INSERT INTO `schedule_job_log` VALUES (281, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 19:30:00');
INSERT INTO `schedule_job_log` VALUES (282, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 20:00:00');
INSERT INTO `schedule_job_log` VALUES (283, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 20:30:00');
INSERT INTO `schedule_job_log` VALUES (284, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-06 21:00:00');
INSERT INTO `schedule_job_log` VALUES (285, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-06 21:30:00');
INSERT INTO `schedule_job_log` VALUES (286, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 22:00:00');
INSERT INTO `schedule_job_log` VALUES (287, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 22:30:00');
INSERT INTO `schedule_job_log` VALUES (288, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-06 23:00:00');
INSERT INTO `schedule_job_log` VALUES (289, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-06 23:30:00');
INSERT INTO `schedule_job_log` VALUES (290, 1, 'testTask', 'renren', 0, NULL, 23, '2021-05-07 00:00:00');
INSERT INTO `schedule_job_log` VALUES (291, 1, 'testTask', 'renren', 0, NULL, 2, '2021-05-07 00:30:00');
INSERT INTO `schedule_job_log` VALUES (292, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-07 01:00:00');
INSERT INTO `schedule_job_log` VALUES (293, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-07 13:00:00');
INSERT INTO `schedule_job_log` VALUES (294, 1, 'testTask', 'renren', 0, NULL, 14, '2021-05-07 13:30:00');
INSERT INTO `schedule_job_log` VALUES (295, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-07 15:00:00');
INSERT INTO `schedule_job_log` VALUES (296, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-07 15:30:00');
INSERT INTO `schedule_job_log` VALUES (297, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-07 16:00:00');
INSERT INTO `schedule_job_log` VALUES (298, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-07 16:30:00');
INSERT INTO `schedule_job_log` VALUES (299, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-07 17:00:00');
INSERT INTO `schedule_job_log` VALUES (300, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-08 11:30:00');
INSERT INTO `schedule_job_log` VALUES (301, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-08 12:00:00');
INSERT INTO `schedule_job_log` VALUES (302, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-23 18:30:00');
INSERT INTO `schedule_job_log` VALUES (303, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-23 19:00:00');
INSERT INTO `schedule_job_log` VALUES (304, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-23 19:30:00');
INSERT INTO `schedule_job_log` VALUES (305, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-23 20:00:00');
INSERT INTO `schedule_job_log` VALUES (306, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-23 20:30:00');
INSERT INTO `schedule_job_log` VALUES (307, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-24 20:00:00');
INSERT INTO `schedule_job_log` VALUES (308, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-24 20:30:00');
INSERT INTO `schedule_job_log` VALUES (309, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-24 21:00:00');
INSERT INTO `schedule_job_log` VALUES (310, 1, 'testTask', 'renren', 0, NULL, 0, '2021-05-24 21:30:00');
INSERT INTO `schedule_job_log` VALUES (311, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-24 23:30:00');
INSERT INTO `schedule_job_log` VALUES (312, 1, 'testTask', 'renren', 0, NULL, 3, '2021-05-25 00:00:00');
INSERT INTO `schedule_job_log` VALUES (313, 1, 'testTask', 'renren', 0, NULL, 2, '2021-05-25 00:30:00');
INSERT INTO `schedule_job_log` VALUES (314, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-25 01:00:00');
INSERT INTO `schedule_job_log` VALUES (315, 1, 'testTask', 'renren', 0, NULL, 1, '2021-05-25 21:00:00');
INSERT INTO `schedule_job_log` VALUES (316, 1, 'testTask', 'renren', 0, NULL, 1, '2021-06-09 18:30:00');
INSERT INTO `schedule_job_log` VALUES (317, 1, 'testTask', 'renren', 0, NULL, 0, '2021-06-09 19:00:00');
INSERT INTO `schedule_job_log` VALUES (318, 1, 'testTask', 'renren', 0, NULL, 0, '2021-06-09 19:30:00');
INSERT INTO `schedule_job_log` VALUES (319, 1, 'testTask', 'renren', 0, NULL, 0, '2021-06-09 20:00:00');
INSERT INTO `schedule_job_log` VALUES (320, 1, 'testTask', 'renren', 0, NULL, 1, '2021-06-09 20:30:00');
INSERT INTO `schedule_job_log` VALUES (321, 1, 'testTask', 'renren', 0, NULL, 1, '2021-06-10 15:00:00');
INSERT INTO `schedule_job_log` VALUES (322, 1, 'testTask', 'renren', 0, NULL, 1, '2021-06-10 15:30:00');
INSERT INTO `schedule_job_log` VALUES (323, 1, 'testTask', 'renren', 0, NULL, 0, '2021-06-14 21:00:00');
INSERT INTO `schedule_job_log` VALUES (324, 1, 'testTask', 'renren', 0, NULL, 0, '2021-06-14 21:30:00');
INSERT INTO `schedule_job_log` VALUES (325, 1, 'testTask', 'renren', 0, NULL, 1, '2021-07-06 10:30:00');
INSERT INTO `schedule_job_log` VALUES (326, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-04 00:30:00');
INSERT INTO `schedule_job_log` VALUES (327, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-04 01:00:00');
INSERT INTO `schedule_job_log` VALUES (328, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-10 21:00:00');
INSERT INTO `schedule_job_log` VALUES (329, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-10 21:30:00');
INSERT INTO `schedule_job_log` VALUES (330, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-10 22:00:00');
INSERT INTO `schedule_job_log` VALUES (331, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-11 20:30:00');
INSERT INTO `schedule_job_log` VALUES (332, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-11 21:00:00');
INSERT INTO `schedule_job_log` VALUES (333, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-11 21:30:00');
INSERT INTO `schedule_job_log` VALUES (334, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-11 23:30:00');
INSERT INTO `schedule_job_log` VALUES (335, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-14 13:30:00');
INSERT INTO `schedule_job_log` VALUES (336, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-14 20:30:00');
INSERT INTO `schedule_job_log` VALUES (337, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-14 21:00:00');
INSERT INTO `schedule_job_log` VALUES (338, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-14 21:30:00');
INSERT INTO `schedule_job_log` VALUES (339, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-14 22:00:00');
INSERT INTO `schedule_job_log` VALUES (340, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-14 22:30:00');
INSERT INTO `schedule_job_log` VALUES (341, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-16 22:00:00');
INSERT INTO `schedule_job_log` VALUES (342, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-17 00:30:00');
INSERT INTO `schedule_job_log` VALUES (343, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-17 01:00:00');
INSERT INTO `schedule_job_log` VALUES (344, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-20 23:30:00');
INSERT INTO `schedule_job_log` VALUES (345, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-21 00:30:00');
INSERT INTO `schedule_job_log` VALUES (346, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-21 01:00:00');
INSERT INTO `schedule_job_log` VALUES (347, 1, 'testTask', 'renren', 0, NULL, 0, '2021-08-21 01:30:00');
INSERT INTO `schedule_job_log` VALUES (348, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-23 21:30:00');
INSERT INTO `schedule_job_log` VALUES (349, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-26 14:30:00');
INSERT INTO `schedule_job_log` VALUES (350, 1, 'testTask', 'renren', 0, NULL, 1, '2021-08-31 18:30:00');
INSERT INTO `schedule_job_log` VALUES (351, 1, 'testTask', 'renren', 0, NULL, 0, '2021-09-05 19:00:00');
INSERT INTO `schedule_job_log` VALUES (352, 1, 'testTask', 'renren', 0, NULL, 1, '2021-09-05 19:30:00');
INSERT INTO `schedule_job_log` VALUES (353, 1, 'testTask', 'renren', 0, NULL, 1, '2021-09-23 23:30:01');
INSERT INTO `schedule_job_log` VALUES (354, 1, 'testTask', 'renren', 0, NULL, 0, '2021-09-24 01:30:00');
INSERT INTO `schedule_job_log` VALUES (355, 1, 'testTask', 'renren', 0, NULL, 0, '2021-09-24 15:30:00');
INSERT INTO `schedule_job_log` VALUES (356, 1, 'testTask', 'renren', 0, NULL, 0, '2021-09-24 16:00:00');
INSERT INTO `schedule_job_log` VALUES (357, 1, 'testTask', 'renren', 0, NULL, 1, '2022-04-24 22:30:00');
INSERT INTO `schedule_job_log` VALUES (358, 1, 'testTask', 'renren', 0, NULL, 0, '2022-04-24 23:00:00');
INSERT INTO `schedule_job_log` VALUES (359, 1, 'testTask', 'renren', 0, NULL, 0, '2022-04-24 23:30:00');
INSERT INTO `schedule_job_log` VALUES (360, 1, 'testTask', 'renren', 0, NULL, 11, '2022-04-25 00:00:00');
INSERT INTO `schedule_job_log` VALUES (361, 1, 'testTask', 'renren', 0, NULL, 0, '2022-04-25 00:30:00');
INSERT INTO `schedule_job_log` VALUES (362, 1, 'testTask', 'renren', 0, NULL, 0, '2022-04-25 01:00:00');
INSERT INTO `schedule_job_log` VALUES (363, 1, 'testTask', 'renren', 0, NULL, 1, '2022-04-25 01:30:00');
INSERT INTO `schedule_job_log` VALUES (364, 1, 'testTask', 'renren', 0, NULL, 0, '2022-04-25 08:30:00');
INSERT INTO `schedule_job_log` VALUES (365, 1, 'testTask', 'renren', 0, NULL, 1, '2022-04-25 09:30:00');
INSERT INTO `schedule_job_log` VALUES (366, 1, 'testTask', 'renren', 0, NULL, 1, '2022-04-25 11:00:00');

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------
INSERT INTO `sys_captcha` VALUES ('0143f70a-06a5-4603-8527-5a5a5c812d5c', '26cm3', '2021-04-06 20:16:40');
INSERT INTO `sys_captcha` VALUES ('01dd96fb-dfa3-4e3f-883f-af07b47f34b3', 'a56yc', '2021-05-07 12:49:06');
INSERT INTO `sys_captcha` VALUES ('01f5e255-b475-4cfb-873c-ebb389ac3f9a', 'fywg2', '2021-04-24 18:40:57');
INSERT INTO `sys_captcha` VALUES ('02e0776f-f762-433b-8462-54adcf0f01e0', '78c3a', '2021-04-24 16:11:43');
INSERT INTO `sys_captcha` VALUES ('11abeb37-31f7-4f77-8806-525a8d220692', 'f37dm', '2021-04-14 13:25:36');
INSERT INTO `sys_captcha` VALUES ('13a435d1-f790-4db6-8727-992b4d895837', '6b54f', '2021-04-24 16:36:32');
INSERT INTO `sys_captcha` VALUES ('13cb9f03-04cc-48d1-8929-ffdbc4236260', 'dgedg', '2021-04-24 18:36:31');
INSERT INTO `sys_captcha` VALUES ('1925d605-feea-4834-8d6e-59984aeff02b', '87wyn', '2021-04-06 20:16:37');
INSERT INTO `sys_captcha` VALUES ('1d77e62d-5945-4bb2-899a-6e81f5d033fb', 'x7pn3', '2021-04-12 16:56:17');
INSERT INTO `sys_captcha` VALUES ('1ee48fa8-e0bc-4434-8997-92403bc10139', '6ew2w', '2021-08-06 00:51:24');
INSERT INTO `sys_captcha` VALUES ('1fc29df1-38d2-44b9-89ad-aaa0c00332c2', 'nexyg', '2021-08-06 00:51:28');
INSERT INTO `sys_captcha` VALUES ('215d6ffa-5db7-4df4-8b2f-d672e2d72636', 'ngg24', '2021-04-24 18:33:33');
INSERT INTO `sys_captcha` VALUES ('22a6fc4c-a505-40b6-8f9f-7bde332a187d', 'ewdp6', '2021-04-24 18:26:50');
INSERT INTO `sys_captcha` VALUES ('24c284d6-eb1a-43ac-84a2-bfe125f918a3', '5d38x', '2021-04-24 16:32:18');
INSERT INTO `sys_captcha` VALUES ('25be8f48-87dc-4105-83b4-61858ebc7627', 'f33g3', '2021-04-24 16:11:35');
INSERT INTO `sys_captcha` VALUES ('262ecc92-d8fb-42ba-86cc-be346ff57972', 'mann6', '2021-08-26 14:35:53');
INSERT INTO `sys_captcha` VALUES ('2c16a321-98d0-4209-8484-99a53eb1fbdd', '6nna2', '2021-04-24 18:40:50');
INSERT INTO `sys_captcha` VALUES ('2d4aed1f-9c7a-4a47-8165-eeeaca37ff78', '3cw3m', '2021-04-24 18:25:44');
INSERT INTO `sys_captcha` VALUES ('2e11131e-99c5-4f54-8f30-85a358fa16af', 'en6yb', '2021-04-12 16:56:25');
INSERT INTO `sys_captcha` VALUES ('3087674b-fb4d-428e-8ef8-40fc9d32bafa', 'w6mxd', '2021-05-25 20:50:06');
INSERT INTO `sys_captcha` VALUES ('30c3e7f9-149b-4029-8f56-6fa23813b100', 'x2we5', '2021-04-24 18:42:27');
INSERT INTO `sys_captcha` VALUES ('34de7b84-1127-4fd4-89b0-3fcdaf128d72', '3xg3f', '2021-04-24 16:32:40');
INSERT INTO `sys_captcha` VALUES ('3a4450ba-a9b1-4abc-8db1-398957916d58', 'fnfyn', '2021-04-24 16:34:28');
INSERT INTO `sys_captcha` VALUES ('3bbb776a-0819-40b2-893a-886a9bc0340d', 'cyc7f', '2021-04-12 17:41:09');
INSERT INTO `sys_captcha` VALUES ('3be6ddf3-2461-4a89-88f9-091edf2142c0', 'f537c', '2021-04-12 18:39:44');
INSERT INTO `sys_captcha` VALUES ('3d511454-2bb4-407e-82ad-493a25743c4f', 'dmnpb', '2021-04-24 19:01:49');
INSERT INTO `sys_captcha` VALUES ('4349f880-8eed-40d7-8ed9-6181dde3204b', 'gnc5a', '2021-04-24 16:21:47');
INSERT INTO `sys_captcha` VALUES ('48384171-9b5f-4b0b-8526-7b4fa178a1bc', 'wn6p5', '2021-04-06 20:16:37');
INSERT INTO `sys_captcha` VALUES ('48c76a25-888b-4249-8f26-41a3010745db', 'adby5', '2021-04-24 15:57:27');
INSERT INTO `sys_captcha` VALUES ('4aa36a60-3137-42b7-8152-a62bf97b5f7a', 'a4n6x', '2021-04-24 16:42:09');
INSERT INTO `sys_captcha` VALUES ('4e15e0c9-1727-4323-85ea-2e278896530f', 'm32c4', '2021-04-25 11:22:51');
INSERT INTO `sys_captcha` VALUES ('4fd38e29-1b3b-4f14-8aa1-37f1816b7950', '2b43c', '2021-04-24 16:29:57');
INSERT INTO `sys_captcha` VALUES ('516c9a98-b3e6-4057-8a60-ab1964cb1417', 'dgc2f', '2021-04-24 16:33:48');
INSERT INTO `sys_captcha` VALUES ('51e9c19a-f584-4217-8761-953929369c98', 'f464n', '2021-04-12 17:41:09');
INSERT INTO `sys_captcha` VALUES ('54923256-4b83-43d4-81bb-8e809124383c', '8y8yp', '2021-04-24 16:38:41');
INSERT INTO `sys_captcha` VALUES ('549cd105-abcc-40cf-8cb0-5bc3d96dfdf3', 'w3n3n', '2021-04-12 18:12:11');
INSERT INTO `sys_captcha` VALUES ('5a35bba5-9e40-4753-851a-72e7a98e0f00', '24f5x', '2021-04-25 23:10:08');
INSERT INTO `sys_captcha` VALUES ('5b83be2e-c84a-42a2-888e-b712d5e71d55', 'cdn7g', '2021-04-12 20:54:06');
INSERT INTO `sys_captcha` VALUES ('5d947987-f289-4e6f-8a31-2fe0688807fa', 'cmwn8', '2021-04-24 16:42:24');
INSERT INTO `sys_captcha` VALUES ('5e9565c8-c757-4ec7-8bda-3b8cfdbde866', 'f4fd8', '2022-04-25 01:43:30');
INSERT INTO `sys_captcha` VALUES ('63a70632-b567-4bbc-8792-f19a60eb6e38', 'an6d7', '2021-04-24 16:31:22');
INSERT INTO `sys_captcha` VALUES ('69051aa4-66f3-466b-814f-3c4e4a8c235f', 'e8fnw', '2021-04-24 19:06:41');
INSERT INTO `sys_captcha` VALUES ('6b2daf63-22ae-4829-8bbd-cc1d0c02dcda', 'x4cxw', '2021-04-13 21:22:06');
INSERT INTO `sys_captcha` VALUES ('6d91090d-1487-4532-80d2-a5f23896192c', 'n5nd8', '2021-04-24 16:30:20');
INSERT INTO `sys_captcha` VALUES ('70c617ad-6fe8-4683-8312-3e5443df6a88', '4memn', '2021-04-10 15:29:02');
INSERT INTO `sys_captcha` VALUES ('72b50493-dbbf-4ee3-8e21-c72dbc595dc9', 'fpdx3', '2021-08-06 00:51:29');
INSERT INTO `sys_captcha` VALUES ('74901192-c4b4-4a42-8a2c-bd91065d7a5b', '2mexg', '2021-06-14 20:58:58');
INSERT INTO `sys_captcha` VALUES ('7938b6f4-70b7-47b7-8be3-f4097e784e55', 'n7ncb', '2021-04-24 18:32:44');
INSERT INTO `sys_captcha` VALUES ('7ee80180-158d-4dd0-8f4f-b4d4d02226f2', 'dcnng', '2021-04-25 23:58:48');
INSERT INTO `sys_captcha` VALUES ('84238f88-3ca8-4800-8485-0484b89f6683', '3bppd', '2021-04-12 17:41:10');
INSERT INTO `sys_captcha` VALUES ('898a0f9c-ed48-4fe4-85bd-81edc4bf42e9', 'aepcn', '2021-04-06 20:16:34');
INSERT INTO `sys_captcha` VALUES ('8abef560-8074-4968-895b-0a69d5fd73e0', '3e5xn', '2021-04-24 16:23:14');
INSERT INTO `sys_captcha` VALUES ('90923e7d-0907-4819-8262-dfdc4be21a22', '73ebw', '2021-04-24 16:41:28');
INSERT INTO `sys_captcha` VALUES ('90d5f3df-7cab-4203-84ed-1747ed79a23b', '3p4wn', '2021-04-24 16:30:30');
INSERT INTO `sys_captcha` VALUES ('918accfc-1703-4c9b-814e-aaa3d7c504cb', 'amcgy', '2021-04-26 16:40:28');
INSERT INTO `sys_captcha` VALUES ('942e744c-7201-4178-8188-44b491bf5d06', 'd3eey', '2021-04-24 18:41:21');
INSERT INTO `sys_captcha` VALUES ('95bc4ed2-e30b-4fbd-8f6b-ce7358d2fcd9', '4f4p5', '2021-04-24 16:35:52');
INSERT INTO `sys_captcha` VALUES ('9709756d-8584-41d0-839d-0a70b1e0eb09', 'f8pdc', '2021-04-24 16:31:52');
INSERT INTO `sys_captcha` VALUES ('980d8708-d5cd-4cf8-8beb-4bc5d67a9494', 'ybe2a', '2021-04-24 16:32:49');
INSERT INTO `sys_captcha` VALUES ('aa001206-355c-477c-8c19-a7e2bc3443c4', 'wn75w', '2021-04-24 16:12:36');
INSERT INTO `sys_captcha` VALUES ('aa2bf1ce-14b9-4fff-8264-ca2f71f2b754', 'w44n3', '2021-04-24 18:37:04');
INSERT INTO `sys_captcha` VALUES ('ae0dd148-5d01-49d2-8091-2baad4906f85', 'wgyp2', '2021-08-06 00:42:53');
INSERT INTO `sys_captcha` VALUES ('b76bb666-f0e2-4fa0-8985-13edc0284a90', 'amnf6', '2021-04-24 16:32:04');
INSERT INTO `sys_captcha` VALUES ('bf2c707b-bfac-43aa-8f66-41dc8f1d0756', 'na878', '2021-04-24 16:42:47');
INSERT INTO `sys_captcha` VALUES ('c16a8d73-3130-4273-8339-5f664c0d5cb1', 'n3pn3', '2021-04-24 20:58:13');
INSERT INTO `sys_captcha` VALUES ('c300f045-ae66-4338-80ec-b77163ad3c5b', '38b4a', '2021-06-14 21:34:22');
INSERT INTO `sys_captcha` VALUES ('c452eb9a-e2f3-40b2-868c-3529b4de6964', '5paxc', '2021-04-24 18:22:04');
INSERT INTO `sys_captcha` VALUES ('c5b3d73b-12bc-4d82-863b-9abc6b708c0c', '655n6', '2021-04-25 12:59:44');
INSERT INTO `sys_captcha` VALUES ('cba09ef8-0d85-4838-8fbd-2d44ac72d9d1', '8dnxa', '2021-04-22 19:34:11');
INSERT INTO `sys_captcha` VALUES ('d0ace48a-4714-43d3-8e79-05af03dc8434', '5ec6p', '2021-04-24 18:31:12');
INSERT INTO `sys_captcha` VALUES ('d1de422d-a951-4782-89f8-e2298a0e229d', '4dmed', '2021-04-10 15:28:37');
INSERT INTO `sys_captcha` VALUES ('d1ff4550-ab01-4a7d-8a54-7cf5ae2db8dc', 'pb4mn', '2021-04-10 15:28:47');
INSERT INTO `sys_captcha` VALUES ('d7e3a7c4-8c6a-4d07-80a1-026fccfe8efe', 'b68xp', '2021-08-14 01:47:52');
INSERT INTO `sys_captcha` VALUES ('da3ad6ad-951d-4535-8012-2e1d052b6c89', 'pwya7', '2021-04-24 16:15:04');
INSERT INTO `sys_captcha` VALUES ('db5debd1-c400-409d-86f3-85bacd3de9a3', 'xg73e', '2021-04-06 20:16:38');
INSERT INTO `sys_captcha` VALUES ('dc9c971a-91fc-4884-8974-94701b09da59', 'p7b2y', '2021-04-24 16:43:59');
INSERT INTO `sys_captcha` VALUES ('dca87a06-90fe-4ada-8fde-6e9a92d18970', 'cgmpf', '2021-04-12 17:39:59');
INSERT INTO `sys_captcha` VALUES ('ddcfe005-c368-4e62-88aa-d112226891d2', 'p28b3', '2021-04-24 16:41:49');
INSERT INTO `sys_captcha` VALUES ('dfa42d19-428f-4675-87cd-b4452bd9b794', 'c3b4y', '2021-08-06 00:51:30');
INSERT INTO `sys_captcha` VALUES ('e36dec19-27b2-4693-8c84-6980bab4fc49', 'm4n22', '2021-04-24 18:42:30');
INSERT INTO `sys_captcha` VALUES ('fbb885f5-4e50-40f6-86b1-03abf01884b4', 'c4wbm', '2021-04-24 18:39:28');
INSERT INTO `sys_captcha` VALUES ('fc772dd1-3ab1-47fc-80b4-6f944ea9b49d', 'a7326', '2021-08-06 00:51:29');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}', 0, '云存储配置信息');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":31,\"parentId\":0,\"name\":\"商品系统\",\"url\":\"\",\"perms\":\"\",\"type\":0,\"icon\":\"editor\",\"orderNum\":0}]', 6, '0:0:0:0:0:0:0:1', '2021-04-12 16:53:35');
INSERT INTO `sys_log` VALUES (2, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":32,\"parentId\":31,\"name\":\"商品分类\",\"url\":\"product/category\",\"perms\":\"\",\"type\":1,\"icon\":\"menu\",\"orderNum\":0}]', 5, '0:0:0:0:0:0:0:1', '2021-04-12 16:56:26');
INSERT INTO `sys_log` VALUES (3, 'admin', '保存菜单', 'io.renren.modules.sys.controller.SysMenuController.save()', '[{\"menuId\":33,\"parentId\":31,\"name\":\"品牌管理\",\"url\":\"product/brand\",\"perms\":\"\",\"type\":1,\"icon\":\"editor\",\"orderNum\":0}]', 9, '0:0:0:0:0:0:0:1', '2021-04-13 18:13:51');
INSERT INTO `sys_log` VALUES (4, 'admin', '保存用户', 'io.renren.modules.sys.controller.SysUserController.save()', '[{\"userId\":2,\"username\":\"LJL\",\"password\":\"2ebedcf33885e9c23e13ff301288dfb5c53018d3414cd6b51e0bae65150496ed\",\"salt\":\"1mJFrkrDeQk2fVmLJfmu\",\"email\":\"18066550996@163.com\",\"mobile\":\"18066550996\",\"status\":1,\"roleIdList\":[],\"createUserId\":1,\"createTime\":\"Apr 22, 2021 11:15:32 AM\"}]', 209, '0:0:0:0:0:0:0:1', '2021-04-22 11:15:33');
INSERT INTO `sys_log` VALUES (5, 'admin', '修改密码', 'io.renren.modules.sys.controller.SysUserController.password()', '[{\"password\":\"denfwefoewf\",\"newPassword\":\"lijiale123\"}]', 59, '0:0:0:0:0:0:0:1', '2021-04-24 18:53:57');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 'system', 0);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'sys/user', NULL, 1, 'admin', 1);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'sys/role', NULL, 1, 'role', 2);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'sys/menu', NULL, 1, 'menu', 3);
INSERT INTO `sys_menu` VALUES (5, 1, 'SQL监控', 'http://localhost:8080/renren-fast/druid/sql.html', NULL, 1, 'sql', 4);
INSERT INTO `sys_menu` VALUES (6, 1, '定时任务', 'job/schedule', NULL, 1, 'job', 5);
INSERT INTO `sys_menu` VALUES (7, 6, '查看', NULL, 'sys:schedule:list,sys:schedule:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (8, 6, '新增', NULL, 'sys:schedule:save', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (9, 6, '修改', NULL, 'sys:schedule:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, 6, '删除', NULL, 'sys:schedule:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, 6, '暂停', NULL, 'sys:schedule:pause', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (12, 6, '恢复', NULL, 'sys:schedule:resume', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (13, 6, '立即执行', NULL, 'sys:schedule:run', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 6, '日志列表', NULL, 'sys:schedule:log', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (27, 1, '参数管理', 'sys/config', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'config', 6);
INSERT INTO `sys_menu` VALUES (29, 1, '系统日志', 'sys/log', 'sys:log:list', 1, 'log', 7);
INSERT INTO `sys_menu` VALUES (30, 1, '文件上传', 'oss/oss', 'sys:oss:all', 1, 'oss', 6);
INSERT INTO `sys_menu` VALUES (31, 0, '商品系统', '', '', 0, 'editor', 0);
INSERT INTO `sys_menu` VALUES (32, 31, '分类维护', 'product/category', '', 1, 'menu', 0);
INSERT INTO `sys_menu` VALUES (34, 31, '品牌管理', 'product/brand', '', 1, 'editor', 0);
INSERT INTO `sys_menu` VALUES (37, 31, '平台属性', '', '', 0, 'system', 0);
INSERT INTO `sys_menu` VALUES (38, 37, '属性分组', 'product/attrgroup', '', 1, 'tubiao', 0);
INSERT INTO `sys_menu` VALUES (39, 37, '规格参数', 'product/baseattr', '', 1, 'log', 0);
INSERT INTO `sys_menu` VALUES (40, 37, '销售属性', 'product/saleattr', '', 1, 'zonghe', 0);
INSERT INTO `sys_menu` VALUES (41, 31, '商品维护', 'product/spu', '', 0, 'zonghe', 0);
INSERT INTO `sys_menu` VALUES (42, 0, '优惠营销', '', '', 0, 'mudedi', 0);
INSERT INTO `sys_menu` VALUES (43, 0, '库存系统', '', '', 0, 'shouye', 0);
INSERT INTO `sys_menu` VALUES (44, 0, '订单系统', '', '', 0, 'config', 0);
INSERT INTO `sys_menu` VALUES (45, 0, '用户系统', '', '', 0, 'admin', 0);
INSERT INTO `sys_menu` VALUES (46, 0, '内容管理', '', '', 0, 'sousuo', 0);
INSERT INTO `sys_menu` VALUES (47, 42, '优惠券管理', 'coupon/coupon', '', 1, 'zhedie', 0);
INSERT INTO `sys_menu` VALUES (48, 42, '发放记录', 'coupon/history', '', 1, 'sql', 0);
INSERT INTO `sys_menu` VALUES (49, 42, '专题活动', 'coupon/subject', '', 1, 'tixing', 0);
INSERT INTO `sys_menu` VALUES (50, 42, '秒杀活动', 'coupon/seckill', '', 1, 'daohang', 0);
INSERT INTO `sys_menu` VALUES (51, 42, '积分维护', 'coupon/bounds', '', 1, 'geren', 0);
INSERT INTO `sys_menu` VALUES (52, 42, '满减折扣', 'coupon/full', '', 1, 'shoucang', 0);
INSERT INTO `sys_menu` VALUES (53, 43, '仓库维护', 'ware/wareinfo', '', 1, 'shouye', 0);
INSERT INTO `sys_menu` VALUES (54, 43, '库存工作单', 'ware/task', '', 1, 'log', 0);
INSERT INTO `sys_menu` VALUES (55, 43, '商品库存', 'ware/sku', '', 1, 'jiesuo', 0);
INSERT INTO `sys_menu` VALUES (56, 44, '订单查询', 'order/order', '', 1, 'zhedie', 0);
INSERT INTO `sys_menu` VALUES (57, 44, '退货单处理', 'order/return', '', 1, 'shanchu', 0);
INSERT INTO `sys_menu` VALUES (58, 44, '等级规则', 'order/settings', '', 1, 'system', 0);
INSERT INTO `sys_menu` VALUES (59, 44, '支付流水查询', 'order/payment', '', 1, 'job', 0);
INSERT INTO `sys_menu` VALUES (60, 44, '退款流水查询', 'order/refund', '', 1, 'mudedi', 0);
INSERT INTO `sys_menu` VALUES (61, 45, '会员列表', 'member/member', '', 1, 'geren', 0);
INSERT INTO `sys_menu` VALUES (62, 45, '会员等级', 'member/level', '', 1, 'tubiao', 0);
INSERT INTO `sys_menu` VALUES (63, 45, '积分变化', 'member/growth', '', 1, 'bianji', 0);
INSERT INTO `sys_menu` VALUES (64, 45, '统计信息', 'member/statistics', '', 1, 'sql', 0);
INSERT INTO `sys_menu` VALUES (65, 46, '首页推荐', 'content/index', '', 1, 'shouye', 0);
INSERT INTO `sys_menu` VALUES (66, 46, '分类热门', 'content/category', '', 1, 'zhedie', 0);
INSERT INTO `sys_menu` VALUES (67, 46, '评论管理', 'content/comments', '', 1, 'pinglun', 0);
INSERT INTO `sys_menu` VALUES (68, 41, 'spu管理', 'product/spu', '', 1, 'config', 0);
INSERT INTO `sys_menu` VALUES (69, 41, '发布商品', 'product/spuadd', '', 1, 'bianji', 0);
INSERT INTO `sys_menu` VALUES (70, 43, '采购单维护', '', '', 0, 'tubiao', 0);
INSERT INTO `sys_menu` VALUES (71, 70, '采购需求', 'ware/purchaseitem', '', 1, 'editor', 0);
INSERT INTO `sys_menu` VALUES (72, 70, '采购单', 'ware/purchase', '', 1, 'menu', 0);
INSERT INTO `sys_menu` VALUES (73, 41, '商品管理', 'product/manager', '', 1, 'zonghe', 0);
INSERT INTO `sys_menu` VALUES (74, 42, '会员价格', 'coupon/memberprice', '', 1, 'admin', 0);
INSERT INTO `sys_menu` VALUES (75, 42, '每日秒杀', 'coupon/seckillsession', '', 1, 'job', 0);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', 1, 1, '2016-11-11 11:11:11');
INSERT INTO `sys_user` VALUES (2, 'LJL', '2ebedcf33885e9c23e13ff301288dfb5c53018d3414cd6b51e0bae65150496ed', '1mJFrkrDeQk2fVmLJfmu', '18066550996@163.com', '18066550996', 1, 1, '2021-04-22 11:15:33');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES (1, 'dadf7b4fec0dda1767ab132be6ffae81', '2022-04-25 23:22:58', '2022-04-25 11:22:58');
INSERT INTO `sys_user_token` VALUES (2, '9e7f1b8b23499eb989d02f633bd29cb6', '2021-04-25 07:06:20', '2021-04-24 19:06:20');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'mark', '13612345678', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2017-03-23 22:37:41');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
