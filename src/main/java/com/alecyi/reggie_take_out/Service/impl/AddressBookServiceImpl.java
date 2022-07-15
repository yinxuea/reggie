package com.alecyi.reggie_take_out.Service.impl;

import com.alecyi.reggie_take_out.Service.AddressBookService;
import com.alecyi.reggie_take_out.entity.AddressBook;
import com.alecyi.reggie_take_out.mapper.AddressBookMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
