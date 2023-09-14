package com.shopee.clone.DTO.auth.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAddressDTO {
    @NotNull
    private String address;
}
