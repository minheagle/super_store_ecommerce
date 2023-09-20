package com.shopee.clone.DTO.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopee.clone.entity.OptionValueEntity;
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
public class ProductItem {
    private Long pItemId;
    private Double price;
    private Integer qtyInStock;
    private Boolean status;
    private List<ImageProduct> imageProductList;
    @JsonIgnore
    private Product product;
    private List<OptionValue> optionValueEntities;
}
