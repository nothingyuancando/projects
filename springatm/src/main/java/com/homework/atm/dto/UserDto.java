package com.homework.atm.dto;

import com.homework.atm.entity.User;
import lombok.Data;


@Data
public class UserDto extends User {

    private String cardnumber;
}
