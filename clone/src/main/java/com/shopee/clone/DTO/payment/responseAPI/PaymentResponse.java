package com.shopee.clone.DTO.payment.responseAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("signature")
    private String signature;
}
