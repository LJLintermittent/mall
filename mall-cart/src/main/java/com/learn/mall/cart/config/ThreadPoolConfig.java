package com.learn.mall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * date: 2021/5/7 13:55
 * Package: com.learn.mall.product.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties properties) {
        return new ThreadPoolExecutor(
                properties.getCoreSize()
                , properties.getMaxSize()
                , properties.getKeepAliveTime()
                , TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(10000)
                , Executors.defaultThreadFactory()
                , new ThreadPoolExecutor.AbortPolicy());
    }

}
