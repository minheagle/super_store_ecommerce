package com.shopee.clone.entity.promotion;

import com.shopee.clone.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "promotion_belong_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionBeLongUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private PromotionEntity promotion;

    private Integer usageAvailable;
}
