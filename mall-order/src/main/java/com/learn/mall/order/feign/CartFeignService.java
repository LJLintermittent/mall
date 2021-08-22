package com.learn.mall.order.feign;

import com.learn.mall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Description:
 * date: 2021/5/12 21:43
 * Package: com.learn.mall.order.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-cart")
public interface CartFeignService {

    /**
     * 获取当前用户的购物车中的购物项
     */
    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();

}
