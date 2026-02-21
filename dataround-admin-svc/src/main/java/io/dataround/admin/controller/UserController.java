/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.dataround.admin.common.Result;
import io.dataround.admin.common.UserInfo;
import io.dataround.admin.common.controller.BaseController;
import io.dataround.admin.common.enums.UserStatusEnum;
import io.dataround.admin.entity.User;
import io.dataround.admin.entity.req.PasswdUpdateRequest;
import io.dataround.admin.service.UserService;
import io.dataround.admin.utils.MessageUtils;
import io.dataround.admin.utils.SHA256Util;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * User controller
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "user", description = "user info")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<Page<User>> list(Page<User> params, User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        // search conditions
        if (user.getName() != null && !user.getName().isEmpty()) {
            queryWrapper.like(User::getName, user.getName());
        }
        if (user.getDepartment() != null && !user.getDepartment().isEmpty()) {
            queryWrapper.like(User::getDepartment, user.getDepartment());
        }
        if (user.getPosition() != null && !user.getPosition().isEmpty()) {
            queryWrapper.like(User::getPosition, user.getPosition());
        }
        queryWrapper.orderByDesc(User::getId);
        Page<User> page = userService.page(params, queryWrapper);
        page.getRecords().forEach(u -> {
            u.setPasswd(null);
        });
        return Result.success(page);
    }

    @GetMapping("/info")
    public Result<User> getById() {
        UserInfo currentUser = getCurrentUser();
        User user = userService.getById(currentUser.getUserId());
        return Result.success(user);
    }

    /**
     * Use this method to update user information. 
     * @param user
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody User user) {
        Long currentUserId = getCurrentUserId();
        Date now = new Date();
        if (user.getId() == null) {
            user.setCreatorId(currentUserId);
            user.setCreateTime(now);
        } 
        if (user.getStatus() == null) {
            user.setStatus(UserStatusEnum.NORMAL.getCode());
        }
        if (StringUtils.isNotBlank(user.getPasswd())) {
            user.setPasswd(SHA256Util.getSHA256(user.getPasswd().trim()));
        }
        user.setUpdaterId(currentUserId);
        user.setUpdateTime(now);
        boolean bool = userService.saveOrUpdate(user);
        return Result.success(bool);
    } 

    @PostMapping("/updatePasswd")
    public Result<Boolean> updatePasswd(@RequestBody PasswdUpdateRequest request) {
        Long currentUserId = getCurrentUserId();
        User user = userService.getById(currentUserId);
        // Verify old password
        String oldPasswdHash = SHA256Util.getSHA256(request.getOldPasswd().trim());
        if (!oldPasswdHash.equals(user.getPasswd())) {
            return Result.error(MessageUtils.getMessage("user.old.password.error"));
        }
        // Update to new password
        Date now = new Date();
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, currentUserId)
                     .set(User::getPasswd, SHA256Util.getSHA256(request.getNewPasswd().trim()))
                     .set(User::getUpdaterId, currentUserId)
                     .set(User::getUpdateTime, now);
        boolean bool = userService.update(updateWrapper);
        return Result.success(bool);
    }

}
