package com.learn.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.learn.common.to.SkuReductionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.coupon.entity.SkuFullReductionEntity;
import com.learn.mall.coupon.service.SkuFullReductionService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 商品满减信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:34:26
 */
@RestController
@RequestMapping("coupon/skufullreduction")
@SuppressWarnings("all")
public class SkuFullReductionController {

    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * 保存该sku满一定钱减多少钱，满几件打几折 和 会员减钱 等各种信息
     *
     * @param skuReductionTo 该sku 满一定钱减多少钱，满几件打几折 和 会员减钱 等各种信息
     * @return
     */
    @PostMapping("/saveInfo")
    public R saveInfo(@RequestBody SkuReductionTo skuReductionTo) {
        skuFullReductionService.saveSkuReduction(skuReductionTo);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);
        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.save(skuFullReduction);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.updateById(skuFullReduction);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        skuFullReductionService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
