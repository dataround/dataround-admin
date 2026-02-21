/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;   
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dataround.admin.common.CookieUtils;   
import io.dataround.admin.common.JwtUtil;
import io.dataround.admin.common.Result;
import io.dataround.admin.common.UserInfo;
import io.dataround.admin.common.controller.BaseController;
import io.dataround.admin.entity.Project;
import io.dataround.admin.entity.ProjectUser;
import io.dataround.admin.entity.vo.ProjectVO;
import io.dataround.admin.service.ProjectService;
import io.dataround.admin.service.ProjectUserSerivce;
import io.dataround.admin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project controller
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@RestController
@RequestMapping("/api/project")
@Slf4j
public class ProjectController extends BaseController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectUserSerivce projectUserService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<Page<ProjectVO>> list(Page<Project> params) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Project::getId);
        Page<Project> page = projectService.page(params, queryWrapper);
        // fill createUser
        Set<Long> creatorIds = page.getRecords().stream().map(Project::getCreatorId).collect(Collectors.toSet());
        Map<Long, String> userMap = userService.listNameByIds(creatorIds);
        page.getRecords().forEach(p -> p.setCreateUser(userMap.get(p.getCreatorId())));
        // project members
        List<Long> projectIds = page.getRecords().stream().map(Project::getId).collect(Collectors.toList());
        List<ProjectUser> projectUsers = projectUserService.listByProjectIds(projectIds);
        List<ProjectVO> vos = new ArrayList<>();
        for (Project project : page.getRecords()) {
            ProjectVO vo = new ProjectVO(project);
            vo.setMembers(projectUsers.stream().filter(pu -> pu.getProjectId().equals(project.getId())).collect(Collectors.toList()));
            vo.setAdmins(projectUsers.stream().filter(pu -> pu.getProjectId().equals(project.getId()) && pu.getIsAdmin()).collect(Collectors.toList()));
            vos.add(vo);
        }
        Page<ProjectVO> pagevo = new Page<>();
        pagevo.setRecords(vos);
        pagevo.setSize(page.getSize());
        pagevo.setTotal(page.getTotal());
        return Result.success(pagevo);
    }

    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody ProjectVO project, HttpServletRequest request) {
        UserInfo currentUser = getCurrentUser();
        Date now = new Date();
        if (project.getId() == null) {
            project.setCreatorId(currentUser.getUserId());
            project.setCreateTime(now);
        }
        Map<Long, ProjectUser> memberMap = new HashMap<>();
        // distinct and for key-val iterator
        project.getMembers().forEach(member -> memberMap.put(member.getUserId(), member));
        // admins must be in members and overwrite isAdmin
        project.getAdmins().forEach(pu -> {
            if (memberMap.containsKey(pu.getUserId())) {
                // keep ProjectUser id exists, avoid insert new record
                memberMap.get(pu.getUserId()).setIsAdmin(true);
            } else {
                memberMap.put(pu.getUserId(), pu);
            }
        });
        List<ProjectUser> members = memberMap.values().stream().toList();
        boolean bool = projectService.saveOrUpdate(project, members);
        return Result.success(bool);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id, HttpServletRequest request) {
        Assert.notNull(id, "project id should not be null");
        // check project used or not
        UserInfo currentUser = getCurrentUser();
        // only the project admin and no other project member, then can delete it
        List<ProjectUser> projectUsers = projectUserService.listByProjectIds(List.of(id));
        if (projectUsers.isEmpty() || projectUsers.size() == 1 && projectUsers.get(0).getIsAdmin() && projectUsers.get(0).getUserId().longValue() == currentUser.getUserId()) {
            // TODO: If the project is current user's selected, change to other project
            log.info("delete project id:{}", id);
            boolean bool = projectService.removeById(id);
            return Result.success(bool);
        } else {
            log.info("delete failed, project {} is used by other member", id);
            return Result.error("Project is used by other member");
        }
    }

    @GetMapping("/mine")
    public Result<List<Project>> myProject(HttpServletRequest request, HttpServletResponse response) {
        UserInfo currentUser = getCurrentUser();
        List<Project> projects = projectService.myProject(currentUser.getUserId());
        return Result.success(projects);
    }

    @PostMapping("/selected/{projectId}")
    public Result<Boolean> selectedChange(@PathVariable Long projectId, HttpServletRequest request, HttpServletResponse response) {
        UserInfo currentUser = getCurrentUser();
        log.info("update selected project to {} by {}", projectId, currentUser.getUserId());
        boolean result = projectService.updateSelected(currentUser.getUserId(), projectId);
        if (result) {
            currentUser.setProjectId(projectId);
            String projectName = projectService.getById(projectId).getName();
            currentUser.setProjectName(projectName);
            CookieUtils.addUidCookie(JwtUtil.genToken(currentUser), request, response);
            // setting current project, used for scheduler iframe request only
            CookieUtils.addProjectCookie(projectId+ "%2C" + projectName, request, response);
        }
        return Result.success(result);
    }

}
