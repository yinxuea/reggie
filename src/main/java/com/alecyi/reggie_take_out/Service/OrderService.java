package com.alecyi.reggie_take_out.Service;

import com.alecyi.reggie_take_out.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {

    void submit(Orders orders);
}
