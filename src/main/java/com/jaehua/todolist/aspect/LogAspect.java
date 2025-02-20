package com.jaehua.todolist.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect 注解，表明该类包含可以应用于指定连接点的通知
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.jaehua.todolist.controller.v1..*.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        try {
            log.info("Start: {}.{}, args: {}", className, methodName, joinPoint.getArgs());
            Object result = joinPoint.proceed();
            log.info("End: {}.{}, time taken: {}ms, result: {}", 
                    className, methodName, System.currentTimeMillis() - start, result);
            return result;
        } catch (Throwable e) {
            log.error("Exception: {}.{}, time taken: {}ms, error: {}", 
                    className, methodName, System.currentTimeMillis() - start, e.getMessage());
            throw e;
        }
    }
}