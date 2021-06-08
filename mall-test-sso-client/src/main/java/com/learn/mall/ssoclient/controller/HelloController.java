package com.learn.mall.ssoclient.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Description:
 * date: 2021/5/10 13:32
 * Package: com.learn.mall.ssoclient.controller
 *
 * @author 李佳乐
 * @version 1.0
 */
@Controller
@RequestMapping("ssoclient")
public class HelloController {

    @Value("${sso.server.url}")
    String ssoServerUrl;

    /**
     * 无需登录就可以访问
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * 登录才可以访问
     */
    @GetMapping("/employees")
    public String list(Model model, HttpSession session, @RequestParam(value = "token", required = false)
            String token) {
        //跳回来的请求里面如果有token，那么说明已经登录成功
        if (!StringUtils.isEmpty(token)) {
            //去ssoserver获取当前token对应的真正的用户信息
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> entity = restTemplate
                    .getForEntity("http://ssoserver.com:9988/userInfo?token=" + token, String.class);
            String data = entity.getBody();
            session.setAttribute("loginUser", data);
        }
        Object attribute = session.getAttribute("loginUser");
        if (attribute != null) {
            ArrayList<String> list = Lists.newArrayList();
            list.add("李佳乐");
            list.add("xxx");
            model.addAttribute("epms", list);
            return "list";
        } else {
            return "redirect:" + ssoServerUrl + "?redirect_url=http://client1.com:8801/ssoclient/employees";
        }
    }
}
