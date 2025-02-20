package com.jaehua.todolist.controller;

import com.jaehua.todolist.common.Result;
import com.jaehua.todolist.dto.RegisterRequest;
import com.jaehua.todolist.entity.User;
import com.jaehua.todolist.mapper.UserMapper;
import com.jaehua.todolist.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
       try {
           authService.register(request);
           User user = userMapper.findByUsername(request.getUsername());
           return Result.success(user);
       }catch (Exception e){
           return Result.error(500,e.getMessage());
       }
    }
}
