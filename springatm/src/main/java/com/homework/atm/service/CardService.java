package com.homework.atm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.atm.entity.Card;

public interface CardService extends IService<Card> {

    public void updateTrade(Card card1,Card card2);

}
