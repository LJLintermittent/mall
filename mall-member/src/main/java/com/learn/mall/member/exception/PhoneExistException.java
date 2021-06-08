package com.learn.mall.member.exception;

/**
 * Description:
 * date: 2021/5/8 13:48
 * Package: com.learn.mall.member.exception
 *
 * @author 李佳乐
 * @version 1.0
 */
public class PhoneExistException extends RuntimeException {
    public PhoneExistException() {
        super("手机号已存在");
    }
}
