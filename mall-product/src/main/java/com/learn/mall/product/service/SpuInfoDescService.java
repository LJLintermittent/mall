package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SPU的描述信息，这个描述信息字段是longtext长文本，用来保存很多的图片描述
     */
    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);

}

