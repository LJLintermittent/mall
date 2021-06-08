package com.learn.mall.product.dao;

import com.learn.mall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectCanSearchAttrIds(@Param("attrIds") List<Long> attrIds);

}
