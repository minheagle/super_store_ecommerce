package com.shopee.clone.DTO.category.update;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCategoryNoChangeImage {
    private Long id;
    private String content;
}
