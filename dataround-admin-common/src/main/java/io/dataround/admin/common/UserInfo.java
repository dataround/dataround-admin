/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */
package io.dataround.admin.common;

import lombok.Data;

/**
 * UserInfo
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Data
public class UserInfo {
    // user id
    private Long userId;
    // user name
    private String userName;
    // current project id
    private Long projectId;
    // current project name
    private String projectName;
    // user ip
    private String userIp;
    // expire time
    private long expiration;
}
