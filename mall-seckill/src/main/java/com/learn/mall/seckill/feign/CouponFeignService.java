package com.learn.mall.seckill.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Description:
 * date: 2021/5/23 20:49
 * Package: com.learn.mall.seckill.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/getLatest3DaysSession")
    R getLatest3DaysSession();

}
