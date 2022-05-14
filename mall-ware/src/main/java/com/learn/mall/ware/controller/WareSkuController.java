package com.learn.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.learn.common.exception.StatusCodeEnum;
import com.learn.mall.ware.entity.vo.SkuHasStockVo;
import com.learn.mall.ware.entity.vo.WareSkuLockVo;
import com.learn.mall.ware.exception.NoStockException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.ware.entity.WareSkuEntity;
import com.learn.mall.ware.service.WareSkuService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 商品库存
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:18:51
 */
@Api(tags = "商品库存模块")
@RestController
@SuppressWarnings("all")
@RequestMapping("ware/waresku")
public class WareSkuController {

    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 订单业务锁定库存需求
     * 返回哪个sku锁定了几件，锁定成功了还是失败了
     */
    @ApiOperation(value = "订单业务锁定库存,返回哪个sku锁定了几件，锁定成功了还是失败了")
    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo) {
        // flag代表锁成功了还是失败了
        try {
            Boolean flag = wareSkuService.orderLockStock(vo);
            return R.ok();
        } catch (NoStockException e) {
            return R.error(StatusCodeEnum.NO_STOCK_EXCEPTION.getCode(),
                    StatusCodeEnum.NO_STOCK_EXCEPTION.getMsg());
        }
    }

    /**
     * 查询这些Sku是否有库存
     * 远程调用方法
     *
     * @param skuIds 要上架的SPU对应的所有SKU的ID （需要检查这些SkuId对应的Sku是否还有库存）
     * @return
     */
    @ApiOperation(value = "查询这些Sku是否有库存")
    @PostMapping("/hasstock")
    public R getSkuHasStock(@RequestBody List<Long> skuIds) {
        List<SkuHasStockVo> vos = wareSkuService.getSkuHasStock(skuIds);
        return R.ok().setData(vos);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);
        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateAllById(wareSku);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
