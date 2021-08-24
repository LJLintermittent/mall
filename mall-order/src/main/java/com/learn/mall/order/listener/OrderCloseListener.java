package com.learn.mall.order.listener;

import com.learn.mall.order.entity.OrderEntity;
import com.learn.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Description:
 * date: 2021/5/19 16:06
 * Package: com.learn.mall.order.listener
 *
 * @author 李佳乐
 * @version 1.0
 */
@Component
@SuppressWarnings("all")
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {

    @Autowired
    private OrderService orderService;

    /**
     * 监听订单服务的消息，如果订单超过系统设置的时间，比如用户在半小时内没有付款，那么订单为失效状态
     * 这个订单就过期了，会将订单工作单状态设置为过期状态
     */
    @RabbitHandler
    public void listener(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单，准备关闭订单：" + orderEntity.getOrderSn());
        try {
            orderService.closeOrder(orderEntity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
