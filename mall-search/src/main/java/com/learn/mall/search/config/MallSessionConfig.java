package com.learn.mall.search.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Description:
 * date: 2021/5/9 12:38
 * Package: com.learn.mall.search.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class MallSessionConfig {

    @Value("${sessionConfig.domainName}")
    private String DomainName;

    @Value("${sessionConfig.cookieName}")
    private String CookieName;

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName(DomainName);
        cookieSerializer.setCookieName(CookieName);
        return cookieSerializer;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
