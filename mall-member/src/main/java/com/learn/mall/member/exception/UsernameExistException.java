package com.learn.mall.member.exception;

/**
 * Description:
 * date: 2021/5/8 13:47
 * Package: com.learn.mall.member.exception
 *
 * @author 李佳乐
 * @version 1.0
 */
public class UsernameExistException extends RuntimeException{

    public UsernameExistException() {
        super("用户名已存在");
    }

}
