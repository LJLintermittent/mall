package com.learn.mall.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * date: 2021/5/23 21:31
 * Package: com.learn.mall.seckill.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class SecKillSkuVo {

    private Long id;
    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private Integer seckillCount;
    /**
     * 每人限购数量
     */
    private Integer seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

}
