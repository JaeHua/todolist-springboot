package com.jaehua.todolist.security;

import com.jaehua.todolist.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RestControllerAdvice用于处理全局异常和统一返回结果，
 * 同时适用于使用 @RestController 注解的控制器。它结合了 @ControllerAdvice 和 @ResponseBody 的功能
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //MethodArgumentNotValidException 类型的异常通常在使用 Spring 的数据绑定和验证时发生
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // Iterate over all errors and add them to the map
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Combine all error messages into a single string
        String combinedErrorMessage = String.join("; ", errors.values());

        // Return the combined error message as part of the Result
        return Result.error(HttpStatus.BAD_REQUEST.value(), combinedErrorMessage);
    }
} 