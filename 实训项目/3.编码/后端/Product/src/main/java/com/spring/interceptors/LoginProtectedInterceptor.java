package com.spring.interceptors;

//登录包含拦截器，检查请求头中是否含有有效token
//有，有效。放行
//没有，无效，返回504

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.utils.JwtHelper;
import com.spring.utils.Result;
import com.spring.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component//将拦截器放进ioc容器
public class LoginProtectedInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取token
        String token = request.getHeader("token");
        //检查是否有效
        boolean expiration = jwtHelper.isExpiration(token);
        //有效放行
        if (!expiration) {
            //放行，没有过期
            return true;
        }

        //无效 返回504状态json
        Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);
        //使用Java jackson提供的java对象转换json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().println(json);

        return false;
    }
}
