package com.learn.mall.order.vo;

/**
 * Description:
 * date: 2021/5/12 20:31
 * Package: com.learn.mall.order.vo
 *
 * @author 李佳乐
 * @version 1.0
 */

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单确认页面 confirm.html需要展示的信息
 */
public class OrderConfirmVo {

    //会员的收货地址列表
    @Setter
    @Getter
    List<MemberAddressVo> address;

    //所有选中的购物项
    @Setter
    @Getter
    List<OrderItemVo> items;

    //会员的积分信息
    @Setter
    @Getter
    Integer integration;

    //封装每一个sku是否有库存
    @Setter
    @Getter
    Map<Long,Boolean> stock;

    //订单防重令牌
    @Setter
    @Getter
    String orderToken;
    
    public Integer getCount(){
        return items.size();
    }

    //订单总额
    BigDecimal total;

    public BigDecimal getTotal() {
        BigDecimal sum = new BigDecimal("0");
        if (items != null) {
            for (OrderItemVo item : items) {
                BigDecimal total = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                sum = sum.add(total);
            }
        }
        return sum;
    }

    //应付价格
    BigDecimal payPrice;

    public BigDecimal getPayPrice() {
        return getTotal();
    }
}
