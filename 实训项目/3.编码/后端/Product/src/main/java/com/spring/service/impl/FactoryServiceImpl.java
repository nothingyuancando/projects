package com.spring.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.mapper.*;
import com.spring.pojo.*;
import com.spring.request.PlanAcceptAndRejectRequest;
import com.spring.service.FactoryService;
import com.spring.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author Baiyixuan
* @description 针对表【factory】的数据库操作Service实现
* @createDate 2024-06-25 15:24:22
*/
@Service
public class FactoryServiceImpl extends ServiceImpl<FactoryMapper, Factory>
    implements FactoryService{

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private FactoryMapper factoryMapper;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private WorkorderMapper workorderMapper;
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProducerelationMapper producerelationMapper;
    @Autowired
    private WorkreportMapper workreportMapper;
    @Autowired
    private OrderMapper orderMapper;


    @Override
    public Result factoryregister(Factory factory) {
                /*        注册业务
        1.依然检查账号是否已经被注册
        2.密码加密处理 账号数据保存
        4.返回结果*/

        // 1. 检查工厂名称是否已经存在
        LambdaQueryWrapper<Factory> factoryQueryWrapper = new LambdaQueryWrapper<>();
        factoryQueryWrapper.eq(Factory::getFactoryName, factory.getFactoryName());
        Long factoryCount = factoryMapper.selectCount(factoryQueryWrapper);
        if (factoryCount > 0) {
            return Result.build(null, ResultCodeEnum.FACTORY_NAME_USED);
        }

        //2.账号数据的密码加密和保存
        factory.setFactoryName(factory.getFactoryName());
        factory.setPassword(MD5Util.encrypt(factory.getPassword()));
        factoryMapper.insert(factory);
        return Result.ok(null);
    }

    @Override
    public Result factorylogin(Factory factory) {


/*        登录业务
        1.据账号，查询用户对象-loginUser
        2.四果用户对象为null，查询失败，账号错读!501
        3.对比，医码，失败 返同503的错误
        4.展据用广id生成一个token，token->result 返回*/

        //根据账号查询数据
        LambdaQueryWrapper<Factory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Factory::getFactoryName, factory.getFactoryName());
        Factory loginuser = factoryMapper.selectOne(lambdaQueryWrapper);

        //判断账号
        if (loginuser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //对比密码
        if(!StringUtils.isEmpty(factory.getPassword())
                && MD5Util.encrypt(factory.getPassword()).equals(loginuser.getPassword())){

            //登录成功
            //根据用户id生成token
            String token =jwtHelper.createToken(Long.valueOf(loginuser.getFactoryId()));
            //将token封装到result返回
            LinkedHashMap<String,Object> data = new LinkedHashMap<>();
            data.put("token", token);
            return Result.ok(data);
        }

        //密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    @Override
    public Result acceptPlan(String token, PlanAcceptAndRejectRequest plan) {
                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int planId = plan.getPlan_id();
        int factoryId = jwtHelper.getUserId(token).intValue();
        int planStates = plan.getPlan_states();
        //1表示未接受
        //2表示已接受
        //3表示拒绝
        planMapper.updatePlan(planId,factoryId,planStates);

        return Result.ok(null);
    }

    @Override
    public Result rejectPlan(String token, PlanAcceptAndRejectRequest plan) {
                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int planId = plan.getPlan_id();
        int factoryId = jwtHelper.getUserId(token).intValue();
        Integer planStates = plan.getPlan_states();
        planMapper.updatePlan(planId,factoryId,planStates);
        return Result.ok(null);
    }

    @Override
    public Result creataWorkorder(String token, Workorder workorder) {
                                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int factoryid = jwtHelper.getUserId(token).intValue();
        workorder.setEquipmentId(workorder.getEquipmentId());
        workorder.setProductId(workorder.getProductId());
        workorder.setProduceNumber(workorder.getProduceNumber());
        workorder.setStarttime(workorder.getStarttime());
        workorder.setEndtime(workorder.getEndtime());
        workorder.setWorkorderStates(workorder.getWorkorderStates());
        workorder.setFactoryid(factoryid);
        workorderMapper.insertworkorder(workorder);
        Map data = new HashMap();
        int workorderId = workorderMapper.queryWorkorderId(factoryid);
        data.put("workorder_id", workorderId);
        return Result.ok(data);
    }

    @Override
    public Result produceWorkorder(String token, Workorder workorder) {
                                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int workorderid = workorder.getWorkorderId();
        int workerstates = workorder.getWorkorderStates();
        workorderMapper.updateStates(workorderid,workerstates);
        return Result.ok(null);
    }

    @Override
    public Result doneWorkorder(String token, Workorder workorder) {
                                                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int workorderid = workorder.getWorkorderId();
        int workerstates = workorder.getWorkorderStates();
        workorderMapper.updateStates(workorderid,workerstates);
        return Result.ok(null);
    }

    @Override
    public Result addEquipment(String token, Equipment equipment) {
                                                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        equipment.setEquipmentName(equipment.getEquipmentName());
        equipment.setFactoryId(equipment.getFactoryId());
        equipment.setEquipmentStates(equipment.getEquipmentStates());
        equipment.setSerialNumber(equipment.getSerialNumber());
        equipmentMapper.insert(equipment);
        return Result.ok(null);
    }

    @Override
    public Result deleteEquipment(String token, Equipment equipment) {
                                                                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int equipmentid = equipment.getEquipmentId();
        equipmentMapper.deleteByequipmentId(equipmentid);
        return Result.ok(null);
    }

    @Override
    public Result updateEquipment(String token, Equipment equipment) {
                                                                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        equipmentMapper.updateByequipmentId(equipment);
        return Result.ok(null);
    }

    @Override
    public Result getEquipment(String token) {
                                                                                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int factoryId = jwtHelper.getUserId(token).intValue();
        List<Map<String,Object>> equipmentListMaps = new ArrayList<>();
        List<Equipment> equipmentlist = equipmentMapper.selectEquipment(factoryId);
        for(Equipment equipment:equipmentlist){
            LinkedHashMap<String,Object> equipmentMap = new LinkedHashMap<>();
            equipmentMap.put("factoryId", factoryId);
            equipmentMap.put("equipment_states",equipment.getEquipmentStates());
            equipmentMap.put("equipment_name",equipment.getEquipmentName());
            equipmentMap.put("serial_number",equipment.getSerialNumber());
            equipmentMap.put("equipment_id",equipment.getEquipmentId());
            equipmentListMaps.add(equipmentMap);
        }
        return Result.ok(equipmentListMaps);
    }

    @Override
    public Result getWorkorder(String token) {
                                                                                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可
        */

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int factoryId = jwtHelper.getUserId(token).intValue();
        List<Map<String,Object>> workorderListMaps = new ArrayList<>();
        List<Workorder> workorderlist = workorderMapper.selectWorkorder(factoryId);
        for(Workorder workorder:workorderlist){
            LinkedHashMap<String,Object> workorderMap = new LinkedHashMap<>();
            workorderMap.put("workorder_id",workorder.getWorkorderId());
            workorderMap.put("equipment_id",workorder.getEquipmentId());
            workorderMap.put("product_id",workorder.getProductId());
            workorderMap.put("produce_number",workorder.getProduceNumber());
            workorderMap.put("starttime",workorder.getStarttime());
            workorderMap.put("endtime",workorder.getEndtime());
            workorderMap.put("workorder_states",workorder.getWorkorderStates());
            workorderListMaps.add(workorderMap);
        }
        return Result.ok(workorderListMaps);
    }

    @Override
    public Result addProduct(String token, Product product) {
                                                                                                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可
        */
        LambdaQueryWrapper<Product> lambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Product::getProductName, product.getProductName());
        Long count = productMapper.selectCount(lambdaQueryWrapper);
        if (count > 0) {
            return Result.build(null, ResultCodeEnum.PRODUCT_NAME_USED);
        }

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        product.setProductName(product.getProductName());
        product.setProductDescrible(product.getProductDescrible());
        product.setFactoryId(product.getFactoryId());
        product.setIsProducing(product.getIsProducing());
        productMapper.insert(product);
        return Result.ok(null);
    }

    @Override
    public Result getProduct(String token) {
        /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可
        */

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int factoryId = jwtHelper.getUserId(token).intValue();
        List<Map<String,Object>> productListMaps = new ArrayList<>();
        List<Product> productlist = productMapper.selectproduct(factoryId);
        for(Product product:productlist){
            LinkedHashMap<String,Object> productMap = new LinkedHashMap<>();
            productMap.put("product_id",product.getProductId());
            productMap.put("product_name",product.getProductName());
            productMap.put("product_describle",product.getProductDescrible());
            productMap.put("create_time",product.getCreateTime());
            productMap.put("isProducing",product.getIsProducing());
            productListMaps.add(productMap);
        }
        return Result.ok(productListMaps);
    }

    @Override
    public Result updateProduct(String token, Product product) {
                /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可
        */
        LambdaQueryWrapper<Product> lambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Product::getProductName, product.getProductName());
        Long count = productMapper.selectCount(lambdaQueryWrapper);
        if (count > 0) {
            return Result.build(null, ResultCodeEnum.PRODUCT_NAME_USED);
        }

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        product.setProductId(product.getProductId());
        product.setProductName(product.getProductName());
        product.setProductDescrible(product.getProductDescrible());
        product.setIsProducing(product.getIsProducing());
        productMapper.updateProduct(product);
        return Result.ok(null);
    }

    @Override
    public Result deleteProduct(String token, Product product) {
                        /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可
        */

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int productId = product.getProductId();
        productMapper.deleteByproductId(productId);
        return Result.ok(null);
    }

    @Override
    public Result addRelation(String token, Producerelation producerelation) {
                                /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可
        */

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int equipmentId = producerelation.getEquipmentId();
        int productId = producerelation.getProductId();
        int canproduce = producerelation.getCanproduce();
        int yield = producerelation.getYield();
        Producerelation producerelation1 = new Producerelation();
        producerelation1.setEquipmentId(equipmentId);
        producerelation1.setProductId(productId);
        producerelation1.setYield(yield);
        producerelation1.setCanproduce(canproduce);
        producerelationMapper.insertproducerelation(producerelation1);
        return Result.ok(null);
    }

    @Override
    public Result addWorkreport(String token, Workreport workreport) {
                                        /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可
        */

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        workreportMapper.insert(workreport);
        return Result.ok(null);
    }

    @Override
    public Result getOrder(String token) {
                /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可
        */

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int factoryId = jwtHelper.getUserId(token).intValue();
        List<Map<String,Object>> workreportListMaps = new ArrayList<>();
        List<Workreport> workreportlist = workreportMapper.selectworkreport(factoryId);
        for(Workreport workreport:workreportlist){
            LinkedHashMap<String,Object> workreportMap = new LinkedHashMap<>();
            workreportMap.put("workorder_id",workreport.getWorkorderId());
            workreportMap.put("producing_states",workreport.getProducingStates());
            workreportMap.put("workreport_time",workreport.getWorkreportTime());
            workreportMap.put("work_hour",workreport.getWorkHour());
            workreportMap.put("processing_number",workreport.getProcessingNumber());
            workreportMap.put("qualify_number",workreport.getQualifiyNumber());
            workreportListMaps.add(workreportMap);
        }
        return Result.ok(workreportListMaps);
    }

    @Override
    public Result getView(String token) {

         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //失效  未登录看待
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        //根据token获取对应的用户id
        int factoryId = jwtHelper.getUserId(token).intValue();
        int produceNumber = 0;
        //获取多个pequipmentid
        List<Integer> equipmentids = equipmentMapper.selectequipmentids(factoryId);

        List<Map<String, Object>> equipmentMaps = new ArrayList<>();
        List<Map<String, Object>> productionEfficiencies = new ArrayList<>();
        List<Map<String, Object>> orderstatisticses = new ArrayList<>();
        boolean flag = true;
        for (int equipmentId : equipmentids) {
            //设备运行状况 工单中的设备ID和对应订单中产品的产量
            //LinkedHashMap是HashMap的子类，但是内部还有一个双向链表维护键值对的顺序，
            // 每个键值对既位于哈希表中，也位于双向链表中。LinkedHashMap支持两种顺序插入顺序 、 访问顺序
            LinkedHashMap<String,Object> equipmentMap = new LinkedHashMap<>();
            List<Integer> addnumbers = producerelationMapper.selectProduceNumber(equipmentId);
            for(int addnumber : addnumbers){
                produceNumber+=addnumber;
            }
            int States = equipmentMapper.selectStates(equipmentId);
            equipmentMap.put("equipmentId", equipmentId);
            equipmentMap.put("States", States);
            equipmentMap.put("produceNumber", produceNumber);
            equipmentMaps.add(equipmentMap);
        }

        List<Integer> workorderIds = workorderMapper.queryWorkorderIds(factoryId);
        for(int workorderId : workorderIds){
            List<Integer> workreportIds = workreportMapper.queryWorkreportIds(workorderId);
            for(int workreportid : workreportIds){
                //根据factoryid查一堆workorderid，再根据这一堆workorderid查出一堆workreportid
                //再根据workreportid查出对应的报工日期与加工数量和合格数量
                //工厂生产效益 工单中的报工日期和加工数量
                LinkedHashMap<String,Object> productionEfficiency = new LinkedHashMap<>();
                Date reportdate = workreportMapper.queryreportdate(workreportid);
                int processingnumber = workreportMapper.queryprocessingnumber(workreportid);
                int qualifiynumber = workreportMapper.queryqualifiynumber(workreportid);
                productionEfficiency.put("reportdate", reportdate);
                productionEfficiency.put("processingnumber", processingnumber);
                productionEfficiency.put("qualifiynumber", qualifiynumber);
                productionEfficiencies.add(productionEfficiency);
            }
        }


        //订单统计情况 订单状态和每个状态对应订单数量
        OrderStatus[] orderstatuses = OrderStatus.values();
        for(int i = 0; i < orderstatuses.length; i++) {
            LinkedHashMap<String,Object> orderStatistics = new LinkedHashMap<>();
            Integer orderstatus = orderstatuses[i].getDescription();
            int ordernumber = orderMapper.queryordersnumber(orderstatus);
            orderStatistics.put("orderstatus", orderstatus);
            orderStatistics.put("doneordernumber", ordernumber);
            orderstatisticses.add(orderStatistics);
        }

        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        data.put("equipmentStatus", equipmentMaps);
        data.put("productionEfficiency", productionEfficiencies);
        data.put("orderStatistics", orderstatisticses);
        return Result.ok(data);
    }

    @Override
    public Result getFactoryInfo(String token) {
                                                 /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        //根据token获取客户id
        int factoryID = jwtHelper.getUserId(token).intValue();
        Factory factory = factoryMapper.selectByfactoryId(factoryID);
        factory.setPassword("");
        Map data = new HashMap();
        data.put("factory",factory);
        return Result.ok(data);
    }

    @Override
    public Result getPlan(String token) {
                                                         /*       根据token获取用户数据
        1.token是否在有效期
        2.根据token解析userId
        3.根据用户id查询用数据
        4.去掉密码，封装result结果返回即可*/

        //是否过期 true 过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //失效  未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        //根据token获取客户id
        int factoryID = jwtHelper.getUserId(token).intValue();
        //生产计划 展示生产计划表
        List<Map<String,Object>> planMaps = new ArrayList<>();

        List<Plan> plans = planMapper.selectPlans(factoryID);
        for(Plan plan:plans){
            LinkedHashMap<String,Object> producePlan = new LinkedHashMap<>();
            producePlan.put("plan_id",plan.getPlanId());
            producePlan.put("order_id",plan.getOrderId());
            producePlan.put("starttime",plan.getStarttime());
            producePlan.put("endtime",plan.getEndtime());
            producePlan.put("factory_id",factoryID);
            producePlan.put("plan_states",plan.getPlanStates());
            producePlan.put("product_id",plan.getProductId());
            producePlan.put("remark",plan.getRemark());
            planMaps.add(producePlan);
        }
        return Result.ok(planMaps);
    }
}




