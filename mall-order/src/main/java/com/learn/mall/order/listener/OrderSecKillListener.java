package com.learn.mall.order.listener;

import com.learn.common.to.mq.SeckillOrderTo;
import com.learn.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Description:
 * date: 2021/5/25 0:53
 * Package: com.learn.mall.order.listener
 *
 * @author 李佳乐
 * @version 1.0
 */
@Slf4j
@Component
@SuppressWarnings("all")
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSecKillListener {

    @Autowired
    private OrderService orderService;

    /**
     * 监听来自秒杀服务发来的秒杀成功的消息
     * 从而去创建秒杀订单相关信息
     */
    @RabbitHandler
    public void listener(SeckillOrderTo seckillOrderTo, Channel channel, Message message) throws IOException {
        log.info("准备创建秒杀订单的详细信息");
        try {
            orderService.createSecKillOrder(seckillOrderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
