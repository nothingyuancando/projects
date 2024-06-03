package com.homework.atm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homework.atm.common.R;
import com.homework.atm.entity.Card;
import com.homework.atm.entity.Employee;
import com.homework.atm.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public R<Card> login(HttpServletRequest request, @RequestBody Card card){

        //MD5加密
        String password = card.getPassword();

        //查数据库
        LambdaQueryWrapper<Card> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Card::getNumber, card.getNumber());
        Card emp = customerService.getOne(queryWrapper);

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

//update

    //query




}
