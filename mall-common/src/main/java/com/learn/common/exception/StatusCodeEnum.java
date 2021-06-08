package com.learn.common.exception;

/**
 * Description:
 * date: 2021/4/14 15:08
 * Package: com.learn.common.exception
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 错误码定义规则为五位
 * 前两位表示业务场景，最后三位便是错误码
 * 10：通用  + (001：参数格式校验)  10001
 * 11：商品
 * 12：订单
 * 13：购物车
 * 14：物流
 * 15. 用户
 * 16. 库存
 */
@SuppressWarnings({"all"})
public enum StatusCodeEnum {
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验异常"),
    SMS_CODE_EXCEPTION(10002, "验证码发送频率过高"),
    TOO_MANY_REQUEST(10003, "请求流量过大"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    USER_EXIST_EXCEPTION(15000, "用户名已存在"),
    PHONE_EXIST_EXCEPTION(15001, "手机号已存在"),
    LOGIN_EXCEPTION(15002, "用户名(手机号)或密码错误"),
    NO_STOCK_EXCEPTION(16000, "商品库存不足");

    private int code;
    private String msg;

    StatusCodeEnum(int code, String msg) {
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
