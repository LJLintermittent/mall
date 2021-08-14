package com.learn.mall.auth.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description:
 * date: 2021/5/7 19:16
 * Package: com.learn.mall.auth.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-third-party")
public interface ThirdPartyFeignService {

    @GetMapping("/thirdParty/sendCode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
