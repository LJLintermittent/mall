package com.learn.mall.seckill.controller;

import com.learn.common.utils.R;
import com.learn.mall.seckill.service.SecKillService;
import com.learn.mall.seckill.to.SecKillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description:
 * date: 2021/5/24 18:43
 * Package: com.learn.mall.seckill.controller
 *
 * @author 李佳乐
 * @version 1.0
 */
@Controller
@SuppressWarnings("all")
public class SecKillController {

    @Autowired
    private SecKillService secKillService;

    /**
     * 获取当前时间要参与秒杀的商品
     */
    @ResponseBody
    @GetMapping("/CurrentTimeSecKillSkus")
    public R getCurrentTimeSecKillSkus() {
        List<SecKillSkuRedisTo> data = secKillService.getCurrentTimeSecKillSkus();
        return R.ok().setData(data);
    }

    /**
     * 根据商品id查询当前商品是否属于秒杀商品
     * 用于给商品服务做远程调用
     * 业务点：如果该商品是秒杀商品，那么在商品详情页会提示该商品为秒杀商品
     */
    @ResponseBody
    @GetMapping("/sku/secKill/{skuId}")
    public R getSecKillSkuInfo(@PathVariable("skuId") Long skuId) {
        SecKillSkuRedisTo to = secKillService.getSecKillSkuInfo(skuId);
        return R.ok().setData(to);
    }

    /**
     * 秒杀
     */
    @GetMapping("/kill")
    public String secKill(@RequestParam("killId") String killId,
                          @RequestParam("key") String key,
                          @RequestParam("num") Integer num,
                          Model model) {
        String orderSn = secKillService.secKill(killId, key, num);
        model.addAttribute("orderSn", orderSn);
        return "success";
    }
}
