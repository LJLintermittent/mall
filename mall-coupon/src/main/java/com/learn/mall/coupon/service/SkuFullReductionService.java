package com.learn.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.to.SkuReductionTo;
import com.learn.common.utils.PageUtils;
import com.learn.mall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:34:26
 */
@SuppressWarnings("all")
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SKU的满减等信息
     */
    void saveSkuReduction(SkuReductionTo skuReductionTo);

}

