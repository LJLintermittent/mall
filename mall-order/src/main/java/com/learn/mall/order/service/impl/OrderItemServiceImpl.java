package com.learn.mall.order.service.impl;

import com.learn.mall.order.entity.OrderEntity;
import com.learn.mall.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.order.dao.OrderItemDao;
import com.learn.mall.order.entity.OrderItemEntity;
import com.learn.mall.order.service.OrderItemService;

/**
 * @RabbitListener(queues = {"hello-java-queue"}) 标在类上，代表要去哪个队列去获取消息 监听队列
 * @RabbitHandler 标在方法上，重载区分不同的消息
 */
@Service("orderItemService")
@RabbitListener(queues = {"hello-java-queue"})
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * queue:可以很多人来监听，只要收到此消息，队列删除消息，而且只能有一个收到此消息
     * 订单服务启动多个，同一个消息，只能有一个客户端收到
     * 只有一个消息完全处理完，方法运行结束，我们才可以接收到下一个消息
     */
//    @RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void receiveMessage(Message message, OrderReturnReasonEntity orderReturnReasonEntity,
                               Channel channel) {
//        byte[] body = message.getBody();
//        MessageProperties properties = message.getMessageProperties();
//        System.out.println("接收到的消息：" + message + "----->>>内容:" + entity);
        //这个值在channel内按顺序自增
        System.out.println("接收到的消息：" + orderReturnReasonEntity);
        System.out.println("消息处理完成");
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //签收货物，非批量模式
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            //网络中断
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void receiveMessage2(OrderEntity orderEntity) {
        System.out.println("接收到的消息：" + orderEntity);
    }

}