package com.learn.mall.search.controller;

import com.learn.common.exception.StatusCodeEnum;
import com.learn.common.to.es.SkuEsModel;
import com.learn.common.utils.R;
import com.learn.mall.search.service.ProductSaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * date: 2021/4/25 14:27
 * Package: com.learn.mall.search.controller
 *
 * @author 李佳乐
 * @version 1.0
 */
@Api(tags = "商品上架模块(将数据保存到ES中)")
@Slf4j
@RestController
@SuppressWarnings("all")
@RequestMapping("/search/save")
public class ElasticSaveController {

    @Autowired
    private ProductSaveService productSaveService;

    /**
     * 商品上架,接收商品服务传过来的封装好的数据模型集合
     *
     * @param skuEsModels 上架商品数据模型
     */
    @ApiOperation(value = "商品上架,接收商品服务传过来的封装好的数据模型集合")
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean flag = false;
        try {
            flag = productSaveService.productStatusUp(skuEsModels);
        } catch (Exception e) {
            log.error("ElasticSearch服务商品上架错误");
            return R.error(StatusCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), StatusCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        //flag返回true是有错误，所以正常上架返回false
        if (!flag) {
            return R.ok();
        } else {
            return R.error(StatusCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), StatusCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }

}
