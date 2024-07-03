package com.spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.pojo.Order;
import com.spring.service.OrderService;
import com.spring.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author Baiyixuan
* @description 针对表【order】的数据库操作Service实现
* @createDate 2024-06-25 15:24:22
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




