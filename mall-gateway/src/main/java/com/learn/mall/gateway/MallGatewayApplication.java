package com.learn.mall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MallGatewayApplication {

    /**
     * GateWay笔记：
     * 请求发给API网关，到底要不要路由到某个地方，得有一个条件判断，这个条件判断就叫 断言
     * 比如，请求发给网关，网关可以根据请求头，请求参数的不同来路由到哪个服务，这个判断就叫断言
     * 所有请求如果满足了断言的要求，那么才可以到底URI地址，当然，如果有过滤器
     * 那么还有先经过所有的过滤器处理
     * 断言一般作为路径来源的检测，从而根据路径来源，将请求分发给不同的微服务
     * 过滤器可以做 请求头的添加，路径重写等功能
     * 本项目中主要用过滤器做路径重写，从而将请求转换为满足要求的请求格式
     */
    public static void main(String[] args) {
        SpringApplication.run(MallGatewayApplication.class, args);
    }

}
