package com.alecyi.reggie_take_out.Service.impl;

import com.alecyi.reggie_take_out.Service.OrderDetailService;
import com.alecyi.reggie_take_out.entity.OrderDetail;
import com.alecyi.reggie_take_out.mapper.OrderDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
