package com.learn.mall.thirdparty.service;

/**
 * Description:
 * date: 2021/5/7 18:28
 * Package: com.learn.mall.thirdparty.service
 *
 * @author 李佳乐
 * @version 1.0
 */
public interface SmsService {

    /**
     * 调用阿里云SDK发送短信验证码
     */
    void sendSms(String phone, String code);
}
