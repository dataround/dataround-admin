/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dataround.admin.entity.Resource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Resource mapper
 * 
 * @author yuehan124@gmail.com
 * @since 2025/02/19
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    @Select("""
        SELECT DISTINCT r.* FROM resource r
        INNER JOIN role_resource rr ON r.id = rr.resource_id
        INNER JOIN user_role ur ON rr.role_id = ur.role_id
        WHERE ur.user_id = #{userId}
        ORDER BY r.id
        """)
    List<Resource> selectResourcesByUserId(@Param("userId") Long userId);

    @Select("""
        SELECT DISTINCT r.* FROM resource r
        INNER JOIN role_resource rr ON r.id = rr.resource_id
        WHERE rr.role_id = #{roleId}
        ORDER BY r.id
        """)
    List<Resource> selectResourcesByRoleId(@Param("roleId") Long roleId);
}
