package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName orderdetial
 */
@TableName(value ="orderdetial")
@Data
public class Orderdetial implements Serializable {
    private Integer orderdetialId;

    private Integer productId;

    private Integer amount;

    private Integer orderId;

    private static final long serialVersionUID = 1L;
}