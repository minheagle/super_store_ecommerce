package com.shopee.clone.DTO.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionTypeDTO {
    private Long opTypeId;
    private String optionName;
    private OptionValueDTO optionValue;
}
