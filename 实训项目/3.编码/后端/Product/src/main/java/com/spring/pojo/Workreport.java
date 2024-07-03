package com.spring.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName workreport
 */
@TableName(value ="workreport")
@Data
public class Workreport implements Serializable {
    private Integer workreportId;

    private Integer workorderId;

    private Integer producingStates;

    private Date workreportTime;

    private String workHour;

    private Integer processingNumber;

    private Integer qualifiyNumber;

    private static final long serialVersionUID = 1L;
}