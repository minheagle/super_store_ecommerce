package com.shopee.clone.entity.product_review;
import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    private String comment;
    @OneToMany(mappedBy = "productReview", fetch = FetchType.EAGER)
    private List<ImageProductReviewEntity> imageProductReview;
}
