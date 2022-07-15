package com.alecyi.reggie_take_out.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendEmailUtils {

    private static JavaMailSender javaMailSender;

    @Autowired
    private void setJavaMailSender(JavaMailSender javamain){
        SendEmailUtils.javaMailSender = javamain;
    }
    //发送人
    private static String from;

    @Value("${email.from}")
    private void setFrom(String email){
        SendEmailUtils.from = email;
    }


    public static void sendEmail(String sunb,String text,String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(sunb);
        message.setText(text);
        javaMailSender.send(message);
    }


}
