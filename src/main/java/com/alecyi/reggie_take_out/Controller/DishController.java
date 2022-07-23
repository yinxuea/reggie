package com.alecyi.reggie_take_out.Controller;


import com.alecyi.reggie_take_out.Service.CategoryService;
import com.alecyi.reggie_take_out.Service.DishFlavoerService;
import com.alecyi.reggie_take_out.Service.DishService;
import com.alecyi.reggie_take_out.common.R;
import com.alecyi.reggie_take_out.dto.DishDto;
import com.alecyi.reggie_take_out.entity.Category;
import com.alecyi.reggie_take_out.entity.Dish;
import com.alecyi.reggie_take_out.entity.DishFlavor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")

public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    DishFlavoerService dishFlavoerService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;



    @PostMapping
    @CacheEvict(value = "dishCache",key = "#dishDto.categoryId + '_' + #dishDto.status")
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlaover(dishDto);

//        String key = "dish_*";
//        redisTemplate.keys(key);
//        String key = "dish_" + dishDto.getCategoryId() + "__" + dishDto.getStatus();
//        redisTemplate.delete(key);

        return R.success("添加成功！");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Dish> page1 = new Page<>(page,pageSize);

        Page<DishDto> dishDtoPage = new Page<>();


        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(page1,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(page1,dishDtoPage,"records");

        List<Dish> records = page1.getRecords();
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category !=null){
                String name1 = category.getName();
                dishDto.setCategoryName(name1);

            }
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getID(@PathVariable Long id){
        DishDto byIdWithFlaover = dishService.getByIdWithFlaover(id);
        return R.success(byIdWithFlaover);


    }

    @PutMapping
    @CacheEvict(value = "dishCache",key = "#dishDto.categoryId + '_' + #dishDto.status")
    public R<String> dishPut(@RequestBody DishDto dishDto){

        dishService.updateWithFlaover(dishDto);

//        Set key = redisTemplate.keys("dish_*");
//
//        redisTemplate.delete(key);

//        String key = "dish_" + dishDto.getCategoryId() + "__" + dishDto.getStatus();
//        redisTemplate.delete(key);

        return R.success("修改成功");
    }

    /**
     * 根据条件查询特殊的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "dishCache" , key = "#dish.categoryId + '_' + #dish.status")
    public R<List<DishDto>> listR(Dish dish){
        List<DishDto> dishDtoList = null;
        //如果存在就返回不查询数据库
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.like(dish.getName() != null , Dish::getName,dish.getName());
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishes = dishService.list(lambdaQueryWrapper);
        dishDtoList = dishes.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category !=null){
                String name1 = category.getName();
                dishDto.setCategoryName(name1);

            }

            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(DishFlavor::getDishId,id);
            List<DishFlavor> list1 = dishFlavoerService.list(lambdaQueryWrapper1);

            dishDto.setFlavors(list1);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);

    }

}
