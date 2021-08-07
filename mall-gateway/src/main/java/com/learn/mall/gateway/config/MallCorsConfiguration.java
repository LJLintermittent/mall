package com.learn.mall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Description:
 * date: 2021/4/12 18:18
 * Package: com.learn.mall.gateway.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@Configuration
public class MallCorsConfiguration {

    /**
     * TODO 跨域
     * 跨源资源共享，出于安全性考虑，浏览器限制脚本内发起的跨源HTTP请求。
     * 跨源资源共享标准新增了一组 HTTP 首部字段，允许服务器声明哪些源站通过浏览器有权限访问哪些资源。
     * 重点：规范要求，对那些可能对服务器数据产生副作用的请求，（特别是GET请求以外或者某些搭配MIME类型的POST请求）
     * 对非简单请求，浏览器必须首先使用一个OPTIONS方法发起一个预检请求，从而获知服务端是否允许该跨源请求
     * 服务器允许了之后，才发起实际的HTTP请求。
     * 简单请求：
     * 满足下列所有条件，才视为简单请求：
     * 首先必须是 GET,HEAD,POST三者之一
     * content-type仅限于下列三者之一：
     * 1.text/plain
     * 2.multipart/form-data
     * 3.application/x-www-form-urlencoded
     * <p>
     * 不满足简单请求的都是非简单请求：
     * 非简单请求也叫预检请求：
     * 对于预检请求的使用，是为了避免跨域请求对服务器的数据造成不可估量的影响
     */
    @Bean
    public CorsWebFilter CorsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //指明请求中允许携带的首部字段，这里为所有
        corsConfiguration.addAllowedHeader("*");
        //指明实际请求允许使用的HTTP请求方法
        corsConfiguration.addAllowedMethod("*");
        //允许来自所有域的请求
        corsConfiguration.addAllowedOrigin("*");
        //是否允许携带cookie进行跨域
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);
    }
}
