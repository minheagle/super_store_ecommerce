package com.shopee.clone.repository.comment;

import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findByProduct(ProductEntity product);

    @Query("SELECT c FROM CommentEntity c WHERE c.product = :product AND c.parentComment IS NULL")
    List<CommentEntity> findRootCommentsByProduct(ProductEntity product);

    List<CommentEntity> findRootCommentByProduct(ProductEntity product);

    List<CommentEntity> findByProductAndParentComment(ProductEntity product, CommentEntity commentEntity);

    List<CommentEntity> findByProductAndParentComment_Id(ProductEntity product, long l);
}
