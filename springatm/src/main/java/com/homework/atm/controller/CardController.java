package com.homework.atm.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.atm.common.R;
import com.homework.atm.entity.Card;
import com.homework.atm.entity.Employee;
import com.homework.atm.entity.Transfer;
import com.homework.atm.entity.User;
import com.homework.atm.service.CardService;
import com.homework.atm.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.LambdaConversionException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService cardService;

    private TradeService tradeService;

    @PostMapping("/login")
    public R<Card> login(HttpServletRequest request, @RequestBody Card card){

        String password = card.getPassword();

        //查数据库
        LambdaQueryWrapper<Card> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Card::getNumber, card.getNumber());
        Card emp = cardService.getOne(queryWrapper);

        //查到了吗
        if(emp == null){
            return R.error("登录失败");
        }
        //比对密码
        if(!password.equals(emp.getPassword())){
            return R.error("密码错误");
        }


        if(emp.getIsLoss()!=0){
            return R.error("卡被冻结");
        }

        //登录
        request.getSession().setAttribute("card", emp.getNumber());
        return R.success(emp);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        request.getSession().removeAttribute("card");
        return R.success("成功退出");
    }



    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Card card){
        log.info("new user info{}",card.toString());
        cardService.save(card);
        return R.success("新增卡success");

    }

//    @PutMapping
//    public R<String> save(@RequestBody Card card){
//        log.info("save card{}",card.toString());
//
//        cardService.save(card);
//        return R.success("save card success");
//    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number){
        log.info("page={},pageSize={},name ={}",page,pageSize,number);
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Card> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(number),Card::getNumber,number);
        log.info("queryWrapper = {}",queryWrapper);
        cardService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);


    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Card card){

        log.info("update user info{}",card.toString());



        cardService.updateById(card);
        return R.success("卡信息修改成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        log.info("delete card{}",ids.toString());

        cardService.removeByIds(ids);
        return R.success("delete card success");

    }


    @GetMapping(value = "/list")
    public R<List<Card>> list(@RequestParam(value = "number") String number){
        log.info("list number={}",number);
        //条件构造器
        LambdaQueryWrapper<Card> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(Card::getNumber,number);
        //queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        // queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Card> list = cardService.list(queryWrapper);
        return R.success(list);
    }

    @GetMapping("/{id}")
    public R<Card> getByNumber(@PathVariable int id){
        log.info("根据id查询卡信息...");

        LambdaQueryWrapper<Card> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Card::getId,id);
        Card card = cardService.getOne(queryWrapper);

        if(card != null){
            return R.success(card);
        }
        return R.error("没有查询到对应卡信息");
    }


    @PutMapping("/trade")
    public R<String> trade(HttpServletRequest request, @RequestBody Transfer transfer){

       String numher = transfer.getNumber();
       String toNumber = transfer.getToNumber();
       BigDecimal money = transfer.getTradeMoney();
       BigDecimal bal = transfer.getBalance();

       LambdaQueryWrapper<Card> queryWrapper1 = new LambdaQueryWrapper<>();
       LambdaQueryWrapper<Card> queryWrapper2 = new LambdaQueryWrapper<>();

       queryWrapper1.eq(Card::getNumber,numher);
       queryWrapper2.eq(Card::getNumber,toNumber);

       Card card1 = cardService.getOne(queryWrapper1);
       Card card2 = cardService.getOne(queryWrapper2);

       log.info("card1={},card2={}",card1,card2);
       BigDecimal balance1 = card1.getBalance();
       BigDecimal balance2 = card2.getBalance();

       BigDecimal diff = balance1.subtract(money);
       BigDecimal sum = balance2.add(money);

        card1.setBalance(diff);
       card2.setBalance(sum);

       cardService.updateById(card1);
       cardService.updateById(card2);

        return R.success("卡信息修改成功");
    }


}
