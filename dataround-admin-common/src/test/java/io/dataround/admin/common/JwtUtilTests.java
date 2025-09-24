/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */
package io.dataround.admin.common;

import org.junit.Test;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtUtilTests
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Slf4j
public class JwtUtilTests {
    @Test
    public void testCookie() {
        String serverName = "abc.def.dataround.io";
        String[] array = serverName.split("\\.");
        if (array.length > 2) {
            serverName = array[array.length - 2] + "." + array[array.length - 1];
        }
        assert serverName.equals("dataround.io");
    }

    @Test
    public void testGetToken() {
        UserInfo userInfo = buildUserInfo();
        String token = JwtUtil.genToken(userInfo);
        log.info("token: {}", token);
    }

    @Test
    public void testVerifyToken() {
        UserInfo userInfo = buildUserInfo();
        String token = JwtUtil.genToken(userInfo);
        try {
            UserInfo user = JwtUtil.verifyToken(token, "192.168.1.1");
            assert user.getUserId().equals(userInfo.getUserId());
            assert user.getUserName().equals(userInfo.getUserName());
            assert user.getUserIp().equals(userInfo.getUserIp());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserInfo buildUserInfo() {
        Long userId = 1L;
        String userName = "testuser";
        String userIP = "192.168.1.1";
        long expire = System.currentTimeMillis() + CookieUtils.EXPIRATION_TIME * 1000;
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUserName(userName);
        userInfo.setUserIp(userIP);
        userInfo.setExpiration(expire);
        return userInfo;
    }
}
