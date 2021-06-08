package com.learn.mall.ware.listener;

import com.learn.common.to.mq.OrderTo;
import com.learn.common.to.mq.StockLockedTo;
import com.learn.mall.ware.service.WareSkuService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Description:
 * date: 2021/5/19 15:10
 * Package: com.learn.mall.ware.listener
 *
 * @author 李佳乐
 * @version 1.0
 */
@Component
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {

    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 这个监听作用是如果库存后面还有服务出现了异常，导致订单服务回滚了，但是库存无法回滚
     * 解决方法就是通过延时队列来达到库存解锁 最终一致性思想
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        System.out.println("收到解锁库存的消息");
        try {
            wareSkuService.unlockStockRelease(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    /**
     *这个监听作用是如果一切服务都正常，但是订单长时间不付款已经过期了，属于正常情况
     * 这时订单状态为已经取消状态了，那么需要让库存服务感知，来快速解锁库存
     * 与上面的监听做一个双重保证
     * 因为如果订单服务卡顿，导致没有来得及给库存通知，那么库存解锁消息到时了，没人通知，就会永远解不了库存
     */
    @RabbitHandler
    public void handleOrderCloseRelease(OrderTo orderTo, Message message, Channel channel) throws IOException {
        System.out.println("收到订单关闭的消息，准备解锁库存");
        try {
            wareSkuService.unlockStockRelease(orderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
