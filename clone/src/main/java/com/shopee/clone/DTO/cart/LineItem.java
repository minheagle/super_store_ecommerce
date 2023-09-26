package com.shopee.clone.DTO.cart;

import com.shopee.clone.DTO.product.response.ProductMatchToCartResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {
    private Long cartId;
    private ProductMatchToCartResponse product;
    private Integer quantity;
}
