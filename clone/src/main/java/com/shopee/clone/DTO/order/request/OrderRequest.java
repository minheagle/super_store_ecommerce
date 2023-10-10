package com.shopee.clone.DTO.order.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Long addressId;
    private Long userId;
    private List<OrderBelongToSellerRequest> listOrderBelongToSeller;
    private Boolean paymentStatus;
}
