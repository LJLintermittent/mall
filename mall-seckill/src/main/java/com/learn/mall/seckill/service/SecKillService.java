package com.learn.mall.seckill.service;

import com.learn.mall.seckill.to.SecKillSkuRedisTo;

import java.util.List;

/**
 * Description:
 * date: 2021/5/23 20:45
 * Package: com.learn.mall.seckill.service
 *
 * @author 李佳乐
 * @version 1.0
 */
public interface SecKillService {

    void putOnSecKillSkuLatest3Days();

    List<SecKillSkuRedisTo> getCurrentTimeSecKillSkus();

    SecKillSkuRedisTo getSecKillSkuInfo(Long skuId);

    String secKill(String killId, String key, Integer num);

}
