package com.learn.mall.search.service;

import com.learn.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * Description:
 * date: 2021/4/25 14:32
 * Package: com.learn.mall.search.service
 *
 * @author 李佳乐
 * @version 1.0
 */
public interface ProductSaveService {

    /**
     * 收集商品服务传过来的构造好的商品检索数据然后将这些数据保存在ES中
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;

}
