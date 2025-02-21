package com.jaehua.todolist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaehua.todolist.config.CacheConfig;
import com.jaehua.todolist.dto.TaskRequest;
import com.jaehua.todolist.entity.Task;
import com.jaehua.todolist.exception.BusinessException;
import com.jaehua.todolist.mapper.TaskMapper;
import com.jaehua.todolist.service.TaskService;
import com.jaehua.todolist.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.jaehua.todolist.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Caching;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final ApplicationContext applicationContext;

    // 确保#Cacheable注解下，AOP也能生效
    private TaskService self() {
        return applicationContext.getBean(TaskService.class);
    }

    @Override
    @CacheEvict(value = CacheConfig.TASK_LIST_CACHE, allEntries = true)  // 只清除列表缓存
    public Task createTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(0);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUserId(SecurityUtils.getCurrentUserId());
        taskMapper.insert(task);
        return task;  // 返回创建的任务
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = CacheConfig.TASK_CACHE, key = "#id"),  // 清除特定任务缓存
        @CacheEvict(value = CacheConfig.TASK_LIST_CACHE, allEntries = true)  // 清除列表缓存
    })
    public Task updateTask(Long id, TaskRequest taskRequest) {
        Task task = self().getTask(id);
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        return task;
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = CacheConfig.TASK_CACHE, key = "#id"),  // 清除特定任务缓存
        @CacheEvict(value = CacheConfig.TASK_LIST_CACHE, allEntries = true)  // 清除列表缓存
    })
    public void deleteTask(Long id) {
        Task task = self().getTask(id);
        taskMapper.deleteById(task);
    }

    @Override
    @Cacheable(value = CacheConfig.TASK_CACHE, key = "#id", unless = "#result == null")
    public Task getTask(Long id) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        if (!task.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new BusinessException(ErrorCode.TASK_ACCESS_DENIED);
        }
        return task;
    }

    @Override
    @Cacheable(cacheNames = CacheConfig.TASK_LIST_CACHE,
            key = "'user_' + T(com.jaehua.todolist.utils.SecurityUtils).getCurrentUserId() + '_status_' + #status + '_page_' + #pageNum",
            unless = "#result == null")
    public IPage<Task> getTaskList(Integer status, Integer pageNum, Integer pageSize) {
        // Create a Page object for pagination
        Page<Task> page = new Page<>(pageNum, pageSize);

        // Create a query wrapper to filter tasks
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        Long userId = SecurityUtils.getCurrentUserId();
        queryWrapper.eq("user_id", userId);

        if (status != null) {
            queryWrapper.eq("status", status);
        }

        return taskMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = CacheConfig.TASK_CACHE, key = "#id"),  // 清除特定任务缓存
        @CacheEvict(value = CacheConfig.TASK_LIST_CACHE, allEntries = true)  // 清除列表缓存
    })
    public void updateStatus(Long id) {
        Task task = self().getTask(id);
        task.setStatus(task.getStatus() == 0 ? 1 : 0);
        taskMapper.updateById(task);
    }
}
