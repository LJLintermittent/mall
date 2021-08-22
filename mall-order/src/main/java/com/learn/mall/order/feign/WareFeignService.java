package com.learn.mall.order.feign;

import com.learn.common.utils.R;
import com.learn.mall.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Description:
 * date: 2021/5/13 20:09
 * Package: com.learn.mall.order.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-ware")
public interface WareFeignService {

    /**
     * 根据这批skuID来查看是否有库存
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);

    /**
     * 根据收货地址来计算运费
     */
    @GetMapping("/ware/wareinfo/fare")
    R getFare(@RequestParam("addrId") Long addrId);

    /**
     * 根据订单号和锁住的库存信息，返回哪个sku锁定了几件，锁定成功了还是失败了
     */
    @PostMapping("/ware/waresku/lock/order")
    R orderLockStock(@RequestBody WareSkuLockVo vo);

}
