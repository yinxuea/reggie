package com.alecyi.reggie_take_out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan //启动拦截器的注解
@EnableTransactionManagement//开启数据库的事务
@EnableCaching //开启缓存的功能
public class ReggieTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieTakeOutApplication.class, args);
        log.info("启动成功");

    }

}
