package com.learn.mall.auth.feign;

import com.learn.common.utils.R;
import com.learn.mall.auth.vo.SocialUser;
import com.learn.mall.auth.vo.UserLoginVo;
import com.learn.mall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description:
 * date: 2021/5/8 14:49
 * Package: com.learn.mall.auth.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-member")
public interface MemberFeignService {

    /**
     * 会员注册
     */
    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterVo vo);

    /**
     * 会员登录
     */
    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    /**
     * 社交登录
     */
    @PostMapping("/member/member/oauth2/login")
    R oauth2LoginAndRegister(@RequestBody SocialUser socialUser) throws Exception;

}
