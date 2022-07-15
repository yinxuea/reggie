package com.alecyi.reggie_take_out.Service;

import com.alecyi.reggie_take_out.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {


    /**
     * 根据ID删除分类，删除之前查询当前分类是否关联了菜品
     * @param id
     */
    public  void remove(Long id);
}
