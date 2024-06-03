package com.homework.atm.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.atm.common.R;
import com.homework.atm.entity.Trade;
import com.homework.atm.entity.User;
import com.homework.atm.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        log.info("page info{},pageSize{}",page,pageSize);
        Page pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Trade> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Trade::getId);

        tradeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);

    }

}
