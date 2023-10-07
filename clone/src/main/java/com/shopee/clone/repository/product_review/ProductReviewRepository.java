package com.shopee.clone.repository.product_review;

import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.product_review.ProductReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReviewEntity,Long> {
    List<ProductReviewEntity> findByProduct(ProductEntity productEntity);

    List<ProductReviewEntity> findByProduct_ProductId(Long productId);
}
