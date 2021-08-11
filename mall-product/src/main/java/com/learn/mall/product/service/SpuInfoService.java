package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.SpuInfoEntity;
import com.learn.mall.product.entity.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface SpuInfoService extends IService<SpuInfoEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存录入的商品信息，包括SPU信息和SKU信息以及会员满减等跨库信息
     */
    void saveSpuInfo(SpuSaveVo spuSaveVo);

    /**
     * @param spuInfoEntity
     */
    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    /**
     * @param params
     * @return
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * @param spuId
     */
    void up(Long spuId);

    /**
     * @param spuId
     * @param code
     */
    void updateSpuUpStatus(Long spuId, int code);

    /**
     * @param skuId
     * @return
     */
    SpuInfoEntity getSpuInfoBySkuId(Long skuId);

}

