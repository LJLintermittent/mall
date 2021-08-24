package com.learn.mall.product.web;

import com.learn.mall.product.entity.vo.front.SkuItemVo;
import com.learn.mall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * Description:
 * date: 2021/5/6 18:31
 * Package: com.learn.mall.product.web
 *
 * @author 李佳乐
 * @version 1.0
 */
@Controller
@SuppressWarnings("all")
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * TODO 商品详情信息大查询 异步 线程池 优化 压测
     * 跳转并展示当前sku的详细信息
     * 异步 + 线程池优化接口
     * 压测：JVM参数：-Xmx512m -Xms512m 所有涉及到的sql查询语句都添加了适合的索引，使用exlain分析后也都使用到了索引
     * 压测参数:单压微服务接口，不过任何中间件，比如nginx，gateway，线程数200，ramp-up：1s，循环次数：永远，样本数量：20000
     * 1.没有使用  异步+线程池 的实现的压测结果：
     * 吞吐量：335/sec
     * 2.使用异步+线程池优化后的压测结果：
     * 样本数量：20000，测试结果：吞吐量：269.2/sec
     * 这里测试结果发现使用了线程池+异步编排反而不如原来的串行化执行
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = skuInfoService.searchSkuVoInfoBySkuId(skuId);
        model.addAttribute("item", skuItemVo);
        return "item";
    }
}
