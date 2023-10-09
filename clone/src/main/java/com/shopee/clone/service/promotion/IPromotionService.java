package com.shopee.clone.service.promotion;

import com.shopee.clone.DTO.promotion.request.PromotionRequestCreate;
import com.shopee.clone.DTO.promotion.response.PromotionResponse;
import org.springframework.http.ResponseEntity;

public interface IPromotionService {
    ResponseEntity<?> createPromotion(PromotionRequestCreate promotionRequestCreate);
}
