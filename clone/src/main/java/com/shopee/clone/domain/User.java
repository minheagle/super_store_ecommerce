package com.shopee.clone.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String phone;
    private LocalDate brithDate;
    private String role;


}
