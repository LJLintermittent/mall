package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.SkuInfoEntity;
import com.learn.mall.product.entity.vo.front.SkuItemVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface SkuInfoService extends IService<SkuInfoEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SKU的基本信息
     */
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    /**
     * 带条件的分页查询
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 根据spuID查询出所有的SKU
     */
    List<SkuInfoEntity> getSkuInfoBySpuId(Long spuId);

    /**
     * 根据skuId获取前端需要展示的所有sku详情信息，异步+线程池优化
     */
    SkuItemVo searchSkuVoInfoBySkuId(Long skuId) throws ExecutionException, InterruptedException;

}

