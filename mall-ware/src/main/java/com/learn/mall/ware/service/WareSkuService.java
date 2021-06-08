package com.learn.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.to.mq.OrderTo;
import com.learn.common.to.mq.StockLockedTo;
import com.learn.common.utils.PageUtils;
import com.learn.mall.ware.entity.WareSkuEntity;
import com.learn.mall.ware.entity.vo.SkuHasStockVo;
import com.learn.mall.ware.entity.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:18:51
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    void updateAllById(WareSkuEntity wareSku);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStockRelease(StockLockedTo to);

    void unlockStockRelease(OrderTo orderTo);

}

