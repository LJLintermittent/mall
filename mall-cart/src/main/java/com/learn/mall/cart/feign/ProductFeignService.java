package com.learn.mall.cart.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * date: 2021/5/11 0:32
 * Package: com.learn.mall.cart.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    /**
     * 根据skuID来获取sku详细信息
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);

    /**
     * 根据skuID来获取sku的销售属性信息
     */
    @GetMapping("/product/skusaleattrvalue/stringlist/{skuId}")
    List<String> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);

    /**
     * 根据skuID来获取商品价格
     */
    @GetMapping("/product/skuinfo/{skuId}/price")
    R getPrice(@PathVariable("skuId") Long skuId);

}
