package com.shopee.clone.repository.product_review;

import com.shopee.clone.entity.product_review.ImageProductReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageProductReviewRepository extends JpaRepository<ImageProductReviewEntity,Long> {
}
