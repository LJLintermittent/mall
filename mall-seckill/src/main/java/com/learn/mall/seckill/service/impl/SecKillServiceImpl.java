package com.learn.mall.seckill.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.learn.common.to.mq.SeckillOrderTo;
import com.learn.common.utils.R;
import com.learn.common.vo.MemberRespVo;
import com.learn.mall.seckill.feign.CouponFeignService;
import com.learn.mall.seckill.feign.ProductFeignService;
import com.learn.mall.seckill.interceptor.LoginUserInterceptor;
import com.learn.mall.seckill.service.SecKillService;
import com.learn.mall.seckill.to.SecKillSkuRedisTo;
import com.learn.mall.seckill.vo.SecKillSessionsWithSkus;
import com.learn.mall.seckill.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Description:
 * date: 2021/5/23 20:45
 * Package: com.learn.mall.seckill.service.impl
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 秒杀（高并发）系统关注的问题
 * 1.服务单一职责+独立部署
 * 秒杀服务即使自己扛不住压力，挂掉，不要影响其他微服务的运转
 * 2.秒杀链接加密
 * 防止恶意攻击，模拟秒杀请求，防止链接暴露，开发人员拿到链接提前秒杀
 * 3.库存预热+快速扣减
 * 秒杀读多写少，无需每次实时校验库存，应该进行库存预热，放到redis中
 * 分布式信号量控制进来秒杀的请求
 * 4.动静分离
 * nginx做好动静分离，保证秒杀和商品详情页的动态请求才打到后端服务集群，使用CDN网络，分担集群压力
 * 5.恶意请求拦截
 * 识别非法攻击请求并进行拦截，在网关层进行
 * 6.流量错峰
 * 使用各种手段，将流量分担到更大宽度的时间点，比如验证码，加入购物车
 * 7.限流&熔断&降级
 * 前端限流+后端限流
 * 限制次数，限制总量，快速失败降级运行
 * 熔断隔离防止雪崩
 * 8.队列削峰
 * 所有秒杀成功的请求，进入消息队列，慢慢创建订单，慢慢扣减库存
 * 所有人来到系统去redis抢信号量（信号量操作是一个原子操作，不存在超卖问题）
 * 拿到信号量的人放行给后台，后台将订单请求发送给消息队列，订单服务监听这个消息队列
 * 订单服务可以慢慢进行消费，
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //秒杀活动的key
    private static final String SESSIONS_CACHE_PREFIX = "secKill:sessions:";

    //秒杀商品的key
    private static final String SECKILLSKU_CACHE_PREFIX = "secKill:skus";

    //秒杀商品的库存信号量
    private static final String SKU_STOCK_SEMAPHORE = "secKill:stock:";//+商品的随机码

    /**
     * 上架最近三天需要参与秒杀的商品
     * 把最近三天需要参与的秒杀商品放到redis缓存中
     */
    @Override
    public void putOnSecKillSkuLatest3Days() {
        //扫描最近三天需要参加秒杀的活动
        R result = couponFeignService.getLatest3DaysSession();
        if (result.getCode() == 0) {
            List<SecKillSessionsWithSkus> data = result.getData(new TypeReference<List<SecKillSessionsWithSkus>>() {
            });
            saveSessionInfos(data);
            saveSessionSkusInfos(data);
        }
    }

    /**
     * getCurrentTimeSecKillSkusResource资源的限流回调方法
     */
    public List<SecKillSkuRedisTo> blockHandler(BlockException e) {
        log.error("getCurrentTimeSecKillSkusResource 被限流了");
        return null;
    }

    /**
     * 获取当前时间参与秒杀的商品信息
     */
    @SentinelResource(value = "getCurrentTimeSecKillSkusResource", blockHandler = "blockHandler")
    @Override
    public List<SecKillSkuRedisTo> getCurrentTimeSecKillSkus() {
        //确定当前时间属于哪个秒杀场次
        long nowTime = new Date().getTime();
        Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        //"secKill:sessions:1621789200000_1621789800000"
        for (String key : keys) {
            String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
            //1621789200000_1621789800000
            String[] s = replace.split("_");
            Long startTime = Long.parseLong(s[0]);
            Long endTime = Long.parseLong(s[1]);
            if (nowTime >= startTime && nowTime <= endTime) {
                //获取这个秒杀场次需要的所有商品信息
                List<String> range = redisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> operations = redisTemplate.boundHashOps(SECKILLSKU_CACHE_PREFIX);
                List<String> list = operations.multiGet(range);
                if (list != null) {
                    List<SecKillSkuRedisTo> collect = list.stream().map(item -> {
                        SecKillSkuRedisTo redis = JSON.parseObject((String) item, SecKillSkuRedisTo.class);
                        return redis;
                    }).collect(Collectors.toList());
                    return collect;
                }
            }
        }
        return null;
    }

    /**
     * 根据商品id查询当前商品是否属于秒杀商品
     * 用于给商品服务做远程调用
     * 业务点：如果该商品是秒杀商品，那么在商品详情页会提示该商品为秒杀商品
     */
    @Override
    public SecKillSkuRedisTo getSecKillSkuInfo(Long skuId) {
        //找到所有需要参与秒杀的商品的key
        BoundHashOperations<String, String, String> operations = redisTemplate.boundHashOps(SECKILLSKU_CACHE_PREFIX);
        Set<String> keys = operations.keys();
        if (keys != null && keys.size() > 0) {
            String regx = "\\d_" + skuId;
            for (String key : keys) {
                if (Pattern.matches(regx, key)) {
                    String json = operations.get(key);
                    SecKillSkuRedisTo skuRedisTo = JSON.parseObject(json, SecKillSkuRedisTo.class);
                    //随机码
                    long currentTime = new Date().getTime();
                    if (currentTime >= skuRedisTo.getStartTime() && currentTime <= skuRedisTo.getEndTime()) {

                    } else {
                        skuRedisTo.setRandomCode(null);
                    }
                    return skuRedisTo;
                }
            }
        }
        return null;
    }

    /**
     * 秒杀
     *
     * @param killId : 秒杀的商品id 例如：4_16 表示4号秒杀活动下的16号商品
     * @param key    : 秒杀的商品的随机码
     * @param num    : 购买的商品数量
     */
    @Override
    public String secKill(String killId, String key, Integer num) {
        long s1 = System.currentTimeMillis();
        MemberRespVo respVo = LoginUserInterceptor.threadLocal.get();
        //1、获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, String> operations = redisTemplate.boundHashOps(SECKILLSKU_CACHE_PREFIX);
        String json = operations.get(killId);
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            SecKillSkuRedisTo redis = JSON.parseObject(json, SecKillSkuRedisTo.class);
            //校验合法性
            Long startTime = redis.getStartTime();
            Long endTime = redis.getEndTime();
            long nowTime = new Date().getTime();
            long ttl = endTime - nowTime;
            //1、校验时间的合法性
            if (nowTime >= startTime && nowTime <= endTime) {
                //2、校验随机码和商品id
                String randomCode = redis.getRandomCode();
                String skuId = redis.getPromotionSessionId() + "_" + redis.getSkuId();
                if (randomCode.equals(key) && killId.equals(skuId)) {
                    //3、验证购物数量是否合理
                    if (num <= redis.getSeckillLimit()) {
                        //4、验证这个人是否已经购买过。幂等性; 如果只要秒杀成功，就去占位。  userId_SessionId_skuId
                        //SETNX
                        String redisKey = respVo.getId() + "_" + skuId;
                        //自动过期
                        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString()
                                , ttl, TimeUnit.MILLISECONDS);
                        if (aBoolean) {
                            //占位成功说明从来没有买过
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            //120  20ms
                            boolean b = semaphore.tryAcquire(num);
                            if (b) {
                                //秒杀成功;
                                //快速下单。发送MQ消息  10ms
                                String timeId = IdWorker.getTimeId();
                                SeckillOrderTo orderTo = new SeckillOrderTo();
                                orderTo.setOrderSn(timeId);
                                orderTo.setMemberId(respVo.getId());
                                orderTo.setNum(num);
                                orderTo.setPromotionSessionId(redis.getPromotionSessionId());
                                orderTo.setSkuId(redis.getSkuId());
                                orderTo.setSeckillPrice(redis.getSeckillPrice());
                                rabbitTemplate.convertAndSend("order-event-exchange"
                                        , "order.seckill.order", orderTo);
                                long s2 = System.currentTimeMillis();
                                log.info("耗时...{}", (s2 - s1));
                                return timeId;
                            }
                            return null;
                        } else {
                            //说明已经买过了
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * (缓存)保存秒杀活动信息
     * redis数据结构： list
     * key: secKill:sessions:开始时间_结束时间
     * value：该秒杀活动中保存的秒杀商品的id(已更改为秒杀场次id_秒杀商品id)
     */
    private void saveSessionInfos(List<SecKillSessionsWithSkus> list) {
        list.stream().forEach(session -> {
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime + "_" + endTime;
            //定时上架任务的幂等性处理，如果已经存在这个key，则不用再次上架了
            Boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey) {
                List<String> collect = session.getRelationEntities().stream()
                        .map(item -> item.getPromotionSessionId().toString() + "_" + item.getSkuId().toString())
                        .collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key, collect);
            }
        });
    }

    /**
     * (缓存)保存秒杀活动中对应的秒杀商品信息
     * redis数据结构： hash
     * key：秒杀场次id_商品id
     * value：secKillSkuRedisTo:专门为redis存商品信息封装的To
     * 里面包含sku的基本信息以及sku的秒杀信息
     */
    private void saveSessionSkusInfos(List<SecKillSessionsWithSkus> list) {
        list.stream().forEach(session -> {
            //绑定hash操作
            BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(SECKILLSKU_CACHE_PREFIX);
            session.getRelationEntities().stream().forEach(secKillSkuVo -> {
                //4.商品的随机码  seckill?skuId=1&key=xxxxsdewfwef 防止接口名称的暴露
                String randomCode = UUID.randomUUID().toString().replaceAll("-", "");
                if (!operations.hasKey(secKillSkuVo.getPromotionSessionId().toString() + "_"
                        + secKillSkuVo.getSkuId().toString())) {
                    SecKillSkuRedisTo secKillSkuRedisTo = new SecKillSkuRedisTo();
                    //1.sku的基本信息
                    R skuInfo = productFeignService.GetSkuInfo(secKillSkuVo.getSkuId());
                    if (skuInfo.getCode() == 0) {
                        SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                        });
                        secKillSkuRedisTo.setSkuInfo(data);
                    }
                    //2.sku的秒杀信息
                    BeanUtils.copyProperties(secKillSkuVo, secKillSkuRedisTo);
                    //3.设置当前秒杀商品的时间信息
                    secKillSkuRedisTo.setStartTime(session.getStartTime().getTime());
                    secKillSkuRedisTo.setEndTime(session.getEndTime().getTime());
                    //设置当前商品的秒杀随机码
                    secKillSkuRedisTo.setRandomCode(randomCode);
                    String s = JSON.toJSONString(secKillSkuRedisTo);
                    operations.put(secKillSkuVo.getPromotionSessionId().toString() + "_"
                            + secKillSkuVo.getSkuId().toString(), s);
                    //5.引入分布式信号量（使用设置的秒杀总量（可以理解为库存，但不一定等同于商品库存）作为分布式信号量）
                    //信号量的一大作用，就是限流
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                    //商品可以秒杀的数量作为信号量
                    semaphore.trySetPermits(secKillSkuVo.getSeckillCount());
                }
            });
        });
    }
}
