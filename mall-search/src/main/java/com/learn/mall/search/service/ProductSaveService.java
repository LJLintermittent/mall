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

    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;

}
