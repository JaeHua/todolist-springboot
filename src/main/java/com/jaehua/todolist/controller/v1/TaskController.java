package com.jaehua.todolist.controller.v1;

import com.jaehua.todolist.common.Result;
import com.jaehua.todolist.dto.TaskRequest;
import com.jaehua.todolist.entity.Task;
import com.jaehua.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Task", description = "Task management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/create")
    @Operation(summary = "Create a new task", description = "Create a new task with title, description and status")
    public Result<Task> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return Result.success(taskService.createTask(taskRequest));
    }
    @PostMapping("/update")
    @Operation(summary = "Update a task", description = "Update a task with title, description and status")
    public Result<Task> updateTask(@Param("id")Long id, @Valid @RequestBody  TaskRequest taskRequest) {
        return Result.success(taskService.updateTask(id, taskRequest));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a task", description = "Delete a task by id")
    public Result<Void> deleteTask(@Param("id")Long id) {
        taskService.deleteTask(id);
        return Result.success(null);
    }

    @GetMapping("/get")
    @Operation(summary = "Get a task", description = "Get a task by id")
    public Result<Task> getTask(@Param("id")Long id) {
        return Result.success(taskService.getTask(id));
    }

    @PostMapping("/updateStatus")
    @Operation(summary = "Update a task status", description = "Update a task status by id")
    public Result<Void> updateStatus(@Param("id")Long id) {
        taskService.updateStatus(id);
        return Result.success(null);
    }

    @GetMapping("/list")
    @Operation(summary = "Get a task list", description = "Get a task list by userId and status")
    public Result<IPage<Task>> getTaskList(
            @Nullable @Param("status")Integer status,
            @Param("pageNum")Integer pageNum,
            @Param("pageSize")Integer pageSize) {
        return Result.success(taskService.getTaskList(status, pageNum, pageSize));
    }
}
