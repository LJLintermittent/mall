package com.learn.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 封装订单提交的数据
 */
@Data
public class OrderSubmitVo {

    // 用户相关信息 直接去session中取出当前登录的用户
    // 无需提交需要购买的商品，直接去购物车再次获取一遍已选中的商品

    private Long addrId;//收获地址ID

    private Integer payType;//支付方式

    private String orderToken;//防重令牌

    private BigDecimal payPrice;//应付价格 验价

    private String note;//订单备注
}
