/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataround.admin.entity.ProjectUser;

import java.util.List;

/**
 * Project user serivce
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public interface ProjectUserSerivce extends IService<ProjectUser> {

    List<ProjectUser> listByProjectIds(List<Long> projectIds);
}
