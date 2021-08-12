package com.learn.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:18:51
 */
@SuppressWarnings("all")
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 采购信息的带条件分页查询
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 按照采购单ID 找到采购单里面对应的采购项
     */
    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);

    void saveAll(PurchaseDetailEntity purchaseDetail);

    void updateAllById(PurchaseDetailEntity purchaseDetail);

}

