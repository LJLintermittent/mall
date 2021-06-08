package com.learn.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * date: 2021/5/25 0:32
 * Package: com.learn.common.to.mq
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class SeckillOrderTo {

    /**
     * 秒杀订单号 使用IdWorker生成
     */
    private String orderSn;
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
    private Integer num;
    /**
     * 会员id
     */
    private Long memberId;

}
