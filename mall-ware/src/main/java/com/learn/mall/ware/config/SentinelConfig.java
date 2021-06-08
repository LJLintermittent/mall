package com.learn.mall.ware.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.learn.common.exception.StatusCodeEnum;
import com.learn.common.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * date: 2021/5/25 14:05
 * Package: com.learn.mall.seckill.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class SentinelConfig {

    public SentinelConfig() {
        WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler() {
            @Override
            public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
                R result = R.error(StatusCodeEnum.TOO_MANY_REQUEST.getCode()
                        , StatusCodeEnum.TOO_MANY_REQUEST.getMsg());
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json");
                httpServletResponse.getWriter().write(JSON.toJSONString(result));
            }
        });
    }
}
