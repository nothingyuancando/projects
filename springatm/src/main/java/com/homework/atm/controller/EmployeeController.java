package com.homework.atm.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.atm.common.R;
import com.homework.atm.entity.Card;
import com.homework.atm.entity.Employee;
import com.homework.atm.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getName, employee.getName());
        Employee emp = employeeService.getOne(queryWrapper);

        //查到了吗
        if(emp == null){
            return R.error("登录失败");
        }
        //比对密码
        if(!password.equals(emp.getPassword())){
            return R.error("密码错误");
        }


        if(emp.getStatus()==0){
            return R.error("账号禁用");
        }

        //登录
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        request.getSession().removeAttribute("employee");
        return R.success("成功退出");
    }


    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("new employee info{}",employee.toString());

        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));


        employee.setStatus(1);
        //long empId = (Long)request.getSession().getAttribute("employee");

        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增管理员success");

    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){


        log.info("page={},pageSize={},name ={}",page,pageSize,name);


        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

log.info("queryWrapper = {}",queryWrapper);

        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

        log.info("update employee info{}",employee.toString());

        //long empId = (Long)request.getSession().getAttribute("employee");

        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);

        long id = Thread.currentThread().getId();
        log.info("线程id{}",id);

        employeeService.updateById(employee);
        return R.success("管理员信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询管理员信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应管理员信息");
    }



}
