package com.homework.atm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.atm.entity.Card;
import com.homework.atm.mapper.CardMapper;
import com.homework.atm.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {
    @Autowired
    private CardService cardService;
    @Transactional
    public void updateTrade(Card card1,Card card2){
            cardService.updateById(card1);
            cardService.updateById(card2);
    }
}
