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

    /**
     * TODO ThreadLocal
     * ThreadLocal?????????????????????????????????
     * ?????????entry?????????????????????threadlocalmap???????????????????????????entry??????????????????WeakReference??????????????????????????????
     * ????????????GC????????????
     * ??????????????????????????????????????????????????? ????????????????????????????????????????????????????????????
     * ????????????????????????????????????????????????hashmap????????????????????????????????????????????????????????????????????????????????????????????????????????????+1????????????
     * ???????????????????????????
     * private static final int HASH_INCREMENT = 0x61c88647;
     * ???????????????????????????
     * 0x61c88647?????????????????????0.618
     * <p>
     * ??????????????????????????????????????????threadlocal???????????????????????????????????????
     * ????????????????????????????????????threadlocalmap????????????????????????????????????????????????????????????????????????
     * ???set???get?????????????????????????????????????????????
     * set?????????
     * ??????1????????????????????????????????????????????????
     * ??????2. ??????????????????????????????key?????????????????????
     * ??????3. ??????????????????????????????key????????????????????????
     * ??????4. ??????????????????????????????key???????????????????????????key?????????????????????????????????GC??????????????????
     * ??????????????????threadlocal???????????????????????????key
     */
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

    /**
     * ??????????????????
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * ????????????????????????????????????
     */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberRespVo memberRespVo = LoginUserInterceptor.threadLocal.get();
        /*
         * TODO Feign???????????????
         * feign??????????????????????????????????????????
         * ?????? threadLocal???????????????????????????????????????????????????????????????????????????
         * ?????????????????????????????????????????????????????????????????????
         * ????????????????????? requestAttributesHolder ---> threadlocal
         * ????????????????????????????????????????????????threadlocal??????????????????????????????
         * ?????????feign?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * ????????????????????????????????????????????????requestAttributes????????????
         */
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<Void> getAddressTask = CompletableFuture.runAsync(() -> {
            /*
              ???????????????????????????????????????????????????(????????????)
              ????????????????????????????????????????????????
             */
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
            orderConfirmVo.setAddress(address);
        }, threadPoolExecutor);
        /*
         * ????????????????????????????????????????????????(???????????????)
         * feign???????????????????????????????????????
         * feign???????????????????????????????????????????????????????????????????????????
         *
         * ???????????????feign??????????????????????????????
         * feign???????????????
         * ReflectiveFeign.invoke???????????????????????????????????????equlas???hashcode???tostring???????????????
         * ?????????????????????????????????????????????
         * ????????????dispatch.get(method).invoke(args);
         * ??????invoke????????????SynchronousMethodHandler??????????????????invoke??????
         * ???????????????????????????RequestTemplate template = buildTemplateFromArgs.create(argv);
         * ????????????????????????????????????return executeAndDecode(template);
         * ????????????targetRequest?????????????????????????????????
         *   Request targetRequest(RequestTemplate template) {
         *     for (RequestInterceptor interceptor : requestInterceptors) {
         *       interceptor.apply(template);
         *     }
         *  ???????????????????????????????????????????????????apply???????????????????????????????????????????????????????????????????????????
         *  ????????????
         */
        CompletableFuture<Void> getCartItemsTask = CompletableFuture.runAsync(() -> {
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
        //??????????????????
        Integer integration = memberRespVo.getIntegration();
        orderConfirmVo.setIntegration(integration);
        //?????????????????????
        String orderToken = UUID.randomUUID().toString().replace("-", "");
        //???redis?????????????????????orderToken????????????
        stringRedisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId()
                , orderToken, 30, TimeUnit.MINUTES);
        //???????????????????????????orderToken
        orderConfirmVo.setOrderToken(orderToken);
        //????????????????????????
        CompletableFuture.allOf(getAddressTask, getCartItemsTask).get();
        return orderConfirmVo;
    }

    /**
     * TODO ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????
     * ???????????????????????????????????????????????? aop
     * AspectJ????????????
     */
    @Transactional(timeout = 30)
    public void a() {
        OrderServiceImpl orderService = (OrderServiceImpl) AopContext.currentProxy();
        orderService.b();
        orderService.c();
    }

    /**
     * ????????????????????????
     * REQUIRED??????????????????????????????????????????????????? ?????????????????????
     * REQUIRES_NEW?????????????????????????????????????????????????????????????????????????????????
     */
    @Transactional(propagation = Propagation.REQUIRED, timeout = 2)
    public void b() {

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 30)
    public void c() {

    }

    /**
     * ??????
     * ??????????????????????????????????????????????????????????????????
     * ?????????????????????????????????????????????
     * 1.????????????????????????????????????????????????????????????????????????
     * 2.?????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 3.???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 4.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ?????????
     * ???????????????seata
     * ?????? ??????????????? ???????????????AT?????????seata??????????????????
     * ?????????????????????????????????????????? ????????????????????????????????????????????????????????????
     * MQ?????????1.?????????????????????????????????????????????
     */
//    @GlobalTransactional
    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        //???????????????????????????
        submitVoThreadLocal.set(vo);
        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        MemberRespVo memberRespVo = LoginUserInterceptor.threadLocal.get();
        responseVo.setCode(0);
        //?????????????????? ????????????????????????????????????????????????
        //?????????????????? 0????????????????????? | 1???????????????
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del',KEYS[1]) " +
                "else return 0 " +
                "end";
        String orderToken = vo.getOrderToken();
        //?????????????????????????????????
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class)
                , Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId()), orderToken);
        if (result == 0L) {
            //????????????
            responseVo.setCode(1);
            return responseVo;
        } else {
            //????????????
            //1.?????????????????????????????????
            OrderCreateTo order = createOrder();
            //2.??????
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = vo.getPayPrice();
            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
                //3.????????????
                saveOrder(order);
                //4.????????????,??????????????? ??????????????????
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
                //??????????????????
                R r = wareFeignService.orderLockStock(lockVo);
                if (r.getCode() == 0) {
                    //?????????
                    responseVo.setOrder(order.getOrder());
                    //????????????????????????????????????????????????????????????????????????????????????????????????
                    //????????????????????????????????????MQ
                    rabbitTemplate.convertAndSend("order-event-exchange",
                            "order.create.order", order.getOrder());
                    return responseVo;
                } else {
                    //?????????,msg:??????????????????
                    responseVo.setCode(3);
                    String msg = (String) r.get("msg");
                    throw new NoStockException(msg);
                }
            } else {
                //??????????????????
                responseVo.setCode(2);
                return responseVo;
            }
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     */
    @Override
    public OrderEntity getOrderStatusByOrderSn(String orderSn) {
        OrderEntity orderEntity = this.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
        return orderEntity;
    }

    /**
     * ???????????? rabbitMQ???????????????
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    @Override
    public void closeOrder(OrderEntity entity) {
        //???????????????????????????????????????
        OrderEntity orderEntity = this.getById(entity.getId());
        //????????????????????????????????????30???????????????????????????
        if (orderEntity.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())) {
            OrderEntity update = new OrderEntity();
            update.setId(entity.getId());
            update.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(update);
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(orderEntity, orderTo);
            //?????????????????????????????????????????????MQ???????????????????????????????????????????????????
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            //????????????????????????????????????????????????????????????
            rabbitTemplate.convertAndSend("order-event-exchange"
                    , "order.release.other", orderTo);
        }
    }

    /**
     * ?????????????????????????????????
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
     * ??????????????????????????????????????????????????????????????????
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
     * ?????????????????????????????? ????????????vo?????? ????????????????????????
     */
    @Override
    public String handlePayResult(PayAsyncVo payAsyncVo) {
        //1.???????????????????????????????????????oms_payment_info
        PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
        paymentInfoEntity.setOrderSn(payAsyncVo.getOut_trade_no());
        paymentInfoEntity.setAlipayTradeNo(payAsyncVo.getTrade_no());
        paymentInfoEntity.setPaymentStatus(payAsyncVo.getTrade_status());
        paymentInfoEntity.setCallbackTime(payAsyncVo.getNotify_time());
        paymentInfoService.save(paymentInfoEntity);
        //2.?????????????????????
        if (payAsyncVo.getTrade_status().equals("TRADE_SUCCESS") || payAsyncVo.getTrade_status().equals("TRADE_FINISHED")) {
            //????????????????????????
            String tradeNo = payAsyncVo.getOut_trade_no();//?????????
            baseMapper.updateOrderStatus(tradeNo, OrderStatusEnum.PAYED.getCode());
        }
        return "success";
    }

    /**
     * ??????????????????
     * ?????????????????????????????????????????????????????????????????????
     * ????????????????????????
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????MQ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ????????????????????????????????????????????????????????????
     */
    @Override
    public void createSecKillOrder(SeckillOrderTo seckillOrderTo) {
        //????????????????????????
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderEntity.setMemberId(seckillOrderTo.getMemberId());
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        BigDecimal multiply = seckillOrderTo.getSeckillPrice().multiply(new BigDecimal("" + seckillOrderTo.getNum()));
        orderEntity.setPayAmount(multiply);
        this.save(orderEntity);
        //?????????????????????
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderItemEntity.setRealAmount(multiply);
        orderItemEntity.setSkuQuantity(seckillOrderTo.getNum());
        orderItemService.save(orderItemEntity);
    }

    /**
     * ????????????
     */
    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        baseMapper.insert(orderEntity);
        List<OrderItemEntity> orderItems = order.getOrderItems();
        orderItemService.saveBatch(orderItems);
    }

    /**
     * ????????????TO
     * ??????????????????????????????????????????????????????
     */
    private OrderCreateTo createOrder() {
        OrderCreateTo createTo = new OrderCreateTo();
        //???????????????
        String orderSn = IdWorker.getTimeId();
        //????????????
        OrderEntity orderEntity = buildOrder(orderSn);
        //????????????????????????
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderSn);
        //?????? ????????????,?????????????????????
        computePrice(orderEntity, orderItemEntities);
        createTo.setOrder(orderEntity);
        createTo.setOrderItems(orderItemEntities);
        return createTo;
    }

    /**
     * ????????????????????????
     */
    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal integration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");
        Integer giftIntegration = 0;
        Integer giftGrowth = 0;
        //????????????????????? = ??????????????????????????????????????????????????????
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            coupon = coupon.add(orderItemEntity.getCouponAmount());
            integration = integration.add(orderItemEntity.getPromotionAmount());
            promotion = promotion.add(orderItemEntity.getIntegrationAmount());
            total = total.add(orderItemEntity.getRealAmount());
            giftIntegration = giftIntegration + orderItemEntity.getGiftIntegration();
            giftGrowth = giftGrowth + orderItemEntity.getGiftGrowth();
        }
        //????????????
        orderEntity.setTotalAmount(total);
        //???????????? = ???????????? + ??????
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        //????????????
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(integration);
        orderEntity.setCouponAmount(coupon);
        //??????????????????
        orderEntity.setIntegration(giftIntegration);
        orderEntity.setGrowth(giftGrowth);
    }

    /**
     * ????????????
     */
    private OrderEntity buildOrder(String orderSn) {
        MemberRespVo memberRespVo = LoginUserInterceptor.threadLocal.get();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setMemberId(memberRespVo.getId());
        orderEntity.setOrderSn(orderSn);
        //????????????/??????????????????
        OrderSubmitVo orderSubmitVo = submitVoThreadLocal.get();
        R wareFeignResult = wareFeignService.getFare(orderSubmitVo.getAddrId());
        //??????????????????????????????????????????/???????????????
        FareVo fareResp = wareFeignResult.getData(new TypeReference<FareVo>() {
        });
        //??????????????????
        orderEntity.setFreightAmount(fareResp.getFare());
        //?????????????????????
        orderEntity.setReceiverProvince(fareResp.getAddress().getProvince());
        orderEntity.setReceiverCity(fareResp.getAddress().getCity());
        orderEntity.setReceiverRegion(fareResp.getAddress().getRegion());
        orderEntity.setReceiverName(fareResp.getAddress().getName());
        orderEntity.setReceiverPhone(fareResp.getAddress().getPhone());
        orderEntity.setReceiverPostCode(fareResp.getAddress().getPostCode());
        orderEntity.setReceiverDetailAddress(fareResp.getAddress().getDetailAddress());
        //???????????????????????????
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);
        //0???????????????
        orderEntity.setDeleteStatus(0);
        return orderEntity;
    }

    /**
     * ?????????????????????
     * ???????????????????????????????????????????????????
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
     * ????????????????????????
     */
    private OrderItemEntity buildOrderItem(OrderItemVo cartItem) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        //1.????????????????????????
        //2.?????????spu??????
        Long skuId = cartItem.getSkuId();
        R productFeignResult = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoVo = productFeignResult.getData(new TypeReference<SpuInfoVo>() {
        });
        orderItemEntity.setSpuId(spuInfoVo.getId());
        orderItemEntity.setSpuName(spuInfoVo.getSpuName());
        orderItemEntity.setSpuBrand(spuInfoVo.getBrandId().toString());
        orderItemEntity.setCategoryId(spuInfoVo.getCatalogId());
        //3.?????????sku??????
        orderItemEntity.setSkuId(cartItem.getSkuId());
        orderItemEntity.setSkuName(cartItem.getTitle());
        orderItemEntity.setSkuPic(cartItem.getImage());
        orderItemEntity.setSkuPrice(cartItem.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttr);
        orderItemEntity.setSkuQuantity(cartItem.getCount());
        //4.????????????[?????????]
        //5.????????????[???????????????????????????]
        orderItemEntity.setGiftGrowth(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount()
                .toString())).intValue());
        orderItemEntity.setGiftIntegration(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount()
                .toString())).intValue());
        //6.????????????????????????
        orderItemEntity.setPromotionAmount(new BigDecimal("0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0"));
        //?????????????????????????????? = ??????????????? = ?????? ?? ????????? - ??????????????????
        BigDecimal orign = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        BigDecimal realAmount = orign.subtract(orderItemEntity.getPromotionAmount()).subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(realAmount);
        return orderItemEntity;
    }
}