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

    @PostMapping("/member/member/register")
    public R register(@RequestBody UserRegisterVo vo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth2/login")
    public R oauth2LoginAndRegister(@RequestBody SocialUser socialUser) throws Exception;

}
