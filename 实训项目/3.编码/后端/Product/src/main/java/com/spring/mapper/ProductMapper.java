package com.spring.mapper;

import com.spring.pojo.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.pojo.Workreport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【product】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Product
*/
public interface ProductMapper extends BaseMapper<Product> {

    List<Product> selectproduct(@Param("factoryId") int factoryId);

    void updateProduct(Product product);


    void deleteByproductId(@Param("productId") int productId);
}




