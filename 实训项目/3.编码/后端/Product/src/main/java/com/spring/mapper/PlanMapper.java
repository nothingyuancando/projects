package com.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.pojo.Plan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【plan】的数据库操作Mapper
* @createDate 2024-06-26 14:56:34
* @Entity generator.pojo.Plan
*/
public interface PlanMapper extends BaseMapper<Plan> {

    void updatePlan(@Param("planId") int planId, @Param("factoryId") int factoryId, @Param("planStates") Integer planStates);

    List<Plan> selectPlans(@Param("factoryId") int factoryid);

    void updatePlanStates(@Param("planId") Integer planId,@Param("planStates") Integer planStates);
}




