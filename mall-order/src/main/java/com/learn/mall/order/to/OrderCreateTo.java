package com.learn.mall.order.to;

import com.learn.mall.order.entity.OrderEntity;
import com.learn.mall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * date: 2021/5/17 14:00
 * Package: com.learn.mall.order.to
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class OrderCreateTo {

    private OrderEntity order;

    private List<OrderItemEntity> orderItems;

    private BigDecimal payPrice;//订单计算的应付价格

    private BigDecimal fare;//运费

}
