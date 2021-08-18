package com.learn.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:34:26
 */
@SuppressWarnings("all")
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    /**
     * 基本分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
}

