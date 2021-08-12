package com.learn.mall.product.feign;

import com.learn.common.to.SkuReductionTo;
import com.learn.common.to.SpuBoundsTo;
import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description:
 * date: 2021/4/20 21:40
 * Package: com.learn.mall.product.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-coupon")
@SuppressWarnings("all")
public interface CouponFeignService {

    /**
     * CouponFeignService.saveSpuBounds(SpuBoundsTo)
     * 逻辑： @RequestBody此注解将SpuBoundsTo这个对象转为json数据
     * 找到 mall-coupon微服务下的 /coupon/spubounds/save 请求方法 发送请求
     * 将上一步转的json放在请求体的位置，发送请求
     * 对方服务器收到请求，请求体里有json
     * .(@RequestBody SpuBoundsEntity spuBounds) 将请求体里的json转换为SpuBoundsEntity对象
     * 只要 json数据模型是兼容的，双方服务无需使用同一个 TO对象
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);

}
