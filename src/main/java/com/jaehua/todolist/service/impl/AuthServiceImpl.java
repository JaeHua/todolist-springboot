package com.jaehua.todolist.service.impl;

import com.jaehua.todolist.dto.RegisterRequest;
import com.jaehua.todolist.entity.User;
import com.jaehua.todolist.mapper.UserMapper;
import com.jaehua.todolist.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;

    @Override
    public void register(RegisterRequest request) {


        // 检查用户名是否已存在
        if(userMapper.findByUsername(request.getUsername())!=null){
            throw new RuntimeException("用户名已存在");
        }
        // 检查邮箱是否已存在
        if(userMapper.findByEmail(request.getEmail())!=null){
            throw new RuntimeException("邮箱已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        userMapper.insert(user);
    }

    @Override
    public String login(RegisterRequest request) {
        return "";
    }

    @Override
    public void logout() {

    }
}
