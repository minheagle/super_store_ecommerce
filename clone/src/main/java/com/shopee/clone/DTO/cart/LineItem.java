package com.shopee.clone.DTO.cart;

import com.shopee.clone.DTO.product.response.ProductMatchToCartResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {
    private Long cartId;
    private ProductMatchToCartResponse product;
    private Integer quantity;
}
