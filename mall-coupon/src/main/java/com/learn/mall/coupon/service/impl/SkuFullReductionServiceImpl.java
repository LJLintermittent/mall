package com.learn.mall.coupon.service.impl;

import com.learn.common.to.MemberPrice;
import com.learn.common.to.SkuReductionTo;
import com.learn.mall.coupon.entity.MemberPriceEntity;
import com.learn.mall.coupon.entity.SkuLadderEntity;
import com.learn.mall.coupon.service.MemberPriceService;
import com.learn.mall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.coupon.dao.SkuFullReductionDao;
import com.learn.mall.coupon.entity.SkuFullReductionEntity;
import com.learn.mall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 1.保存sms_sku_ladder表信息  满几件打几折
     * 2.保存sms_sku_full_reduction表信息 满多少钱减多少钱
     * 3.保存sms_member_price表信息 不同等级的会员买该商品应该是多少钱
     *
     * @param skuReductionTo 该sku满一定钱减多少钱，满几件打几折 和 会员减钱 等各种信息
     */
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        //1.保存sms_sku_ladder表信息  满几件打几折
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuReductionTo.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }
        //2.保存sms_sku_full_reduction表信息 满多少钱减多少钱
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0")) > 0) {
            this.save(skuFullReductionEntity);
        }
        //3.保存sms_member_price表信息 不同等级的会员买该商品应该是多少钱
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> collect = memberPrice.stream().map((item) -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter((item) -> {
            return item.getMemberPrice().compareTo(new BigDecimal("0")) > 0;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);

    }

}