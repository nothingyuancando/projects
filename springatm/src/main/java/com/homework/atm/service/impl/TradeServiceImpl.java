package com.homework.atm.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.atm.entity.Trade;
import com.homework.atm.mapper.TradeMapper;
import com.homework.atm.service.TradeService;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade> implements TradeService {
}
