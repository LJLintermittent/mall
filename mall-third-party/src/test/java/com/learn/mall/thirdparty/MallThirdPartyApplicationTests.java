package com.learn.mall.thirdparty;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.learn.mall.thirdparty.service.SmsService;
import com.learn.mall.thirdparty.utils.RandomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

@SuppressWarnings("all")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallThirdPartyApplicationTests {

    @Autowired
    OSSClient ossClient;

    @Autowired
    SmsService smsService;

    @Test
    public void testRandom(){
        String code = RandomUtil.getSixBitRandom();
        System.out.println(code);
    }

    /**
     * 测试阿里云短信服务接口
     */
    @Test
    public void testSmsApi(){
        smsService.sendSms("18066550996","11111");
    }

    /**
     * 阿里云短信验证码服务
     */
    @Test
    public void testSms() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou"
                , "LTAI4G9GNwxYQ5mgDQWpDLiv", "8nNOjdtsiPH0fI5Kkr3XBooPr3WUN0");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", "18066550996");
        request.putQueryParameter("SignName", "李哥说Java");
        request.putQueryParameter("TemplateCode", "SMS_195861786");

        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 88888);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 阿里云对象云存储OSS服务
     */
    @Test
    public void testOss() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\20127\\Desktop\\zhou.png");
        ossClient.putObject("mall-ljl", "zhouyang.png", fileInputStream);
        ossClient.shutdown();
        System.out.println("上传成功...");
    }

}
