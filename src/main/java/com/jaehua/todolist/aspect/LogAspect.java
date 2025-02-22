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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Aspect 注解，表明该类包含可以应用于指定连接点的通知
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    
    // 只拦截我们自己的 controller 包
    @Around("execution(* com.jaehua.todolist.controller..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 只在非认证接口获取用户ID
        Long userId = null;
        if (!className.equals("AuthController")) {
            try {
                userId = SecurityUtils.getCurrentUserId();
            } catch (Exception e) {
                // 忽略获取用户ID时的异常
                log.debug("Failed to get current user id", e);
            }
        }

        log.info("Request => Class: {}, Method: {}, UserId: {}, Args: {}", 
                className, methodName, userId, Arrays.toString(args));

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.info("Response => Class: {}, Method: {}, UserId: {}, Result: {}, Time: {}ms",
                className, methodName, userId, result, (endTime - startTime));

        return result;
    }
}