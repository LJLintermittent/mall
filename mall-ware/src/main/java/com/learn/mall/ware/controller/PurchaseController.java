package com.learn.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.learn.mall.ware.entity.vo.MergeVo;
import com.learn.mall.ware.entity.vo.PurchaseDoneVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.ware.entity.PurchaseEntity;
import com.learn.mall.ware.service.PurchaseService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 采购信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:18:51
 */
@Api(tags = "采购信息模块")
@RestController
@RequestMapping("ware/purchase")
@SuppressWarnings("all")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    /**
     * 完成采购，对接采购人员app接口
     */
    @ApiOperation(value = "完成采购，对接采购人员app接口")
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo doneVo) {
        purchaseService.done(doneVo);
        return R.ok();
    }

    /**
     * 领取采购单 对接采购人员app接口
     *
     * @param ids 要领取的采购单ID
     */
    @ApiOperation(value = "领取采购单 对接采购人员app接口")
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids) {
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * 合并采购需求项到某一个采购单中
     *
     * @param mergeVo MergeVo
     * @return
     */
    @ApiOperation(value = "合并采购需求项到某一个采购单中")
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo) {
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * 查询状态为新建和已分配的采购单
     *
     * @param params 前端参数 （分页参数）
     * @return page
     */
    @ApiOperation(value = "查询状态为新建和已分配的采购单")
    @GetMapping("/unreceive/list")
    public R unReceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnReceivePurchase(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);
        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody PurchaseEntity purchase) {
        purchaseService.save(purchase);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
