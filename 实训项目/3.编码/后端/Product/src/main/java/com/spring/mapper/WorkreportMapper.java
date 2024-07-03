package com.spring.mapper;

import com.spring.pojo.Workreport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【workreport】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Workreport
*/
public interface WorkreportMapper extends BaseMapper<Workreport> {

    List<Workreport> selectworkreport(@Param("factoryId") int factoryId);

    List<Integer> queryWorkreportIds(@Param("workorderId") int workorderId);


    Date queryreportdate(@Param("workreportid") int workreportid);

    int queryprocessingnumber(@Param("workreportid") int workreportid);

    int queryqualifiynumber(@Param("workreportid") int workreportid);
}




