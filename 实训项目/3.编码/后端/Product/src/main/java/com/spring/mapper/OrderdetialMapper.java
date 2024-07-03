package com.spring.mapper;

import com.spring.pojo.Orderdetial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【orderdetial】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Orderdetial
*/
public interface OrderdetialMapper extends BaseMapper<Orderdetial> {

    void insertorderdetail(Orderdetial orderDetail);

    void updateByproductID(@Param("productID") int productID,@Param("amount") int amount);

    void deleteByorderId(@Param("orderID") Integer orderID);

    List<Orderdetial> queryOrders(@Param("orderID") Integer orderID);
}




