package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Employee;
import generator.service.EmployeeService;
import generator.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author caiyuan
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2024-06-06 10:46:47
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




