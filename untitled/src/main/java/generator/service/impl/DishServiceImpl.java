package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Dish;
import generator.service.DishService;
import generator.mapper.DishMapper;
import org.springframework.stereotype.Service;

/**
* @author caiyuan
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2024-06-06 10:46:47
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

}




