/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.dataround.admin.common.Result;
import io.dataround.admin.common.UserInfo;
import io.dataround.admin.common.controller.BaseController;
import io.dataround.admin.entity.User;
import io.dataround.admin.service.UserService;
import io.dataround.admin.utils.SHA256Util;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
    // used for web page
    private static final String RESERVED_PASSWD = "******";

    @GetMapping("/list")
    public Result<Page<User>> list(Page<User> params) {
        params.addOrder(new OrderItem("id", true));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> page = userService.page(params, queryWrapper);
        page.getRecords().forEach(u -> u.setPasswd(RESERVED_PASSWD));
        return Result.success(page);
    }

    @GetMapping("/info")
    public Result<User> getById(HttpServletRequest request) {
        UserInfo currentUser = getCurrentUser(request);
        User user = userService.getById(currentUser.getUserId());
        return Result.success(user);
    }

    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody User user, HttpServletRequest request) {
        UserInfo currentUser = getCurrentUser(request);
        Date now = new Date();
        // when disable user, passwd is null
        String passwd = user.getPasswd();
        boolean isUpdate = user.getId() != null;
        if (!isUpdate) {
            user.setDisabled(Boolean.FALSE);
            user.setCreatorId(currentUser.getUserId());
            user.setCreateTime(now);
            user.setPasswd(SHA256Util.getSHA256(passwd));
        } else {
            // if passwd equals RESERVED_PASSWD then not update
            if (RESERVED_PASSWD.equals(passwd)) {
                user.setPasswd(null);
            } else if (passwd != null) {
                user.setPasswd(SHA256Util.getSHA256(passwd));
            }
        }
        user.setUpdateTime(now);
        boolean bool = userService.saveOrUpdate(user);
        return Result.success(bool);
    } 
}
