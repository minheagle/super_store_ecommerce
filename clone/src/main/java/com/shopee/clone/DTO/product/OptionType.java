package com.shopee.clone.DTO.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<OptionValue> optionValueList;
}
