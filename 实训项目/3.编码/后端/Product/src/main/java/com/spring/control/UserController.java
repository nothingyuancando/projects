package com.spring.control;


import com.spring.pojo.Customer;
import com.spring.pojo.Factory;
import com.spring.pojo.Platform;
import com.spring.request.RegisterAndLoginRequest;
import com.spring.service.CustomerService;
import com.spring.service.FactoryService;
import com.spring.service.PlatformService;
import com.spring.utils.Result;
import com.spring.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private PlatformService platformService;


    //用户注册
    @PostMapping("register")
    public Result register(@RequestBody RegisterAndLoginRequest registerAndLoginRequest){
        String role = registerAndLoginRequest.getRole();
        Result result;
        if (Objects.equals(role, "customer")){
            Customer customer = new Customer();
            customer.setCustomerName(registerAndLoginRequest.getUsername());
            customer.setPassword(registerAndLoginRequest.getPassword());
            // Handle customer-specific registration logic
            result = customerService.customerregister(customer);
            return result;
        } else if (Objects.equals(role, "factory")) {
            Factory factory = new Factory();
            factory.setFactoryName(registerAndLoginRequest.getUsername());
            factory.setPassword(registerAndLoginRequest.getPassword());
            // Handle factory-specific registration logic
            result = factoryService.factoryregister(factory);
            return result;
        } else if (Objects.equals(role, "platform")) {
            Platform platform = new Platform();
            platform.setPlatformName(registerAndLoginRequest.getUsername());
            platform.setPassword(registerAndLoginRequest.getPassword());
            // Handle platform-specific registration logic
            result = platformService.platformregister(platform);
            return result;
        }
        return Result.build(null, ResultCodeEnum.ROLE_NAME_ERROR);
    }

    //用户登录
    @PostMapping("login")
    public Result login(@RequestBody RegisterAndLoginRequest registerAndLoginRequest){
        String role = registerAndLoginRequest.getRole();
        Result result;
        if (Objects.equals(role, "customer")){
            Customer customer = new Customer();
            customer.setCustomerName(registerAndLoginRequest.getUsername());
            customer.setPassword(registerAndLoginRequest.getPassword());
            // Handle customer-specific registration logic
            result = customerService.customerlogin(customer);
            return result;
        } else if (Objects.equals(role, "factory")) {
            Factory factory = new Factory();
            factory.setFactoryName(registerAndLoginRequest.getUsername());
            factory.setPassword(registerAndLoginRequest.getPassword());
            // Handle factory-specific registration logic
            result = factoryService.factorylogin(factory);
            return result;
        } else if (Objects.equals(role, "platform")) {
            Platform platform = new Platform();
            platform.setPlatformName(registerAndLoginRequest.getUsername());
            platform.setPassword(registerAndLoginRequest.getPassword());
            // Handle platform-specific registration logic
            result = platformService.platformlogin(platform);
            return result;
        }
        return Result.build(null, ResultCodeEnum.ROLE_NAME_ERROR);
    }
}
