package com.shopee.clone.DTO.auth.user;


import java.util.Date;
import java.util.Set;

public class User {
    private Long id;

    private String userName;

    private String fullName;

    private String email;

    private String phone;

    private String password;

    private Date dataOfBirth;

    private String avatar;

    private Set<String> roles;
}
