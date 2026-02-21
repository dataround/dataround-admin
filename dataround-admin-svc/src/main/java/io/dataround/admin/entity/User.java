/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;
    private String name;
    private String email;
    private String cellphone;
    private String passwd;
    // Profile picture URL
    private String avatar;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    // Department name
    private String department;
    // Job position
    private String position;
    // Address
    private String address;
    private String wechat;
    // Status: 1-Normal, 2-Disabled, 3-Locked
    private Integer status;
    private String remark;
    private String lastLoginIp;
    private String lastLoginTime;
    private Long creatorId;
    private Long updaterId;
    private Date createTime;
    private Date updateTime;
}
