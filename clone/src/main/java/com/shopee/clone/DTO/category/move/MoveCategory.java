package com.shopee.clone.DTO.category.move;

import com.shopee.clone.DTO.category.CategoryDTO;
import com.shopee.clone.DTO.category.ParentCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoveCategory {
    private Long newParentCategoryId;
    private Long currentCategoryId;
}
