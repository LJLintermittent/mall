package com.learn.mall.product.service.impl;

import com.learn.mall.product.entity.vo.front.SkuItemVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.product.dao.SkuSaleAttrValueDao;
import com.learn.mall.product.entity.SkuSaleAttrValueEntity;
import com.learn.mall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据商品（spuID）获取这个商品所有的销售属性和每个销售属性对应的所有的属性值
     * 和每个属性值对应的所有具体销售商品（sku）的ID
     */
    @Override
    public List<SkuItemVo.SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId) {
        List<SkuItemVo.SkuItemSaleAttrVo> saleAttrVos = baseMapper.getSaleAttrsBySpuId(spuId);
        return saleAttrVos;
    }

    /**
     * 根据skuId 返回这个sku对应的销售属性的名字和属性的值
     * 比如： 颜色：蓝色  版本： 8GB+128GB
     */
    @Override
    public List<String> getSkuSaleAttrValuesAsString(Long skuId) {
        return baseMapper.getSkuSaleAttrValuesAsString(skuId);

    }
}