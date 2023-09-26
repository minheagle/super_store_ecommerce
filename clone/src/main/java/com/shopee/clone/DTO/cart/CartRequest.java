package com.shopee.clone.DTO.cart;

import lombok.Data;

import java.util.List;
@Data
public class CartRequest {
    private Long sellerId;
    private List<Long> listCartId;
    private Double shipMoney;
}
