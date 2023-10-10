package com.shopee.clone.repository.promotion;

import com.shopee.clone.entity.promotion.PromotionBeLongUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionBeLongUserRepository extends JpaRepository<PromotionBeLongUserEntity,Long> {
}
