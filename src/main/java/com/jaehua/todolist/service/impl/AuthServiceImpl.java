package com.jaehua.todolist.service.impl;

import com.jaehua.todolist.dto.LoginRequest;
import com.jaehua.todolist.dto.RegisterRequest;
import com.jaehua.todolist.entity.User;
import com.jaehua.todolist.mapper.UserMapper;
import com.jaehua.todolist.service.AuthService;
import com.jaehua.todolist.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

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
    public String login(LoginRequest request) {
       User user = userMapper.findByUsername(request.getUsername());
       if(user == null|| !Objects.equals(user.getPassword(),request.getPassword())){
           throw new RuntimeException("用户名或密码错误");
       }
       return jwtUtils.generateToken(user.getUsername());
    }

    @Override
    public void logout() {

    }
}
