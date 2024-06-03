package com.homework.atm.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.atm.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
