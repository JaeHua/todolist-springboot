package com.jaehua.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jaehua.todolist.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    @Select("select * from task where user_id = #{userId} and status = #{status}")
    IPage <Task> selectByUserIdAndStatus(IPage<Task> page, @Param("userId") Long userId, @Param("status") Integer status);
}
