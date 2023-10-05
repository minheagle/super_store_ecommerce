package com.shopee.clone.DTO.address_data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetWardByNameRequest {
    @NotBlank
    private String name;
}
