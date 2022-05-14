package com.learn.mall.order.web;

import com.learn.mall.order.entity.OrderEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * date: 2021/5/12 0:43
 * Package: com.learn.mall.order.web
 *
 * @author 李佳乐
 * @version 1.0
 */
@Api(tags = "定时关单，页面跳转模块")
@Controller
@SuppressWarnings("all")
public class IndexController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 模拟延时队列定时关单
     */
    @ApiOperation(value = "模拟延时队列定时关单")
    @ResponseBody
    @GetMapping("/test/createOrder")
    public String createOrderTest() {
        //模拟订单下单成功
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(UUID.randomUUID().toString());
        orderEntity.setModifyTime(new Date());
        //给MQ发消息
        rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", orderEntity);
        return "ok";
    }

    /**
     * 页面跳转
     */
    @ApiOperation(value = "页面跳转")
    @GetMapping("/{page}.html")
    public String listPage(@PathVariable("page") String page) {
        return page;
    }
}
