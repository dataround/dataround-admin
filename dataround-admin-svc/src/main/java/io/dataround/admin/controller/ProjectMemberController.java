/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.controller;

import io.dataround.admin.common.Result;
import io.dataround.admin.common.controller.BaseController;
import io.dataround.admin.entity.ProjectUser;
import io.dataround.admin.service.ProjectUserSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project member controller
 * 
 * @author yuehan124@gmail.com
 * @since 2025/02/18
 */
@RestController
@RequestMapping("/api/project/member")
@Slf4j
public class ProjectMemberController extends BaseController {

    @Autowired
    private ProjectUserSerivce projectUserService;

    @GetMapping("/list")
    public Result<List<ProjectUser>> getMembers(@RequestParam(required = false) Long projectId) {
        // If projectId is not provided, use current project
        if (projectId == null) {
            projectId = getCurrentProjectId();
        }
        if (projectId == null) {
            return Result.success(new ArrayList<>());
        }
        List<ProjectUser> projectUsers = projectUserService.listByProjectIds(List.of(projectId));
        // Sort: admins first, then normal members
        List<ProjectUser> sortedUsers = projectUsers.stream()
                .sorted((a, b) -> {
                    if (a.getIsAdmin() && !b.getIsAdmin()) return -1;
                    if (!a.getIsAdmin() && b.getIsAdmin()) return 1;
                    return 0;
                })
                .collect(Collectors.toList());
        return Result.success(sortedUsers);
    }

    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody ProjectUser member) {
        Assert.notNull(member.getProjectId(), "project id should not be null");
        Assert.notNull(member.getUserId(), "user id should not be null");
        if (member.getIsAdmin() == null) {
            member.setIsAdmin(false);
        }
        boolean result = projectUserService.saveOrUpdate(member);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        Assert.notNull(id, "member id should not be null");
        boolean result = projectUserService.removeById(id);
        return Result.success(result);
    }

}
