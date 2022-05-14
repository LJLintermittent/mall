package com.learn.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.coupon.entity.CouponHistoryEntity;
import com.learn.mall.coupon.service.CouponHistoryService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 优惠券领取历史记录
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:34:26
 */
@Api(tags = "优惠券领取历史记录模块")
@RestController
@RequestMapping("coupon/couponhistory")
@SuppressWarnings("all")
public class CouponHistoryController {

    @Autowired
    private CouponHistoryService couponHistoryService;

    /**
     * 列表
     */
    @ApiOperation(value = "查询所有领取历史记录信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = couponHistoryService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "根据ID查询领取历史记录信息")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        CouponHistoryEntity couponHistory = couponHistoryService.getById(id);
        return R.ok().put("couponHistory", couponHistory);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存优惠券历史记录")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody CouponHistoryEntity couponHistory) {
        couponHistoryService.save(couponHistory);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改优惠券历史记录信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody CouponHistoryEntity couponHistory) {
        couponHistoryService.updateById(couponHistory);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除历史记录信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        couponHistoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
