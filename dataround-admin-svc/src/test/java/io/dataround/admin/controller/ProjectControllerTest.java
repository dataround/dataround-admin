/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dataround.admin.entity.Project;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Project controller test
 *
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Slf4j
public class ProjectControllerTest extends BaseControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void list() throws Exception {
        Page<Project> page = new Page<>(1, 10);
        page.setRecords(Collections.singletonList(new Project()));
        mockMvc.perform(get("/api/project/list")
                        .cookie(genCookie(userId))
                        .param("current", String.valueOf(page.getCurrent()))
                        .param("size", String.valueOf(page.getSize())))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    log.info(result.getResponse().getContentAsString());
                    Assertions.assertTrue(result.getResponse().getContentAsString().contains("total"));
                });
    }

    @Test
    public void saveOrUpdate() throws Exception {
        Project project = new Project();
        project.setName("test");
        project.setDescription("test");
        Date now = new Date();
        project.setCreateTime(now);
        project.setCreatorId(userId);
        mockMvc.perform(post("/api/project/saveOrUpdate")
                        .cookie(genCookie(userId))
                        .content(asJsonString(project))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
