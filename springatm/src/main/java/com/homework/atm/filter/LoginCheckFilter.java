package com.homework.atm.filter;

import com.alibaba.fastjson.JSON;
import com.homework.atm.common.BaseContext;
import com.homework.atm.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter{
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();// /backend/index.html
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/card/login",
                "/card/logout",};
        boolean check = check(urls, requestURI);
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;}
        if(request.getSession().getAttribute("employee") != null){//管理员登录
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            int empId = (int) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId((long) empId);
            filterChain.doFilter(request,response);
            return;}
        if(request.getSession().getAttribute("card") != null){//用户登录
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("card"));
            String cardId = (String) request.getSession().getAttribute("card");
            BaseContext.setCurrentId(Long.valueOf((String) cardId));
            filterChain.doFilter(request,response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
