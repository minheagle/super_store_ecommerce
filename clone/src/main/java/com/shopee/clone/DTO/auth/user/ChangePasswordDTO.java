package com.shopee.clone.DTO.auth.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotNull
    private String outPassword;
    @NotNull
    private String newPassword;
    @NotNull
    private String confirmPassword;
}
