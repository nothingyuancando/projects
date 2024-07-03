package com.spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.pojo.Product;
import com.spring.service.ProductService;
import com.spring.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
* @author Baiyixuan
* @description 针对表【product】的数据库操作Service实现
* @createDate 2024-06-25 15:24:22
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

}




