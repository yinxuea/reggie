package com.alecyi.reggie_take_out.Controller;

import com.alecyi.reggie_take_out.Service.CategoryService;
import com.alecyi.reggie_take_out.common.R;
import com.alecyi.reggie_take_out.entity.Category;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;

    @PostMapping
    @CacheEvict(value = "categoryCache",key = "#category.id + '_' + #category.type")
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");

    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> getAllPage( int page,int pageSize){
        Page<Category> iPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        categoryService.page(iPage,lambdaQueryWrapper);
        return R.success(iPage);
    }

    @DeleteMapping
    @CacheEvict(value = "categoryCache",allEntries = true)
    public R<String> delete(Long id){
        categoryService.remove(id);
        return R.success("删除成功");
    }


    @PutMapping
    @CacheEvict(value = "categoryCache",key = "#category.id + '_' + #category.type")
    public R<String> update(@RequestBody Category category){
        boolean b = categoryService.updateById(category);
        if (b) {
            return R.success("修改成功！");
        }else {
            return R.error("修改失败！");
        }
    }


    @GetMapping("/list")
    @Cacheable(value = "categoryCache",key = "#category.id + '_' + #category.type")
    public R<List<Category>> listR(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lambdaQueryWrapper);

        return R.success(list);
    }


}
