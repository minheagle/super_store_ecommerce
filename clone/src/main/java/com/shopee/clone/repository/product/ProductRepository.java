package com.shopee.clone.repository.product;

import com.shopee.clone.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    @Query("SELECT p from ProductEntity  p")
    Page<ProductEntity> findProducts(Pageable pageable);
    @Query("SELECT p From ProductEntity p INNER JOIN CategoryEntity c ON p.category.id = c.id")
    List<ProductEntity> findProductsByCategoryId(Long categoryId);
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.productName) LIKE LOWER(concat('%', :productName, '%'))")
    List<ProductEntity> searchByProductName(@Param("productName") String productName);
}
