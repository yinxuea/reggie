package com.alecyi.reggie_take_out.Service;

import com.alecyi.reggie_take_out.dto.SetmealDto;
import com.alecyi.reggie_take_out.entity.Setmeal;
import com.alecyi.reggie_take_out.entity.SetmealDish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
