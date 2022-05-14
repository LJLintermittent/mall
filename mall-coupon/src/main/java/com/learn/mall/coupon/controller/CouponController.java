package com.learn.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.coupon.entity.CouponEntity;
import com.learn.mall.coupon.service.CouponService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 优惠券信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:34:26
 */
@SuppressWarnings("all")
@Api(tags = "优惠券信息模块")
@RefreshScope
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    /**
     * TODO：配置中心的使用说明
     *
     * @RefreshScope：配置自动刷新注解 使用Nacos配置中心的步骤：
     * 首先建立一个bootstrap.properties文件，这个配置文件的在spingboot的加载顺序中早于application.properties
     * 在bootstrap.properties中配置Nacos的地址和本微服务的服务名称
     * 然后在springboot项目启动的时候会有日志打出，说它会默认到 [应用名].properties文件中去读取配置
     * 那么我们就可以在Nacos中建立这个文件，将我们要动态修改的配置搬家到Nacos中，配置中心中改完配置以后一发布
     * 然后加上 @RefreshScope注解，表示配置刷新，最终就能看到配合修改完毕的效果
     * 注意：如果配置中心有，会优先使用配置中心的
     * <p>
     * 配置中心的细节：
     * 1）.命名空间：
     * 做配置隔离,默认有一个public命名空间，默认新增的配置都在public命名空间
     * 例子：开发环境的配置空间，测试环境的，生产环境的（dev,test,prod）
     * 在配置中心中选择特定的命名空间，比如有开发环境的配置，测试环境的配置等
     * spring.cloud.nacos.config.namespace=94faffaa-9692-44b7-9e98-72cc5891c301
     * 除了开发环境之间的隔离可以用名称空间，每一个微服务还可以做名称空间的隔离
     * 2）.配置集
     * 所有的配置的集合叫配置集
     * 3）.配置集ID
     * DataID 类似于springboot项目中的配置文件名
     * 4）.配置分组
     * 在真实开发中：可以使用名称空间来划分微服务，每个微服务在用配置分组来区分dev，test，prod环境
     */
    @Value("${coupon.user.name}")
    private String name;
    @Value("${coupon.user.age}")
    private String age;

    /**
     * 测试配置中心的接口
     */
    @ApiOperation(value = "测试配置中心的接口,无实际意义")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public R test() {
        return R.ok().put("name", name).put("age", age);
    }

    /**
     * 测试优惠券服务与会员服务的远程调用
     * 模拟一个优惠券
     */
    @ApiOperation(value = "测试优惠券服务与会员服务的远程调用，无实际意义")
    @RequestMapping(value = "/member/list", method = RequestMethod.POST)
    public R memberCoupon() {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("满100减10");
        return R.ok().put("coupons", Arrays.asList(couponEntity));

    }

    /**
     * 列表
     */
    @ApiOperation(value = "查询所有优惠券信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = couponService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation("根据ID来查询优惠券信息")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        CouponEntity coupon = couponService.getById(id);
        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @ApiOperation("保存优惠券信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody CouponEntity coupon) {
        couponService.save(coupon);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("修改优惠券信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody CouponEntity coupon) {
        couponService.updateById(coupon);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation("删除优惠券")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        couponService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
