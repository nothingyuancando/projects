package com.spring.service;

import com.spring.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.pojo.Platform;
import com.spring.request.OrderAndDetails;
import com.spring.utils.Result;

/**
* @author Baiyixuan
* @description 针对表【customer】的数据库操作Service
* @createDate 2024-06-25 15:24:22
*/
public interface CustomerService extends IService<Customer> {


    Result customerregister(Customer customer);

    Result customerlogin(Customer customer);

    Result order(String token, OrderAndDetails orderAndDetails);

    Result getorder(String token);

    Result updateOrder(String token, OrderAndDetails orderAndDetails);

    Result deleteOrder(String token, Integer orderID);

    Result viewPlatform();

    Result viewOrderDetail(String token, Integer orderID);

    Result getCustomerInfo(String token);
}
