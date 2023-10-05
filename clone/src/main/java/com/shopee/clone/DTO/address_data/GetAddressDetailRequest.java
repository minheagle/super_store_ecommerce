package com.shopee.clone.DTO.address_data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetAddressDetailRequest {
    @NotBlank
    private String wardName;
    @NotBlank
    private String districtName;
    @NotBlank
    private String provinceName;
}
