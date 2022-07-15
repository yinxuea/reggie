package com.alecyi.reggie_take_out.Controller;

import com.alecyi.reggie_take_out.Service.AddressBookService;
import com.alecyi.reggie_take_out.common.BaseContext;
import com.alecyi.reggie_take_out.common.R;
import com.alecyi.reggie_take_out.entity.AddressBook;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    /**
     * 保存地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        if (addressBook.getLabel() == null)addressBook.setLabel("无");
        addressBookService.save(addressBook);
        return R.success(addressBook);

    }

    /**
     * 根据ID查地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R getDetailSave(@PathVariable Long id){
        AddressBook byId = addressBookService.getById(id);
        if (byId !=null){
            return R.success(byId);
        }else{
            return R.success("没有对象");
        }

    }

    /**
     * 获取用户全部地址
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> getall(){
        Long userID = BaseContext.getCurrentId();

        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId,userID);
        lambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(lambdaQueryWrapper);
        return R.success(list);

    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        lambdaUpdateWrapper.set(AddressBook::getIsDefault,0);

        addressBookService.update(lambdaUpdateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<AddressBook> setDefaultUpdate(@RequestBody AddressBook addressBook){
        if (addressBook.getLabel() == null)addressBook.setLabel("无");
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }


    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper  = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        lambdaQueryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(lambdaQueryWrapper);
        if (one == null) {
            return R.error("没默认地址");
        }else {
            return R.success(one);
        }

    }


}
