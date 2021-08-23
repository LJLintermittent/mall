package com.learn.mall.ware.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 * date: 2021/4/22 16:23
 * Package: com.learn.mall.ware.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);


}
