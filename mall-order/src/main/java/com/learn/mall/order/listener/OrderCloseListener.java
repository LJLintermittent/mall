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

    /*
      可靠消息：
      1.消息丢失：
      一。由于网络原因，消息没有抵达服务器
      这种问题的解决方式可以使用消息日志记录，在数据库中建立一张mq_message表，保存消息的详细的信息，以及错误异常
      定期扫描数据库，查询发送失败的消息进行重新发送
      二。消息抵达了broker，但是borker在进行消息持久化保存的时候宕机了
      这种问题可以通过生产者的确认回调机制，当消息抵达broker会有一个确认回调，抵达队列后还有一个确认回调
      三。自动ACK模式下，消费者收到消息，但是没来得及消费然后宕机了
      这种情况下开启手动ACK模式，只有消费者把消息消费成功了，才告诉服务器把这个消息删掉
      2.消息重复：
      一。消息消费成功后，业务代码执行完毕，事务已经提交，在正准备要手动ack的时候，宕机，导致没有ack成功
      注意消费扣减两次库存这种重复消费场景
      broker的消息又由unack状态变为ready状态，当服务器重启后，又进行重复消费
      解决：将业务设计成幂等的，或者使用防重表，每个消息都有一个唯一id，放到表里，当处理过后，就不进行二次处理
      rabbitmq还提供了一个属性redeliverd，每次收到消息后可以知道这个消息是不是被重新派发过来的
      3.消息挤压：
      消费者服务消费能力不足，或者宕机了，造成消息的挤压
      可以通过在生产者端限制流量，或者加大消费者的服务器数量，或者将所有的消息先全部放入数据库，表示消息消费完成
      然后再慢慢离线处理消息业务
     */

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
