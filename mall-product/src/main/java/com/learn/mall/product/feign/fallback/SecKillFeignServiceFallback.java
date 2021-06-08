package com.learn.mall.product.feign.fallback;

import com.learn.common.exception.StatusCodeEnum;
import com.learn.common.utils.R;
import com.learn.mall.product.feign.SecKillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description:
 * date: 2021/5/25 15:22
 * Package: com.learn.mall.product.feign.fallback
 *
 * @author 李佳乐
 * @version 1.0
 */
@Slf4j
@Component
public class SecKillFeignServiceFallback implements SecKillFeignService {

    @Override
    public R getSecKillSkuInfo(Long skuId) {
        log.info("getSecKillSkuInfo()--熔断方法调用");
        return R.error(StatusCodeEnum.TOO_MANY_REQUEST.getCode(), StatusCodeEnum.TOO_MANY_REQUEST.getMsg());
    }
}
