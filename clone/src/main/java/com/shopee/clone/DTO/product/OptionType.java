package com.shopee.clone.DTO.product;

import lombok.Data;

import java.util.List;

@Data
public class OptionType {
    private Long opTypeId;
    private String optionName;
    private ProductItem productItem;
    private List<OptionValue> optionValueList;
}
