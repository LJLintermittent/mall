package com.learn.common.constant;



/**
 * Description:
 * date: 2021/4/22 11:46
 * Package: com.learn.common.constant
 *
 * @author 李佳乐
 * @version 1.0
 */
public class WareConstant {

    public enum PurchaseStatusEnum {
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        RECEIVED(2, "已领取"),
        FINISHED(3, "已完成"),
        HASERROR(4, "有异常");

        private int code;
        private String msg;

        PurchaseStatusEnum(int code, String msg) {
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

    public enum PurchaseDetailsStatusEnum {
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"),
        FINISHED(3, "已完成"),
        BUYERROR(4, "采购失败");

        private int code;
        private String msg;

        PurchaseDetailsStatusEnum(int code, String msg) {
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

    public enum PurchaseDetailExceptionEnum {
        ERROR(996, "您输入的商品ID不存在"),
        MERGE(1024,"该采购需求状态不是新建状态，无法合并");
        private int code;
        private String msg;

        PurchaseDetailExceptionEnum(int code, String msg) {
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
