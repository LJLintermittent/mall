package com.learn.mall.ware.service.impl;

import com.learn.common.constant.WareConstant;
import com.learn.common.utils.R;
import com.learn.mall.ware.exception.PurchaseDetailException;
import com.learn.mall.ware.feign.ProductFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.ware.dao.PurchaseDetailDao;
import com.learn.mall.ware.entity.PurchaseDetailEntity;
import com.learn.mall.ware.service.PurchaseDetailService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("purchaseDetailService")
@SuppressWarnings("all")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Autowired
    private ProductFeignService productFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                new QueryWrapper<PurchaseDetailEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((queryWrapper) -> {
                queryWrapper.eq("purchase_id", key).or().eq("sku_id", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);

    }

    /**
     * 按照采购单ID 找到采购单里面对应的采购项
     *
     * @param id 采购单ID
     * @return
     */
    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("purchase_id", id);
        return this.list(wrapper);
    }

    /**
     * 保存的时候除了保存前端传的参数，还计算采购金额
     *
     * @param purchaseDetail
     */
    @Transactional
    @Override
    public void saveAll(PurchaseDetailEntity purchaseDetail) {
        Long skuId = purchaseDetail.getSkuId();
        R result = productFeignService.info(skuId);
        Map<String, Object> data = (Map<String, Object>) result.get("skuInfo");
        if (result.getCode() == 0 && data != null) {
            Double price = (Double) data.get("price");
            Double skuNum = Double.valueOf(purchaseDetail.getSkuNum());
            double total = price * skuNum;
            BigDecimal totalPrice = new BigDecimal(total);
            purchaseDetail.setSkuPrice(totalPrice);
            this.save(purchaseDetail);
        } else {
            throw new PurchaseDetailException(WareConstant.PurchaseDetailExceptionEnum.ERROR.getCode()
                    , WareConstant.PurchaseDetailExceptionEnum.ERROR.getMsg());
        }
    }

    /**
     * id: 39
     * purchaseId: null
     * skuId: 1
     * skuNum: 10
     * skuPrice: 67990
     * status: 0
     * t: 1619149286925
     * wareId: 2
     *
     * @param purchaseDetail 参数
     */
    @Override
    public void updateAllById(PurchaseDetailEntity purchaseDetail) {
        Long skuId = purchaseDetail.getSkuId();
        R result = productFeignService.info(skuId);
        Map<String, Object> data = (Map<String, Object>) result.get("skuInfo");
        Double price = (Double) data.get("price");
        Integer skuNum = purchaseDetail.getSkuNum();
        double num = skuNum.doubleValue();
        double skuPrice;
        skuPrice = num * price;
        BigDecimal finalPrice = new BigDecimal(skuPrice);
        purchaseDetail.setSkuPrice(finalPrice);
        this.updateById(purchaseDetail);
    }

}