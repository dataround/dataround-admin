/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataround.admin.entity.UserRole;

import java.util.List;

/**
 * User-Role service
 * 
 * @author yuehan124@gmail.com
 * @since 2026/02/19
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * Get role IDs by user ID
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * Assign roles to user
     */
    void assignRolesToUser(Long userId, List<Long> roleIds);

    /**
     * Remove user's all roles
     */
    void removeUserRoles(Long userId);
}
