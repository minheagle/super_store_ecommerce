package com.shopee.clone.rest_controller.controller_public.productReview;

import com.shopee.clone.service.product_review.ProductReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/public/product-review")
public class ProductReviewPublicRestController {
    private final ProductReviewService productReviewService;

    public ProductReviewPublicRestController(ProductReviewService productReviewService) {
        this.productReviewService = productReviewService;
    }

    @GetMapping("/get-all-by-product/{productId}")
    public ResponseEntity<?> getAllByProduct(@PathVariable Long productId){
        return productReviewService.getALlByProduct(productId);
    }
}
