package com.shopee.clone.DTO.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
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
