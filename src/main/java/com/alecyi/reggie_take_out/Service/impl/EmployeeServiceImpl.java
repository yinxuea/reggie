package com.alecyi.reggie_take_out.Service.impl;


import com.alecyi.reggie_take_out.Service.EmployeeService;
import com.alecyi.reggie_take_out.entity.Employee;
import com.alecyi.reggie_take_out.mapper.EmployeeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
