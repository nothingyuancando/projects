package com.spring.mapper;

import com.spring.pojo.Platform;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Baiyixuan
* @description 针对表【platform】的数据库操作Mapper
* @createDate 2024-06-25 15:24:22
* @Entity com.spring.pojo.Platform
*/
public interface PlatformMapper extends BaseMapper<Platform> {

    List<Platform> selectPlatform();

    Platform selectByplatformId(@Param("platformid") int platformID);
}




