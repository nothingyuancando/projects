package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @TableName platform
 */
@TableName(value ="platform")
@Data
public class Platform implements Serializable {
    private Integer platformId;

    private String platformName;

    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)//注解指定字段自动填充
    private LocalDateTime createTime = LocalDateTime.now();;

    private String password;

    private static final long serialVersionUID = 1L;
}