package com.learn.mall.coupon.dao;

import com.learn.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:34:26
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
