package com.learn.mall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.learn.common.constant.AuthConstant;
import com.learn.common.utils.HttpUtils;
import com.learn.common.utils.R;
import com.learn.common.vo.MemberRespVo;
import com.learn.mall.auth.feign.MemberFeignService;
import com.learn.mall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * date: 2021/5/8 19:51
 * Package: com.learn.mall.auth.controller
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 处理社交登录请求
 */
@Controller
@Slf4j
@SuppressWarnings("all")
public class OAuth2Controller {

    @Autowired
    private MemberFeignService memberFeignService;

    /**
     * 社交登录成功回调方法
     * 微博登录成功，会回调此方法
     * 拿到了code去换取access_token
     * 得到一个社交用户信息socialUser
     * 然后调用远程服务传入这个socialUser去数据库注册或者登录
     * 无论是注册还是登录都会返回一个数据库的用户信息
     */
    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> query = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "4093990418");
        map.put("client_secret", "4cd20c854c5a01f0c03de09da36a8e47");
        map.put("grand_type", "authorization_code");
        map.put("redirect_uri", "http://auth.mall.com/oauth2.0/weibo/success");
        map.put("code", code);
        //根据code换取accessToken
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post",
                headers, query, map);
        //处理
        if (response.getStatusLine().getStatusCode() == 200) {
            //获取到了access_token
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            //知道了当前是哪个社交用户
            //当前用户如果是第一次进网站，会自动注册进来，为当前用户生成一个会员信息账号，以后这个社交账号就对应指定的会员信息
            //社交用户关联一个本系统的账号信息
            //远程服务判断 这个用户是登录还是注册
            R r = memberFeignService.oauth2LoginAndRegister(socialUser);
            if (r.getCode() == 0) {
                //MemberRespVo 登录成功的用户信息
                MemberRespVo data = r.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("登录成功，用户信息： {}", data);
                //默认发动令牌 session=xxxx 作用域是当前域（解决子域session共享问题）
                //使用json的序列化方式来序列化对象数据到redis
                session.setAttribute(AuthConstant.LOGIN_USER, data);
                //登录成功后就跳回首页
                return "redirect:http://mall.com";
            } else {
                //失败就跳回登录页面
                return "redirect:http://auth.mall.com/auth/login.html";
            }
        } else {
            return "redirect:http://auth.mall.com/auth/login.html";
        }
    }

}
