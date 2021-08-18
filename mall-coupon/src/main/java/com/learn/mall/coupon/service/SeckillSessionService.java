package com.learn.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.coupon.entity.SeckillSessionEntity;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:34:26
 */
@SuppressWarnings("all")
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    /**
     * 基本分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取最近三天的秒杀活动
     */
    List<SeckillSessionEntity> getLatest3DaysSession();

}

