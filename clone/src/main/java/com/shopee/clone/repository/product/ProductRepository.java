package com.shopee.clone.repository.product;

import com.shopee.clone.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long>,
        JpaSpecificationExecutor<ProductEntity> {
    @Query("SELECT p from ProductEntity  p")
    Page<ProductEntity> findProducts(Pageable pageable);
    @Query("SELECT p From ProductEntity p INNER JOIN CategoryEntity c ON p.category.id = c.id WHERE c.id = :categoryId")
    List<ProductEntity> findProductsByCategoryId(@Param("categoryId") Long categoryId);
    @Query("SELECT p From ProductEntity p INNER JOIN SellerEntity  s ON p.seller.id = s.id WHERE s.id = :sellerId")
    List<ProductEntity> findProductsByShopId(@Param("sellerId") Long sellerId);
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.productName) LIKE LOWER(concat('%', :productName, '%'))")
    List<ProductEntity> searchByProductName(@Param("productName") String productName);
}
