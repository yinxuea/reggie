package com.alecyi.reggie_take_out.Service.impl;

import com.alecyi.reggie_take_out.Service.DishFlavoerService;
import com.alecyi.reggie_take_out.Service.DishService;
import com.alecyi.reggie_take_out.entity.DishFlavor;
import com.alecyi.reggie_take_out.mapper.DishFlavorMapper;
import com.alecyi.reggie_take_out.mapper.DishMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavoerService {
}
