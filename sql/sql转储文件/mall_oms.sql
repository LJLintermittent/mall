/*
 Navicat Premium Data Transfer

 Source Server         : mall-电商项目开发服务器
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 192.168.190.132:3307
 Source Schema         : mall_oms

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 05/05/2022 11:00:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT 'member_id',
  `order_sn` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '使用的优惠券',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  `member_username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `total_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '订单总额',
  `pay_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '应付总额',
  `freight_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '运费金额',
  `promotion_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '促销优化金额（促销价、满减、阶梯价）',
  `integration_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '积分抵扣金额',
  `coupon_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '优惠券抵扣金额',
  `discount_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '后台调整订单使用的折扣金额',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】',
  `source_type` tinyint(4) NULL DEFAULT NULL COMMENT '订单来源[0->PC订单；1->app订单]',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】',
  `delivery_company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司(配送方式)',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `auto_confirm_day` int(11) NULL DEFAULT NULL COMMENT '自动确认时间（天）',
  `integration` int(11) NULL DEFAULT NULL COMMENT '可以获得的积分',
  `growth` int(11) NULL DEFAULT NULL COMMENT '可以获得的成长值',
  `bill_type` tinyint(4) NULL DEFAULT NULL COMMENT '发票类型[0->不开发票；1->电子发票；2->纸质发票]',
  `bill_header` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票抬头',
  `bill_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票内容',
  `bill_receiver_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收票人电话',
  `bill_receiver_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收票人邮箱',
  `receiver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `receiver_post_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人邮编',
  `receiver_province` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份/直辖市',
  `receiver_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `receiver_region` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区',
  `receiver_detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `confirm_status` tinyint(4) NULL DEFAULT NULL COMMENT '确认收货状态[0->未确认；1->已确认]',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
  `use_integration` int(11) NULL DEFAULT NULL COMMENT '下单时使用的积分',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '确认收货时间',
  `comment_time` datetime(0) NULL DEFAULT NULL COMMENT '评价时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_sn`(`order_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 124 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order
-- ----------------------------
INSERT INTO `oms_order` VALUES (91, 4, '202106101505335671402884624895483906', NULL, NULL, NULL, 8298.0000, 8304.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 1, NULL, NULL, 7, 8298, 8298, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-10 15:05:34');
INSERT INTO `oms_order` VALUES (93, 4, '202106101530561371402891011008585730', NULL, NULL, NULL, 6799.0000, 6805.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 6799, 6799, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-10 15:30:56');
INSERT INTO `oms_order` VALUES (94, 4, '202106101533181931402891606834634754', NULL, NULL, NULL, 6799.0000, 6805.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 6799, 6799, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-10 15:33:18');
INSERT INTO `oms_order` VALUES (99, 4, '202106101536393311402892450481168386', NULL, NULL, NULL, 6799.0000, 6805.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 6799, 6799, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-10 15:36:39');
INSERT INTO `oms_order` VALUES (100, 4, '202106101537474641402892736230711298', NULL, NULL, NULL, 6799.0000, 6805.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 6799, 6799, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-10 15:37:48');
INSERT INTO `oms_order` VALUES (101, 4, '202106101539288321402893161419890689', NULL, NULL, NULL, 6799.0000, 6805.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 6799, 6799, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-10 15:39:29');
INSERT INTO `oms_order` VALUES (103, 4, '202106101542021281402893804381528066', NULL, NULL, NULL, 4299.0000, 4305.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 4299, 4299, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-10 15:42:02');
INSERT INTO `oms_order` VALUES (104, 4, '202106101543184691402894124582985729', NULL, NULL, NULL, 4299.0000, 4305.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 1, NULL, NULL, 7, 4299, 4299, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-10 15:43:19');
INSERT INTO `oms_order` VALUES (105, 2, '202106142131486101404431379541786626', NULL, NULL, NULL, 6799.0000, 6808.0000, 9.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 1, NULL, NULL, 7, 6799, 6799, NULL, NULL, NULL, NULL, NULL, '郑少伟', '19829385239', '710000', '陕西省', '榆林市', '榆阳区', '至尊贵族煤老板空中楼阁', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-06-14 21:31:49');
INSERT INTO `oms_order` VALUES (106, 5, '202106142137515591404432901856047105', NULL, NULL, NULL, NULL, 2999.0000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order` VALUES (107, 5, '202106142139097481404433229796093953', NULL, NULL, NULL, NULL, 2699.0000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order` VALUES (108, 4, '202107061032277221412237982194212865', NULL, NULL, NULL, 4299.0000, 4302.0000, 3.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 4299, 4299, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066551273', '710000', '陕西省', '西安市', '莲湖区', '枣园东路西控花园', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-07-06 10:32:28');
INSERT INTO `oms_order` VALUES (109, 4, '202107211614130281417759805665296385', NULL, NULL, NULL, 4999.0000, 5005.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 4999, 4999, NULL, NULL, NULL, NULL, NULL, '李佳乐', '18066550996', '710000', '陕西省', '西安市', '莲湖区', '汉城路汉城壹号', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-07-21 16:14:13');
INSERT INTO `oms_order` VALUES (113, 6, '202108210044247531428759836497018882', NULL, NULL, NULL, 4299.0000, 4299.0000, 0.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 4299, 4299, NULL, NULL, NULL, NULL, NULL, 'LJL', '12345678900', '710000', '美国', '洛杉矶', '富人区', '詹姆斯家隔壁', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-08-21 00:44:25');
INSERT INTO `oms_order` VALUES (120, 6, '202108261424287301430778152262574082', NULL, NULL, NULL, 4999.0000, 5005.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 4999, 4999, NULL, NULL, NULL, NULL, NULL, 'LJL', '18066550996', '710000', '美国', '洛杉矶', '富人区', '詹姆斯家隔壁', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2021-08-26 14:24:29');
INSERT INTO `oms_order` VALUES (122, 6, '202204242302523361518244085863821314', NULL, NULL, NULL, 4999.0000, 5005.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 4999, 4999, NULL, NULL, NULL, NULL, NULL, 'LJL', '18066550996', '710000', '陕西省', '西安市', '莲湖区', 'home1', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2022-04-24 23:02:53');
INSERT INTO `oms_order` VALUES (123, 6, '202204250126371961518280261144530946', NULL, NULL, NULL, 4999.0000, 5005.0000, 6.0000, 0.0000, 0.0000, 0.0000, NULL, NULL, NULL, 4, NULL, NULL, 7, 4999, 4999, NULL, NULL, NULL, NULL, NULL, 'LJL', '18066550996', '710000', '陕西省', '西安市', '莲湖区', 'home1', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2022-04-25 01:26:37');

-- ----------------------------
-- Table structure for oms_order_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT 'order_id',
  `order_sn` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'order_sn',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu_id',
  `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu_name',
  `spu_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu_pic',
  `spu_brand` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '商品分类id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品sku编号',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品sku名字',
  `sku_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品sku图片',
  `sku_price` decimal(18, 4) NULL DEFAULT NULL COMMENT '商品sku价格',
  `sku_quantity` int(11) NULL DEFAULT NULL COMMENT '商品购买的数量',
  `sku_attrs_vals` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品销售属性组合（JSON）',
  `promotion_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '商品促销分解金额',
  `coupon_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '优惠券优惠分解金额',
  `integration_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '积分优惠分解金额',
  `real_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '该商品经过优惠后的分解金额',
  `gift_integration` int(11) NULL DEFAULT NULL COMMENT '赠送积分',
  `gift_growth` int(11) NULL DEFAULT NULL COMMENT '赠送成长值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 145 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单项信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_item
-- ----------------------------
INSERT INTO `oms_order_item` VALUES (109, NULL, '202106101505335671402884624895483906', 6, '小米11', NULL, '7', 225, 18, '小米11 烟紫(素皮) 8GB+256GB5G 骁龙888 2K AMOLED四曲面柔性屏 1亿像素 55W有线闪充 50W无线闪充 8GB+256GB 蓝色 游戏手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-25//5057ed14-ee33-4e89-86f5-933418099d70_烟紫.png', 4299.0000, 1, '颜色：烟紫(素皮);版本：8GB+256GB', 0.0000, 0.0000, 0.0000, 4299.0000, 4299, 4299);
INSERT INTO `oms_order_item` VALUES (110, NULL, '202106101505335671402884624895483906', 6, '小米11', NULL, '7', 225, 15, '小米11 蓝色 8GB+128GB5G 骁龙888 2K AMOLED四曲面柔性屏 1亿像素 55W有线闪充 50W无线闪充 8GB+256GB 蓝色 游戏手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-25//dbba9ff6-381a-464a-8db7-796c847c072e_蓝色.png', 3999.0000, 1, '颜色：蓝色;版本：8GB+128GB', 0.0000, 0.0000, 0.0000, 3999.0000, 3999, 3999);
INSERT INTO `oms_order_item` VALUES (114, NULL, '202106101530561371402891011008585730', 2, 'Apple iPhone 12', NULL, '8', 225, 9, 'Apple iPhone 12 绿色 128GB支持移动联通电信5G 双卡双待手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//bc41738d-92f1-4dd9-8994-f430a7799a93_抹茶.png', 6799.0000, 1, '颜色：绿色;版本：128GB', 0.0000, 0.0000, 0.0000, 6799.0000, 6799, 6799);
INSERT INTO `oms_order_item` VALUES (115, NULL, '202106101533181931402891606834634754', 2, 'Apple iPhone 12', NULL, '8', 225, 9, 'Apple iPhone 12 绿色 128GB支持移动联通电信5G 双卡双待手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//bc41738d-92f1-4dd9-8994-f430a7799a93_抹茶.png', 6799.0000, 1, '颜色：绿色;版本：128GB', 0.0000, 0.0000, 0.0000, 6799.0000, 6799, 6799);
INSERT INTO `oms_order_item` VALUES (120, NULL, '202106101536393311402892450481168386', 2, 'Apple iPhone 12', NULL, '8', 225, 9, 'Apple iPhone 12 绿色 128GB支持移动联通电信5G 双卡双待手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//bc41738d-92f1-4dd9-8994-f430a7799a93_抹茶.png', 6799.0000, 1, '颜色：绿色;版本：128GB', 0.0000, 0.0000, 0.0000, 6799.0000, 6799, 6799);
INSERT INTO `oms_order_item` VALUES (121, NULL, '202106101537474641402892736230711298', 2, 'Apple iPhone 12', NULL, '8', 225, 9, 'Apple iPhone 12 绿色 128GB支持移动联通电信5G 双卡双待手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//bc41738d-92f1-4dd9-8994-f430a7799a93_抹茶.png', 6799.0000, 1, '颜色：绿色;版本：128GB', 0.0000, 0.0000, 0.0000, 6799.0000, 6799, 6799);
INSERT INTO `oms_order_item` VALUES (122, NULL, '202106101539288321402893161419890689', 2, 'Apple iPhone 12', NULL, '8', 225, 9, 'Apple iPhone 12 绿色 128GB支持移动联通电信5G 双卡双待手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//bc41738d-92f1-4dd9-8994-f430a7799a93_抹茶.png', 6799.0000, 1, '颜色：绿色;版本：128GB', 0.0000, 0.0000, 0.0000, 6799.0000, 6799, 6799);
INSERT INTO `oms_order_item` VALUES (124, NULL, '202106101542021281402893804381528066', 6, '小米11', NULL, '7', 225, 18, '小米11 烟紫(素皮) 8GB+256GB5G 骁龙888 2K AMOLED四曲面柔性屏 1亿像素 55W有线闪充 50W无线闪充 8GB+256GB 蓝色 游戏手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-25//5057ed14-ee33-4e89-86f5-933418099d70_烟紫.png', 4299.0000, 1, '颜色：烟紫(素皮);版本：8GB+256GB', 0.0000, 0.0000, 0.0000, 4299.0000, 4299, 4299);
INSERT INTO `oms_order_item` VALUES (125, NULL, '202106101543184691402894124582985729', 6, '小米11', NULL, '7', 225, 18, '小米11 烟紫(素皮) 8GB+256GB5G 骁龙888 2K AMOLED四曲面柔性屏 1亿像素 55W有线闪充 50W无线闪充 8GB+256GB 蓝色 游戏手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-25//5057ed14-ee33-4e89-86f5-933418099d70_烟紫.png', 4299.0000, 1, '颜色：烟紫(素皮);版本：8GB+256GB', 0.0000, 0.0000, 0.0000, 4299.0000, 4299, 4299);
INSERT INTO `oms_order_item` VALUES (126, NULL, '202106142131486101404431379541786626', 2, 'Apple iPhone 12', NULL, '8', 225, 9, 'Apple iPhone 12 绿色 128GB支持移动联通电信5G 双卡双待手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//bc41738d-92f1-4dd9-8994-f430a7799a93_抹茶.png', 6799.0000, 1, '颜色：绿色;版本：128GB', 0.0000, 0.0000, 0.0000, 6799.0000, 6799, 6799);
INSERT INTO `oms_order_item` VALUES (127, NULL, '202106142137515591404432901856047105', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 2999.0000, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (128, NULL, '202106142139097481404433229796093953', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 2699.0000, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (129, NULL, '202107061032277221412237982194212865', 6, '小米11', NULL, '7', 225, 18, '小米11 烟紫(素皮) 8GB+256GB5G 骁龙888 2K AMOLED四曲面柔性屏 1亿像素 55W有线闪充 50W无线闪充 8GB+256GB 蓝色 游戏手机', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-25//5057ed14-ee33-4e89-86f5-933418099d70_烟紫.png', 4299.0000, 1, '颜色：烟紫(素皮);版本：8GB+256GB', 0.0000, 0.0000, 0.0000, 4299.0000, 4299, 4299);
INSERT INTO `oms_order_item` VALUES (130, NULL, '202107211614130281417759805665296385', 3, '华为 HUAWEI Mate 40', NULL, '6', 225, 11, '华为 HUAWEI Mate 40 亮黑色 8GB+128GB麒麟9000E SoC芯片 5000万超感知徕卡电影影像 有线无线双超级快充', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//a00520c0-df36-4a05-86ba-6a8fa4a6b0e3_黑色.png', 4999.0000, 1, '颜色：亮黑色;版本：8GB+128GB', 0.0000, 0.0000, 0.0000, 4999.0000, 4999, 4999);
INSERT INTO `oms_order_item` VALUES (133, NULL, '202108210043228751428759576974471170', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1999.0000, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (134, NULL, '202108210044247531428759836497018882', 6, '小米11', NULL, '7', 225, 16, '小米11 蓝色 8GB5G 骁龙888 2K AMOLED四曲面柔性屏 1亿像素 55W有线闪充 50W无线闪充 8GB+256GB 蓝色 游戏手机+256GB', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-25//dbba9ff6-381a-464a-8db7-796c847c072e_蓝色.png', 4299.0000, 1, '颜色：蓝色;版本：8GB+256GB', 0.0000, 0.0000, 0.0000, 4299.0000, 4299, 4299);
INSERT INTO `oms_order_item` VALUES (135, NULL, '202108210046277901428760352555794433', 3, '华为 HUAWEI Mate 40', NULL, '6', 225, 11, '华为 HUAWEI Mate 40 亮黑色 8GB+128GB麒麟9000E SoC芯片 5000万超感知徕卡电影影像 有线无线双超级快充', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//a00520c0-df36-4a05-86ba-6a8fa4a6b0e3_黑色.png', 4999.0000, 3, '颜色：亮黑色;版本：8GB+128GB', 0.0000, 0.0000, 0.0000, 14997.0000, 14997, 14997);
INSERT INTO `oms_order_item` VALUES (139, NULL, '202108261422530821430777751094165506', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 2999.0000, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (141, NULL, '202108261424287301430778152262574082', 3, '华为 HUAWEI Mate 40', NULL, '6', 225, 11, '华为 HUAWEI Mate 40 亮黑色 8GB+128GB麒麟9000E SoC芯片 5000万超感知徕卡电影影像 有线无线双超级快充', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//a00520c0-df36-4a05-86ba-6a8fa4a6b0e3_黑色.png', 4999.0000, 1, '颜色：亮黑色;版本：8GB+128GB', 0.0000, 0.0000, 0.0000, 4999.0000, 4999, 4999);
INSERT INTO `oms_order_item` VALUES (142, NULL, '202108261427020501430778795341643778', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 2999.0000, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (143, NULL, '202204242302523361518244085863821314', 3, '华为 HUAWEI Mate 40', NULL, '6', 225, 11, '华为 HUAWEI Mate 40 亮黑色 8GB+128GB麒麟9000E SoC芯片 5000万超感知徕卡电影影像 有线无线双超级快充', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//a00520c0-df36-4a05-86ba-6a8fa4a6b0e3_黑色.png', 4999.0000, 1, '颜色：亮黑色;版本：8GB+128GB', 0.0000, 0.0000, 0.0000, 4999.0000, 4999, 4999);
INSERT INTO `oms_order_item` VALUES (144, NULL, '202204250126371961518280261144530946', 3, '华为 HUAWEI Mate 40', NULL, '6', 225, 11, '华为 HUAWEI Mate 40 亮黑色 8GB+128GB麒麟9000E SoC芯片 5000万超感知徕卡电影影像 有线无线双超级快充', 'https://mall-ljl.oss-cn-beijing.aliyuncs.com/2021-04-21//a00520c0-df36-4a05-86ba-6a8fa4a6b0e3_黑色.png', 4999.0000, 1, '颜色：亮黑色;版本：8GB+128GB', 0.0000, 0.0000, 0.0000, 4999.0000, 4999, 4999);

-- ----------------------------
-- Table structure for oms_order_operate_history
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_operate_history`;
CREATE TABLE `oms_order_operate_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `operate_man` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人[用户；系统；后台管理员]',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `order_status` tinyint(4) NULL DEFAULT NULL COMMENT '订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单操作历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_return_apply
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_return_apply`;
CREATE TABLE `oms_order_return_apply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT 'order_id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '退货商品id',
  `order_sn` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `member_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员用户名',
  `return_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '退款金额',
  `return_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货人姓名',
  `return_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货人电话',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '申请状态[0->待处理；1->退货中；2->已完成；3->已拒绝]',
  `handle_time` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
  `sku_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_brand` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品牌',
  `sku_attrs_vals` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品销售属性(JSON)',
  `sku_count` int(11) NULL DEFAULT NULL COMMENT '退货数量',
  `sku_price` decimal(18, 4) NULL DEFAULT NULL COMMENT '商品单价',
  `sku_real_price` decimal(18, 4) NULL DEFAULT NULL COMMENT '商品实际支付单价',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原因',
  `description述` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `desc_pics` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '凭证图片，以逗号隔开',
  `handle_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理备注',
  `handle_man` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人员',
  `receive_man` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '收货时间',
  `receive_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货备注',
  `receive_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货电话',
  `company_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司收货地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单退货申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_return_reason
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_return_reason`;
CREATE TABLE `oms_order_return_reason`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货原因名',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退货原因' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_setting
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_setting`;
CREATE TABLE `oms_order_setting`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `flash_order_overtime` int(11) NULL DEFAULT NULL COMMENT '秒杀订单超时关闭时间(分)',
  `normal_order_overtime` int(11) NULL DEFAULT NULL COMMENT '正常订单超时时间(分)',
  `confirm_overtime` int(11) NULL DEFAULT NULL COMMENT '发货后自动确认收货时间（天）',
  `finish_overtime` int(11) NULL DEFAULT NULL COMMENT '自动完成交易时间，不能申请退货（天）',
  `comment_overtime` int(11) NULL DEFAULT NULL COMMENT '订单完成后自动好评时间（天）',
  `member_level` tinyint(2) NULL DEFAULT NULL COMMENT '会员等级【0-不限会员等级，全部通用；其他-对应的其他会员等级】',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单配置信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_payment_info
-- ----------------------------
DROP TABLE IF EXISTS `oms_payment_info`;
CREATE TABLE `oms_payment_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_sn` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号（对外业务号）',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `alipay_trade_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝交易流水号',
  `total_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '支付总金额',
  `subject` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `confirm_time` datetime(0) NULL DEFAULT NULL COMMENT '确认时间',
  `callback_content` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调内容',
  `callback_time` datetime(0) NULL DEFAULT NULL COMMENT '回调时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_sn`(`order_sn`) USING BTREE,
  UNIQUE INDEX `alipay_trade_no`(`alipay_trade_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_payment_info
-- ----------------------------
INSERT INTO `oms_payment_info` VALUES (9, '202106101505335671402884624895483906', NULL, '2021061022001470750501426095', NULL, NULL, 'TRADE_SUCCESS', NULL, NULL, NULL, '2021-06-10 15:06:10');
INSERT INTO `oms_payment_info` VALUES (10, '202106101543184691402894124582985729', NULL, '2021061022001470750501426101', NULL, NULL, 'TRADE_SUCCESS', NULL, NULL, NULL, '2021-06-10 15:44:37');
INSERT INTO `oms_payment_info` VALUES (11, '202106142131486101404431379541786626', NULL, '2021061422001470750501428333', NULL, NULL, 'TRADE_SUCCESS', NULL, NULL, NULL, '2021-06-14 21:32:21');
INSERT INTO `oms_payment_info` VALUES (12, '202106142137515591404432901856047105', NULL, '2021061422001470750501428451', NULL, NULL, 'TRADE_SUCCESS', NULL, NULL, NULL, '2021-06-14 21:38:13');
INSERT INTO `oms_payment_info` VALUES (13, '202106142139097481404433229796093953', NULL, '2021061422001470750501428047', NULL, NULL, 'TRADE_SUCCESS', NULL, NULL, NULL, '2021-06-14 21:39:28');

-- ----------------------------
-- Table structure for oms_refund_info
-- ----------------------------
DROP TABLE IF EXISTS `oms_refund_info`;
CREATE TABLE `oms_refund_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_return_id` bigint(20) NULL DEFAULT NULL COMMENT '退款的订单',
  `refund` decimal(18, 4) NULL DEFAULT NULL COMMENT '退款金额',
  `refund_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款交易流水号',
  `refund_status` tinyint(1) NULL DEFAULT NULL COMMENT '退款状态',
  `refund_channel` tinyint(4) NULL DEFAULT NULL COMMENT '退款渠道[1-支付宝，2-微信，3-银联，4-汇款]',
  `refund_content` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退款信息' ROW_FORMAT = Dynamic;

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
