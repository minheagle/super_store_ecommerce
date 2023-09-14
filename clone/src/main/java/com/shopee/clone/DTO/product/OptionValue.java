package com.shopee.clone.DTO.product;

import lombok.Data;

@Data
public class OptionValue {
    private Long opValueId;
    private String valueName;
    private Double percent_price;
    private OptionType optionType;
}
