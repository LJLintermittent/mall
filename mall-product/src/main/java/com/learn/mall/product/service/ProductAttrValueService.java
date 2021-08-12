package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SPU的属性信息，注意：这个属性只包含基本属性，不包含销售属性
     */
    void saveProductAttrValue(List<ProductAttrValueEntity> collect);

    /**
     * 获取SPU的所有规格参数信息
     */
    List<ProductAttrValueEntity> queryBaseAttrListForSpu(Long spuId);

    /**
     * 更新规格参数信息
     */
    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);

}

