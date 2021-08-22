package com.learn.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.order.entity.PaymentInfoEntity;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:11:13
 */
@SuppressWarnings("all")
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    /**
     * 通用分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
}

