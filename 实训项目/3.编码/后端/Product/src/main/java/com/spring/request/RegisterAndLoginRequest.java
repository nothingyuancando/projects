package com.spring.request;

import lombok.Data;


//注册的请求类
@Data
public class RegisterAndLoginRequest {
    private String username;
    private String password;
    private String role;
}
