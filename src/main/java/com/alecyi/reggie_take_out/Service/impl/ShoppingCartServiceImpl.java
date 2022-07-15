package com.alecyi.reggie_take_out.Service.impl;

import com.alecyi.reggie_take_out.Service.ShoppingCartService;
import com.alecyi.reggie_take_out.entity.ShoppingCart;
import com.alecyi.reggie_take_out.mapper.ShoppingCartMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
