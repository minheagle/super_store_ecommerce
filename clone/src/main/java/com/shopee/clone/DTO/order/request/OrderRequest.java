package com.shopee.clone.DTO.order.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    private Long addressId;
    private Long userId;
    private List<OrderBelongToSellerRequest> listOrderBelongToSeller;
    private Boolean paymentMethod;
    private Double shipMoney;
    private LocalDateTime noteTimeRecipient;
}
