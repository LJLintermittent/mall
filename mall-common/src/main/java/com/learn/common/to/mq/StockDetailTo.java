package com.learn.common.to.mq;

import lombok.Data;

/**
 * Description:
 * date: 2021/5/18 22:35
 * Package: com.learn.common.to.mq
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class StockDetailTo {

    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;
    /**
     * 仓库id
     */
    private Long wareId;
    /**
     * 1-已锁定  2-已解锁  3-扣减
     */
    private Integer lockStatus;
}
