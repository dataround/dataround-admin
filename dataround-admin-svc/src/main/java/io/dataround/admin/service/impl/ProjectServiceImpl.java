/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dataround.admin.entity.Project;
import io.dataround.admin.entity.ProjectUser;
import io.dataround.admin.mapper.ProjectMapper;
import io.dataround.admin.service.ProjectService;
import io.dataround.admin.service.ProjectUserSerivce;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project service impl
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectUserSerivce projectUserSerivce;

    @Override
    public boolean saveOrUpdate(Project project, List<ProjectUser> members) {
        projectUserSerivce.saveOrUpdateBatch(members);
        return super.saveOrUpdate(project);
    }

    @Override
    public List<Project> myProject(Long userId) {
        return projectMapper.myProject(userId);
    }

    @Override
    public boolean updateSelected(Long userId, Long projectId) {
        projectMapper.updateUnSelected(userId);
        int updated = projectMapper.updateSelected(userId, projectId);
        return updated > 0;
    }
}
