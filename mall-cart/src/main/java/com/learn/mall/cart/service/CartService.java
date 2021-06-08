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
public interface CartService {

    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem getCartItem(Long skuId);

    Cart getCart() throws ExecutionException, InterruptedException;

    void clearCart(String cartKey);

    void checkItem(Long skuId, Integer check);

    void changeItemCount(Long skuId, Integer num);

    void deleteItem(Long skuId);

    List<CartItem> getCurrentUserCartItems();

}
