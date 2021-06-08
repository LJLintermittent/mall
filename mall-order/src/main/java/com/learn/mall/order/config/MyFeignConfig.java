package com.learn.mall.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * date: 2021/5/12 22:34
 * Package: com.learn.mall.order.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class MyFeignConfig {

    /**
     * feign远程调用的请求拦截器
     */
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                //拿到刚进来的这个请求
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                //老请求
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    //设置新请求 同步请求头数据 主要是cookie
                    String cookie = request.getHeader("Cookie");
                    //给新请求同步了老请求的cookie
                    template.header("Cookie", cookie);
                }
            }
        };
    }
}
