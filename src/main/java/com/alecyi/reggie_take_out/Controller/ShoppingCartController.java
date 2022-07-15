package com.alecyi.reggie_take_out.Controller;

import com.alecyi.reggie_take_out.Service.CategoryService;
import com.alecyi.reggie_take_out.Service.ShoppingCartService;
import com.alecyi.reggie_take_out.common.BaseContext;
import com.alecyi.reggie_take_out.common.R;
import com.alecyi.reggie_take_out.entity.Category;
import com.alecyi.reggie_take_out.entity.Dish;
import com.alecyi.reggie_take_out.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    CategoryService categoryService;

    /**
     * 获取购物车菜单列表
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getAllList(HttpSession session){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(ShoppingCart::getUserId,session.getAttribute("user"));

        return R.success(shoppingCartService.list(lambdaQueryWrapper));
    }


    /**
     * 清空购物车所有内容
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> delete(HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);

        List<ShoppingCart> shoppingCartslist = shoppingCartService.list(shoppingCartLambdaQueryWrapper);

        shoppingCartService.removeByIds(shoppingCartslist);

        return R.success("清空成功");
    }

    /**
     * 购物车增加菜品
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        Long userID = (Long) session.getAttribute("user");
        shoppingCart.setUserId(userID);

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userID);
        Long dishId = shoppingCart.getDishId();
        if (dishId !=null){
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());

        }else{
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        if (cartServiceOne != null){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    @PostMapping("/sub")
    public R sub(@RequestBody ShoppingCart shoppingCart,HttpSession session){
        Long dishId = shoppingCart.getDishId();
        Long userId = (Long) session.getAttribute("user");

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (dishId !=null){
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        }else{
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        Integer number = cartServiceOne.getNumber();

        if (number > 1) {
            cartServiceOne.setNumber(number-1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            cartServiceOne.setNumber(0);
            shoppingCartService.removeById(cartServiceOne);
        }


        return R.success(cartServiceOne);
    }

}
