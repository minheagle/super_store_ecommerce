package com.shopee.clone.DTO.auth.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAddressDTO {
    @NotBlank
    private String address;
}
