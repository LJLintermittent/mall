package com.learn.mall.thirdparty.controller;

import com.learn.common.utils.R;
import com.learn.mall.thirdparty.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * date: 2021/5/7 19:02
 * Package: com.learn.mall.thirdparty.controller
 *
 * @author 李佳乐
 * @version 1.0
 */
@Api(tags = "短信服务模块")
@RestController
@RequestMapping("/thirdParty")
@SuppressWarnings("all")
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 发送短信验证码
     * 提供给别的服务进行调用
     */
    @ApiOperation(value = "发送短信验证码")
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        smsService.sendSms(phone, code);
        return R.ok();
    }
}
