package com.learn.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learn.mall.product.entity.BrandEntity;
import com.learn.mall.product.entity.vo.BrandVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.product.entity.CategoryBrandRelationEntity;
import com.learn.mall.product.service.CategoryBrandRelationService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 品牌分类关联
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@Api(tags = "品牌分类关联模块")
@RestController
@RequestMapping("product/categorybrandrelation")
@SuppressWarnings("all")
public class CategoryBrandRelationController {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 获取当前品牌关联的所有分类
     */
    @ApiOperation(value = "获取当前品牌关联的所有分类")
    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam("brandId") Long brandId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", brandId);
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(wrapper);
        return R.ok().put("data", data);
    }

    /**
     * 根据分类获取所有品牌  (获取选中的分类 关联的所有品牌)
     * 需求： 发布商品时，选中商品分类以后 ，需要将品牌做一个回显
     * <p>
     * controller:处理请求，接收和校验数据
     * service:接收controller传来的数据，进行业务处理
     * controller:接收service处理完的数据，封装成页面指定的Vo
     *
     * @param catId 当前三级分类ID
     * @return 封装好的BrandVo对象 可能有多个，用List包装
     */
    @ApiOperation(value = "根据分类获取所有品牌")
    @GetMapping("/brands/list")
    public R getBrandsByCategoryId(@RequestParam(value = "catId", required = true) Long catId) {
        List<BrandEntity> brands = categoryBrandRelationService.getBrandByCategoryId(catId);
        List<BrandVo> data = brands.stream().map((item) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data", data);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);
        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存品牌名与分类的关联关系
     */
    @ApiOperation(value = "保存品牌名与分类的关联关系")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.saveDetails(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
