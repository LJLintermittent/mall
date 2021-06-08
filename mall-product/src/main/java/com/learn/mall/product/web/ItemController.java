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
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 跳转并展示当前sku的详细信息
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = skuInfoService.searchSkuVoInfoBySkuId(skuId);
        model.addAttribute("item", skuItemVo);
        return "item";
    }
}
