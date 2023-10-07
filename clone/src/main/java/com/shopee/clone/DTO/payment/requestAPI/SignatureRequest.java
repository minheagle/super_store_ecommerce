package com.shopee.clone.DTO.payment.requestAPI;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SignatureRequest {
    private String amount;
    private String cancelUrl;
    private String description;
    private String orderCode;
    private String returnUrl;
    private String secretKey;
}
