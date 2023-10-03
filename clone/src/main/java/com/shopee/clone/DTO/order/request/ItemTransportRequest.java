package com.shopee.clone.DTO.order.request;

import lombok.Data;

@Data
public class ItemTransportRequest {
    private String productName;
    private Integer quantity;
    private Double unitPrice;
}
