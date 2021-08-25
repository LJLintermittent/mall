package com.learn.mall.seckill.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 * date: 2021/5/23 22:41
 * Package: com.learn.mall.seckill.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    /**
     * 根据秒杀商品ID查询秒杀商品详情
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R GetSkuInfo(@PathVariable("skuId") Long skuId);

}
