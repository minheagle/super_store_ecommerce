package com.shopee.clone.rest_controller.product_review;

import com.shopee.clone.DTO.fieldErrorDTO.FieldError;
import com.shopee.clone.DTO.product_review.ProductReviewRequest;
import com.shopee.clone.DTO.product_review.ProductReviewUpdateRequest;
import com.shopee.clone.service.product_review.ProductReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product-review")
public class ProductReviewRestController {
    @Autowired
    private ProductReviewService productReviewService;
    @PostMapping("/create")
    public ResponseEntity<?> createRating(@Valid ProductReviewRequest productReviewRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            FieldError.throwErrorHandler(bindingResult);
        }
        return productReviewService.createRating(productReviewRequest);
    }
    @GetMapping("/check-rating")
    public boolean checkRating(@RequestParam Long userId, Long productId){
        return productReviewService.checkRating(userId,productId);
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateRating(@Valid ProductReviewUpdateRequest productReviewUpdateRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            FieldError.throwErrorHandler(bindingResult);
        }
        return productReviewService.updateRating(productReviewUpdateRequest);
    }
    @PostMapping("/delete/{productReviewId}")
    public ResponseEntity<?> deleteProductReview(@PathVariable Long productReviewId){
        return productReviewService.deleteProductReview(productReviewId);
    }
    @GetMapping("/{pReview}")
    public ResponseEntity<?> getRating(@PathVariable Long pReview){
        return productReviewService.getRating(pReview);
    }
    @GetMapping("/get-all-by-product/{productId}")
    public ResponseEntity<?> getAllByProduct(@PathVariable Long productId){
        return productReviewService.getALlByProduct(productId);
    }
}
