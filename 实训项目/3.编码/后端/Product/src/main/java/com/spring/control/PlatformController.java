package com.spring.control;

import com.spring.pojo.Order;
import com.spring.pojo.Plan;
import com.spring.service.PlatformService;
import com.spring.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("platform")
@CrossOrigin(origins = "http://localhost:5173")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    //平台信息回显
    @GetMapping("platforminfo")
    public Result getPlatformInfo(@RequestHeader String Authorization){
        Result result = platformService.getPlatformInfo(Authorization);
        return result;
    }

    //查看工厂
    //获取所有工厂的信息
    @GetMapping("factories")
    public Result getFactories(@RequestHeader String Authorization) {
        Result result = platformService.getFactories(Authorization);
        return result;
    }

    //平台总览
    @GetMapping("overview")
    public Result getPlatformOverview(@RequestHeader String Authorization) {
        Result result = platformService.getOverview(Authorization);
        return result;
    }

    //订单初始状态是1未接收状态
    //接收订单2
    //拒绝订单3
    //完成订单4
    @PutMapping("orders")
    public Result updateOrders(@RequestHeader String Authorization,@RequestBody Order order) {
        Result result = platformService.updateOrders(Authorization,order);
        return result;
    }
    //新建生产计划
    @PostMapping("plan")
    public Result addPlan(@RequestHeader String Authorization,@RequestBody Plan plan) {
        Result result = platformService.addPlan(Authorization,plan);
        return result;
    }
    //生产计划初始状态为1未启动
    //启动生产计划2
    //完成生产计划3
    @PutMapping("plan")
    public Result updatePlan(@RequestHeader String Authorization,@RequestBody Plan plan) {
        Result result = platformService.updatePlan(Authorization,plan);
        return result;
    }

    //查看所有订单
    @GetMapping("getorder")
    public Result getOrder(@RequestHeader String Authorization) {
        Result result = platformService.getOrder(Authorization);
        return result;
    }

}
