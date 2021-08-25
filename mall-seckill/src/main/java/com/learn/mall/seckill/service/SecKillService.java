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
@SuppressWarnings("all")
public interface SecKillService {

    /**
     * 秒杀商品的定时上架
     */
    void putOnSecKillSkuLatest3Days();

    /**
     * 获取当前时间参与秒杀的商品信息
     */
    List<SecKillSkuRedisTo> getCurrentTimeSecKillSkus();

    /**
     * 根据商品id查询当前商品是否属于秒杀商品
     * 用于给商品服务做远程调用
     * 业务点：如果该商品是秒杀商品，那么在商品详情页会提示该商品为秒杀商品
     */
    SecKillSkuRedisTo getSecKillSkuInfo(Long skuId);

    /**
     * 秒杀
     */
    String secKill(String killId, String key, Integer num);

}
