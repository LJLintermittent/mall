package com.learn.mall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.learn.common.constant.AuthConstant;
import com.learn.common.exception.StatusCodeEnum;
import com.learn.common.utils.R;
import com.learn.common.vo.MemberRespVo;
import com.learn.mall.auth.feign.MemberFeignService;
import com.learn.mall.auth.feign.ThirdPartyFeignService;
import com.learn.mall.auth.utils.RandomUtil;
import com.learn.mall.auth.vo.UserLoginVo;
import com.learn.mall.auth.vo.UserRegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Description:
 * date: 2021/5/7 15:47
 * Package: com.learn.mall.auth.controller
 *
 * @author 李佳乐
 * @version 1.0
 */
@SuppressWarnings("all")
@Api(tags = "认证模块")
@Controller
@RequestMapping("/auth")
public class IndexController {

    @Autowired
    private ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MemberFeignService memberFeignService;

    /**
     * 发送一个请求直接跳转到一个页面（不做多余的操作）
     * springMVC viewController 将请求和页面映射过来
     * 以后不需要在controller里面写跳转逻辑的空方法
     *
     * @GetMapping("/login.html") public String login() {
     * return "login";
     * }
     * @GetMapping("/reg.html") public String reg() {
     * return "reg";
     * }
     */

    /**
     * 验证码再次校验 （存Redis）
     * key： sms:code:18066550996   value:  998176
     */
    @ApiOperation(value = "发送短信验证码")
    @ResponseBody
    @GetMapping("/sendSms")
    public R sendCode(@RequestParam("phone") String phone) {
        if (StringUtils.isEmpty(phone)) {
            return R.error();
        }
        String redisCode = stringRedisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            //阻止在一分钟以内用户恶意通过postman等工具刷短信验证码 60*1000
            if (System.currentTimeMillis() - l < 60000) {
                return R.error(StatusCodeEnum.SMS_CODE_EXCEPTION.getCode(),
                        StatusCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        //随机生成一个六位数的验证码
        String code = RandomUtil.getSixBitRandom();
        stringRedisTemplate.opsForValue().set(AuthConstant.SMS_CODE_CACHE_PREFIX + phone,
                code + "_" + System.currentTimeMillis()
                , 5, TimeUnit.MINUTES);
        thirdPartyFeignService.sendCode(phone, code);
        return R.ok();
    }

    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo vo, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream().collect(
                    Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/auth/reg.html";
        }
        String code = vo.getCode();
        String redisCode = stringRedisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (!StringUtils.isEmpty(redisCode)) {
            //redis中的code不为空（没过期）且页面输入的code与redis中的code相等，代表校验成功
            if (code.equals(redisCode.split("_")[0])) {
                //校验成功以后，需要删除这个code
                stringRedisTemplate.delete(AuthConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //验证通过 调用远程服务(member服务) 加入注册用户
                R r = memberFeignService.register(vo);
                if (r.getCode() == 0) {
                    //成功
                    return "redirect:http://auth.mall.com/auth/login.html";
                } else {
                    //失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getData("msg", new TypeReference<String>() {
                    }));
                    attributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.mall.com/auth/reg.html";
                }
            } else {
                //验证不通过
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误，请您确认收到的验证码与输入的是否一致！");
                attributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.mall.com/auth/reg.html";
            }
        } else {
            //redis中的code为空，代表这个code已经过期了
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "您输入的验证码不存在或者已经过期！");
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/auth/reg.html";
        }
    }

    /**
     * 用户登录
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes attributes,
                        HttpSession session) {
        R result = memberFeignService.login(vo);
        if (result.getCode() == 0) {
            MemberRespVo data = result.getData("data", new TypeReference<MemberRespVo>() {
            });
            //成功后跳回首页
            session.setAttribute(AuthConstant.LOGIN_USER, data);
            return "redirect:http://mall.com";
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", result.getData("msg", new TypeReference<String>() {
            }));
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/auth/login.html";
        }
    }

    /**
     * 发送登录请求，加入一个逻辑判断，如果用户已经登录了
     * 那就跳转到首页，如果没登录才可以通过登录请求跳转到登录页
     */
    @ApiOperation(value = "登录跳转接口(发送登录请求，加入一个逻辑判断，如果用户已经登录了," +
            "那就跳转到首页，如果没登录才可以通过登录请求跳转到登录页)")
    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {
        Object data = session.getAttribute(AuthConstant.LOGIN_USER);
        if (data == null) {
            //没登录
            return "login";
        } else {
            //已登录，无法再去登录页面，直接跳转到首页，除非注销用户
            return "redirect:http://mall.com";
        }
    }

    /**
     * 用户注销 测试接口 返回json数据
     */
    @ApiOperation(value = "用户注销测试接口")
    @ResponseBody
    @GetMapping("/logoutTest")
    public R logoutTest() {
        Set<String> keys = stringRedisTemplate.keys("*");
        System.out.println("----------------Redis中的所有key------------------");
        System.out.println(keys);
        for (String key : keys) {
            if (key.contains("spring")) {
                System.out.println("------------------将要被删除的key------------------");
                System.out.println(key);
                stringRedisTemplate.delete(key);
            }
        }
        System.out.println("------------------此时redis中剩下的key-------------------");
        Set<String> nowKeys = stringRedisTemplate.keys("*");
        System.out.println(nowKeys);
        return R.ok().put("data", "注销成功");
    }

    /**
     * 用户注销正式接口，返回页面
     */
    @ApiOperation(value = "用户注销正式接口")
    @GetMapping("/logout")
    public String logout() {
        Set<String> keys = stringRedisTemplate.keys("*");
        for (String key : keys) {
            if (key.contains("spring")) {
                stringRedisTemplate.delete(key);
            }
        }
        return "redirect:http://mall.com";
    }

}
