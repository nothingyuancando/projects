package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Category;
import generator.service.CategoryService;
import generator.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author caiyuan
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2024-06-06 10:46:47
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




