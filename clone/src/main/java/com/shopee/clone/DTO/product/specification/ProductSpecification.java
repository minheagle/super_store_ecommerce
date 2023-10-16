package com.shopee.clone.DTO.product.specification;

import com.shopee.clone.entity.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductSpecification {
    public static Specification<ProductEntity> searchByName(String productName){
        return (root, query, criteriaBuilder) -> {
            if(productName.isBlank()){
                //Trả về biểu thức and trống
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + productName.toLowerCase() + "%");
        };
    }
    public static Specification<ProductEntity> filterByPrice(Double minPrice, Double maxPrice){
        return (root, query, criteriaBuilder) -> {
          if(minPrice == null && maxPrice == null){
              return criteriaBuilder.conjunction();
          }
          if(minPrice == null){
              return criteriaBuilder.lessThanOrEqualTo(root.get("maxPrice"),maxPrice);
          }
          if(maxPrice == null){
              return criteriaBuilder.greaterThanOrEqualTo(root.get("minPrice"),minPrice);
          }
          return criteriaBuilder.between(root.get("minPrice"),minPrice, maxPrice);
        };
    }
    public static Specification<ProductEntity> filterByCategory(Long categoryId){
        return (root, query, criteriaBuilder) -> {
          if(categoryId == null){
            return criteriaBuilder.conjunction();
          }
          return criteriaBuilder.equal(root.get("category").get("id"),categoryId);
        };
    }
}
