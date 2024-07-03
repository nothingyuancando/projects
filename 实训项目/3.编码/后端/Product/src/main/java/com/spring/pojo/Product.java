package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product implements Serializable {
    private Integer productId;

    private String productName;

    private String productDescrible;

    private Integer factoryId;

    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)//注解指定字段自动填充
    private LocalDateTime createTime = LocalDateTime.now();;

    private Integer isProducing;

    private static final long serialVersionUID = 1L;
}