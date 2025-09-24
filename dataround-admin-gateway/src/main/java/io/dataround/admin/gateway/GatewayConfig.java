/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.gateway;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway configuration
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Data
@Configuration
public class GatewayConfig {

    @Value("${dataround.admin.domain}")
    private String adminDomain;

    @Value("${dataround.admin.loginPath}")
    private String loginPath;

    @Value("${dataround.admin.escapeUris}")
    private String escapeUris;

}
