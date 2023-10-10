package com.shopee.clone.DTO.promotion.request;

import com.shopee.clone.entity.payment.EDiscountType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromotionRequestCreate {
    @NotBlank(message = "Name's required")
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalDate endDate;
    @NotBlank
    private Long sellerId;
    @NotBlank
    private EDiscountType discountType;
    @NotBlank
    private Integer discountValue;
    private Integer minPurchaseAmount;
    private Boolean isActive;
    private Integer usageLimitPerUser;
}
