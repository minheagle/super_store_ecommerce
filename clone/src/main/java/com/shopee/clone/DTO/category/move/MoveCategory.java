package com.shopee.clone.DTO.category.move;

import com.shopee.clone.DTO.category.CategoryDTO;
import com.shopee.clone.DTO.category.ParentCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoveCategory {
    private ParentCategory oldParentCategory;
    private ParentCategory newParentCategory;
    private CategoryDTO movedCategory;
}
