package com.learn.mall.ware.service.impl;

import com.learn.common.constant.WareConstant;
import com.learn.mall.ware.dao.PurchaseDetailDao;
import com.learn.mall.ware.entity.PurchaseDetailEntity;
import com.learn.mall.ware.entity.vo.MergeVo;
import com.learn.mall.ware.entity.vo.PurchaseDoneVo;
import com.learn.mall.ware.entity.vo.PurchaseItemsDoneVo;
import com.learn.mall.ware.exception.PurchaseDetailException;
import com.learn.mall.ware.service.PurchaseDetailService;
import com.learn.mall.ware.service.WareSkuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.ware.dao.PurchaseDao;
import com.learn.mall.ware.entity.PurchaseEntity;
import com.learn.mall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@SuppressWarnings("all")
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private PurchaseDetailDao purchaseDetailDao;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询状态为新建和已分配的采购单
     * 采购单新建状态为0 已分配状态为1
     *
     * @param params 前端参数 （分页参数）
     * @return page
     */
    @Override
    public PageUtils queryPageUnReceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );
        return new PageUtils(page);
    }

    /**
     * 合并采购需求到采购单中
     */
    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        List<Long> itemIds = mergeVo.getItems();
        List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailDao.selectBatchIds(itemIds);
        double total = 0.0;
        for (PurchaseDetailEntity purchaseDetailEntity : purchaseDetailEntityList) {
            BigDecimal skuPrice = purchaseDetailEntity.getSkuPrice();
            Integer status = purchaseDetailEntity.getStatus();
            if (status == 1 || status == 2 || status == 3 || status == 4) {
                //确认采购需求项状态是0(新建状态)才可以合并 ，如果采购项状态为已分配，那么就已经有了采购单，无法再合并整单
                throw new PurchaseDetailException(WareConstant.PurchaseDetailExceptionEnum.MERGE.getCode(),
                        WareConstant.PurchaseDetailExceptionEnum.MERGE.getMsg());
            }
            double price = skuPrice.doubleValue();
            total = total + price;
        }
        if (purchaseId == null) {
            BigDecimal totalPrice = new BigDecimal(total);
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setAmount(totalPrice);
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            PurchaseEntity purchaseEntity = this.baseMapper.selectById(purchaseId);
            BigDecimal price1 = purchaseEntity.getAmount();
            Double doublePrice1 = 0.0;
            Double doublePrice2;
            if (price1 != null) {
                doublePrice1 = price1.doubleValue();
            }
            doublePrice2 = total + doublePrice1;
            BigDecimal bigPrice2 = new BigDecimal(doublePrice2);
            purchaseEntity.setAmount(bigPrice2);
            purchaseEntity.setId(purchaseId);
            this.updateById(purchaseEntity);
        }
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = itemIds.stream().map((i) -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(i);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailsStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
    }

    /**
     * 采购人员在采购人员的APP上领取采购单功能
     */
    @Override
    public void received(List<Long> ids) {
        //1.确认该采购单是新建状态或已分配状态
        List<PurchaseEntity> collect = ids.stream().map((id) -> {
            PurchaseEntity purchaseEntity = this.getById(id);
            return purchaseEntity;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()
                    || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            } else {
                return false;
            }
        }).map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
            return item;
        }).collect(Collectors.toList());
        //2.改变采购单状态
        this.updateBatchById(collect);
        //3.改变采购项状态
        collect.forEach((item) -> {
            //拿到采购项
            List<PurchaseDetailEntity> PurchaseDetailEntity = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> collect1 = PurchaseDetailEntity.stream().map((entity -> {
                com.learn.mall.ware.entity.PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailsStatusEnum.BUYING.getCode());
                return entity1;
            })).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);
        });

    }

    /**
     * 完成采购，将会改变采购单状态和每一个采购需求的状态，如果采购失败，还要有每一项采购需求失败的原因
     */
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        //改变采购项状态
        boolean flag = true;
        List<PurchaseItemsDoneVo> items = doneVo.getItems();
        List<PurchaseDetailEntity> purchaseDetailEntities = new ArrayList<>();
        for (PurchaseItemsDoneVo item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailsStatusEnum.BUYERROR.getCode()) {
                flag = false;
                purchaseDetailEntity.setStatus(item.getStatus());
            } else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailsStatusEnum.FINISHED.getCode());
                //将成功采购的商品入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
            }
            purchaseDetailEntity.setId(item.getItemId());
            purchaseDetailEntities.add(purchaseDetailEntity);
        }
        purchaseDetailService.updateBatchById(purchaseDetailEntities);
        //改变采购单状态
        Long id = doneVo.getId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISHED.getCode() :
                WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        this.updateById(purchaseEntity);

    }

    /**
     * 带条件的分页查询
     *
     * @param params 条件参数
     *               page: 1
     *               limit: 10
     *               key: 1
     *               status: 2
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((queryWrapper) -> {
                queryWrapper.eq("id", key).or().eq("assignee_id", key)
                        .or().like("assignee_name", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);

    }

}