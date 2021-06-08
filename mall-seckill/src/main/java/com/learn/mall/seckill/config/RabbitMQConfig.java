package com.learn.mall.seckill.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * date: 2021/5/25 0:27
 * Package: com.learn.mall.seckill.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 给容器中放一个消息转换器
     * 使用json序列化机制，进行消息转换
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
