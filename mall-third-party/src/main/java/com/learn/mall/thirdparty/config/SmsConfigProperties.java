package com.learn.mall.thirdparty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * date: 2021/5/7 18:30
 * Package: com.learn.mall.thirdparty.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@ConfigurationProperties(prefix = "spring.cloud.alicloud.mysms")
@Data
@Component
@SuppressWarnings("all")
public class SmsConfigProperties {

    private String regionId;

    private String accessKeyId;

    private String secret;

    private String signName;

    private String TemplateCode;

}
