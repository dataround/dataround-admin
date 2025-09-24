/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dataround.admin.common.UserInfo;
import io.dataround.admin.entity.User;
import io.dataround.admin.mapper.UserMapper;
import io.dataround.admin.service.UserService;
import io.dataround.admin.utils.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User service impl
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfo login(String name, String passwd) {
        // If the user name and password is not correctly, return null
        return userMapper.login(name, SHA256Util.getSHA256(passwd));
    }

    @Override
    public Map<Long, String> listNameByIds(Set<Long> userIds) {
        if (userIds.isEmpty()) {
            return new HashMap<>();
        }
        List<User> users = this.listByIds(userIds);
        return users.stream().collect(Collectors.toMap(User::getId, User::getName));
    }
}
