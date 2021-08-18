package com.learn.mall.cart.controller;

import com.learn.mall.cart.service.CartService;
import com.learn.mall.cart.vo.Cart;
import com.learn.mall.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 * date: 2021/5/10 20:49
 * Package: com.learn.mall.cart.controller
 *
 * @author 李佳乐
 * @version 1.0
 */
@Controller
@SuppressWarnings("all")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 获取当前用户的购物车中所有被选中的购物项，用于订单服务用来生成订单确认数据
     */
    @GetMapping("/currentUserCartItems")
    @ResponseBody
    public List<CartItem> getCurrentUserCartItems() {
        return cartService.getCurrentUserCartItems();
    }

    /**
     * 删除某一个购物项
     */
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId) {
        cartService.deleteItem(skuId);
        return "redirect:http://cart.mall.com/cart.html";
    }

    /**
     * 改变商品（购物项）的数量
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.changeItemCount(skuId, num);
        return "redirect:http://cart.mall.com/cart.html";
    }

    /**
     * 改变商品的选中状态
     */
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId, @RequestParam("check") Integer check) {
        cartService.checkItem(skuId, check);
        return "redirect:http://cart.mall.com/cart.html";
    }

    /**
     * 跳转到购物车列表页
     * 此需求需要判断用户有没有登录
     * <p>
     * 浏览器有一个cookie：user-key，标识用户身份，一个月后过期
     * 如果第一次使用jd购物车功能，也就是鼠标放到商城首页的购物车图标上，就会发送一个请求
     * 这个请求的请求头里面的cookie一定会带上user-kry这个cookie
     * 这个user-key就是一个临时的用户身份
     * <p>
     * 如果用户登录过了 那么就会就会有session
     * 如果没有登录 那就按照cookie里面带来的user-key来做
     * 第一次：没有临时用户，我们需要帮忙创建一个临时用户
     */
    @GetMapping("cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     * 并跳转到添加商品成功页面
     * RedirectAttributes.addFlashAttribute()将数据放在session里面可以在页面取出来，但是只能取一次
     * RedirectAttributes.addAttribute() 将数据放在url后面
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num
            , RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.mall.com/addToCartSuccess.html";
    }

    /**
     * 跳转到成功页面，如果我们在成功页面刷新，相当于我们总是在这个页面重新获取数据
     * 而不是刷新一次就向购物车添加一次
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model) {
        //重定向到成功页面，重新查出数据即可
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);
        return "success";
    }
}
