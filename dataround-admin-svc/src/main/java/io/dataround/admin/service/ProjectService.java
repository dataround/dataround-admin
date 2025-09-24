/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataround.admin.entity.Project;
import io.dataround.admin.entity.ProjectUser;

import java.util.List;

/**
 * Project service
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public interface ProjectService extends IService<Project> {

    boolean saveOrUpdate(Project project, List<ProjectUser> members);

    List<Project> myProject(Long userId);

    boolean updateSelected(Long userId, Long projectId);
}
