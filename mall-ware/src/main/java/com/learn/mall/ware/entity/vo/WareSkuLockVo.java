package com.learn.mall.ware.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * date: 2021/5/17 18:56
 * Package: com.learn.mall.order.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class WareSkuLockVo {

    private String orderSn;//订单号

    private List<OrderItemVo> locks;//需要锁住的所有库存信息

}
