package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Orders;
import generator.service.OrdersService;
import generator.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author caiyuan
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2024-06-06 10:46:47
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




