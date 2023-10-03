package com.shopee.clone.DTO.auth.user;

import com.shopee.clone.entity.AddressEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserUpdateDTO {
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username must contain only letters, numbers, or underscores")
    private String userName;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]*$", message = "Phone number must contain only numbers")
    private String phone;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private Date dateOfBirth;
}
