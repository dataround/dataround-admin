/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dataround.admin.common.CookieUtils;
import io.dataround.admin.common.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base controller test
 *
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest {

    // testuser's id
    protected Long userId = 2L;

    public Cookie genCookie(Long userId) {
        String token = JwtUtil.genToken(null);
        return CookieUtils.addUidCookie( token, null, null);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
