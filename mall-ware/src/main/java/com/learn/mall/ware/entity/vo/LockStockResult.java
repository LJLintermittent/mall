package com.learn.mall.ware.entity.vo;

import lombok.Data;

/**
 * Description:
 * date: 2021/5/17 19:05
 * Package: com.learn.mall.ware.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class LockStockResult {

    private Long skuId;//锁定的商品ID

    private Integer num;//锁定的库存数量

    private Boolean locked;//锁定成功与否

}
