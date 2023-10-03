package com.shopee.clone.DTO.address_data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetProvinceByNameRequest {
    @NotBlank
    private String name;
}
