package com.learn.mall.ware.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description:
 * date: 2021/4/14 20:59
 * Package: com.learn.mall.ware.config
 *
 * @author 李佳乐
 * @version 1.0
 */
@MapperScan("com.learn.mall.ware.dao")
@Configuration
@EnableTransactionManagement
public class MyBatisConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置请求的页面大于最大页后的操作，true表示调回到首页，false继续请求 ，默认false
        paginationInterceptor.setOverflow(true);
        //设置最大单页限制数量，默认500条，-1不限制
        paginationInterceptor.setLimit(1000);
        return paginationInterceptor;
    }
}
