package com.learn.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.learn.mall.product.entity.ProductAttrValueEntity;
import com.learn.mall.product.entity.vo.AttrRespVo;
import com.learn.mall.product.entity.vo.AttrVo;
import com.learn.mall.product.service.ProductAttrValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.product.service.AttrService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 商品属性
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@Api(tags = "商品属性模块")
@RestController
@RequestMapping("product/attr")
@SuppressWarnings("all")
public class AttrController {

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * 更新规格参数信息
     *
     * @param spuId    SpuId
     * @param entities 前端表单提交的更新后的数据 数据与数据库表对应，不需要写Vo类
     * @return
     */
    @ApiOperation(value = "更新规格参数信息")
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities) {
        productAttrValueService.updateSpuAttr(spuId, entities);
        return R.ok();
    }

    /**
     * 查询当前spuId的所有规格参数信息
     *
     * @param spuId 当前spuId
     * @return
     */
    @ApiOperation(value = "查询当前spuId的所有规格参数信息")
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrListForSpu(@PathVariable("spuId") Long spuId) {
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.queryBaseAttrListForSpu(spuId);
        return R.ok().put("data", productAttrValueEntities);
    }

    /**
     * 查询规格参数
     */
    @ApiOperation(value = "查询规格参数")
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType") String type) {
        PageUtils page = attrService.queryPageAttrPage(params, catelogId, type);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @ApiOperation("列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation("信息")
    @RequestMapping(value = "/info/{attrId}", method = RequestMethod.GET)
    public R info(@PathVariable("attrId") Long attrId) {
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody AttrVo attr) {
        attrService.saveAttr(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public R update(@RequestBody AttrVo attr) {
        attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));
        return R.ok();
    }

}
