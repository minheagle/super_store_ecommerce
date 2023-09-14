package com.shopee.clone.DTO.auth.user;

import com.shopee.clone.entity.AddressEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserUpdateDTO {
    private String userName;
    private String fullName;
    private String phone;
    private String email;
    private String avatar;
    private String address;
    private Date dateOfBirth;
}
