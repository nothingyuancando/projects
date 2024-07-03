package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @TableName order
 */
@TableName(value ="order")
@Data
public class Order implements Serializable {
    private Integer orderId;

    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)//注解指定字段自动填充
    private LocalDateTime orderTime = LocalDateTime.now();;

    private Integer orderCustomerid;

    private Integer ordeStates;

    private static final long serialVersionUID = 1L;
}