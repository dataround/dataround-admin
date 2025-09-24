/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataround.admin.common.UserInfo;
import io.dataround.admin.entity.User;

import java.util.Map;
import java.util.Set;

/**
 * User service
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public interface UserService extends IService<User> {

    public UserInfo login(String name, String passwd);

    Map<Long, String> listNameByIds(Set<Long> userIds);
}
