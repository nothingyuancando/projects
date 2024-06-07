package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.ShoppingCart;
import generator.service.ShoppingCartService;
import generator.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author caiyuan
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2024-06-06 10:46:47
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




