package com.jaehua.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jaehua.todolist.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper注解作用：MyBatis 会在运行时自动生成该接口的实现类，处理与数据库的交互
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username}")
    User findByUsername(String username);
    @Select("select * from user where email = #{email}")
    User findByEmail(String email);
}
