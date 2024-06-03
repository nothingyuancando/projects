package com.homework.atm.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Trade implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private String number;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String tradeMoney;

    private String tradeType;

    private String remark;
}
