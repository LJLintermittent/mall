/*
 Navicat Premium Data Transfer

 Source Server         : mall-电商项目开发服务器
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 192.168.190.132:3307
 Source Schema         : mall_wms

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 05/05/2022 11:01:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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

-- ----------------------------
-- Table structure for wms_purchase
-- ----------------------------
DROP TABLE IF EXISTS `wms_purchase`;
CREATE TABLE `wms_purchase`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assignee_id` bigint(20) NULL DEFAULT NULL,
  `assignee_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `priority` int(4) NULL DEFAULT NULL,
  `status` int(4) NULL DEFAULT NULL,
  `ware_id` bigint(20) NULL DEFAULT NULL,
  `amount` decimal(18, 4) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_purchase
-- ----------------------------
INSERT INTO `wms_purchase` VALUES (21, 2, 'LJL', '18066550996', 1, 1, NULL, 36395.0000, '2021-04-23 11:31:55', '2021-04-24 12:44:57');
INSERT INTO `wms_purchase` VALUES (22, 1, 'admin', '13612345678', 1, 1, NULL, 184774.0000, '2021-04-23 12:08:26', '2021-04-23 12:19:47');
INSERT INTO `wms_purchase` VALUES (23, 2, 'LJL', '18066550996', 1, 3, NULL, 22797.0000, '2021-04-23 12:36:25', '2021-04-23 12:38:06');
INSERT INTO `wms_purchase` VALUES (24, 2, 'LJL', '18066550996', 1, 3, NULL, 6799.0000, '2021-04-23 12:39:41', '2021-04-24 02:07:31');
INSERT INTO `wms_purchase` VALUES (25, 1, 'admin', '13612345678', 1, 3, NULL, 7599.0000, '2021-04-23 23:59:58', '2021-04-24 02:15:48');
INSERT INTO `wms_purchase` VALUES (30, 2, 'LJL', '18066550996', 1, 4, NULL, 75990.0000, '2021-04-24 14:36:38', '2021-04-24 14:42:31');
INSERT INTO `wms_purchase` VALUES (31, 1, 'admin', '13612345678', 1, 3, NULL, 104980.0000, '2021-04-24 14:57:34', '2021-04-24 15:07:53');
INSERT INTO `wms_purchase` VALUES (32, 2, 'LJL', '18066550996', 1, 3, NULL, 165960.0000, '2021-04-25 23:43:06', '2021-04-25 23:47:41');
INSERT INTO `wms_purchase` VALUES (33, 2, 'LJL', '18066550996', 1, 1, NULL, 24894.0000, '2021-04-26 16:26:18', '2021-04-26 16:32:02');

-- ----------------------------
-- Table structure for wms_purchase_detail
-- ----------------------------
DROP TABLE IF EXISTS `wms_purchase_detail`;
CREATE TABLE `wms_purchase_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_id` bigint(20) NULL DEFAULT NULL COMMENT '采购单id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '采购商品id',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '采购数量',
  `sku_price` decimal(18, 4) NULL DEFAULT NULL COMMENT '采购金额',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态[0新建，1已分配，2正在采购，3已完成，4采购失败]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_purchase_detail
-- ----------------------------
INSERT INTO `wms_purchase_detail` VALUES (39, 22, 1, 10, 67990.0000, 1, 1);
INSERT INTO `wms_purchase_detail` VALUES (40, 22, 2, 10, 75990.0000, 2, 1);
INSERT INTO `wms_purchase_detail` VALUES (41, 22, 3, 6, 40794.0000, 1, 1);
INSERT INTO `wms_purchase_detail` VALUES (43, 21, 4, 2, 15198.0000, 1, 1);
INSERT INTO `wms_purchase_detail` VALUES (44, 21, 5, 1, 6799.0000, 1, 1);
INSERT INTO `wms_purchase_detail` VALUES (46, 21, 6, 1, 7599.0000, 1, 1);
INSERT INTO `wms_purchase_detail` VALUES (48, 21, 7, 1, 6799.0000, 1, 1);
INSERT INTO `wms_purchase_detail` VALUES (49, 23, 8, 3, 22797.0000, 1, 3);
INSERT INTO `wms_purchase_detail` VALUES (50, 24, 9, 1, 6799.0000, 1, 3);
INSERT INTO `wms_purchase_detail` VALUES (51, 25, 8, 1, 7599.0000, 1, 3);
INSERT INTO `wms_purchase_detail` VALUES (54, 30, 10, 10, 75990.0000, 3, 4);
INSERT INTO `wms_purchase_detail` VALUES (55, 31, 11, 10, 49990.0000, 2, 3);
INSERT INTO `wms_purchase_detail` VALUES (56, 31, 12, 10, 54990.0000, 2, 3);
INSERT INTO `wms_purchase_detail` VALUES (57, 32, 15, 10, 39990.0000, 2, 3);
INSERT INTO `wms_purchase_detail` VALUES (58, 32, 16, 10, 42990.0000, 2, 3);
INSERT INTO `wms_purchase_detail` VALUES (59, 32, 17, 10, 39990.0000, 2, 3);
INSERT INTO `wms_purchase_detail` VALUES (60, 32, 18, 10, 42990.0000, 2, 3);
INSERT INTO `wms_purchase_detail` VALUES (61, 33, 15, 3, 11997.0000, 2, 1);
INSERT INTO `wms_purchase_detail` VALUES (62, 33, 16, 3, 12897.0000, 2, 1);

-- ----------------------------
-- Table structure for wms_ware_info
-- ----------------------------
DROP TABLE IF EXISTS `wms_ware_info`;
CREATE TABLE `wms_ware_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库地址',
  `areacode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_ware_info
-- ----------------------------
INSERT INTO `wms_ware_info` VALUES (1, '西安莲湖总仓枣园分仓', '西安市莲湖区汉城壹号', '1001');
INSERT INTO `wms_ware_info` VALUES (2, '西安临潼总仓', '西安市临潼区西安科技大学', '1002');
INSERT INTO `wms_ware_info` VALUES (3, '西安雁塔总仓', '西安市雁塔区', '1003');

-- ----------------------------
-- Table structure for wms_ware_order_task
-- ----------------------------
DROP TABLE IF EXISTS `wms_ware_order_task`;
CREATE TABLE `wms_ware_order_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT 'order_id',
  `order_sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'order_sn',
  `consignee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `consignee_tel` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `delivery_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送地址',
  `order_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `payment_way` tinyint(1) NULL DEFAULT NULL COMMENT '付款方式【 1:在线付款 2:货到付款】',
  `task_status` tinyint(2) NULL DEFAULT NULL COMMENT '任务状态',
  `order_body` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单描述',
  `tracking_no` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `task_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工作单备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存工作单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_ware_order_task
-- ----------------------------
INSERT INTO `wms_ware_order_task` VALUES (6, NULL, '202105191645291491394937239145893889', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (7, NULL, '202105191807588391394957999654567937', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (8, NULL, '202105191926327511394977771230453762', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (9, NULL, '202105192000558181394986424364769281', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (10, NULL, '202105201614148531395291765661102082', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (11, NULL, '202105201619291041395293083695968258', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (12, NULL, '202105202150482121395376462734852097', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (13, NULL, '202105202153227541395377110914203649', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (14, NULL, '202105202154375131395377424476176385', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (15, NULL, '202105202240543831395389071525715970', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (16, NULL, '202105202243274571395389713564585985', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (17, NULL, '202105202316185561395397980969828354', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (18, NULL, '202105202319377091395398816257081345', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (19, NULL, '202105202340437971395404126598266882', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (20, NULL, '202105211310381671395607946179641345', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (21, NULL, '202105211409145161395622694816325633', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (22, NULL, '202105211416037431395624411238866945', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (23, NULL, '202105211428170931395627487131717634', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (24, NULL, '202105211431342521395628314063917058', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (25, NULL, '202105211434134121395628981629341698', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (26, NULL, '202105251251203641397052641464295425', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (29, NULL, '202105251503062831397085801287901185', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (30, NULL, '202105251504330401397086165173133313', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (31, NULL, '202105251946140951397157053344100354', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (33, NULL, '202106092031082361402604171365982209', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (34, NULL, '202106101505335671402884624895483906', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (36, NULL, '202106101530561371402891011008585730', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (37, NULL, '202106101533181931402891606834634754', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (42, NULL, '202106101536393311402892450481168386', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (43, NULL, '202106101537474641402892736230711298', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (44, NULL, '202106101539288321402893161419890689', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (46, NULL, '202106101542021281402893804381528066', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (47, NULL, '202106101543184691402894124582985729', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (48, NULL, '202106142131486101404431379541786626', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (49, NULL, '202107061032277221412237982194212865', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (50, NULL, '202107211614130281417759805665296385', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (53, NULL, '202108210044247531428759836497018882', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (54, NULL, '202108210046277901428760352555794433', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (59, NULL, '202108261424287301430778152262574082', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (60, NULL, '202204242302523361518244085863821314', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wms_ware_order_task` VALUES (61, NULL, '202204250126371961518280261144530946', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for wms_ware_order_task_detail
-- ----------------------------
DROP TABLE IF EXISTS `wms_ware_order_task_detail`;
CREATE TABLE `wms_ware_order_task_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku_name',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '购买个数',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '工作单id',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `lock_status` int(1) NULL DEFAULT NULL COMMENT '1-已锁定  2-已解锁  3-扣减',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存工作单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_ware_order_task_detail
-- ----------------------------
INSERT INTO `wms_ware_order_task_detail` VALUES (8, 8, '', 2, 6, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (9, 8, '', 1, 7, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (10, 8, '', 1, 8, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (11, 8, '', 1, 9, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (12, 8, '', 1, 10, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (13, 8, '', 1, 11, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (14, 8, '', 1, 12, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (15, 8, '', 1, 13, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (16, 8, '', 1, 14, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (17, 8, '', 1, 15, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (18, 8, '', 1, 16, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (19, 8, '', 1, 17, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (20, 17, '', 1, 18, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (21, 15, '', 1, 18, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (22, 9, '', 1, 19, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (23, 11, '', 1, 19, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (24, 8, '', 2, 20, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (25, 9, '', 1, 21, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (26, 9, '', 1, 22, 1, 1);
INSERT INTO `wms_ware_order_task_detail` VALUES (27, 8, '', 3, 23, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (28, 15, '', 1, 24, 2, 1);
INSERT INTO `wms_ware_order_task_detail` VALUES (29, 17, '', 1, 25, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (30, 17, '', 1, 26, 2, 1);
INSERT INTO `wms_ware_order_task_detail` VALUES (31, 11, '', 1, 29, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (32, 11, '', 1, 30, 2, 1);
INSERT INTO `wms_ware_order_task_detail` VALUES (33, 16, '', 1, 31, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (35, 18, '', 1, 33, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (36, 18, '', 1, 34, 2, 1);
INSERT INTO `wms_ware_order_task_detail` VALUES (37, 15, '', 1, 34, 2, 1);
INSERT INTO `wms_ware_order_task_detail` VALUES (38, 9, '', 1, 36, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (39, 9, '', 1, 37, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (40, 9, '', 1, 42, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (41, 9, '', 1, 43, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (42, 9, '', 1, 44, 1, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (43, 18, '', 1, 46, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (44, 18, '', 1, 47, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (45, 9, '', 1, 48, 1, 1);
INSERT INTO `wms_ware_order_task_detail` VALUES (46, 18, '', 1, 49, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (47, 11, '', 1, 50, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (48, 16, '', 1, 53, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (49, 11, '', 3, 54, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (50, 11, '', 1, 59, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (51, 11, '', 1, 60, 2, 2);
INSERT INTO `wms_ware_order_task_detail` VALUES (52, 11, '', 1, 61, 2, 2);

-- ----------------------------
-- Table structure for wms_ware_sku
-- ----------------------------
DROP TABLE IF EXISTS `wms_ware_sku`;
CREATE TABLE `wms_ware_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `ware_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `stock` int(11) NULL DEFAULT NULL COMMENT '库存数',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku_name',
  `stock_locked` int(11) NULL DEFAULT 0 COMMENT '锁定库存',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sku_id`(`sku_id`) USING BTREE,
  INDEX `ware_id`(`ware_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品库存' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_ware_sku
-- ----------------------------
INSERT INTO `wms_ware_sku` VALUES (11, 8, 1, '西安莲湖总仓枣园分仓', 4, 'Apple iPhone 12 红色 256GB', 0);
INSERT INTO `wms_ware_sku` VALUES (12, 9, 1, '西安莲湖总仓枣园分仓', 1, 'Apple iPhone 12 绿色 128GB', 1);
INSERT INTO `wms_ware_sku` VALUES (13, 12, 2, '西安临潼总仓', 10, '华为 HUAWEI Mate 40 亮黑色 8GB+256GB', 0);
INSERT INTO `wms_ware_sku` VALUES (14, 11, 2, '西安临潼总仓', 10, '华为 HUAWEI Mate 40 亮黑色 8GB+128GB', 0);
INSERT INTO `wms_ware_sku` VALUES (15, 15, 2, '西安临潼总仓', 10, '小米11 蓝色 8GB+128GB', 1);
INSERT INTO `wms_ware_sku` VALUES (16, 16, 2, '西安临潼总仓', 10, '小米11 蓝色 8GB+256GB', 0);
INSERT INTO `wms_ware_sku` VALUES (17, 17, 2, '西安临潼总仓', 10, '小米11 烟紫(素皮) 8GB+128GB', 0);
INSERT INTO `wms_ware_sku` VALUES (18, 18, 2, '西安临潼总仓', 10, '小米11 烟紫(素皮) 8GB+256GB', 1);

SET FOREIGN_KEY_CHECKS = 1;
