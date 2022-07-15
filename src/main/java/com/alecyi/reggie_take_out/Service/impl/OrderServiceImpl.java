package com.alecyi.reggie_take_out.Service.impl;

import com.alecyi.reggie_take_out.Service.*;
import com.alecyi.reggie_take_out.common.BaseContext;
import com.alecyi.reggie_take_out.common.CustomException;
import com.alecyi.reggie_take_out.entity.*;
import com.alecyi.reggie_take_out.mapper.OrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    UserService userService;

    @Autowired
    AddressBookService addressBookService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    OrderDetailService orderDetailService;

    @Override
    @Transactional
    public void submit(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);

        if (shoppingCartList == null || shoppingCartList.size() == 0){
            throw new CustomException("购物车无数据不能下单");
        }

        //查询用户数据

        User userById = userService.getById(userId);

        //查询地址数据
        Long address = orders.getAddressBookId();
        AddressBook addressBookServiceById = addressBookService.getById(address);
        if (addressBookServiceById ==null){
            throw new CustomException("地址信息错误，不能下单");
        }

        //设置orders的订单内容
        long id = IdWorker.getId();
        AtomicInteger atomicInteger = new AtomicInteger(0);

        List<OrderDetail> detailList = shoppingCartList.stream().map((item)->{

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(id);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            atomicInteger.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return  orderDetail;
        }).collect(Collectors.toList());


        orders.setNumber(String.valueOf(id));
        orders.setId(id);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(atomicInteger.get()));
        orders.setUserId(userId);
        orders.setUserName(userById.getName());
        orders.setConsignee(addressBookServiceById.getConsignee());
        orders.setPhone(addressBookServiceById.getPhone());
        orders.setAddress(
                        (addressBookServiceById.getProvinceName() == null ? "":addressBookServiceById.getProvinceName())+
                        (addressBookServiceById.getCityName() == null ? "" : addressBookServiceById.getCityName())+
                        (addressBookServiceById.getDistrictName() == null ? "" : addressBookServiceById.getDistrictName())+
                        (addressBookServiceById.getDetail() == null ? "" : addressBookServiceById.getDetail())
        );
        this.save(orders);

        //订单表插入多个数据
        orderDetailService.saveBatch(detailList);

        //清空购物车

        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

    }
}
