package com.shopee.clone.DTO.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopee.clone.entity.CategoryEntity;
import com.shopee.clone.entity.SellerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private Long productId;
    private String productName;
    private Double minPrice;
    private Double maxPrice;
    private String description;
    private Boolean status;
//    @JsonIgnore
    private List<ProductItem> productItemList;
    private CategoryEntity category;
    private SellerEntity seller;
}
