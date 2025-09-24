/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */
package io.dataround.admin.gateway;

import io.dataround.admin.common.CookieUtils;
import io.dataround.admin.common.JwtUtil;
import io.dataround.admin.common.UserInfo;
import io.dataround.admin.common.utils.RequestUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gateway authentication filter
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Autowired
    private GatewayConfig config;
    private static final List<String> STATIC_RESOURCES = Arrays.asList("js", "css", "html", "ico", "jpg", "png", ".ttf", ".xml", ".pdf", ".txt", ".xls", ".doc", ".xlsx", ".docx");
    private final List<String> ESCAPE_RESOURCES = new ArrayList<>(2);

    public AuthenticationFilter() {
    }

    @PostConstruct
    public void init() {
        String[] uris = config.getEscapeUris().split(",");
        for (String uri : uris) {
            ESCAPE_RESOURCES.add(uri.trim());
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getRequest().getURI();
        log.info("http request: {}", uri.toString());
        String rawPath = uri.getRawPath();
        if (STATIC_RESOURCES.stream().anyMatch(rawPath::endsWith)) {
            // return static resources
            return chain.filter(exchange);
        }
        // if admin login/logout request, forward to admin
        if (ESCAPE_RESOURCES.contains(uri.getRawPath())) {
            return chain.filter(exchange);
        }
        // the following requests need login
        HttpCookie cookie = getAuthCookie(exchange.getRequest());
        if (cookie == null) {
            return redirectToLogin(exchange);
        }
        try {
            String remoteIp = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
            if (remoteIp == null) {
                InetSocketAddress address = exchange.getRequest().getRemoteAddress();
                remoteIp = address == null ? "" : address.getHostString();
            }
            UserInfo userInfo = JwtUtil.verifyToken(cookie.getValue(), remoteIp);
            // login success and then continue
            // uid header used to create account automatically by dolphinscheduler/sqlQuery
            // Modify the request headers before forwarding the request
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("uid", String.valueOf(userInfo.getUserId()))
                    .header("un", userInfo.getUserName())
                    .header("pid", String.valueOf(userInfo.getProjectId()))
                    .header("pn", userInfo.getProjectName())
                    .build();
            // Continue with the modified request
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } catch (Exception e) {
            log.error("verify token error", e);
            return redirectToLogin(exchange);
        }
    }

    private Mono<Void> redirectToLogin(ServerWebExchange exchange) {
        if (RequestUtils.isAjaxRequest(exchange.getRequest())) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        } else {
            URI uri = exchange.getRequest().getURI();
            String path = uri.getScheme() + "://" + config.getAdminDomain() + config.getLoginPath();
            exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
            //set header
            exchange.getResponse().getHeaders().add("location", path);
        }
        // IMPORTANT!!! setComplete()
        return exchange.getResponse().setComplete();
    }

    private HttpCookie getAuthCookie(ServerHttpRequest request) {
        return request.getCookies().getFirst(CookieUtils.COOKIE_KEY_UID);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
