/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataround.admin.entity.RoleResource;

import java.util.List;

/**
 * Role-Resource service
 * 
 * @author yuehan124@gmail.com
 * @since 2026/02/19
 */
public interface RoleResourceService extends IService<RoleResource> {

    /**
     * Get resource IDs by role ID
     */
    List<Long> getResourceIdsByRoleId(Long roleId);

    /**
     * Assign resources to role
     */
    void assignResourcesToRole(Long roleId, List<Long> resourceIds);

    /**
     * Remove role's all resources
     */
    void removeRoleResources(Long roleId);
}
