/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dataround.admin.common.Result;
import io.dataround.admin.common.controller.BaseController;
import io.dataround.admin.entity.Role;
import io.dataround.admin.entity.User;
import io.dataround.admin.service.RoleService;
import io.dataround.admin.service.UserRoleService;
import io.dataround.admin.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User-Role controller
 * 
 * @author yuehan124@gmail.com
 * @since 2025/02/19
 */
@RestController
@RequestMapping("/api/userRole")
@Tag(name = "userRole", description = "user role management")
@Slf4j
public class UserRoleController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/list")
    public Result<Page<UserRoleVO>> list(Page<User> params) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getId);
        Page<User> page = userService.page(params, wrapper);
        
        // Get role info for each user
        List<Long> userIds = page.getRecords().stream().map(User::getId).collect(Collectors.toList());
        Map<Long, List<Role>> userRoleMap = new HashMap<>();
        for (Long userId : userIds) {
            userRoleMap.put(userId, roleService.getRolesByUserId(userId));
        }
        
        // Convert to VO
        Page<UserRoleVO> voPage = new Page<>();
        voPage.setSize(page.getSize());
        voPage.setTotal(page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(user -> {
            UserRoleVO vo = new UserRoleVO();
            vo.setUserId(user.getId());
            vo.setUserName(user.getName());
            vo.setEmail(user.getEmail());
            vo.setRoles(userRoleMap.get(user.getId()));
            return vo;
        }).collect(Collectors.toList()));
        
        return Result.success(voPage);
    }

    @GetMapping("/{userId}/roles")
    public Result<List<Long>> getUserRoleIds(@PathVariable Long userId) {
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        return Result.success(roleIds);
    }

    @PostMapping("/{userId}/roles")
    public Result<Boolean> assignRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        userRoleService.assignRolesToUser(userId, roleIds);
        return Result.success(true);
    }

    /**
     * User Role VO
     */
    @lombok.Data
    public static class UserRoleVO {
        private Long userId;
        private String userName;
        private String email;
        private List<Role> roles;
    }
}
