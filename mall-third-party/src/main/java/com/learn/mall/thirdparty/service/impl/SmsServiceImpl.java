package com.learn.mall.thirdparty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.learn.mall.thirdparty.config.SmsConfigProperties;
import com.learn.mall.thirdparty.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**
 * Description:
 * date: 2021/5/7 18:28
 * Package: com.learn.mall.thirdparty.service.impl
 *
 * @author 李佳乐
 * @version 1.0
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsConfigProperties properties;

    /**
     * TODO:接口防刷
     * 验证码再次校验 （存Redis）
     * 防止同一个手机号在60s内通过postman绕过前端校验再次发送验证码
     * <p>
     * key： sms:code:18066550996   value:  998176
     */
    @Override
    public void sendSms(String phone, String code) {
        DefaultProfile profile =
                DefaultProfile.getProfile(
                        properties.getRegionId(),
                        properties.getAccessKeyId(),
                        properties.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", properties.getSignName());
        request.putQueryParameter("TemplateCode", properties.getTemplateCode());
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
