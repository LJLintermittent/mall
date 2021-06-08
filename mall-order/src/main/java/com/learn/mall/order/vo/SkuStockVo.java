package com.learn.mall.order.vo;

import lombok.Data;

/**
 * Description:
 * date: 2021/5/13 20:16
 * Package: com.learn.mall.order.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class SkuStockVo {
    private Long skuId;
    private Boolean hasStock;
}
