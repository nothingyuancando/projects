package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @TableName factory
 */
@TableName(value ="factory")
@Data
public class Factory implements Serializable {
    private Integer factoryId;

    private String factoryName;

    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)//注解指定字段自动填充
    private LocalDateTime createTime = LocalDateTime.now();

    private String password;

    private static final long serialVersionUID = 1L;

}