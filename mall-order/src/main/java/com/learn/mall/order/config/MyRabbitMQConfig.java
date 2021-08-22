package com.learn.mall.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * date: 2021/5/11 21:07
 * Package: com.learn.mall.order.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@SuppressWarnings("all")
@Configuration
public class MyRabbitMQConfig {

    //    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 解决循环依赖问题
     * <p>
     * The dependencies of some of the beans in the application context form a cycle:
     * <p>
     * servletEndpointRegistrar defined in class path resource [org/springframework/boot/actuate/autoconfigure/endpoint/web/ServletEndpointManagementContextConfiguration$WebMvcServletEndpointManagementContextConfiguration.class]
     * ↓
     * healthEndpoint defined in class path resource [org/springframework/boot/actuate/autoconfigure/health/HealthEndpointConfiguration.class]
     * ↓
     * healthIndicatorRegistry defined in class path resource [org/springframework/boot/actuate/autoconfigure/health/HealthIndicatorAutoConfiguration.class]
     * ↓
     * org.springframework.boot.actuate.autoconfigure.amqp.RabbitHealthIndicatorAutoConfiguration
     * ┌─────┐
     * |  rabbitTemplate defined in class path resource [org/springframework/boot/autoconfigure/amqp/RabbitAutoConfiguration$RabbitTemplateConfiguration.class]
     * ↑     ↓
     * |  myRabbitMQConfig (field org.springframework.amqp.rabbit.core.RabbitTemplate com.learn.mall.order.config.MyRabbitMQConfig.rabbitTemplate)
     */
    //TODO 循环依赖
    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setMessageConverter(messageConverter());
        initRabbitTemplate();
        return rabbitTemplate;
    }

    /**
     * 模拟定时关单测试
     */
//    @RabbitListener(queues = "order.release.order.queue")
//    public void listener(OrderEntity entity, Channel channel, Message message) throws IOException {
//        System.out.println("收到过期的订单消息，准备关闭订单" + entity.getOrderSn());
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//    }

    /**
     * 创建队列 交换机 绑定关系
     * 容器中的Binding Queue Exchange 都会自动创建（在RabbitMQ没有的情况下）
     */
    @Bean
    public Queue orderDelayQueue() {
        //String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", 60000);
        Queue queue = new Queue("order.delay.queue", true, false
                , false, arguments);
        return queue;
    }

    @Bean
    public Queue orderReleaseOrderQueue() {
        Queue queue = new Queue("order.release.order.queue", true, false
                , false);
        return queue;
    }

    @Bean
    public Exchange orderEventExchange() {
        //String name, boolean durable, boolean autoDelete, Map<String, Object> arguments
        TopicExchange exchange = new TopicExchange("order-event-exchange", true, false);
        return exchange;
    }

    @Bean
    public Binding orderCreateOrderBinding() {
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        //			Map<String, Object> arguments
        Binding binding = new Binding("order.delay.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.create.order", null);
        return binding;
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        Binding binding = new Binding("order.release.order.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.order", null);
        return binding;
    }

    /**
     * 订单释放直接和库存释放进行绑定
     */
    @Bean
    public Binding orderReleaseOtherBinding() {
        Binding binding = new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.other.#", null);
        return binding;
    }

    /**
     * 秒杀服务的MQ业务（主要作用是削峰）
     */
    @Bean
    public Queue orderSeckillOrderQueue() {
        Queue queue = new Queue("order.seckill.order.queue", true, false, false);
        return queue;
    }

    @Bean
    public Binding orderSeckillOrderQueueBinding() {
        Binding binding = new Binding("order.seckill.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.seckill.order",
                null);
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

    /**
     * 定制RabbitTemplate
     */
//    @PostConstruct//MyRabbitMQConfig对象创建完成以后，执行这个方法
    public void initRabbitTemplate() {
        //1.设置生产者发送给broker的确认回调机制
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * MQ的代理 也就是broker收到了消息，那么这个方法会自动回调
             * 只要消息抵达服务器代理 broker 就ack=true
             * @param correlationData 当前消息唯一关联的数据（唯一ID）
             * @param ack 消息是否成功送到
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("correlationData::" + correlationData + "akc::" + "cause::" + cause);
            }
        });
        //2.设置交换机给队列发送消息的失败回调机制
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 只要消息没有投递给指定的队列，就会触发这个失败的回调
             * @param message 投递失败的消息的详细信息
             * @param replyCode 回复的状态码
             * @param replyText 回复的文本内容
             * @param exchange 当时这个消息发给哪个交换机
             * @param routingKey 当时这个消息用哪个路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("message::" + message + "replyCode::" + replyCode + "replyText::" + replyText
                        + "exchange::" + exchange + "routingKey::" + routingKey);
            }
        });
        /**
         *  3.消费端确认(保证每个消息被正确消费，此时broker才会删除这个消息)
         *  默认是自动确认的，只要消息接收到了，客户端会自动确认，服务器就会移除这个消息
         *  这种默认机制是有问题的: 我们收到很多消息，自动回复给服务器ack，只有一个消息处理成功，宕机了，消息丢失
         *  解决： 消费者手动确认模式，只要我们没有明确告诉MQ，货物被签收，没有ack，消息就一直是ack状态
         *  即使consumer宕机了，消息也不会丢失，会重新变为ready状态，下一次有新的consumer连接进来，就发给它
         */
    }

}
