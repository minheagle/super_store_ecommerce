package com.shopee.clone.DTO.payment.requestAPI;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentRequest {
    private Integer orderCode;
    private Integer amount;
    private String description;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
//    private String buyerAddress;
//    private List<Item> items;
    private String cancelUrl;
    private String returnUrl;
    private String signature;
}

