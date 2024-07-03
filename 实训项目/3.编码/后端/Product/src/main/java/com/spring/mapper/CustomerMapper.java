package com.spring.mapper;

import com.spring.pojo.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Baiyixuan
* @description 针对表【customer】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Customer
*/
public interface CustomerMapper extends BaseMapper<Customer> {

    Customer selectByCustomerId(@Param("customerid") int customerID);
}




