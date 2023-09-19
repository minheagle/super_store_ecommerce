package com.shopee.clone.repository.product;

import com.shopee.clone.entity.ImageProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageProductRepository extends JpaRepository<ImageProductEntity, Long> {
}
