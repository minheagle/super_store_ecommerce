package com.shopee.clone.service.promotion;

import com.shopee.clone.DTO.promotion.request.PromotionRequestCreate;
import com.shopee.clone.DTO.promotion.response.PromotionResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPromotionService {
    ResponseEntity<?> createPromotion(PromotionRequestCreate promotionRequestCreate);
    ResponseEntity<?> editStatusPromotion(Long promotionId, Boolean status);
    Boolean isValidPromotion(String name);
    ResponseEntity<?> addPromotionBeLongUser(Long userId, List<Long> listPromotionId);
    Boolean checkValidUsage(Long userId, String promotionName);
    Boolean minusUsage(Long userId, String promotionName);
    Boolean plusUsage(Long userId, String promotionName);
    ResponseEntity<?> getAllPromotionAvailable();
    ResponseEntity<?> getAllPromotionBySellerId(Long sellerId);
    ResponseEntity<?> getPromotionOfUser(Long userId);
}
