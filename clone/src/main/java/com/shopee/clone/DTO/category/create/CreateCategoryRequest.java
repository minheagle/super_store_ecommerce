package com.shopee.clone.DTO.category.create;

import com.shopee.clone.DTO.category.ParentCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCategoryRequest {
    private CreateCategory newCategory;
    private ParentCategory parentCategory;
}
