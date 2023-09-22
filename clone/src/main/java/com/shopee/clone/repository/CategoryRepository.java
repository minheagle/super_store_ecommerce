package com.shopee.clone.repository;

import com.shopee.clone.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByContent(String content);
    @Query("SELECT c FROM CategoryEntity c WHERE c.left > :left AND c.right < :right")
    List<CategoryEntity> findAllByParentCategory(@Param("left") int left, @Param("right") int right);

    @Query("SELECT c FROM CategoryEntity c WHERE c.right - c.left = 1")
    List<CategoryEntity> findAllLeaf();

    @Query("SELECT c FROM CategoryEntity c WHERE c.left < :left AND c.right > :right")
    Optional<CategoryEntity> findParentCategoryFromChild(@Param("left") int left, @Param("right") int right);

    @Modifying
    @Query("UPDATE CategoryEntity c SET c.left = c.left + :space WHERE c.left > :rightPosition")
    void updateLeft(@Param("rightPosition") int rightPosition, @Param("space") int space);

    @Modifying
    @Query("UPDATE CategoryEntity c SET c.right = c.right + :space WHERE c.right >= :rightPosition")
    void updateRight(@Param("rightPosition") int rightPosition, @Param("space") int space);

    @Modifying
    @Query("UPDATE CategoryEntity c SET c.right = c.right + :space WHERE c.right > :rightPosition")
    void updateAfterMove(@Param("rightPosition") int rightPosition, @Param("space") int space);

    @Modifying
    @Query("UPDATE CategoryEntity c SET c.left = c.left + :space, c.right = c.right + :space WHERE c.left >= :leftPosition AND c.right <= :rightPosition")
    void updateForSubNode(@Param("leftPosition") int leftPosition, @Param("rightPosition") int rightPosition, @Param("space") int space);
}
