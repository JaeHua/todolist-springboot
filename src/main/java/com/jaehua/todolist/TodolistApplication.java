package com.jaehua.todolist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MapperScan 注解告诉 Spring 框架扫描指定包下的所有接口，并将这些接口注册为 MyBatis 的 Mappe
 */
@SpringBootApplication
@MapperScan("com.jaehua.todolist.mapper")
public class TodolistApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodolistApplication.class, args);
    }

}
