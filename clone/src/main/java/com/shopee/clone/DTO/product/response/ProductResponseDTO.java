package com.shopee.clone.DTO.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private String description;
    private Boolean status;
    private Long sellerId;
    private Long categoryId;
    private List<ProductItemResponseDTO> productItemResponseList;
}
