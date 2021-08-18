package com.learn.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.coupon.entity.SeckillPromotionEntity;

import java.util.Map;

/**
 * 秒杀活动
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:34:26
 */
@SuppressWarnings("all")
public interface SeckillPromotionService extends IService<SeckillPromotionEntity> {

    /**
     * 基本分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
}

