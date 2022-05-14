package com.learn.mall.auth.config;


import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;



/**
 * Description:
 * date: 2022/5/8 1:18
 * Package: com.learn.mall.auth.config
 *
 * @author 李佳乐
 * @email 18066550996@163.com
 */
@Configuration
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
@SuppressWarnings("all")
public class SwaggerConfig {

    @Bean(value = "authApi")
    @Order(value = 1)
    public Docket groupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.learn.mall.auth.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo groupApiInfo() {
        return new ApiInfoBuilder()
                .title("电商项目-认证服务")
                .description("<div style='font-size:14px;color:red;'>电商项目-认证服务</div>")
                .termsOfServiceUrl("http://mall.com")
                .contact("李佳乐")
                .version("1.0")
                .build();
    }



}
