/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataround.admin.entity.Role;

import java.util.List;

/**
 * Role service
 * 
 * @author yuehan124@gmail.com
 * @since 2026/09/21
 */
public interface RoleService extends IService<Role> {

    /**
     * Get roles by user ID
     */
    List<Role> getRolesByUserId(Long userId);
}
