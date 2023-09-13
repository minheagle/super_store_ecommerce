package com.shopee.clone.DTO.auth.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private Set<String> address;
    private Set<String> role;
}
