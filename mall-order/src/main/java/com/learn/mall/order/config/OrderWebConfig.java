package com.learn.mall.order.config;

import com.learn.mall.order.interceptor.LoginUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description:
 * date: 2021/5/12 17:22
 * Package: com.learn.mall.order.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
@SuppressWarnings("all")
public class OrderWebConfig implements WebMvcConfigurer {

    @Autowired
    LoginUserInterceptor interceptor;

    /**
     * 注册一个登录拦截器，拦截订单服务的所有请求
     * 在所有的订单服务请求之前，先执行拦截器中的方法，获取session中的登录信息
     * 然后放到threadlocal中，其他业务可以从threadlocal中拿到登录用户的一些基本信息
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}
