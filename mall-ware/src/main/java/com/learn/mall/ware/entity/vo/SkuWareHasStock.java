package com.learn.mall.ware.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * date: 2021/5/17 19:31
 * Package: com.learn.mall.ware.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 保存每个商品都在哪些仓库拥有库存
 */
@Data
public class SkuWareHasStock {

    private Long skuId;

    private Integer num;//需要锁定几件

    private List<Long> wareId;
}
