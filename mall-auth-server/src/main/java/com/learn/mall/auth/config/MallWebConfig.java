package com.learn.mall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description:
 * date: 2021/5/7 16:33
 * Package: com.learn.mall.auth.config
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 页面跳转请求映射
 */
@Configuration
public class MallWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/auth/reg.html").setViewName("reg");
    }

}
