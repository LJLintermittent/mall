package com.learn.mall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.learn.common.valid.AddGroup;
import com.learn.common.valid.UpdateGroup;
import com.learn.common.valid.UpdateStatusGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.product.entity.BrandEntity;
import com.learn.mall.product.service.BrandService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@Api(tags = "品牌管理模块")
@SuppressWarnings("all")
@RestController
@RequestMapping("product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 返回多个品牌信息
     */
    @ApiOperation(value = "返回多个品牌信息")
    @GetMapping("/infos")
    public R infos(@RequestParam("brandIds") List<Long> brandIds) {
        List<BrandEntity> brand = brandService.getBrandsByIds(brandIds);
        return R.ok().put("brand", brand);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @RequestMapping(value = "/info/{brandId}", method = RequestMethod.GET)
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);
        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@Validated(value = {AddGroup.class}) @RequestBody BrandEntity brand) {
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@Validated(value = {UpdateGroup.class}) @RequestBody BrandEntity brand) {
        brandService.updateByIdDetails(brand);
        return R.ok();
    }

    /**
     * 修改品牌状态
     */
    @ApiOperation(value = "修改品牌状态")
    @RequestMapping(value = "/update/status", method = RequestMethod.POST)
    public R updateStatus(@Validated(value = {UpdateStatusGroup.class}) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));
        return R.ok();
    }

    /**
     * 使用BindingResult的接口写法（不推荐使用，太繁琐，应该使用全局统一异常处理）
     * 此接口为示例接口，仅为演示代码所用
     */
    @ApiOperation(value = "使用BindingResult的接口写法（不推荐使用，太繁琐，应该使用全局统一异常处理）" +
            "此接口为示例接口，仅为演示代码所用")
    @RequestMapping(value = "/saveTest", method = RequestMethod.POST)
    public R saveTest(@Valid @RequestBody BrandEntity brand, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> map = new HashMap<>(16);
            result.getFieldErrors().forEach(item -> {
                String message = item.getDefaultMessage();
                String field = item.getField();
                map.put(field, message);
            });
            return R.error(400, "提交的数据不合法").put("data", map);
        } else {
            brandService.save(brand);
            return R.ok();
        }
    }

}
