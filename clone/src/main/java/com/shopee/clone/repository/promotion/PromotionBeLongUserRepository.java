package com.shopee.clone.repository.promotion;

import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.promotion.PromotionBeLongUserEntity;
import com.shopee.clone.entity.promotion.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionBeLongUserRepository extends JpaRepository<PromotionBeLongUserEntity,Long> {
    @Query("Select pbl From PromotionBeLongUserEntity pbl Where pbl.user = :user ")
    List<PromotionBeLongUserEntity> getPromotionOfUser(@Param("user") UserEntity user);
    @Query("Select pbl.promotion From PromotionBeLongUserEntity pbl Where pbl.user =:user")
    List<PromotionEntity> getAllPromotionUserHas(@Param("user") UserEntity user);
}
