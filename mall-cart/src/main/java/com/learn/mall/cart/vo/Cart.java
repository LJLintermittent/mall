package com.learn.mall.cart.vo;

/**
 * Description:
 * date: 2021/5/10 18:40
 * Package: com.learn.mall.cart.vo
 *
 * @author 李佳乐
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.List;

/**
 * 整个购物车
 */
public class Cart {

    List<CartItem> items;//购物车中所有的购物项
    private Integer countNum;//商品数量
    private Integer countType;//商品种类
    private BigDecimal totalAmount;//商品总价
    private BigDecimal reduce = new BigDecimal("0.00");//减免价格

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    /**
     * 获取这个购物车中商品的总数量
     */
    public Integer getCountNum() {
        int count = 0;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    /**
     * 获取这个购物车中有几种商品
     */
    public Integer getCountType() {
        return items.size();
    }

    /**
     * 获取这个购物车中所有选中商品的总价
     */
    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        //1.计算购物总价
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                //必须是选中的商品
                if (item.getCheck()) {
                    BigDecimal totalPrice = item.getTotalPrice();
                    amount = amount.add(totalPrice);
                }
            }
        }
        //2.减去优惠总价
        BigDecimal finalAmount = amount.subtract(getReduce());
        return finalAmount;
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
