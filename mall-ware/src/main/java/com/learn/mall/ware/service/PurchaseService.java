package com.learn.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.ware.entity.PurchaseEntity;
import com.learn.mall.ware.entity.vo.MergeVo;
import com.learn.mall.ware.entity.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:18:51
 */
@SuppressWarnings("all")
public interface PurchaseService extends IService<PurchaseEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未被领取的采购单，也就是可以将采购需求合并进去的采购单
     */
    PageUtils queryPageUnReceivePurchase(Map<String, Object> params);

    /**
     * 合并采购需求到采购单中
     */
    void mergePurchase(MergeVo mergeVo);

    /**
     * 采购人员在采购人员的APP上领取采购单功能
     */
    void received(List<Long> ids);

    /**
     * 完成采购，将会改变采购单状态和每一个采购需求的状态，如果采购失败，还要有每一项采购需求失败的原因
     */
    void done(PurchaseDoneVo doneVo);

    /**
     * 带条件的分页查询
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

}

