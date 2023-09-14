package com.shopee.clone.repository;

import com.shopee.clone.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("SELECT c FROM CategoryEntity c WHERE c.left > :left AND c.right < :right")
    List<CategoryEntity> findAllByParentCategory(@Param("left") int left, @Param("right") int right);

    @Query("SELECT c FROM CategoryEntity c WHERE c.right - c.left = 1")
    List<CategoryEntity> findAllLeaf();

    @Modifying
    @Query("UPDATE CategoryEntity c SET c.left = c.left + :space WHERE c.left > :parentRight")
    void updateLeft(@Param("parentRight") int parentRight, @Param("space") int space);

    @Modifying
    @Query("UPDATE CategoryEntity c SET c.right = c.right + :space WHERE c.right >= :parentRight")
    void updateRight(@Param("parentRight") int parentRight, @Param("space") int space);
}
