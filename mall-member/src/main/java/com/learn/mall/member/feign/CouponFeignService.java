package com.learn.mall.member.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 * date: 2021/4/10 16:32
 * Package: com.learn.mall.member.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupon();

}
