package com.learn.mall.product.feign;

import com.learn.common.to.es.SkuEsModel;
import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Description:
 * date: 2021/4/25 15:25
 * Package: com.learn.mall.product.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);

}
