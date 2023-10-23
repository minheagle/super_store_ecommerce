package com.shopee.clone.DTO.order.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderBelongToSellerRequest {
    private Long sellerId;
    private List<Long> cartId;
    private Double shipMoney;
    private Double amount;
    private Long promotionId;
}
