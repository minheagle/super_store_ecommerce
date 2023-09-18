package com.shopee.clone.DTO.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionType {
    private Long opTypeId;
    private String optionName;
    private Set<ProductItem> productItems = new HashSet<>();
    private List<OptionValue> optionValueList;
}
