package com.alecyi.reggie_take_out.Controller;


import com.alecyi.reggie_take_out.common.R;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonConrtoller {

    @Value("${reggie.path}")
    public String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        //获取上传的文件名
        String originalFilename = file.getOriginalFilename();

        //截取上传文件的后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf('.'));

        //使用uuid生成文件名
        String fileName = UUID.randomUUID().toString()+substring;

        //获取文件目录，如果目录不存在就直接创建
        File file1 = new File(basePath);
        if (!file1.exists())
        {
            file1.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));


            ServletOutputStream servletOutputStream = response.getOutputStream();

            response.setContentType("image/jpg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!= -1){
                servletOutputStream.write(bytes,0,len);
                servletOutputStream.flush();
            }
            fileInputStream.close();
            servletOutputStream.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
