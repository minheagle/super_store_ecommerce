package com.shopee.clone.rest_controller.controller_public.promotion;

import com.shopee.clone.service.promotion.IPromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/promotions")
public class PromotionPublicRestController {
    private final IPromotionService promotionService;

    public PromotionPublicRestController(IPromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("seller/{id}")
    public ResponseEntity<?> getAllPromotionBySeller(@PathVariable(name = "id") Long sellerId){
        return promotionService.getAllPromotionBySellerId(sellerId);
    }

    @PostMapping("user/{userId}/choose")
    public ResponseEntity<?> addPromotionBeLongUser(@PathVariable Long userId, @RequestBody List<Long> listPromotionId){
        return promotionService.addPromotionBeLongUser(userId, listPromotionId);
    }
}
