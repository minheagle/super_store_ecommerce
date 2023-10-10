package com.shopee.clone.DTO.auth.user;


import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.RoleEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data

public class User {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Date dateOfBirth;
    private String imageUrl;
    private String imgPublicId;
    private List<AddressEntity> address;
    private List<RoleEntity> roles;
    private String chatId;
}
