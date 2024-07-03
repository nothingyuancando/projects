package com.spring.control;

import com.mysql.cj.protocol.ResultsetRow;
import com.spring.mapper.EquipmentMapper;
import com.spring.pojo.*;
import com.spring.request.PlanAcceptAndRejectRequest;
import com.spring.service.FactoryService;
import com.spring.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Relation;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("factory")
public class FactoryController {

    @Autowired
    private FactoryService factoryService;

    //工厂信息回显
    @GetMapping("factoryinfo")
    public Result getFactoryInfo(@RequestHeader String Authorization){
        Result result = factoryService.getFactoryInfo(Authorization);
        return result;
    }

    //接受生产计划
    @PutMapping("plan")
    public Result acceptPlan(@RequestHeader String Authorization, @RequestBody PlanAcceptAndRejectRequest plan){
        if(plan.getPlan_states() == 2) {
            Result result = factoryService.acceptPlan(Authorization, plan);
            return result;
        }
        return rejectPlan(Authorization,plan);
    }

    //拒绝生产计划
    public Result rejectPlan(@RequestHeader String Authorization,@RequestBody PlanAcceptAndRejectRequest plan){
        Result result = factoryService.rejectPlan(Authorization,plan);
        return result;
    }

    //新建工单
    @PostMapping("workorder")
    public Result createWorkorder(@RequestHeader String Authorization,@RequestBody Workorder workorder){
        Result result = factoryService.creataWorkorder(Authorization,workorder);
        return result;
    }

    //生产工单
    @PutMapping("workorder")
    public Result produceWorkorder(@RequestHeader String Authorization,@RequestBody Workorder workorder){
        //1:未生产  2：生产 3：生产完成
        if(workorder.getWorkorderStates() == 2){
            Result result = factoryService.produceWorkorder(Authorization,workorder);
            return result;
        }
        return doneWorkorder(Authorization,workorder);
    }

    //完成工单
    public Result doneWorkorder(@RequestHeader String Authorization,@RequestBody Workorder workorder){
        Result result = factoryService.doneWorkorder(Authorization,workorder);
        return result;
    }

    //添加设备
    @PostMapping("equipment")
    public Result addEquipment(@RequestHeader String Authorization,@RequestBody Equipment equipment){
        Result result = factoryService.addEquipment(Authorization,equipment);
        return result;
    }

    //删除设备
    @DeleteMapping("equipment")
    public Result deleteEquipment(@RequestHeader String Authorization,@RequestBody Equipment equipment){
        Result result = factoryService.deleteEquipment(Authorization,equipment);
        return result;
    }

    //修改设备
    @PutMapping("equipment")
    public Result updateEquipment(@RequestHeader String Authorization,@RequestBody Equipment equipment){
        Result result = factoryService.updateEquipment(Authorization,equipment);
        return result;
    }

    //查询设备
    @GetMapping("equipment")
    public Result getEquipment(@RequestHeader String Authorization){
        Result result = factoryService.getEquipment(Authorization);
        return result;
    }

    //查看工单
    @GetMapping("workorder")
    public Result getWorkorder(@RequestHeader String Authorization){
        Result result = factoryService.getWorkorder(Authorization);
        return result;
    }

    //添加产品
    @PostMapping("product")
    public Result addProduct(@RequestHeader String Authorization,@RequestBody Product product){
        Result result = factoryService.addProduct(Authorization,product);
        return result;
    }

    //查看产品
    @GetMapping("product")
    public Result getProduct(@RequestHeader String Authorization){
        Result result = factoryService.getProduct(Authorization);
        return result;
    }

    //更新产品信息
    @PutMapping("product")
    public Result updateProduct(@RequestHeader String Authorization,@RequestBody Product product){
        Result result = factoryService.updateProduct(Authorization,product);
        return result;
    }

    //删除产品
    @DeleteMapping("product")
    public Result deleteProduct(@RequestHeader String Authorization,@RequestBody Product product){
        Result result = factoryService.deleteProduct(Authorization,product);
        return result;
    }

    //关联产品设备
    @PostMapping("relation")
    public Result addRelation(@RequestHeader String Authorization, @RequestBody Producerelation producerelation){
        Result result =factoryService.addRelation(Authorization,producerelation);
        return result;
    }

    //进行报工
    @PostMapping("workreport")
    public Result addWorkreport(@RequestHeader String Authorization, @RequestBody Workreport workreport){
        Result result = factoryService.addWorkreport(Authorization,workreport);
        return result;
    }

    //报工汇总
    @GetMapping("order")
    public Result getworkreport(@RequestHeader String Authorization){
        Result result = factoryService.getOrder(Authorization);
        return result;
    }

    //工厂总览
    @GetMapping("view")
    public Result getView(@RequestHeader String Authorization){
        Result result = factoryService.getView(Authorization);
        return result;
    }

    //查看所属生产计划
    @GetMapping("getplan")
    public Result getPlan(@RequestHeader String Authorization){
        Result result = factoryService.getPlan(Authorization);
        return result;
    }
}
