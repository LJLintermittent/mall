package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.BrandEntity;
import com.learn.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存品牌ID和分类ID在品牌分类关联表中
     * 电商系统的大表连接查询，一般使用冗余表来代替
     * 在冗余表中添加冗余字段，比如品牌名称和分类名称，这样就不用关联查询了
     */
    void saveDetails(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 更新其他表的品牌名称，将冗余表字段与实际表字段进行一致性更新
     */
    void updateBrand(Long brandId, String name);

    /**
     * 根据分类ID来更新分类名称，此接口SQL语句手写，非调用API
     */
    void updateCategory(Long catId, String name);

    /**
     * 根据分类来获取所有品牌
     */
    List<BrandEntity> getBrandByCategoryId(Long catId);

}

