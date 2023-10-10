package com.shopee.clone.repository.promotion;

import com.shopee.clone.entity.promotion.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity,Long> {
    PromotionEntity findByName(String name);
}
