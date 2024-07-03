package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName plan
 */
@TableName(value ="plan")
@Data
public class Plan implements Serializable {
    private Integer planId;

    private Integer orderId;

    private Date starttime;

    private Date endtime;

    private Integer factoryId;

    private Integer planStates;

    private Integer productId;

    private String remark;

    private static final long serialVersionUID = 1L;
}