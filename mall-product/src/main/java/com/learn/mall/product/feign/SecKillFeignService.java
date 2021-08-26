package com.learn.mall.product.feign;

import com.learn.common.utils.R;
import com.learn.mall.product.feign.fallback.SecKillFeignServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description:
 * date: 2021/5/24 20:41
 * Package: com.learn.mall.product.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@SuppressWarnings("all")
@FeignClient(value = "mall-seckill",fallback = SecKillFeignServiceFallback.class)
public interface SecKillFeignService {

    /**
     * 获取秒杀的商品信息
     */
    @GetMapping("/sku/secKill/{skuId}")
    R getSecKillSkuInfo(@PathVariable("skuId") Long skuId);

}
