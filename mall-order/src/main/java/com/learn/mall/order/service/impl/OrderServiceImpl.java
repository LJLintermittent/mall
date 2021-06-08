package com.learn.mall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.learn.common.to.mq.OrderTo;
import com.learn.common.to.mq.SeckillOrderTo;
import com.learn.common.utils.R;
import com.learn.common.vo.MemberRespVo;
import com.learn.mall.order.constant.OrderConstant;
import com.learn.mall.order.entity.OrderItemEntity;
import com.learn.mall.order.entity.PaymentInfoEntity;
import com.learn.mall.order.enume.OrderStatusEnum;
import com.learn.mall.order.exception.NoStockException;
import com.learn.mall.order.feign.CartFeignService;
import com.learn.mall.order.feign.MemberFeignService;
import com.learn.mall.order.feign.ProductFeignService;
import com.learn.mall.order.feign.WareFeignService;
import com.learn.mall.order.interceptor.LoginUserInterceptor;
import com.learn.mall.order.service.OrderItemService;
import com.learn.mall.order.service.PaymentInfoService;
import com.learn.mall.order.to.OrderCreateTo;
import com.learn.mall.order.vo.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.order.dao.OrderDao;
import com.learn.mall.order.entity.OrderEntity;
import com.learn.mall.order.service.OrderService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@SuppressWarnings("all")
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<OrderSubmitVo> submitVoThreadLocal = new ThreadLocal<>();

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private MemberFeignService memberFeignService;

    @Autowired
    private CartFeignService cartFeignService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 返回订单确认页需要的数据
     */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberRespVo memberRespVo = LoginUserInterceptor.threadLocal.get();
        /**
         * feign在异步调用丢失应用上下文问题
         * 原因 threadLocal只在当前线程执行中共享数据，而异步任务开了多个线程
         * 解决：获取之前的请求域，在每一异步任务中共享
         */
        //获取之前的请求
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<Void> getAddressTask = CompletableFuture.runAsync(() -> {
            //远程查询当前会员所有的收获地址列表(会员服务)
            //每一个线程都来共享之前的请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
            orderConfirmVo.setAddress(address);
        }, threadPoolExecutor);
        CompletableFuture<Void> getCartItemsTask = CompletableFuture.runAsync(() -> {
            //远程查询购物车中所有选中的购物项(购物车服务)
            /**
             * feign远程调用丢失请求头的问题
             *解决：加上feign远程调用的请求拦截器
             */
            //每一个线程都来共享之前的请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> items = cartFeignService.getCurrentUserCartItems();
            orderConfirmVo.setItems(items);
        }, threadPoolExecutor).thenRunAsync(() -> {
            List<OrderItemVo> items = orderConfirmVo.getItems();
            List<Long> collect = items.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
            R r = wareFeignService.getSkuHasStock(collect);
            List<SkuStockVo> data = r.getData(new TypeReference<List<SkuStockVo>>() {
            });
            if (data != null) {
                Map<Long, Boolean> map = data.stream().collect(Collectors
                        .toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock));
                orderConfirmVo.setStock(map);
            }
        }, threadPoolExecutor);
        //查询用户积分
        Integer integration = memberRespVo.getIntegration();
        orderConfirmVo.setIntegration(integration);
        //订单的防重令牌
        String orderToken = UUID.randomUUID().toString().replace("-", "");
        //给redis服务器保存一份orderToken用于校验
        stringRedisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId()
                , orderToken, 30, TimeUnit.MINUTES);
        //给前端页面保存一份orderToken
        orderConfirmVo.setOrderToken(orderToken);
        //其他数据自动计算
        CompletableFuture.allOf(getAddressTask, getCartItemsTask).get();
        return orderConfirmVo;
    }

    /**
     * 本地事务的失效问题
     * 同一个对象内事务方法的互相调用，事务方法会默认失效，原因：绕过了代理对象
     * 事务是使用代理对象来控制的
     * <p>
     * 解决：使用代理对象来调用事务方法 aop
     * AspectJ动态代理
     */
    @Transactional(timeout = 30)
    public void a() {
        OrderServiceImpl orderService = (OrderServiceImpl) AopContext.currentProxy();
        orderService.b();
        orderService.c();
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 2)
    public void b() {

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 30)
    public void c() {

    }

    /**
     * 下单
     * 创建订单，验证防重令牌，验证价格，锁库存。。
     * <p>
     * 本地事务在分布式环境下的问题：
     * 1.订单服务异常，库存锁定不运行，全部回滚，撤销操作
     * 2.库存服务自治，锁定失败全部回滚，订单感受到，继续回滚（订单服务也回滚）
     * 3.库存服务锁定成功，但是网络原因返回数据中途出现问题，出现库存服务假异常，导致订单回滚，从而锁了库存但是没有生成订单
     * 4.库存服务锁定成功，库存服务下面的逻辑发生故障，订单回滚，但是库存服务不会回滚，已执行的远程请求，不能回滚
     * <p>
     * 解决：
     * 分布式事务seata
     * <p>
     * 下单 高并发场景 不适合使用AT模式的seata做分布式事务
     * 为了在订单回滚后库存也能回滚 使用消息队列给库存服务发送一个消息来回滚
     * MQ用途：1.保证事务的最终一致性，延时队列
     */
//    @GlobalTransactional
    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        //前端表单提交的数据
        submitVoThreadLocal.set(vo);
        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        MemberRespVo memberRespVo = LoginUserInterceptor.threadLocal.get();
        responseVo.setCode(0);
        //验证防重令牌 令牌的对比和删除必须是原子性操作
        //脚本返回值： 0：令牌校验失败 | 1：删除成功
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del',KEYS[1]) " +
                "else return 0 " +
                "end";
        String orderToken = vo.getOrderToken();
        //原子验证令牌和删除令牌
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class)
                , Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId()), orderToken);
        if (result == 0L) {
            //验证失败
            responseVo.setCode(1);
            return responseVo;
        } else {
            //验证成功
            //1.创建订单，订单项等信息
            OrderCreateTo order = createOrder();
            //2.验价
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = vo.getPayPrice();
            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
                //3.保存订单
                saveOrder(order);
                //4.锁定库存,只要有异常 回滚订单数据
                WareSkuLockVo lockVo = new WareSkuLockVo();
                lockVo.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVo> locks = order.getOrderItems().stream().map(item -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).collect(Collectors.toList());
                lockVo.setLocks(locks);
                //远程锁定库存
                R r = wareFeignService.orderLockStock(lockVo);
                if (r.getCode() == 0) {
                    //锁成功
                    responseVo.setOrder(order.getOrder());
                    //模拟远程积分服务异常，如果使用本地事务会导致订单回滚，库存不回滚
//                    int i = 10 / 0;
                    //订单创建成功就发送消息给MQ
                    rabbitTemplate.convertAndSend("order-event-exchange",
                            "order.create.order", order.getOrder());
                    return responseVo;
                } else {
                    //锁失败,msg:商品库存不足
                    String msg = (String) r.get("msg");
                    throw new NoStockException(msg);
                }
            } else {
                //价格验证失败
                responseVo.setCode(2);
                return responseVo;
            }
        }
    }

    /**
     * 根据订单号查询订单的状态信息（远程调用接口）
     */
    @Override
    public OrderEntity getOrderStatusByOrderSn(String orderSn) {
        OrderEntity orderEntity = this.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
        return orderEntity;
    }

    /**
     * 定时关单 rabbitMQ监听器调用
     */
    @Override
    public void closeOrder(OrderEntity entity) {
        //查询当前这个订单的最新状态
        OrderEntity orderEntity = this.getById(entity.getId());
        //状态为待付款状态，超过了30分钟，自动关闭订单
        if (orderEntity.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())) {
            OrderEntity update = new OrderEntity();
            update.setId(entity.getId());
            update.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(update);
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(orderEntity, orderTo);
            //给MQ发送消息，通知库存服务解锁库存
            rabbitTemplate.convertAndSend("order-event-exchange"
                    , "order.release.other", orderTo);
        }
    }

    /**
     * 获取当前订单的支付信息
     */
    @Override
    public PayVo getOrderPay(String orderSn) {
        PayVo payVo = new PayVo();
        OrderEntity orderEntity = this.getOrderStatusByOrderSn(orderSn);
        BigDecimal payAmount = orderEntity.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        payVo.setTotal_amount(payAmount.toString());
        payVo.setOut_trade_no(orderEntity.getOrderSn());
        List<OrderItemEntity> orderItemEntities = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderSn));
        OrderItemEntity orderItemEntity = orderItemEntities.get(0);
        payVo.setSubject(orderItemEntity.getSkuName());
        payVo.setBody(orderItemEntity.getSkuAttrsVals());
        return payVo;
    }

    /**
     * 支付完成以后查询出当前登录用户的订单列表数据
     */
    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        MemberRespVo memberRespVo = LoginUserInterceptor.threadLocal.get();
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
                        .eq("member_id", memberRespVo.getId())
                        .orderByDesc("id")
        );
        List<OrderEntity> collect = page.getRecords().stream().map(order -> {
            List<OrderItemEntity> itemEntities = orderItemService.list(new QueryWrapper<OrderItemEntity>()
                    .eq("order_sn", order.getOrderSn()));
            order.setItemEntities(itemEntities);
            return order;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        return new PageUtils(page);
    }

    /**
     * 根据支付宝的异步回调 发过来的vo数据 来更改订单的状态
     */
    @Override
    public String handlePayResult(PayAsyncVo payAsyncVo) {
        //1.保存交易流水信息，涉及表：oms_payment_info
        PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
        paymentInfoEntity.setOrderSn(payAsyncVo.getOut_trade_no());
        paymentInfoEntity.setAlipayTradeNo(payAsyncVo.getTrade_no());
        paymentInfoEntity.setPaymentStatus(payAsyncVo.getTrade_status());
        paymentInfoEntity.setCallbackTime(payAsyncVo.getNotify_time());
        paymentInfoService.save(paymentInfoEntity);
        //2.修改订单的状态
        if (payAsyncVo.getTrade_status().equals("TRADE_SUCCESS") || payAsyncVo.getTrade_status().equals("TRADE_FINISHED")) {
            //订单支付成功状态
            String tradeNo = payAsyncVo.getOut_trade_no();//订单号
            baseMapper.updateOrderStatus(tradeNo, OrderStatusEnum.PAYED.getCode());
        }
        return "success";
    }

    /**
     * TODO 创建秒杀订单
     * 只完成了基本的订单表和订单项表的部分数据的填充
     */
    @Override
    public void createSecKillOrder(SeckillOrderTo seckillOrderTo) {
        //保存部分订单信息
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderEntity.setMemberId(seckillOrderTo.getMemberId());
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        BigDecimal multiply = seckillOrderTo.getSeckillPrice().multiply(new BigDecimal("" + seckillOrderTo.getNum()));
        orderEntity.setPayAmount(multiply);
        this.save(orderEntity);
        //保存订单项信息
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderItemEntity.setRealAmount(multiply);
        orderItemEntity.setSkuQuantity(seckillOrderTo.getNum());
        orderItemService.save(orderItemEntity);
    }

    /**
     * 保存订单
     */
    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        baseMapper.insert(orderEntity);
        List<OrderItemEntity> orderItems = order.getOrderItems();
        orderItemService.saveBatch(orderItems);
    }

    /**
     * 创建订单TO
     * 里面包含订单，订单项，价格，运费信息
     */
    private OrderCreateTo createOrder() {
        OrderCreateTo createTo = new OrderCreateTo();
        //生成订单号
        String orderSn = IdWorker.getTimeId();
        //构建订单
        OrderEntity orderEntity = buildOrder(orderSn);
        //构建所有的订单项
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderSn);
        //验价 计算价格,积分等相关信息
        computePrice(orderEntity, orderItemEntities);
        createTo.setOrder(orderEntity);
        createTo.setOrderItems(orderItemEntities);
        return createTo;
    }

    /**
     * 计算相关价格信息
     */
    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal integration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");
        Integer giftIntegration = 0;
        Integer giftGrowth = 0;
        //整体订单的总额 = 每一个订单项的总额（实际金额）的叠加
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            coupon = coupon.add(orderItemEntity.getCouponAmount());
            integration = integration.add(orderItemEntity.getPromotionAmount());
            promotion = promotion.add(orderItemEntity.getIntegrationAmount());
            total = total.add(orderItemEntity.getRealAmount());
            giftIntegration = giftIntegration + orderItemEntity.getGiftIntegration();
            giftGrowth = giftGrowth + orderItemEntity.getGiftGrowth();
        }
        //订单总额
        orderEntity.setTotalAmount(total);
        //应付总额 = 订单总额 + 运费
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        //三项信息
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(integration);
        orderEntity.setCouponAmount(coupon);
        //设置积分信息
        orderEntity.setIntegration(giftIntegration);
        orderEntity.setGrowth(giftGrowth);
    }

    /**
     * 构建订单
     */
    private OrderEntity buildOrder(String orderSn) {
        MemberRespVo memberRespVo = LoginUserInterceptor.threadLocal.get();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setMemberId(memberRespVo.getId());
        orderEntity.setOrderSn(orderSn);
        //获取运费/收货地址信息
        OrderSubmitVo orderSubmitVo = submitVoThreadLocal.get();
        R wareFeignResult = wareFeignService.getFare(orderSubmitVo.getAddrId());
        //远程获得的运费信息和收货地址/收货人信息
        FareVo fareResp = wareFeignResult.getData(new TypeReference<FareVo>() {
        });
        //设置运费信息
        orderEntity.setFreightAmount(fareResp.getFare());
        //设置收货人信息
        orderEntity.setReceiverProvince(fareResp.getAddress().getProvince());
        orderEntity.setReceiverCity(fareResp.getAddress().getCity());
        orderEntity.setReceiverRegion(fareResp.getAddress().getRegion());
        orderEntity.setReceiverName(fareResp.getAddress().getName());
        orderEntity.setReceiverPhone(fareResp.getAddress().getPhone());
        orderEntity.setReceiverPostCode(fareResp.getAddress().getPostCode());
        orderEntity.setReceiverDetailAddress(fareResp.getAddress().getDetailAddress());
        //设置订单的状态信息
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);
        //0代表未删除
        orderEntity.setDeleteStatus(0);
        return orderEntity;
    }

    /**
     * 构建所有订单项
     * 将购物车中的购物项转化为订单项实体
     */
    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemVo> cartItems = cartFeignService.getCurrentUserCartItems();
        if (cartItems != null && cartItems.size() > 0) {
            List<OrderItemEntity> orderItemEntities = cartItems.stream().map(cartItem -> {
                OrderItemEntity orderItemEntity = buildOrderItem(cartItem);
                orderItemEntity.setOrderSn(orderSn);
                return orderItemEntity;
            }).collect(Collectors.toList());
            return orderItemEntities;
        }
        return null;
    }

    /**
     * 构建每一个订单项
     */
    private OrderItemEntity buildOrderItem(OrderItemVo cartItem) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        //1.订单信息：订单号
        //2.商品的spu信息
        Long skuId = cartItem.getSkuId();
        R productFeignResult = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoVo = productFeignResult.getData(new TypeReference<SpuInfoVo>() {
        });
        orderItemEntity.setSpuId(spuInfoVo.getId());
        orderItemEntity.setSpuName(spuInfoVo.getSpuName());
        orderItemEntity.setSpuBrand(spuInfoVo.getBrandId().toString());
        orderItemEntity.setCategoryId(spuInfoVo.getCatalogId());
        //3.商品的sku信息
        orderItemEntity.setSkuId(cartItem.getSkuId());
        orderItemEntity.setSkuName(cartItem.getTitle());
        orderItemEntity.setSkuPic(cartItem.getImage());
        orderItemEntity.setSkuPrice(cartItem.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttr);
        orderItemEntity.setSkuQuantity(cartItem.getCount());
        //4.优惠信息[未开发]
        //5.积分信息[模拟价格作为积分值]
        orderItemEntity.setGiftGrowth(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount()
                .toString())).intValue());
        orderItemEntity.setGiftIntegration(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount()
                .toString())).intValue());
        //6.订单项的价格信息
        orderItemEntity.setPromotionAmount(new BigDecimal("0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0"));
        //当前订单项的实际金额 = 总额（总额 = 单价 × 数量） - 各种优惠金额
        BigDecimal orign = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        BigDecimal realAmount = orign.subtract(orderItemEntity.getPromotionAmount()).subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(realAmount);
        return orderItemEntity;
    }
}