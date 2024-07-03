package com.spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.pojo.Workreport;
import com.spring.service.WorkreportService;
import com.spring.mapper.WorkreportMapper;
import org.springframework.stereotype.Service;

/**
* @author Baiyixuan
* @description 针对表【workreport】的数据库操作Service实现
* @createDate 2024-06-25 15:24:22
*/
@Service
public class WorkreportServiceImpl extends ServiceImpl<WorkreportMapper, Workreport>
    implements WorkreportService{

}




