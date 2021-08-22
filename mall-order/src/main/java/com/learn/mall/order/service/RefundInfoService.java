package com.learn.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:11:13
 */
@SuppressWarnings("all")
public interface RefundInfoService extends IService<RefundInfoEntity> {

    /**
     * 通用分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
}

