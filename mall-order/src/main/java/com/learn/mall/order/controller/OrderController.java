package com.learn.mall.order.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.order.entity.OrderEntity;
import com.learn.mall.order.service.OrderService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;

/**
 * 订单
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:11:13
 */
@Api(tags = "订单服务模块")
@RestController
@RequestMapping("order/order")
@SuppressWarnings("all")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 支付成功以后的回调页面
     * 需要查出当前登录用户的所有订单列表数据
     */
    @ApiOperation(value = "支付完成以后查询出当前登录用户的订单列表数据")
    @GetMapping("/listWithItem")
    public R OrderSuccessReturnPage(@RequestParam Map<String, Object> params) {
        PageUtils page = orderService.queryPageWithItem(params);
        return R.ok().put("page", page);
    }

    /**
     * 根据订单号获取订单状态，远程调用接口
     */
    @ApiOperation(value = "根据订单号获取订单状态，远程调用接口")
    @GetMapping("/status/{OrderSn}")
    public R getOrderStatusByOrderSn(@PathVariable("OrderSn") String OrderSn) {
        OrderEntity orderEntity = orderService.getOrderStatusByOrderSn(OrderSn);
        return R.ok().setData(orderEntity);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        OrderEntity order = orderService.getById(id);
        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody OrderEntity order) {
        orderService.updateById(order);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        orderService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
