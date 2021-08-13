package com.learn.mall.search;

import com.alibaba.fastjson.JSON;
import com.learn.mall.search.config.MallElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings("all")
public class MallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testSearch() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("product");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(QueryBuilders.idsQuery().
                addIds("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"));
        builder.query(boolQueryBuilder);
        searchRequest.source(builder);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, MallElasticSearchConfig.COMMON_OPTIONS);
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                list.add(sourceAsMap);
            }
            map.put("data", list);
            System.out.println(map.get("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void contextLoads() {
        System.out.println(restHighLevelClient);
    }

    /**
     * 索引  插入更新二合一
     *
     * @throws IOException
     */
    @Test
    public void testIndex() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user = new User();
        user.setUserName("李佳乐");
        user.setAge(21);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, MallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Data
    static class User {
        private String userName;
        private String gender;
        private Integer age;
    }

}
