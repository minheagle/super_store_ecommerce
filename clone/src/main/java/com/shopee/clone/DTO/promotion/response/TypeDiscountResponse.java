package com.shopee.clone.DTO.promotion.response;

import com.shopee.clone.entity.payment.EDiscountType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TypeDiscountResponse {
    private EDiscountType discountType;
    private Integer discountValue;
}
