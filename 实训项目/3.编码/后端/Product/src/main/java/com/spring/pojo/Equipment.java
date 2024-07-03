package com.spring.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName equipment
 */
@TableName(value ="equipment")
@Data
public class Equipment implements Serializable {
    private Integer equipmentId;

    private Integer factoryId;

    private Integer equipmentStates;

    private String equipmentName;

    private String serialNumber;

    private static final long serialVersionUID = 1L;
}