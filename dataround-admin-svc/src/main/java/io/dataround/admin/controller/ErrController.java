/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Error controller
 *
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Slf4j
@Controller
public class ErrController implements ErrorController {
    private static final String PATH = "/error";

    /**
     * HttpServletResponse.SC_NOT_FOUND is not exception, GlobalExceptionHandler will not handle it.
     * When user visit url from browser, React route is not springboot handler, so it will return 404.
     * All 404 request forward to index.html, then react router will handle it.
     * react router maybe match with springboot route, so all backend interface change to start with /api
     *
     * @param request servlet request
     * @return ResponseEntity
     */
    @RequestMapping(value = PATH)
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null && Integer.valueOf(status.toString()).equals(HttpServletResponse.SC_NOT_FOUND)) {
            // forward to index.html
            try {
                Resource resource = new ClassPathResource("/static/index.html");
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(bytes);
            } catch (IOException e) {
                log.error("IOException", e);
            }
        }
        // forward to error.html
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
