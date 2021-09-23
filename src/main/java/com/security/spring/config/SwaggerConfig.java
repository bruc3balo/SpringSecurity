package com.security.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.security.spring")).paths(PathSelectors.any()).build().apiInfo(this.apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Spring Security Title").description("Spring security API Application.")
                .termsOfServiceUrl("terms url ").contact("Spring Security Team")
                .license("Apache License Version 1.0").licenseUrl("license url ").version("version here").build();
    }
}

