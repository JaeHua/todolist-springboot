package com.jaehua.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehua.todolist.controller.v1.TaskController;
import com.jaehua.todolist.dto.TaskRequest;
import com.jaehua.todolist.entity.Task;
import com.jaehua.todolist.mapper.UserMapper;
import com.jaehua.todolist.service.TaskService;
import com.jaehua.todolist.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import(TestConfig.class)
class TaskControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // 配置 userMapper 的行为
        com.jaehua.todolist.entity.User mockUser = new com.jaehua.todolist.entity.User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser1");
        when(userMapper.findByUsername("testUser")).thenReturn(mockUser);
    }

    /// 于在测试环境中模拟已认证的用户
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void createTask_Success() throws Exception {
        // 准备测试数据
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setUserId(1L);

        when(taskService.createTask(any(TaskRequest.class))).thenReturn(task);

        // 执行测试
        mockMvc.perform(post("/api/v1/tasks/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void createTask_Unauthorized() throws Exception {
        // 准备测试数据
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");

        // 执行测试 - 未认证用户应该返回401
        mockMvc.perform(post("/api/v1/tasks/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
} 