//package com.learn.mall.ware.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import io.seata.rm.datasource.DataSourceProxy;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//
//import javax.sql.DataSource;
//
///**
// * Description:
// * date: 2021/5/18 16:08
// * Package: com.learn.mall.ware.config
// *
// * @author 李佳乐
// * @version 1.0
// */
//@SuppressWarnings("all")
//@Configuration
//public class MallSeataConfig {
//
//    @Autowired
//    private DataSourceProperties dataSourceProperties;
//
//    @Bean
//    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
//        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
//                .type(HikariDataSource.class).build();
//        if (StringUtils.hasText(dataSourceProperties.getName())) {
//            dataSource.setPoolName(dataSourceProperties.getName());
//        }
//        return new DataSourceProxy(dataSource);
//    }
//
//}
