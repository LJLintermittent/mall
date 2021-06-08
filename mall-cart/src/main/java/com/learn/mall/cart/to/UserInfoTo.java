package com.learn.mall.cart.to;

import lombok.Data;

/**
 * Description:
 * date: 2021/5/10 21:20
 * Package: com.learn.mall.cart.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class UserInfoTo {
    //如果登录了，会有一个用户id
    private Long userId;
    //如果没登录，会有一个用户的临时键
    private String userKey;

    //如果cookie中有临时用户（user-key），那就为true，反之为false
    private boolean tempUser = false;
}
