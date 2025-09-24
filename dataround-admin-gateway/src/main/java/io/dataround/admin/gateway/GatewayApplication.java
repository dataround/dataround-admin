/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */
package io.dataround.admin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Gateway application
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@SpringBootApplication
public class GatewayApplication {
    public static void main( String[] args ) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
