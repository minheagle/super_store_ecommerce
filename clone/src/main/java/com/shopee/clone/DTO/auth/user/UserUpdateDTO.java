package com.shopee.clone.DTO.auth.user;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserUpdateDTO {
    private String userName;
    private String fullName;
    private String phone;
    private String email;
    private String avatar;
    private Date dateOfBirth;
}
