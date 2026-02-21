/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataround.admin.entity.Resource;

import java.util.List;

/**
 * Resource service
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public interface ResourceService extends IService<Resource> {

    /**
     * Get resources by user ID (through user's roles)
     */
    List<Resource> getResourcesByUserId(Long userId);

    /**
     * Get resources by role ID
     */
    List<Resource> getResourcesByRoleId(Long roleId);
}
