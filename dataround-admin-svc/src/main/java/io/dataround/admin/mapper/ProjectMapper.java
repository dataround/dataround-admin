/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dataround.admin.entity.Project;
import io.dataround.admin.entity.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Project mapper
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public interface ProjectMapper extends BaseMapper<Project> {

    @Select("select a.*, b.is_admin, b.selected from project a, project_user b where a.id=b.project_id and b.user_id = #{userId}")
    List<Project> myProject(Long userId);

    @Update("update project_user set selected = true where project_id = #{projectId} and user_id = #{userId}")
    int updateSelected(Long userId, Long projectId);

    @Update("update project_user set selected = false where user_id = #{userId}")
    int updateUnSelected(Long userId);

    @Select("select u.* from project_user pu, user u where pu.user_id = u.id and pu.project_id = #{projectId}")
    List<User> getProjectUsers(Long projectId);
}