package com.homework.atm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.atm.entity.Card;
import com.homework.atm.mapper.CardMapper;
import com.homework.atm.service.CardService;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {
}
