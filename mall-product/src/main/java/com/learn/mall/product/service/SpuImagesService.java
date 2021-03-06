package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface SpuImagesService extends IService<SpuImagesEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SPU的图片集
     */
    void saveImages(Long id, List<String> images);

}

