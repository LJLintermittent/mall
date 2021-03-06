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
@SuppressWarnings("all")
public interface CategoryService extends IService<CategoryEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询三级分类（封装成树形数据结构展示）
     */
    List<CategoryEntity> listWithTree();

    /**
     * 根据分类id来批量删除分类
     */
    void removeMenusByIds(List<Long> asList);

    /**
     * 根据分类ID类查询当前分类往上的完整分类路径
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 更新分类本身的时候，还要更新在其他表中分类的名字
     */
    void updateDetails(CategoryEntity category);

    /**
     * 获取所有的一级分类,作为刚进入商城首页以后展示的数据，当鼠标放到一级分类上时，才会打开所有的分类
     */
    List<CategoryEntity> getLevel1Categorys();

    /**
     * 查询二级分类和三级分类
     */
    Map<String, List<Catelog2Vo>> getCatalogJson();

    /**
     * 不使用spring cache，只使用原生的redis查询策略进行压测
     */
    Map<String, List<Catelog2Vo>> getDataFromDB();

    /**
     * 不使用spring cache，使用Redisson分布锁+普通缓存读写策略
     */
    Map<String, List<Catelog2Vo>> getCatalogJsonFromDBOrRedisWithWithRedissonLock();
}

