package com.alecyi.reggie_take_out.Service.impl;

import com.alecyi.reggie_take_out.Service.SetmealDishService;
import com.alecyi.reggie_take_out.Service.SetmealService;
import com.alecyi.reggie_take_out.common.CustomException;
import com.alecyi.reggie_take_out.dto.SetmealDto;
import com.alecyi.reggie_take_out.entity.Setmeal;
import com.alecyi.reggie_take_out.entity.SetmealDish;
import com.alecyi.reggie_take_out.mapper.SetmealMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmaelServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {



    @Autowired
     private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        log.info(String.valueOf(setmealDishes));
        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = (int) this.count(queryWrapper);

        if (count > 0){
            throw new CustomException("套餐正在售卖不能删除");
        }


        //如果不在售卖中就先删除套餐的表

        this.removeByIds(ids);

        //删除关系表的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(lambdaQueryWrapper);


    }
}
