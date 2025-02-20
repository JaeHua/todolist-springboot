package com.jaehua.todolist.service;

import com.jaehua.todolist.dto.LoginRequest;
import com.jaehua.todolist.dto.RegisterRequest;

/**
 *  认证服务接口
 */
public interface AuthService {
    void register(RegisterRequest request);
    String login(LoginRequest request);
    void logout();
}
