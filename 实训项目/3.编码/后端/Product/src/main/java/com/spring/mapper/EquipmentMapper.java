package com.spring.mapper;

import com.spring.pojo.Equipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【equipment】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Equipment
*/
public interface EquipmentMapper extends BaseMapper<Equipment> {

    List<Equipment> selectEquipment(@Param("factoryId") int factoryId);

    List<Integer> selectequipmentids(@Param("factoryId") int factoryId);

    int selectStates(@Param("equipmentId") int equipmentId);

    void deleteByequipmentId(@Param("equipmentId") int equipmentid);

    void updateByequipmentId(Equipment equipment);
}




