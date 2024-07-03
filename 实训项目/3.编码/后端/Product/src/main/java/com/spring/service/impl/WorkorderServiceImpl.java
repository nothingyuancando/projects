package com.spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.pojo.Workorder;
import com.spring.service.WorkorderService;
import com.spring.mapper.WorkorderMapper;
import org.springframework.stereotype.Service;

/**
* @author Baiyixuan
* @description 针对表【workorder】的数据库操作Service实现
* @createDate 2024-06-25 15:24:22
*/
@Service
public class WorkorderServiceImpl extends ServiceImpl<WorkorderMapper, Workorder>
    implements WorkorderService{

}




