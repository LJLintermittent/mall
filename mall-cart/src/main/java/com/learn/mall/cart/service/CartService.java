package com.learn.mall.cart.service;

import com.learn.mall.cart.vo.Cart;
import com.learn.mall.cart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 * date: 2021/5/10 20:43
 * Package: com.learn.mall.cart.service
 *
 * @author 李佳乐
 * @version 1.0
 */
@SuppressWarnings("all")
public interface CartService {

    /**
     * 加入购物车
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车中某个购物项
     */
    CartItem getCartItem(Long skuId);

    /**
     * 获取整个购物车
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空指定的购物车（临时或在线）
     */
    void clearCart(String cartKey);

    /**
     * 根据购物车中购物项的选中状态来更改界面显示的总价
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 改变购物项的数量
     */
    void changeItemCount(Long skuId, Integer num);

    /**
     * 删除指定的购物项
     */
    void deleteItem(Long skuId);

    /**
     * 获取当前用户购物车中被选中的所有购物项，用于给订单服务返回需要在订单确认页面展示的数据
     */
    List<CartItem> getCurrentUserCartItems();

}
