package com.jaehua.todolist.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskServiceTest {
    @Autowired
    private TaskService taskService;
    
    @Test
    void createTask_ShouldCreateTaskSuccessfully() {
        // 测试代码
    }
} 