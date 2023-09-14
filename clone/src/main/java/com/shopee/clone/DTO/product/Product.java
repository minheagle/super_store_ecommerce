package com.shopee.clone.DTO.product;

import com.shopee.clone.entity.CategoryEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Product {
    private Long productId;
    private String productName;
    private String description;
    private Boolean status;
    private List<ProductItem> productItemList;
    private CategoryEntity category;
}
