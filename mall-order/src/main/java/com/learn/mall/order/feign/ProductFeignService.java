package com.learn.mall.order.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description:
 * date: 2021/5/17 15:32
 * Package: com.learn.mall.order.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    /**
     * 通过skuID来获取spu的详细信息
     */
    @GetMapping("/product/spuinfo/skuId/{id}")
    R getSpuInfoBySkuId(@PathVariable("id") Long skuId);

}
