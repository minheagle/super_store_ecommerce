package com.shopee.clone.DTO.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDTO {
    private Long id;
    private String content;
    private String imageUrl;
    private Integer left;
    private Integer right;
}
