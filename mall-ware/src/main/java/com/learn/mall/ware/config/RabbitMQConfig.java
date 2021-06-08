package com.learn.mall.ware.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * date: 2021/5/18 21:06
 * Package: com.learn.mall.ware.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
@SuppressWarnings("all")
public class RabbitMQConfig {

    /**
     * 第一次连上消息队列，来监听消息的时候，没有的组件会去创建
     * 比如 交换机和队列
     */
//    @RabbitListener(queues = "stock.release.stock.queue")
//    public void handle() {
//    }

    /**
     * 创建业务交换机 队列 和他们的绑定关系
     */
    @Bean
    public Exchange stockEventExchange() {
        TopicExchange topicExchange = new TopicExchange("stock-event-exchange", true, false);
        return topicExchange;
    }

    @Bean
    public Queue stockReleaseStockQueue() {
        return new Queue("stock.release.stock.queue", true, false, false);
    }

    @Bean
    public Queue stockDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "stock-event-exchange");
        arguments.put("x-dead-letter-routing-key", "stock.release");
        arguments.put("x-message-ttl", 120000);
        return new Queue("stock.delay.queue", true, false, false, arguments);
    }

    @Bean
    public Binding stockReleaseBinding() {
        Binding binding = new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.release.#", null);
        return binding;
    }

    @Bean
    public Binding stockLockedBinding() {
        Binding binding = new Binding("stock.delay.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.locked", null);
        return binding;
    }

    /**
     * 给容器中放一个消息转换器
     * 使用json序列化机制，进行消息转换
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
