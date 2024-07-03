package com.spring.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.mapper.OrderMapper;
import com.spring.mapper.OrderdetialMapper;
import com.spring.mapper.PlatformMapper;
import com.spring.pojo.*;
import com.spring.request.OrderAndDetails;
import com.spring.service.CustomerService;
import com.spring.mapper.CustomerMapper;
import com.spring.service.OrderdetialService;
import com.spring.utils.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author Baiyixuan
* @description 针对表【customer】的数据库操作Service实现
* @createDate 2024-06-25 15:24:22
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService{

    @Autowired
    private OrderdetialMapper orderdetialMapper;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PlatformMapper platformMapper;

    @Override
    public Result customerregister(Customer customer) {
                        /*        注册业务
        1.依然检查账号是否已经被注册
        2.密码加密处理 账号数据保存
        4.返回结果*/

        // 1. 检查客户名称是否已经存在
        LambdaQueryWrapper<Customer> customerQueryWrapper = new LambdaQueryWrapper<>();
        customerQueryWrapper.eq(Customer::getCustomerName, customer.getCustomerName());
        Long factoryCount = customerMapper.selectCount(customerQueryWrapper);
        if (factoryCount > 0) {
            return Result.build(null, ResultCodeEnum.CUSTOMER_NAME_USED);
        }

        //2.账号数据的密码加密和保存
        customer.setCustomerName(customer.getCustomerName());
        customer.setPassword(MD5Util.encrypt(customer.getPassword()));
        customerMapper.insert(customer);
        return Result.ok(null);
    }

    @Override
    public Result customerlogin(Customer customer) {


/*        登录业务
        1.据账号，查询用户对象-loginUser
        2.四果用户对象为null，查询失败，账号错读!501
        3.对比，医码，失败 返同503的错误
        4.展据用广id生成一个token，token->result 返回*/

        //根据账号查询数据
        LambdaQueryWrapper<Customer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Customer::getCustomerName, customer.getCustomerName());
        Customer loginuser = customerMapper.selectOne(lambdaQueryWrapper);

        //判断账号
        if (loginuser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //对比密码
        if(!StringUtils.isEmpty(customer.getPassword())
                && MD5Util.encrypt(customer.getPassword()).equals(loginuser.getPassword())){

            //登录成功
            //根据用户id生成token
            String token =jwtHelper.createToken(Long.valueOf(loginuser.getCustomerId()));
            //将token封装到result返回
            LinkedHashMap<String,Object> data = new LinkedHashMap<>();
            data.put("token", token);
            return Result.ok(data);
        }

        //密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    @Override
    public Result order(String token, OrderAndDetails orderAndDetails) {
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
        Order order = new Order();
        int customerID = jwtHelper.getUserId(token).intValue();
        order.setOrderCustomerid(customerID);
        order.setOrdeStates(orderAndDetails.getOrder_states());
        orderMapper.insertorder(order);

        //复杂查询返回的是最新的订单id
        int orderId = orderMapper.selectorderId(customerID);
        List<Orderdetial> orderDetails = new ArrayList<>();
        //循环OrderAndDetail的orderDetails列表
        // orderAndDetails.getOrderDetails()返回的是包含OrderDetailRequest对象的列表
        //detailRequest是当前循环迭代的OrderDetailRequest的对象
        for (OrderAndDetails.OrderDetailRequest detailRequest : orderAndDetails.getOrder_details()) {
            Orderdetial orderDetail = new Orderdetial();
            orderDetail.setProductId(detailRequest.getProduct_id());
            orderDetail.setAmount(detailRequest.getAmount());
            orderDetail.setOrderId(orderId);
            orderDetails.add(orderDetail);
        }
        for(Orderdetial orderDetail : orderDetails) {
            orderdetialMapper.insertorderdetail(orderDetail);
        }
        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        data.put("order_id", orderId);
        return Result.ok(data);
    }

    @Override
    public Result getorder(String token) {
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
        int customerID = jwtHelper.getUserId(token).intValue();

        List<Map<String,Object>> orderListMaps = new ArrayList<>();
        List<Integer> orderidlist = orderMapper.queryorderId(customerID);
        for(int orderId:orderidlist){
            LinkedHashMap<String,Object> orderMap = new LinkedHashMap<>();
            Date order_time = orderMapper.queryordertime(orderId);
            int order_states = orderMapper.queryorderstates(orderId);
            orderMap.put("order_id", orderId);
            orderMap.put("order_time",order_time);
            orderMap.put("customer_id", customerID);
            orderMap.put("order_states",order_states);
            orderListMaps.add(orderMap);
        }
        return Result.ok(orderListMaps);
    }

    @Override
    public Result updateOrder(String token, OrderAndDetails orderAndDetails) {
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
        int orderId = orderAndDetails.getOrder_id();
        int orderStates = orderAndDetails.getOrder_states();
        orderMapper.updateByOrderId(orderId,orderStates);

        //循环OrderAndDetail的orderDetails列表
        // orderAndDetails.getOrderDetails()返回的是包含OrderDetailRequest对象的列表
        //detailRequest是当前循环迭代的OrderDetailRequest的对象
        for (OrderAndDetails.OrderDetailRequest detailRequest : orderAndDetails.getOrder_details()) {
            Orderdetial orderDetail = new Orderdetial();
            int productID = detailRequest.getProduct_id();
            int amount = detailRequest.getAmount();
            orderdetialMapper.updateByproductID(productID,amount);
        }
        return Result.ok(null);
    }

    @Override
    public Result deleteOrder(String token, Integer orderID) {
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
        orderMapper.deleteByorderId(orderID);
        orderdetialMapper.deleteByorderId(orderID);
        return Result.ok(null);
    }

    @Override
    public Result viewPlatform() {
        List<Map<String,Object>> platForm = new ArrayList<>();
        List<Platform> platforms = platformMapper.selectPlatform();
        for(Platform platform:platforms){
            LinkedHashMap<String,Object> platformMap = new LinkedHashMap<>();
            platformMap.put("platform_id", platform.getPlatformId());
            platformMap.put("platform_name", platform.getPlatformName());
            platformMap.put("create_time", platform.getCreateTime());
            platForm.add(platformMap);
        }
        return Result.ok(platForm);
    }

    @Override
    public Result viewOrderDetail(String token, Integer orderID) {
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
        int customerID = jwtHelper.getUserId(token).intValue();
        Order order = orderMapper.queryOrder(orderID);
        List<Map<String,Object>> orderDetails = new ArrayList<>();
        List<Map<String,Object>> orderDetails1 = new ArrayList<>();
        List<Orderdetial> orderdetialList = orderdetialMapper.queryOrders(orderID);
        for(Orderdetial orderdetial:orderdetialList){
            LinkedHashMap<String,Object> orderDetail = new LinkedHashMap<>();
            orderDetail.put("product_id",orderdetial.getProductId());
            orderDetail.put("amount",orderdetial.getAmount());
            orderDetails1.add(orderDetail);
        }
        LinkedHashMap<String,Object> orderDetailMap = new LinkedHashMap<>();
        orderDetailMap.put("order_id",orderID);
        orderDetailMap.put("create_time",order.getOrderTime());
        orderDetailMap.put("customer_id",customerID);
        orderDetailMap.put("order_states",order.getOrdeStates());
        orderDetailMap.put("order_detail",orderDetails1);
        orderDetails.add(orderDetailMap);
        return Result.ok(orderDetails);
    }

    @Override
    public Result getCustomerInfo(String token) {
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
        int customerID = jwtHelper.getUserId(token).intValue();
        Customer customer = customerMapper.selectByCustomerId(customerID);
        customer.setPassword("");
        Map data = new HashMap();
        data.put("customer",customer);
        return Result.ok(data);
    }
}




