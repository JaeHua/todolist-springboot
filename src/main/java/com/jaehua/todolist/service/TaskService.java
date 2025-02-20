package com.jaehua.todolist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jaehua.todolist.dto.TaskRequest;
import com.jaehua.todolist.entity.Task;

public interface TaskService {
    Task createTask(TaskRequest taskRequest);
    Task updateTask(Long id,TaskRequest taskRequest);
    void deleteTask(Long id);
    Task getTask(Long id);
    IPage<Task> getTaskList(Integer status,Integer pageNum,Integer pageSize);
    void updateStatus(Long id);
}
