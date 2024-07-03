package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @TableName customer
 */
@TableName(value ="customer")
@Data
public class Customer implements Serializable {
    private Integer customerId;

    private String customerName;
    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)//注解指定字段自动填充
    private LocalDateTime createTime = LocalDateTime.now();;

    private String password;

    private static final long serialVersionUID = 1L;
}