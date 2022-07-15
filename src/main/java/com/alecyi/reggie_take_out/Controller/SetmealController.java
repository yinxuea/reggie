package com.alecyi.reggie_take_out.Controller;


import com.alecyi.reggie_take_out.Service.CategoryService;
import com.alecyi.reggie_take_out.Service.DishService;
import com.alecyi.reggie_take_out.Service.SetmealDishService;
import com.alecyi.reggie_take_out.Service.SetmealService;
import com.alecyi.reggie_take_out.common.R;
import com.alecyi.reggie_take_out.dto.DishDto;
import com.alecyi.reggie_take_out.dto.SetmealDto;
import com.alecyi.reggie_take_out.entity.Category;
import com.alecyi.reggie_take_out.entity.Dish;
import com.alecyi.reggie_take_out.entity.Setmeal;
import com.alecyi.reggie_take_out.entity.SetmealDish;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(String.valueOf(setmealDto));
        setmealService.saveWithDish(setmealDto);
        String key = "seemeal_"+setmealDto.getCategoryId() +"_"+setmealDto.getStatus();
        redisTemplate.delete(key);
        return R.success("添加成功！");
    }

    @GetMapping("/page")
    public R<Page> getPage(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,lambdaQueryWrapper);
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if (byId!=null){
                String catgeName = byId.getName();
                setmealDto.setCategoryName(catgeName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);


        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        Set key = redisTemplate.keys("seemeal_*");
        redisTemplate.delete(key);
        return R.success("删除成功");
    }



    @GetMapping("/list")
    public R<List<Setmeal>> listR(Setmeal setmeal){
        List<Setmeal> list = null;

        String key = "seemeal_"+setmeal.getCategoryId() +"_"+setmeal.getStatus();

        list = (List<Setmeal>) redisTemplate.opsForValue().get(key);

        if (list != null){
            return R.success(list);
        }

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        lambdaQueryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());

        list = setmealService.list(lambdaQueryWrapper);

        redisTemplate.opsForValue().set(key,list);

        return R.success(list);
    }
}
