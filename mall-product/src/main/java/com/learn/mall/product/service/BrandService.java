package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface BrandService extends IService<BrandEntity> {

    /**
     * 带条件的分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 修改商品信息，如果有关联数据，会同步更新其他表这个字段(就是将外键约束功能放在代码中实现)
     * 在数据库中禁止使用外键
     */
    void updateByIdDetails(BrandEntity brand);

    /**
     * 批量获取商品信息
     */
    List<BrandEntity> getBrandsByIds(List<Long> brandIds);

}

