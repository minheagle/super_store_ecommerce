package com.shopee.clone.entity.product_review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "image_product_reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageProductReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long imgProductReviewId;

    @Column(name = "image_public_id")
    private String imgPublicId;

    @Column(name = "image_product_review_url")
    private String imgProductReviewURL;

    @ManyToOne
    @JoinColumn(name = "product_review_id")
    private ProductReviewEntity productReview;
    }
