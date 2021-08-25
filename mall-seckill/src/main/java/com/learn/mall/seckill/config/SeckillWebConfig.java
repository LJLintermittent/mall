package com.learn.mall.seckill.config;

import com.learn.mall.seckill.interceptor.LoginUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description:
 * date: 2021/5/24 22:40
 * Package: com.learn.mall.seckill.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
@SuppressWarnings("all")
public class SeckillWebConfig implements WebMvcConfigurer {

    @Autowired
    LoginUserInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }

}
