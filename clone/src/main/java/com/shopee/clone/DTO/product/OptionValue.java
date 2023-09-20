package com.shopee.clone.DTO.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionValue {
    private Long opValueId;
    private String valueName;
    private Double percent_price;
    private OptionType optionType;
    @JsonIgnore
    private ProductItem productItem;
}
