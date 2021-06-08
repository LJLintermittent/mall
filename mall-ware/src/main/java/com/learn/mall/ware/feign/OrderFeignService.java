package com.learn.mall.ware.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description:
 * date: 2021/5/19 14:19
 * Package: com.learn.mall.ware.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-order")
public interface OrderFeignService {

    @GetMapping("/order/order/status/{OrderSn}")
    R getOrderStatusByOrderSn(@PathVariable("OrderSn") String OrderSn);

}
