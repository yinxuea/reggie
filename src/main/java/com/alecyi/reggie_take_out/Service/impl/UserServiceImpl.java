package com.alecyi.reggie_take_out.Service.impl;

import com.alecyi.reggie_take_out.Service.UserService;
import com.alecyi.reggie_take_out.entity.User;
import com.alecyi.reggie_take_out.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
