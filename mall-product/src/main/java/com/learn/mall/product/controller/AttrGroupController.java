package com.learn.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.learn.mall.product.entity.AttrEntity;
import com.learn.mall.product.entity.vo.AttrGroupRelationVo;
import com.learn.mall.product.entity.vo.AttrGroupWithAttrsVo;
import com.learn.mall.product.service.AttrAttrgroupRelationService;
import com.learn.mall.product.service.AttrService;
import com.learn.mall.product.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.product.entity.AttrGroupEntity;
import com.learn.mall.product.service.AttrGroupService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 属性分组
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@Api(tags = "属性分组模块")
@RestController
@RequestMapping("product/attrgroup")
@SuppressWarnings("all")
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 保存属性与分组的关联关系
     */
    @ApiOperation(value = "保存属性与分组的关联关系")
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos) {
        attrAttrgroupRelationService.saveBatch(vos);
        return R.ok();
    }

    /**
     * 查出当前三级分类下所有的属性分组和每个属性分组下的所有属性
     *
     * @param catelogId 三级分类ID
     * @return 当前三级分类下所有的属性分组和每个属性分组下的所有属性
     * 对应Vo实体类 ： AttrGroupWithAttrsVo
     */
    @ApiOperation(value = "查出当前三级分类下所有的属性分组和每个属性分组下的所有属性")
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrByCategoryId(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrByCategoryId(catelogId);
        return R.ok().put("data", vos);
    }

    /**
     * 删除分组与属性的关联关系
     */
    @ApiOperation(value = "删除分组与属性的关联关系")
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos) {
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 获取当前分组关联的所有属性
     *
     * @param attrgroupId 当前分组ID
     * @return 当前分组关联的所有属性
     */
    @ApiOperation(value = "获取当前分组关联的所有属性")
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> data = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", data);
    }

    /**
     * 获取当前分组没有关联的所有属性
     *
     * @param attrgroupId 当前分组ID
     * @return 当前分组没有关联的所有属性
     */
    @ApiOperation(value = "获取当前分组没有关联的所有属性")
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     * PathVariable注解：
     * 接收路径请求中占位符的值，绑定到处理器类中的方法形参中
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list/{catelogId}", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }

    /**
     * 信息,根据属性分组的ID来获取属性分组的详细信息
     * 注意，这里还需要查询当前分类的父分类，从而给前端完整的分类数据
     * 这个接口是给属性分组，回显表单的时候查询用的
     */
    @ApiOperation(value = "根据属性分组的ID来获取属性分组的详细信息")
    @RequestMapping(value = "/info/{attrGroupId}", method = RequestMethod.GET)
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }

}
