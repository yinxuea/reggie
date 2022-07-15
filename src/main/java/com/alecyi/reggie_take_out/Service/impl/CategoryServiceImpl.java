package com.alecyi.reggie_take_out.Service.impl;


import com.alecyi.reggie_take_out.Service.CategoryService;
import com.alecyi.reggie_take_out.Service.DishService;
import com.alecyi.reggie_take_out.Service.SetmealService;
import com.alecyi.reggie_take_out.common.CustomException;
import com.alecyi.reggie_take_out.entity.Category;
import com.alecyi.reggie_take_out.entity.Dish;
import com.alecyi.reggie_take_out.entity.Setmeal;
import com.alecyi.reggie_take_out.mapper.CategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;

    /**
     * 据ID删除分类，删除之前查询当前分类是否关联了菜品
     * @param id
     */
    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = (int) dishService.count(dishLambdaQueryWrapper);

        //查询是否有菜品，
        if (dishCount > 0){
            throw new CustomException("当前菜品已存在");
        }

        //查询是否有套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        int setmealCount = (int) setmealService.count(setmealLambdaQueryWrapper);

        if (setmealCount >0){
            throw new CustomException("当前套餐已存在");

        }

        super.removeById(id);
    }
}
