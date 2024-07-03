package com.spring.utils;

/**
 * 统一返回结果状态信息类
 *
 */
public enum ResultCodeEnum {

    //登录注册枚举
    NOTLOGIN(504, "notLogin"),
    PASSWORD_ERROR(503, "passwordError"),
    SUCCESS(200, "success"),
    USERNAME_ERROR(501, "usernameError"),
    USERNAME_USED(505, "userNameUsed"),

    //订单状态枚举
    // 注意枚举常量通常使用大写字母，并且单词之间使用下划线分隔
    //初始订单状态未接收
    NOT_TAKEN(1,"order Nottoken"),// 如果需要额外的信息或字符串表示，可以使用构造函数
    //接收订单
    TAKEN(2,"Order accepted."),
    //拒绝订单
    REJECTED(3,"Order rejected."),
    //完成订单
    COMPLETED(4,"Order completed."),

    //产品枚举
    PRODUCT_NAME_USED(507,"productNameUsed"),


    //生产计划状态，枚举
    //生产计划准备中
    BE_READY(1,"Plan Ready"),
    //生产计划启动
    START_PLAN(2,"Start Plan"),
    //生产计划完成
    COMPLETE_PLAN(3,"Complete Plan"),
    //生产计划创建成功
    PLAN_CREATED(4,"Plan Created"),

    //杂项枚举
    CUSTOMER_NAME_USED(201, "customer_name_used"),
    PLATFORM_NME_USED(200, "platform_ume_used"),
    FACTORY_NAME_USED(506,"factoryNameUsed"),

    PRODUCT_ADD_SUCCESS(505,"productAddSuccess"),
    ROLE_NAME_ERROR(502,"role_name_error");



    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
