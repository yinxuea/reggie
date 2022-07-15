package com.alecyi.reggie_take_out.Service;

import com.alecyi.reggie_take_out.dto.DishDto;
import com.alecyi.reggie_take_out.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品同时增加口味数据
     * @param dishDto
     */
    void saveWithFlaover(DishDto dishDto);


    /**
     * 根据ID查询菜品和口味ID
     * @param id
     * @return
     */
    DishDto getByIdWithFlaover(Long id);

    void updateWithFlaover(DishDto dishDto);

}
