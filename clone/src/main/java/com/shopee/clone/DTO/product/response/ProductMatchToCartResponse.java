package com.shopee.clone.DTO.product.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMatchToCartResponse {
    private String productName;
    private String description;
    private Boolean status;
    private ProductItemResponseDTO productItemResponse;
}
