package com.spring.service;

import com.spring.pojo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.request.PlanAcceptAndRejectRequest;
import com.spring.utils.Result;

/**
* @author Baiyixuan
* @description 针对表【factory】的数据库操作Service
* @createDate 2024-06-25 15:24:22
*/
public interface FactoryService extends IService<Factory> {


    Result factoryregister(Factory factory);

    Result factorylogin(Factory factory);

    Result acceptPlan(String token, PlanAcceptAndRejectRequest plan);

    Result rejectPlan(String token, PlanAcceptAndRejectRequest plan);

    Result creataWorkorder(String token, Workorder workorder);

    Result produceWorkorder(String token, Workorder workorder);

    Result doneWorkorder(String token, Workorder workorder);

    Result addEquipment(String token, Equipment equipment);

    Result deleteEquipment(String token, Equipment equipment);

    Result updateEquipment(String token, Equipment equipment);

    Result getEquipment(String token);

    Result getWorkorder(String token);

    Result addProduct(String token, Product product);

    Result getProduct(String token);

    Result updateProduct(String token, Product product);

    Result deleteProduct(String token, Product product);

    Result addRelation(String token, Producerelation producerelation);

    Result addWorkreport(String token, Workreport workreport);

    Result getOrder(String token);

    Result getView(String token);

    Result getFactoryInfo(String token);

    Result getPlan(String token);
}
