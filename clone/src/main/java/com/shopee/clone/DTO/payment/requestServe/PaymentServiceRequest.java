package com.shopee.clone.DTO.payment.requestServe;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentServiceRequest {
    Long userId;
    Integer orderNumber;
    Integer amountPayment;
}
