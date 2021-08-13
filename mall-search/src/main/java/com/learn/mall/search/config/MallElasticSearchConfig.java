package com.learn.mall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * date: 2021/4/23 21:18
 * Package: com.learn.mall.search.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class MallElasticSearchConfig {

    /**
     * 将elasticsearch的主机地址配置抽取到开发和生产两个配置文件中
     */
    @Value("${elasticsearch.host}")
    private String Host;

    @Value("${elasticsearch.scheme}")
    private String SCHEME;

    @Value("${elasticsearch.port}")
    private Integer PORT;

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient EsRestClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(Host, PORT, SCHEME)));
        return client;
    }
}
