package com.jaehua.todolist.exception;

public enum ErrorCode {
    // 通用错误码
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "无权限操作"),
    VALIDATION_ERROR(400, "参数验证错误"),
    
    // 业务错误码
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    INVALID_CREDENTIALS(1003, "用户名或密码错误"),
    
    // 任务相关错误码
    TASK_NOT_FOUND(2001, "任务不存在"),
    TASK_ACCESS_DENIED(2002, "无权访问该任务");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 