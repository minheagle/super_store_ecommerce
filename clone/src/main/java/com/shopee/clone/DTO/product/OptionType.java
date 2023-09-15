package com.shopee.clone.DTO.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionType {
    private Long opTypeId;
    private String optionName;
    private ProductItem productItem;
    private List<OptionValue> optionValueList;
}
