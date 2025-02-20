package com.jaehua.todolist.controller.v1;

import com.jaehua.todolist.common.Result;
import com.jaehua.todolist.dto.LoginRequest;
import com.jaehua.todolist.dto.RegisterRequest;
import com.jaehua.todolist.entity.User;
import com.jaehua.todolist.mapper.UserMapper;
import com.jaehua.todolist.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @Operation(summary = "Register a new user", description = "Register a new user with username, password and email")
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
            authService.register(request);
            User user = userMapper.findByUsername(request.getUsername());
            return Result.success(user);
    }
    @Operation(summary = "Login", description = "Login with username and password")
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginRequest request) {
            String token = authService.login(request);
            return Result.success(token);
    }
}
