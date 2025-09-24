/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */
package io.dataround.admin.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Application Initializer
 * Executes initialization tasks after Spring Boot startup is complete
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Component
@Slf4j
public class ApplicationInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("Application initialization started...");

        // Add initialization tasks here
        // For example: loading configurations, initializing caches, warming up data, etc.
        log.info("Application initialization completed");
    }

}