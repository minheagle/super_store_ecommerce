package com.shopee.clone.DTO.product_review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequest {
    private Long orderId;
    private Long userId;
    private Long productId;
    @Min(1)
    @Max(5)
    private int voteStar;
    private String comment;
    private MultipartFile[] imageProductReviewFile;
}
