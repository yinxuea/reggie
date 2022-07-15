package com.alecyi.reggie_take_out.Controller;


import com.alecyi.reggie_take_out.Service.UserService;
import com.alecyi.reggie_take_out.common.R;
import com.alecyi.reggie_take_out.entity.User;
import com.alecyi.reggie_take_out.utils.SendEmailUtils;
import com.alecyi.reggie_take_out.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 邮箱验证码发送
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        String email = user.getPhone();
        if (StringUtils.isNotEmpty(email)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //SendEmailUtils.sendEmail("短信","您的邮箱验证码是："+code,email);

            redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);

           return R.success(code);
        }
        return R.error("验证码发送失败！！");


    }

    /**
     * 移动用户登陆
     *
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        String email =map.get("phone").toString();
        String code = map.get("code").toString();

        //获取session保存的验证码
        Object phone = redisTemplate.opsForValue().get(email);
        if (phone != null && phone.equals(code)){

            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,email);
            User user = userService.getOne(lambdaQueryWrapper);
            if (user == null){
                user = new User();
                user.setPhone(email);
                user.setStatus(1);
                userService.save(user);
            }

           session.setAttribute("user",user.getId());

            //从redis获取验证码

            redisTemplate.delete(email);
            return R.success(user);

        }
        return R.error("登陆失败！");
    }

}
