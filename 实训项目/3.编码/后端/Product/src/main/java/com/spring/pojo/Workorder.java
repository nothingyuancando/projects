package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName workorder
 */
@TableName(value ="workorder")
@Data
public class Workorder implements Serializable {
    private Integer workorderId;

    private Integer equipmentId;

    private Integer productId;

    private Integer produceNumber;

    private Date starttime;

    private Date endtime;

    private Integer workorderStates;

    private Integer factoryid;

    private static final long serialVersionUID = 1L;
}