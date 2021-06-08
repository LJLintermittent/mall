package com.learn.mall.product.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Description:
 * date: 2021/4/25 13:42
 * Package: com.learn.mall.product.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-ware")
public interface WareFeignService {

    /**
     * 1.R对象设计的时候可以加上泛型
     * 2.直接返回我们想要的结果
     * 3.自己封装解析的结果
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);

}
