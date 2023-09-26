package com.shopee.clone.DTO.order.request;


import com.shopee.clone.DTO.cart.CartRequest;
import lombok.Data;

import java.util.List;
@Data
public class OrderRequest {
    private String address;
    private Long userId;
    private List<CartRequest> cartRequest;
    private String paymentMethod;
}
