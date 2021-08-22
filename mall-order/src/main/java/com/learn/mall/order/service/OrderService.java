package com.learn.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.to.mq.SeckillOrderTo;
import com.learn.common.utils.PageUtils;
import com.learn.mall.order.entity.OrderEntity;
import com.learn.mall.order.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:11:13
 */
@SuppressWarnings("all")
public interface OrderService extends IService<OrderEntity> {

    /**
     * 通用分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 返回订单确认页需要的数据
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    /**
     * 下单
     * 创建订单，验证防重令牌，验证价格，锁库存等等
     */
    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);

    /**
     * 根据订单号查询订单的状态信息（远程调用接口）
     */
    OrderEntity getOrderStatusByOrderSn(String orderSn);

    /**
     * 定时关单 rabbitMQ监听器调用
     */
    void closeOrder(OrderEntity orderEntity);

    /**
     * 获取当前订单的支付信息
     */
    PayVo getOrderPay(String orderSn);

    /**
     * 支付完成以后查询出当前登录用户的订单列表数据
     */
    PageUtils queryPageWithItem(Map<String, Object> params);

    /**
     * 根据支付宝的异步回调 发过来的vo数据 来更改订单的状态
     */
    String handlePayResult(PayAsyncVo payAsyncVo);

    /**
     * 创建秒杀订单
     */
    void createSecKillOrder(SeckillOrderTo seckillOrderTo);

}

