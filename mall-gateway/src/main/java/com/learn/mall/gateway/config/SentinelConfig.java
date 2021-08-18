package com.learn.mall.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson.JSON;
import com.learn.common.exception.StatusCodeEnum;
import com.learn.common.utils.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description:
 * date: 2021/5/25 17:22
 * Package: com.learn.mall.gateway.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class SentinelConfig {

    public SentinelConfig() {
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            //网关限流了请求，就会调用此回调方法
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                R result = R.error(StatusCodeEnum.TOO_MANY_REQUEST.getCode(), StatusCodeEnum.TOO_MANY_REQUEST.getMsg());
                String s = JSON.toJSONString(result);
                Mono<ServerResponse> body = ServerResponse.ok().body(Mono.just(s), String.class);
                return body;
            }
        });
    }

}
