package com.learn.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.order.entity.OrderReturnReasonEntity;

import java.util.Map;

/**
 * 退货原因
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:11:13
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    /**
     * 通用分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
}

