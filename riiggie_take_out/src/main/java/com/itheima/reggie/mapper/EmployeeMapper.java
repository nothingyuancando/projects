package com.itheima.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.itheima.reggie.entity.Employee;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
