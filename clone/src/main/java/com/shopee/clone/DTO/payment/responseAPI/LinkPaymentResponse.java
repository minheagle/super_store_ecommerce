package com.shopee.clone.DTO.payment.responseAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LinkPaymentResponse {
    @JsonProperty("bin")
    private String bin;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("orderCode")
    private Integer orderCode;

    @JsonProperty("paymentLinkId")
    private String paymentLinkId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("checkoutUrl")
    private String checkoutUrl;

    @JsonProperty("qrCode")
    private String qrCode;
}
