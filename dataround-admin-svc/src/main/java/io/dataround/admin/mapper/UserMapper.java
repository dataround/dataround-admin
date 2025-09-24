/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dataround.admin.common.UserInfo;
import io.dataround.admin.entity.User;
import org.apache.ibatis.annotations.Select;

/**
 * User mapper
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("""
        select u.id as userId, u.name as userName, p.id as projectId, p.name as projectName from "user" u 
        left join project_user pu on u.id=pu.user_id 
        left join project p on pu.project_id=p.id
        where pu.selected=true and u.name = #{name} and passwd = #{passwd}
        """)
    UserInfo login(String name, String passwd);
}
