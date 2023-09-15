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
public class ProductItem {
    private Long pItemId;
    private Double price;
    private Integer qtyInStock;
    private Boolean status;
    private List<ImageProduct> imageProductList;
    private Product product;
    private List<OptionType> optionTypeList;
}
