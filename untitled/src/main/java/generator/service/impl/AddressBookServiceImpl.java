package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.AddressBook;
import generator.service.AddressBookService;
import generator.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author caiyuan
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2024-06-06 10:46:47
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




