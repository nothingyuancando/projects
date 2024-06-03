package com.homework.atm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.atm.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
