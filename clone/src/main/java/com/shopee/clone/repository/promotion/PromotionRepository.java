package com.shopee.clone.repository.promotion;

import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.promotion.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity,Long> {
    PromotionEntity findByName(String name);
    @Query("Select pn From PromotionEntity pn Where pn.isActive=True And pn.endDate >= :currentDate")
    List<PromotionEntity> findAllByIsActiveAvailable(@Param("currentDate") LocalDate currentDate);
    @Query("Select pn From PromotionEntity pn Where pn.seller_created=:seller")
    List<PromotionEntity> findAllBySeller_created(@Param("seller") SellerEntity seller);
}
