package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.DishFlavor;
import generator.service.DishFlavorService;
import generator.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author caiyuan
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2024-06-06 10:46:47
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




