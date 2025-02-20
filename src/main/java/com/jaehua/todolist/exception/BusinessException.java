package com.jaehua.todolist.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    // 保留这些构造方法以兼容现有代码，但标记为过时以鼓励使用 ErrorCode
    @Deprecated
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    @Deprecated
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
} 