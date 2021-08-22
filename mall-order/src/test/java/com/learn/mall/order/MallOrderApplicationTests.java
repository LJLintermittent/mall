package com.learn.mall.order;

import com.learn.mall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
@SuppressWarnings("all")
public class MallOrderApplicationTests {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * TODO:rabbitMQ
     * 一个队列可以由很多个服务来监听，只要收到消息，队列删除消息，而且只能有一个服务可以收到此消息
     * 也就是说假如订单服务有多个复制，订单服务以集群的模式部署，那么只会有一个服务来接收到消息
     * 在接收消息的方法处理中，只有这一个消息处理完毕了，才可以接收并处理下一个消息
     */
    @Test
    public void testSendMsg() {
        OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
        entity.setId(1L);
        entity.setCreateTime(new Date());
        entity.setName("xxx");
        rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java", entity);
        log.info("消息发送完成{}", entity);
    }

    @Test
    public void TestCreateExchange() {
        /**
         * public DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments
         */
        DirectExchange directExchange = new DirectExchange("hello-java-exchange");
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功", "hello-java-exchange");
    }

    @Test
    public void TestCreateQueue() {
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功", "hello-java-queue");
    }
}
