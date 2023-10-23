package com.shopee.clone.DTO.promotion.response;

import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.payment.EDiscountType;
import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromotionOfUserResponse {
    private Long promotionId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Seller seller;
    private EDiscountType discountType;
    private Integer discountValue;
    private Integer minPurchaseAmount;
    private Integer usageAvailable;
}
