package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName producerelation
 */
@TableName(value ="producerelation")
@Data
public class Producerelation implements Serializable {
    private Integer producerelationId;

    private Integer equipmentId;

    private Integer productId;

    private Integer yield;

    private Integer canproduce;

    private static final long serialVersionUID = 1L;
}