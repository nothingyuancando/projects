package com.spring.control;

import com.spring.request.OrderAndDetails;
import com.spring.service.CustomerService;
import com.spring.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //用户信息回显
    @GetMapping("customerinfo")
    public Result getCustomerInfo(@RequestHeader String Authorization){
        Result result = customerService.getCustomerInfo(Authorization);
        return result;
    }

    //用户下单
    @PostMapping("order")
    public Result order(@RequestHeader String Authorization, @RequestBody OrderAndDetails orderAndDetails) {
        Result result = customerService.order(Authorization,orderAndDetails);
        return result;
    }

    //查看所有订单
    @GetMapping("order")
    public Result getOrder(@RequestHeader String Authorization) {
        Result result = customerService.getorder(Authorization);
        return result;
    }

    //修改订单
    @PutMapping("order")
    public Result updateOrder(@RequestHeader String Authorization, @RequestBody OrderAndDetails orderAndDetails) {
        Result result = customerService.updateOrder(Authorization,orderAndDetails);
        return result;
    }

    //取消订单
    @DeleteMapping("order/{orderID}")
    public Result deleteOrder(@RequestHeader String Authorization, @PathVariable("orderID") Integer orderID) {
        Result result = customerService.deleteOrder(Authorization,orderID);
        return result;
    }

    //查看平台信息
    @GetMapping("view")
    public Result viewPlatform() {
        Result result = customerService.viewPlatform();
        return result;
    }

    //产看订单明细
    @GetMapping("order/{orderID}")
    public Result viewOrderDetail(@RequestHeader String Authorization, @PathVariable("orderID") Integer orderID) {
        Result result = customerService.viewOrderDetail(Authorization,orderID);
        return result;
    }
}
