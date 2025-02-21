package com.jaehua.todolist.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@TableName("task")
public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
