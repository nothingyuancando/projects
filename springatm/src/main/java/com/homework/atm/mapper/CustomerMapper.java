package com.homework.atm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.atm.entity.Card;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Card> {
}
