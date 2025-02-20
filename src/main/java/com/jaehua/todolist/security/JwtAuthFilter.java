package com.jaehua.todolist.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehua.todolist.common.Result;
import com.jaehua.todolist.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 对于不需要认证的路径，直接放行
        if (isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取请求头中的token
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 如果请求头中有token且格式正确
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // 验证token
            if (jwtUtils.validateToken(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                request.setAttribute("username", username);
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 返回统一的未授权响应
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        Result<?> result = Result.error(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token is missing or invalid");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth/") ||
                path.startsWith("/v3/api-docs");
    }
}