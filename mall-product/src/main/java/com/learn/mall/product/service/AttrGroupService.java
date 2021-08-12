package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.AttrGroupEntity;
import com.learn.mall.product.entity.vo.AttrGroupWithAttrsVo;
import com.learn.mall.product.entity.vo.front.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface AttrGroupService extends IService<AttrGroupEntity> {

    /**
     * 原生分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据分类ID来进行分页查询
     */
    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    /**
     * 查出当前三级分类下所有的属性分组以及当前分组下所有的基本属性
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrByCategoryId(Long catelogId);

    /**
     * 查出当前spu对应的所有属性分组信息以及每一个分组下对应的属性名字和属性值
     */
    List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrBySpuId(Long spuId, Long catalogId);

}

