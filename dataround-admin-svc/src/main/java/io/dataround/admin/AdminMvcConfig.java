/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Admin mvc config
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Configuration
public class AdminMvcConfig {
    @Value("${dataround.login.interceptor.escape}")
    private String loginEscape;
    @Autowired
    private AuthInterceptor authInterceptor;

    @Bean
    public WebMvcConfigurer interceptorConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                String[] escapePatterns = loginEscape.split(",");
                for (int i = 0; i < escapePatterns.length; i++) {
                    escapePatterns[i] = escapePatterns[i].trim();
                }
                registry.addInterceptor(authInterceptor)
                        .addPathPatterns("/**") // intercept all request
                        .excludePathPatterns(escapePatterns); // exclude path that do not require login
            }
        };
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        // Set date-time format for LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }

    @Bean
    public OpenAPI springDocOpenAPI() {
        return new OpenAPI().info(new Info()
                        .title("MyDP manager API")
                        .description("api document")
                        .version("v1.0")
                        .license(new License().name("mydp.io")
                                .url("https://mydp.io")));
    }
}
