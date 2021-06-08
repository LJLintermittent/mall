package com.learn.mall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.learn.common.constant.CartConstant;
import com.learn.common.utils.R;
import com.learn.mall.cart.feign.ProductFeignService;
import com.learn.mall.cart.interceptor.CartInterceptor;
import com.learn.mall.cart.service.CartService;
import com.learn.mall.cart.to.UserInfoTo;
import com.learn.mall.cart.vo.Cart;
import com.learn.mall.cart.vo.CartItem;
import com.learn.mall.cart.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * Description:
 * date: 2021/5/10 20:43
 * Package: com.learn.mall.cart.service.impl
 *
 * @author 李佳乐
 * @version 1.0
 */
@SuppressWarnings("all")
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 加入购物车
     */
    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        //根据用户是否登录来判断是加入临时购物车还是在线购物车
        BoundHashOperations<String, Object, Object> operations = getCartOps();
        String result = (String) operations.get(skuId.toString());
        if (StringUtils.isEmpty(result)) {
            CartItem cartItem = new CartItem();
            //购物车无此商品
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                //远程查询我们要添加的商品信息
                R r = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = r.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                //把查询到的这个商品信息(商品)添加到购物车
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setPrice(data.getPrice());
                cartItem.setTotalPrice(cartItem.getTotalPrice());
                cartItem.setSkuId(skuId);
            }, threadPoolExecutor);
            //远程查询sku的组合信息
            CompletableFuture<Void> getSkuSaleAttrValuesTask = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, threadPoolExecutor);
            //等待所有异步任务完成，在向redis中保存数据
            CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrValuesTask).get();
            String s = JSON.toJSONString(cartItem);
            operations.put(skuId.toString(), s);
            return cartItem;
        } else {
            //购物车已经添加过了这个商品，只需要修改商品数量
            CartItem cartItem = JSON.parseObject(result, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);
            operations.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    /**
     * 获取购物车中某个购物项
     */
    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> operations = getCartOps();
        String s = (String) operations.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(s, CartItem.class);
        return cartItem;
    }

    /**
     * 获取整个购物车
     */
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() != null) {
            //已登录
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            //如果临时购物车的数据还没有和在线购物车进行合并，那么先合并购物车
            //临时购物车中的数据
            String tempCartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if (tempCartItems != null) {
                //临时购物车有数据，需要进行合并
                for (CartItem cartItem : tempCartItems) {
                    addToCart(cartItem.getSkuId(), cartItem.getCount());
                }
                //合并完成以后，清除临时购物车中的数据
                clearCart(tempCartKey);
            }
            //获取登录后的购物车的数据（包含合并过来的临时购物车中的数据）
            List<CartItem> LoginCartItems = getCartItems(cartKey);
            cart.setItems(LoginCartItems);
        } else {
            //未登录
            String tempCartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> unLoginCartItems = getCartItems(tempCartKey);
            cart.setItems(unLoginCartItems);
        }
        return cart;
    }

    /**
     * 清空指定的购物车（临时或在线）
     */
    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    /**
     * 如果选中状态变了，需要更改redis中的商品选中状态
     * 从而更新页面上的信息（总价，商品总数量）
     */
    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> operations = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check == 1);
        String s = JSON.toJSONString(cartItem);
        operations.put(skuId.toString(), s);
    }

    /**
     * 改变购物项的数量
     */
    @Override
    public void changeItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> operations = getCartOps();
        operations.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    /**
     * 删除指定的购物项
     */
    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> operations = getCartOps();
        operations.delete(skuId.toString());
    }

    /**
     * 获取当前用户购物车中被选中的所有购物项，用于给订单服务返回需要在订单确认页面展示的数据
     */
    @Override
    public List<CartItem> getCurrentUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() == null) {
            //未登录
            return null;
        } else {
            //登陆了
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            //获取所有被选中的购物项
            List<CartItem> collect = cartItems.stream().filter(item -> item.getCheck())
                    .map(item -> {
                        R r = productFeignService.getPrice(item.getSkuId());
                        //更新为最新价格
                        //BUG:无法批量购买
                        String data = (String) r.get("data");
                        item.setPrice(new BigDecimal(data));
                        return item;
                    }).collect(Collectors.toList());
            return collect;
        }
    }

    /**
     * 返回一个购物车操作（redis hash数据结构）
     * 获取到我们要操作的购物车
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            //已登录（放入在线购物车）
            //mall:cart:1
            cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
        } else {
            //未登录（放入离线购物车）
            cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }

    /**
     * 获取指定的购物车里面所有的购物项
     */
    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (values != null && values.size() > 0) {
            List<CartItem> collect = values.stream().map((value) -> {
                String str = (String) value;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }
}
