package com.homework.atm.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transfer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String toNumber;
    private String number;
    private BigDecimal tradeMoney;
    private BigDecimal balance;


}
