package com.alecyi.reggie_take_out.Controller;


import com.alecyi.reggie_take_out.Service.OrderDetailService;
import com.alecyi.reggie_take_out.Service.OrderService;
import com.alecyi.reggie_take_out.common.R;
import com.alecyi.reggie_take_out.dto.DishDto;
import com.alecyi.reggie_take_out.dto.OrdersDto;
import com.alecyi.reggie_take_out.entity.Category;
import com.alecyi.reggie_take_out.entity.Dish;
import com.alecyi.reggie_take_out.entity.OrderDetail;
import com.alecyi.reggie_take_out.entity.Orders;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("下单成功");
    }

//    @GetMapping("/userPage")
//    public R<Page> page(int page, int pageSize){
//        Page<Orders> iPage = new Page<>(page,pageSize);
//
//        Page<OrdersDto> ordersDtoPage = new Page<>();
//
//        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.orderByAsc(Orders::getOrderTime);
//        orderService.page(iPage,lambdaQueryWrapper);
//
//        BeanUtils.copyProperties(iPage,ordersDtoPage);
//
//        List<Orders> orders = iPage.getRecords();
//        List<OrdersDto> list = orders.stream().map((item)->
//        {
//            OrdersDto ordersDto = new OrdersDto();
//            BeanUtils.copyProperties(item,ordersDto);
//            Long number = Long.valueOf(item.getNumber());
//            LambdaQueryWrapper<OrderDetail> detailLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            detailLambdaQueryWrapper.eq(OrderDetail::getOrderId,orders);
//            long count = orderDetailService.count(detailLambdaQueryWrapper);
//
//
//        }).collect(Collectors.toList());
//
//
//        return R.success(iPage);
//
//    }

    @GetMapping("/page")
    public R<Page> getAllPage( int page,int pageSize){
        Page<Orders> iPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Orders::getOrderTime);

        orderService.page(iPage,lambdaQueryWrapper);
        return R.success(iPage);
    }
}
