package com.spring.utils;


public enum OrderStatus {
    // 注意枚举常量通常使用大写字母，并且单词之间使用下划线分隔
    //初始订单状态未接收
    NOT_TAKEN(1),// 如果需要额外的信息或字符串表示，可以使用构造函数
    //接收订单
    TAKEN(2),
    //拒绝订单
    REJECTED(3),
    //完成订单
    COMPLETED(4);
  
    // 如果需要额外的信息或字符串表示，可以添加一个字段和一个构造函数  
    private final Integer description;
  
    // 私有构造函数，仅供枚举常量使用  
    OrderStatus(Integer description) {
        this.description = description;  
    }  
  
    // 如果需要，可以添加一个方法以获取描述信息  
    public Integer getDescription() {
        return description;  
    }  
  
    // 如果需要，可以添加其他方法或逻辑  
  
    // 例如，一个用于检查订单是否已完成的方法  
    public boolean isCompleted() {  
        return this == COMPLETED;  
    }
}