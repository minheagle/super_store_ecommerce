package com.shopee.clone.DTO.order.response;

import com.shopee.clone.DTO.cart.CartResponse;
import com.shopee.clone.entity.SellerEntity;
import lombok.Builder;
import lombok.Data;

@Data
public class CheckOutResponse {
    private CartResponse cartResponse;
    private Double amount;
    private Double shipMoney;
    private Double total;
}
