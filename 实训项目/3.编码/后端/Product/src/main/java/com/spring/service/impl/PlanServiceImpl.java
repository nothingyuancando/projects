package com.spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.mapper.PlanMapper;
import com.spring.pojo.Plan;
import com.spring.service.PlanService;
import org.springframework.stereotype.Service;

/**
* @author Baiyixuan
* @description 针对表【plan】的数据库操作Service实现
* @createDate 2024-06-26 14:56:34
*/
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan>
    implements PlanService{

}




