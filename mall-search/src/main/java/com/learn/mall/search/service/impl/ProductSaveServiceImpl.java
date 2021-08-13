package com.learn.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.learn.common.to.es.SkuEsModel;
import com.learn.mall.search.config.MallElasticSearchConfig;
import com.learn.mall.search.constant.EsConstant;
import com.learn.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * date: 2021/4/25 14:34
 * Package: com.learn.mall.search.service.impl
 *
 * @author 李佳乐
 * @version 1.0
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 收集商品服务传过来的构造好的商品检索数据然后将这些数据保存在ES中
     */
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        //在ES中建立索引，product，并建立好映射关系 (操作Kibana)
        //向Es中保存数据
        //BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel esModel : skuEsModels) {
            //构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(esModel.getSkuId().toString());
            String jsonString = JSON.toJSONString(esModel);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, MallElasticSearchConfig.COMMON_OPTIONS);
        //如果批量处理错误，返回一个错误结果
        boolean flag = bulk.hasFailures();
        //返回true是有错误,所以正常返回是false，那么打印正常的上架日志应该为!false
        if (!flag) {
            List<Object> collect = Arrays.stream(bulk.getItems()).map(item -> {
                return item.getId();
            }).collect(Collectors.toList());
            log.info("成功上架的商品的ID: {}", collect);
        } else {
            log.info("商品上架出错");
        }
        return flag;
    }
}
