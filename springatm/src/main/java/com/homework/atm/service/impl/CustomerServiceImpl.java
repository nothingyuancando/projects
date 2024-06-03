package com.homework.atm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.atm.entity.Card;
import com.homework.atm.mapper.CardMapper;
import com.homework.atm.mapper.CustomerMapper;
import com.homework.atm.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Card> implements CustomerService {

}
