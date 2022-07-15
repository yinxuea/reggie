package com.alecyi.reggie_take_out.Fileter;

import com.alecyi.reggie_take_out.common.BaseContext;
import com.alecyi.reggie_take_out.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤器检查用户是否登录
 */

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {


    //通配符请求器
    public static final AntPathMatcher PATH_PATTERN = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //log.info("拦截到请求:{}",request.getRequestURI());


        String requestUrl = request.getRequestURI();
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        boolean check = check(urls, requestUrl);

        if (check) {
            filterChain.doFilter(request,response);
            return;
        }

       // log.info( request.getSession().getAttribute("employee").toString());

        //如果登陆了就直接放行后台
        if (request.getSession().getAttribute("employee") != null){
            Long employee = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employee);
            filterChain.doFilter(request,response);
            return;
        }

        //如果登陆了就直接放行移动端的
        if (request.getSession().getAttribute("user") != null){
            Long user = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(user);

            filterChain.doFilter(request,response);
            return;
        }

        //log.info(JSON.toJSONString(R.error("NOTLOGIN")));
        //log.info(R.error("NOTLOGIN").toString());

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    public boolean check(String[] urls, String requestUrl){
        for (String url : urls) {
            boolean match = PATH_PATTERN.match(url, requestUrl);
            if (match){
                return true;
            }

        }
        return false;
    }
}
