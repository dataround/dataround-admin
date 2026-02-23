/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */
package io.dataround.admin.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * RequestUtils
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public class RequestUtils {

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    public static String getDomain(HttpServletRequest request) {
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null) {
            host = request.getServerName();
        } else if (host.contains(",")) {
            host = host.substring(0, host.indexOf(","));
        }
        if (host.contains(":")) {
            host = host.substring(0, host.indexOf(":"));
        }
        return host;
    }

    public static String getDomain(ServerHttpRequest request) {
        String host = request.getHeaders().getFirst("X-Forwarded-Host");
        if (host == null) {
            host = request.getURI().getHost();
        } else if (host.contains(",")) {
            host = host.substring(0, host.indexOf(","));
        }
        if (host != null && host.contains(":")) {
            host = host.substring(0, host.indexOf(":"));
        }
        return host;
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        } else if (ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    public static boolean isDefaultPort(HttpServletRequest request) {
        int port = getRequestPort(request);
        String schema = getRequestSchema(request);
        return (schema.equalsIgnoreCase("https") && port == 443)
                || (schema.equalsIgnoreCase("http") && port == 80);
    }

    public static boolean isDefaultPort(ServerHttpRequest request) {
        int port = getRequestPort(request);
        String schema = getRequestSchema(request);
        return (schema.equalsIgnoreCase("https") && port == 443)
                || (schema.equalsIgnoreCase("http") && port == 80);
    }

    public static int getRequestPort(HttpServletRequest request) {
        String port = request.getHeader("X-Forwarded-Port");
        if (port != null) {
            if (port.contains(",")) {
                port = port.substring(0, port.indexOf(","));
            }
            return Integer.parseInt(port);
        }
        return request.getServerPort();
    }

    public static int getRequestPort(ServerHttpRequest request) {
        String port = request.getHeaders().getFirst("X-Forwarded-Port");
        if (port != null) {
            if (port.contains(",")) {
                port = port.substring(0, port.indexOf(","));
            }
            return Integer.parseInt(port);
        }
        int uriPort = request.getURI().getPort();
        return uriPort != -1 ? uriPort : ("https".equalsIgnoreCase(request.getURI().getScheme()) ? 443 : 80);
    }

    // get original request schema
    public static String getRequestSchema(HttpServletRequest request) {
        String schema = request.getHeader("X-Forwarded-Proto");
        if (schema == null) {
            schema = request.getScheme();
        } else if (schema.contains(",")) {
            schema = schema.substring(0, schema.indexOf(","));
        }
        return schema;
    }

    public static String getRequestSchema(ServerHttpRequest request) {
        String schema = request.getHeaders().getFirst("X-Forwarded-Proto");
        if (schema == null) {
            schema = request.getURI().getScheme();
        } else if (schema.contains(",")) {
            schema = schema.substring(0, schema.indexOf(","));
        }
        return schema;
    }

    public static String getLoginUrl(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        String schema = getRequestSchema(request);
        builder.append(schema).append("://").append(getDomain(request));
        if (!isDefaultPort(request)) {
            builder.append(":").append(getRequestPort(request));
        }
        StringBuilder redirectUrl = new StringBuilder(builder).append(request.getRequestURI());
        if (request.getQueryString() != null) {
            redirectUrl.append("?").append(request.getQueryString());
        }
        // redirect parameter must be encoded
        String encodedRedirectUrl = URLEncoder.encode(redirectUrl.toString(), StandardCharsets.UTF_8);
        builder.append(request.getContextPath()).append("/login?redirect=").append(encodedRedirectUrl);
        return builder.toString();
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equalsIgnoreCase(request.getHeader("x-requested-with"));
    }

    /**
     * Build login URL dynamically from ServerHttpRequest
     * @param request ServerHttpRequest
     * @param loginPath login path, e.g. "/admin/login"
     * @return full login URL with redirect parameter
     */
    public static String getLoginUrl(ServerHttpRequest request, String loginPath) {
        StringBuilder builder = new StringBuilder();
        String schema = getRequestSchema(request);
        builder.append(schema).append("://").append(getDomain(request));
        if (!isDefaultPort(request)) {
            builder.append(":").append(getRequestPort(request));
        }
        // Build redirect URL with current request path
        StringBuilder redirectUrl = new StringBuilder(builder).append(request.getURI().getRawPath());
        if (request.getURI().getRawQuery() != null) {
            redirectUrl.append("?").append(request.getURI().getRawQuery());
        }
        String encodedRedirectUrl = URLEncoder.encode(redirectUrl.toString(), StandardCharsets.UTF_8);
        builder.append(loginPath).append("?redirect=").append(encodedRedirectUrl);
        return builder.toString();
    }

    public static boolean isAjaxRequest(ServerHttpRequest request) {
        return XML_HTTP_REQUEST.equalsIgnoreCase(request.getHeaders().getFirst("x-requested-with"));
    }
}
