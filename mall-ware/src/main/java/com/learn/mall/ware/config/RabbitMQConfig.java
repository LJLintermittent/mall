package com.learn.mall.ware.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
     * 第一次连上消息队列，来监听消息的时候，没有的组件会去创建,比如交换机和队列
     * @RabbitListener(queues = "stock.release.stock.queue")
     * public void handle(Message message){}
     */

    /**
     * 给容器中放一个消息转换器
     * 使用json序列化机制，进行消息转换
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建业务交换机 队列 和他们的绑定关系
     */
    @Bean
    public Exchange stockEventExchange() {
        TopicExchange topicExchange = new TopicExchange("stock-event-exchange", true, false);
        return topicExchange;
    }

    /**
     * 真正被消费者监听的释放库存的队列，前面有一个死信队列给这个队里发送过期的消息
     */
    @Bean
    public Queue stockReleaseStockQueue() {
        return new Queue("stock.release.stock.queue", true, false, false);
    }

    /**
     * 库存延迟队列
     * 库存锁了以后需要等待一段时间，比如消费者在规定时间内没有支付订单，那么库存需要在这个时间之后进行解锁
     * 以及订单服务发生异常后，库存是远程调用的服务，需要保证事务，那么采用最终一致性进行处理
     */
    @Bean
    public Queue stockDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "stock-event-exchange");
        arguments.put("x-dead-letter-routing-key", "stock.release");
        arguments.put("x-message-ttl", 120000);
        return new Queue("stock.delay.queue", true, false, false, arguments);
    }

    /**
     * 释放库存的消息队列的路由键
     */
    @Bean
    public Binding stockReleaseBinding() {
        Binding binding = new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.release.#", null);
        return binding;
    }

    /**
     * 指向锁定成功以后放入的延迟队列的路由键
     */
    @Bean
    public Binding stockLockedBinding() {
        Binding binding = new Binding("stock.delay.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.locked", null);
        return binding;
    }



}
