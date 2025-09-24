/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Admin application
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Slf4j
@SpringBootApplication
@MapperScan(basePackages = {"io.dataround.admin.mapper"})
@ComponentScan(basePackages = {"io.dataround.admin"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        log.info("SpringDoc URI: /swagger-ui/index.html");
    }

}