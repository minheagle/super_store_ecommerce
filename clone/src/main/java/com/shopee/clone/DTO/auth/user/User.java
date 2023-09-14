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

    private Date dataOfBirth;

    private String avatar;

    private List<AddressEntity> addressEntities;

    private List<RoleEntity> roles;

}
