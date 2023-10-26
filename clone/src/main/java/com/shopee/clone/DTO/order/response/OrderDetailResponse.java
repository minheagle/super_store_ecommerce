package com.shopee.clone.DTO.order.response;

import com.shopee.clone.DTO.product.response.ProductMatchToCartResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private ProductMatchToCartResponse product;
    private Double unitPrice;
    private Integer quantity;
}
