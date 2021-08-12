package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.SkuSaleAttrValueEntity;
import com.learn.mall.product.entity.vo.front.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据商品（spuID）获取这个商品所有的销售属性和每个销售属性对应的所有的属性值
     * 和每个属性值对应的所有具体销售商品（sku）的ID
     */
    List<SkuItemVo.SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId);

    /**
     * 根据skuId 返回这个sku对应的销售属性的名字和属性的值
     * 比如： 颜色：蓝色  版本： 8GB+128GB
     */
    List<String> getSkuSaleAttrValuesAsString(Long skuId);

}

