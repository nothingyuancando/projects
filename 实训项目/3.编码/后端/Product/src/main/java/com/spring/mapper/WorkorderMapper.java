package com.spring.mapper;

import com.spring.pojo.Workorder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【workorder】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Workorder
*/
public interface WorkorderMapper extends BaseMapper<Workorder> {

    int queryWorkorderId(@Param("factoryId") int factoryId);

    void insertworkorder(Workorder workorder);

    void updateStates(@Param("workorderid") int pworkorderid,@Param("workorderstates") int workerstates);

    List<Workorder> selectWorkorder(@Param("factoryId") int factoryId);

    int selectYieldByProductId(@Param("productId") Integer productId);

    List<Integer> queryWorkorderIds(@Param("factoryId") int factoryId);


}




