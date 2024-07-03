package com.spring.mapper;

import com.spring.pojo.Factory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【factory】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Factory
*/
public interface FactoryMapper extends BaseMapper<Factory> {


    List<Factory> getfactory();

    List<Integer> getfactoryids();

    Factory selectByfactoryId(@Param("factoryid") int factoryID);
}




