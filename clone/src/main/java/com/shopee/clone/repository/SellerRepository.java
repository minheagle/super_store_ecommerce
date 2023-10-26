package com.shopee.clone.repository;

import com.shopee.clone.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {
    boolean existsByStoreName(String storeName);
    Optional<SellerEntity> findByUserId(Long userId);
    Optional<SellerEntity> findByStoreName(String storeName);
}
