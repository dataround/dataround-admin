/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * User entity
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Data
@TableName("public.user")
public class User {

    @TableId(type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String email;
    private String cellphone;
    private String passwd;
    private Boolean disabled;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long creatorId;
    private Date createTime;
    private Date updateTime;
}
