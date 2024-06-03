package com.homework.atm.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Card implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String userName;
    private String userPid;
    private String number;


    private String moneyType;
    private String password;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime openDate;
    private BigDecimal openMoney;
    private BigDecimal balance;
    private int isLoss;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    private int isDelete;

}
