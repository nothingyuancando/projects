package com.homework.atm.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.atm.common.R;
import com.homework.atm.entity.Employee;
import com.homework.atm.entity.User;
import com.homework.atm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody User user){
        log.info("new user info{}",user.toString());


        userService.save(user);

        return R.success("新增用户success");

    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        log.info("page info{},pageSize{}",page,pageSize);
        Page pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(User::getId);

        userService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);

    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody User user){

        log.info("update user info{}",user.toString());


        userService.updateById(user);
        return R.success("员工信息修改成功");
    }

    @GetMapping(value = "/list")
    public R<List<User>> list(@RequestParam(value = "id") int id){
        //条件构造器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(User::getId, id);
        //queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
       // queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<User> list = userService.list(queryWrapper);
        return R.success(list);
    }


    @GetMapping("/{id}")
    public R<User> getById(@PathVariable int id){
        log.info("根据id查询用户信息...");
        User user = userService.getById(id);
        if(user != null){
            return R.success(user);
        }
        return R.error("没有查询到对应用户信息");
    }

}
