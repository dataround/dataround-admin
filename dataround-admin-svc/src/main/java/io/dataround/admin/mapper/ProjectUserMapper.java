/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dataround.admin.entity.ProjectUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Project user mapper
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public interface ProjectUserMapper extends BaseMapper<ProjectUser> {

    @Select({"<script>select pu.*, u.name as userName from project_user pu, \"user\" u where pu.user_id = u.id and pu.project_id in "
            + "<foreach item='id' collection='projectIds' open='(' separator=',' close=')'>#{id}</foreach></script>"})
    List<ProjectUser> listByProjectIds(@Param("projectIds") List<Long> projectIds);
}