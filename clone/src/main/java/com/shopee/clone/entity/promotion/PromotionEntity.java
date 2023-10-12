package com.shopee.clone.entity.promotion;

import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.payment.EDiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promotions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long promotionId;
    private String name;
    private String description;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller_created;
    private LocalDate createAt;

    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private EDiscountType discountType;
    private Integer discountValue;
    private Integer minPurchaseAmount;
    private Boolean isActive;

    private Integer usageLimitPerUser;

    @OneToMany(mappedBy = "promotion")
    private Set<PromotionBeLongUserEntity> userEntities = new HashSet<>();
}
