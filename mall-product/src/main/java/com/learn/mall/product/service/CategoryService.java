package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.CategoryEntity;
import com.learn.mall.product.entity.vo.front.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenusByIds(List<Long> asList);

    Long[] findCatelogPath(Long catelogId);

    void updateDetails(CategoryEntity category);

    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>> getCatalogJson();

}
