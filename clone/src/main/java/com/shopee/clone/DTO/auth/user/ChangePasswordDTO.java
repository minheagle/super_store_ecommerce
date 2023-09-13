package com.shopee.clone.DTO.auth.user;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String outPassword;
    private String newPassword;
    private String confirmPassword;
}
