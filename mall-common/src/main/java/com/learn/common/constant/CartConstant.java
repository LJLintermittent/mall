package com.learn.common.constant;

/**
 * Description:
 * date: 2021/5/10 21:24
 * Package: com.learn.common.constant
 *
 * @author 李佳乐
 * @version 1.0
 */
/*
   购物车服务的常量
 */
public class CartConstant {

    /**
     * 用户登录以后的cookie名字
     */
    public static final String TEMP_USER_COOKIE_NAME = "user-key";

    /**
     * 登录成功的用户的cookie存活时间：30天
     */
    public static final int TEMP_USER_COOKIE_TIMEOUT = 60 * 60 * 24 * 30;

    /**
     * 在redis中的购物车数据的缓存前缀
     */
    public static final String CART_PREFIX = "mall:cart:";
}
