package com.learn.mall.product.entity.vo.front;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * date: 2021/5/24 20:49
 * Package: com.learn.mall.product.entity.vo.front
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class SecKillInfoVo {

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
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;
    /**
     * 当前商品秒杀的开始时间
     */
    private Long startTime;

    /**
     * 当前商品秒杀的结束时间
     */
    private Long endTime;
    /**
     * 商品秒杀的随机码
     * 请求uri中必须带有该商品的秒杀随机码
     */
    private String randomCode;

}
