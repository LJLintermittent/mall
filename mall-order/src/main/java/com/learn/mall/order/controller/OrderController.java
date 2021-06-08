package com.learn.mall.order.controller;

import java.util.Arrays;
import java.util.Map;

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
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 支付成功以后的回调页面
     * 需要查出当前登录用户的所有订单列表数据
     *
     * ps：课程中此接口为member服务远程调用接口
     */
//    @GetMapping("/listWithItem")
//    public R OrderSuccessReturnPage(@RequestParam Map<String, Object> params) {
//        PageUtils page = orderService.queryPageWithItem(params);
//        return R.ok().put("page",page);
//    }

    /**
     * 根据订单号获取订单状态，远程调用接口
     */
    @GetMapping("/status/{OrderSn}")
    public R getOrderStatusByOrderSn(@PathVariable("OrderSn") String OrderSn) {
        OrderEntity orderEntity = orderService.getOrderStatusByOrderSn(OrderSn);
        return R.ok().setData(orderEntity);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("order:order:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("order:order:info")
    public R info(@PathVariable("id") Long id) {
        OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids) {
        orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
