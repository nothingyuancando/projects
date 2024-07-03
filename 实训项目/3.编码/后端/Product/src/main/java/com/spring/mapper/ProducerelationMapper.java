package com.spring.mapper;

import com.spring.pojo.Producerelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【producerelation】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Producerelation
*/
public interface ProducerelationMapper extends BaseMapper<Producerelation> {


    List<Integer> selectProduceNumber(@Param("equipmentId") int equipmentId);

    void insertproducerelation(Producerelation producerelation1);

}




