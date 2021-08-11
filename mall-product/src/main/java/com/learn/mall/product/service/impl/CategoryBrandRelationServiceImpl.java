package com.learn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.learn.mall.product.entity.BrandEntity;
import com.learn.mall.product.entity.CategoryEntity;
import com.learn.mall.product.service.BrandService;
import com.learn.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.product.dao.CategoryBrandRelationDao;
import com.learn.mall.product.entity.CategoryBrandRelationEntity;
import com.learn.mall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
@SuppressWarnings("all")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    /**
     * 分页查询
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );
        return new PageUtils(page);
    }

    /**
     * 保存品牌ID和分类ID在品牌分类关联表中
     * 不仅保存两者的ID，还要查出他们的名称并保存
     */
    @Override
    public void saveDetails(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandService.getById(brandId);
        String brandEntityName = brandEntity.getName();
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        String categoryEntityName = categoryEntity.getName();
        categoryBrandRelation.setBrandName(brandEntityName);
        categoryBrandRelation.setCatelogName(categoryEntityName);
        this.save(categoryBrandRelation);

    }

    /**
     * 更新其他表的品牌名称，将冗余表字段与实际表字段进行一致性更新
     */
    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        UpdateWrapper<CategoryBrandRelationEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("brand_id", brandId);
        this.update(categoryBrandRelationEntity, wrapper);
    }

    /**
     * 根据分类ID来更新分类名称，此接口SQL语句手写，非调用API
     */
    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId, name);
    }

    /**
     * 根据分类来获取所有品牌
     */
    @Override
    public List<BrandEntity> getBrandByCategoryId(Long catId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", catId);
        List<CategoryBrandRelationEntity> relationEntities = categoryBrandRelationDao.selectList(wrapper);
        //通过查出的关联关系，利用里面的品牌ID 查出品牌的详情
        //想通过这个方法不止查出品牌ID和品牌名字，还查出品牌的详细信息，为其他业务做代码复用
        List<BrandEntity> collect = relationEntities.stream().map((item) -> {
            Long brandId = item.getBrandId();
            BrandEntity brandEntity = brandService.getById(brandId);
            return brandEntity;
        }).collect(Collectors.toList());
        return collect;
    }

}