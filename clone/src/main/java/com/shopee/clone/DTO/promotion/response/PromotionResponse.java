package com.shopee.clone.DTO.promotion.response;

import com.shopee.clone.entity.payment.EDiscountType;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromotionResponse {
    private Long promotionId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long sellerId;
    private EDiscountType discountType;
    private Integer discountValue;
    private Integer minPurchaseAmount;
    private Boolean isActive;
    private Integer usageLimitPerUser;
}
