package com.learn.mall.seckill.scheduled;

import com.learn.mall.seckill.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * date: 2021/5/23 20:37
 * Package: com.learn.mall.seckill.scheduled
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 秒杀商品的定时上架
 * 例如：每天凌晨三点，上架最近三天需要参与秒杀的商品
 * 当天 00:00:00 -- 23:59:59
 * 明天 00:00:00 -- 23:59:59
 * 后天 00:00:00 -- 23:59:59
 */
@Service
@Slf4j
public class SecKillSkuSchedule {

    @Autowired
    private SecKillService secKillService;

    @Autowired
    private RedissonClient redissonClient;

    //秒杀商品上架的定时任务的分布式锁
    private static final String PUT_ON_LOCK = "secKill:putOn:lock";

    /**
     * 例如：每天凌晨三点整，上架今天需要参与秒杀的商品
     * 秒分时日月周
     */
    @Scheduled(cron = "* * */5 * * ?")
    public void putOnSecKillSkuLatest3Days() {
        log.info("上架秒杀的商品信息");
        RLock lock = redissonClient.getLock(PUT_ON_LOCK);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            secKillService.putOnSecKillSkuLatest3Days();
        } finally {
            lock.unlock();
        }
    }

}
