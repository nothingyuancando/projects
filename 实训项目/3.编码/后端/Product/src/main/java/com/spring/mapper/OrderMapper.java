package com.spring.mapper;

import com.spring.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.pojo.Orderdetial;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【order】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Order
*/
public interface OrderMapper extends BaseMapper<Order> {

    int selectorderId(@Param("customerId") int customerId);

    void insertorder(Order order);

    List<Integer> queryorderId(@Param("customerID") int customerID);

    Date queryordertime(@Param("orderId") int orderId);

    int queryorderstates(@Param("orderId") int orderId);

    void updateByOrderId(@Param("orderId") int orderId,@Param("orderStates") int orderStates);

    void deleteByorderId(@Param("orderID") Integer orderID);

    Order queryOrder(@Param("orderID") Integer orderID);

    int queryordersnumber(@Param("orderstates") Integer orderstatus);

    List<Order> selectOrders();

    void updateOrderStates(@Param("orderId") Integer orderId,@Param("ordeStates") Integer ordeStates);

}




