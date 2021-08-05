package com.learn.mall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MallGatewayApplication {

    /**
     * TODO: GateWay笔记：
     * 请求发给API网关，到底要不要路由到某个地方，得有一个条件判断，这个条件判断就叫 断言
     * 比如，请求发给网关，网关可以根据请求头，请求参数的不同来路由到哪个服务，这个判断就叫断言
     * 所有请求如果满足了断言的要求，那么才可以到底URI地址，当然，如果有过滤器
     * 那么还有先经过所有的过滤器处理
     * 断言一般作为路径来源的检测，从而根据路径来源，将请求分发给不同的微服务
     * 过滤器可以做 请求头的添加，路径重写等功能
     * 本项目中主要用过滤器做路径重写，从而将请求转换为满足要求的请求格式
     * 网关路径重写需求实例：
     * renrenfast后台管理系统微服务的登录验证码原生接口格式：http://localhost:8080/renrenfast/captcha.jpg/xxx...
     * 因为我们改了基准请求路径，所以网关收到的请求如下：
     * http://localhost:88/api/captcha.jpg/xxx....
     * 根据断言规则匹配: /api/**  ,匹配成功，按理说应该发送到了 lb://renren-fast 微服务
     * 但是这样子它会先去注册中心找到renren-fast的地址， 假设 http://renrenfast:8090/api/captcha.jpg/xxx....
     * 也就是说，不做路径重写的话相当于我们前端人为地破坏了原生路径，当然就请求不过去
     *
     * 路径重写案例：
     * RewritePath=/api/(?<segment>.*),/renren-fast/$\{segment}
     * 意思是：/api/xxx  ->>>> /renren-fast/xxx
     * http://renrenfast:8090/api/captcha.jpg/xxx....
     * http://renrenfast:8090/renren-fast/captcha.jpg/xxx....
     */
    public static void main(String[] args) {
        SpringApplication.run(MallGatewayApplication.class, args);
    }

}
