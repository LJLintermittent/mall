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
@SuppressWarnings("all")
public interface WareSkuService extends IService<WareSkuEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * SKU库存的带条件分页查询
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 采购成功以后要将商品入库（入的是指定的库）
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * 修改仓库的时候不只是修改表中的仓库ID字段，还要将仓库对应的名字改正(级联更新)
     */
    void updateAllById(WareSkuEntity wareSku);

    /**
     * 检查每一个SKU是否有库存
     */
    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStockRelease(StockLockedTo to);

    void unlockStockRelease(OrderTo orderTo);

}

