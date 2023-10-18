package com.shopee.clone.DTO.order.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryStatusRequest {
    private Long sellerId;
    private Integer orderNumber;
    private Boolean status;
}
