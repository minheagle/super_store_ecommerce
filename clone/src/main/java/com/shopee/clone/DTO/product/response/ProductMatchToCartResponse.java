package com.shopee.clone.DTO.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductMatchToCartResponse {
    private String productName;
    private String description;
    private Boolean status;
    private ProductItemResponseDTO productItemResponse;
}
