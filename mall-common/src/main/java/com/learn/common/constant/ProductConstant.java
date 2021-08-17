package com.learn.common.constant;

/**
 * Description:
 * date: 2021/4/19 18:41
 * Package: com.learn.common.constant
 *
 * @author 李佳乐
 * @version 1.0
 */
// 商品服务的常量和枚举类
@SuppressWarnings("all")
public class ProductConstant {

    /**
     * 商品属性枚举
     */
    public enum AttrEnum {

        /**
         * 基本属性
         */
        ATTR_TYPE_BASE(1, "基本属性"),


        /**
         * 销售属性
         */
        ATTR_TYPE_SALE(0, "销售属性");

        private int code;

        private String msg;

        AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 商品上架状态枚举
     */
    public enum ProductUpStatusEnum {

        NEW_SPU(0, "新建"),

        SPU_UP(1, "商品上架"),

        SPU_DOWN(2, "商品下架");

        private int code;

        private String msg;

        ProductUpStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
