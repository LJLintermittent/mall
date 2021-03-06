package com.learn.mall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * date: 2021/5/7 14:01
 * Package: com.learn.mall.product.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@ConfigurationProperties(prefix = "mall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {

    /**
     * 核心线程数
     */
    private Integer coreSize;

    /**
     * 最大线程数
     */
    private Integer maxSize;

    /**
     * 线程存活时间
     */
    private Integer keepAliveTime;

}
