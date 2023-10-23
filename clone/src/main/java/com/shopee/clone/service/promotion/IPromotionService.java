package com.shopee.clone.service.promotion;

import com.shopee.clone.DTO.promotion.request.PromotionRequestCreate;
import com.shopee.clone.DTO.promotion.response.PromotionResponse;
import com.shopee.clone.DTO.promotion.response.TypeDiscountResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPromotionService {
    ResponseEntity<?> createPromotion(PromotionRequestCreate promotionRequestCreate);
    ResponseEntity<?> editStatusPromotion(Long promotionId, Boolean status);
    Boolean isValidPromotion(Long promotionId, Double purchasedAmount, Long seller_id);
    ResponseEntity<?> addPromotionBeLongUser(Long userId, List<Long> listPromotionId);
    //Call To this Service is Enough
    Boolean checkValidUsage(Long userId, Long promotionId, Double purchasedAmount, Long seller_id);
    Boolean minusUsage(Long userId, Long promotionId, Double purchasedAmount, Long seller_id);
    Boolean plusUsage(Long userId, Long promotionId, Double purchasedAmount, Long seller_id);
    ResponseEntity<?> getAllPromotionAvailable(Long userId);
    ResponseEntity<?> getAllPromotionBySellerId(Long sellerId);
    ResponseEntity<?> getPromotionOfUser(Long userId);
    TypeDiscountResponse getTypeDiscount(Long promotionId);
}
