package com.alecyi.reggie_take_out.common;


/**
 * 基于ThreadLocal封装工具类，用于保存和获取用户的ID
 * 基于当前线程做为公寓类，
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
