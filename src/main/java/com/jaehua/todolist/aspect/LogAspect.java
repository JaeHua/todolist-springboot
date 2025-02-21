package com.jaehua.todolist.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehua.todolist.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Aspect 注解，表明该类包含可以应用于指定连接点的通知
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogAspect {
    private final ObjectMapper objectMapper;

    @Around("execution(* com.jaehua.todolist.service..*.*(..)) || execution(* com.jaehua.todolist.controller..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        // 构建日志上下文
        Map<String, Object> logContext = new HashMap<>();
        logContext.put("class", className);
        logContext.put("method", methodName);
        logContext.put("userId", SecurityUtils.getCurrentUserId());
        
        // 获取请求信息（如果是Controller）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logContext.put("url", request.getRequestURL().toString());
            logContext.put("httpMethod", request.getMethod());
            logContext.put("ip", request.getRemoteAddr());
        }

        // 记录入参
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            params.put(paramNames[i], args[i]);
        }
        logContext.put("params", params);

        // 方法执行前日志
        log.info("Request start - {}", objectMapper.writeValueAsString(logContext));

        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            
            // 记录执行时间
            long duration = System.currentTimeMillis() - startTime;
            logContext.put("duration", duration);
            logContext.put("status", "success");
            if (result != null) {
                logContext.put("result", result);
            }
            
            // 方法执行成功日志
            log.info("Request end - {}", objectMapper.writeValueAsString(logContext));
            return result;
        } catch (Throwable e) {
            // 记录异常信息
            long duration = System.currentTimeMillis() - startTime;
            logContext.put("duration", duration);
            logContext.put("status", "error");
            logContext.put("errorMessage", e.getMessage());
            logContext.put("errorType", e.getClass().getName());
            
            // 方法执行异常日志
            log.error("Request error - {}", objectMapper.writeValueAsString(logContext));
            throw e;
        }
    }
}