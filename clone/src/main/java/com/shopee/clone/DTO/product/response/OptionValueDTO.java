package com.shopee.clone.DTO.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionValueDTO {
    private Long opValueId;
    private String valueName;
//    private Double percent_price;
}
