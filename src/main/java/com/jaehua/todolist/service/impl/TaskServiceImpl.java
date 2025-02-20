package com.jaehua.todolist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaehua.todolist.dto.TaskRequest;
import com.jaehua.todolist.entity.Task;
import com.jaehua.todolist.exception.BusinessException;
import com.jaehua.todolist.mapper.TaskMapper;
import com.jaehua.todolist.service.TaskService;
import com.jaehua.todolist.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jaehua.todolist.exception.ErrorCode;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;


    @Override
    public Task createTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(0);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUserId(SecurityUtils.getCurrentUserId());
        taskMapper.insert(task);
        return null;
    }

    @Override
    public Task updateTask(Long id, TaskRequest taskRequest) {
        Task task = getTask(id);
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        return task;
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTask(id);
        taskMapper.deleteById(task);
    }

    @Override
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
    public void updateStatus(Long id) {
        Task task = getTask(id);
        Integer status = task.getStatus()==0?1:0;
        task.setStatus(status);
        taskMapper.updateById(task);
    }
}
