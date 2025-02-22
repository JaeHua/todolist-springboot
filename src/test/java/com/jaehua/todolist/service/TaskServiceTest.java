package com.jaehua.todolist.service;

import com.jaehua.todolist.dto.TaskRequest;
import com.jaehua.todolist.entity.Task;
import com.jaehua.todolist.entity.User;
import com.jaehua.todolist.exception.BusinessException;
import com.jaehua.todolist.mapper.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
    "spring.cache.type=none"  // 禁用缓存
})
@ActiveProfiles("test")  // 使用测试配置文件
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    /// 模拟的对象，用于测试期间替代实际的数据库操作
    @MockBean
    private TaskMapper taskMapper;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        // 设置测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        // 设置认证上下文
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(testUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 准备测试任务
        testTask = new Task();
        testTask.setId(1L);
        testTask.setUserId(testUser.getId());
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setStatus(0);
        testTask.setCreatedAt(LocalDateTime.now());
        testTask.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createTask_Success() {
        // 准备测试数据
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");

        when(taskMapper.insert(any(Task.class))).thenReturn(1);

        // 执行测试
        Task result = taskService.createTask(request);

        // 验证结果
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
    }

    @Test
    void updateTask_Success() {
        // 准备测试数据
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Task");
        request.setDescription("Updated Description");

        when(taskMapper.selectById(1L)).thenReturn(testTask);
        when(taskMapper.updateById(any(Task.class))).thenReturn(1);

        // 执行测试
        Task updatedTask = taskService.updateTask(1L, request);

        // 验证结果
        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        ///  verify 方法确认 taskMapper 的 updateById 方法确实被调用，确保更新操作得以执行
        verify(taskMapper).updateById(any(Task.class));
    }

    @Test
    void updateTask_NotFound() {
        // 准备测试数据
        when(taskMapper.selectById(1L)).thenReturn(null);
        
        // 准备请求参数
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");

        // 验证抛出异常
        assertThrows(
            BusinessException.class,
            () -> taskService.updateTask(1L, request)
        );
    }

    @Test
    void deleteTask_Success() {
        // 准备测试数据
        when(taskMapper.selectById(1L)).thenReturn(testTask);
        when(taskMapper.deleteById(any())).thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() -> taskService.deleteTask(1L));

        // 验证删除操作被执行
        verify(taskMapper).deleteById(any());
    }

    @Test
    void deleteTask_NotFound() {
        // 准备测试数据
        when(taskMapper.selectById(1L)).thenReturn(null);

        // 验证抛出异常
        assertThrows(
            BusinessException.class,
            () -> taskService.deleteTask(1L)
        );
        
        // 验证只调用了查询，没有调用删除
        verify(taskMapper, never()).deleteById(any());
    }

    @Test
    void getTask_Success() {
        when(taskMapper.selectById(1L)).thenReturn(testTask);

        // 执行测试
        Task result = taskService.getTask(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testTask.getId(), result.getId());
        assertEquals(testTask.getTitle(), result.getTitle());
    }

    @Test
    void updateStatus_Success() {
        when(taskMapper.selectById(1L)).thenReturn(testTask);
        when(taskMapper.updateById(any(Task.class))).thenReturn(1);

        // 执行测试
        taskService.updateStatus(1L);

        // 验证状态更新
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskMapper).updateById(taskCaptor.capture());
        Task updatedTask = taskCaptor.getValue();
        assertEquals(1, updatedTask.getStatus());
    }
} 