package com.shopee.clone.service.category.common;

import com.shopee.clone.entity.CategoryEntity;
import com.shopee.clone.repository.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryComponent {
    private final CategoryRepository categoryRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public CategoryComponent(CategoryRepository categoryRepository,
                             EntityManager entityManager) {
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public CategoryEntity getParent(int left, int right){
        TypedQuery<CategoryEntity> query = entityManager.createQuery(
                "SELECT c FROM CategoryEntity c WHERE c.left < :left AND c.right > :right",
                CategoryEntity.class
        );
        query.setParameter("left", left);
        query.setParameter("right", right);
        query.setMaxResults(1);

        List<CategoryEntity> result = query.getResultList();
        if (result.isEmpty()){
            throw new RuntimeException("Not found !");
        }
        return result.get(0);
    }

    @Transactional
    public CategoryEntity getById(long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Transactional
    public void createSpace(int rightPositionCreateSpace, int space){
        categoryRepository.updateLeft(rightPositionCreateSpace, space);
        categoryRepository.updateRight(rightPositionCreateSpace, space);
    }

    @Transactional
    public void updatePositionForSubNode(int leftPosition, int rightPosition, int space){
        categoryRepository.updateForSubNode(leftPosition, rightPosition, space);
    }


    @Transactional
    public void updateAllPosition(int rightPosition, int space){
        categoryRepository.updateLeft(rightPosition, space);
        categoryRepository.updateAfterMove(rightPosition, space);
    }
}
