package com.learn.mall.order.controller;

import com.learn.mall.order.entity.OrderEntity;
import com.learn.mall.order.entity.OrderReturnReasonEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * date: 2021/5/11 21:49
 * Package: com.learn.mall.order.controller
 *
 * @author 李佳乐
 * @version 1.0
 */

@Api(tags = "RabbitMQ测试接口模块")
@SuppressWarnings("all")
@RestController
public class RabbitController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试rabbithandler与rabbitlistener注解作用的接口
     * 无实际调用
     */
    @ApiOperation(value = "测试rabbithandler与rabbitlistener注解作用的接口")
    @GetMapping("/sendMq")
    public String sendMq(@RequestParam(value = "num", defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                OrderReturnReasonEntity orderReturnReasonEntity = new OrderReturnReasonEntity();
                orderReturnReasonEntity.setId(1L);
                orderReturnReasonEntity.setCreateTime(new Date());
                orderReturnReasonEntity.setName("李佳乐" + i);
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java", orderReturnReasonEntity);
            } else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java", orderEntity);
            }
        }
        return "ok";
    }
}
