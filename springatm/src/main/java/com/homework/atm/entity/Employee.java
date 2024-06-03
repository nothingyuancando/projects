package com.homework.atm.entity;


import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String password;

    private int status;


}
