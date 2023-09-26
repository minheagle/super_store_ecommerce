package com.shopee.clone.DTO.order.response;

import com.shopee.clone.DTO.product.response.ProductMatchToCartResponse;
import lombok.Data;

@Data

public class OrderDetailResponse {
    private Long id;
    private ProductMatchToCartResponse product;
    private Integer quantity;
    private Double unitPrice;
}
