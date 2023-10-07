package com.shopee.clone.DTO.payment.responseAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InformationLinkPaymentResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("orderCode")
    private Integer orderCode;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("amountPaid")
    private Integer amountPaid;

    @JsonProperty("amountRemaining")
    private Integer amountRemaining;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("transactions")
    private List<TransactionPaymentResponse> transactions;

    @JsonProperty("cancellationReason")
    private String cancellationReason;

    @JsonProperty("canceledAt")
    private String canceledAt;
}
