/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dataround.admin.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * User-Role mapper
 * 
 * @author yuehan124@gmail.com
 * @since 2026/02/19
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("SELECT role_id FROM user_role WHERE user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    @Select("SELECT r.* FROM role r INNER JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    List<io.dataround.admin.entity.Role> selectRolesByUserId(@Param("userId") Long userId);
}
