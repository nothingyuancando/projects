package com.spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.pojo.Equipment;
import com.spring.service.EquipmentService;
import com.spring.mapper.EquipmentMapper;
import org.springframework.stereotype.Service;

/**
* @author Baiyixuan
* @description 针对表【equipment】的数据库操作Service实现
* @createDate 2024-06-25 15:24:22
*/
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment>
    implements EquipmentService{

}




