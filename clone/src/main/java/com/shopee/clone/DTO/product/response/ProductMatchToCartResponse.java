package com.shopee.clone.DTO.product.response;

import com.shopee.clone.DTO.cart.ProductItemMatchToCart;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMatchToCartResponse {
    private Long productId;
    private String productName;
    private String description;
    private Boolean status;
    private ProductItemMatchToCart productItemResponse;
}