package com.learn.mall.ssoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Description:
 * date: 2021/5/10 13:58
 * Package: com.learn.mall.ssoserver.controller
 *
 * @author 李佳乐
 * @version 1.0
 */
@Controller
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 客户端从认证中心 ssoserver中根据token获取用户信息
     */
    @ResponseBody
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("token") String token) {
        String s = stringRedisTemplate.opsForValue().get(token);
        return s;
    }

    /**
     * 登录页面的映射
     */
    @GetMapping("/login.html")
    public String loginPage(@RequestParam("redirect_url") String url
            , Model model
            , @CookieValue(value = "sso_token", required = false) String sso_token) {
        //说明之前已经有人在其他项目登录过了，在浏览器留下了痕迹（cookie）
        if (!StringUtils.isEmpty(sso_token)) {
            return "redirect:" + url + "?token=" + sso_token;
        }
        model.addAttribute("url", url);
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(
            @RequestParam("username") String username
            , @RequestParam("password") String password
            , @RequestParam("url") String url
            , HttpServletResponse response) {
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            //登录成功以后跳转到之前的页面
            //把登录成功的用户存起来
            String uuid = UUID.randomUUID().toString().replace("-", "");
            stringRedisTemplate.opsForValue().set(uuid, username);
            Cookie cookie = new Cookie("sso_token", uuid);
            response.addCookie(cookie);
            return "redirect:" + url + "?token=" + uuid;
        }
        //登录失败，展示登录页
        return "login";
    }
}
