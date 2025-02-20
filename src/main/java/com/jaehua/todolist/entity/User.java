package com.jaehua.todolist.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * Data表明自动生成getter/setter方法
 */
@Data
@TableName("user")
public class User {
    // 主键自增
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
}
