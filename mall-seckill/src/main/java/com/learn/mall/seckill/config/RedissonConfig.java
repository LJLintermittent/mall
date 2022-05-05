package com.learn.mall.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * date: 2021/4/27 19:17
 * Package: com.learn.mall.product.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.190.141:6379").setPassword("root");
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
