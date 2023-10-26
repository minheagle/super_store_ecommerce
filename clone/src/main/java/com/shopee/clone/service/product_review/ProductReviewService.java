package com.shopee.clone.service.product_review;

import com.shopee.clone.DTO.product_review.ProductReviewRequest;
import com.shopee.clone.DTO.product_review.ProductReviewUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface ProductReviewService {
    ResponseEntity<?> createRating(ProductReviewRequest productReviewRequest);

    ResponseEntity<?> getRating(Long pReview);

    ResponseEntity<?> getALlByProduct(Long productId);

    ResponseEntity<?> deleteProductReview(Long productReviewId);

    ResponseEntity<?> updateRating(ProductReviewUpdateRequest productReviewUpdateRequest);

    boolean checkRating(Long userId, Long productId);
}