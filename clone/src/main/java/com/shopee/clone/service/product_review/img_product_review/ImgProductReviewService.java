package com.shopee.clone.service.product_review.img_product_review;

import com.shopee.clone.entity.product_review.ImageProductReviewEntity;
import com.shopee.clone.entity.product_review.ProductReviewEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImgProductReviewService {
    void saveAllImageProduct(MultipartFile[] imageFiles, ProductReviewEntity productReview);

    void delete(List<ImageProductReviewEntity> imageProductReview);
}
