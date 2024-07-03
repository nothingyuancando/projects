package com.spring.service;

import com.spring.pojo.Order;
import com.spring.pojo.Plan;
import com.spring.pojo.Platform;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.utils.Result;

/**
* @author Baiyixuan
* @description 针对表【platform】的数据库操作Service
* @createDate 2024-06-25 15:24:22
*/
public interface PlatformService extends IService<Platform> {


    Result platformregister(Platform platform);

    Result platformlogin(Platform platform);

    Result getFactories(String token);

    Result getOverview(String token);

    Result updateOrders(String token, Order order);

    Result addPlan(String token, Plan plan);

    Result updatePlan(String token, Plan plan);

    Result getPlatformInfo(String token);

    Result getOrder(String token);
}
