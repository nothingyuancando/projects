package com.spring.config;

import com.spring.interceptors.LoginProtectedInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //配置拦截器

    @Autowired
    private LoginProtectedInterceptor loginProtectedInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(loginProtectedInterceptor).addPathPatterns("/**");//添加拦截器并且配置拦截路径
        registry.addInterceptor(loginProtectedInterceptor).addPathPatterns("/customer/view");//添加拦截器并且配置拦截路径
    }
}
