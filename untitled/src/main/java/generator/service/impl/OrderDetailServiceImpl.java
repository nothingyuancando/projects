package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.OrderDetail;
import generator.service.OrderDetailService;
import generator.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author caiyuan
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2024-06-06 10:46:47
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




