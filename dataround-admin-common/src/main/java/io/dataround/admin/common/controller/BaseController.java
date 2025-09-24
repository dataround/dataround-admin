/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.common.controller;

import io.dataround.admin.common.UserInfo;
import io.dataround.admin.common.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * BaseController
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public class BaseController {

    public UserInfo getCurrentUser(HttpServletRequest request) {
        return (UserInfo) request.getAttribute(Constants.CURRENT_USER);
    }

    public UserInfo getCurrentUser() {
        return (UserInfo) getRequest().getAttribute(Constants.CURRENT_USER);
    }

    public Long getCurrentUserId() {
        UserInfo user = getCurrentUser();
        if (user != null) {
            return user.getUserId();
        }
        return null;
    }

    public Long getCurrentProjectId() {
        UserInfo user = getCurrentUser();
        if (user != null) {
            return user.getProjectId();
        }
        return null;
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    public HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getResponse();
        }
        return null;
    }
}
