package com.spring.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.mapper.*;
import com.spring.pojo.*;
import com.spring.service.PlatformService;
import com.spring.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author Baiyixuan
* @description 针对表【platform】的数据库操作Service实现
* @createDate 2024-06-25 15:24:22
*/
@Service
public class PlatformServiceImpl extends ServiceImpl<PlatformMapper, Platform>
    implements PlatformService{

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private PlatformMapper platformMapper;
    @Autowired
    private FactoryMapper factoryMapper;
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Result platformregister(Platform platform) {
        /*        注册业务
        1.依然检查账号是否已经被注册
        2.密码加密处理 账号数据保存
        4.返回结果*/

        // 1. 检查平台人员名称是否已经存在
        LambdaQueryWrapper<Platform> platformQueryWrapper = new LambdaQueryWrapper<>();
        platformQueryWrapper.eq(Platform::getPlatformName, platform.getPlatformName());
        Long platformCount = platformMapper.selectCount(platformQueryWrapper);
        if (platformCount > 0) {
            return Result.build(null, ResultCodeEnum.PLATFORM_NME_USED);
        }

        //2.账号数据的密码加密和保存
        platform.setPlatformName(platform.getPlatformName());
        platform.setPassword(MD5Util.encrypt(platform.getPassword()));
        platformMapper.insert(platform);
        return Result.ok(null);
    }

    @Override
    public Result platformlogin(Platform platform) {


/*        登录业务
        1.据账号，查询用户对象-loginUser
        2.四果用户对象为null，查询失败，账号错读!501
        3.对比，医码，失败 返同503的错误
        4.展据用广id生成一个token，token->result 返回*/

        //根据账号查询数据
        LambdaQueryWrapper<Platform> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Platform::getPlatformName, platform.getPlatformName());
        Platform loginuser = platformMapper.selectOne(lambdaQueryWrapper);

        //判断账号
        if (loginuser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //对比密码
        if(!StringUtils.isEmpty(platform.getPassword())
                && MD5Util.encrypt(platform.getPassword()).equals(loginuser.getPassword())){

            //登录成功
            //根据用户id生成token
            String token =jwtHelper.createToken(Long.valueOf(loginuser.getPlatformId()));
            //将token封装到result返回
            LinkedHashMap<String,Object> data = new LinkedHashMap<>();
            data.put("token", token);
            return Result.ok(data);
        }

        //密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    @Override
    public Result getFactories(String token) {
                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        List<Map<String,Object>> factoryListMaps = new ArrayList<>();
        List<Factory> factorylist = factoryMapper.getfactory();
        for(Factory factory:factorylist){
            LinkedHashMap<String,Object> productMap = new LinkedHashMap<>();
            productMap.put("factory_id",factory.getFactoryId());
            productMap.put("factory_name",factory.getFactoryName());
            productMap.put("create_time",factory.getCreateTime());
            factoryListMaps.add(productMap);
        }
        return Result.ok(factoryListMaps);
    }

    @Override
    public Result getOverview(String token) {

                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //失效  未登录看待
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        //工厂运行情况展示设备和产品
        //遍历工厂id，遍历对应工厂id的设备，遍历对应工厂id的产品
        List<Map<String, Object>> equipmentMaps = new ArrayList<>();
        List<Map<String, Object>> productMaps = new ArrayList<>();
        List<Map<String, Object>> factoryDetail = new ArrayList<>();
        List<Integer> factoryids = factoryMapper.getfactoryids();
        for(int factoryid : factoryids){
            List<Equipment> equipments = equipmentMapper.selectEquipment(factoryid);
            for(Equipment equipment : equipments){
                LinkedHashMap<String,Object> equipmentMap = new LinkedHashMap<>();
                equipmentMap.put("equipmentID",equipment.getEquipmentId());
                equipmentMap.put("equipmentName",equipment.getEquipmentName());
                equipmentMap.put("equipmentStates",equipment.getEquipmentStates());
                equipmentMaps.add(equipmentMap);
            }
            List<Product> products = productMapper.selectproduct(factoryid);
            for(Product product : products){
                LinkedHashMap<String,Object> productMap = new LinkedHashMap<>();
                productMap.put("productID",product.getProductId());
                productMap.put("productName",product.getProductName());
                productMap.put("isProducing",product.getIsProducing());
                productMaps.add(productMap);
            }
            LinkedHashMap<String, Object> factoryMap = new LinkedHashMap<>();
            factoryMap.put("factoryID", factoryid);
            factoryMap.put("equipmentDetail", equipmentMaps);
            factoryMap.put("productDetail", productMaps);
            factoryDetail.add(factoryMap);
        }

        //生产计划 展示生产计划表
        List<Map<String,Object>> planMaps = new ArrayList<>();
        for(int factoryid : factoryids){
            List<Plan> plans = planMapper.selectPlans(factoryid);
            for(Plan plan:plans){
                LinkedHashMap<String,Object> producePlan = new LinkedHashMap<>();
                producePlan.put("plan_id",plan.getPlanId());
                producePlan.put("order_id",plan.getOrderId());
                producePlan.put("starttime",plan.getStarttime());
                producePlan.put("endtime",plan.getEndtime());
                producePlan.put("factory_id",factoryid);
                producePlan.put("plan_states",plan.getPlanStates());
                producePlan.put("product_id",plan.getProductId());
                producePlan.put("remark",plan.getRemark());
                planMaps.add(producePlan);
            }
        }

        //订单统计 展示订单表
        List<Map<String,Object>> orderMaps = new ArrayList<>();
        List<Order> orders = orderMapper.selectOrders();
        for(Order order:orders){
            LinkedHashMap<String,Object> orderLinkMap = new LinkedHashMap<>();
            orderLinkMap.put("order_id",order.getOrderId());
            orderLinkMap.put("order_time",order.getOrderTime());
            orderLinkMap.put("order_customerID",order.getOrderCustomerid());
            orderLinkMap.put("orde_states",order.getOrdeStates());
            orderMaps.add(orderLinkMap);
        }
        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        data.put("equipmentStatus", factoryDetail);
        data.put("productionEfficiency", planMaps);
        data.put("orderStatistics", orderMaps);
        return Result.ok(data);
    }

    @Override
    public Result updateOrders(String token, Order order) {
                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //失效  未登录看待
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        orderMapper.updateOrderStates(order.getOrderId(),order.getOrdeStates());
        if(order.getOrdeStates() == 2){
            return Result.build(null, ResultCodeEnum.TAKEN);
        } else if (order.getOrdeStates() == 3) {
            return Result.build(null, ResultCodeEnum.REJECTED);
        } else if (order.getOrdeStates() == 4) {
            return Result.build(null, ResultCodeEnum.COMPLETED);
        }
        return Result.build(null, ResultCodeEnum.NOT_TAKEN);
    }

    @Override
    public Result addPlan(String token, Plan plan) {
                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //失效  未登录看待
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        plan.setOrderId(plan.getOrderId());
        plan.setStarttime(plan.getStarttime());
        plan.setEndtime(plan.getEndtime());
        plan.setFactoryId(plan.getFactoryId());
        plan.setPlanStates(1);
        plan.setRemark(plan.getRemark());
        planMapper.insert(plan);
        return Result.build(null, ResultCodeEnum.PLAN_CREATED);
    }

    @Override
    public Result updatePlan(String token, Plan plan) {
                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //失效  未登录看待
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        planMapper.updatePlanStates(plan.getPlanId(),plan.getPlanStates());
        if(plan.getPlanStates() == 2){
            return Result.build(null, ResultCodeEnum.START_PLAN);
        } else if (plan.getPlanStates() == 3) {
            return Result.build(null, ResultCodeEnum.COMPLETE_PLAN);
        }
        return Result.build(null, ResultCodeEnum.BE_READY);
    }

    @Override
    public Result getPlatformInfo(String token) {
                                                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        //根据token获取客户id
        int platformID = jwtHelper.getUserId(token).intValue();
        Platform platform = platformMapper.selectByplatformId(platformID);
        platform.setPassword("");
        Map data = new HashMap();
        data.put("platform",platform);
        return Result.ok(data);
    }

    @Override
    public Result getOrder(String token) {
                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }

        List<Map<String,Object>> orderListMaps = new ArrayList<>();
        List<Order> orderlist = orderMapper.selectOrders();
        for(Order order:orderlist){
            LinkedHashMap<String,Object> orderMap = new LinkedHashMap<>();
            orderMap.put("order_id",order.getOrderId());
            orderMap.put("order_time",order.getOrderTime());
            orderMap.put("customer_id", order.getOrderCustomerid());
            orderMap.put("order_states",order.getOrdeStates());
            orderListMaps.add(orderMap);
        }
        return Result.ok(orderListMaps);
    }
}